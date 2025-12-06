package com.pluginhub.commands;

import com.pluginhub.managers.PluginDownloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class PluginUpdateCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final PluginDownloader downloader;

    public PluginUpdateCommand(JavaPlugin plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.update")) {
            sender.sendMessage("§c✗ No tienes permiso para actualizar plugins");
            return true;
        }

        sender.sendMessage("§e⏳ Verificando actualizaciones...");

        // Verificación asincrónica
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<String> installedPlugins = downloader.getInstalledPlugins();

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (installedPlugins.isEmpty()) {
                    sender.sendMessage("§c✗ No hay plugins instalados");
                } else {
                    sender.sendMessage("§a✓ Plugins instalados (" + installedPlugins.size() + "):");
                    sender.sendMessage("§6════════════════════════════════════");
                    for (String pluginName : installedPlugins) {
                        sender.sendMessage("§e• " + pluginName);
                    }
                    sender.sendMessage("§6════════════════════════════════════");
                    sender.sendMessage("§7En v2.0 habrá actualizaciones automáticas");
                }
            });
        });

        return true;
    }
}
