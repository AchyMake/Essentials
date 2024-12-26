package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.runnable.Tab;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TablistHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "tablist.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public FileConfiguration getConfig() {
        return config;
    }
    public int getInt(String path) {
        return config.getInt(path);
    }
    public boolean isList(String path) {
        return config.isList(path);
    }
    public boolean isSection(String section) {
        return config.isConfigurationSection(section);
    }
    public boolean isEnable() {
        return config.getBoolean("enable");
    }
    public boolean hasTab(Player player) {
        return getInstance().getUserdata().hasTaskID(player, "tab");
    }
    public void apply(Player player) {
        if (isEnable()) {
            var world = player.getWorld().getName();
            if (isSection("worlds." + world + ".header")) {
                if (isList("worlds." + world + ".header.lines") && isList("worlds." + world + ".footer.lines")) {
                    var tick = getInt("worlds." + world + ".tick");
                    var taskID = getScheduler().runTimer(new Tab(player), tick, tick).getTaskId();
                    getInstance().getUserdata().addTaskID(player, "tab", taskID);
                }
            } else if (isList("header.lines") && isList("footer.lines")) {
                var tick = getInt("tick");
                var taskID = getScheduler().runTimer(new Tab(player), tick, tick).getTaskId();
                getInstance().getUserdata().addTaskID(player, "tab", taskID);
            }
        }
    }
    public void disable(Player player) {
        if (getInstance().getUserdata().hasTaskID(player, "tab")) {
            getInstance().getUserdata().removeTask(player, "tab");
            player.setPlayerListHeader(null);
            player.setPlayerListFooter(null);
        }
    }
    private void setup() {
        config.set("enable", false);
        var header = new ArrayList<String>();
        header.add("&6--------&l[&e&lplay.yourserver.org&6&l]&6--------");
        var footer = new ArrayList<String>();
        footer.add("&6--------&l[&f%essentials_online_players%&e/&f%server_max_players%&6&l]&6--------");
        config.set("tick", 20);
        config.set("header.lines", header);
        config.set("name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("footer.lines", footer);
        config.set("worlds.world.tick", 20);
        config.set("worlds.world.header.lines", header);
        config.set("worlds.world.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.world.footer.lines", footer);
        var testHeader = new ArrayList<String>();
        testHeader.add("&6--------&l[&e&lplay.yourserver.org&6&l]&6--------");
        testHeader.add("&etime&f: %server_time_hh mm ss%&e, date&f: %server_time_dd MM yyyy%");
        testHeader.add("&etps&f: %server_tps%&f");
        var testFooter = new ArrayList<String>();
        testFooter.add("&echunks&f: %server_total_chunks%&e, entities&f: %server_total_living_entities%&e/&f%server_total_entities%");
        testFooter.add("&eram&f: %server_ram_used%&e/&f%server_ram_max%");
        testFooter.add("&euptime&f: %server_uptime%");
        testFooter.add("&6--------&l[&f%essentials_online_players%&e/&f%server_max_players%&6&l]&6--------");
        config.set("worlds.test.tick", 20);
        config.set("worlds.test.header.lines", testHeader);
        config.set("worlds.test.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.test.footer.lines", testFooter);
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