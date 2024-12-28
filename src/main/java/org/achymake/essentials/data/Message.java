package org.achymake.essentials.data;

import me.clip.placeholderapi.PlaceholderAPI;
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
        config.options().copyDefaults(true);
        config.set("enable", "&aEnable");
        config.set("disable", "&cDisable");
        config.set("gamemode.adventure", "Adventure");
        config.set("gamemode.creative", "Creative");
        config.set("gamemode.spectator", "Spectator");
        config.set("gamemode.survival", "Survival");
        config.set("gamemode.change", "&6Gamemode&f: {0}");
        config.set("gamemode.invalid", "{0}&c is not a gamemode");
        config.set("error.target.offline", "{0}&c is currently offline");
        config.set("error.target.invalid", "{0}&c has never joined");
        config.set("error.world.invalid", "{0}&c does not exists");
        config.set("error.item.invalid", "&cYou have to hold an item");
        config.set("error.not-provided", "&cServer does not provide this function");
        config.set("commands.announcement", "&6Server&f: {0}");
        config.set("commands.anvil.sender", "&6You opened anvil for&f {0}");
        config.set("commands.anvil.exempt", "&cYou are not allowed to open anvil for&f {0}");
        config.set("commands.back.exempt", "&cYou are not allowed to teleport&f {0}&c back");
        config.set("commands.balance.self", "&6Balance&f:&a {0}");
        config.set("commands.balance.top.title", "&6Top 10 Balance:");
        config.set("commands.balance.top.listed", "&6{0}&f {1}&a {2}");
        config.set("commands.ban.success", "&6You banned&f {0}&6 for&f {1} {2}&6 reason&f: {3}");
        config.set("commands.ban.exempt", "&cYou are not allowed to ban&f {0}");
        config.set("commands.ban.banned", "{0}&c is already banned");
        config.set("commands.bank.self", "&6Bank&f:&a {0}");
        config.set("commands.bank.top.title", "&6Top 10 Bank:");
        config.set("commands.bank.top.listed", "&6{0} &f{1} &a{2}");
        config.set("commands.bank.withdraw.success", "&6You withdrew&a {0}&6 from bank");
        config.set("commands.bank.withdraw.left", "&6You now have&a {0}&6 left in the bank");
        config.set("commands.bank.withdraw.insufficient-funds", "&cYou do not have&a {0}&c to withdraw from bank");
        config.set("commands.bank.withdraw.minimum", "&cYou have to withdraw at least&a {0}");
        config.set("commands.bank.deposit.success", "&6You deposit&a {0}&6 to bank");
        config.set("commands.bank.deposit.left", "&6You now have&a {0}&6 left in the bank");
        config.set("commands.bank.deposit.insufficient-funds", "&cYou do not have&a {0}&c to deposit");
        config.set("commands.bank.deposit.minimum", "&cYou have to deposit at least&a {0}");
        config.set("commands.board.self", "&6Board is now {0}");
        config.set("commands.board.other", "{0}&6 board is now {1}");
        config.set("commands.board.exempt", "&cYou are not allowed to toggle board for&f {0}");
        config.set("commands.cartography.sender", "&6You opened cartography table for&f {0}");
        config.set("commands.cartography.exempt", "&cYou are not allowed to open cartography table for&f {0}");
        config.set("commands.color.title", "&6Minecraft colors:");
        config.set("commands.color.exempt", "&cYou are not allowed to send color codes for&f {0}");
        config.set("commands.delhome.success", "{0}&6 has been deleted");
        config.set("commands.delhome.invalid", "{0}&c does not exists");
        config.set("commands.delwarp.success", "{0}&6 has been deleted");
        config.set("commands.delwarp.invalid", "{0}&c does not exists");
        config.set("commands.eco.reset", "&6You reset&f {0}&6 account to&a {1}");
        config.set("commands.eco.add", "&6You added&a {0}&6 to&f {1}");
        config.set("commands.eco.remove.success", "&6You removed&a {0}&6 from&f {1}");
        config.set("commands.eco.remove.insufficient-funds", "{0}&c does not have&a {1}");
        config.set("commands.eco.set", "&6You set&a {0}&6 to&f {1}");
        config.set("commands.enchant.add", "&6You added&f {0}&6 with lvl&f {1}");
        config.set("commands.enchant.remove", "&6You removed&f {0}");
        config.set("commands.enchanting.sender", "&6You opened enchanting table for&f {0}");
        config.set("commands.enchanting.exempt", "&cYou are not allowed to open enchanting table for&f {0}");
        config.set("commands.enderchest.title", "{0} Ender Chest");
        config.set("commands.enderchest.exempt", "&cYou are not allowed to open enderchest from&f {0}");
        config.set("commands.entity.hostile", "{0}&6 hostile is now&f {1}");
        config.set("commands.entity.chunk-limit", "{0}&6 chunk-limit is now&f {1}");
        config.set("commands.entity.disable-spawn", "{0}&6 disable-spawn is now&f {1}");
        config.set("commands.entity.disable-block-form", "{0}&6 disable-block-form is now&f {1}");
        config.set("commands.entity.disable-explode", "{0}&6 disable-explode is now&f {1}");
        config.set("commands.entity.disable-change-block", "{0}&6 disable-change-block is now&f {1}");
        config.set("commands.entity.disable-interact", "{0}&6 disable-interact&f {1}&6 is now&f {2}");
        config.set("commands.entity.disable-target", "{0}&6 disable-target&f {1}&6 is now&f {2}");
        config.set("commands.entity.disable-entity-damage", "{0}&6 disable-damage&f {1}&6 is now&f {2}");
        config.set("commands.entity.disable-hanging-break", "{0}&6 disable-hanging-break&f {1}&6 is now&f {2}");
        config.set("commands.entity.disable-spawn-reason", "{0}&6 disable-spawn-reason&f {1}&6 is now&f {2}");
        config.set("commands.exp.self", "&6Your exp is&f {0}");
        config.set("commands.exp.other", "{0}&6 exp is&f {1}");
        config.set("commands.exp.add", "&6You added&f {0}&6 exp to&f {1}");
        config.set("commands.exp.remove", "&6You removed&f {0}&6 exp to&f {1}");
        config.set("commands.exp.set", "&6You set&f {0}&6 exp to&f {1}");
        config.set("commands.feed.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.set("commands.feed.success", "&6Your starvation has been satisfied");
        config.set("commands.feed.sender", "&6You satisfied&f {0}&6 starvation");
        config.set("commands.feed.exempt", "&cYou are not allowed to satisfy&f {0}&6 starvation");
        config.set("commands.fly.self", "&6&lFly: {0}");
        config.set("commands.fly.sender", "&6You {1} fly for&f {0}");
        config.set("commands.fly.exempt", "&cYou are not allowed to toggle fly for&f {0}");
        config.set("commands.flyspeed.self", "&6Your fly speed has changed to&f {0}");
        config.set("commands.flyspeed.target", "{0}&6 changed your flyspeed to&f {1}");
        config.set("commands.flyspeed.sender", "&6You changed&f {0}&6 flyspeed to&f {1}");
        config.set("commands.flyspeed.exempt", "&cYou are not allowed to change flyspeed for&f {1}");
        config.set("commands.freeze.enable", "&6You froze&f {0}");
        config.set("commands.freeze.disable", "&6You unfroze&f {0}");
        config.set("commands.freeze.exempt", "&cYou are not allowed to freeze&f {0}");
        config.set("commands.gamemode.sender", "&6You changed&f {0}&6 gamemode to&f {1}");
        config.set("commands.gamemode.invalid", "{0}&c is not a gamemode");
        config.set("commands.gamemode.exempt", "&cYou are not allowed to change gamemode for&f {0}");
        config.set("commands.give", "&6You gave&f {0} {1} {2}");
        config.set("commands.grindstone.sender", "&6You opened grindstone for&f {0}");
        config.set("commands.grindstone.exempt", "&cYou are not allowed to open grindstone for&f {0}");
        config.set("commands.hat.success", "&6You are now wearing&f {0}");
        config.set("commands.hat.occupied", "&6You are already wearing&f {0}");
        config.set("commands.hat.target.success", "{0}&6 is now wearing&f {1}");
        config.set("commands.hat.target.occupied", "{0}&c is already wearing&f {1}");
        config.set("commands.hat.exempt", "&cYou are not allowed to change hat for&f {0}");
        config.set("commands.heal.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.set("commands.heal.success", "&6Your health has been satisfied");
        config.set("commands.heal.sender", "&6You satisfied&f {0}&6 health");
        config.set("commands.heal.exempt", "&cYou are not allowed to satisfy&f {0}&6 health");
        config.set("commands.help.sender", "&6You sent help message to&f {0}");
        config.set("commands.help.exempt", "&cYou are not allowed to send help message to&f {0}");
        config.set("commands.home.invalid", "{0}&c does not exists");
        config.set("commands.homes.empty", "&cYou do not have any homes yet");
        config.set("commands.homes.title", "&6Homes:");
        config.set("commands.homes.listed", "- {0}");
        config.set("commands.homes.delete", "&6You deleted&f {0}&6 of&f {1}");
        config.set("commands.homes.invalid", "{0}&c does not have&f {1}&c as home");
        config.set("commands.homes.teleport", "&6Teleporting to&f {0}&6 of&f {1}");
        config.set("commands.information.title", "&6Information:");
        config.set("commands.information.name", "&6name:&f {0}");
        config.set("commands.information.account", "&6account:&f {0}");
        config.set("commands.information.bank", "&6bank:&f {0}");
        config.set("commands.information.homes", "&6homes:&f {0}");
        config.set("commands.information.listed", "- {0}");
        config.set("commands.information.muted", "&6muted:&f {0}");
        config.set("commands.information.frozen", "&6frozen:&f {0}");
        config.set("commands.information.jailed", "&6jailed:&f {0}");
        config.set("commands.information.pvp", "&6pvp:&f {0}");
        config.set("commands.information.banned", "&6banned:&f {0}");
        config.set("commands.information.ban-reason", "&6ban-reason:&f {0}");
        config.set("commands.information.ban-expire", "&6ban-expire:&f {0}");
        config.set("commands.information.vanished", "&6vanished:&f {0}");
        config.set("commands.information.last-online", "&6last-online:&f {0}");
        config.set("commands.information.quit-location", "&6quit-location:&7 World:&f{0}&7 X:&f{1}&7 Y:&f{2}&7 Z:&f{3}");
        config.set("commands.information.uuid", "&6uuid:&f {0}");
        config.set("commands.inventory.self", "&cYou can not open an inventory of your own");
        config.set("commands.inventory.exempt", "&cYou are not allowed to open&f {0}&6 inventory");
        config.set("commands.inventory.title", "{0} inventory");
        config.set("commands.jail.toggle", "{0}&6 jailed if now {1}");
        config.set("commands.jail.invalid", "&cJail has not been set");
        config.set("commands.jail.exempt", "&cYou are not allowed to jail&f {0}");
        config.set("commands.kit.title", "&6Kits:");
        config.set("commands.kit.listed", "- {0}");
        config.set("commands.kit.empty", "&cKits are currently empty");
        config.set("commands.kit.received", "&6You received&f {0}&6 kit");
        config.set("commands.kit.insufficient-funds", "&cYou do not have&a {0}&c to receive the&f {1}&c kit");
        config.set("commands.kit.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.set("commands.kit.invalid", "{0}&c does not exists");
        config.set("commands.kit.sender", "&6You gave&f {0}&6 kit to&f {1}");
        config.set("commands.kit.exempt", "&cYou are not allowed to give kits to&f {0}");
        config.set("commands.loom.sender", "&6You opened loom for&f {0}");
        config.set("commands.loom.exempt", "&cYou are not allowed to open loom for&f {0}");
        config.set("commands.lvl.self", "&6Your lvl is&f {0}");
        config.set("commands.lvl.other", "{0}&6 lvl is&f {1}");
        config.set("commands.lvl.add", "&6You added&f {0}&6 lvl to&f {1}");
        config.set("commands.lvl.remove", "&6You removed&f {0}&6 lvl to&f {1}");
        config.set("commands.lvl.set", "&6You set&f {0}&6 lvl to&f {1}");
        config.set("commands.motd.invalid", "{0}&c does not exist");
        config.set("commands.motd.exempt", "&cYou are not allowed to send motd to&f {0}");
        config.set("commands.mute.enable", "&6You muted&f {0}");
        config.set("commands.mute.disable", "&6You unmuted&f {0}");
        config.set("commands.mute.exempt", "&cYou are not allowed to mute&f {0}");
        config.set("commands.nickname.self", "&6You changed your nickname to&f {0}");
        config.set("commands.nickname.sender", "&6You changed&f {0}&6 nickname to&f {1}");
        config.set("commands.nickname.exempt", "&cYou are not allowed to change nickname for&f {0}");
        config.set("commands.pay.target", "&6You received&a {0}&6 from&f {1}");
        config.set("commands.pay.sender", "&6You paid&a {0}&f {1}");
        config.set("commands.pay.insufficient-funds", "&cYou do not have&a {0}&c to pay&f {1}");
        config.set("commands.pay.minimum-payment", "&cYou have to at least pay&a {0}");
        config.set("commands.pay.self", "&cYou can not pay your self");
        config.set("commands.pvp.self", "&6&lPVP: {0}");
        config.set("commands.pvp.sender", "&6You {1} pvp for&f {0}");
        config.set("commands.pvp.exempt", "&cYou are not allowed to toggle pvp for&f {0}");
        config.set("commands.repair.damaged", "&6You repaired&f {0}");
        config.set("commands.repair.non-damaged", "{0}&c is already fully repaired");
        config.set("commands.repair.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.set("commands.respond.target", "&7{0} > You&f: {1}");
        config.set("commands.respond.sender", "&7You > {0}&f: {1}");
        config.set("commands.respond.notify", "&7{0} > {1}&f: {2}");
        config.set("commands.respond.invalid", "&cYou do not have a recent whisper");
        config.set("commands.rtp.cooldown", "&cYou have to wait&f {0}&c seconds");
        config.set("commands.rtp.sender", "&6You teleported&f {0}&6 to a random location");
        config.set("commands.rtp.exempt", "&cYou are not allowed to rtp&f {0}");
        config.set("commands.rtp.post-teleport", "&6Finding locations...");
        config.set("commands.rtp.liquid", "&cFinding new location due to liquid block");
        config.set("commands.rtp.teleport", "&6Teleporting to&f random");
        config.set("commands.rules.sender", "&6You sent rules message to&f {0}");
        config.set("commands.rules.exempt", "&cYou are not allowed to send rules to&f {0}");
        config.set("commands.sell.sellable", "&6You sold&f {0} {1}&6 for&a {2}");
        config.set("commands.sell.unsellable", "{0}&c is not able to sell");
        config.set("commands.sell.insufficient", "&cYou do not have enough&f {0}");
        config.set("commands.sethome.success", "{0}&6 has been set");
        config.set("commands.sethome.limit-reached", "&cYou have reached your limit of&f {0}&c homes");
        config.set("commands.sethome.bed", "&cYou can not sethome for&f bed");
        config.set("commands.setjail.relocated", "&6Jail has been relocated");
        config.set("commands.setjail.set", "&6Jail has been set");
        config.set("commands.setspawn.relocated", "&6Spawn has been relocated");
        config.set("commands.setspawn.set", "&6Spawn has been set");
        config.set("commands.setwarp.relocated", "{0}&6 has been relocated");
        config.set("commands.setwarp.set", "{0}&6 has been set");
        config.set("commands.setworth.enable", "{0}&6 is now&a {1}");
        config.set("commands.setworth.disable", "{0}&6 is now worthless");
        config.set("commands.skull.received", "&6You received&f {0}&6 skull");
        config.set("commands.skull.sender", "&6You gave&f {0}&6 a&f {1}&6 skull");
        config.set("commands.skull.invalid", "{0}&c does not exist");
        config.set("commands.smithing.sender", "&6You opened smithing table for&f {0}");
        config.set("commands.spawn.invalid", "&cSpawn has not been set");
        config.set("commands.spawn.exempt", "&cYou are not allowed to tp&f {0}&c to spawn");
        config.set("commands.spawner.info.title", "&6Spawner info:");
        config.set("commands.spawner.info.entity-type", "&6entity-type&f: {0}");
        config.set("commands.spawner.info.spawn-count", "&6spawn-count&f: {0}");
        config.set("commands.spawner.info.delay", "&6delay&f: {0}");
        config.set("commands.spawner.info.max-spawn-delay", "&6max-spawn-delay&f: {0}");
        config.set("commands.spawner.info.min-spawn-delay", "&6min-spawn-delay&f: {0}");
        config.set("commands.spawner.info.spawn-range", "&6spawn-range&f: {0}");
        config.set("commands.spawner.info.non-spawner", "&cYou have to look at a&f Spawner");
        config.set("commands.spawner.info.invalid", "&cYou have to look at a block at least 1 block away");
        config.set("commands.spawner.set.entity-type", "&6Spawner entity type is now set to&f {0}");
        config.set("commands.spawner.set.spawn-count", "&6Spawner spawn count is now set to&f {0}");
        config.set("commands.spawner.set.max-spawn-delay", "&6Spawner max spawn delay is now set to&f {0}");
        config.set("commands.spawner.set.min-spawn-delay", "&6Spawner min spawn delay is now set to&f {0}");
        config.set("commands.spawner.set.spawn-range", "&6Spawner spawn range is now set to&f {0}");
        config.set("commands.spawner.give", "&6You gave&f {0} {1} {2}&6 spawner");
        config.set("commands.stonecutter.sender", "&6You opened stonecutter for&f {0}");
        config.set("commands.stonecutter.exempt", "&cYou are not allowed to open stonecutter for&f {0}");
        config.set("commands.store.sender", "&6You sent store message to&f {0}");
        config.set("commands.store.exempt", "&cYou are not allowed to send store to&f {0}");
        config.set("commands.time.set", "{0}&6 set time to&f {1}");
        config.set("commands.time.add", "{0}&6 added&f {1}&6 time");
        config.set("commands.time.remove", "{0}&6 removed&f {1}&6 time");
        config.set("commands.tpaccept.tpa.target", "{0}&6 accepted tpa request");
        config.set("commands.tpaccept.tpa.sender", "&6You accepted tpa request from&f {0}");
        config.set("commands.tpaccept.tpahere.target", "{0}&6 accepted tpahere request");
        config.set("commands.tpaccept.tpahere.sender", "&6You accepted tpahere request from&f {0}");
        config.set("commands.tpaccept.invalid", "&cYou do not have any tpa requests");
        config.set("commands.tpa.expired", "&cTeleport request has been expired");
        config.set("commands.tpa.target.notify", "{0}&6 has sent you a tpa request");
        config.set("commands.tpa.target.decide", "&6You can type&a /tpaccept&6 or&c /tpdeny");
        config.set("commands.tpa.sender.notify", "&6You sent a tpa request to&f {0}");
        config.set("commands.tpa.sender.decide", "&6You can type&c /tpcancel&6 to cancel tpa request");
        config.set("commands.tpa.occupied", "&cYou already sent a tpa request");
        config.set("commands.tpa.request-self", "&cYou can not send tpa request to your self");
        config.set("commands.tpahere.expired", "&cTeleport request has been expired");
        config.set("commands.tpahere.target.notify", "{0}&6 has sent you a tpahere request");
        config.set("commands.tpahere.target.decide", "&6You can type&a /tpaccept&6 or&c /tpdeny");
        config.set("commands.tpahere.sender.notify", "&6You have sent a tpahere request to&f {0}");
        config.set("commands.tpahere.sender.decide", "&6You can type&c /tpcancel&6 to cancel the request");
        config.set("commands.tpahere.occupied", "&cYou already sent a tpahere request");
        config.set("commands.tpahere.request-self", "&cYou can not send tpahere request to your self");
        config.set("commands.tpcancel.target", "{0}&6 cancelled teleport request");
        config.set("commands.tpcancel.sender", "&6You cancelled teleport request");
        config.set("commands.tpcancel.invalid", "&cYou have not sent any teleport request");
        config.set("commands.tp.exempt", "&cYou are not allowed to teleport to&f {0}");
        config.set("commands.tpdeny.target", "{0}&6 denied teleport request");
        config.set("commands.tpdeny.sender", "&6You denied teleport request from&f {0}");
        config.set("commands.tpdeny.non-requested", "&cYou do not have any teleport request");
        config.set("commands.tphere.sender", "&6Teleporting&f {0}&6 to you");
        config.set("commands.tphere.exempt", "&cYou are not allowed to teleport&f {0}");
        config.set("commands.unban.banned", "{0}&6 is no longer banned");
        config.set("commands.unban.unbanned", "{0}&c is not banned");
        config.set("commands.vanish.sender", "{0}&6 vanish is now {1}");
        config.set("commands.vanish.exempt", "&cYou are not allowed to toggle vanish for&f {0}");
        config.set("commands.walkspeed.changed", "&6You changed your walkspeed to&f {0}");
        config.set("commands.walkspeed.sender", "&6You changed&f {0}&6 walkspeed to&f {1}");
        config.set("commands.walkspeed.exempt", "&cYou are not allowed to change walkspeed for&f {0}");
        config.set("commands.warp.title", "&6Warps:");
        config.set("commands.warp.listed", "- {0}");
        config.set("commands.warp.empty", "&cWarps are currently empty");
        config.set("commands.warp.sender", "&6You warped&f {0}&6 to&f {1}");
        config.set("commands.warp.invalid", "{0}&c does not exists");
        config.set("commands.warp.exempt", "&cYou are not allowed to warp&f {0}");
        config.set("commands.whisper.target", "&7{0} > You&f: {1}");
        config.set("commands.whisper.sender", "&7You > {0}&f: {1}");
        config.set("commands.whisper.notify", "&7{0} > {1}&f: {2}");
        config.set("commands.workbench.sender", "&6You opened workbench for&f {0}");
        config.set("commands.workbench.exempt", "&cYou are not allowed to open workbench for&f {0}");
        config.set("commands.worth.listed", "{0}&6 is worth&a {1}");
        config.set("commands.worth.unlisted", "{0}&c is not sellable");
        config.set("events.vanish", "&6&lVanish&f: {0}");
        config.set("events.teleport.post", "&6Teleporting in&f {0}&6 seconds");
        config.set("events.teleport.success", "&6Teleporting to&f {0}");
        config.set("events.teleport.has-task", "&cYou can not teleport twice you have to wait");
        config.set("events.pvp.self", "&cYour PVP is Disabled");
        config.set("events.pvp.target", "{0}&c PVP is Disabled");
        config.set("events.death", "&cYou lost&a {0}&c {1}");
        config.set("events.gamemode.change", "&6Your gamemode has changed to&f {0}");
        config.set("events.gamemode.notify", "{0}&6 changed gamemode to&f {1}");
        config.set("events.move", "&cYou moved before teleporting!");
        config.set("events.damage", "&cYou got damaged before teleporting!");
        config.set("events.respawn.title", "&6Death location&f:");
        config.set("events.respawn.location", "&6World&f: {0}&6 X&f: {1}&6 Y&f: {2}&6 Z&f: {3}");
        config.set("events.quit.notify", "{0}&7 left the Server");
        config.set("events.join.notify", "{0}&7 joined the Server");
        config.set("events.join.vanished", "{0}&6 joined while vanished");
        config.set("events.login.banned", "&cReason&f: {0}&c, Expires&f: {1}");
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
    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(addColor(message)));
    }
    public void sendStringList(Player player, List<String> strings) {
        strings.forEach(string -> player.sendMessage(addPlaceholder(player, string)));
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
    public String addPlaceholder(Player player, String message) {
        return addColor(PlaceholderAPI.setPlaceholders(player, message));
    }
    public String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public void sendStringList(ConsoleCommandSender sender, List<String> strings) {
        strings.forEach(sender::sendMessage);
    }
    public String getBuilder(String[] args, int value) {
        var builder = getBuilder();
        for(var i = value; i < args.length; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }
        return builder.toString().strip();
    }
    public String toString(List<String> lines) {
        var builder = getBuilder();
        for (var line : lines) {
            builder.append(line).append("&r");
            builder.append("\n");
        }
        return builder.toString().strip();
    }
    public String toTitleCase(String string) {
        if (string.contains(" ")) {
            var builder = getBuilder();
            for (var strings : string.split(" ")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else if (string.contains("_")) {
            var builder = getBuilder();
            for (var strings : string.split("_")) {
                builder.append(strings.toUpperCase().charAt(0)).append(strings.substring(1).toLowerCase());
                builder.append(" ");
            }
            return builder.toString().strip();
        } else return string.toUpperCase().charAt(0) + string.substring(1).toLowerCase();
    }
    public StringBuilder getBuilder() {
        return new StringBuilder();
    }
    public String censor(String message) {
        for (var censored : getInstance().getConfig().getStringList("chat.censor")) {
            if (message.toLowerCase().contains(censored.toLowerCase())) {
                return message.toLowerCase().replace(censored.toLowerCase(), "*".repeat(censored.length()));
            }
        }
        return message;
    }
}