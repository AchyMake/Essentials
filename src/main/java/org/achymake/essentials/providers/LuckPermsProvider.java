package org.achymake.essentials.providers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.achymake.essentials.Essentials;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LuckPermsProvider {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    public int getWeight(Player player) {
        if (getPlayerGroup(player) != null) {
            return getPlayerGroup(player).getWeight().getAsInt();
        } else return 0;
    }
    public Group getPlayerGroup(Player player) {
        getLuckPerms().getGroupManager().loadAllGroups();
        var group = getGroup(player);
        if (group != null) {
            if (getLuckPerms().getGroupManager().isLoaded(group)) {
                return getLuckPerms().getGroupManager().getGroup(group);
            } else {
                return (Group) getLuckPerms().getGroupManager().loadGroup(group);
            }
        } else return null;
    }
    public String getGroup(Player player) {
        var user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getPrimaryGroup();
        } else return null;
    }
    public Set<Map.Entry<Player, Integer>> getWeightedPlayers() {
        var accounts = new HashMap<Player, Integer>();
        for (var player : getInstance().getOnlinePlayers()) {
            accounts.put(player, getWeight(player));
        }
        var list = new ArrayList<>(accounts.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        var result = new LinkedHashMap<Player, Integer>();
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return result.entrySet();
    }
    public LuckPerms getLuckPerms() {
        return net.luckperms.api.LuckPermsProvider.get();
    }
}