package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.BackupManager;
import com.pluginhub.managers.HistoryManager;
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
 * Comando para actualizar plugins instalados
 */
public final class PluginUpdateCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final PluginDownloader downloader;
    private final HistoryManager historyManager;
    private final BackupManager backupManager;

    public PluginUpdateCommand(PluginHub plugin, PluginDownloader downloader,
                               HistoryManager historyManager, BackupManager backupManager) {
        this.plugin = plugin;
        this.downloader = downloader;
        this.historyManager = historyManager;
        this.backupManager = backupManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.update")) {
            sender.sendMessage("§c✗ No tienes permiso para actualizar plugins");
            sender.sendMessage("§7Permiso requerido: §fpluginhub.update");
            return true;
        }

        if (args.length == 0) {
            // Listar todos los plugins instalados
            listInstalledPlugins(sender);
        } else {
            // Actualizar un plugin específico
            String pluginName = args[0].toLowerCase().trim();
            updateSpecificPlugin(sender, pluginName);
        }

        return true;
    }

    /**
     * Lista todos los plugins instalados
     */
    private void listInstalledPlugins(CommandSender sender) {
        sender.sendMessage("§e⏳ Verificando plugins instalados...");

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            List<String> installedPlugins = downloader.getInstalledPlugins();

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (installedPlugins.isEmpty()) {
                    sender.sendMessage("§c✗ No hay plugins instalados en el servidor");
                    return;
                }

                displayInstalledPlugins(sender, installedPlugins);
            });
        });
    }

    /**
     * Muestra los plugins instalados
     */
    private void displayInstalledPlugins(CommandSender sender, List<String> installedPlugins) {
        sender.sendMessage("§a✓ Plugins instalados (§f" + installedPlugins.size() + "§a):");
        sender.sendMessage("§6════════════════════════════════════════");

        for (String pluginName : installedPlugins) {
            PluginInfo info = downloader.getPluginInfo(pluginName.toLowerCase());
            
            if (info != null) {
                sender.sendMessage("§e● " + pluginName + " §7(v" + info.getVersion() + " disponible)");
                sender.sendMessage("  §9→ /phupdate " + pluginName.toLowerCase());
            } else {
                sender.sendMessage("§e● " + pluginName + " §7(no en repositorio)");
            }
        }

        sender.sendMessage("§6════════════════════════════════════════");
        sender.sendMessage("§7Usa §e/phupdate <nombre>§7 para actualizar un plugin");
        sender.sendMessage("§8Nota: Las actualizaciones automáticas estarán disponibles en v2.0");
    }

    /**
     * Actualiza un plugin específico
     */
    private void updateSpecificPlugin(CommandSender sender, String pluginName) {
        PluginInfo info = downloader.getPluginInfo(pluginName);

        if (info == null) {
            sender.sendMessage("§c✗ Plugin no encontrado en el repositorio: §f" + pluginName);
            sender.sendMessage("§7Intenta buscar con: §e/phsearch " + pluginName);
            return;
        }

        // Verificar si está instalado
        if (!isPluginInstalled(info.getName())) {
            sender.sendMessage("§c✗ El plugin §f" + info.getName() + "§c no está instalado");
            sender.sendMessage("§7Usa §e/phinstall " + pluginName + "§7 para instalarlo");
            return;
        }

        // Crear backup antes de actualizar
        sender.sendMessage("§e⏳ Creando backup antes de actualizar...");
        if (backupManager.createBackup(info.getName())) {
            sender.sendMessage("§a✓ Backup creado exitosamente");
        } else {
            sender.sendMessage("§e⚠ No se pudo crear backup, continuando...");
        }

        sender.sendMessage("§e⏳ Actualizando §f" + info.getName() + "§e a la versión §f" + info.getVersion() + "§e...");
        sender.sendMessage("§7Fuente: §d" + info.getSource().getDisplayName());
        sender.sendMessage("§7Esto puede tardar unos segundos...");

        // Actualización asincrónica (reinstalación)
        downloader.installPluginAsync(info).thenAccept(success -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                if (success) {
                    handleSuccessfulUpdate(sender, info);
                } else {
                    handleFailedUpdate(sender, info);
                }
            });
        }).exceptionally(throwable -> {
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                sender.sendMessage("§c✗ Error crítico durante la actualización");
                plugin.getLogger().severe("Error actualizando " + pluginName + ": " + throwable.getMessage());
            });
            return null;
        });
    }

    /**
     * Maneja una actualización exitosa
     */
    private void handleSuccessfulUpdate(CommandSender sender, PluginInfo info) {
        // Registrar en historial
        String updatedBy = sender.getName();
        historyManager.recordUpdate(info.getName(), "unknown", info.getVersion(), updatedBy);

        sender.sendMessage("§a✓ §f" + info.getName() + "§a actualizado a la versión §f" + info.getVersion());
        sender.sendMessage("");
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lActualización completada            §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("§7Reinicia el servidor para aplicar los cambios");
        sender.sendMessage("§7Comando: §e/reload confirm §7o reinicio completo");
        sender.sendMessage("");
        sender.sendMessage("§7Backup disponible: §e/phbackup list " + info.getName().toLowerCase());
        sender.sendMessage("§7Historial: §e/phhistory " + info.getName().toLowerCase());
    }

    /**
     * Maneja una actualización fallida
     */
    private void handleFailedUpdate(CommandSender sender, PluginInfo info) {
        sender.sendMessage("§c✗ Error durante la actualización de §f" + info.getName());
        sender.sendMessage("");
        sender.sendMessage("§ePosibles causas:");
        sender.sendMessage("§7● Problemas de conexión a internet");
        sender.sendMessage("§7● URL de descarga no disponible");
        sender.sendMessage("§7● Permisos insuficientes en el servidor");
        sender.sendMessage("");
        sender.sendMessage("§7Descarga manual: §9" + info.getSourceUrl());
    }

    /**
     * Verifica si un plugin está instalado
     */
    private boolean isPluginInstalled(String pluginName) {
        return downloader.getInstalledPlugins().stream()
                .anyMatch(installed -> installed.equalsIgnoreCase(pluginName));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Sugerir solo plugins instalados
            return downloader.getInstalledPlugins().stream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
