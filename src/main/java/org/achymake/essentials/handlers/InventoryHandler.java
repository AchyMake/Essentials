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
    public InventoryView openAnvil(Player player) {
        if (!isBukkit()) {
            return player.openAnvil(null, true);
        } else return null;
    }
    public InventoryView openCartographyTable(Player player) {
        if (!isBukkit()) {
            return player.openCartographyTable(null, true);
        } else return null;
    }
    public InventoryView openEnchanting(Player player) {
        return player.openEnchanting(null, true);
    }
    public InventoryView openEnderchest(Player player, Player target) {
        return player.openInventory(target.getEnderChest());
    }
    public InventoryView openGrindstone(Player player) {
        if (!isBukkit()) {
            return player.openGrindstone(null, true);
        } else return null;
    }
    public InventoryView openLoom(Player player) {
        if (!isBukkit()) {
            return player.openLoom(null, true);
        } else return null;
    }
    public InventoryView openSmithingTable(Player player) {
        if (!isBukkit()) {
            return player.openSmithingTable(null, true);
        } else return null;
    }
    public InventoryView openStonecutter(Player player) {
        if (!isBukkit()) {
            return player.openStonecutter(null, true);
        } else return null;
    }
    public InventoryView openWorkbench(Player player) {
        return player.openWorkbench(null, true);
    }
}