package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.plugin.PluginManager;

public class PrepareAnvil implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PrepareAnvil() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        var result = event.getResult();
        if (result == null)return;
        var meta = result.getItemMeta();
        var view = event.getView();
        var player = view.getPlayer();
        var rename = view.getRenameText();
        if (rename != null) {
            if (rename.contains("&") && player.hasPermission("essentials.event.anvil.color")) {
                meta.setDisplayName(getMessage().addColor(rename));
            }
        }
        var secondItem = event.getInventory().getSecondItem();
        if (secondItem != null && secondItem.getType().equals(getMaterials().get("enchanted_book"))) {
            var secondItemMeta = secondItem.getItemMeta();
            if (secondItemMeta.hasEnchants()) {
                secondItemMeta.getEnchants().keySet().forEach(enchantment -> {
                    var bookLevel = secondItemMeta.getEnchants().get(enchantment);
                    var maxLevel = enchantment.getMaxLevel();
                    if (bookLevel > maxLevel) {
                        if (player.hasPermission("essentials.event.anvil.unsafe")) {
                            meta.addEnchant(enchantment, bookLevel, true);
                        } else if (player.hasPermission("essentials.event.anvil.safe")) {
                            meta.addEnchant(enchantment, maxLevel, true);
                        }
                    }
                });
            }
        }
        result.setItemMeta(meta);
        event.setResult(result);
    }
}