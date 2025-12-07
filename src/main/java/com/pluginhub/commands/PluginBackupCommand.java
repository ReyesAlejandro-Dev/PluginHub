package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.BackupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Comando para gestionar backups de plugins
 */
public final class PluginBackupCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final BackupManager backupManager;

    public PluginBackupCommand(PluginHub plugin, BackupManager backupManager) {
        this.plugin = plugin;
        this.backupManager = backupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.backup")) {
            sender.sendMessage("§c✗ No tienes permiso para gestionar backups");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "create":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phbackup create <plugin>");
                    return true;
                }
                handleCreate(sender, args[1]);
                break;

            case "restore":
                if (args.length < 3) {
                    sender.sendMessage("§c✗ Uso: §e/phbackup restore <plugin> <backup>");
                    return true;
                }
                handleRestore(sender, args[1], args[2]);
                break;

            case "list":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phbackup list <plugin>");
                    return true;
                }
                handleList(sender, args[1]);
                break;

            case "delete":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phbackup delete <plugin>");
                    return true;
                }
                handleDelete(sender, args[1]);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void handleCreate(CommandSender sender, String pluginName) {
        sender.sendMessage("§e⏳ Creando backup de §f" + pluginName + "§e...");

        if (backupManager.createBackup(pluginName)) {
            sender.sendMessage("§a✓ Backup creado exitosamente");
            sender.sendMessage("§7Usa §e/phbackup list " + pluginName + "§7 para ver backups");
        } else {
            sender.sendMessage("§c✗ Error creando backup");
            sender.sendMessage("§7Verifica que el plugin esté instalado");
        }
    }

    private void handleRestore(CommandSender sender, String pluginName, String backupName) {
        sender.sendMessage("§e⏳ Restaurando §f" + pluginName + "§e desde backup...");

        if (backupManager.restoreBackup(pluginName, backupName)) {
            sender.sendMessage("§a✓ Plugin restaurado exitosamente");
            sender.sendMessage("");
            sender.sendMessage("§6╔════════════════════════════════════════╗");
            sender.sendMessage("§6║  §e§lImportante                         §6║");
            sender.sendMessage("§6╚════════════════════════════════════════╝");
            sender.sendMessage("§7Reinicia el servidor para aplicar los cambios");
        } else {
            sender.sendMessage("§c✗ Error restaurando backup");
            sender.sendMessage("§7Verifica que el backup exista");
        }
    }

    private void handleList(CommandSender sender, String pluginName) {
        List<String> backups = backupManager.listBackups(pluginName);

        if (backups.isEmpty()) {
            sender.sendMessage("§e⚠ No hay backups para: §f" + pluginName);
            sender.sendMessage("§7Usa §e/phbackup create " + pluginName + "§7 para crear uno");
            return;
        }

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lBackups: §f" + pluginName + "                §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        for (int i = 0; i < backups.size(); i++) {
            String backup = backups.get(i);
            String marker = (i == 0) ? " §a(más reciente)" : "";
            sender.sendMessage("§7" + (i + 1) + ". §f" + backup + marker);
        }

        sender.sendMessage("");
        sender.sendMessage("§7Total: §f" + backups.size() + "§7 backup(s)");
        sender.sendMessage("§7Usa §e/phbackup restore " + pluginName + " <backup>§7 para restaurar");
    }

    private void handleDelete(CommandSender sender, String pluginName) {
        List<String> backups = backupManager.listBackups(pluginName);

        if (backups.isEmpty()) {
            sender.sendMessage("§e⚠ No hay backups para eliminar");
            return;
        }

        if (backupManager.deleteAllBackups(pluginName)) {
            sender.sendMessage("§a✓ Todos los backups de §f" + pluginName + "§a han sido eliminados");
            sender.sendMessage("§7Total eliminado: §f" + backups.size() + "§7 backup(s)");
        } else {
            sender.sendMessage("§c✗ Error eliminando algunos backups");
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lBackups §6- §fAyuda                   §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");
        sender.sendMessage("§e/phbackup create <plugin> §7- Crear backup");
        sender.sendMessage("§e/phbackup restore <plugin> <backup> §7- Restaurar");
        sender.sendMessage("§e/phbackup list <plugin> §7- Ver backups");
        sender.sendMessage("§e/phbackup delete <plugin> §7- Eliminar todos");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "restore", "list", "delete").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
