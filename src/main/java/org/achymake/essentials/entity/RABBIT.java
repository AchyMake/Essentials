package org.achymake.essentials.entity;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class RABBIT {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "entity/" + this.getClass().getSimpleName() + ".yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    private boolean setup() {
        config.options().copyDefaults(true);
        config.set("levels.example-1.chance", -1.0);
        config.set("levels.example-1.health", 20.0);
        config.set("levels.example-1.scale.min", 1.0);
        config.set("levels.example-1.scale.max", 1.25);
        config.set("levels.example-1.exp.min", 1);
        config.set("levels.example-1.exp.max", 2);
        config.set("levels.example-2.chance", -1.0);
        config.set("levels.example-2.health", 20.0);
        config.set("levels.example-2.scale", 1.0);
        config.set("levels.example-2.exp", 1);
        config.set("levels.example-2.main-hand.type", "diamond_sword");
        config.set("levels.example-2.main-hand.amount", 1);
        config.set("levels.example-2.main-hand.enchantments.unbreaking", 1);
        config.set("levels.example-2.main-hand.drop-chance", 0.25);
        config.set("levels.example-2.off-hand.type", "enchanted_book");
        config.set("levels.example-2.off-hand.amount", 1);
        config.set("levels.example-2.off-hand.enchantments.unbreaking", 1);
        config.set("levels.example-2.off-hand.drop-chance", 0.25);
        config.set("levels.example-2.helmet.type", "diamond_helmet");
        config.set("levels.example-2.helmet.amount", 1);
        config.set("levels.example-2.helmet.enchantments.unbreaking", 1);
        config.set("levels.example-2.helmet.drop-chance", 0.25);
        config.set("levels.example-2.chestplate.type", "diamond_chestplate");
        config.set("levels.example-2.chestplate.amount", 1);
        config.set("levels.example-2.chestplate.enchantments.unbreaking", 1);
        config.set("levels.example-2.chestplate.drop-chance", 0.25);
        config.set("levels.example-2.leggings.type", "diamond_leggings");
        config.set("levels.example-2.leggings.amount", 1);
        config.set("levels.example-2.leggings.enchantments.unbreaking", 1);
        config.set("levels.example-2.leggings.drop-chance", 0.25);
        config.set("levels.example-2.boots.type", "diamond_boots");
        config.set("levels.example-2.boots.amount", 1);
        config.set("levels.example-2.boots.enchantments.unbreaking", 1);
        config.set("levels.example-2.boots.drop-chance", 0.25);
        config.set("settings.hostile", false);
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
        config.set("settings.disable-explode", false);
        config.set("settings.disable-interact.FARMLAND", true);
        config.set("settings.disable-interact.TURTLE_EGG", true);
        config.set("settings.disable-interact.SNIFFER_EGG", true);
        config.set("settings.disable-target.VILLAGER", false);
        config.set("settings.disable-hanging-break.ITEM_FRAME", true);
        config.set("settings.disable-hanging-break.GLOW_ITEM_FRAME", true);
        config.set("settings.disable-hanging-break.PAINTING", true);
        config.set("settings.disable-hanging-break.LEASH_KNOT", true);
        config.set("settings.disable-hanging-break.ARMOR_STAND", true);
        for(var reason : getInstance().getEntityHandler().getSpawnReasons()) {
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