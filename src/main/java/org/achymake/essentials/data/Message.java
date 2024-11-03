package org.achymake.essentials.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.achymake.essentials.Essentials;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class Message {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private final File file = new File(getInstance().getDataFolder(), "message.yml");
    private FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    public String get(String path) {
        if (config.isString(path)) {
            return addColor(config.getString(path));
        } else return path + ": is missing a value";
    }
    public String get(String path, String... format) {
        if (config.isString(path)) {
            return addColor(MessageFormat.format(config.getString(path), format));
        } else return path + ": is missing a value";
    }
    private void setup() {
        config.addDefault("error.target.offline", "{0}&c is currently offline");
        config.addDefault("error.target.invalid", "{0}&c has never joined");
        config.addDefault("error.world.invalid", "{0}&c does not exists");
        config.addDefault("error.world.exists", "{0}&c already exists");
        config.addDefault("error.world.folder-exists", "{0}&c folder already exists");
        config.addDefault("error.world.folder-invalid", "{0}&c folder does not exists");
        config.addDefault("error.item.invalid", "&cYou have to hold an item");
        config.addDefault("error.not-provided", "&cServer does not provide this function");
        config.addDefault("creator.post", "{0}&6 is about to be created");
        config.addDefault("creator.title", "{0}&6 has been created with the following:");
        config.addDefault("creator.environment", "&6environment&f: {0}");
        config.addDefault("creator.seed", "&6seed&f: {0}");
        config.addDefault("commands.announcement", "&6Server&f: {0}");
        config.addDefault("commands.anvil.sender", "&6You opened anvil for&f {0}");
        config.addDefault("commands.anvil.exempt", "&cYou are not allowed to open anvil for&f {0}");
        config.addDefault("commands.back.exempt", "&cYou are not allowed to teleport&f {0}&c back");
        config.addDefault("commands.balance.self", "&6Balance&f:&a {0}");
        config.addDefault("commands.balance.top.title", "&6Top 10 Balance:");
        config.addDefault("commands.balance.top.listed", "&6{0}&f {1}&a {2}");
        config.addDefault("commands.ban.success", "&6You banned&f {0}&6 for&f {1} {2}&6 reason&f: {3}");
        config.addDefault("commands.ban.exempt", "&cYou are not allowed to ban&f {0}");
        config.addDefault("commands.ban.banned", "{0}&c is already banned");
        config.addDefault("commands.bank.self", "&6Bank&f:&a {0}");
        config.addDefault("commands.bank.top.title", "&6Top 10 Bank:");
        config.addDefault("commands.bank.top.listed", "&6{0} &f{1} &a{2}");
        config.addDefault("commands.bank.withdraw.success", "&6You withdrew&a {0}&6 from bank");
        config.addDefault("commands.bank.withdraw.left", "&6You now have&a {0}&6 left in the bank");
        config.addDefault("commands.bank.withdraw.insufficient-funds", "&cYou do not have&a {0}&c to withdraw from bank");
        config.addDefault("commands.bank.withdraw.minimum", "&cYou have to withdraw at least&a {0}");
        config.addDefault("commands.bank.deposit.success", "&6You deposit&a {0}&6 to bank");
        config.addDefault("commands.bank.deposit.left", "&6You now have&a {0}&6 left in the bank");
        config.addDefault("commands.bank.deposit.insufficient-funds", "&cYou do not have&a {0}&c to deposit");
        config.addDefault("commands.bank.deposit.minimum", "&cYou have to deposit at least&a {0}");
        config.addDefault("commands.cartography.sender", "&6You opened cartography table for&f {0}");
        config.addDefault("commands.cartography.exempt", "&cYou are not allowed to open cartography table for&f {0}");
        config.addDefault("commands.color.title", "&6Minecraft colors:");
        config.addDefault("commands.color.exempt", "&cYou are not allowed to send color codes for&f {0}");
        config.addDefault("commands.delhome.success", "{0}&6 has been deleted");
        config.addDefault("commands.delhome.invalid", "{0}&c does not exists");
        config.addDefault("commands.delwarp.success", "{0}&6 has been deleted");
        config.addDefault("commands.delwarp.invalid", "{0}&c does not exists");
        config.addDefault("commands.eco.reset", "&6You reset&f {0}&6 account to&a {1}");
        config.addDefault("commands.eco.add", "&6You added&a {0}&6 to&f {1}");
        config.addDefault("commands.eco.remove.success", "&6You removed&a {0}&6 from&f {1}");
        config.addDefault("commands.eco.remove.insufficient-funds", "{0}&c does not have&a {1}");
        config.addDefault("commands.eco.set", "&6You set&a {0}&6 to&f {1}");
        config.addDefault("commands.enchant.add", "&6You added&f {0}&6 with lvl&f {1}");
        config.addDefault("commands.enchant.remove", "&6You removed&f {0}");
        config.addDefault("commands.enchanting.sender", "&6You opened enchanting table for&f {0}");
        config.addDefault("commands.enchanting.exempt", "&cYou are not allowed to open enchanting table for&f {0}");
        config.addDefault("commands.enderchest.title", "{0} Ender Chest");
        config.addDefault("commands.enderchest.exempt", "&cYou are not allowed to open enderchest from&f {0}");
        config.addDefault("commands.entity.hostile", "{0}&6 hostile is now&f {1}");
        config.addDefault("commands.entity.chunk-limit", "{0}&6 chunk-limit is now&f {1}");
        config.addDefault("commands.entity.disable-spawn", "{0}&6 disable-spawn is now&f {1}");
        config.addDefault("commands.entity.disable-block-form", "{0}&6 disable-block-form is now&f {1}");
        config.addDefault("commands.entity.disable-block-damage", "{0}&6 disable-block-damage is now&f {1}");
        config.addDefault("commands.entity.disable-block-change", "{0}&6 disable-block-change is now&f {1}");
        config.addDefault("commands.entity.disable-block-interact", "{0}&6 disable-block-interact&f {1}&6 is now&f {2}");
        config.addDefault("commands.entity.disable-target", "{0}&6 disable-target&f {1}&6 is now&f {2}");
        config.addDefault("commands.entity.disable-damage", "{0}&6 disable-damage&f {1}&6 is now&f {2}");
        config.addDefault("commands.entity.disabled-spawn-reason", "{0}&6 disabled-spawn-reason&f {1}&6 is now&f {2}");
        config.addDefault("commands.feed.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.addDefault("commands.feed.success", "&6Your starvation has been satisfied");
        config.addDefault("commands.feed.sender", "&6You satisfied&f {0}&6 starvation");
        config.addDefault("commands.feed.exempt", "&cYou are not allowed to satisfy&f {0}&6 starvation");
        config.addDefault("commands.fly.enable", "&6&lFly: &aEnabled");
        config.addDefault("commands.fly.disable", "&6&lFly: &cDisabled");
        config.addDefault("commands.fly.target", "{0}&6 {1} fly for you");
        config.addDefault("commands.fly.sender", "&6You {0} fly for&f {1}");
        config.addDefault("commands.fly.exempt", "&cYou are not allowed to toggle fly for&f {0}");
        config.addDefault("commands.flyspeed.self", "&6Your fly speed has changed to&f {0}");
        config.addDefault("commands.flyspeed.target", "{0}&6 changed your flyspeed to&f {1}");
        config.addDefault("commands.flyspeed.sender", "&6You changed&f {0}&6 flyspeed to&f {1}");
        config.addDefault("commands.flyspeed.exempt", "&cYou are not allowed to change flyspeed for&f {1}");
        config.addDefault("commands.freeze.enable", "&6You froze&f {0}");
        config.addDefault("commands.freeze.disable", "&6You unfroze&f {0}");
        config.addDefault("commands.gamemode.adventure", "&6&lGamemode:&f Adventure");
        config.addDefault("commands.gamemode.creative", "&6&lGamemode:&f Creative");
        config.addDefault("commands.gamemode.survival", "&6&lGamemode:&f Survival");
        config.addDefault("commands.gamemode.spectator", "&6&lGamemode:&f Spectator");
        config.addDefault("commands.gamemode.sender", "&6You changed&f {0}&6 gamemode to&f {1}");
        config.addDefault("commands.gamemode.invalid", "{0}&c is not a gamemode");
        config.addDefault("commands.gamemode.exempt", "&cYou are not allowed to change gamemode for&f {0}");
        config.addDefault("commands.give", "&6You gave&f {0} {1} {2}");
        config.addDefault("commands.grindstone.sender", "&6You opened grindstone for&f {0}");
        config.addDefault("commands.grindstone.exempt", "&cYou are not allowed to open grindstone for&f {0}");
        config.addDefault("commands.hat.success", "&6You are now wearing&f {0}");
        config.addDefault("commands.hat.occupied", "&6You are already wearing&f {0}");
        config.addDefault("commands.hat.target-success", "{0}&6 is now wearing&f {1}");
        config.addDefault("commands.hat.target-occupied", "{0}&c is already wearing&f {1}");
        config.addDefault("commands.hat.exempt", "&cYou are not allowed to change hat for&f {0}");
        config.addDefault("commands.heal.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.addDefault("commands.heal.success", "&6Your health has been satisfied");
        config.addDefault("commands.heal.sender", "&6You satisfied&f {0}&6 health");
        config.addDefault("commands.heal.exempt", "&cYou are not allowed to satisfy&f {0}&6 health");
        config.addDefault("commands.help.sender", "&6You sent help message to&f {0}");
        config.addDefault("commands.help.exempt", "&cYou are not allowed to send help message to&f {0}");
        config.addDefault("commands.home.invalid", "{0}&c does not exists");
        config.addDefault("commands.home.has-task", "&cYou cannot teleport twice you have to wait");
        config.addDefault("commands.homes.empty", "&cYou do not have any homes yet");
        config.addDefault("commands.homes.title", "&6Homes:");
        config.addDefault("commands.homes.listed", "- {0}");
        config.addDefault("commands.homes.delete", "&6You deleted&f {0}&6 of&f {1}");
        config.addDefault("commands.homes.invalid", "{0}&c does not have&f {1}&c as home");
        config.addDefault("commands.homes.teleport", "&6Teleporting to&f {0}&6 of&f {1}");
        config.addDefault("commands.information.title", "&6Information:");
        config.addDefault("commands.information.name", "&6name:&f {0}");
        config.addDefault("commands.information.account", "&6account:&f {0}");
        config.addDefault("commands.information.bank", "&6bank:&f {0}");
        config.addDefault("commands.information.homes", "&6homes:&f {0}");
        config.addDefault("commands.information.listed", "- {0}");
        config.addDefault("commands.information.muted", "&6muted:&f {0}");
        config.addDefault("commands.information.frozen", "&6frozen:&f {0}");
        config.addDefault("commands.information.jailed", "&6jailed:&f {0}");
        config.addDefault("commands.information.pvp", "&6pvp:&f {0}");
        config.addDefault("commands.information.banned", "&6banned:&f {0}");
        config.addDefault("commands.information.ban-reason", "&6ban-reason:&f {0}");
        config.addDefault("commands.information.ban-expire", "&6ban-expire:&f {0}");
        config.addDefault("commands.information.vanished", "&6vanished:&f {0}");
        config.addDefault("commands.information.last-online", "&6last-online:&f {0}");
        config.addDefault("commands.information.quit-location", "&6quit-location:&7 World:&f{0}&7 X:&f{1}&7 Y:&f{2}&7 Z:&f{3}");
        config.addDefault("commands.information.uuid", "&6uuid:&f {0}");
        config.addDefault("commands.inventory.self", "&cYou can not open an inventory of your own");
        config.addDefault("commands.inventory.exempt", "&cYou are not allowed to open&f {0}&6 inventory");
        config.addDefault("commands.inventory.title", "{0} inventory");
        config.addDefault("commands.jail.enable", "&6You jailed&f {0}");
        config.addDefault("commands.jail.disable", "&6You freed&f {0}&6 from jail");
        config.addDefault("commands.jail.invalid", "&cJail has not been set");
        config.addDefault("commands.jail.exempt", "&cYou are not allowed to jail&f {0}");
        config.addDefault("commands.kit.title", "&6Kits:");
        config.addDefault("commands.kit.listed", "- {0}");
        config.addDefault("commands.kit.empty", "&cKits are currently empty");
        config.addDefault("commands.kit.received", "&6You received&f {0}&6 kit");
        config.addDefault("commands.kit.non-sufficient-funds", "&cYou do not have&a {0}&c to receive the&f {1}&c kit");
        config.addDefault("commands.kit.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.addDefault("commands.kit.invalid", "{0}&c does not exists");
        config.addDefault("commands.kit.sender", "&6You gave&f {0}&6 kit to&f {1}");
        config.addDefault("commands.kit.exempt", "&cYou are not allowed to give kits to&f {0}");
        config.addDefault("commands.loom.sender", "&6You opened loom for&f {0}");
        config.addDefault("commands.loom.exempt", "&cYou are not allowed to open loom for&f {0}");
        config.addDefault("commands.motd.invalid", "{0}&c does not exist");
        config.addDefault("commands.motd.exempt", "&cYou are not allowed to send motd to&f {0}");
        config.addDefault("commands.mute.enable", "&6You muted&f {0}");
        config.addDefault("commands.mute.disable", "&6You unmuted&f {0}");
        config.addDefault("commands.mute.exempt", "&cYou are not allowed to mute&f {0}");
        config.addDefault("commands.nickname.self", "&6You changed your nickname to&f {0}");
        config.addDefault("commands.nickname.sender", "&6You changed&f {0}&6 nickname to&f {1}");
        config.addDefault("commands.nickname.exempt", "&cYou are not allowed to change nickname for&f {0}");
        config.addDefault("commands.pay.target", "&6You received&a {0}&6 from&f {1}");
        config.addDefault("commands.pay.sender", "&6You paid&a {0}&f {1}");
        config.addDefault("commands.pay.insufficient-funds", "&cYou do not have&a {0}&c to pay&f {1}");
        config.addDefault("commands.pay.minimum-payment", "&cYou have to at least pay&a {0}");
        config.addDefault("commands.pay.self", "&cYou can not pay your self");
        config.addDefault("commands.portal.wand", "&6You received portal wand");
        config.addDefault("commands.portal.remove.success", "{0}&6 has been removed");
        config.addDefault("commands.portal.invalid", "{0}&c does not exist");
        config.addDefault("commands.portal.exists", "{0}&c already exist");
        config.addDefault("commands.portal.primary.invalid", "&cYou have to add primary location");
        config.addDefault("commands.portal.secondary.invalid", "&cYou have to add secondary location");
        config.addDefault("commands.portal.set.primary", "&6You resized primary location for&f {0}&6 portal");
        config.addDefault("commands.portal.set.secondary", "&6You resized primary location for&f {0}&6 portal");
        config.addDefault("commands.portal.set.portal-type", "&6You changed portal-type to&f {0}&6for portal&f {1}");
        config.addDefault("commands.portal.create.success", "&6You created a portal named&f {0}");
        config.addDefault("commands.pvp.enable", "&6&lPVP:&a Enable");
        config.addDefault("commands.pvp.disable", "&6&lPVP:&c Disable");
        config.addDefault("commands.pvp.sender", "&6You {0} pvp for&f {1}");
        config.addDefault("commands.pvp.exempt", "&cYou are not allowed to toggle pvp for&f {0}");
        config.addDefault("commands.repair.damaged", "&6You repaired&f {0}");
        config.addDefault("commands.repair.non-damaged", "{0}&c is already fully repaired");
        config.addDefault("commands.repair.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.addDefault("commands.respond.target", "&7{0} > You&f: {1}");
        config.addDefault("commands.respond.sender", "&7You > {0}&f: {1}");
        config.addDefault("commands.respond.notify", "&7{0} > {1}&f: {2}");
        config.addDefault("commands.respond.invalid", "&cYou do not have a recent whisper");
        config.addDefault("commands.rtp.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.addDefault("commands.rtp.sender", "&6You teleported&f {0}&6 to a random location");
        config.addDefault("commands.rtp.exempt", "&cYou are not allowed to rtp&f {0}");
        config.addDefault("commands.rtp.post-teleport", "&6Finding locations...");
        config.addDefault("commands.rtp.liquid", "&cFinding new location due to liquid block");
        config.addDefault("commands.rtp.teleport", "&6Teleporting to&f random");
        config.addDefault("commands.rules.sender", "&6You sent rules message to&f {0}");
        config.addDefault("commands.rules.exempt", "&cYou are not allowed to send rules to&f {0}");
        config.addDefault("commands.sell.sellable", "&6You sold&f {0} {1}&6 for&a {2}");
        config.addDefault("commands.sell.unsellable", "{0}&c is not able to sell");
        config.addDefault("commands.sell.insufficient", "&cYou do not have enough&f {0}");
        config.addDefault("commands.sethome.success", "{0}&6 has been set");
        config.addDefault("commands.sethome.limit-reached", "&cYou have reached your limit of&f {0}&c homes");
        config.addDefault("commands.sethome.bed", "&cYou can not sethome for&f bed");
        config.addDefault("commands.setjail.relocated", "&6Jail has been relocated");
        config.addDefault("commands.setjail.set", "&6Jail has been set");
        config.addDefault("commands.setspawn.relocated", "&6Spawn has been relocated");
        config.addDefault("commands.setspawn.set", "&6Spawn has been set");
        config.addDefault("commands.setwarp.relocated", "{0}&6 has been relocated");
        config.addDefault("commands.setwarp.set", "{0}&6 has been set");
        config.addDefault("commands.setworth.enable", "{0}&6 is now&a {1}");
        config.addDefault("commands.setworth.disable", "{0}&6 is now worthless");
        config.addDefault("commands.skull.received", "&6You received&f {0}&6 skull");
        config.addDefault("commands.skull.sender", "&6You gave&f {0}&6 a&f {1}&6 skull");
        config.addDefault("commands.skull.invalid", "{0}&c does not exist");
        config.addDefault("commands.smithing.sender", "&6You opened smithing table for&f {0}");
        config.addDefault("commands.spawn.invalid", "&cSpawn has not been set");
        config.addDefault("commands.spawn.exempt", "&cYou are not allowed to tp&f {0}&c to spawn");
        config.addDefault("commands.stonecutter.sender", "&6You opened stonecutter for&f {0}");
        config.addDefault("commands.stonecutter.exempt", "&cYou are not allowed to open stonecutter for&f {0}");
        config.addDefault("commands.store.sender", "&6You sent store message to&f {0}");
        config.addDefault("commands.store.exempt", "&cYou are not allowed to send store to&f {0}");
        config.addDefault("commands.tpaccept.tpa.target", "{0}&6 accepted tpa request");
        config.addDefault("commands.tpaccept.tpa.sender", "&6You accepted tpa request from&f {0}");
        config.addDefault("commands.tpaccept.tpahere.target", "{0}&6 accepted tpahere request");
        config.addDefault("commands.tpaccept.tpahere.sender", "&6You accepted tpahere request from&f {0}");
        config.addDefault("commands.tpaccept.non-requests", "&cYou do not have any tpa requests");
        config.addDefault("commands.tpa.expired", "&cTeleport request has been expired");
        config.addDefault("commands.tpa.target.notify", "{0} has sent you a tpa request");
        config.addDefault("commands.tpa.target.decide", "&6You can type&a /tpaccept&6 or&c /tpdeny");
        config.addDefault("commands.tpa.sender.notify", "&6You sent a tpa request to&f {0}");
        config.addDefault("commands.tpa.sender.decide", "&6You can type&c /tpcancel&6 to cancel tpa request");
        config.addDefault("commands.tpa.occupied", "&cYou already sent a tpa request");
        config.addDefault("commands.tpa.request-self", "&cYou can not send tpa request to your self");
        config.addDefault("commands.tpahere.expired", "&cTeleport request has been expired");
        config.addDefault("commands.tpahere.target.notify", "{0}&6 has sent you a tpahere request");
        config.addDefault("commands.tpahere.target.decide", "&6You can type&a /tpaccept&6 or&c /tpdeny");
        config.addDefault("commands.tpahere.sender.notify", "&6You have sent a tpa request to&f {0}");
        config.addDefault("commands.tpahere.sender.decide", "&6You can type&c /tpcancel&6 to cancel the request");
        config.addDefault("commands.tpahere.occupied", "&cYou already sent a tpahere request");
        config.addDefault("commands.tpahere.request-self", "&cYou can not send tpahere request to your self");
        config.addDefault("commands.tpcancel.target", "{0}&6 cancelled teleport request");
        config.addDefault("commands.tpcancel.sender", "&6You cancelled teleport request");
        config.addDefault("commands.tpcancel.non-requested", "&cYou have not sent any teleport request");
        config.addDefault("commands.tp.exempt", "&cYou are not allowed to teleport to&f {0}");
        config.addDefault("commands.tpdeny.target", "{0}&6 denied teleport request");
        config.addDefault("commands.tpdeny.sender", "&6You denied teleport request from&f {0}");
        config.addDefault("commands.tpdeny.non-requested", "&cYou do not have any teleport request");
        config.addDefault("commands.tphere.sender", "&6Teleporting&f {0}&6 to you");
        config.addDefault("commands.tphere.exempt", "&cYou are not allowed to teleport&f {0}");
        config.addDefault("commands.unban.banned", "{0}&6 is no longer banned");
        config.addDefault("commands.unban.unbanned", "{0}&c is not banned");
        config.addDefault("commands.vanish.enable", "{0}&6 is now vanished");
        config.addDefault("commands.vanish.disable", "{0}&6 is no longer vanished");
        config.addDefault("commands.vanish.exempt", "&cYou are not allowed to toggle vanish for&f {0}");
        config.addDefault("commands.walkspeed.changed", "&6You changed your walkspeed to&f {0}");
        config.addDefault("commands.walkspeed.sender", "&6You changed&f {0}&6 walkspeed to&f {1}");
        config.addDefault("commands.walkspeed.exempt", "&cYou are not allowed to change walkspeed for&f {0}");
        config.addDefault("commands.warp.title", "&6Warps:");
        config.addDefault("commands.warp.listed", "- {0}");
        config.addDefault("commands.warp.empty", "&cWarps are currently empty");
        config.addDefault("commands.warp.invalid", "{0}&c does not exists");
        config.addDefault("commands.warp.exempt", "&cYou are not allowed to warp&f {0}");
        config.addDefault("commands.whisper.target", "&7{0} > You&f: {1}");
        config.addDefault("commands.whisper.sender", "&7You > {0}&f: {1}");
        config.addDefault("commands.whisper.notify", "&7{0} > {1}&f: {2}");
        config.addDefault("commands.workbench.sender", "&6You opened workbench for&f {0}");
        config.addDefault("commands.workbench.exempt", "&cYou are not allowed to open workbench for&f {0}");
        config.addDefault("commands.world.setspawn",  "{0}&6 changed spawn point");
        config.addDefault("commands.world.pvp.enable", "{0}&6 is now pvp mode");
        config.addDefault("commands.world.pvp.disable", "{0}&6 is no longer pvp mode");
        config.addDefault("commands.world.remove", "{0}&6 is saved and removed");
        config.addDefault("commands.world.gamerule.changed", "{0}&6 changed&f {1}&6 to&f {2}");
        config.addDefault("commands.worth.listed", "{0}&6 is worth&a {1}");
        config.addDefault("commands.worth.unlisted", "{0}&c is not sellable");
        config.addDefault("events.vanish.enable", "&6&lVanish&f:&a Enable");
        config.addDefault("events.vanish.disable", "&6&lVanish&f:&c Disable");
        config.addDefault("events.portal.secondary", "&6Secondary location saved");
        config.addDefault("events.portal.primary", "&6Primary location saved");
        config.addDefault("events.teleport.post", "&6Teleporting in&f {0}&6 seconds");
        config.addDefault("events.teleport.success", "&6Teleporting to&f {0}");
        config.addDefault("events.teleport.has-task", "&cYou can not teleport twice you have to wait");
        config.addDefault("events.pvp.self", "&cYour PVP is Disabled");
        config.addDefault("events.pvp.target", "{0}&c PVP is Disabled");
        config.addDefault("events.death", "&cYou lost&a {0}&c {1}");
        config.addDefault("events.gamemode.change", "&6Your gamemode has changed to&f {0}");
        config.addDefault("events.gamemode.notify", "{0}&6 changed gamemode to&f {1}");
        config.addDefault("events.move", "&cYou moved before teleporting!");
        config.addDefault("events.damage", "&cYou got damaged before teleporting!");
        config.addDefault("events.respawn.title", "&6Death location&f:");
        config.addDefault("events.respawn.location", "&6World&f: {0}&6 X&f: {1}&6 Y&f: {2}&6 Z&f: {3}");
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
    public void send(Player player, String message) {
        player.sendMessage(addColor(message));
    }
    public void sendStringList(Player player, List<String> strings) {
        strings.forEach(string -> send(player, string.replaceAll("%player%", player.getName())));
    }
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public void sendAll(String message) {
        getInstance().getOnlinePlayers().forEach(player -> player.sendMessage(addColor(message)));
    }
    public void sendAll(String message, String permission) {
        getInstance().getOnlinePlayers().forEach(player -> {
            if (player.hasPermission(permission)) {
                player.sendMessage(addColor(message));
            }
        });
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendStringList(ConsoleCommandSender sender, List<String> strings) {
        strings.forEach(string -> sender.sendMessage(string.replaceAll("%player%", sender.getName())));
    }
    public String getBuilder(String[] args, int value) {
        var stringBuilder = new StringBuilder();
        for(var i = value; i < args.length; i++) {
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().strip();
    }
    public String toTitleCase(String string) {
        if (string.contains(" ")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split(" ")) {
                stringBuilder.append(strings.toUpperCase().charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else if (string.contains("_")) {
            var stringBuilder = new StringBuilder();
            for (var strings : string.split("_")) {
                stringBuilder.append(strings.toUpperCase().charAt(0) + strings.substring(1).toLowerCase());
                stringBuilder.append(" ");
            }
            return stringBuilder.toString().strip();
        } else return string.toUpperCase().charAt(0) + string.substring(1).toLowerCase();
    }
}