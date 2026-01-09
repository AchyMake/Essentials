package org.achymake.essentials.entity;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.EntityHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FIREBALL {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private EntityHandler getEntityHandler() {
        return getInstance().getEntityHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "entity/" + getClass().getSimpleName() + ".yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    private boolean setup() {
        config.options().copyDefaults(true);
        config.set("settings.hostile", true);
        config.set("settings.chunk-limit", -1);
        config.set("settings.disable-spawn", false);
        config.set("settings.disable-block-form", false);
        config.set("settings.disable-change-block", false);
        config.set("settings.disable-entity-damage.ITEM", true);
        config.set("settings.disable-entity-damage.ITEM_FRAME", true);
        config.set("settings.disable-entity-damage.GLOW_ITEM_FRAME", true);
        config.set("settings.disable-entity-damage.PAINTING", true);
        config.set("settings.disable-entity-damage.LEASH_KNOT", true);
        config.set("settings.disable-entity-damage.ARMOR_STAND", true);
        config.set("settings.disable-explode", true);
        config.set("settings.disable-interact.FARMLAND", true);
        config.set("settings.disable-interact.TURTLE_EGG", true);
        config.set("settings.disable-interact.SNIFFER_EGG", true);
        config.set("settings.disable-target.VILLAGER", false);
        config.set("settings.disable-hanging-break.ITEM_FRAME", true);
        config.set("settings.disable-hanging-break.GLOW_ITEM_FRAME", true);
        config.set("settings.disable-hanging-break.PAINTING", true);
        config.set("settings.disable-hanging-break.LEASH_KNOT", true);
        config.set("settings.disable-hanging-break.ARMOR_STAND", true);
        for(var reason : getEntityHandler().getSpawnReasons()) {
            config.set("settings.disable-spawn-reason." + reason.toString(), false);
        }
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    public boolean reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
}