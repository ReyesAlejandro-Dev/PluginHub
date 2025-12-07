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

/**
 * Comando para ver información detallada de plugins
 */
public final class PluginInfoCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final PluginDownloader downloader;

    public PluginInfoCommand(PluginHub plugin, PluginDownloader downloader) {
        this.plugin = plugin;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.info")) {
            sender.sendMessage("§c✗ No tienes permiso para ver información");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§c✗ Uso: §e/phinfo <plugin>");
            sender.sendMessage("§7Ejemplo: §f/phinfo essentialsx");
            return true;
        }

        String pluginName = args[0].toLowerCase().trim();
        
        sender.sendMessage("§e⏳ Buscando información de §f" + pluginName + "§e...");

        PluginInfo info = downloader.getPluginInfo(pluginName);

        if (info == null) {
            sender.sendMessage("§c✗ Plugin no encontrado: §f" + pluginName);
            sender.sendMessage("§7Intenta buscar con: §e/phsearch " + pluginName);
            return true;
        }

        displayPluginInfo(sender, info);
        return true;
    }

    private void displayPluginInfo(CommandSender sender, PluginInfo info) {
        sender.sendMessage("");
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§l" + centerText(info.getName(), 36) + "  §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        // Información básica
        sender.sendMessage("§e● Versión: §f" + info.getVersion());
        sender.sendMessage("§e● Autor: §f" + info.getAuthor());
        sender.sendMessage("§e● Fuente: §d" + info.getSource().getDisplayName());
        
        // Descripción
        if (info.getDescription() != null && !info.getDescription().isEmpty()) {
            sender.sendMessage("");
            sender.sendMessage("§e● Descripción:");
            String desc = info.getDescription();
            if (desc.length() > 100) {
                desc = desc.substring(0, 97) + "...";
            }
            sender.sendMessage("§7  " + desc);
        }

        // Estadísticas
        sender.sendMessage("");
        sender.sendMessage("§e● Estadísticas:");
        sender.sendMessage("§7  Descargas: §f" + formatNumber(info.getDownloads()));
        sender.sendMessage("§7  Rating: §f" + info.getRating() + "§7/5.0 ⭐");

        // Enlaces
        sender.sendMessage("");
        sender.sendMessage("§e● Enlaces:");
        sender.sendMessage("§7  Página: §9" + info.getSourceUrl());
        if (info.getDownloadUrl() != null && !info.getDownloadUrl().isEmpty()) {
            sender.sendMessage("§7  Descarga: §9" + info.getDownloadUrl());
        }

        // Estado de instalación
        sender.sendMessage("");
        boolean installed = downloader.getInstalledPlugins().stream()
                .anyMatch(p -> p.equalsIgnoreCase(info.getName()));
        
        if (installed) {
            sender.sendMessage("§a✓ Este plugin está instalado");
            sender.sendMessage("§7Usa §e/phupdate " + info.getName().toLowerCase() + "§7 para actualizar");
        } else {
            sender.sendMessage("§7Este plugin no está instalado");
            sender.sendMessage("§7Usa §e/phinstall " + info.getName().toLowerCase() + "§7 para instalar");
        }

        sender.sendMessage("");
    }

    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        
        sb.append(text);
        return sb.toString();
    }

    private String formatNumber(int number) {
        if (number >= 1000000) {
            return String.format("%.1fM", number / 1000000.0);
        } else if (number >= 1000) {
            return String.format("%.1fK", number / 1000.0);
        }
        return String.valueOf(number);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(downloader.getInstalledPlugins());
        }
        return new ArrayList<>();
    }
}
