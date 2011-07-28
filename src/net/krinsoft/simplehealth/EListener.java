package net.krinsoft.simplehealth;

import java.util.ArrayList;

import org.bukkit.entity.Player;
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
				if (Settings.basic.getStringList("settings.peaceful_worlds", new ArrayList<String>()).contains(event.getEntity().getWorld().getName())) {
					event.setCancelled(true);
				}
			}
		}
	}

}
