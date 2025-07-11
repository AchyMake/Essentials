package org.achymake.essentials.handlers;

import io.papermc.paper.scoreboard.numbers.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class PaperHandler {
    public InventoryView openAnvil(Player player) {
        return player.openAnvil(null, true);
    }
    public InventoryView openCartographyTable(Player player) {
        return player.openCartographyTable(null, true);
    }
    public InventoryView openGrindstone(Player player) {
        return player.openGrindstone(null, true);
    }
    public InventoryView openLoom(Player player) {
        return player.openLoom(null, true);
    }
    public InventoryView openSmithingTable(Player player) {
        return player.openSmithingTable(null, true);
    }
    public InventoryView openStonecutter(Player player) {
        return player.openStonecutter(null, true);
    }
    public NumberFormat getBlank() {
        return NumberFormat.blank();
    }
}