package com.pluginhub.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Gestor de plugins favoritos
 */
public class FavoritesManager {
    
    private final JavaPlugin plugin;
    private final File favoritesFile;
    private FileConfiguration favoritesConfig;
    private final Set<String> favorites;

    public FavoritesManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.favoritesFile = new File(plugin.getDataFolder(), "favorites.yml");
        this.favorites = new HashSet<>();
        loadFavorites();
    }

    private void loadFavorites() {
        if (!favoritesFile.exists()) {
            try {
                favoritesFile.getParentFile().mkdirs();
                favoritesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Error creando favorites.yml: " + e.getMessage());
            }
        }
        
        favoritesConfig = YamlConfiguration.loadConfiguration(favoritesFile);
        favorites.clear();
        favorites.addAll(favoritesConfig.getStringList("favorites"));
    }

    public boolean addFavorite(String pluginName) {
        if (favorites.add(pluginName.toLowerCase())) {
            save();
            return true;
        }
        return false;
    }

    public boolean removeFavorite(String pluginName) {
        if (favorites.remove(pluginName.toLowerCase())) {
            save();
            return true;
        }
        return false;
    }

    public boolean isFavorite(String pluginName) {
        return favorites.contains(pluginName.toLowerCase());
    }

    public List<String> getFavorites() {
        return new ArrayList<>(favorites);
    }

    private void save() {
        favoritesConfig.set("favorites", new ArrayList<>(favorites));
        try {
            favoritesConfig.save(favoritesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error guardando favorites.yml: " + e.getMessage());
        }
    }

    public void reload() {
        loadFavorites();
    }
}
