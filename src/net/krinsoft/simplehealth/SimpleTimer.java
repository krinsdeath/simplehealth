package net.krinsoft.simplehealth;

import java.util.TimerTask;

import org.bukkit.entity.Player;

public class SimpleTimer extends TimerTask {
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
		// Iterate through online players
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			SimplePlayer sPlayer = SimpleHealth.players.get(player);
			if (sPlayer == null) { sPlayer = Settings.addNewUser(player); }
			// Iterate through the groups
			if (sPlayer.isRespawning() && player.getHealth() > 0) {
				player.setHealth(Settings.basic.getInt("groups." + sPlayer.getGroup() + ".events._respawn", 1));
				sPlayer.setRespawn(false);
			}
			for (String key : Settings.basic.getKeys("groups")) {
				// Check for permissions
				if (perm) {
					// Check if the player has the current key in his permission nodes
					if (Settings.permissions.has(player, "simplehealth." + key)) {
						if ((Settings.basic.getInt("groups." + key + ".regen.rate", 0) * 1000) + sPlayer.getLastTick() <= System.currentTimeMillis()) {
							int amt = Settings.basic.getInt("groups.admins.regen.amount", 0);
							amt = (player.getHealth() + amt <= 20) ? player.getHealth() + amt : 20;
							player.setHealth(amt);
							sPlayer.setLastTick(System.currentTimeMillis());
							SimpleHealth.players.put(player, sPlayer);
						}
						break;
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
						}
						break;
					} else {
						if ((Settings.basic.getInt("groups.users.regen.rate", 0) * 1000) + sPlayer.getLastTick() <= System.currentTimeMillis()) {
							int amt = Settings.basic.getInt("groups.users.regen.amount", 0);
							amt = (player.getHealth() + amt <= 20) ? player.getHealth() + amt : 20;
							player.setHealth(amt);
							sPlayer.setLastTick(System.currentTimeMillis());
							SimpleHealth.players.put(player, sPlayer);
						}
						break;
					}
				}
			}
		}
	}

}
