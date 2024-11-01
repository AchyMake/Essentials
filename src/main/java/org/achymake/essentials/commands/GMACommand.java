package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GMACommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public GMACommand() {
        getInstance().getCommand("gma").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!player.getGameMode().equals(GameMode.ADVENTURE)) {
                    setAdventure(player);
                    return true;
                }
            } else if (args.length == 1) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
                    var target = sender.getServer().getPlayerExact(args[0]);
                    if (target != null) {
                        if (target == player) {
                            if (!target.getGameMode().equals(GameMode.ADVENTURE)) {
                                setAdventure(target);
                            }
                        } else if (!target.hasPermission("essentials.command.gamemode.exempt")) {
                            if (!target.getGameMode().equals(GameMode.ADVENTURE)) {
                                setAdventure(target);
                            }
                        } else player.sendMessage(getMessage().get("commands.gamemode.exempt", target.getName()));
                        return true;
                    }
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                var target = sender.getServer().getPlayerExact(args[0]);
                if (target != null) {
                    if (!target.getGameMode().equals(GameMode.ADVENTURE)) {
                        setAdventure(target);
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
        if (sender instanceof Player player) {
            if (args.length == 1) {
                if (player.hasPermission("essentials.command.gamemode.other")) {
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
    private void setAdventure(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        getMessage().sendActionBar(player, getMessage().get("commands.gamemode.adventure"));
    }
}