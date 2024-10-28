package org.achymake.essentials.handlers;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class CooldownHandler {
    private final HashMap<String, Long> cooldown = new HashMap<>();
    public boolean has(OfflinePlayer offlinePlayer, String name, int timer) {
        var uuid = offlinePlayer.getUniqueId();
        if (cooldown.containsKey(name + "-" + uuid)) {
            var timeElapsed = System.currentTimeMillis() - cooldown.get(name + "-" + uuid);
            return timeElapsed < Integer.parseInt(timer + "000");
        } else return false;
    }
    public void add(OfflinePlayer offlinePlayer, String name, int timer) {
        var uuid = offlinePlayer.getUniqueId();
        if (cooldown.containsKey(name + "-" + uuid)) {
            var timeElapsed = System.currentTimeMillis() - cooldown.get(name + "-" + uuid);
            if (timeElapsed > Integer.parseInt(timer + "000")) {
                cooldown.put(name + "-" + uuid, System.currentTimeMillis());
            }
        } else cooldown.put(name + "-" + uuid, System.currentTimeMillis());
    }
    public String get(OfflinePlayer offlinePlayer, String name, int timer) {
        var uuid = offlinePlayer.getUniqueId();
        if (cooldown.containsKey(name + "-" + uuid)) {
            var timeElapsed = System.currentTimeMillis() - cooldown.get(name + "-" + uuid);
            var timerResult = Integer.parseInt(timer + "000");
            if (timeElapsed < timerResult) {
                var result = (timerResult - timeElapsed);
                return String.valueOf(result).substring(0, String.valueOf(result).length() - 3);
            } else return "0";
        } else return "0";
    }
    public HashMap<String, Long> getCooldown() {
        return cooldown;
    }
}