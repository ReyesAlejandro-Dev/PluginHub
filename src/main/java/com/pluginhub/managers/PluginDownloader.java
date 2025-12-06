package com.pluginhub.managers;

import com.pluginhub.models.PluginInfo;
import com.pluginhub.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Gestor de descarga e instalación de plugins
 */
public final class PluginDownloader {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, PluginInfo> pluginCache;
    private final ExecutorService executorService;
    private final Map<String, Long> downloadCache;

    private static final int BUFFER_SIZE = 8192;
    private static final String USER_AGENT = "PluginHub/1.0";

    public PluginDownloader(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.pluginCache = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(3);
        this.downloadCache = new ConcurrentHashMap<>();
        
        initializePluginDatabase();
    }

    /**
     * Inicializa la base de datos de plugins disponibles
     */
    private void initializePluginDatabase() {
        // Plugins populares de SpigotMC
        addPlugin("essentialsx", new PluginInfo(
                "EssentialsX",
                "2.20.1",
                "https://ci.ender.zone/job/EssentialsX/lastSuccessfulBuild/artifact/Essentials/target/EssentialsX-2.20.1.jar",
                "https://www.spigotmc.org/resources/essentialsx.9089/",
                "Essential commands and utilities for Minecraft servers"
        ));

        addPlugin("luckperms", new PluginInfo(
                "LuckPerms",
                "5.4.121",
                "https://download.luckperms.net/1556/bukkit/loader/LuckPerms-Bukkit-5.4.121.jar",
                "https://www.spigotmc.org/resources/luckperms.28140/",
                "Advanced permissions management system"
        ));

        addPlugin("worldedit", new PluginInfo(
                "WorldEdit",
                "7.2.15",
                "https://dev.bukkit.org/projects/worldedit/files/latest",
                "https://dev.bukkit.org/projects/worldedit",
                "In-game world editing and building tool"
        ));

        addPlugin("vault", new PluginInfo(
                "Vault",
                "1.7.3",
                "https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar",
                "https://www.spigotmc.org/resources/vault.34315/",
                "Permission, chat and economy API abstraction layer"
        ));

        addPlugin("protocollib", new PluginInfo(
                "ProtocolLib",
                "5.1.0",
                "https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/build/libs/ProtocolLib.jar",
                "https://www.spigotmc.org/resources/protocollib.1997/",
                "Protocol packet manipulation library"
        ));

        addPlugin("plotsquared", new PluginInfo(
                "PlotSquared",
                "7.3.8",
                "https://github.com/IntellectualSites/PlotSquared/releases/latest",
                "https://www.spigotmc.org/resources/plotsquared-v6.77506/",
                "Advanced plot world management system"
        ));

        addPlugin("coreprotect", new PluginInfo(
                "CoreProtect",
                "22.2",
                "https://www.spigotmc.org/resources/coreprotect.8631/download",
                "https://www.spigotmc.org/resources/coreprotect.8631/",
                "Fast and efficient block logging and rollback tool"
        ));

        addPlugin("citizens", new PluginInfo(
                "Citizens",
                "2.0.33",
                "https://ci.citizensnpcs.co/job/Citizens2/lastSuccessfulBuild/artifact/dist/target/Citizens-2.0.33-b3290.jar",
                "https://www.spigotmc.org/resources/citizens.13811/",
                "Advanced NPC creation and management"
        ));
    }

    /**
     * Añade un plugin al caché
     */
    private void addPlugin(String key, PluginInfo info) {
        pluginCache.put(key.toLowerCase(), info);
    }

    /**
     * Busca plugins por nombre o descripción
     */
    public List<PluginInfo> searchPlugins(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String lowerQuery = query.toLowerCase().trim();

        return pluginCache.entrySet().stream()
                .filter(entry -> matchesQuery(entry, lowerQuery))
                .map(Map.Entry::getValue)
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Verifica si un plugin coincide con la búsqueda
     */
    private boolean matchesQuery(Map.Entry<String, PluginInfo> entry, String query) {
        PluginInfo info = entry.getValue();
        return entry.getKey().contains(query) ||
               info.getName().toLowerCase().contains(query) ||
               info.getDescription().toLowerCase().contains(query);
    }

    /**
     * Obtiene información de un plugin específico
     */
    public PluginInfo getPluginInfo(String pluginName) {
        if (pluginName == null || pluginName.trim().isEmpty()) {
            return null;
        }
        return pluginCache.get(pluginName.toLowerCase().trim());
    }

    /**
     * Descarga un plugin desde su URL
     */
    public CompletableFuture<Boolean> downloadPluginAsync(String pluginName, File destination) {
        return CompletableFuture.supplyAsync(() -> {
            PluginInfo info = getPluginInfo(pluginName);
            if (info == null) {
                plugin.getLogger().warning("Plugin no encontrado: " + pluginName);
                return false;
            }

            return downloadWithRetry(info, destination);
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
                plugin.getLogger().info(String.format("⏳ Descargando %s v%s (intento %d/%d)...", 
                        info.getName(), info.getVersion(), attempt, maxRetries));

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
     * Realiza la descarga del archivo
     */
    private boolean performDownload(String urlString, File destination, int timeout) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setInstanceFollowRedirects(true);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            // Crear directorio padre si no existe
            File parentDir = destination.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Descargar archivo
            try (InputStream in = new BufferedInputStream(connection.getInputStream());
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
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Instala un plugin en la carpeta de plugins del servidor
     */
    public CompletableFuture<Boolean> installPluginAsync(String pluginName) {
        return CompletableFuture.supplyAsync(() -> {
            PluginInfo info = getPluginInfo(pluginName);
            if (info == null) {
                return false;
            }

            File pluginsFolder = plugin.getServer().getPluginsFolder();
            File destination = new File(pluginsFolder, info.getName() + ".jar");

            try {
                if (performDownload(info.getDownloadUrl(), destination, configManager.getDownloadTimeout())) {
                    plugin.getLogger().info("✓ " + info.getName() + " instalado correctamente");
                    downloadCache.put(pluginName.toLowerCase(), System.currentTimeMillis());
                    return true;
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error instalando " + pluginName, e);
            }

            return false;
        }, executorService);
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
     * Obtiene todos los plugins disponibles
     */
    public Collection<PluginInfo> getAllAvailablePlugins() {
        return Collections.unmodifiableCollection(pluginCache.values());
    }

    /**
     * Limpia recursos al desactivar el plugin
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
