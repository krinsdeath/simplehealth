package net.krinsoft.simplehealth;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleHealth extends JavaPlugin {
	// public variables
	public static PluginDescriptionFile description;
	public static PluginManager manager;
	public static Plugin plugin;
	public static HashMap<Player, SimplePlayer> players = new HashMap<Player, SimplePlayer>();
	
	// private variables
	private static String LOG_PREFIX;
	private static final Logger logger = Logger.getLogger("SimpleHealth");
	
	// Listeners
	private final PListener pListener = new PListener(this);
	private final EListener eListener = new EListener(this);

	@Override
	public void onEnable() {
		// Plugin setup
		plugin = this;
		description = getDescription();
		LOG_PREFIX = "[" + description.getName() + "] ";
		manager = this.getServer().getPluginManager();

		// player events
		manager.registerEvent(Event.Type.PLAYER_INTERACT, pListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_RESPAWN, pListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, pListener, Event.Priority.Lowest, this);
		manager.registerEvent(Event.Type.PLAYER_JOIN, pListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_QUIT, pListener, Event.Priority.Normal, this);
		manager.registerEvent(Event.Type.PLAYER_KICK, pListener, Event.Priority.Normal, this);

		// entity events
		manager.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, eListener, Event.Priority.Low, this);
		manager.registerEvent(Event.Type.ENTITY_DAMAGE, eListener, Event.Priority.High, this);

		// other stuff
		Settings.dataFolder = getDataFolder();
		if (Settings.configure()) {
			LOG_PREFIX = Settings.locale.get(Settings.basic.getString("plugin.default_locale")).getString("plugin.log_prefix");
		}
		SimpleTimer.setParent(this);
		logAdd(Settings.locale.get(Settings.basic.getString("plugin.default_locale")).getString("plugin.enabled"));
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		players.clear();
		logAdd(Settings.locale.get(Settings.basic.getString("plugin.default_locale")).getString("plugin.disabled"));
		Settings.locale.clear();
	}

	public static void logAdd(String msg) {
		msg = LOG_PREFIX + msg;
		msg = msg.replaceAll("<version>", description.getVersion());
		msg = msg.replaceAll("<name>", description.getName());
		msg = msg.replaceAll("<fullname>", description.getFullName());
		logger.info(msg);
	}

}
