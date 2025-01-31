package org.achymake.essentials.providers;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.entity.Player;

public class LuckPermsProvider {
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
    public LuckPerms getLuckPerms() {
        return net.luckperms.api.LuckPermsProvider.get();
    }
}