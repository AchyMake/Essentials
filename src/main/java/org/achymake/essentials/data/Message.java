package org.achymake.essentials.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.essentials.Essentials;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Message {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public void sendStringList(Player player, List<String> strings) {
        strings.forEach(string -> send(player, string.replaceAll("%player%", player.getName())));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public void sendAll(String message) {
        getInstance().getServer().getOnlinePlayers().forEach(player -> send(player, message));
    }
    public void sendAll(String message, String permission) {
        getInstance().getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission)) {
                send(player, message);
            }
        });
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendStringList(ConsoleCommandSender sender, List<String> strings) {
        strings.forEach(string -> sender.sendMessage(string.replaceAll("%player%", sender.getName())));
    }
    public String getBuilder(String[] args, int value) {
        var stringBuilder = new StringBuilder();
        for(var i = value; i < args.length; i++) {
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().strip();
    }
    public String toTitleCase(String string) {
        if (string.contains(" ")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split(" ")) {
                stringBuilder.append(strings.charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else if (string.contains("_")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split("_")) {
                stringBuilder.append(strings.charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else return string.charAt(0) + string.substring(1).toLowerCase();
    }
    public void sendColorCodes(Player player) {
        player.sendMessage(ChatColor.GOLD + "Minecraft colors:");
        player.sendMessage(ChatColor.BLACK + "&0" + ChatColor.DARK_BLUE + " &1" + ChatColor.DARK_GREEN + " &2" + ChatColor.DARK_AQUA + " &3");
        player.sendMessage(ChatColor.DARK_RED + "&4" + ChatColor.DARK_PURPLE + " &5" + ChatColor.GOLD + " &6" + ChatColor.GRAY + " &7");
        player.sendMessage(ChatColor.DARK_GRAY + "&8" + ChatColor.BLUE + " &9" + ChatColor.GREEN + " &a" + ChatColor.AQUA + " &b");
        player.sendMessage(ChatColor.RED + "&c" + ChatColor.LIGHT_PURPLE + " &d" + ChatColor.YELLOW + " &e");
        player.sendMessage("");
        player.sendMessage("&k" + ChatColor.MAGIC + " magic" + ChatColor.RESET + " &l" + ChatColor.BOLD + " Bold");
        player.sendMessage("&m" + ChatColor.STRIKETHROUGH + " Strike" + ChatColor.RESET + " &n" + ChatColor.UNDERLINE + " Underline");
        player.sendMessage("&o" + ChatColor.ITALIC + " Italic" + ChatColor.RESET + " &r Reset");
    }
}