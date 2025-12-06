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
 * Cliente para la API de Spiget (SpigotMC)
 * Documentación: https://spiget.org/documentation/
 */
public class SpigotAPI {

    private static final String BASE_URL = "https://api.spiget.org/v2";
    private static final String USER_AGENT = "PluginHub/2.0";
    
    private final OkHttpClient client;
    private final Logger logger;

    public SpigotAPI(Logger logger) {
        this.logger = logger;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Busca plugins en SpigotMC
     */
    public List<PluginInfo> searchPlugins(String query, int limit) throws IOException {
        List<PluginInfo> results = new ArrayList<>();
        
        String url = String.format("%s/search/resources/%s?size=%d&sort=-downloads", 
                BASE_URL, query, limit);

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la API de Spigot: " + response.code());
            }

            String body = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(body).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                
                try {
                    PluginInfo info = parsePluginInfo(obj);
                    results.add(info);
                } catch (Exception e) {
                    logger.warning("Error parseando plugin de Spigot: " + e.getMessage());
                }
            }
        }

        return results;
    }

    /**
     * Obtiene información detallada de un plugin por ID
     */
    public PluginInfo getPluginById(String resourceId) throws IOException {
        String url = BASE_URL + "/resources/" + resourceId;

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
     * Obtiene la URL de descarga de un plugin
     */
    public String getDownloadUrl(String resourceId) {
        return BASE_URL + "/resources/" + resourceId + "/download";
    }

    /**
     * Obtiene las versiones soportadas de un plugin
     */
    public List<String> getSupportedVersions(String resourceId) throws IOException {
        String url = BASE_URL + "/resources/" + resourceId + "/versions?size=1";

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        List<String> versions = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                JsonArray jsonArray = JsonParser.parseString(body).getAsJsonArray();
                
                if (jsonArray.size() > 0) {
                    JsonObject versionObj = jsonArray.get(0).getAsJsonObject();
                    if (versionObj.has("name")) {
                        versions.add(versionObj.get("name").getAsString());
                    }
                }
            }
        } catch (Exception e) {
            logger.warning("Error obteniendo versiones: " + e.getMessage());
        }

        return versions;
    }

    /**
     * Parsea un JsonObject a PluginInfo
     */
    private PluginInfo parsePluginInfo(JsonObject obj) {
        String id = obj.get("id").getAsString();
        String name = obj.get("name").getAsString();
        
        // Obtener autor
        String author = "Desconocido";
        if (obj.has("author")) {
            JsonObject authorObj = obj.getAsJsonObject("author");
            if (authorObj.has("name")) {
                author = authorObj.get("name").getAsString();
            }
        }

        // Obtener descripción (tag line)
        String description = obj.has("tag") ? obj.get("tag").getAsString() : "Sin descripción";

        // Obtener versión
        String version = "latest";
        if (obj.has("version")) {
            JsonObject versionObj = obj.getAsJsonObject("version");
            if (versionObj.has("name")) {
                version = versionObj.get("name").getAsString();
            }
        }

        // Obtener estadísticas
        int downloads = obj.has("downloads") ? obj.get("downloads").getAsInt() : 0;
        double rating = 0.0;
        if (obj.has("rating")) {
            JsonObject ratingObj = obj.getAsJsonObject("rating");
            if (ratingObj.has("average")) {
                rating = ratingObj.get("average").getAsDouble();
            }
        }

        // Obtener última actualización
        long lastUpdate = obj.has("updateDate") ? obj.get("updateDate").getAsLong() * 1000 : System.currentTimeMillis();

        // Obtener categoría
        String category = "General";
        if (obj.has("category")) {
            JsonObject categoryObj = obj.getAsJsonObject("category");
            if (categoryObj.has("name")) {
                category = categoryObj.get("name").getAsString();
            }
        }

        // Verificar si es premium
        boolean premium = obj.has("premium") && obj.get("premium").getAsBoolean();

        String downloadUrl = getDownloadUrl(id);
        String sourceUrl = "https://www.spigotmc.org/resources/" + id + "/";

        return new PluginInfo.Builder()
                .id(id)
                .name(name)
                .version(version)
                .author(author)
                .description(description)
                .downloadUrl(downloadUrl)
                .sourceUrl(sourceUrl)
                .source(PluginSource.SPIGOT)
                .downloads(downloads)
                .rating(rating)
                .lastUpdate(lastUpdate)
                .category(category)
                .premium(premium)
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
