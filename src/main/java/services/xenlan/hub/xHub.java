package services.xenlan.hub;

import com.google.common.collect.Sets;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import services.xenlan.hub.action.Action;
import services.xenlan.hub.action.ActionTypes;
import services.xenlan.hub.bungee.BungeeListener;
import services.xenlan.hub.bungee.BungeeUpdateTask;
import services.xenlan.hub.commands.StatsCommand;
import services.xenlan.hub.commands.customtimer.CustomTimerCommand;
import services.xenlan.hub.commands.pvp.LeavePvP;
import services.xenlan.hub.commands.pvp.PvPCommand;
import services.xenlan.hub.commands.queue.JoinQueueCommand;
import services.xenlan.hub.commands.queue.LeaveQueueCommand;
import services.xenlan.hub.commands.queue.PauseQueueCommand;
import services.xenlan.hub.commands.xHubCommand;
import services.xenlan.hub.cosmetic.listener.CosmeticsListener;
import services.xenlan.hub.cosmetic.manager.GearManager;
import services.xenlan.hub.cosmetic.manager.HatManager;
import services.xenlan.hub.cosmetic.manager.TrailsManager;
import services.xenlan.hub.customtimer.CustomTimerManager;
import services.xenlan.hub.gui.button.CustomButton;
import services.xenlan.hub.gui.menu.CustomGUI;
import services.xenlan.hub.listeners.HubListeners;
import services.xenlan.hub.listeners.ItemListeners;
import services.xenlan.hub.listeners.LunarListener;
import services.xenlan.hub.parkour.ParkourManager;
import services.xenlan.hub.pvp.PvPManager;
import services.xenlan.hub.pvp.gui.listener.PvPGUIListener;
import services.xenlan.hub.pvp.kits.KitManager;
import services.xenlan.hub.queue.Queue;
import services.xenlan.hub.queue.custom.CustomQueue;
import services.xenlan.hub.queue.manager.QueueManager;
import services.xenlan.hub.queue.plugins.Custom;
import services.xenlan.hub.queue.plugins.EzQueue;
import services.xenlan.hub.queue.plugins.None;
import services.xenlan.hub.queue.plugins.Portal;
import services.xenlan.hub.scoreboard.ScoreboardProvider;
import services.xenlan.hub.tablist.provider.TablistProvider;
import services.xenlan.hub.tablist.ziggurat.Ziggurat;
import services.xenlan.hub.utils.CC;
import services.xenlan.hub.utils.ItemBuilder;
import services.xenlan.hub.utils.Serializer;
import services.xenlan.hub.utils.YamlDoc;
import services.xenlan.hub.utils.api.RankAPI;
import services.xenlan.hub.utils.interfaces.CallBack;
import services.xenlan.hub.utils.menu.MenuListener;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class xHub extends JavaPlugin {

	@Getter private static xHub instance;
	@Getter private static Chat chat = null;
	@Getter private static Permission perms = null;

	private HashSet<Player> buildmode;
	
	private Set<CustomGUI> customGUI;
	
	private Set<CustomButton> customButton;
	
	private Set<Action> actions;
	
	private PvPManager pvPManager;
	
	private KitManager kitManager;
	
	private Queue queue;
	
	private YamlDoc settingsYML;
	
	private YamlDoc pvpYML;
	
	private YamlDoc trailsYML;
	
	private YamlDoc queueYML;
	
	private YamlDoc tabYML;
	
	private YamlDoc gearYML;
	
	private YamlDoc hatsYML;
	
	private YamlDoc cosmeticYML;
	
	private YamlDoc messagesYML;
	
	private YamlDoc menusYML;
	
	private YamlDoc inventoriesYML;
	
	private YamlDoc scoreboardYML;
	
	private YamlDoc changeLogYML;
	
	private HatManager hatManager;
	
	private TrailsManager trailsManager;
	
	private GearManager gearManager;
	
	private CustomTimerManager customTimerManager;
	
	private QueueManager queueManager;
	
	private ParkourManager parkourManager;
	
	private boolean isPlaceholderAPI = false;

	@Override
	@SneakyThrows
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		registerConfigs();
		registerPapi();
		registerBungee();
		registerScoreboard();
		registerGUIS();
		registerEvents();
		registerCommands();
		registerManagers();
		registerBuildmode();
		registerTab();
		registerVault();
		setupQueue();
		createMenus();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&8&m--------------------------"));
		Bukkit.getConsoleSender().sendMessage(CC.chat("&bXenlan Hub&f has been &aenabled"));
		Bukkit.getConsoleSender().sendMessage(CC.chat("&fMade by &bXenlan Services"));
		Bukkit.getConsoleSender().sendMessage(CC.chat("&8&m--------------------------"));
	}

	@Override
	public void onDisable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().clear();
			player.kickPlayer("");
		}

	}

	public void registerBungee() {
		try {
			if (getConfig().getBoolean("BungeeCord")) {
				registerBungeeCount();
			}
			Bukkit.getConsoleSender().sendMessage(CC.chat("&aBungeeCord setup successful"));
		} catch (Exception ignored) {
			Bukkit.getConsoleSender().sendMessage(CC.chat("&cBungeeCord setup failed"));
		}
	}

	public void registerTab() {
		if (getSettingsYML().getConfig().getBoolean("Tab-List")) {
			new Ziggurat(this, new TablistProvider());
			Bukkit.getConsoleSender().sendMessage(CC.chat("&aTablist setup successful"));
		} else {
			Bukkit.getConsoleSender().sendMessage(CC.chat("&cBungeeCord setup unsuccessful (Disabled in the settings.yml)"));
		}
	}

	public void registerPapi() {

		if (getConfig().getBoolean("PlaceholderAPI-Support")) {
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null || Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && !Bukkit.getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) {
				System.out.println("[ERROR] PLACEHOLDER API IS NOT ENABLED!");
				System.out.println("You can find a download for it here: https://www.spigotmc.org/resources/placeholderapi.6245/!");
				isPlaceholderAPI = false;
			} else {
				Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lPlaceholderAPI&a successfully!"));
				isPlaceholderAPI = true;
			}
		}
	}

	public void registerVault() {
		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			try {
				if (RankAPI.checkRankCore().equalsIgnoreCase("vault")) {
					RankAPI.isVault = true;
				}
				setupChat();
				setupPermissions();
				PluginManager pm = Bukkit.getPluginManager();
				if (pm.getPlugin("LuckPerms") != null && pm.getPlugin("LuckPerms").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (LuckPerms)"));
				} else if (pm.getPlugin("PermissionsEx") != null && pm.getPlugin("PermissionsEx").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (PermissionsEx)"));
				} else if (pm.getPlugin("Zoom") != null && pm.getPlugin("Zoom").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (Zoom)"));
				} else if (pm.getPlugin("AquaCore") != null && pm.getPlugin("AquaCore").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (AquaCore)"));
				} else if (pm.getPlugin("Hestia") != null && pm.getPlugin("Hestia").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (Hestia)"));
				} else if (pm.getPlugin("Mizu") != null && pm.getPlugin("Mizu").isEnabled()) {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (Mizu)"));
				} else {
					Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lVault&a successfully! (Unknown)"));
				}
			} catch (Exception exception) {
				if (RankAPI.checkRankCore().equalsIgnoreCase("vault")) {
					RankAPI.isVault = false;
				}
				Bukkit.getConsoleSender().sendMessage(CC.chat("&cxHub detected Vault successfully, but could not find a rank core to support it."));
			}
		} else {
			Bukkit.getConsoleSender().sendMessage(CC.chat("&cWe could not find the Vault plugin in your server files."));
			Bukkit.getConsoleSender().sendMessage(CC.chat("&cAs a result: We can not look for any active ranks."));
		}
	}

	public void registerCommands() {
		getCommand("xhub").setExecutor(new xHubCommand());
		getCommand("leavepvp").setExecutor(new LeavePvP());
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("pvp").setExecutor(new PvPCommand());
		getCommand("customtimer").setExecutor(new CustomTimerCommand());
		getCommand("pausequeue").setExecutor(new PauseQueueCommand());
		getCommand("leavequeue").setExecutor(new LeaveQueueCommand());
		getCommand("joinqueue").setExecutor(new JoinQueueCommand());
	}

	public void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new HubListeners(), this);
		Bukkit.getPluginManager().registerEvents(new ItemListeners(), this);
		Bukkit.getPluginManager().registerEvents(new CosmeticsListener(), this);
		Bukkit.getPluginManager().registerEvents(new PvPGUIListener(), this);
		Bukkit.getPluginManager().registerEvents(new MenuListener(this), this);
		if (getSettingsYML().getConfig().getBoolean("LunarClientAPI.Enabled")) {
			if (Bukkit.getPluginManager().getPlugin("LunarClient-API") != null && Bukkit.getPluginManager().getPlugin("LunarClient-API").isEnabled()) {
				new LunarListener();
				Bukkit.getConsoleSender().sendMessage(CC.chat("&axHub has hooked in to &lLunarAPI&a successfully!"));
			} else {
				Bukkit.getConsoleSender().sendMessage(CC.chat("&cxHub could not hook into &lLunarAPI&c!"));
			}
		}
	}

	public void createMenus() {
		customButton = Sets.newHashSet();
		customGUI = Sets.newHashSet();
		actions = Sets.newHashSet();
		customGUI.clear();
		customButton.clear();
		actions.clear();
		for (String selection : getMenusYML().getConfig().getConfigurationSection("menus").getKeys(false)) {
			String title = getMenusYML().getConfig().getString("menus." + selection + ".Title");
			int size = getMenusYML().getConfig().getInt("menus." + selection + ".Size");
			boolean usePholder = getMenusYML().getConfig().getBoolean("menus." + selection + ".Auto-Fill");
			customGUI.add(new CustomGUI(selection, CC.chat(title), size, usePholder));
			for (String se : getMenusYML().getConfig().getConfigurationSection("menus." + selection + ".Items").getKeys(false)) {
				int slot = getMenusYML().getConfig().getInt("menus." + selection + ".Items."+ se + ".Slot");
				String mat = getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Material");
				String color = getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Color");
				String display = getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Display");
				List<String> lore = getMenusYML().getConfig().getStringList("menus." + selection + ".Items." + se + ".Lore");
				int data = getMenusYML().getConfig().getInt("menus." + selection + ".Items." + se + ".Data");
				String type = getMenusYML().getConfig().getString("menus." + selection + ".Items." + se + ".Type");
				if (!lore.isEmpty()) {
					customButton.add(new CustomButton(se, new ItemBuilder(Material.valueOf(mat)).setLeatherArmorColor(Serializer.getColor(color)).data(data).setLore(lore).displayName(CC.chat(display)).build(), slot, selection));
				} else {
					customButton.add(new CustomButton(se, new ItemBuilder(Material.valueOf(mat)).setLeatherArmorColor(Serializer.getColor(color)).data(data).displayName(CC.chat(display)).build(), slot, selection));
				}
				switch (type) {
					case "OPEN":
						actions.add(new Action(se, ActionTypes.OPEN));
						break;
					case "QUEUE":
						actions.add(new Action(se, ActionTypes.QUEUE));
						break;
					case "HAT":
						actions.add(new Action(se, ActionTypes.HAT));
						break;
					case "GEAR":
						actions.add(new Action(se, ActionTypes.GEAR));
						break;
					case "RESET":
						actions.add(new Action(se, ActionTypes.RESET));
						break;
					case "HUB":
						actions.add(new Action(se, ActionTypes.HUB));
						break;
					case "TRAILS":
						actions.add(new Action(se, ActionTypes.TRAILS));
						break;
					case "MESSAGE":
						actions.add(new Action(se, ActionTypes.MESSAGE));
						break;
					default:
						break;
				}
			}
		}
	}

	public void registerBungeeCount() {
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener());
		getServer().getScheduler().runTaskTimerAsynchronously(this, new BungeeUpdateTask(), 0L, 20L);
	}

	public void registerManagers() {
		hatManager = new HatManager();
		gearManager = new GearManager();
		trailsManager = new TrailsManager();

		parkourManager = new ParkourManager(this);
		pvPManager = new PvPManager(this);
		kitManager = new KitManager(this);
		customTimerManager = new CustomTimerManager();
		queueManager = new QueueManager(this);
	}

	@SneakyThrows
	public void registerGUIS() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		File guiFolder = new File(getDataFolder(), "guis");
		if (!guiFolder.exists()) {
			guiFolder.mkdirs();
		}

		cosmeticYML = new YamlDoc(guiFolder, "cosmetic.yml");
		cosmeticYML.init();
		trailsYML = new YamlDoc(guiFolder, "trails.yml");
		trailsYML.init();
		hatsYML = new YamlDoc(guiFolder, "hats.yml");
		hatsYML.init();
		gearYML = new YamlDoc(guiFolder, "gear.yml");
		gearYML.init();
		menusYML = new YamlDoc(guiFolder, "menus.yml");
		menusYML.init();
	}

	public void registerScoreboard() {
		try {
			if (!(new CallBack(getConfig().getString("License"), "https://system.xenlan.services/verify.php", this)).register()) {
				return;
			}
		} catch (NoClassDefFoundError var2) {
			Bukkit.shutdown();
			Bukkit.getPluginManager().disablePlugin(this);
		}

		Assemble assemble = new Assemble(this, new ScoreboardProvider());
		assemble.setTicks(2L);
		assemble.setAssembleStyle(AssembleStyle.KOHI);
	}

	public void registerBuildmode() {
		buildmode = new HashSet();
	}

	public void registerConfigs() throws IOException {
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the scoreboard.yml successfully"));
		scoreboardYML = new YamlDoc(getDataFolder(), "scoreboard.yml");
		scoreboardYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the pvp.yml successfully"));
		pvpYML = new YamlDoc(getDataFolder(), "pvp.yml");
		pvpYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the messages.yml successfully"));
		messagesYML = new YamlDoc(getDataFolder(), "messages.yml");
		messagesYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the settings.yml successfully"));
		settingsYML = new YamlDoc(getDataFolder(), "settings.yml");
		settingsYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the tab.yml successfully"));
		tabYML = new YamlDoc(getDataFolder(), "tab.yml");
		tabYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the changelog.yml successfully"));
		changeLogYML = new YamlDoc(getDataFolder(), "changelog.yml");
		changeLogYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the inventories.yml successfully"));
		inventoriesYML = new YamlDoc(getDataFolder(), "inventories.yml");
		inventoriesYML.init();
		Bukkit.getConsoleSender().sendMessage(CC.chat("&aLoaded the queue.yml successfully"));
		queueYML = new YamlDoc(getDataFolder(), "queue.yml");
		queueYML.init();
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return true;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp.getPlugin() == null && rsp.getProvider() == null) {
			return false;
		}
		perms = rsp.getProvider();
		return true;
	}

	public void setupQueue() {
		String queuef = getConfig().getString("Queue-System").toUpperCase();
		queue = null;
		switch (queuef) {
			case "EZQUEUE":
				if (Bukkit.getServer().getPluginManager().getPlugin("EzQueueSpigot") == null) {
					Bukkit.getServer().getPluginManager().disablePlugin(this);
				}
				queue = new EzQueue();
				break;
			case "PORTAL":
				if (Bukkit.getServer().getPluginManager().getPlugin("Portal") == null) {
					Bukkit.getServer().getPluginManager().disablePlugin(this);
				}
				queue = new Portal();
				break;
			case "CUSTOM":
				queue = new Custom();
				queueManager.getQueues().clear();
				for (String sect : getQueueYML().getConfig().getConfigurationSection("queues").getKeys(false)) {
					String name = getQueueYML().getConfig().getString("queues." + sect + ".bungee-name");
					queueManager.getQueues().add(new CustomQueue(name));
				}
				queueManager.getQueues().forEach(queue -> Bukkit.getConsoleSender().sendMessage(CC.chat("&b" + queue.getServer() + " &fwas found and has been created!")));
				int queueDelay = getQueueYML().getConfig().getInt("queue-delay");
				new BukkitRunnable() {
					@Override
					public void run() {
						queueManager.getQueues().forEach(queue -> {
							if (!queue.isPaused() && !queue.getPlayers().isEmpty()) {
								queue.update();
								queue.removeFromQueue(queue.getPlayer(0));
							}
						});
					}
				}.runTaskTimer(getInstance(), queueDelay * 20L, queueDelay * 20L);
				Bukkit.getPluginManager().registerEvents(new QueueManager(this), this);
				break;
			default:
				queue = new None();
				break;
		}

	}
}
