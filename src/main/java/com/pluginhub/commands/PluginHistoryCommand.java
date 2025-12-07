package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.HistoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Comando para ver historial de instalaciones
 */
public final class PluginHistoryCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final HistoryManager historyManager;

    public PluginHistoryCommand(PluginHub plugin, HistoryManager historyManager) {
        this.plugin = plugin;
        this.historyManager = historyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.history")) {
            sender.sendMessage("§c✗ No tienes permiso para ver el historial");
            return true;
        }

        if (args.length == 0) {
            showAllHistory(sender);
        } else {
            showPluginHistory(sender, args[0]);
        }

        return true;
    }

    private void showAllHistory(CommandSender sender) {
        List<Map<String, String>> history = historyManager.getAllHistory();

        if (history.isEmpty()) {
            sender.sendMessage("§e⚠ No hay historial de instalaciones");
            return;
        }

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lHistorial de Instalaciones         §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        int count = 0;
        for (Map<String, String> entry : history) {
            if (count >= 10) break; // Mostrar solo las últimas 10

            String action = entry.getOrDefault("action", "UNKNOWN");
            String timestamp = entry.getOrDefault("timestamp", "N/A");

            if ("INSTALL".equals(action)) {
                String version = entry.getOrDefault("version", "N/A");
                String source = entry.getOrDefault("source", "N/A");
                String by = entry.getOrDefault("installed-by", "Console");
                
                sender.sendMessage("§a✓ INSTALACIÓN");
                sender.sendMessage("§7  Versión: §f" + version);
                sender.sendMessage("§7  Fuente: §d" + source);
                sender.sendMessage("§7  Por: §e" + by);
                sender.sendMessage("§7  Fecha: §f" + timestamp);
            } else if ("UPDATE".equals(action)) {
                String oldVer = entry.getOrDefault("old-version", "N/A");
                String newVer = entry.getOrDefault("new-version", "N/A");
                String by = entry.getOrDefault("updated-by", "Console");
                
                sender.sendMessage("§e⬆ ACTUALIZACIÓN");
                sender.sendMessage("§7  De: §f" + oldVer + " §7→ §f" + newVer);
                sender.sendMessage("§7  Por: §e" + by);
                sender.sendMessage("§7  Fecha: §f" + timestamp);
            }

            sender.sendMessage("");
            count++;
        }

        sender.sendMessage("§7Total de registros: §f" + history.size());
        if (history.size() > 10) {
            sender.sendMessage("§7Mostrando los últimos 10 registros");
        }
    }

    private void showPluginHistory(CommandSender sender, String pluginName) {
        List<Map<String, String>> history = historyManager.getHistory(pluginName);

        if (history.isEmpty()) {
            sender.sendMessage("§e⚠ No hay historial para: §f" + pluginName);
            return;
        }

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lHistorial: §f" + pluginName + "                §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        for (Map<String, String> entry : history) {
            String action = entry.getOrDefault("action", "UNKNOWN");
            String timestamp = entry.getOrDefault("timestamp", "N/A");

            if ("INSTALL".equals(action)) {
                String version = entry.getOrDefault("version", "N/A");
                String source = entry.getOrDefault("source", "N/A");
                
                sender.sendMessage("§a✓ Instalado v" + version);
                sender.sendMessage("§7  Fuente: §d" + source);
                sender.sendMessage("§7  Fecha: §f" + timestamp);
            } else if ("UPDATE".equals(action)) {
                String oldVer = entry.getOrDefault("old-version", "N/A");
                String newVer = entry.getOrDefault("new-version", "N/A");
                
                sender.sendMessage("§e⬆ Actualizado");
                sender.sendMessage("§7  De: §f" + oldVer + " §7→ §f" + newVer);
                sender.sendMessage("§7  Fecha: §f" + timestamp);
            }

            sender.sendMessage("");
        }

        sender.sendMessage("§7Total: §f" + history.size() + "§7 registro(s)");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Podríamos sugerir plugins instalados aquí
        return new ArrayList<>();
    }
}
