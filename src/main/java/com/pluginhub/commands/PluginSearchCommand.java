package com.pluginhub.commands;

import com.pluginhub.managers.PluginDownloader;
import com.pluginhub.managers.PluginDownloader.PluginInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public class PluginSearchCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final PluginDownloader downloader;

    public PluginSearchCommand(JavaPlugin plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c✗ Uso: /phsearch <nombre del plugin>");
            return true;
        }

        String query = String.join(" ", args);
        plugin.getLogger().info("🔍 Buscando: " + query);

        // Búsqueda asincrónica
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<PluginInfo> results = downloader.searchPlugins(query);

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (results.isEmpty()) {
                    sender.sendMessage("§c✗ No se encontraron plugins para: " + query);
                } else {
                    sender.sendMessage("§a✓ Se encontraron " + results.size() + " resultados:");
                    sender.sendMessage("§6════════════════════════════════════");
                    
                    for (PluginInfo info : results) {
                        sender.sendMessage("§e• " + info.getName() + " §7(v" + info.getVersion() + ")");
                        sender.sendMessage("  §7" + info.getDescription());
                        sender.sendMessage("  §9→ /phinstall " + info.getName().toLowerCase());
                        sender.sendMessage("§6────────────────────────────────────");
                    }
                }
            });
        });

        return true;
    }
}
