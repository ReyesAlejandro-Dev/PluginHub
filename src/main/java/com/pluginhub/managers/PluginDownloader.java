package com.pluginhub.managers;

import com.pluginhub.api.*;
import com.pluginhub.models.PluginInfo;
import com.pluginhub.utils.ConfigManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Gestor avanzado de descarga e instalación de plugins
 * Integra múltiples fuentes: Spigot, Modrinth, Hangar, Bukkit
 */
public final class PluginDownloader {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final ExecutorService executorService;
    private final Map<String, PluginInfo> searchCache;
    private final Map<String, Long> cacheTimestamps;
    
    // APIs de diferentes fuentes
    private final SpigotAPI spigotAPI;
    private final ModrinthAPI modrinthAPI;
    private final HangarAPI hangarAPI;
    private final BukkitAPI bukkitAPI;
    private final OkHttpClient httpClient;

    private static final int BUFFER_SIZE = 8192;
    private static final String USER_AGENT = "PluginHub/2.0";
    private static final int SEARCH_LIMIT = 10;

    public PluginDownloader(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        
        // Usar configuración para thread pool
        int threadPoolSize = configManager.getThreadPoolSize();
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.searchCache = new ConcurrentHashMap<>();
        this.cacheTimestamps = new ConcurrentHashMap<>();
        
        // Inicializar APIs según configuración
        this.spigotAPI = configManager.isSpigotEnabled() ? new SpigotAPI(plugin.getLogger()) : null;
        this.modrinthAPI = configManager.isModrinthEnabled() ? new ModrinthAPI(plugin.getLogger()) : null;
        this.hangarAPI = configManager.isHangarEnabled() ? new HangarAPI(plugin.getLogger()) : null;
        this.bukkitAPI = configManager.isBukkitEnabled() ? new BukkitAPI(plugin.getLogger()) : null;
        
        // Configurar HTTP client con opciones personalizadas
        int timeout = configManager.getHttpTimeout();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .followRedirects(configManager.followRedirects())
                .build();
        
        plugin.getLogger().info("PluginDownloader inicializado con múltiples fuentes");
        if (configManager.isDebugEnabled()) {
            plugin.getLogger().info("Debug: Thread pool size = " + threadPoolSize);
            plugin.getLogger().info("Debug: Spigot = " + configManager.isSpigotEnabled());
            plugin.getLogger().info("Debug: Modrinth = " + configManager.isModrinthEnabled());
            plugin.getLogger().info("Debug: Hangar = " + configManager.isHangarEnabled());
            plugin.getLogger().info("Debug: Bukkit = " + configManager.isBukkitEnabled());
        }
    }

    /**
     * Busca plugins en todas las fuentes disponibles
     */
    public CompletableFuture<List<PluginInfo>> searchPluginsAsync(String query) {
        // Verificar caché
        if (configManager.isCacheEnabled()) {
            String cacheKey = query.toLowerCase();
            if (searchCache.containsKey(cacheKey)) {
                Long timestamp = cacheTimestamps.get(cacheKey);
                long cacheAge = System.currentTimeMillis() - timestamp;
                long maxAge = configManager.getCacheDuration() * 60 * 1000L;
                
                if (cacheAge < maxAge) {
                    plugin.getLogger().info("Usando resultados en caché para: " + query);
                    return CompletableFuture.completedFuture(
                        searchCache.values().stream()
                            .filter(p -> matchesQuery(p, query))
                            .collect(Collectors.toList())
                    );
                }
            }
        }

        return CompletableFuture.supplyAsync(() -> {
            List<PluginInfo> allResults = new ArrayList<>();
            List<CompletableFuture<List<PluginInfo>>> futures = new ArrayList<>();

            int searchLimit = configManager.getMaxSearchResults();
            
            // Buscar en Spigot (si está habilitado)
            if (spigotAPI != null) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        plugin.getLogger().info("Buscando en SpigotMC...");
                        return spigotAPI.searchPlugins(query, searchLimit);
                    } catch (IOException e) {
                        plugin.getLogger().warning("Error buscando en Spigot: " + e.getMessage());
                        return Collections.emptyList();
                    }
                }, executorService));
            }

            // Buscar en Modrinth (si está habilitado)
            if (modrinthAPI != null) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        plugin.getLogger().info("Buscando en Modrinth...");
                        return modrinthAPI.searchPlugins(query, searchLimit);
                    } catch (IOException e) {
                        plugin.getLogger().warning("Error buscando en Modrinth: " + e.getMessage());
                        return Collections.emptyList();
                    }
                }, executorService));
            }

            // Buscar en Hangar (si está habilitado)
            if (hangarAPI != null) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        plugin.getLogger().info("Buscando en Hangar...");
                        return hangarAPI.searchPlugins(query, searchLimit);
                    } catch (IOException e) {
                        plugin.getLogger().warning("Error buscando en Hangar: " + e.getMessage());
                        return Collections.emptyList();
                    }
                }, executorService));
            }

            // Buscar en BukkitDev (si está habilitado)
            if (bukkitAPI != null) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        plugin.getLogger().info("Buscando en BukkitDev...");
                        return bukkitAPI.searchPlugins(query, searchLimit);
                    } catch (IOException e) {
                        // BukkitDev frecuentemente bloquea requests, no mostrar warning
                        plugin.getLogger().fine("BukkitDev no disponible: " + e.getMessage());
                        return Collections.emptyList();
                    }
                }, executorService));
            }

            // Esperar a que todas las búsquedas terminen
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // Combinar resultados
            for (CompletableFuture<List<PluginInfo>> future : futures) {
                try {
                    allResults.addAll(future.get());
                } catch (Exception e) {
                    plugin.getLogger().warning("Error obteniendo resultados: " + e.getMessage());
                }
            }

            // Eliminar duplicados (mismo nombre)
            Map<String, PluginInfo> uniqueResults = new LinkedHashMap<>();
            for (PluginInfo info : allResults) {
                String key = info.getName().toLowerCase();
                if (!uniqueResults.containsKey(key)) {
                    uniqueResults.put(key, info);
                }
            }

            List<PluginInfo> finalResults = new ArrayList<>(uniqueResults.values());

            // Ordenar por descargas (más popular primero)
            finalResults.sort((a, b) -> Integer.compare(b.getDownloads(), a.getDownloads()));

            // Guardar en caché
            if (configManager.isCacheEnabled()) {
                String cacheKey = query.toLowerCase();
                for (PluginInfo info : finalResults) {
                    searchCache.put(info.getName().toLowerCase(), info);
                }
                cacheTimestamps.put(cacheKey, System.currentTimeMillis());
            }

            plugin.getLogger().info("Búsqueda completada: " + finalResults.size() + " resultados únicos");
            return finalResults;

        }, executorService);
    }

    /**
     * Verifica si un plugin coincide con la búsqueda
     */
    private boolean matchesQuery(PluginInfo info, String query) {
        String lowerQuery = query.toLowerCase();
        return info.getName().toLowerCase().contains(lowerQuery) ||
               info.getDescription().toLowerCase().contains(lowerQuery) ||
               info.getAuthor().toLowerCase().contains(lowerQuery);
    }

    /**
     * Obtiene información de un plugin por nombre (busca en caché primero)
     */
    public PluginInfo getPluginInfo(String pluginName) {
        if (pluginName == null || pluginName.trim().isEmpty()) {
            return null;
        }
        return searchCache.get(pluginName.toLowerCase().trim());
    }

    /**
     * Descarga un plugin desde su URL
     */
    public CompletableFuture<Boolean> downloadPluginAsync(PluginInfo pluginInfo, File destination) {
        return CompletableFuture.supplyAsync(() -> {
            if (pluginInfo == null || pluginInfo.getDownloadUrl() == null) {
                plugin.getLogger().warning("URL de descarga no disponible");
                return false;
            }

            return downloadWithRetry(pluginInfo, destination);
        }, executorService);
    }

    /**
     * Descarga con reintentos automáticos
     */
    private boolean downloadWithRetry(PluginInfo info, File destination) {
        int maxRetries = configManager.getDownloadRetries();
        int timeout = configManager.getDownloadTimeout();

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                plugin.getLogger().info(String.format("⏳ Descargando %s v%s desde %s (intento %d/%d)...", 
                        info.getName(), info.getVersion(), info.getSource().getDisplayName(), attempt, maxRetries));

                if (performDownload(info.getDownloadUrl(), destination, timeout)) {
                    plugin.getLogger().info("✓ " + info.getName() + " descargado correctamente");
                    return true;
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, 
                        String.format("Error en intento %d/%d: %s", attempt, maxRetries, e.getMessage()));
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(2000 * attempt); // Backoff exponencial
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }

        plugin.getLogger().severe("✗ Falló la descarga de " + info.getName() + " después de " + maxRetries + " intentos");
        return false;
    }

    /**
     * Realiza la descarga del archivo usando OkHttp
     */
    private boolean performDownload(String urlString, File destination, int timeout) throws IOException {
        Request request = new Request.Builder()
                .url(urlString)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP error code: " + response.code());
            }

            // Crear directorio padre si no existe
            File parentDir = destination.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Descargar archivo
            try (InputStream in = new BufferedInputStream(response.body().byteStream());
                 FileOutputStream out = new FileOutputStream(destination)) {
                
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                long totalBytes = 0;
                
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                }

                plugin.getLogger().info(String.format("Descargados %.2f MB", totalBytes / 1024.0 / 1024.0));
                return true;
            }
        }
    }

    /**
     * Instala un plugin en la carpeta de plugins del servidor
     */
    public CompletableFuture<Boolean> installPluginAsync(PluginInfo pluginInfo) {
        return CompletableFuture.supplyAsync(() -> {
            if (pluginInfo == null) {
                return false;
            }

            File pluginsFolder = plugin.getServer().getPluginsFolder();
            String fileName = sanitizeFileName(pluginInfo.getName()) + ".jar";
            File destination = new File(pluginsFolder, fileName);

            try {
                if (performDownload(pluginInfo.getDownloadUrl(), destination, configManager.getDownloadTimeout())) {
                    plugin.getLogger().info("✓ " + pluginInfo.getName() + " instalado correctamente");
                    return true;
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error instalando " + pluginInfo.getName(), e);
            }

            return false;
        }, executorService);
    }

    /**
     * Sanitiza el nombre del archivo
     */
    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    /**
     * Obtiene la lista de plugins instalados en el servidor
     */
    public List<String> getInstalledPlugins() {
        File pluginsFolder = plugin.getServer().getPluginsFolder();
        
        if (!pluginsFolder.exists() || !pluginsFolder.isDirectory()) {
            return Collections.emptyList();
        }

        File[] files = pluginsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
                .map(File::getName)
                .map(name -> name.replace(".jar", ""))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los plugins del caché
     */
    public Collection<PluginInfo> getAllCachedPlugins() {
        return Collections.unmodifiableCollection(searchCache.values());
    }

    /**
     * Limpia el caché de búsqueda
     */
    public void clearCache() {
        searchCache.clear();
        cacheTimestamps.clear();
        plugin.getLogger().info("Caché de búsqueda limpiado");
    }

    /**
     * Obtiene estadísticas del caché
     */
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cached_plugins", searchCache.size());
        stats.put("cache_enabled", configManager.isCacheEnabled());
        stats.put("cache_duration_minutes", configManager.getCacheDuration());
        return stats;
    }

    /**
     * Limpia recursos al desactivar el plugin
     */
    public void shutdown() {
        plugin.getLogger().info("Cerrando PluginDownloader...");
        
        // Cerrar APIs (solo si están inicializadas)
        if (spigotAPI != null) spigotAPI.shutdown();
        if (modrinthAPI != null) modrinthAPI.shutdown();
        if (hangarAPI != null) hangarAPI.shutdown();
        if (bukkitAPI != null) bukkitAPI.shutdown();
        
        // Cerrar HTTP client
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
        
        // Cerrar executor service
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        plugin.getLogger().info("PluginDownloader cerrado correctamente");
    }
}
