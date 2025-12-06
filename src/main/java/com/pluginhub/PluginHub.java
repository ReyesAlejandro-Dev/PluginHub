package com.pluginhub;

import com.pluginhub.commands.*;
import com.pluginhub.managers.PluginDownloader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginHub extends JavaPlugin {

    private PluginDownloader pluginDownloader;
    
    // Códigos de color ANSI para consola
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    private static final String BLUE = "\u001B[34m";

    @Override
    public void onEnable() {
        // Log inicial con ASCII Art colorido
        printBanner();

        // Inicializar el descargador
        this.pluginDownloader = new PluginDownloader(this);
        logSuccess("PluginDownloader inicializado");

        // Registrar comandos
        registerCommands();

        // Crear carpeta de plugins si no existe
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            logInfo("Carpeta de datos creada");
        }

        // Mensaje final de éxito
        logSuccess("PluginHub iniciado correctamente!");
        printSeparator();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(RED + BOLD + "  ✖ " + RESET + RED + "PluginHub " + YELLOW + "v" + getDescription().getVersion() + RED + " deshabilitado" + RESET);
        Bukkit.getConsoleSender().sendMessage("");
    }

    private void printBanner() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ╔══════════════════════════════════════════════════════╗" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██████╗ ██╗     ██╗   ██╗ ██████╗ ██╗███╗   ██╗  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██╔══██╗██║     ██║   ██║██╔════╝ ██║████╗  ██║  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██████╔╝██║     ██║   ██║██║  ███╗██║██╔██╗ ██║  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██╔═══╝ ██║     ██║   ██║██║   ██║██║██║╚██╗██║  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██║     ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ╚═╝     ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + YELLOW + "              ★ " + WHITE + "HUB" + YELLOW + " ★                                 " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ╠══════════════════════════════════════════════════════╣" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Versión: " + YELLOW + "v" + getDescription().getVersion() + WHITE + "                                    " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Autor: " + CYAN + "Tu Nombre" + WHITE + "                                  " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Gestor centralizado de plugins" + WHITE + "                    " + PURPLE + "║" + RESET);
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ╚══════════════════════════════════════════════════════╝" + RESET);
        Bukkit.getConsoleSender().sendMessage("");
    }

    private void printSeparator() {
        Bukkit.getConsoleSender().sendMessage(PURPLE + "  ══════════════════════════════════════════════════════" + RESET);
        Bukkit.getConsoleSender().sendMessage("");
    }

    private void registerCommands() {
        // Comando principal
        getCommand("pluginhub").setExecutor(new PluginHubCommand(this));
        getCommand("phsearch").setExecutor(new PluginSearchCommand(this, pluginDownloader));
        getCommand("phinstall").setExecutor(new PluginInstallCommand(this, pluginDownloader));
        getCommand("phupdate").setExecutor(new PluginUpdateCommand(this, pluginDownloader));

        logSuccess("Comandos registrados:");
        logCommand("/pluginhub", "Comando principal");
        logCommand("/phsearch", "Buscar plugins");
        logCommand("/phinstall", "Instalar plugins");
        logCommand("/phupdate", "Actualizar plugins");
    }

    // ═══════════════════════════════════════════════════════
    //  Métodos de logging personalizados
    // ═══════════════════════════════════════════════════════

    public void logSuccess(String message) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │ " + GREEN + "✔ " + WHITE + message + RESET
        );
    }

    public void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │ " + CYAN + "ℹ " + WHITE + message + RESET
        );
    }

    public void logWarning(String message) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │ " + YELLOW + "⚠ " + YELLOW + message + RESET
        );
    }

    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │ " + RED + "✖ " + RED + message + RESET
        );
    }

    public void logCommand(String command, String description) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │   " + YELLOW + "→ " + CYAN + command + WHITE + " - " + BLUE + description + RESET
        );
    }

    public void logDownload(String pluginName, String status) {
        Bukkit.getConsoleSender().sendMessage(
            PURPLE + "  │ " + BLUE + "⬇ " + WHITE + pluginName + " - " + CYAN + status + RESET
        );
    }

    public PluginDownloader getPluginDownloader() {
        return pluginDownloader;
    }
}