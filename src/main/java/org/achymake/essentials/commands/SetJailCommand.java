package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Jail;
import org.achymake.essentials.data.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetJailCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Jail getJail() {
        return getInstance().getJail();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetJailCommand() {
        getInstance().getCommand("setjail").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getJail().getLocation() != null) {
                    getJail().setLocation(player.getLocation());
                    player.sendMessage(getMessage().get("commands.setjail.relocated"));
                } else {
                    getJail().setLocation(player.getLocation());
                    player.sendMessage(getMessage().get("commands.setjail.set"));
                }
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