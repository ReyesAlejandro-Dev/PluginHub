package com.pluginhub.api;

/**
 * Enum que representa las diferentes fuentes de plugins
 */
public enum PluginSource {
    SPIGOT("SpigotMC", "https://api.spiget.org/v2"),
    BUKKIT("BukkitDev", "https://dev.bukkit.org"),
    MODRINTH("Modrinth", "https://api.modrinth.com/v2"),
    GITHUB("GitHub", "https://api.github.com"),
    HANGAR("Hangar", "https://hangar.papermc.io/api/v1"),
    CUSTOM("Custom", "");

    private final String displayName;
    private final String apiUrl;

    PluginSource(String displayName, String apiUrl) {
        this.displayName = displayName;
        this.apiUrl = apiUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
