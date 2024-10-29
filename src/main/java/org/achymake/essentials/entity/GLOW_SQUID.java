package org.achymake.essentials.entity;

import org.achymake.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GLOW_SQUID {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "entity/" + this.getClass().getSimpleName() + ".yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    private void setup() {
        config.set("hostile", false);
        config.set("chunk-limit", -1);
        config.set("disable-spawn", false);
        config.set("disable-block-form", false);
        config.set("disable-block-damage", false);
        config.set("disable-block-change", false);
        config.set("disable-block-interact.FARMLAND", true);
        config.set("disable-block-interact.TURTLE_EGG", true);
        config.set("disable-block-interact.SNIFFER_EGG", true);
        config.set("disable-target.VILLAGER", false);
        config.set("disable-damage.ITEM", true);
        config.set("disable-damage.ITEM_FRAME", true);
        config.set("disable-damage.GLOW_ITEM_FRAME", true);
        config.set("disable-damage.PAINTING", true);
        config.set("disable-damage.LEASH_KNOT", true);
        config.set("disable-damage.ARMOR_STAND", true);
        config.set("disabled-spawn-reason.BEEHIVE", false);
        config.set("disabled-spawn-reason.BREEDING", false);
        config.set("disabled-spawn-reason.BUILD_IRONGOLEM", false);
        config.set("disabled-spawn-reason.BUILD_SNOWMAN", false);
        config.set("disabled-spawn-reason.BUILD_WITHER", false);
        config.set("disabled-spawn-reason.CHUNK_GEN", false);
        config.set("disabled-spawn-reason.COMMAND", false);
        config.set("disabled-spawn-reason.CURED", false);
        config.set("disabled-spawn-reason.CUSTOM", false);
        config.set("disabled-spawn-reason.DEFAULT", false);
        config.set("disabled-spawn-reason.DISPENSE_EGG", false);
        config.set("disabled-spawn-reason.DROWNED", false);
        config.set("disabled-spawn-reason.DUPLICATION", false);
        config.set("disabled-spawn-reason.EGG", false);
        config.set("disabled-spawn-reason.ENCHANTMENT", false);
        config.set("disabled-spawn-reason.ENDER_PEARL", false);
        config.set("disabled-spawn-reason.EXPLOSION", false);
        config.set("disabled-spawn-reason.FROZEN", false);
        config.set("disabled-spawn-reason.INFECTION", false);
        config.set("disabled-spawn-reason.JOCKEY", false);
        config.set("disabled-spawn-reason.LIGHTING", false);
        config.set("disabled-spawn-reason.METAMORPHOSIS", false);
        config.set("disabled-spawn-reason.MOUNT", false);
        config.set("disabled-spawn-reason.NATURAL", false);
        config.set("disabled-spawn-reason.NETHER_PORTAL", false);
        config.set("disabled-spawn-reason.OCELOT_BABY", false);
        config.set("disabled-spawn-reason.PATROL", false);
        config.set("disabled-spawn-reason.PIGLIN_ZOMBIFIED", false);
        config.set("disabled-spawn-reason.POTION_EFFECT", false);
        config.set("disabled-spawn-reason.RAID", false);
        config.set("disabled-spawn-reason.REINFORCEMENTS", false);
        config.set("disabled-spawn-reason.SHEARED", false);
        config.set("disabled-spawn-reason.SHOULDER_ENTITY", false);
        config.set("disabled-spawn-reason.SILVERFISH_BLOCK", false);
        config.set("disabled-spawn-reason.SLIME_SPLIT", false);
        config.set("disabled-spawn-reason.SPAWNER", false);
        config.set("disabled-spawn-reason.SPAWNER_EGG", false);
        config.set("disabled-spawn-reason.SPELL", false);
        config.set("disabled-spawn-reason.TRAP", false);
        config.set("disabled-spawn-reason.TRIAL_SPAWNER", false);
        config.set("disabled-spawn-reason.VILLAGE_DEFENSE", false);
        config.set("disabled-spawn-reason.VILLAGE_INVASION", false);
        config.options().copyDefaults(true);
        try {
            config.save(file);
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
    public void reload() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else setup();
    }
}