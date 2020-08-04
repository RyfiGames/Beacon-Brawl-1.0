/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatTag {
	
	public BBMain main;
	
	public Player player;
	public Player hitter;
	public int timeAgo;
	
	public CombatTag(BBMain main, Player player, Player hitter) {
		this.main = main;
		this.player = player;
		this.hitter = hitter;
		
		timeAgo = 30;
		
		TimePass();
	}
	
	public void TimePass () {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (timeAgo > 0) {
					timeAgo--;
					TimePass();
				}
			}
		}.runTaskLater(main, 20);
	}
	
	public Boolean valid() {
		return timeAgo > 0;
	}
}
