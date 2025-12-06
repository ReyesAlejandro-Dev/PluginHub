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
 * Cliente para la API de Hangar (PaperMC)
 * Documentación: https://hangar.papermc.io/api-docs
 */
public class HangarAPI {

    private static final String BASE_URL = "https://hangar.papermc.io/api/v1";
    private static final String USER_AGENT = "PluginHub/2.0";
    
    private final OkHttpClient client;
    private final Logger logger;

    public HangarAPI(Logger logger) {
        this.logger = logger;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Busca plugins en Hangar
     */
    public List<PluginInfo> searchPlugins(String query, int limit) throws IOException {
        List<PluginInfo> results = new ArrayList<>();
        
        String url = String.format("%s/projects?q=%s&limit=%d&sort=-downloads", 
                BASE_URL, query, limit);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la API de Hangar: " + response.code());
            }

            String body = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            JsonArray projects = jsonObject.getAsJsonArray("result");

            for (JsonElement element : projects) {
                JsonObject obj = element.getAsJsonObject();
                
                try {
                    PluginInfo info = parsePluginInfo(obj);
                    results.add(info);
                } catch (Exception e) {
                    logger.warning("Error parseando plugin de Hangar: " + e.getMessage());
                }
            }
        }

        return results;
    }

    /**
     * Obtiene información detallada de un plugin
     */
    public PluginInfo getPluginBySlug(String owner, String slug) throws IOException {
        String url = BASE_URL + "/projects/" + owner + "/" + slug;

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
    public String getLatestDownloadUrl(String owner, String slug) throws IOException {
        String url = BASE_URL + "/projects/" + owner + "/" + slug + "/versions";

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error obteniendo versiones: " + response.code());
            }

            String body = response.body().string();
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            JsonArray versions = jsonObject.getAsJsonArray("result");
            
            if (versions.size() > 0) {
                JsonObject latestVersion = versions.get(0).getAsJsonObject();
                String versionName = latestVersion.get("name").getAsString();
                
                return String.format("https://hangar.papermc.io/api/v1/projects/%s/%s/versions/%s/PAPER/download",
                        owner, slug, versionName);
            }
        }

        return null;
    }

    /**
     * Parsea un JsonObject a PluginInfo
     */
    private PluginInfo parsePluginInfo(JsonObject obj) throws IOException {
        String name = obj.get("name").getAsString();
        String namespace = obj.get("namespace").getAsJsonObject().get("slug").getAsString();
        String owner = obj.get("namespace").getAsJsonObject().get("owner").getAsString();
        
        String description = obj.has("description") ? obj.get("description").getAsString() : "Sin descripción";
        
        int downloads = obj.has("stats") ? 
                obj.getAsJsonObject("stats").get("downloads").getAsInt() : 0;

        String category = "General";
        if (obj.has("category")) {
            category = obj.get("category").getAsString();
        }

        String downloadUrl = getLatestDownloadUrl(owner, namespace);
        String sourceUrl = "https://hangar.papermc.io/" + owner + "/" + namespace;

        return new PluginInfo.Builder()
                .id(owner + "/" + namespace)
                .name(name)
                .version("latest")
                .author(owner)
                .description(description)
                .downloadUrl(downloadUrl)
                .sourceUrl(sourceUrl)
                .source(PluginSource.HANGAR)
                .downloads(downloads)
                .category(category)
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
