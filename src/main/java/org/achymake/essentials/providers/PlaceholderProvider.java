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
                case "account" -> {
                    return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(instance.getUserdata().getAccount(player));
                }
                case "bank_name" -> {
                    if (instance.getUserdata().hasBank(player)) {
                        return instance.getUserdata().getBank(player);
                    } else return "None";
                }
                case "bank_account" -> {
                    if (instance.getUserdata().hasBank(player)) {
                        return instance.getEconomyHandler().currency() + instance.getEconomyHandler().format(instance.getBank().get(instance.getUserdata().getBank(player)));
                    } else return "0";
                }
                case "pvp" -> {
                    return String.valueOf(instance.getUserdata().isPVP(player));
                }
                case "homes_max" -> {
                    return String.valueOf(instance.getUserdata().getMaxHomes(player));
                }
                case "homes_size" -> {
                    return String.valueOf(instance.getUserdata().getHomes(player).size());
                }
                case "homes_left" -> {
                    return String.valueOf(instance.getUserdata().getMaxHomes(player) - instance.getUserdata().getHomes(player).size());
                }
                case "vanished" -> {
                    return String.valueOf(instance.getVanishHandler().isVanish(player));
                }
                case "online_players" -> {
                    return String.valueOf(instance.getServer().getOnlinePlayers().size() - instance.getVanishHandler().getVanished().size());
                }
                case "health" -> {
                    return String.valueOf((int) player.getHealth());
                }
                case "walk_speed" -> {
                    return String.valueOf(player.getWalkSpeed());
                }
                case "fly_speed" -> {
                    return String.valueOf(player.getFlySpeed());
                }
                case "is_flying" -> {
                    return String.valueOf(player.isFlying());
                }
                case "is_invulnerable" -> {
                    return String.valueOf(player.isInvulnerable());
                }
                case "has_passenger" -> {
                    return String.valueOf(!player.isEmpty());
                }
                case "is_inside_vehicle" -> {
                    return String.valueOf(player.isInsideVehicle());
                }
                case "is_sleeping" -> {
                    return String.valueOf(player.isSleeping());
                }
                case "is_whitelisted" -> {
                    return String.valueOf(player.isWhitelisted());
                }
                case "is_collidable" -> {
                    return String.valueOf(player.isCollidable());
                }
                case "is_sprinting" -> {
                    return String.valueOf(player.isSprinting());
                }
                case "is_sneaking" -> {
                    return String.valueOf(player.isSneaking());
                }
                case "experience_needed_for_next_level" -> {
                    return String.valueOf(player.getExperiencePointsNeededForNextLevel());
                }
                case "locale" -> {
                    return player.getLocale();
                }
                case "ping" -> {
                    return String.valueOf(player.getPing());
                }
                case "world_name" -> {
                    return player.getWorld().getName();
                }
                case "world_seed" -> {
                    return String.valueOf(player.getWorld().getSeed());
                }
                case "world_environment" -> {
                    return instance.getMessage().toTitleCase(player.getWorld().getEnvironment().toString());
                }
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}