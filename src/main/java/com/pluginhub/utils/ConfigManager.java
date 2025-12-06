package com.pluginhub.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Gestor de configuración del plugin
 */
public final class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    // Claves de configuración
    private static final String KEY_DOWNLOAD_TIMEOUT = "download.timeout";
    private static final String KEY_DOWNLOAD_RETRIES = "download.retries";
    private static final String KEY_AUTO_UPDATE = "auto-update.enabled";
    private static final String KEY_CACHE_ENABLED = "cache.enabled";
    private static final String KEY_CACHE_DURATION = "cache.duration-minutes";

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Carga la configuración del plugin
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    /**
     * Recarga la configuración
     */
    public void reloadConfiguration() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    // ═══════════════════════════════════════════════════════
    //  Getters de configuración
    // ═══════════════════════════════════════════════════════

    public int getDownloadTimeout() {
        return config.getInt(KEY_DOWNLOAD_TIMEOUT, 30000);
    }

    public int getDownloadRetries() {
        return config.getInt(KEY_DOWNLOAD_RETRIES, 3);
    }

    public boolean isAutoUpdateEnabled() {
        return config.getBoolean(KEY_AUTO_UPDATE, false);
    }

    public boolean isCacheEnabled() {
        return config.getBoolean(KEY_CACHE_ENABLED, true);
    }

    public int getCacheDuration() {
        return config.getInt(KEY_CACHE_DURATION, 60);
    }

    public List<String> getTrustedSources() {
        return config.getStringList("trusted-sources");
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
