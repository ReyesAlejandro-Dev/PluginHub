package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.FavoritesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Comando para gestionar plugins favoritos
 */
public final class PluginFavoriteCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final FavoritesManager favoritesManager;

    public PluginFavoriteCommand(PluginHub plugin, FavoritesManager favoritesManager) {
        this.plugin = plugin;
        this.favoritesManager = favoritesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.favorite")) {
            sender.sendMessage("§c✗ No tienes permiso para gestionar favoritos");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phfavorite add <plugin>");
                    return true;
                }
                handleAdd(sender, args[1]);
                break;

            case "remove":
            case "delete":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phfavorite remove <plugin>");
                    return true;
                }
                handleRemove(sender, args[1]);
                break;

            case "list":
                handleList(sender);
                break;

            case "clear":
                handleClear(sender);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void handleAdd(CommandSender sender, String pluginName) {
        if (favoritesManager.addFavorite(pluginName)) {
            sender.sendMessage("§a✓ §f" + pluginName + "§a agregado a favoritos");
        } else {
            sender.sendMessage("§e⚠ §f" + pluginName + "§e ya está en favoritos");
        }
    }

    private void handleRemove(CommandSender sender, String pluginName) {
        if (favoritesManager.removeFavorite(pluginName)) {
            sender.sendMessage("§a✓ §f" + pluginName + "§a eliminado de favoritos");
        } else {
            sender.sendMessage("§c✗ §f" + pluginName + "§c no está en favoritos");
        }
    }

    private void handleList(CommandSender sender) {
        List<String> favorites = favoritesManager.getFavorites();

        if (favorites.isEmpty()) {
            sender.sendMessage("§e⚠ No tienes plugins favoritos");
            sender.sendMessage("§7Usa §e/phfavorite add <plugin>§7 para agregar");
            return;
        }

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lPlugins Favoritos                  §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        for (int i = 0; i < favorites.size(); i++) {
            String fav = favorites.get(i);
            sender.sendMessage("§7" + (i + 1) + ". §f" + fav + " §7⭐");
        }

        sender.sendMessage("");
        sender.sendMessage("§7Total: §f" + favorites.size() + "§7 favorito(s)");
    }

    private void handleClear(CommandSender sender) {
        List<String> favorites = favoritesManager.getFavorites();
        
        if (favorites.isEmpty()) {
            sender.sendMessage("§e⚠ No tienes plugins favoritos");
            return;
        }

        for (String fav : new ArrayList<>(favorites)) {
            favoritesManager.removeFavorite(fav);
        }

        sender.sendMessage("§a✓ Todos los favoritos han sido eliminados");
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lFavoritos §6- §fAyuda                §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");
        sender.sendMessage("§e/phfavorite add <plugin> §7- Agregar favorito");
        sender.sendMessage("§e/phfavorite remove <plugin> §7- Eliminar favorito");
        sender.sendMessage("§e/phfavorite list §7- Ver favoritos");
        sender.sendMessage("§e/phfavorite clear §7- Limpiar todos");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("add", "remove", "list", "clear").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return favoritesManager.getFavorites().stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
