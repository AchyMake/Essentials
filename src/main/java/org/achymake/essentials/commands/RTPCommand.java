package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.CooldownHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RTPCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private CooldownHandler getCooldown() {
        return getInstance().getCooldownHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public RTPCommand() {
        getInstance().getCommand("rtp").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (userdata.isDisabled()) {
                getMessage().send(player, command.getPermissionMessage() + ": " + command.getName());
                return true;
            } else if (args.length == 0) {
                int timer = getInstance().getConfig().getInt("commands.cooldown.rtp");
                if (!getCooldown().has(player, "rtp", timer)) {
                    getCooldown().add(player, "rtp", timer);
                    getMessage().sendActionBar(player, "&6Finding safe locations...");
                    randomTeleport(player);
                } else getMessage().sendActionBar(player, "&cYou have to wait&f " + getCooldown().get(player, "rtp", timer) + "&c seconds");
                return true;
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.rtp.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            getMessage().sendActionBar(player, "&6Finding safe locations...");
                            randomTeleport(player);
                            return true;
                        } else if (!target.hasPermission("essentials.command.rtp.exempt")) {
                            getMessage().sendActionBar(target, "&6Finding safe locations...");
                            randomTeleport(target);
                        } else getMessage().send(player, command.getPermissionMessage());
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    getMessage().sendActionBar(target, "&6Finding safe locations...");
                    randomTeleport(target);
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var commands = new ArrayList<String>();
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.rtp.other")) {
                    getInstance().getOnlinePlayers().forEach(target -> {
                        if (!getUserdata(target).isVanished()) {
                            if (target.getName().startsWith(args[0])) {
                                commands.add(target.getName());
                            }
                        }
                    });
                }
            }
        }
        return commands;
    }
    private Block highestRandomBlock() {
        var worldName = getInstance().getConfig().getString("commands.rtp.world");
        var random = new Random();
        var x = random.nextInt(0, getInstance().getConfig().getInt("commands.rtp.spread"));
        var z = random.nextInt(0, getInstance().getConfig().getInt("commands.rtp.spread"));
        return getInstance().getServer().getWorld(worldName).getHighestBlockAt(x, z);
    }
    private void randomTeleport(Player player) {
        var block = highestRandomBlock();
        if (block.isLiquid()) {
            getMessage().sendActionBar(player, "&cFinding new location due to liquid block");
            randomTeleport(player);
        } else {
            if (!block.getChunk().isLoaded()) {
                block.getChunk().load();
            }
            getMessage().sendActionBar(player, "&6Teleporting to&f random");
            player.teleport(block.getLocation().add(0.5,1,0.5));
        }
    }
}