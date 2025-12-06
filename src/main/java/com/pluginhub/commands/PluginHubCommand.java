package com.pluginhub.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginHubCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public PluginHubCommand(JavaPlugin plugin) {
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
                sender.sendMessage("§aPluginHub v" + plugin.getDescription().getVersion());
                break;
            default:
                sender.sendMessage("§c✗ Comando desconocido. Usa /pluginhub help");
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════════╗");
        sender.sendMessage("§6║      PluginHub - Ayuda             ║");
        sender.sendMessage("§6╚════════════════════════════════════╝");
        sender.sendMessage("§e/phsearch <nombre> §7- Buscar un plugin");
        sender.sendMessage("§e/phinstall <nombre> §7- Instalar un plugin");
        sender.sendMessage("§e/phupdate [nombre] §7- Actualizar plugins");
        sender.sendMessage("§e/pluginhub version §7- Ver versión");
    }
}
