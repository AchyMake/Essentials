package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
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
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    private final File file = new File(getInstance().getDataFolder(), "tablist.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public FileConfiguration getConfig() {
        return config;
    }
    public boolean isEnable() {
        return config.getBoolean("enable");
    }
    public long getTick() {
        return config.getLong("tick");
    }
    public long getTick(String worldName) {
        return config.getLong("worlds." + worldName + ".tick");
    }
    private boolean hasName() {
        return config.isString("name");
    }
    private boolean hasName(String worldName) {
        return config.isString("worlds." + worldName + ".name");
    }
    private boolean hasHeaderLines() {
        return config.isList("header.lines");
    }
    private boolean hasHeaderLines(String worldName) {
        return config.isList("worlds." + worldName + ".header.lines");
    }
    private boolean hasFooterLines() {
        return config.isList("footer.lines");
    }
    private boolean hasFooterLines(String worldName) {
        return config.isList("worlds." + worldName + ".footer.lines");
    }
    public void apply(Player player) {
        if (isEnable()) {
            var world = player.getWorld().getName();
            if (hasName(world) && hasHeaderLines(world) && hasFooterLines(world)) {
                var taskID = getScheduler().runTimer(new Tab(player), getTick(world), getTick(world)).getTaskId();
                getUserdata().addTaskID(player, "tab", taskID);
            } else if (hasName() && hasHeaderLines() && hasFooterLines()) {
                var taskID = getScheduler().runTimer(new Tab(player), getTick(), getTick()).getTaskId();
                getUserdata().addTaskID(player, "tab", taskID);
            }
        }
    }
    public void disable(Player player) {
        if (getUserdata().hasTaskID(player, "tab")) {
            getUserdata().removeTask(player, "tab");
            player.setPlayerListHeader(null);
            player.setPlayerListName(player.getName());
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
        config.set("name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("header.lines", header);
        config.set("footer.lines", footer);
        config.set("worlds.world.tick", 20);
        config.set("worlds.world.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.world.header.lines", header);
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
        config.set("worlds.test.name", "%vault_prefix%%essentials_display_name%%vault_suffix%");
        config.set("worlds.test.header.lines", testHeader);
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