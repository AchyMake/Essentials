package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.data.Portals;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;

public class PlayerInteract implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    private Portals getPortals() {
        return getInstance().getPortals();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PlayerInteract() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null)return;
        var player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getHand() != EquipmentSlot.HAND)return;
            if (!getUserdata(player).isDisabled()) {
                if (getPortals().hasWand(player.getInventory().getItemInMainHand())) {
                    event.setCancelled(true);
                    getPortals().setSecondary(player.getInventory().getItemInMainHand(), event.getClickedBlock());
                    player.sendMessage(getMessage().get("events.portal.secondary"));
                }
            } else event.setCancelled(true);
        } else if (event.getAction().equals(Action.PHYSICAL)) {
            if (event.getClickedBlock().getType() == getInstance().getMaterialHandler().get("farmland")) {
                if (!getConfig().getBoolean("crops.disable-tramping-farmland"))return;
                event.setCancelled(true);
            } else if (event.getClickedBlock().getType() == getInstance().getMaterialHandler().get("turtle_egg")) {
                if (!getConfig().getBoolean("eggs.disable-tramping-turtle-egg"))return;
                event.setCancelled(true);
            } else if (event.getClickedBlock().getType() == getInstance().getMaterialHandler().get("sniffer_egg")) {
                if (!getConfig().getBoolean("eggs.disable-tramping-sniffer-egg"))return;
                event.setCancelled(true);
            } else if (getUserdata(player).isDisabled() || getUserdata(player).isVanished()) {
                event.setCancelled(true);
            }
        }
    }
}