package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Userdata;
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
                if (arrow.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target) return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            case Player player -> {
                var userdata = getUserdata(player);
                if (userdata.isDisabled()) {
                    event.setCancelled(true);
                } else if (entity instanceof Player target) {
                    if (player == target)return;
                    var userdataTarget = getUserdata(target);
                    if (!userdata.isPVP()) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                    } else if (!userdataTarget.isPVP()) {
                        event.setCancelled(true);
                        getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                    } else disableTeleport(player);
                }
            }
            case Snowball snowball -> {
                if (snowball.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            case SpectralArrow spectralArrow -> {
                if (spectralArrow.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            case ThrownPotion thrownPotion -> {
                if (thrownPotion.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            case Trident trident -> {
                if (trident.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            case WindCharge windCharge -> {
                if (windCharge.getShooter() instanceof Player player) {
                    var userdata = getUserdata(player);
                    if (userdata.isDisabled()) {
                        event.setCancelled(true);
                    } else if (entity instanceof Player target) {
                        if (player == target)return;
                        var userdataTarget = getUserdata(target);
                        if (!userdata.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but your PVP is Disabled");
                        } else if (!userdataTarget.isPVP()) {
                            event.setCancelled(true);
                            getMessage().sendActionBar(player, "&c&lHey!&7 Sorry but&f " + target.getName() + "&7's PVP is Disabled");
                        } else disableTeleport(player);
                    }
                }
            }
            default -> {
                if (entity instanceof Player player) {
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