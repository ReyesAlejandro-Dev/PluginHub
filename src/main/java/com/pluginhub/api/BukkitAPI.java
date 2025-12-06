package com.pluginhub.api;

import com.pluginhub.models.PluginInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Cliente para BukkitDev (web scraping)
 * No tiene API pública, así que usamos web scraping
 */
public class BukkitAPI {

    private static final String BASE_URL = "https://dev.bukkit.org";
    private static final String SEARCH_URL = BASE_URL + "/bukkit-plugins?filter-search=";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    
    private final OkHttpClient client;
    private final Logger logger;

    public BukkitAPI(Logger logger) {
        this.logger = logger;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .followRedirects(true)
                .build();
    }

    /**
     * Busca plugins en BukkitDev mediante web scraping
     */
    public List<PluginInfo> searchPlugins(String query, int limit) throws IOException {
        List<PluginInfo> results = new ArrayList<>();
        
        String url = SEARCH_URL + query.replace(" ", "+");

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error buscando en BukkitDev: " + response.code());
            }

            String html = response.body().string();
            Document doc = Jsoup.parse(html);
            
            Elements projectItems = doc.select(".project-list-item");
            
            int count = 0;
            for (Element item : projectItems) {
                if (count >= limit) break;
                
                try {
                    PluginInfo info = parsePluginFromElement(item);
                    if (info != null) {
                        results.add(info);
                        count++;
                    }
                } catch (Exception e) {
                    logger.warning("Error parseando plugin de BukkitDev: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warning("Error en búsqueda de BukkitDev: " + e.getMessage());
        }

        return results;
    }

    /**
     * Obtiene información de un plugin desde un elemento HTML
     */
    private PluginInfo parsePluginFromElement(Element item) {
        try {
            Element nameElement = item.selectFirst(".project-list-item__name a");
            if (nameElement == null) return null;

            String name = nameElement.text();
            String relativeUrl = nameElement.attr("href");
            String sourceUrl = BASE_URL + relativeUrl;
            String id = relativeUrl.replaceAll(".*/", "");

            Element descElement = item.selectFirst(".project-list-item__description");
            String description = descElement != null ? descElement.text() : "Sin descripción";

            Element authorElement = item.selectFirst(".project-list-item__author a");
            String author = authorElement != null ? authorElement.text() : "Desconocido";

            Element downloadsElement = item.selectFirst(".project-list-item__downloads");
            int downloads = 0;
            if (downloadsElement != null) {
                String downloadsText = downloadsElement.text().replaceAll("[^0-9]", "");
                if (!downloadsText.isEmpty()) {
                    downloads = Integer.parseInt(downloadsText);
                }
            }

            // La URL de descarga requiere visitar la página del proyecto
            String downloadUrl = sourceUrl + "/files/latest";

            return new PluginInfo.Builder()
                    .id(id)
                    .name(name)
                    .version("latest")
                    .author(author)
                    .description(description)
                    .downloadUrl(downloadUrl)
                    .sourceUrl(sourceUrl)
                    .source(PluginSource.BUKKIT)
                    .downloads(downloads)
                    .category("Bukkit")
                    .build();

        } catch (Exception e) {
            logger.warning("Error parseando elemento: " + e.getMessage());
            return null;
        }
    }

    /**
     * Cierra el cliente HTTP
     */
    public void shutdown() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}
