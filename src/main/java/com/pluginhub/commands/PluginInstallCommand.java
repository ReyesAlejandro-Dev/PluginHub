package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.PluginDownloader;
import com.pluginhub.models.PluginInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Comando para instalar plugins
 */
public final class PluginInstallCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final PluginDownloader downloader;

    public PluginInstallCommand(PluginHub plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c✗ Uso: §e/phinstall <nombre del plugin>");
            sender.sendMessage("§7Ejemplo: §f/phinstall essentialsx");
            return true;
        }

        if (!sender.hasPermission("pluginhub.install")) {
            sender.sendMessage("§c✗ No tienes permiso para instalar plugins");
            sender.sendMessage("§7Permiso requerido: §fpluginhub.install");
            return true;
        }

        String pluginName = args[0].toLowerCase().trim();
        
        if (pluginName.isEmpty()) {
            sender.sendMessage("§c✗ El nombre del plugin no puede estar vacío");
            return true;
        }

        PluginInfo info = downloader.getPluginInfo(pluginName);

        if (info == null) {
            sender.sendMessage("§c✗ Plugin no encontrado: §f" + pluginName);
            sender.sendMessage("§7Intenta buscar con: §e/phsearch " + pluginName);
            return true;
        }

        // Verificar si ya está instalado
        if (isPluginInstalled(info.getName())) {
            sender.sendMessage("§e⚠ El plugin §f" + info.getName() + "§e ya está instalado");
            sender.sendMessage("§7Usa §e/phupdate " + pluginName + "§7 para actualizarlo");
            return true;
        }

        sender.sendMessage("§e⏳ Instalando §f" + info.getName() + " v" + info.getVersion() + "§e...");
        sender.sendMessage("§7Fuente: §d" + info.getSource().getDisplayName());
        sender.sendMessage("§7Esto puede tardar unos segundos...");

        // Instalación asincrónica
        CompletableFuture<Boolean> future = downloader.installPluginAsync(info);
        
        future.thenAccept(success -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (success) {
                    handleSuccessfulInstallation(sender, info);
                } else {
                    handleFailedInstallation(sender, info);
                }
            });
        }).exceptionally(throwable -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§c✗ Error crítico durante la instalación");
                plugin.getLogger().severe("Error instalando " + pluginName + ": " + throwable.getMessage());
            });
            return null;
        });

        return true;
    }

    /**
     * Maneja una instalación exitosa
     */
    private void handleSuccessfulInstallation(CommandSender sender, PluginInfo info) {
        sender.sendMessage("§a✓ §f" + info.getName() + " v" + info.getVersion() + "§a instalado correctamente");
        sender.sendMessage("");
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lPróximos pasos                      §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("§71. Reinicia el servidor para cargar el plugin");
        sender.sendMessage("§72. Configura el plugin según tus necesidades");
        sender.sendMessage("§73. Verifica que funcione correctamente");
        sender.sendMessage("");
        sender.sendMessage("§7Más información: §9" + info.getSourceUrl());
    }

    /**
     * Maneja una instalación fallida
     */
    private void handleFailedInstallation(CommandSender sender, PluginInfo info) {
        sender.sendMessage("§c✗ Error durante la instalación de §f" + info.getName());
        sender.sendMessage("");
        sender.sendMessage("§ePosibles causas:");
        sender.sendMessage("§7● Problemas de conexión a internet");
        sender.sendMessage("§7● URL de descarga no disponible");
        sender.sendMessage("§7● Permisos insuficientes en el servidor");
        sender.sendMessage("");
        sender.sendMessage("§7Intenta nuevamente o descarga manualmente desde:");
        sender.sendMessage("§9" + info.getSourceUrl());
    }

    /**
     * Verifica si un plugin ya está instalado
     */
    private boolean isPluginInstalled(String pluginName) {
        return downloader.getInstalledPlugins().stream()
                .anyMatch(installed -> installed.equalsIgnoreCase(pluginName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Sugerir plugins en caché que no estén instalados
            List<String> installed = downloader.getInstalledPlugins();
            
            return downloader.getAllCachedPlugins().stream()
                    .map(PluginInfo::getName)
                    .filter(name -> !installed.contains(name))
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .map(name -> name.toLowerCase().replaceAll("\\s+", ""))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
