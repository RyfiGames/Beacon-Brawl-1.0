/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ResourceItem {
	
	public BBMain main;
	
	public int pickUpDelay;
	//public int count;
	public List<Player> nearPlayers = new ArrayList<Player>();
	public Item i;
	
	public Boolean killMe = false;
	
	public ResourceItem(BBMain m, Location loc, Material type) {
		main = m;
		
		pickUpDelay = 7;
		//count = 1;
		
		i = loc.getWorld().dropItemNaturally(loc, new ItemStack(type, 1));
		i.setVelocity(new Vector(0, 0, 0));
		
		TestNear();
	}
	
	public void TestNear () {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (i != null) {
					i.setPickupDelay(300);
					nearPlayers.clear();
					
					List<Entity> es = i.getNearbyEntities(.5, .5, .5);
					for (Entity e : es) {
						if (e.getType().equals(EntityType.PLAYER) && ((Player)e).getGameMode().equals(GameMode.SURVIVAL)) {
							nearPlayers.add((Player)e);
						}
					}
					
					if (nearPlayers.size() > 0) {
						pickUpDelay--;
					} else {
						pickUpDelay = 10;
					}
					if (pickUpDelay <= 0) {
						for (Player p : nearPlayers) {
							p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 3.0f, 1f);
							p.getInventory().addItem(new ItemStack(i.getItemStack().getType(), 1));
						}
						nearPlayers.clear();
						i.remove();
						i = null;
						
					}
					
					TestNear();

				}
			}
			
		}.runTaskLater(main, 1);
	}
}
