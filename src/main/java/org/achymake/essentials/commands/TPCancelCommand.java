package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.OfflinePlayer;
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
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
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
            var userdata = getUserdata(player);
            if (args.length == 0) {
                if (userdata.getTpaSent() != null) {
                    var target = sender.getServer().getPlayer(userdata.getTpaSent().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var tpaTask = userdata.getTaskID("tpa");
                        if (getScheduler().isQueued(tpaTask)) {
                            userdataTarget.setString("tpa.from", null);
                            userdata.setString("tpa.sent", null);
                            userdata.disableTask("tpa");
                            target.sendMessage(getMessage().get("commands.tpcancel.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpcancel.sender"));
                        }
                    }
                } else if (userdata.getTpaHereSent() != null) {
                    var target = sender.getServer().getPlayer(userdata.getTpaHereSent().getUniqueId());
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var tpaHereTask = userdata.getTaskID("tpahere");
                        if (getScheduler().isQueued(tpaHereTask)) {
                            userdataTarget.setString("tpahere.from", null);
                            userdata.setString("tpahere.sent", null);
                            userdata.disableTask("tpahere");
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