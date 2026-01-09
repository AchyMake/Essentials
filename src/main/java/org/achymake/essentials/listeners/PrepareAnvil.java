package org.achymake.essentials.listeners;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Message;
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
        var anvilView = event.getView();
        var rename = anvilView.getRenameText();
        if (rename == null)return;
        if (!rename.contains("&"))return;
        if (!anvilView.getPlayer().hasPermission("essentials.event.anvil.color"))return;
        meta.setDisplayName(getMessage().addColor(rename));
        result.setItemMeta(meta);
    }
}