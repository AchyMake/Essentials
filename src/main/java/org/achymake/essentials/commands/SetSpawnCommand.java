package org.achymake.essentials.commands;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Spawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor, TabCompleter {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Spawn getSpawn() {
        return getInstance().getSpawn();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    public SetSpawnCommand() {
        getInstance().getCommand("setspawn").setExecutor(this);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (getSpawn().getLocation() != null) {
                    getSpawn().setLocation(player.getLocation());
                    player.sendMessage(getMessage().get("commands.setspawn.relocated"));
                } else {
                    getSpawn().setLocation(player.getLocation());
                    player.sendMessage(getMessage().get("commands.setspawn.set"));
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