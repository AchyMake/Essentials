package org.achymake.essentials;

import net.milkbowl.vault.economy.Economy;
import org.achymake.essentials.commands.*;
import org.achymake.essentials.data.*;
import org.achymake.essentials.handlers.*;
import org.achymake.essentials.listeners.*;
import org.achymake.essentials.providers.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class Essentials extends JavaPlugin {
    private static Essentials instance;
    private Jail jail;
    private Kits kits;
    private Message message;
    private Skulls skulls;
    private Spawn spawn;
    private Warps warps;
    private Worth worth;
    private CooldownHandler cooldownHandler;
    private DateHandler dateHandler;
    private EconomyHandler economyHandler;
    private InventoryHandler inventoryHandler;
    private MaterialHandler materialHandler;
    private ScheduleHandler scheduleHandler;
    private VanishHandler vanishHandler;
    private WorldHandler worldHandler;
    private UpdateChecker updateChecker;
    private PluginManager manager;
    private BukkitScheduler bukkitScheduler;
    @Override
    public void onEnable() {
        instance = this;
        jail = new Jail();
        kits = new Kits();
        message = new Message();
        skulls = new Skulls();
        spawn = new Spawn();
        warps = new Warps();
        worth = new Worth();
        cooldownHandler = new CooldownHandler();
        dateHandler = new DateHandler();
        economyHandler = new EconomyHandler();
        inventoryHandler = new InventoryHandler();
        materialHandler = new MaterialHandler();
        scheduleHandler = new ScheduleHandler();
        vanishHandler = new VanishHandler();
        worldHandler = new WorldHandler();
        updateChecker = new UpdateChecker();
        manager = getServer().getPluginManager();
        bukkitScheduler = getServer().getScheduler();
        commands();
        events();
        reload();
        new PlaceholderProvider().register();
        getServer().getServicesManager().register(Economy.class, new VaultEconomyProvider(), this, ServicePriority.Normal);
        sendInfo("Enabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
        getUpdateChecker().getUpdate();
    }
    @Override
    public void onDisable() {
        getVanishHandler().disable();
        new PlaceholderProvider().unregister();
        getScheduleHandler().cancelAll();
        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }
    private void commands() {
        new AnnouncementCommand();
        new AnvilCommand();
        new BackCommand();
        new BalanceCommand();
        new BanCommand();
        new BankCommand();
        new CartographyCommand();
        new ColorCommand();
        new DelHomeCommand();
        new DelWarpCommand();
        new EcoCommand();
        new EnchantCommand();
        new EnchantingCommand();
        new EnderChestCommand();
        new EssentialsCommand();
        new FeedCommand();
        new FlyCommand();
        new FlySpeedCommand();
        new FreezeCommand();
        new GameModeCommand();
        new GiveCommand();
        new GMACommand();
        new GMCCommand();
        new GMSCommand();
        new GMSPCommand();
        new GrindstoneCommand();
        new HatCommand();
        new HealCommand();
        new HelpCommand();
        new HomeCommand();
        new HomesCommand();
        new InformationCommand();
        new InventoryCommand();
        new JailCommand();
        new KitCommand();
        new LoomCommand();
        new MOTDCommand();
        new MuteCommand();
        new NicknameCommand();
        new PayCommand();
        new PVPCommand();
        new RepairCommand();
        new RespondCommand();
        new RTPCommand();
        new RulesCommand();
        new SellCommand();
        new SetHomeCommand();
        new SetJailCommand();
        new SetSpawnCommand();
        new SetWarpCommand();
        new SetWorthCommand();
        new SkullCommand();
        new SmithingCommand();
        new SpawnCommand();
        new StonecutterCommand();
        new StoreCommand();
        new TPAcceptCommand();
        new TPACommand();
        new TPAHereCommand();
        new TPCancelCommand();
        new TPCommand();
        new TPDenyCommand();
        new TPHereCommand();
        new UnBanCommand();
        new VanishCommand();
        new WalkSpeedCommand();
        new WarpCommand();
        new WhisperCommand();
        new WorkbenchCommand();
        new WorthCommand();
    }
    private void events() {
        new AsyncPlayerChat();
        new BlockBreak();
        new BlockFertilize();
        new BlockIgnite();
        new BlockPlace();
        new BlockReceiveGame();
        new EntityDamageByEntity();
        new EntityMount();
        new EntityTarget();
        new EntityTargetLivingEntity();
        new PlayerBucketEmpty();
        new PlayerBucketEntity();
        new PlayerBucketFill();
        new PlayerChangedChunk();
        new PlayerCommandPreprocess();
        new PlayerDeath();
        new PlayerGameModeChange();
        new PlayerHarvestBlock();
        new PlayerInteract();
        new PlayerInteractEntity();
        new PlayerJoin();
        new PlayerLeashEntity();
        new PlayerLevelChange();
        new PlayerLogin();
        new PlayerMove();
        new PlayerQuit();
        new PlayerRespawn();
        new PlayerShearBlock();
        new PlayerShearEntity();
        new PlayerTeleport();
        new PlayerToggleFlight();
        new PlayerToggleSneak();
        new PrepareAnvil();
        new SignChange();
    }
    public void reload() {
        var file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            try {
                getConfig().load(file);
            } catch (IOException | InvalidConfigurationException e) {
                sendWarning(e.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);
            try {
                getConfig().save(file);
            } catch (IOException e) {
                sendWarning(e.getMessage());
            }
        }
        if (getConfig().getBoolean("server.motd.enable")) {
            var line1 = getMessage().addColor(getConfig().getString("server.motd.line-1"));
            var line2 = getMessage().addColor(getConfig().getString("server.motd.line-2"));
            getServer().setMotd(line1 + "\n" + line2);
        }
        getJail().reload();
        getKits().reload();
        getMessage();
        getSkulls().reload();
        getSpawn().reload();
        getWarps().reload();
        getWorth().reload();
    }
    public void reloadUserdata() {
        var userdata = new File(getDataFolder(), "userdata");
        if (userdata.exists() && userdata.isDirectory()) {
            for (var file : userdata.listFiles()) {
                var config = YamlConfiguration.loadConfiguration(file);
                try {
                    config.load(file);
                } catch (IOException | InvalidConfigurationException e) {
                    sendWarning(e.getMessage());
                }
            }
        }
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        var listed = new ArrayList<OfflinePlayer>();
        var folder = new File(getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    var uuidString = file.getName().replace(".yml", "");
                    listed.add(getOfflinePlayer(UUID.fromString(uuidString)));
                }
            }
        }
        return listed;
    }
    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
    public BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }
    public PluginManager getManager() {
        return manager;
    }
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
    public WorldHandler getWorldHandler() {
        return worldHandler;
    }
    public VanishHandler getVanishHandler() {
        return vanishHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }
    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
    public DateHandler getDateHandler() {
        return dateHandler;
    }
    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }
    public Worth getWorth() {
        return worth;
    }
    public Warps getWarps() {
        return warps;
    }
    public Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return new Userdata(offlinePlayer);
    }
    public Spawn getSpawn() {
        return spawn;
    }
    public Skulls getSkulls() {
        return skulls;
    }
    public Message getMessage() {
        return message;
    }
    public Kits getKits() {
        return kits;
    }
    public Jail getJail() {
        return jail;
    }
    public static Essentials getInstance() {
        return instance;
    }
    public void sendInfo(String message) {
        getLogger().info(message);
    }
    public void sendWarning(String message) {
        getLogger().warning(message);
    }
    public String name() {
        return getDescription().getName();
    }
    public String version() {
        return getDescription().getVersion();
    }
    public String getMinecraftVersion() {
        return getServer().getBukkitVersion();
    }
    public String getMinecraftProvider() {
        return getServer().getName();
    }
    public boolean isSpigot() {
        return getMinecraftProvider().equals("CraftBukkit");
    }
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }
}