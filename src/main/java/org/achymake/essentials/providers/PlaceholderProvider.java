package org.achymake.essentials.providers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class PlaceholderProvider extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "essentials";
    }
    @Override
    public String getAuthor() {
        return "AchyMake";
    }
    @Override
    public String getVersion() {
        return Essentials.getInstance().version();
    }
    @Override
    public boolean canRegister() {
        return true;
    }
    @Override
    public boolean register() {
        return super.register();
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        } else {
            var instance = Essentials.getInstance();
            var userdata = instance.getUserdata(player);
            switch (params) {
                case "name" -> {
                    return player.getName();
                }
                case "display_name" -> {
                    return userdata.getDisplayName();
                }
                case "vanished" -> {
                    return String.valueOf(instance.getVanishHandler().getVanished().contains(player));
                }
                case "online_players" -> {
                    return String.valueOf(instance.getServer().getOnlinePlayers().size() - instance.getVanishHandler().getVanished().size());
                }
                case "account" -> {
                    return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(userdata.getAccount());
                }
                case "bank" -> {
                    return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(userdata.getBankAccount());
                }
                case "pvp" -> {
                    return String.valueOf(userdata.isPVP());
                }
                case "max_homes" -> {
                    return String.valueOf(userdata.getMaxHomes());
                }
                case "homes_left" -> {
                    return String.valueOf(userdata.getMaxHomes() - userdata.getHomes().size());
                }
                case "world_name" -> {
                    return player.getWorld().getName();
                }
                case "world_pvp" -> {
                    return String.valueOf(player.getWorld().getPVP());
                }
                case "world_difficulty" -> {
                    return instance.getMessage().toTitleCase(player.getWorld().getDifficulty().toString());
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}