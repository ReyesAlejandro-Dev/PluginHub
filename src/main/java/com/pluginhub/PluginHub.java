package com.pluginhub;

import com.pluginhub.commands.*;
import com.pluginhub.managers.*;
import com.pluginhub.utils.ColorLogger;
import com.pluginhub.utils.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

/**
 * PluginHub - Gestor centralizado de plugins para servidores Paper
 * 
 * @author Pecar
 * @version 1.0.0
 */
public final class PluginHub extends JavaPlugin {

    private PluginDownloader pluginDownloader;
    private ConfigManager configManager;
    private ColorLogger colorLogger;
    private FavoritesManager favoritesManager;
    private HistoryManager historyManager;
    private ProfileManager profileManager;
    private BackupManager backupManager;

    @Override
    public void onEnable() {
        try {
            // Inicializar utilidades
            this.colorLogger = new ColorLogger(this);
            colorLogger.printBanner(getDescription().getVersion());

            // Cargar configuración
            initializeConfiguration();

            // Inicializar managers
            initializeManagers();

            // Registrar comandos
            registerCommands();

            // Mensaje de éxito
            colorLogger.logSuccess("PluginHub iniciado correctamente");
            colorLogger.printSeparator();

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error crítico durante la inicialización", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            // Cleanup de recursos
            if (pluginDownloader != null) {
                pluginDownloader.shutdown();
            }

            colorLogger.logShutdown(getDescription().getVersion());
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error durante el apagado", e);
        }
    }

    /**
     * Inicializa la configuración del plugin
     */
    private void initializeConfiguration() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
        colorLogger.logSuccess("Configuración cargada");
    }

    /**
     * Inicializa los managers del plugin
     */
    private void initializeManagers() {
        this.pluginDownloader = new PluginDownloader(this, configManager);
        this.favoritesManager = new FavoritesManager(this);
        this.historyManager = new HistoryManager(this);
        this.profileManager = new ProfileManager(this);
        this.backupManager = new BackupManager(this);
        
        colorLogger.logSuccess("Managers inicializados");

        // Crear carpeta de datos si no existe
        if (!getDataFolder().exists() && getDataFolder().mkdirs()) {
            colorLogger.logInfo("Carpeta de datos creada");
        }
    }

    /**
     * Registra todos los comandos del plugin
     */
    private void registerCommands() {
        registerCommand("pluginhub", new PluginHubCommand(this));
        registerCommand("phsearch", new PluginSearchCommand(this, pluginDownloader));
        registerCommand("phinstall", new PluginInstallCommand(this, pluginDownloader, historyManager, backupManager));
        registerCommand("phupdate", new PluginUpdateCommand(this, pluginDownloader, historyManager, backupManager));
        registerCommand("phfavorite", new PluginFavoriteCommand(this, favoritesManager));
        registerCommand("phhistory", new PluginHistoryCommand(this, historyManager));
        registerCommand("phprofile", new PluginProfileCommand(this, profileManager, pluginDownloader));
        registerCommand("phbackup", new PluginBackupCommand(this, backupManager));
        registerCommand("phinfo", new PluginInfoCommand(this, pluginDownloader));

        colorLogger.logSuccess("Comandos registrados");
        logRegisteredCommands();
    }

    /**
     * Registra un comando individual con validación
     */
    private void registerCommand(String name, org.bukkit.command.CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name), "Comando no encontrado en plugin.yml: " + name)
                .setExecutor(executor);
    }

    /**
     * Muestra los comandos registrados en consola
     */
    private void logRegisteredCommands() {
        colorLogger.logCommand("/pluginhub", "Comando principal");
        colorLogger.logCommand("/phsearch", "Buscar plugins");
        colorLogger.logCommand("/phinstall", "Instalar plugins");
        colorLogger.logCommand("/phupdate", "Actualizar plugins");
        colorLogger.logCommand("/phfavorite", "Gestionar favoritos");
        colorLogger.logCommand("/phhistory", "Ver historial");
        colorLogger.logCommand("/phprofile", "Gestionar perfiles");
        colorLogger.logCommand("/phbackup", "Gestionar backups");
        colorLogger.logCommand("/phinfo", "Información de plugins");
    }

    // ═══════════════════════════════════════════════════════
    //  Getters públicos
    // ═══════════════════════════════════════════════════════

    public PluginDownloader getPluginDownloader() {
        return pluginDownloader;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ColorLogger getColorLogger() {
        return colorLogger;
    }

    public FavoritesManager getFavoritesManager() {
        return favoritesManager;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }
}
