package com.pluginhub.commands;

import com.pluginhub.PluginHub;
import com.pluginhub.managers.ProfileManager;
import com.pluginhub.managers.PluginDownloader;
import com.pluginhub.models.PluginInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Comando para gestionar perfiles de plugins
 */
public final class PluginProfileCommand implements CommandExecutor, TabCompleter {

    private final PluginHub plugin;
    private final ProfileManager profileManager;
    private final PluginDownloader downloader;

    public PluginProfileCommand(PluginHub plugin, ProfileManager profileManager, PluginDownloader downloader) {
        this.plugin = plugin;
        this.profileManager = profileManager;
        this.downloader = downloader;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("pluginhub.profile")) {
            sender.sendMessage("§c✗ No tienes permiso para gestionar perfiles");
            return true;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "list":
                handleList(sender);
                break;

            case "info":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile info <nombre>");
                    return true;
                }
                handleInfo(sender, args[1]);
                break;

            case "create":
                if (args.length < 3) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile create <nombre> <descripción>");
                    return true;
                }
                handleCreate(sender, args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                break;

            case "delete":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile delete <nombre>");
                    return true;
                }
                handleDelete(sender, args[1]);
                break;

            case "install":
                if (args.length < 2) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile install <nombre>");
                    return true;
                }
                handleInstall(sender, args[1]);
                break;

            case "add":
                if (args.length < 3) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile add <perfil> <plugin>");
                    return true;
                }
                handleAddPlugin(sender, args[1], args[2]);
                break;

            case "remove":
                if (args.length < 3) {
                    sender.sendMessage("§c✗ Uso: §e/phprofile remove <perfil> <plugin>");
                    return true;
                }
                handleRemovePlugin(sender, args[1], args[2]);
                break;

            default:
                showHelp(sender);
                break;
        }

        return true;
    }

    private void handleList(CommandSender sender) {
        Set<String> profiles = profileManager.getAllProfiles();

        if (profiles.isEmpty()) {
            sender.sendMessage("§e⚠ No hay perfiles disponibles");
            return;
        }

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lPerfiles Disponibles               §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");

        for (String profile : profiles) {
            String desc = profileManager.getProfileDescription(profile);
            List<String> plugins = profileManager.getProfilePlugins(profile);
            
            sender.sendMessage("§e● §f" + profile);
            sender.sendMessage("§7  " + desc);
            sender.sendMessage("§7  Plugins: §f" + plugins.size());
        }

        sender.sendMessage("");
        sender.sendMessage("§7Usa §e/phprofile info <nombre>§7 para más detalles");
    }

    private void handleInfo(CommandSender sender, String profileName) {
        if (!profileManager.profileExists(profileName)) {
            sender.sendMessage("§c✗ Perfil no encontrado: §f" + profileName);
            return;
        }

        Map<String, Object> info = profileManager.getProfileInfo(profileName);
        List<String> plugins = profileManager.getProfilePlugins(profileName);

        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lPerfil: §f" + profileName + "                §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");
        sender.sendMessage("§7Descripción: §f" + info.get("description"));
        sender.sendMessage("§7Plugins (" + plugins.size() + "):");
        sender.sendMessage("");

        for (int i = 0; i < plugins.size(); i++) {
            sender.sendMessage("§7  " + (i + 1) + ". §f" + plugins.get(i));
        }

        sender.sendMessage("");
        sender.sendMessage("§7Usa §e/phprofile install " + profileName + "§7 para instalar");
    }

    private void handleCreate(CommandSender sender, String name, String description) {
        if (profileManager.createProfile(name, description, new ArrayList<>())) {
            sender.sendMessage("§a✓ Perfil §f" + name + "§a creado");
            sender.sendMessage("§7Usa §e/phprofile add " + name + " <plugin>§7 para agregar plugins");
        } else {
            sender.sendMessage("§c✗ El perfil §f" + name + "§c ya existe");
        }
    }

    private void handleDelete(CommandSender sender, String name) {
        if (profileManager.deleteProfile(name)) {
            sender.sendMessage("§a✓ Perfil §f" + name + "§a eliminado");
        } else {
            sender.sendMessage("§c✗ Perfil no encontrado: §f" + name);
        }
    }

    private void handleInstall(CommandSender sender, String profileName) {
        if (!profileManager.profileExists(profileName)) {
            sender.sendMessage("§c✗ Perfil no encontrado: §f" + profileName);
            return;
        }

        List<String> plugins = profileManager.getProfilePlugins(profileName);

        if (plugins.isEmpty()) {
            sender.sendMessage("§e⚠ El perfil §f" + profileName + "§e no tiene plugins");
            return;
        }

        sender.sendMessage("§e⏳ Instalando perfil §f" + profileName + "§e...");
        sender.sendMessage("§7Plugins a instalar: §f" + plugins.size());
        sender.sendMessage("");

        int installed = 0;
        int failed = 0;

        for (String pluginName : plugins) {
            PluginInfo info = downloader.getPluginInfo(pluginName);
            
            if (info != null) {
                sender.sendMessage("§7Instalando §f" + pluginName + "§7...");
                
                if (downloader.installPluginAsync(info).join()) {
                    installed++;
                    sender.sendMessage("§a  ✓ Instalado");
                } else {
                    failed++;
                    sender.sendMessage("§c  ✗ Error");
                }
            } else {
                failed++;
                sender.sendMessage("§c✗ No se encontró: §f" + pluginName);
            }
        }

        sender.sendMessage("");
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lResumen de Instalación             §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("§a✓ Instalados: §f" + installed);
        sender.sendMessage("§c✗ Fallidos: §f" + failed);
        sender.sendMessage("");
        sender.sendMessage("§7Reinicia el servidor para cargar los plugins");
    }

    private void handleAddPlugin(CommandSender sender, String profileName, String pluginName) {
        if (profileManager.addPluginToProfile(profileName, pluginName)) {
            sender.sendMessage("§a✓ §f" + pluginName + "§a agregado al perfil §f" + profileName);
        } else {
            sender.sendMessage("§c✗ No se pudo agregar (perfil no existe o plugin ya está)");
        }
    }

    private void handleRemovePlugin(CommandSender sender, String profileName, String pluginName) {
        if (profileManager.removePluginFromProfile(profileName, pluginName)) {
            sender.sendMessage("§a✓ §f" + pluginName + "§a eliminado del perfil §f" + profileName);
        } else {
            sender.sendMessage("§c✗ No se pudo eliminar (perfil no existe o plugin no está)");
        }
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6╔════════════════════════════════════════╗");
        sender.sendMessage("§6║  §e§lPerfiles §6- §fAyuda                  §6║");
        sender.sendMessage("§6╚════════════════════════════════════════╝");
        sender.sendMessage("");
        sender.sendMessage("§e/phprofile list §7- Ver perfiles");
        sender.sendMessage("§e/phprofile info <nombre> §7- Ver detalles");
        sender.sendMessage("§e/phprofile create <nombre> <desc> §7- Crear");
        sender.sendMessage("§e/phprofile delete <nombre> §7- Eliminar");
        sender.sendMessage("§e/phprofile install <nombre> §7- Instalar perfil");
        sender.sendMessage("§e/phprofile add <perfil> <plugin> §7- Agregar plugin");
        sender.sendMessage("§e/phprofile remove <perfil> <plugin> §7- Quitar plugin");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("list", "info", "create", "delete", "install", "add", "remove").stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && !args[0].equalsIgnoreCase("create")) {
            return new ArrayList<>(profileManager.getAllProfiles()).stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
