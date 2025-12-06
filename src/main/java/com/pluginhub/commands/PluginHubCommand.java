package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Comando principal de PluginHub
 */
public final class PluginHubCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private static final List<String> SUBCOMMANDS = Arrays.asList("help", "version", "reload", "info", "clearcache");

    public PluginHubCommand(PluginHub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "help":
                showHelp(sender);
                break;
            
            case "version":
                showVersion(sender);
                break;
            
            case "reload":
                reloadConfig(sender);
                break;
            
            case "info":
                showInfo(sender);
                break;
            
            case "clearcache":
                clearCache(sender);
                break;
            
            default:
                sender.sendMessage("§c✗ Comando desconocido. Usa §e/pluginhub help");
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║      §e§lPluginHub §6- §fAyuda             §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");
        sender.sendMessage("§e/phsearch <nombre> §7- Buscar plugins disponibles");
        sender.sendMessage("§e/phinstall <nombre> §7- Instalar un plugin");
        sender.sendMessage("§e/phupdate [nombre] §7- Actualizar plugins");
        sender.sendMessage("");
        sender.sendMessage("§e/pluginhub version §7- Ver versión del plugin");
        sender.sendMessage("§e/pluginhub reload §7- Recargar configuración");
        sender.sendMessage("§e/pluginhub info §7- Información del sistema");
        sender.sendMessage("§e/pluginhub clearcache §7- Limpiar caché de búsqueda");
        sender.sendMessage("");
        sender.sendMessage("§7Permisos: §fpluginhub.admin");
    }

    private void clearCache(CommandSender sender) {
        if (!sender.hasPermission("pluginhub.admin")) {
            sender.sendMessage("§c✗ No tienes permiso para limpiar el caché");
            return;
        }

        try {
            plugin.getPluginDownloader().clearCache();
            sender.sendMessage("§a✓ Caché de búsqueda limpiado correctamente");
        } catch (Exception e) {
            sender.sendMessage("§c✗ Error al limpiar el caché: " + e.getMessage());
            plugin.getLogger().severe("Error limpiando caché: " + e.getMessage());
        }
    }

    private void showVersion(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String author = plugin.getDescription().getAuthors().isEmpty() 
                ? "Desconocido" 
                : plugin.getDescription().getAuthors().get(0);
        
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║      §e§lPluginHub §6- §fVersión           §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("§a● Versión: §f" + version);
        sender.sendMessage("§a● Autor: §f" + author);
        sender.sendMessage("§a● API: §f" + plugin.getDescription().getAPIVersion());
    }

    private void reloadConfig(CommandSender sender) {
        if (!sender.hasPermission("pluginhub.admin")) {
            sender.sendMessage("§c✗ No tienes permiso para recargar la configuración");
            return;
        }

        try {
            plugin.getConfigManager().reloadConfiguration();
            sender.sendMessage("§a✓ Configuración recargada correctamente");
        } catch (Exception e) {
            sender.sendMessage("§c✗ Error al recargar la configuración: " + e.getMessage());
            plugin.getLogger().severe("Error recargando configuración: " + e.getMessage());
        }
    }

    private void showInfo(CommandSender sender) {
        int cachedPlugins = plugin.getPluginDownloader().getAllCachedPlugins().size();
        int installedPlugins = plugin.getPluginDownloader().getInstalledPlugins().size();
        Map<String, Object> cacheStats = plugin.getPluginDownloader().getCacheStats();
        
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║      §e§lPluginHub §6- §fInformación       §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("§a● Plugins en caché: §f" + cachedPlugins);
        sender.sendMessage("§a● Plugins instalados: §f" + installedPlugins);
        sender.sendMessage("§a● Fuentes activas: §fSpigot, Modrinth, Hangar, BukkitDev");
        sender.sendMessage("§a● Caché habilitado: §f" + cacheStats.get("cache_enabled"));
        sender.sendMessage("§a● Auto-actualización: §f" + plugin.getConfigManager().isAutoUpdateEnabled());
        sender.sendMessage("");
        sender.sendMessage("§7Usa §e/phsearch <nombre>§7 para buscar plugins");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return SUBCOMMANDS.stream()
                    .filter(sub -> sub.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
