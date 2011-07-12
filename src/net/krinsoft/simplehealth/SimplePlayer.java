package net.krinsoft.simplehealth;

import org.bukkit.entity.Player;

public class SimplePlayer {
	private Player player;
	private long lastTick;
	private String group;
	private boolean respawn;
	
	public SimplePlayer(Player player, String group) {
		setPlayer(player);
		setGroup(group);
		setLastTick(System.currentTimeMillis());
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public void setLastTick(long lastTick) {
		this.lastTick = lastTick;
	}

	public long getLastTick() {
		return lastTick;
	}

	public String getGroup() {
		// TODO Auto-generated method stub
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}

	public void setRespawn(boolean respawn) {
		this.respawn = respawn;
	}

	public boolean isRespawning() {
		return respawn;
	}
}
