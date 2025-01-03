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
            switch (params) {
                case "name" -> {
                    return player.getName();
                }
                case "display_name" -> {
                    return instance.getUserdata().getDisplayName(player);
                }
                case "vanished" -> {
                    return String.valueOf(instance.getVanishHandler().getVanished().contains(player));
                }
                case "online_players" -> {
                    return String.valueOf(instance.getServer().getOnlinePlayers().size() - instance.getVanishHandler().getVanished().size());
                }
                case "account" -> {
                    return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(instance.getUserdata().getAccount(player));
                }
                case "bank" -> {
                    if (instance.getUserdata().hasBank(player)) {
                        return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(instance.getBank().get(instance.getUserdata().getBank(player)));
                    } else return "0";
                }
                case "bank_name" -> {
                    if (instance.getUserdata().hasBank(player)) {
                        return instance.getUserdata().getBank(player);
                    } else return "None";
                }
                case "pvp" -> {
                    return String.valueOf(instance.getUserdata().isPVP(player));
                }
                case "max_homes" -> {
                    return String.valueOf(instance.getUserdata().getMaxHomes(player));
                }
                case "homes_left" -> {
                    return String.valueOf(instance.getUserdata().getMaxHomes(player) - instance.getUserdata().getHomes(player).size());
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}