package net.krinsoft.simplehealth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;

public class SimpleTimer implements Runnable {
	/*
	 * Class members and methods
	 */
	/**
	 * Contains a list of all active tasks relating to the player by name
	 */
	private static List<String> tasks = new ArrayList<String>();
	/**
	 * Contains a Map of all keys by player to their Timer instance
	 */
	private static HashMap<String, SimpleTimer> timers = new HashMap<String, SimpleTimer>();
	/**
	 * Contains a static private reference to this plugin
	 */
	private static SimpleHealth plugin;
	/**
	 * Contains a list of worlds that players will regenerate on.
	 */
	private static List<String> worlds = new ArrayList<String>();

	/**
	 * Sets the plugin instance
	 * @param instance
	 */
	public static void setParent(SimpleHealth instance) {
		plugin = instance;
		worlds = Settings.basic.getStringList("plugin.worlds", new ArrayList<String>());
	}

	/**
	 * Returns whether or not the specified instance of a timer exists
	 * @param name
	 * The player's name to lookup
	 * @return
	 * true if the player has a task, false if not
	 */
	public static boolean hasTask(String name) {
		return tasks.contains(name);
	}

	/**
	 * Removes this task from the timers
	 * @param instance
	 */
	public static void remove(SimpleTimer instance) {
		plugin.getServer().getScheduler().cancelTask(instance.taskId);
		timers.remove(instance.name);
		tasks.remove(instance.name);
		instance = null;
	}

	/**
	 * Returns a SimpleTimer object relating to the specified name
	 * @param name
	 * @return
	 * The SimpleTimer instance, or null for an error
	 */
	public static SimpleTimer getTask(String name) {
		return timers.get(name);
	}

	/**
	 * Adds the task to the class list
	 * @param name
	 * The name of the player this task relates to
	 * @param instance
	 * The instance of the SimpleTimer
	 */
	public static void addTask(String name, SimpleTimer instance) {
		timers.put(name, instance);
	}


	/*
	 * Instance fields and methods
	 */

	/*
	 * The name of the player this SimpleTimer relates to
	 */
	private String name;
	/*
	 * The group the player belongs to
	 */
	private String group;
	/*
	 * The amount of health this player respawns with when killed
	 */
	private final int respawn;
	/*
	 * The amount of health this user regenerates per iteration of the timer
	 */
	private final int amount;
	/*
	 * This Timer's task ID, for cancellation on completion (full health)
	 */
	private int taskId;

	/**
	 * Class constructor. Creates a new instance of a SimpleTimer relating to the
	 * player specified by the parameter
	 * @param player
	 * The player relating to this timer
	 */
	public SimpleTimer(String player) {
		tasks.add(player);
		this.name = player;
		this.group = SimpleHealth.players.get(plugin.getServer().getPlayer(player)).getGroup();
		this.respawn = Settings.basic.getInt("groups." + group + ".events._respawn", 1);
		this.amount = Settings.basic.getInt("groups." + group + ".regen.amount", 1);
	}

	/**
	 * Sets the task ID of this timer, for cancellation upon completion (full health)
	 * @param Id
	 * The ID returned by the task scheduler when this timer was created
	 */
	public void setTaskId(int Id) {
		this.taskId = Id;
	}

	@Override
	public void run() {
		regenerate();
	}

	/**
	 * Attempts to regenerate the player's health
	 * Cancels the task when the player reaches maximum (20)
	 */
	public void regenerate() {
		Player player = plugin.getServer().getPlayer(name);
		SimplePlayer sPlayer = SimpleHealth.players.get(player);
		if (sPlayer == null) {
			sPlayer = Settings.addNewUser(player);
		}
		if (sPlayer.isRespawning() && player.getHealth() > 0) {
			player.setHealth(respawn);
			sPlayer.setRespawn(false);
		}
		if (worlds.contains(player.getWorld().getName())) {
			int amt = amount;
			amt = (player.getHealth() + amt > 20) ? 20 : player.getHealth() + amt;
			player.setHealth(amt);
		} else {
			SimpleTimer.remove(this);
			return;
		}
		if (player.getHealth() == 20) {
			SimpleTimer.remove(this);
			return;
		}
	}

}
