package com.pluginhub.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utilidad para logging con colores ANSI en consola
 */
public final class ColorLogger {

    // Códigos de color ANSI
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";
    private static final String BOLD = "\u001B[1m";
    private static final String BLUE = "\u001B[34m";

    private final JavaPlugin plugin;

    public ColorLogger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void printBanner(String version) {
        sendMessage("");
        sendMessage(PURPLE + "  ╔══════════════════════════════════════════════════════╗" + RESET);
        sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██████╗ ██╗     ██╗   ██╗ ██████╗ ██╗███╗   ██╗  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██╔══██╗██║     ██║   ██║██╔════╝ ██║████╗  ██║  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██████╔╝██║     ██║   ██║██║  ███╗██║██╔██╗ ██║  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██╔═══╝ ██║     ██║   ██║██║   ██║██║██║╚██╗██║  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ██║     ███████╗╚██████╔╝╚██████╔╝██║██║ ╚████║  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + CYAN + BOLD + "    ╚═╝     ╚══════╝ ╚═════╝  ╚═════╝ ╚═╝╚═╝  ╚═══╝  " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + YELLOW + "              ★ " + WHITE + "HUB" + YELLOW + " ★                                 " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + RESET + "                                                      " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ╠══════════════════════════════════════════════════════╣" + RESET);
        sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Versión: " + YELLOW + "v" + version + WHITE + "                                    " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Autor: " + CYAN + "Pecar" + WHITE + "                                     " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ║" + GREEN + "  ● " + WHITE + "Gestor centralizado de plugins" + WHITE + "                    " + PURPLE + "║" + RESET);
        sendMessage(PURPLE + "  ╚══════════════════════════════════════════════════════╝" + RESET);
        sendMessage("");
    }

    public void printSeparator() {
        sendMessage(PURPLE + "  ══════════════════════════════════════════════════════" + RESET);
        sendMessage("");
    }

    public void logSuccess(String message) {
        sendMessage(PURPLE + "  │ " + GREEN + "✔ " + WHITE + message + RESET);
    }

    public void logInfo(String message) {
        sendMessage(PURPLE + "  │ " + CYAN + "ℹ " + WHITE + message + RESET);
    }

    public void logWarning(String message) {
        sendMessage(PURPLE + "  │ " + YELLOW + "⚠ " + YELLOW + message + RESET);
    }

    public void logError(String message) {
        sendMessage(PURPLE + "  │ " + RED + "✖ " + RED + message + RESET);
    }

    public void logCommand(String command, String description) {
        sendMessage(PURPLE + "  │   " + YELLOW + "→ " + CYAN + command + WHITE + " - " + BLUE + description + RESET);
    }

    public void logDownload(String pluginName, String status) {
        sendMessage(PURPLE + "  │ " + BLUE + "⬇ " + WHITE + pluginName + " - " + CYAN + status + RESET);
    }

    public void logShutdown(String version) {
        sendMessage("");
        sendMessage(RED + BOLD + "  ✖ " + RESET + RED + "PluginHub " + YELLOW + "v" + version + RED + " deshabilitado" + RESET);
        sendMessage("");
    }

    private void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
