package org.achymake.essentials.entity;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PAINTING {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "entity/" + this.getClass().getSimpleName() + ".yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    private boolean setup() {
        config.options().copyDefaults(true);
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
        config.set("settings.disable-spawn-reason.BEEHIVE", false);
        config.set("settings.disable-spawn-reason.BREEDING", false);
        config.set("settings.disable-spawn-reason.BUILD_IRONGOLEM", false);
        config.set("settings.disable-spawn-reason.BUILD_SNOWMAN", false);
        config.set("settings.disable-spawn-reason.BUILD_WITHER", false);
        config.set("settings.disable-spawn-reason.CHUNK_GEN", false);
        config.set("settings.disable-spawn-reason.COMMAND", false);
        config.set("settings.disable-spawn-reason.CURED", false);
        config.set("settings.disable-spawn-reason.CUSTOM", false);
        config.set("settings.disable-spawn-reason.DEFAULT", false);
        config.set("settings.disable-spawn-reason.DISPENSE_EGG", false);
        config.set("settings.disable-spawn-reason.DROWNED", false);
        config.set("settings.disable-spawn-reason.DUPLICATION", false);
        config.set("settings.disable-spawn-reason.EGG", false);
        config.set("settings.disable-spawn-reason.ENCHANTMENT", false);
        config.set("settings.disable-spawn-reason.ENDER_PEARL", false);
        config.set("settings.disable-spawn-reason.EXPLOSION", false);
        config.set("settings.disable-spawn-reason.FROZEN", false);
        config.set("settings.disable-spawn-reason.INFECTION", false);
        config.set("settings.disable-spawn-reason.JOCKEY", false);
        config.set("settings.disable-spawn-reason.LIGHTING", false);
        config.set("settings.disable-spawn-reason.METAMORPHOSIS", false);
        config.set("settings.disable-spawn-reason.MOUNT", false);
        config.set("settings.disable-spawn-reason.NATURAL", false);
        config.set("settings.disable-spawn-reason.NETHER_PORTAL", false);
        config.set("settings.disable-spawn-reason.OCELOT_BABY", false);
        config.set("settings.disable-spawn-reason.PATROL", false);
        config.set("settings.disable-spawn-reason.PIGLIN_ZOMBIFIED", false);
        config.set("settings.disable-spawn-reason.POTION_EFFECT", false);
        config.set("settings.disable-spawn-reason.RAID", false);
        config.set("settings.disable-spawn-reason.REINFORCEMENTS", false);
        config.set("settings.disable-spawn-reason.SHEARED", false);
        config.set("settings.disable-spawn-reason.SHOULDER_ENTITY", false);
        config.set("settings.disable-spawn-reason.SILVERFISH_BLOCK", false);
        config.set("settings.disable-spawn-reason.SLIME_SPLIT", false);
        config.set("settings.disable-spawn-reason.SPAWNER", false);
        config.set("settings.disable-spawn-reason.SPAWNER_EGG", false);
        config.set("settings.disable-spawn-reason.SPELL", false);
        config.set("settings.disable-spawn-reason.TRAP", false);
        config.set("settings.disable-spawn-reason.TRIAL_SPAWNER", false);
        config.set("settings.disable-spawn-reason.VILLAGE_DEFENSE", false);
        config.set("settings.disable-spawn-reason.VILLAGE_INVASION", false);
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