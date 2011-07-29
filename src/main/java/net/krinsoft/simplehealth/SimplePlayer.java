package net.krinsoft.simplehealth;

import org.bukkit.entity.Player;

public class SimplePlayer {
	private Player player;
	private String group;
	private boolean respawn;
	
	public SimplePlayer(Player player, String group) {
		setPlayer(player);
		setGroup(group);
	}

	public final void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public String getGroup() {
		return group;
	}
	
	public final void setGroup(String group) {
		this.group = group;
	}

	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}

	public boolean isRespawning() {
		return respawn;
	}
	
	@Override
	public String toString() {
		return "SimplePlayer{name=" + player.getName() + ",group=" + getGroup() + "}";
	}
}
