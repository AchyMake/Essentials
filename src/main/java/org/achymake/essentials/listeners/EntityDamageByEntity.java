package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;

public class EntityDamageByEntity implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public EntityDamageByEntity() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        var entity = event.getEntity();
        var damager = event.getDamager();
        switch (damager) {
            case Arrow arrow -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (arrow.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case Player player -> {
                if (!getUserdata().isDisabled(player)) {
                    var heldItem = player.getInventory().getItemInMainHand();
                    var test = getConfig().getInt("attack.cooldown." + heldItem.getType().toString().toLowerCase());
                    if (!player.hasCooldown(heldItem)) {
                        if (entity instanceof Player target) {
                            if (!target.getWorld().getPVP())return;
                            if (!getUserdata().isPVP(player)) {
                                event.setCancelled(true);
                                getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                            } else if (!getUserdata().isPVP(target)) {
                                event.setCancelled(true);
                                getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                            } else disableTeleport(target);
                        }
                        if (test > 0) {
                            player.setCooldown(heldItem.getType(), test);
                        }
                    } else if (getConfig().getBoolean("attack.cooldown.enable")) {
                        event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case Snowball snowball -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (snowball.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case SpectralArrow spectralArrow -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (spectralArrow.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case ThrownPotion thrownPotion -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (thrownPotion.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case Trident trident -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (trident.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            case WindCharge windCharge -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (windCharge.getShooter() instanceof Player player) {
                        if (!getUserdata().isDisabled(player)) {
                            if (entity instanceof Player target) {
                                if (!target.getWorld().getPVP())return;
                                if (target == player)return;
                                if (!getUserdata().isPVP(player)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                                } else if (!getUserdata().isPVP(target)) {
                                    event.setCancelled(true);
                                    getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                                } else disableTeleport(target);
                            }
                        } else event.setCancelled(true);
                    }
                } else event.setCancelled(true);
            }
            default -> {
                if (!getEntityHandler().isEntityDamageByEntityDisabled(damager.getType(), entity.getType())) {
                    if (entity instanceof Player player) {
                        disableTeleport(player);
                    }
                } else event.setCancelled(true);
            }
        }
    }
    private void disableTeleport(Player player) {
        if (getConfig().getBoolean("teleport.cancel-on-damage")) {
            if (getUserdata().hasTaskID(player, "teleport")) {
                getMessage().sendActionBar(player, getMessage().get("events.damage"));
                getUserdata().removeTask(player, "teleport");
            }
        }
    }
}