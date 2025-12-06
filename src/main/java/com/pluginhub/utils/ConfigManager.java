package com.pluginhub.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Gestor de configuración altamente personalizable
 */
public final class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

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
    //  CONFIGURACIÓN GENERAL
    // ═══════════════════════════════════════════════════════

    public String getLanguage() {
        return config.getString("general.language", "es");
    }

    public String getPrefix() {
        return config.getString("general.prefix", "&6[&ePluginHub&6]&r");
    }

    public boolean showBanner() {
        return config.getBoolean("general.show-banner", true);
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("general.debug", false);
    }

    public boolean checkUpdates() {
        return config.getBoolean("general.check-updates", true);
    }

    // ═══════════════════════════════════════════════════════
    //  CONFIGURACIÓN DE BÚSQUEDA
    // ═══════════════════════════════════════════════════════

    public int getMaxSearchResults() {
        return config.getInt("search.max-results", 10);
    }

    public int getSearchTimeout() {
        return config.getInt("search.timeout", 10);
    }

    public boolean showDetailedInfo() {
        return config.getBoolean("search.show-detailed-info", true);
    }

    public boolean sortByDownloads() {
        return config.getBoolean("search.sort-by-downloads", true);
    }

    public boolean isSpigotEnabled() {
        return config.getBoolean("search.sources.spigot", true);
    }

    public boolean isModrinthEnabled() {
        return config.getBoolean("search.sources.modrinth", true);
    }

    public boolean isHangarEnabled() {
        return config.getBoolean("search.sources.hangar", true);
    }

    public boolean isBukkitEnabled() {
        return config.getBoolean("search.sources.bukkit", true);
    }

    // ═══════════════════════════════════════════════════════
    //  CONFIGURACIÓN DE DESCARGAS
    // ═══════════════════════════════════════════════════════

    public int getDownloadTimeout() {
        return config.getInt("download.timeout", 30000);
    }

    public int getDownloadRetries() {
        return config.getInt("download.retries", 3);
    }

    public int getRetryDelay() {
        return config.getInt("download.retry-delay", 2000);
    }

    public boolean useExponentialBackoff() {
        return config.getBoolean("download.exponential-backoff", true);
    }

    public boolean showProgress() {
        return config.getBoolean("download.show-progress", true);
    }

    public boolean verifyIntegrity() {
        return config.getBoolean("download.verify-integrity", false);
    }

    public int getMaxFileSize() {
        return config.getInt("download.max-file-size", 50);
    }

    // ═══════════════════════════════════════════════════════
    //  CONFIGURACIÓN DE CACHÉ
    // ═══════════════════════════════════════════════════════

    public boolean isCacheEnabled() {
        return config.getBoolean("cache.enabled", true);
    }

    public int getCacheDuration() {
        return config.getInt("cache.duration-minutes", 60);
    }

    public boolean clearCacheOnRestart() {
        return config.getBoolean("cache.clear-on-restart", false);
    }

    public int getMaxCacheSize() {
        return config.getInt("cache.max-size", 1000);
    }

    public boolean isPersistentCache() {
        return config.getBoolean("cache.persistent", false);
    }

    // ═══════════════════════════════════════════════════════
    //  ACTUALIZACIONES AUTOMÁTICAS
    // ═══════════════════════════════════════════════════════

    public boolean isAutoUpdateEnabled() {
        return config.getBoolean("auto-update.enabled", false);
    }

    public int getUpdateCheckInterval() {
        return config.getInt("auto-update.check-interval", 24);
    }

    public boolean isWhitelistEnabled() {
        return config.getBoolean("auto-update.whitelist-enabled", false);
    }

    public List<String> getUpdateWhitelist() {
        return config.getStringList("auto-update.whitelist");
    }

    public boolean isBlacklistEnabled() {
        return config.getBoolean("auto-update.blacklist-enabled", false);
    }

    public List<String> getUpdateBlacklist() {
        return config.getStringList("auto-update.blacklist");
    }

    public boolean backupBeforeUpdate() {
        return config.getBoolean("auto-update.backup-before-update", true);
    }

    public boolean notifyAdmins() {
        return config.getBoolean("auto-update.notify-admins", true);
    }

    // ═══════════════════════════════════════════════════════
    //  SEGURIDAD
    // ═══════════════════════════════════════════════════════

    public List<String> getTrustedSources() {
        return config.getStringList("security.trusted-sources");
    }

    public boolean verifySSL() {
        return config.getBoolean("security.verify-ssl", true);
    }

    public boolean httpsOnly() {
        return config.getBoolean("security.https-only", true);
    }

    public boolean blockPremium() {
        return config.getBoolean("security.block-premium", false);
    }

    public boolean requireConfirmation() {
        return config.getBoolean("security.require-confirmation", false);
    }

    // ═══════════════════════════════════════════════════════
    //  RENDIMIENTO
    // ═══════════════════════════════════════════════════════

    public int getThreadPoolSize() {
        return config.getInt("performance.thread-pool-size", 5);
    }

    public boolean compressCache() {
        return config.getBoolean("performance.compress-cache", false);
    }

    public int getMaxConcurrentSearches() {
        return config.getInt("performance.max-concurrent-searches", 2);
    }

    public int getSearchCooldown() {
        return config.getInt("performance.search-cooldown", 3);
    }

    // ═══════════════════════════════════════════════════════
    //  MENSAJES PERSONALIZADOS
    // ═══════════════════════════════════════════════════════

    public String getMessage(String key) {
        return config.getString("messages." + key, "");
    }

    public String getSearchStartMessage() {
        return getMessage("search-start");
    }

    public String getSearchWaitMessage() {
        return getMessage("search-wait");
    }

    public String getSearchCompleteMessage() {
        return getMessage("search-complete");
    }

    public String getSearchNoResultsMessage() {
        return getMessage("search-no-results");
    }

    public String getInstallStartMessage() {
        return getMessage("install-start");
    }

    public String getInstallSuccessMessage() {
        return getMessage("install-success");
    }

    public String getInstallErrorMessage() {
        return getMessage("install-error");
    }

    // ═══════════════════════════════════════════════════════
    //  NOTIFICACIONES
    // ═══════════════════════════════════════════════════════

    public boolean notifyOnStart() {
        return config.getBoolean("notifications.notify-on-start", true);
    }

    public boolean notifyOnInstall() {
        return config.getBoolean("notifications.notify-on-install", true);
    }

    public boolean notifyOnError() {
        return config.getBoolean("notifications.notify-on-error", true);
    }

    public boolean isDiscordEnabled() {
        return config.getBoolean("notifications.discord.enabled", false);
    }

    public String getDiscordWebhook() {
        return config.getString("notifications.discord.webhook-url", "");
    }

    // ═══════════════════════════════════════════════════════
    //  ESTADÍSTICAS
    // ═══════════════════════════════════════════════════════

    public boolean isStatisticsEnabled() {
        return config.getBoolean("statistics.enabled", true);
    }

    public boolean sendAnonymousStats() {
        return config.getBoolean("statistics.send-anonymous", true);
    }

    public boolean saveHistory() {
        return config.getBoolean("statistics.save-history", true);
    }

    public boolean showStatsInInfo() {
        return config.getBoolean("statistics.show-in-info", true);
    }

    // ═══════════════════════════════════════════════════════
    //  COMANDOS
    // ═══════════════════════════════════════════════════════

    public boolean isCommandEnabled(String command) {
        return config.getBoolean("commands." + command + ".enabled", true);
    }

    public int getCommandCooldown(String command) {
        return config.getInt("commands." + command + ".cooldown", 0);
    }

    // ═══════════════════════════════════════════════════════
    //  AVANZADO
    // ═══════════════════════════════════════════════════════

    public boolean isProxyEnabled() {
        return config.getBoolean("advanced.proxy.enabled", false);
    }

    public String getProxyHost() {
        return config.getString("advanced.proxy.host", "");
    }

    public int getProxyPort() {
        return config.getInt("advanced.proxy.port", 8080);
    }

    public String getUserAgent() {
        return config.getString("advanced.user-agent", "PluginHub/1.0");
    }

    public int getHttpTimeout() {
        return config.getInt("advanced.http-timeout", 15000);
    }

    public boolean followRedirects() {
        return config.getBoolean("advanced.follow-redirects", true);
    }

    public int getMaxRedirects() {
        return config.getInt("advanced.max-redirects", 5);
    }

    // ═══════════════════════════════════════════════════════
    //  EXPERIMENTAL
    // ═══════════════════════════════════════════════════════

    public boolean autoDependencies() {
        return config.getBoolean("experimental.auto-dependencies", false);
    }

    public boolean verifyChecksums() {
        return config.getBoolean("experimental.verify-checksums", false);
    }

    public boolean versionFiltering() {
        return config.getBoolean("experimental.version-filtering", false);
    }

    public boolean isWebInterfaceEnabled() {
        return config.getBoolean("experimental.web-interface.enabled", false);
    }

    public boolean isDatabaseEnabled() {
        return config.getBoolean("experimental.database.enabled", false);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
