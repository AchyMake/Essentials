package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class InventoryHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private boolean isSpigot() {
        return getInstance().isBukkit();
    }
    public InventoryView openAnvil(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openAnvil(null, true);
    }
    public InventoryView openCartographyTable(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openCartographyTable(null, true);
    }
    public InventoryView openEnchanting(Player player) {
        return player.openEnchanting(null, true);
    }
    public InventoryView openEnderchest(Player player, Player target) {
        return player.openInventory(target.getEnderChest());
    }
    public InventoryView openGrindstone(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openGrindstone(null, true);
    }
    public InventoryView openLoom(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openLoom(null, true);
    }
    public InventoryView openStonecutter(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openStonecutter(null, true);
    }
    public InventoryView openSmithingTable(Player player) {
        if (isSpigot()) {
            return null;
        } else return player.openSmithingTable(null, true);
    }
    public InventoryView openWorkbench(Player player) {
        return player.openWorkbench(null, true);
    }
}