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
import java.util.stream.Collectors;

/**
 * Comando para buscar plugins disponibles
 */
public final class PluginSearchCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final PluginDownloader downloader;

    public PluginSearchCommand(PluginHub plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§c✗ Uso: §e/phsearch <nombre del plugin>");
            sender.sendMessage("§7Ejemplo: §f/phsearch essentials");
            return true;
        }

        String query = String.join(" ", args);
        
        if (query.length() < 2) {
            sender.sendMessage("§c✗ La búsqueda debe tener al menos 2 caracteres");
            return true;
        }

        sender.sendMessage("§e⏳ Buscando plugins que coincidan con: §f" + query);

        // Búsqueda asincrónica para no bloquear el servidor
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<PluginInfo> results = downloader.searchPlugins(query);

            // Volver al thread principal para enviar mensajes
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                displayResults(sender, results, query);
            });
        });

        return true;
    }

    /**
     * Muestra los resultados de la búsqueda
     */
    private void displayResults(CommandSender sender, List<PluginInfo> results, String query) {
        if (results.isEmpty()) {
            sender.sendMessage("§c✗ No se encontraron plugins para: §f" + query);
            sender.sendMessage("§7Intenta con otro término de búsqueda");
            return;
        }

        sender.sendMessage("§a✓ Se encontraron §f" + results.size() + "§a resultado(s):");
        sender.sendMessage("§6════════════════════════════════════════");

        for (PluginInfo info : results) {
            displayPluginInfo(sender, info);
        }

        sender.sendMessage("§6════════════════════════════════════════");
        sender.sendMessage("§7Usa §e/phinstall <nombre> §7para instalar");
    }

    /**
     * Muestra información de un plugin individual
     */
    private void displayPluginInfo(CommandSender sender, PluginInfo info) {
        sender.sendMessage("");
        sender.sendMessage("§e● " + info.getName() + " §7(v" + info.getVersion() + ")");
        sender.sendMessage("  §f" + info.getDescription());
        sender.sendMessage("  §9→ /phinstall " + info.getName().toLowerCase().replaceAll("\\s+", ""));
        sender.sendMessage("  §8" + info.getSourceUrl());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Sugerir nombres de plugins populares
            return downloader.getAllAvailablePlugins().stream()
                    .map(PluginInfo::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
