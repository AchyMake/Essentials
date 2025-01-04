package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class InventoryHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private boolean isBukkit() {
        return getInstance().isBukkit();
    }
    /**
     * open anvil
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openAnvil(Player player) {
        if (!isBukkit()) {
            return player.openAnvil(null, true);
        } else return null;
    }
    /**
     * open cartography
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openCartographyTable(Player player) {
        if (!isBukkit()) {
            return player.openCartographyTable(null, true);
        } else return null;
    }
    /**
     * open enchanting table
     * @param player target
     * @return inventoryView
     * @since many moons ago
     */
    public InventoryView openEnchanting(Player player) {
        return player.openEnchanting(null, true);
    }
    /**
     * open enderchest
     * @param player target
     * @return inventoryView
     * @since many moons ago
     */
    public InventoryView openEnderchest(Player player, Player target) {
        return player.openInventory(target.getEnderChest());
    }
    /**
     * open grindstone
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openGrindstone(Player player) {
        if (!isBukkit()) {
            return player.openGrindstone(null, true);
        } else return null;
    }
    /**
     * open loom
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openLoom(Player player) {
        if (!isBukkit()) {
            return player.openLoom(null, true);
        } else return null;
    }
    /**
     * open smithing table
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openSmithingTable(Player player) {
        if (!isBukkit()) {
            return player.openSmithingTable(null, true);
        } else return null;
    }
    /**
     * open stonecutter
     * @param player target
     * @return inventoryView else null if bukkit
     * @since many moons ago
     */
    public InventoryView openStonecutter(Player player) {
        if (!isBukkit()) {
            return player.openStonecutter(null, true);
        } else return null;
    }
    /**
     * open workbench
     * @param player target
     * @return inventoryView
     * @since many moons ago
     */
    public InventoryView openWorkbench(Player player) {
        return player.openWorkbench(null, true);
    }
}