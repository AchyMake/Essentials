package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.plugin.PluginManager;

public class BlockDropItem implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterialHandler() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockDropItem() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDropItem(BlockDropItemEvent event) {
        var player = event.getPlayer();
        var heldItem = player.getInventory().getItemInMainHand();
        if (getMaterialHandler().isAir(heldItem))return;
        if (!getMaterialHandler().hasEnchantment(heldItem, "smelting_touch"))return;
        for (var item : event.getItems()) {
            var itemStack = item.getItemStack();
            var material = itemStack.getType();
            if (material.equals(getMaterialHandler().get("raw_copper"))) {
                itemStack.setType(Material.COPPER_INGOT);
            } else if (material.equals(getMaterialHandler().get("raw_iron"))) {
                itemStack.setType(Material.IRON_INGOT);
            } else if (material.equals(getMaterialHandler().get("raw_gold"))) {
                itemStack.setType(Material.GOLD_INGOT);
            }
        }
    }
}