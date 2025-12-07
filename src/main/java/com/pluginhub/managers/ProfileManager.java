package com.pluginhub.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Gestor de perfiles de plugins (conjuntos predefinidos)
 */
public class ProfileManager {
    
    private final JavaPlugin plugin;
    private final File profilesFile;
    private FileConfiguration profilesConfig;

    public ProfileManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.profilesFile = new File(plugin.getDataFolder(), "profiles.yml");
        loadProfiles();
        createDefaultProfiles();
    }

    private void loadProfiles() {
        if (!profilesFile.exists()) {
            try {
                profilesFile.getParentFile().mkdirs();
                profilesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Error creando profiles.yml: " + e.getMessage());
            }
        }
        
        profilesConfig = YamlConfiguration.loadConfiguration(profilesFile);
    }

    private void createDefaultProfiles() {
        // Perfil Starter Pack
        if (!profileExists("starter-pack")) {
            createProfile("starter-pack", "Plugins esenciales para comenzar", 
                Arrays.asList("essentialsx", "luckperms", "vault"));
        }

        // Perfil Survival
        if (!profileExists("survival")) {
            createProfile("survival", "Servidor de supervivencia completo",
                Arrays.asList("essentialsx", "luckperms", "vault", "worldguard", "coreprotect"));
        }

        // Perfil Creative
        if (!profileExists("creative")) {
            createProfile("creative", "Servidor creativo con herramientas",
                Arrays.asList("essentialsx", "worldedit", "plotsquared", "luckperms"));
        }

        // Perfil Minigames
        if (!profileExists("minigames")) {
            createProfile("minigames", "Base para servidor de minijuegos",
                Arrays.asList("multiverse-core", "citizens", "essentialsx", "luckperms"));
        }
    }

    public boolean createProfile(String name, String description, List<String> plugins) {
        if (profileExists(name)) {
            return false;
        }

        String path = "profiles." + name;
        profilesConfig.set(path + ".description", description);
        profilesConfig.set(path + ".plugins", plugins);
        profilesConfig.set(path + ".created", System.currentTimeMillis());
        
        save();
        return true;
    }

    public boolean deleteProfile(String name) {
        if (!profileExists(name)) {
            return false;
        }

        profilesConfig.set("profiles." + name, null);
        save();
        return true;
    }

    public boolean addPluginToProfile(String profileName, String pluginName) {
        if (!profileExists(profileName)) {
            return false;
        }

        List<String> plugins = getProfilePlugins(profileName);
        if (!plugins.contains(pluginName.toLowerCase())) {
            plugins.add(pluginName.toLowerCase());
            profilesConfig.set("profiles." + profileName + ".plugins", plugins);
            save();
            return true;
        }
        return false;
    }

    public boolean removePluginFromProfile(String profileName, String pluginName) {
        if (!profileExists(profileName)) {
            return false;
        }

        List<String> plugins = getProfilePlugins(profileName);
        if (plugins.remove(pluginName.toLowerCase())) {
            profilesConfig.set("profiles." + profileName + ".plugins", plugins);
            save();
            return true;
        }
        return false;
    }

    public boolean profileExists(String name) {
        return profilesConfig.contains("profiles." + name);
    }

    public List<String> getProfilePlugins(String name) {
        if (!profileExists(name)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(profilesConfig.getStringList("profiles." + name + ".plugins"));
    }

    public String getProfileDescription(String name) {
        if (!profileExists(name)) {
            return null;
        }
        return profilesConfig.getString("profiles." + name + ".description");
    }

    public Set<String> getAllProfiles() {
        if (!profilesConfig.contains("profiles")) {
            return Collections.emptySet();
        }
        
        var section = profilesConfig.getConfigurationSection("profiles");
        return section != null ? section.getKeys(false) : Collections.emptySet();
    }

    public Map<String, Object> getProfileInfo(String name) {
        if (!profileExists(name)) {
            return null;
        }

        Map<String, Object> info = new HashMap<>();
        String path = "profiles." + name;
        
        info.put("name", name);
        info.put("description", profilesConfig.getString(path + ".description"));
        info.put("plugins", profilesConfig.getStringList(path + ".plugins"));
        info.put("created", profilesConfig.getLong(path + ".created"));
        
        return info;
    }

    private void save() {
        try {
            profilesConfig.save(profilesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error guardando profiles.yml: " + e.getMessage());
        }
    }

    public void reload() {
        loadProfiles();
    }
}
