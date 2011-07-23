package net.krinsoft.simplehealth;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PListener extends PlayerListener {
	public SimpleHealth plugin;
	public boolean perm = false;
	
	public PListener(SimpleHealth instance) {
		plugin = instance;
	}

	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled()) { return; }
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			SimplePlayer sPlayer = SimpleHealth.players.get(player);
			if (sPlayer == null) { sPlayer = Settings.addNewUser(player); }
			String mat = event.getMaterial().toString().toLowerCase();
			if (mat == null) { return; }
			if (Settings.basic.getKeys("groups." + sPlayer.getGroup() + ".items") == null) { return; }
			if (Settings.basic.getKeys("groups." + sPlayer.getGroup() + ".items").contains(mat)) {
				ItemStack item = player.getItemInHand();
				int hp = Settings.basic.getInt("groups." + sPlayer.getGroup() + ".items." + mat, 0);
				if (player.getHealth() + hp > 20) { return; }
				hp = (player.getHealth() + hp <= 20) ? player.getHealth() + hp : 20;
				item.setAmount(item.getAmount() - 1);
				player.setItemInHand(item);
				player.setHealth(hp);
				event.setCancelled(true);
			}
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		SimpleHealth.players.get(player).setRespawn(true);
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.isCancelled()) { return; }
		Player player = event.getPlayer();
		String cmd = event.getMessage().split(" ")[0].substring(1).toLowerCase();
		// check for permissions
		if (Settings.basic.getBoolean("plugin.permissions", false)) {
			// permissions is used
			for (String key : Settings.basic.getKeys("groups")) {
				if (Settings.permissions.has(player, "simplehealth." + key) || player.hasPermission("simplehealth." + key)) {
					if (Settings.basic.getStringList("groups." + key + ".disabled_commands", new ArrayList<String>()).contains(key)) {
						// command is listed as disabled for this group
						event.setCancelled(true);
						return;
					}
				}
			}
		} else {
			if (player.isOp() && Settings.basic.getStringList("groups.admins.disabled_commands", new ArrayList<String>()).contains(cmd)) {
				// Player is op, and the command is in his disabled commands list
				event.setCancelled(true);
				return;
			} else {
				// player is not op
				if (Settings.basic.getStringList("groups.users.disabled_commands", new ArrayList<String>()).contains(cmd)) {
					// command was found in user's disabled commands list
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
}
