package org.achymake.essentials.handlers;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PointsHandler {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata() {
        return getInstance().getUserdata();
    }
    public Set<Map.Entry<OfflinePlayer, Integer>> getTopPoints() {
        var accounts = new HashMap<OfflinePlayer, Integer>();
        for (var offlinePlayer : getInstance().getOfflinePlayers()) {
            if (!getUserdata().isBanned(offlinePlayer) || !getUserdata().isDisabled(offlinePlayer)) {
                if (has(offlinePlayer, 1)) {
                    accounts.put(offlinePlayer, get(offlinePlayer));
                }
            }
        }
        var list = new ArrayList<>(accounts.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        var result = new LinkedHashMap<OfflinePlayer, Integer>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
    public int get(OfflinePlayer offlinePlayer) {
        if (getUserdata().exists(offlinePlayer)) {
            return getUserdata().getConfig(offlinePlayer).getInt("points");
        } else return 0;
    }
    public boolean has(OfflinePlayer offlinePlayer, int amount) {
        return get(offlinePlayer) >= amount;
    }
    public boolean set(OfflinePlayer offlinePlayer, int amount) {
        if (getUserdata().exists(offlinePlayer)) {
            var file = getUserdata().getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("points", amount);
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public boolean add(OfflinePlayer offlinePlayer, int amount) {
        if (getUserdata().exists(offlinePlayer)) {
            var file = getUserdata().getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("points", amount + get(offlinePlayer));
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public boolean remove(OfflinePlayer offlinePlayer, int amount) {
        if (getUserdata().exists(offlinePlayer)) {
            var file = getUserdata().getFile(offlinePlayer);
            var config = YamlConfiguration.loadConfiguration(file);
            config.set("points", get(offlinePlayer) - amount);
            try {
                config.save(file);
                return true;
            } catch (IOException e) {
                getInstance().sendWarning(e.getMessage());
                return false;
            }
        } else return false;
    }
    public String format(int amount) {
        return new DecimalFormat("#,##0").format(amount);
    }
}