/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;

public class ConfigManager {

	private BBMain main = BBMain.getPlugin(BBMain.class);
	
	// Files Here
	public FileConfiguration mapscfg;
	public File mapsFile;
	//------------------------------
	
	public void Setup() {
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
		}
		
		mapsFile = new File(main.getDataFolder(), "maps.yml");
		
		if (!mapsFile.exists()) {
			try {
				mapsFile.createNewFile();
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Beacon Brawl] maps.yml file created!");
			} catch (IOException e) {
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Beacon Brawl] Couldn't create maps.yml file!");
			}
		}
		
		mapscfg = YamlConfiguration.loadConfiguration(mapsFile);
	}
	
	public FileConfiguration GetMaps() {
		return mapscfg;
	}
	
	public void SaveMaps() {
		try {
			mapscfg.save(mapsFile);
			//Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Beacon Brawl] maps.yml file saved!");
		} catch (IOException e) {
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Beacon Brawl] Couldn't save maps.yml file!");
		}
	}
	
	public void ReloadMaps() {
		mapscfg = YamlConfiguration.loadConfiguration(mapsFile);
		//Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Beacon Brawl] maps.yml file reloaded!");
	}
}
