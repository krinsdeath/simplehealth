package net.krinsoft.simplehealth;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class SimpleTimer implements Runnable {
	private SimpleHealth plugin;
	private boolean perm = false;

	public SimpleTimer(SimpleHealth instance) {
		plugin = instance;
		perm = Settings.basic.getBoolean("plugin.permissions", false);
	}

	@Override
	public void run() {
		regenerate();
	}
	
	public void regenerate() {
		// Loop through worlds
		for (World world : plugin.getServer().getWorlds()) {
			// Iterate through online players
			for (Player player : world.getPlayers()) {
				if (player.getHealth() == 20) { continue; }
				SimplePlayer sPlayer = SimpleHealth.players.get(player);
				if (sPlayer == null) { sPlayer = Settings.addNewUser(player); }
				// Iterate through the groups
				if (sPlayer.isRespawning() && player.getHealth() > 0) {
					player.setHealth(Settings.basic.getInt("groups." + sPlayer.getGroup() + ".events._respawn", 1));
					sPlayer.setRespawn(false);
				}
				// Check for permissions
				if (perm) {
					// Check if the player has the current key in his permission nodes
					String key = sPlayer.getGroup();
					if (player.hasPermission("simplehealth." + key)) {
						if ((Settings.basic.getInt("groups." + key + ".regen.rate", 0) * 1000) + sPlayer.getLastTick() <= System.currentTimeMillis()) {
							int amt = Settings.basic.getInt("groups." + key + ".regen.amount", 0);
							amt = (player.getHealth() + amt <= 20) ? player.getHealth() + amt : 20;
							player.setHealth(amt);
							sPlayer.setLastTick(System.currentTimeMillis());
							SimpleHealth.players.put(player, sPlayer);
//							String dbg = Settings.basic.getInt("groups." + key + ".regen.rate", 0) + ":" + Settings.basic.getInt("groups." + key + ".regen.amount", 0);
//							SimpleHealth.logAdd(sPlayer.toString() + " -> " + dbg);
						}
					}
				} else { // permissions is disabled or not used
					// check if player is an operator
					if (player.isOp()) {
						// Check if the regen is ready
						if ((Settings.basic.getInt("groups.admins.regen.rate", 0) * 1000) + sPlayer.getLastTick() <= System.currentTimeMillis()) {
							int amt = Settings.basic.getInt("groups.admins.regen.amount", 0);
							amt = (player.getHealth() + amt <= 20) ? player.getHealth() + amt : 20;
							player.setHealth(amt);
							sPlayer.setLastTick(System.currentTimeMillis());
							SimpleHealth.players.put(player, sPlayer);
//							String dbg = Settings.basic.getInt("groups.admins.regen.rate", 0) + ":" + Settings.basic.getInt("groups.admins.regen.amount", 0);
//							SimpleHealth.logAdd(sPlayer.toString() + " -> " + dbg);
						}
					} else {
						if ((Settings.basic.getInt("groups.users.regen.rate", 0) * 1000) + sPlayer.getLastTick() <= System.currentTimeMillis()) {
							int amt = Settings.basic.getInt("groups.users.regen.amount", 0);
							amt = (player.getHealth() + amt <= 20) ? player.getHealth() + amt : 20;
							player.setHealth(amt);
							sPlayer.setLastTick(System.currentTimeMillis());
							SimpleHealth.players.put(player, sPlayer);
//							String dbg = Settings.basic.getInt("groups.users.regen.rate", 0) + ":" + Settings.basic.getInt("groups.users.regen.amount", 0);
//							SimpleHealth.logAdd(sPlayer.toString() + " -> " + dbg);
						}
					}
				}
			}
		}
	}

}
