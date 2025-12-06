package com.pluginhub.models;

import com.pluginhub.api.PluginSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Modelo de datos para información de plugins
 */
public final class PluginInfo {

    private final String id;
    private final String name;
    private final String version;
    private final String author;
    private final String description;
    private final String downloadUrl;
    private final String sourceUrl;
    private final PluginSource source;
    private final List<String> supportedVersions;
    private final int downloads;
    private final double rating;
    private final long lastUpdate;
    private final String category;
    private final boolean premium;
    private final List<String> dependencies;

    private PluginInfo(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "El ID no puede ser null");
        this.name = Objects.requireNonNull(builder.name, "El nombre no puede ser null");
        this.version = Objects.requireNonNull(builder.version, "La versión no puede ser null");
        this.author = builder.author != null ? builder.author : "Desconocido";
        this.description = builder.description != null ? builder.description : "Sin descripción";
        this.downloadUrl = builder.downloadUrl;
        this.sourceUrl = Objects.requireNonNull(builder.sourceUrl, "La URL de origen no puede ser null");
        this.source = Objects.requireNonNull(builder.source, "La fuente no puede ser null");
        this.supportedVersions = Collections.unmodifiableList(new ArrayList<>(builder.supportedVersions));
        this.downloads = builder.downloads;
        this.rating = builder.rating;
        this.lastUpdate = builder.lastUpdate;
        this.category = builder.category != null ? builder.category : "General";
        this.premium = builder.premium;
        this.dependencies = Collections.unmodifiableList(new ArrayList<>(builder.dependencies));
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getDownloadUrl() { return downloadUrl; }
    public String getSourceUrl() { return sourceUrl; }
    public PluginSource getSource() { return source; }
    public List<String> getSupportedVersions() { return supportedVersions; }
    public int getDownloads() { return downloads; }
    public double getRating() { return rating; }
    public long getLastUpdate() { return lastUpdate; }
    public String getCategory() { return category; }
    public boolean isPremium() { return premium; }
    public List<String> getDependencies() { return dependencies; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginInfo that = (PluginInfo) o;
        return id.equals(that.id) && source == that.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source);
    }

    @Override
    public String toString() {
        return String.format("PluginInfo{name='%s', version='%s', source=%s}", name, version, source);
    }

    /**
     * Builder para crear instancias de PluginInfo
     */
    public static class Builder {
        private String id;
        private String name;
        private String version;
        private String author;
        private String description;
        private String downloadUrl;
        private String sourceUrl;
        private PluginSource source;
        private List<String> supportedVersions = new ArrayList<>();
        private int downloads = 0;
        private double rating = 0.0;
        private long lastUpdate = System.currentTimeMillis();
        private String category;
        private boolean premium = false;
        private List<String> dependencies = new ArrayList<>();

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder sourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
            return this;
        }

        public Builder source(PluginSource source) {
            this.source = source;
            return this;
        }

        public Builder supportedVersions(List<String> versions) {
            this.supportedVersions = versions != null ? versions : new ArrayList<>();
            return this;
        }

        public Builder downloads(int downloads) {
            this.downloads = downloads;
            return this;
        }

        public Builder rating(double rating) {
            this.rating = rating;
            return this;
        }

        public Builder lastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public Builder dependencies(List<String> dependencies) {
            this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
            return this;
        }

        public PluginInfo build() {
            return new PluginInfo(this);
        }
    }
}
