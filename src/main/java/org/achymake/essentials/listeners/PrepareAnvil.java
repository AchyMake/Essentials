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
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getManager() {
        return getInstance().getManager();
    }
    public PrepareAnvil() {
        getManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        var result = event.getResult();
        if (result == null)return;
        var resultMeta = result.getItemMeta();
        var view = event.getView();
        var player = view.getPlayer();
        var rename = view.getRenameText();
        if (rename != null) {
            if (rename.contains("&")) {
                if (player.hasPermission("essentials.event.anvil.color")) {
                    resultMeta.setDisplayName(getMessage().addColor(rename));
                }
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
                            resultMeta.addEnchant(enchantment, bookLevel, true);
                        } else if (player.hasPermission("essentials.event.anvil.safe")) {
                            resultMeta.addEnchant(enchantment, maxLevel, true);
                        }
                    }
                });
            }
        }
        result.setItemMeta(resultMeta);
        event.setResult(result);
    }
}