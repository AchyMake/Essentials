package org.achymake.essentials;

import net.milkbowl.vault.economy.Economy;
import org.achymake.essentials.commands.*;
import org.achymake.essentials.data.*;
import org.achymake.essentials.handlers.*;
import org.achymake.essentials.listeners.*;
import org.achymake.essentials.providers.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
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
    private Userdata userdata;
    private Warps warps;
    private Worth worth;
    private CooldownHandler cooldownHandler;
    private DateHandler dateHandler;
    private EconomyHandler economyHandler;
    private EntityHandler entityHandler;
    private InventoryHandler inventoryHandler;
    private MaterialHandler materialHandler;
    private ProjectileHandler projectileHandler;
    private RandomHandler randomHandler;
    private ScheduleHandler scheduleHandler;
    private ScoreboardHandler scoreboardHandler;
    private TablistHandler tablistHandler;
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
        userdata = new Userdata();
        warps = new Warps();
        worth = new Worth();
        cooldownHandler = new CooldownHandler();
        dateHandler = new DateHandler();
        economyHandler = new EconomyHandler();
        entityHandler = new EntityHandler();
        inventoryHandler = new InventoryHandler();
        materialHandler = new MaterialHandler();
        projectileHandler = new ProjectileHandler();
        randomHandler = new RandomHandler();
        scheduleHandler = new ScheduleHandler();
        scoreboardHandler = new ScoreboardHandler();
        tablistHandler = new TablistHandler();
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
        getProjectileHandler().disable();
        getScheduleHandler().disable();
        new PlaceholderProvider().unregister();
        sendInfo("Disabled for " + getMinecraftProvider() + " " + getMinecraftVersion());
    }
    private void commands() {
        new AnnouncementCommand();
        new AnvilCommand();
        new BackCommand();
        new BalanceCommand();
        new BanCommand();
        new BankCommand();
        new BoardCommand();
        new CartographyCommand();
        new ColorCommand();
        new DelHomeCommand();
        new DelWarpCommand();
        new EcoCommand();
        new EnchantCommand();
        new EnchantingCommand();
        new EnderChestCommand();
        new EntityCommand();
        new EssentialsCommand();
        new ExpCommand();
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
        new LvlCommand();
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
        new SpawnerCommand();
        new StonecutterCommand();
        new StoreCommand();
        new TimeCommand();
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
        new BellRing();
        new BlockBreak();
        new BlockDamage();
        new BlockDispense();
        new BlockDispenseArmor();
        new BlockDispenseLoot();
        new BlockFertilize();
        new BlockIgnite();
        new BlockPistonExtend();
        new BlockPistonRetract();
        new BlockPlace();
        new BlockReceiveGame();
        new BlockRedstone();
        new BlockShearEntity();
        new BlockSpread();
        new CrafterCraft();
        new CreatureSpawn();
        new EntityBlockForm();
        new EntityChangeBlock();
        new EntityDamage();
        new EntityDamageByEntity();
        new EntityExplode();
        new EntityInteract();
        new EntityMount();
        new EntityTarget();
        new EntityTargetLivingEntity();
        new HangingBreakByEntity();
        new HangingPlace();
        new InventoryClick();
        new InventoryClose();
        new NotePlay();
        new PlayerBucketEmpty();
        new PlayerBucketEntity();
        new PlayerBucketFill();
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
        new PlayerShearEntity();
        new PlayerSpawnLocation();
        new PlayerTakeLecternBook();
        new PlayerTeleport();
        new PlayerToggleFlight();
        new PlayerToggleSneak();
        new PrepareAnvil();
        new ProjectileHit();
        new ProjectileLaunch();
        new SignChange();
        new VehicleCreate();
        if (!isBukkit()) {
            new PlayerShearBlock();
        }
    }
    public void reload() {
        getOnlinePlayers().forEach(this::disableTab);
        getOnlinePlayers().forEach(this::disableBoard);
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
        getEntityHandler().reload();
        getJail().reload();
        getKits().reload();
        getMessage().reload();
        getSkulls().reload();
        getSpawn().reload();
        getWarps().reload();
        getWorth().reload();
        getScoreboardHandler().reload();
        getTablistHandler().reload();
        getOnlinePlayers().forEach(this::applyTab);
        getOnlinePlayers().forEach(this::applyBoard);
    }
    public void reloadUserdata() {
        if (!getOfflinePlayers().isEmpty()) {
            getOfflinePlayers().forEach(offlinePlayer -> getUserdata().reload(offlinePlayer));
        }
    }
    public Collection<? extends Player> getOnlinePlayers() {
        return getServer().getOnlinePlayers();
    }
    public List<OfflinePlayer> getOfflinePlayers() {
        var listed = new ArrayList<OfflinePlayer>();
        if (!getUUIDS().isEmpty()) {
            getUUIDS().forEach(uuid -> listed.add(getOfflinePlayer(uuid)));
        }
        return listed;
    }
    public List<UUID> getUUIDS() {
        var listed = new ArrayList<UUID>();
        var folder = new File(getDataFolder(), "userdata");
        if (folder.exists() && folder.isDirectory()) {
            for (var file : folder.listFiles()) {
                if (file.exists() && file.isFile()) {
                    listed.add(UUID.fromString(file.getName().replace(".yml", "")));
                }
            }
        }
        return listed;
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
    public TablistHandler getTablistHandler() {
        return tablistHandler;
    }
    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }
    public ScheduleHandler getScheduleHandler() {
        return scheduleHandler;
    }
    public RandomHandler getRandomHandler() {
        return randomHandler;
    }
    public ProjectileHandler getProjectileHandler() {
        return projectileHandler;
    }
    public MaterialHandler getMaterialHandler() {
        return materialHandler;
    }
    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }
    public EntityHandler getEntityHandler() {
        return entityHandler;
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
    public Userdata getUserdata() {
        return userdata;
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
    public boolean isBukkit() {
        return getMinecraftProvider().equals("Bukkit") || getMinecraftProvider().equals("CraftBukkit");
    }
    public Player getPlayer(String username) {
        return getServer().getPlayerExact(username);
    }
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }
    public OfflinePlayer getOfflinePlayer(String username) {
        return getServer().getOfflinePlayer(username);
    }
    public void applyTab(Player player) {
        getTablistHandler().apply(player);
    }
    public void disableTab(Player player) {
        getTablistHandler().disable(player);
    }
    public void applyBoard(Player player) {
        getScoreboardHandler().apply(player);
    }
    public void disableBoard(Player player) {
        getScoreboardHandler().disable(player);
    }
}