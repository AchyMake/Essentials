package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class TPAcceptCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    public TPAcceptCommand() {
        getInstance().getCommand("tpaccept").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().hasTaskID(player, "tpa")) {
                    if (getUserdata().getTpaFrom(player) != null) {
                        var target = getUserdata().getTpaFrom(player).getPlayer();
                        if (target != null) {
                            var tpaTask = getUserdata().getTaskID(target, "tpa");
                            if (getScheduler().isQueued(tpaTask)) {
                                target.sendMessage(getMessage().get("commands.tpaccept.tpa.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.tpaccept.tpa.sender", target.getName()));
                                getMessage().sendActionBar(target, getMessage().get("events.teleport.success", player.getName()));
                                target.teleport(player);
                                getUserdata().setString(target, "tpa.sent", null);
                                getUserdata().setString(player, "tpa.from", null);
                                getUserdata().removeTask(target, "tpa");
                                getUserdata().removeTask(player, "tpa");
                            }
                        }
                    }
                } else if (getUserdata().hasTaskID(player, "tpahere")) {
                    if (getUserdata().getTpaHereFrom(player) != null) {
                        var target = getUserdata().getTpaHereFrom(player).getPlayer();
                        if (target != null) {
                            var tpaHereTask = getUserdata().getTaskID(target, "tpahere");
                            if (getScheduler().isQueued(tpaHereTask)) {
                                target.sendMessage(getMessage().get("commands.tpaccept.tpahere.target", player.getName()));
                                player.sendMessage(getMessage().get("commands.tpaccept.tpahere.sender", target.getName()));
                                getMessage().sendActionBar(player, getMessage().get("events.teleport.success", target.getName()));
                                player.teleport(target);
                                getUserdata().setString(target, "tpahere.sent", null);
                                getUserdata().setString(player, "tpahere.from", null);
                                getUserdata().removeTask(target, "tpahere");
                                getUserdata().removeTask(player, "tpahere");
                            }
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.tpaccept.invalid"));
                return true;
            }
        }
        return false;
    }
    @Override
    public List onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.EMPTY_LIST;
    }
}