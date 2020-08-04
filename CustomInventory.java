/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import java.util.ArrayList;
//import java.util.Dictionary;
//import java.util.Hashtable;
import java.util.List;
import java.util.Set;

//import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class CustomInventory implements Listener {
	private Plugin plugin = BBMain.getPlugin(BBMain.class);
	
	public void newInventory(Player player) {
		Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_AQUA + "Test Inventory");
		
		int healthi = (int)player.getHealth();
		int foodi = (int)player.getFoodLevel();
		
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,(byte) 15);
		ItemMeta emptyMeta = empty.getItemMeta();
		emptyMeta.setDisplayName(" ");
		empty.setItemMeta(emptyMeta);
		
		ItemStack health = new ItemStack(Material.INK_SACK, healthi, (byte) 1);
		ItemMeta hMeta = health.getItemMeta();
		hMeta.setDisplayName(ChatColor.RED + "Health");
		health.setItemMeta(hMeta);
		ItemStack food = new ItemStack(Material.APPLE, foodi);
		ItemMeta fMeta = food.getItemMeta();
		fMeta.setDisplayName(ChatColor.YELLOW + "Food");
		food.setItemMeta(fMeta);
		
		for (int c = 0; c < 9; c++) {
			i.setItem(c, empty);
		}
		
		i.setItem(3, health);
		i.setItem(5, food);
		
		player.openInventory(i); 
	}
	
	public ItemStack createShopItem(Material m, int count, int d, String iname, String lore1, String lore2) {
		ItemStack i = new ItemStack(m, count,(byte) d);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.AQUA + iname);
		List<String> lore = new ArrayList<String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add(ChatColor.RESET + "" + ChatColor.GREEN + lore1);
			add(ChatColor.RESET + "" + ChatColor.GREEN + lore2);
		}};
		meta.setLore(lore);
		i.setItemMeta(meta);
		
		return i;
	}
	
	public void openShop(Player player) {
		Inventory i = plugin.getServer().createInventory(null, 54, ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + "Beacon Brawl Shop");
		
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,(byte) 15);
		ItemMeta emptyMeta = empty.getItemMeta();
		emptyMeta.setDisplayName(" ");
		empty.setItemMeta(emptyMeta);
		
		for (int c = 0; c < 54; c++) {
			i.setItem(c, empty);
		}
		
		i.setItem(10, createShopItem(Material.SANDSTONE, 16, 2, "Sandstone", "Building Block", "4 Iron"));
		//i.setItem(11, createShopItem(Material.WOOD_PICKAXE, 1, 0, "Wood Pick", "Tool", "10 Iron"));
		i.setItem(12, createShopItem(Material.LEATHER_CHESTPLATE, 1, 0, "Leather", "Armor", "15 Iron"));
		//i.setItem(13, createShopItem(Material.WOOD_SWORD, 1, 0, "Wood Sword", "Weapon", "10 Iron"));
		
		i.setItem(19, createShopItem(Material.ENDER_STONE, 16, 2, "Endstone", "Building Block", "5 Gold"));
		i.setItem(11, createShopItem(Material.STONE_PICKAXE, 1, 0, "Stone Pick", "Tool", "10 Iron"));
		i.setItem(21, createShopItem(Material.CHAINMAIL_CHESTPLATE, 1, 0, "Chain", "Armor", "15 Gold"));
		i.setItem(13, createShopItem(Material.STONE_SWORD, 1, 0, "Stone Sword", "Weapon", "10 Iron"));
		
		i.setItem(28, createShopItem(Material.WOOD, 16, 1, "Wood", "Building Block", "5 Diamond"));
		i.setItem(20, createShopItem(Material.IRON_PICKAXE, 1, 0, "Iron Pick", "Tool", "10 Gold"));
		i.setItem(30, createShopItem(Material.IRON_CHESTPLATE, 1, 0, "Iron", "Armor", "15 Diamond"));
		i.setItem(22, createShopItem(Material.IRON_SWORD, 1, 0, "Iron Sword", "Weapon", "10 Gold"));
		
		i.setItem(37, createShopItem(Material.OBSIDIAN, 8, 0, "Obsidian", "Building Block", "30 Emerald"));
		i.setItem(29, createShopItem(Material.DIAMOND_PICKAXE, 1, 0, "Diamond Pick", "Tool", "10 Emerald"));
		
		ItemStack is = createShopItem(Material.DIAMOND_PICKAXE, 1, 0, "God Pick", "Tool", "20 Emerald");
		is.addEnchantment(Enchantment.DIG_SPEED, 2);
		i.setItem(38, is);
		i.setItem(39, createShopItem(Material.DIAMOND_CHESTPLATE, 1, 0, "Diamond", "Armor", "15 Emerald"));
		i.setItem(31, createShopItem(Material.DIAMOND_SWORD, 1, 0, "Diamond Sword", "Weapon", "20 Emerald"));
		//i.setItem(40, createShopItem(Material.DIAMOND_SWORD, 1, 0, "Diamond Sword", "Weapon", "20 Emerald"));
		
		is = createShopItem(Material.DIAMOND_SWORD, 1, 0, "God Sword", "Weapon", "50 Emerald");
		is.addEnchantment(Enchantment.DAMAGE_ALL, 2);
		i.setItem(40, is);
		
		i.setItem(14, createShopItem(Material.SNOW_BALL, 16, 0, "Snowballs", "Projectile", "10 Iron"));
		i.setItem(23, createShopItem(Material.BOW, 1, 0, "Bow", "Weapon", "10 Diamond"));
		i.setItem(32, createShopItem(Material.ARROW, 32, 0, "Arrows", "Projectile", "5 Gold"));
		i.setItem(41, createShopItem(Material.ENDER_PEARL, 1, 0, "Ender Pearl", "Teleporter", "5 Redstone"));
		
		i.setItem(15, createShopItem(Material.GOLDEN_APPLE, 1, 0, "Gapple", "Effect", "8 Diamond"));
		
		ItemStack i2 = createShopItem(Material.POTION, 1, 8261, "Haste", "Effect", "15 Emerald");
		PotionMeta meta = (PotionMeta) i2.getItemMeta();
		meta.setMainEffect(PotionEffectType.FAST_DIGGING);
		i2.setItemMeta(meta);
		i.setItem(24, i2);
		
		//i.setItem(24, createShopItem(Material.POTION, 1, 8261, "Haste", "Effect", "15 Emerald"));
		i.setItem(33, createShopItem(Material.POTION, 1, 8235, "Jump Boost", "Effect", "20 Emerald"));
		i.setItem(42, createShopItem(Material.POTION, 1, 8238, "Invisibility", "Effect", "20 Emerald"));
		
		i.setItem(16, createShopItem(Material.INK_SACK, 1, 1, "Alarm", "Alarm", "10 Emerald"));
		i.setItem(25, createShopItem(Material.SKULL_ITEM, 1, 3, "Head", "Currency", "5 Redstone"));
		i.setItem(34, createShopItem(Material.DRAGON_EGG, 1, 0, "Dragon", "Spawnable", "50 Redstone"));
		i.setItem(43, createShopItem(Material.NETHER_STAR, 1, 0, "Second Chance", "Respawns your beacon", "10 Heads"));
		
		player.openInventory(i);
	}

	public void openBeacon(Player player, int teamTime, int teamCount) {
		Inventory i = plugin.getServer().createInventory(null, 9, ChatColor.DARK_PURPLE + "Team Beacon");
		
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,(byte) 15);
		ItemMeta emptyMeta = empty.getItemMeta();
		emptyMeta.setDisplayName(" ");
		empty.setItemMeta(emptyMeta);
		
		for (int c = 0; c < 9; c++) {
			i.setItem(c, empty);
		}
		
		//i.setItem(3, createShopItem(Material.STAINED_GLASS_PANE, 1, 14,"Right Click Item ->","Beacon Reset","1 Nether Star"));
		//i.setItem(5, createShopItem(Material.STAINED_GLASS_PANE, 1, 14,"<- Right Click Item","Beacon Reset","1 Nether Star"));
		if (teamTime <= -100) {
			i.setItem(4, createShopItem(Material.STAINED_GLASS_PANE, 1, 14, "Beacon Status:", "DEAD", "No Revivals"));
		} else if(teamTime <= 0) {
			i.setItem(4, createShopItem(Material.NETHER_STAR, teamCount, 0, "Revive Beacon", "Requires " + teamCount + " Nether Star", ""));
		} else {
			i.setItem(4, createShopItem(Material.STAINED_GLASS_PANE, 1, 5, "Beacon Status:", "Alive", ""));
		}

		player.openInventory(i);
	}
	
	public void openMaps(Player player, BBMain main) {
		
		Inventory i = plugin.getServer().createInventory(null, 54, ChatColor.BOLD + "Beacon Brawl Maps");
		
		ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1,(byte) 15);
		ItemMeta emptyMeta = empty.getItemMeta();
		emptyMeta.setDisplayName(" ");
		empty.setItemMeta(emptyMeta);
		
		for (int c = 0; c < 54; c++) {
			i.setItem(c, empty);
		}
		
		int counter = 10;
		Set<String> maps = main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false);
		for (String map : maps) {
			if (main.cfgm.GetMaps().getConfigurationSection("Maps." + map + ".positions").getKeys(false).contains("spawn")) {
				if (!main.cfgm.GetMaps().getConfigurationSection("Maps." + map).getKeys(false).contains("icon")) {
					main.cfgm.GetMaps().set("Maps." + map + ".icon", Material.GRASS.toString());
					main.cfgm.SaveMaps();
					main.cfgm.ReloadMaps();
				}
				Material mapIcon = Material.getMaterial(main.cfgm.GetMaps().getString("Maps." + map + ".icon"));
				i.setItem(counter, createShopItem(mapIcon, 1, 0, map, "Teleport to " + map, "Vote to play " + map));
				
				counter++;
				if (counter == 17 || counter == 26 || counter == 35 || counter == 44) {
					counter += 2;
				}
				
			} else {
				if (main.debugMode) {
					System.out.println("Map " + map + " has no spawn and will be skipped in map menu.");
				}
			}
		}
		
		player.openInventory(i);
	}
	
}
