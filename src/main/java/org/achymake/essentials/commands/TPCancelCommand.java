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

public class TPCancelCommand implements CommandExecutor, TabCompleter {
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
    public TPCancelCommand() {
        getInstance().getCommand("tpcancel").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getUserdata().getTpaSent(player) != null) {
                    var target = getUserdata().getTpaSent(player).getPlayer();
                    if (target != null) {
                        var tpaTask = getUserdata().getTaskID(player, "tpa");
                        if (getScheduler().isQueued(tpaTask)) {
                            getUserdata().setString(target, "tpa.from", null);
                            getUserdata().setString(player, "tpa.sent", null);
                            getUserdata().removeTask(player, "tpa");
                            target.sendMessage(getMessage().get("commands.tpcancel.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpcancel.sender"));
                        }
                    }
                } else if (getUserdata().getTpaHereSent(player) != null) {
                    var target = getUserdata().getTpaHereSent(player).getPlayer();
                    if (target != null) {
                        var tpaHereTask = getUserdata().getTaskID(player, "tpahere");
                        if (getScheduler().isQueued(tpaHereTask)) {
                            getUserdata().setString(target, "tpahere.from", null);
                            getUserdata().setString(player, "tpahere.sent", null);
                            getUserdata().removeTask(player, "tpahere");
                            target.sendMessage(getMessage().get("commands.tpcancel.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpcancel.sender"));
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.tpcancel.non-requested"));
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