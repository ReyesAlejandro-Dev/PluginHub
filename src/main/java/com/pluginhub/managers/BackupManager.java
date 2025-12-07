package com.pluginhub.managers;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gestor de backups de plugins
 */
public class BackupManager {
    
    private final JavaPlugin plugin;
    private final File backupFolder;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final int MAX_BACKUPS_PER_PLUGIN = 5;

    public BackupManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.backupFolder = new File(plugin.getDataFolder(), "backups");
        
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }
    }

    /**
     * Crea un backup de un plugin
     */
    public boolean createBackup(String pluginName) {
        File pluginsFolder = plugin.getServer().getPluginsFolder();
        File pluginFile = findPluginFile(pluginsFolder, pluginName);
        
        if (pluginFile == null || !pluginFile.exists()) {
            plugin.getLogger().warning("No se encontró el archivo del plugin: " + pluginName);
            return false;
        }

        try {
            String timestamp = DATE_FORMAT.format(new Date());
            String backupName = pluginName + "_" + timestamp + ".jar";
            File backupFile = new File(backupFolder, backupName);
            
            Files.copy(pluginFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            plugin.getLogger().info("Backup creado: " + backupName);
            
            // Limpiar backups antiguos
            cleanOldBackups(pluginName);
            
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Error creando backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Restaura un plugin desde un backup
     */
    public boolean restoreBackup(String pluginName, String backupName) {
        File backupFile = new File(backupFolder, backupName);
        
        if (!backupFile.exists()) {
            plugin.getLogger().warning("Backup no encontrado: " + backupName);
            return false;
        }

        try {
            File pluginsFolder = plugin.getServer().getPluginsFolder();
            File pluginFile = new File(pluginsFolder, pluginName + ".jar");
            
            Files.copy(backupFile.toPath(), pluginFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            plugin.getLogger().info("Plugin restaurado desde backup: " + backupName);
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("Error restaurando backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos los backups de un plugin
     */
    public List<String> listBackups(String pluginName) {
        File[] files = backupFolder.listFiles((dir, name) -> 
            name.startsWith(pluginName + "_") && name.endsWith(".jar"));
        
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
                .map(File::getName)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el backup más reciente de un plugin
     */
    public String getLatestBackup(String pluginName) {
        List<String> backups = listBackups(pluginName);
        return backups.isEmpty() ? null : backups.get(0);
    }

    /**
     * Limpia backups antiguos manteniendo solo los últimos N
     */
    private void cleanOldBackups(String pluginName) {
        List<String> backups = listBackups(pluginName);
        
        if (backups.size() > MAX_BACKUPS_PER_PLUGIN) {
            for (int i = MAX_BACKUPS_PER_PLUGIN; i < backups.size(); i++) {
                File oldBackup = new File(backupFolder, backups.get(i));
                if (oldBackup.delete()) {
                    plugin.getLogger().info("Backup antiguo eliminado: " + backups.get(i));
                }
            }
        }
    }

    /**
     * Busca el archivo JAR de un plugin
     */
    private File findPluginFile(File folder, String pluginName) {
        File[] files = folder.listFiles((dir, name) -> 
            name.toLowerCase().contains(pluginName.toLowerCase()) && name.endsWith(".jar"));
        
        return (files != null && files.length > 0) ? files[0] : null;
    }

    /**
     * Elimina todos los backups de un plugin
     */
    public boolean deleteAllBackups(String pluginName) {
        List<String> backups = listBackups(pluginName);
        boolean success = true;
        
        for (String backup : backups) {
            File backupFile = new File(backupFolder, backup);
            if (!backupFile.delete()) {
                success = false;
            }
        }
        
        return success;
    }
}
