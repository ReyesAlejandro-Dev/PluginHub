package com.pluginhub.managers;

import org.bukkit.plugin.java.JavaPlugin;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PluginDownloader {

    private final JavaPlugin plugin;
    private final Map<String, PluginInfo> pluginCache = new HashMap<>();

    public PluginDownloader(JavaPlugin plugin) {
        this.plugin = plugin;
        initializeCache();
    }

    // Base de datos simulada de plugins populares (en v2.0 usarás APIs reales)
    private void initializeCache() {
        // SpigotMC popular plugins
        pluginCache.put("essentialsx", new PluginInfo(
                "EssentialsX",
                "2.20.1",
                "https://ci.ender.zone/job/EssentialsX/lastSuccessfulBuild/artifact/Essentials/target/EssentialsX-2.20.1.jar",
                "https://www.spigotmc.org/resources/essentialsx.9089/",
                "Essential commands and utilities"
        ));

        pluginCache.put("luckperms", new PluginInfo(
                "LuckPerms",
                "5.4.121",
                "https://download.luckperms.net/latest/luckperms-bukkit.jar",
                "https://www.spigotmc.org/resources/luckperms.34309/",
                "Advanced permissions management"
        ));

        pluginCache.put("worldedit", new PluginInfo(
                "WorldEdit",
                "7.2.15",
                "https://builds.enginehub.org/job/WorldEdit/lastSuccessfulBuild/artifact/worldedit-bukkit-7.2.15-dist.jar",
                "https://dev.bukkit.org/projects/worldedit",
                "World editing and building tool"
        ));

        pluginCache.put("vault", new PluginInfo(
                "Vault",
                "1.7.3",
                "https://ci.enginehub.org/job/Vault/lastSuccessfulBuild/artifact/target/Vault.jar",
                "https://www.spigotmc.org/resources/vault.41918/",
                "Permission, chat and economy API"
        ));

        pluginCache.put("protocollib", new PluginInfo(
                "ProtocolLib",
                "5.1.0",
                "https://ci.dmulloy2.net/job/ProtocolLib/lastSuccessfulBuild/artifact/target/ProtocolLib.jar",
                "https://www.spigotmc.org/resources/protocollib.1997/",
                "Protocol packet manipulation"
        ));

        pluginCache.put("plotsquared", new PluginInfo(
                "PlotSquared",
                "7.3.8",
                "https://ci.intellectualsites.com/job/PlotSquared-Bukkit/lastSuccessfulBuild/artifact/target/PlotSquared-Bukkit-7.3.8.jar",
                "https://www.spigotmc.org/resources/plotsquared.1177/",
                "Plotworld and claiming system"
        ));
    }

    // Buscar plugin en la base de datos local
    public List<PluginInfo> searchPlugins(String query) {
        List<PluginInfo> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Map.Entry<String, PluginInfo> entry : pluginCache.entrySet()) {
            if (entry.getValue().getName().toLowerCase().contains(lowerQuery) ||
                    entry.getKey().toLowerCase().contains(lowerQuery) ||
                    entry.getValue().getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(entry.getValue());
            }
        }

        return results;
    }

    // Obtener información específica de un plugin
    public PluginInfo getPluginInfo(String pluginName) {
        return pluginCache.get(pluginName.toLowerCase());
    }

    // Descargar plugin
    public boolean downloadPlugin(String pluginName, File destination) {
        PluginInfo info = getPluginInfo(pluginName);
        if (info == null) {
            return false;
        }

        try {
            plugin.getLogger().info("⏳ Descargando " + info.getName() + " v" + info.getVersion() + "...");
            
            URL url = new URL(info.getDownloadUrl());
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            try (InputStream in = connection.getInputStream();
                 FileOutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            plugin.getLogger().info("✓ " + info.getName() + " descargado correctamente");
            return true;
        } catch (IOException e) {
            plugin.getLogger().warning("✗ Error descargando " + pluginName + ": " + e.getMessage());
            return false;
        }
    }

    // Instalar plugin en la carpeta de plugins
    public boolean installPlugin(String pluginName) {
        PluginInfo info = getPluginInfo(pluginName);
        if (info == null) {
            return false;
        }

        File pluginsFolder = new File(plugin.getServer().getPluginsFolder(), pluginName + ".jar");
        
        if (downloadPlugin(pluginName, pluginsFolder)) {
            plugin.getLogger().info("✓ " + info.getName() + " instalado. Reinicia el servidor para activar.");
            return true;
        }
        return false;
    }

    // Obtener lista de plugins instalados
    public List<String> getInstalledPlugins() {
        File pluginsFolder = plugin.getServer().getPluginsFolder();
        List<String> plugins = new ArrayList<>();

        if (pluginsFolder.exists() && pluginsFolder.isDirectory()) {
            File[] files = pluginsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
            if (files != null) {
                for (File file : files) {
                    plugins.add(file.getName().replace(".jar", ""));
                }
            }
        }

        return plugins;
    }

    // Clase interna para almacenar información del plugin
    public static class PluginInfo {
        private final String name;
        private final String version;
        private final String downloadUrl;
        private final String sourceUrl;
        private final String description;

        public PluginInfo(String name, String version, String downloadUrl, String sourceUrl, String description) {
            this.name = name;
            this.version = version;
            this.downloadUrl = downloadUrl;
            this.sourceUrl = sourceUrl;
            this.description = description;
        }

        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getDownloadUrl() { return downloadUrl; }
        public String getSourceUrl() { return sourceUrl; }
        public String getDescription() { return description; }
    }
}
