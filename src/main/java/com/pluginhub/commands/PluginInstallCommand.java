package com.pluginhub.commands;

import com.pluginhub.managers.PluginDownloader;
import com.pluginhub.managers.PluginDownloader.PluginInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginInstallCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final PluginDownloader downloader;

    public PluginInstallCommand(JavaPlugin plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c✗ Uso: /phinstall <nombre del plugin>");
            return true;
        }

        if (!sender.hasPermission("pluginhub.install")) {
            sender.sendMessage("§c✗ No tienes permiso para instalar plugins");
            return true;
        }

        String pluginName = args[0];
        PluginInfo info = downloader.getPluginInfo(pluginName);

        if (info == null) {
            sender.sendMessage("§c✗ Plugin no encontrado: " + pluginName);
            sender.sendMessage("§7Intenta: /phsearch " + pluginName);
            return true;
        }

        sender.sendMessage("§e⏳ Instalando " + info.getName() + "...");

        // Instalación asincrónica
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean success = downloader.installPlugin(pluginName);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (success) {
                    sender.sendMessage("§a✓ " + info.getName() + " v" + info.getVersion() + " instalado correctamente");
                    sender.sendMessage("§6Reinicia el servidor para activar: §e/reload confirm");
                } else {
                    sender.sendMessage("§c✗ Error durante la instalación de " + pluginName);
                }
            });
        });

        return true;
    }
}
