package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.OfflinePlayer;
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
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public EntityDamageByEntity() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        var entity = event.getEntity();
        var damager = event.getDamager();
        switch (damager) {
            case Arrow arrow -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (arrow.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            case Player player -> {
                if (getEntityHandler().disableDamage(player.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (getUserdata(player).isDisabled()) {
                    event.setCancelled(true);
                } else if (entity instanceof Player target) {
                    var userdata = getUserdata(player);
                    if (!target.getWorld().getPVP())return;
                    var userdataTarget = getUserdata(target);
                    if (!userdata.isPVP()) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                    } else if (!userdataTarget.isPVP()) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                    } else disableTeleport(player);
                }
            }
            case Snowball snowball -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (snowball.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            case SpectralArrow spectralArrow -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (spectralArrow.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            case ThrownPotion thrownPotion -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (thrownPotion.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            case Trident trident -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (trident.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            case WindCharge windCharge -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (windCharge.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (!target.getWorld().getPVP())return;
                        if (target == player)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.self"));
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, getMessage().get("events.pvp.target", target.getName()));
                        } else disableTeleport(player);
                    }
                }
            }
            default -> {
                if (getEntityHandler().disableDamage(damager.getType(), entity.getType())) {
                    event.setCancelled(true);
                } else if (entity instanceof Player player) {
                    disableTeleport(player);
                }
            }
        }
    }
    private void disableTeleport(Player player) {
        var userdata = getUserdata(player);
        if (getConfig().getBoolean("teleport.cancel-on-damage")) {
            if (userdata.hasTaskID("teleport")) {
                getMessage().sendActionBar(player, "&cYou moved before teleporting!");
                userdata.disableTask("teleport");
            }
        }
    }
}