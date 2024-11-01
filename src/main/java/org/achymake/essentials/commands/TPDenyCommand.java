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

public class TPDenyCommand implements CommandExecutor, TabCompleter {
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
    public TPDenyCommand() {
        getInstance().getCommand("tpdeny").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            var userdata = getUserdata(player);
            if (args.length == 0) {
                if (userdata.getTpaFrom() != null) {
                    var target = userdata.getTpaFrom().getPlayer();
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var tpaTask = userdataTarget.getTaskID("tpa");
                        if (getScheduler().isQueued(tpaTask)) {
                            getScheduler().cancel(tpaTask);
                            userdata.setString("tpa.from", null);
                            userdataTarget.setString("tpa.sent", null);
                            userdataTarget.disableTask("tpa");
                            target.sendMessage(getMessage().get("commands.tpdeny.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpdeny.sender", target.getName()));
                        }
                    }
                } else if (userdata.getTpaHereFrom() != null) {
                    var target = userdata.getTpaHereFrom().getPlayer();
                    if (target != null) {
                        var userdataTarget = getUserdata(target);
                        var tpaHereTask = userdataTarget.getTaskID("tpahere");
                        if (getScheduler().isQueued(tpaHereTask)) {
                            getScheduler().cancel(tpaHereTask);
                            userdata.setString("tpahere.from", null);
                            userdataTarget.setString("tpahere.sent", null);
                            userdataTarget.disableTask("tpahere");
                            target.sendMessage(getMessage().get("commands.tpdeny.target", player.getName()));
                            player.sendMessage(getMessage().get("commands.tpdeny.sender", target.getName()));
                        }
                    }
                } else player.sendMessage(getMessage().get("commands.tpdeny.non-requested"));
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