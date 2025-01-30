package org.achymake.essentials.data;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.achymake.essentials.Essentials;
import org.achymake.essentials.handlers.MaterialHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class Skulls {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "skulls.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public File getFile() {
        return file;
    }
    public FileConfiguration getConfig() {
        return config;
    }
    /**
     * get listed
     * @return set string
     * @since many moons ago
     */
    public Set<String> getListed() {
        return config.getKeys(false);
    }
    /**
     * is skullName listed
     * @return true if skullName exists else false
     * @since many moons ago
     */
    public boolean isListed(String skullName) {
        return getListed().contains(skullName);
    }
    /**
     * get player head with key
     * @param skullName string
     * @param amount integer
     * @return item stack which is likely the custom player head
     * @since many moons ago
     */
    public ItemStack getCustomHead(String skullName, int amount) {
        var skullItem = getMaterials().getItemStack("player_head", amount);
        var skullMeta = (SkullMeta) skullItem.getItemMeta();
        if (16 >= skullName.length()) {
            var profile = getInstance().getServer().createProfile(UUID.randomUUID(), skullName);
            profile.setProperty(new ProfileProperty("textures", config.getString(skullName)));
            profile.update();
            skullMeta.setPlayerProfile(profile);
            skullItem.setItemMeta(skullMeta);
        }
        return skullItem;
    }
    /**
     * setup
     * @since many moons ago
     */
    private boolean setup() {
        config.set("Chest", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDliMjk4M2MwMWI4ZGE3ZGMxYzBmMTJkMDJjNGFiMjBjZDhlNjg3NWU4ZGY2OWVhZTJhODY3YmFlZTYyMzZkNCJ9fX0=");
        config.set("Barrel", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmE1MDIwY2VjYjAzODg0NzQ2ZWNlNzE2N2E2YWVlOWNiOGM3Y2U1ZDNkYzlkZWNiYzM2OTBiMjIyYzZlMjEyZSJ9fX0=");
        config.set("Raw_Copper", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY3YzVlOGMzYTIwOGRhN2Y3ODBiMzQwY2VmMjI2NDJkNTVlMDA0NzJkMzY5M2IzNDg2ZDcxNDVkNDk5NzBiYiJ9fX0=");
        config.set("Raw_Iron", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE0MmU3ODgyNzU3ZTBmMzNiZjc0NGI2ZTJhYzdlN2I4NWY1ZjFlNmYyZTJjZDY1NThlZjA4NzNiYTQ2YThmMSJ9fX0=");
        config.set("Raw_Gold", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWI2OTk2NzE2M2M3NDNkZGIxZjA4MzU2Njc1NzU3NmI5ZTYzYWMzODBjYzE1MGY1MThiMzNkYzRlOTFlZjcxMiJ9fX0=");
        config.set("Deep_Diamond_Ore", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTgyODUyMmU4NjgwYzU1YjJmZTViOWJjZmM4YWQwNmUzMzIzZjk3ODNkODM1MWNhNTBlZGViNDMxZTYzMTkwZiJ9fX0=");
        config.set("Deep_Emerald_Ore", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE4ZGZjYTY2ZTQ5N2Y2MjQxZGI2YjFmMmRmZTEzNTMwOWE1NjZjZDZjMDc3NWE5Y2E1NWU1ZmFlZTBjNjA1YSJ9fX0=");
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
            return false;
        }
    }
    /**
     * reload skulls.yml
     * @since many moons ago
     */
    public boolean reload() {
        if (getInstance().isBukkit())return false;
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
            return true;
        } else return setup();
    }
}