package com.pluginhub.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Gestor de historial de instalaciones
 */
public class HistoryManager {
    
    private final JavaPlugin plugin;
    private final File historyFile;
    private FileConfiguration historyConfig;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public HistoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.historyFile = new File(plugin.getDataFolder(), "history.yml");
        loadHistory();
    }

    private void loadHistory() {
        if (!historyFile.exists()) {
            try {
                historyFile.getParentFile().mkdirs();
                historyFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Error creando history.yml: " + e.getMessage());
            }
        }
        
        historyConfig = YamlConfiguration.loadConfiguration(historyFile);
    }

    public void recordInstall(String pluginName, String version, String source, String installedBy) {
        String timestamp = DATE_FORMAT.format(new Date());
        String path = "installs." + pluginName.toLowerCase() + "." + System.currentTimeMillis();
        
        historyConfig.set(path + ".version", version);
        historyConfig.set(path + ".source", source);
        historyConfig.set(path + ".timestamp", timestamp);
        historyConfig.set(path + ".installed-by", installedBy);
        historyConfig.set(path + ".action", "INSTALL");
        
        save();
    }

    public void recordUpdate(String pluginName, String oldVersion, String newVersion, String updatedBy) {
        String timestamp = DATE_FORMAT.format(new Date());
        String path = "installs." + pluginName.toLowerCase() + "." + System.currentTimeMillis();
        
        historyConfig.set(path + ".old-version", oldVersion);
        historyConfig.set(path + ".new-version", newVersion);
        historyConfig.set(path + ".timestamp", timestamp);
        historyConfig.set(path + ".updated-by", updatedBy);
        historyConfig.set(path + ".action", "UPDATE");
        
        save();
    }

    public List<Map<String, String>> getHistory(String pluginName) {
        List<Map<String, String>> history = new ArrayList<>();
        String path = "installs." + pluginName.toLowerCase();
        
        if (historyConfig.contains(path)) {
            var section = historyConfig.getConfigurationSection(path);
            if (section != null) {
                for (String key : section.getKeys(false)) {
                    Map<String, String> entry = new HashMap<>();
                    var entrySection = section.getConfigurationSection(key);
                    if (entrySection != null) {
                        for (String field : entrySection.getKeys(false)) {
                            entry.put(field, entrySection.getString(field));
                        }
                        history.add(entry);
                    }
                }
            }
        }
        
        return history;
    }

    public List<Map<String, String>> getAllHistory() {
        List<Map<String, String>> allHistory = new ArrayList<>();
        
        if (historyConfig.contains("installs")) {
            var installs = historyConfig.getConfigurationSection("installs");
            if (installs != null) {
                for (String pluginName : installs.getKeys(false)) {
                    allHistory.addAll(getHistory(pluginName));
                }
            }
        }
        
        return allHistory;
    }

    private void save() {
        try {
            historyConfig.save(historyFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error guardando history.yml: " + e.getMessage());
        }
    }

    public void reload() {
        loadHistory();
    }
}
