package com.pluginhub.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pluginhub.models.PluginInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Cliente para la API de Modrinth
 * Documentación: https://docs.modrinth.com/api-spec/
 */
public class ModrinthAPI {

    private static final String BASE_URL = "https://api.modrinth.com/v2";
    private static final String USER_AGENT = "PluginHub/2.0";
    
    private final OkHttpClient client;
    private final Logger logger;

    public ModrinthAPI(Logger logger) {
        this.logger = logger;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Busca plugins en Modrinth
     */
    public List<PluginInfo> searchPlugins(String query, int limit) throws IOException {
        List<PluginInfo> results = new ArrayList<>();
        
        // Buscar solo plugins de Bukkit/Spigot/Paper
        String url = String.format("%s/search?query=%s&limit=%d&facets=[[\"project_type:plugin\"],[\"categories:bukkit\"]]", 
                BASE_URL, query, limit);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la API de Modrinth: " + response.code());
            }

            String body = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            JsonArray hits = jsonObject.getAsJsonArray("hits");

            for (JsonElement element : hits) {
                JsonObject obj = element.getAsJsonObject();
                
                try {
                    PluginInfo info = parsePluginInfo(obj);
                    results.add(info);
                } catch (Exception e) {
                    logger.warning("Error parseando plugin de Modrinth: " + e.getMessage());
                }
            }
        }

        return results;
    }

    /**
     * Obtiene información detallada de un plugin
     */
    public PluginInfo getPluginById(String projectId) throws IOException {
        String url = BASE_URL + "/project/" + projectId;

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo plugin: " + response.code());
            }

            String body = response.body().string();
            JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
            
            return parsePluginInfo(obj);
        }
    }

    /**
     * Obtiene la URL de descarga más reciente
     */
    public String getLatestDownloadUrl(String projectId) throws IOException {
        String url = BASE_URL + "/project/" + projectId + "/version";

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo versiones: " + response.code());
            }

            String body = response.body().string();
            JsonArray versions = JsonParser.parseString(body).getAsJsonArray();
            
            if (versions.size() > 0) {
                JsonObject latestVersion = versions.get(0).getAsJsonObject();
                JsonArray files = latestVersion.getAsJsonArray("files");
                
                if (files.size() > 0) {
                    JsonObject file = files.get(0).getAsJsonObject();
                    return file.get("url").getAsString();
                }
            }
        }

        return null;
    }

    /**
     * Parsea un JsonObject a PluginInfo
     */
    private PluginInfo parsePluginInfo(JsonObject obj) throws IOException {
        String id = obj.get("project_id").getAsString();
        String name = obj.get("title").getAsString();
        String description = obj.get("description").getAsString();
        String author = obj.get("author").getAsString();
        
        int downloads = obj.has("downloads") ? obj.get("downloads").getAsInt() : 0;
        
        // Obtener categorías
        String category = "General";
        if (obj.has("categories")) {
            JsonArray categories = obj.getAsJsonArray("categories");
            if (categories.size() > 0) {
                category = categories.get(0).getAsString();
            }
        }

        // Obtener versiones soportadas
        List<String> supportedVersions = new ArrayList<>();
        if (obj.has("game_versions")) {
            JsonArray versions = obj.getAsJsonArray("game_versions");
            for (JsonElement version : versions) {
                supportedVersions.add(version.getAsString());
            }
        }

        String downloadUrl = getLatestDownloadUrl(id);
        String sourceUrl = "https://modrinth.com/plugin/" + obj.get("slug").getAsString();

        return new PluginInfo.Builder()
                .id(id)
                .name(name)
                .version("latest")
                .author(author)
                .description(description)
                .downloadUrl(downloadUrl)
                .sourceUrl(sourceUrl)
                .source(PluginSource.MODRINTH)
                .downloads(downloads)
                .category(category)
                .supportedVersions(supportedVersions)
                .build();
    }

    /**
     * Cierra el cliente HTTP
     */
    public void shutdown() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}
