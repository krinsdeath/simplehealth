package net.krinsoft.simplehealth;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

@SuppressWarnings("unused")
public class EListener extends EntityListener {
	private SimpleHealth plugin;
	
	public EListener(SimpleHealth instance) {
		plugin = instance;
	}

	@Override
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getRegainReason().equals(RegainReason.REGEN)) {
				if (Settings.basic.getStringList("settings.natural_regen", new ArrayList<String>()).contains(event.getEntity().getWorld().getName())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) { return; }
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent evt = (EntityDamageByEntityEvent) event;
			if (evt.getDamager() instanceof Player && event.getEntity() instanceof Player) {
				if (Settings.basic.getStringList("plugin.pvp", new ArrayList<String>()).contains(evt.getDamager().getWorld().getName())) {
					event.setCancelled(true);
					return;
				}
			}
		}
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			SimplePlayer sPlayer = SimpleHealth.players.get(player);
			if (sPlayer == null) { sPlayer = Settings.addNewUser(player); }
			if (!SimpleTimer.hasTask(player.getName())) {
				int rate = Settings.basic.getInt("groups." + sPlayer.getGroup() + ".regen.rate", 30);
				SimpleTimer tmp = new SimpleTimer(player.getName());
				tmp.setTaskId(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, tmp, rate * 20, rate * 20));
				SimpleTimer.addTask(player.getName(), tmp);
			}
		}
	}
}
