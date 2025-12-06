package com.pluginhub.models;

import java.util.Objects;

/**
 * Modelo de datos para información de plugins
 */
public final class PluginInfo {

    private final String name;
    private final String version;
    private final String downloadUrl;
    private final String sourceUrl;
    private final String description;

    public PluginInfo(String name, String version, String downloadUrl, String sourceUrl, String description) {
        this.name = Objects.requireNonNull(name, "El nombre no puede ser null");
        this.version = Objects.requireNonNull(version, "La versión no puede ser null");
        this.downloadUrl = Objects.requireNonNull(downloadUrl, "La URL de descarga no puede ser null");
        this.sourceUrl = Objects.requireNonNull(sourceUrl, "La URL de origen no puede ser null");
        this.description = Objects.requireNonNull(description, "La descripción no puede ser null");
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginInfo that = (PluginInfo) o;
        return name.equals(that.name) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }

    @Override
    public String toString() {
        return String.format("PluginInfo{name='%s', version='%s'}", name, version);
    }
}
