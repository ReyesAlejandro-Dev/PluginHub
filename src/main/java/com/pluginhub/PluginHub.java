package com.pluginhub;

import com.pluginhub.commands.*;
import com.pluginhub.managers.PluginDownloader;
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
        colorLogger.logSuccess("PluginDownloader inicializado");

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
        registerCommand("phinstall", new PluginInstallCommand(this, pluginDownloader));
        registerCommand("phupdate", new PluginUpdateCommand(this, pluginDownloader));

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
}
