package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Portals;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PortalCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Portals getPortals() {
        return getInstance().getPortals();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public PortalCommand() {
        getInstance().getCommand("portal").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("wand")) {
                    getPortals().getWand(player);
                    player.sendMessage(getMessage().get("commands.portal.wand"));
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove")) {
                    var idName = args[1];
                    if (getPortals().delPortal(idName)) {
                        player.sendMessage(getMessage().get("commands.portal.remove.success", idName));
                    } else player.sendMessage(getMessage().get("commands.portal.invalid", idName));
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (getPortals().hasWand(player.getInventory().getItemInMainHand())) {
                        var primary = getPortals().getPrimary(player.getInventory().getItemInMainHand());
                        var secondary = getPortals().getSecondary(player.getInventory().getItemInMainHand());
                        var idName = args[2];
                        if (args[1].equalsIgnoreCase("primary")) {
                            if (primary != null) {
                                if (getPortals().exists(idName)) {
                                    getPortals().setPrimary(idName, primary);
                                    player.sendMessage(getMessage().get("commands.portal.set.primary", idName));
                                } else player.sendMessage(getMessage().get("commands.portal.invalid", idName));
                            } else player.sendMessage(getMessage().get("commands.portal.primary.invalid"));
                        } else if (args[1].equalsIgnoreCase("secondary")) {
                            if (secondary != null) {
                                if (getPortals().exists(idName)) {
                                    getPortals().setSecondary(idName, secondary);
                                    player.sendMessage(getMessage().get("commands.portal.set.secondary", idName));
                                } else player.sendMessage(getMessage().get("commands.portal.invalid", idName));
                            } else player.sendMessage(getMessage().get("commands.portal.secondary.invalid"));
                        }
                        return true;
                    }
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (getPortals().hasWand(player.getInventory().getItemInMainHand())) {
                        var idName = args[1];
                        var portalType = args[2];
                        var warp = args[3];
                        var primary = getPortals().getPrimary(player.getInventory().getItemInMainHand());
                        var secondary = getPortals().getSecondary(player.getInventory().getItemInMainHand());
                        if (primary != null) {
                            if (secondary != null) {
                                if (getPortals().createPortal(idName, portalType, warp, primary, secondary)) {
                                    player.sendMessage(getMessage().get("commands.portal.create.success", idName));
                                } else player.sendMessage(getMessage().get("commands.portal.exists", idName));
                            } else player.sendMessage(getMessage().get("commands.portal.secondary.invalid"));
                        } else player.sendMessage(getMessage().get("commands.portal.primary.invalid"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("portal-type")) {
                        var portalType = args[3];
                        getPortals().setPortalType(args[2], portalType.toUpperCase());
                        player.sendMessage(getMessage().get("commands.portal.set.portal-type", getMessage().toTitleCase(portalType), args[2]));
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                commands.add("wand");
                commands.add("create");
                commands.add("remove");
                commands.add("set");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    commands.add("primary");
                    commands.add("secondary");
                    commands.add("portal-type");
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    commands.addAll(getPortals().getPortals().keySet());
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("create")) {
                    commands.addAll(getPortals().getTypes());
                }
                if (args[0].equalsIgnoreCase("set")) {
                    commands.addAll(getPortals().getPortals().keySet());
                }
            } else if (args.length == 4) {
                if (args[0].equalsIgnoreCase("create")) {
                    commands.addAll(getInstance().getWarps().getListed());
                }
            }
        }
        return commands;
    }
}