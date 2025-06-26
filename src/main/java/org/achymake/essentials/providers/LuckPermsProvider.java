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
        var group = getPlayerGroup(player);
        if (group != null) {
            if (group.getWeight().isEmpty()) {
                return 0;
            } else return group.getWeight().getAsInt();
        } else return 0;
    }
    public int getWeighted(Player player) {
        var listed = new ArrayList<>(getWeightedPlayers());
        for (var i = 0; i < listed.size(); i++) {
            if (listed.get(i).getKey() == player) {
                return i;
            }
        }
        return 0;
    }
    public Group getPlayerGroup(Player player) {
        var groupName = getGroupName(player);
        if (groupName != null) {
            if (getLuckPerms().getGroupManager().isLoaded(groupName)) {
                return getLuckPerms().getGroupManager().getGroup(groupName);
            } else return (Group) getLuckPerms().getGroupManager().loadGroup(groupName);
        } else return null;
    }
    public String getGroupName(Player player) {
        var user = getLuckPerms().getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getPrimaryGroup();
        } else return null;
    }
    public Set<Map.Entry<Player, Integer>> getWeightedPlayers() {
        var weights = new HashMap<Player, Integer>();
        for (var player : getInstance().getOnlinePlayers()) {
            weights.put(player, getWeight(player));
        }
        var list = new ArrayList<>(weights.entrySet());
        list.sort(Map.Entry.comparingByValue());
        var result = new LinkedHashMap<Player, Integer>();
        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (var entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result.entrySet();
    }
    public LuckPerms getLuckPerms() {
        return net.luckperms.api.LuckPermsProvider.get();
    }
}