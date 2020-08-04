/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
//import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.block.Chest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
//import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.util.Vector;

//import com.sun.tools.classfile.Opcode.Set;

import me.RyfiMagicman.BeaconBrawl.Events.EventsClass;
import net.md_5.bungee.api.ChatColor;
//import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
//import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class BBMain extends JavaPlugin {

	public Boolean debugMode = false;

	public ConfigManager cfgm;
	private Commands commands = new Commands();
	private EventsClass events = new EventsClass();
	public Scoreboards sbman;

	public String gameMode;
	public String currentMap = "";

	public int gameTimer = 0;

	public List<Player> bbplayers = new ArrayList<Player>();

	public Dictionary<Player, String> mapVotes = new Hashtable<Player, String>();

	public List<Player> red = new ArrayList<Player>();
	public List<Player> blue = new ArrayList<Player>();
	public List<Player> green = new ArrayList<Player>();
	public List<Player> yellow = new ArrayList<Player>();

	public int redTime;
	public int redAlarms;

	public int blueTime;
	public int blueAlarms;

	public int greenTime;
	public int greenAlarms;

	public int yellowTime;
	public int yellowAlarms;

	// public List<Integer> genProcessID = new ArrayList<>();
	public Dictionary<String, List<Integer>> genProcessID = new Hashtable<String, List<Integer>>();

	public List<Block> playerBlocks = new ArrayList<Block>();
	public List<Entity> playerShops = new ArrayList<Entity>();
	public List<CombatTag> combatTags = new ArrayList<CombatTag>();

	public List<ResourceItem> ri = new ArrayList<ResourceItem>();

	public List<Player> lockedPlayers = new ArrayList<Player>();

	public void onEnable() {

		LoadConfigManager();

		commands.GetMain(this);
		events.GetMain(this);

		// getCommand(commands.cmd1).setExecutor(commands);
		// getCommand(commands.cmd2).setExecutor(commands);
		for (String cmd : commands.bbcommands) {
			getCommand(cmd).setExecutor(commands);
		}

		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Beacon Brawl has been Enabled");
		getServer().getPluginManager().registerEvents(events, this);

		gameMode = "off";

		Set<String> maps = cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false);
		for (String mapName : maps) {
			// getServer().getConsoleSender().sendMessage(ChatColor.RED + mapName);
			genProcessID.put(mapName, new ArrayList<>());
		}

		sbman = new Scoreboards(this);
		// TestResourceItems();
	}

	public void LoadConfigManager() {
		cfgm = new ConfigManager();
		cfgm.Setup();
		cfgm.SaveMaps();
		cfgm.ReloadMaps();
	}

	public Boolean TestBlockSafe(Location block, String map) {
		Boolean safe = true;

		safe = safe && !IsInRange(GetMapPos(currentMap, "positions", "team1"), 2, block);
		safe = safe && !IsInRange(GetMapPos(currentMap, "positions", "team2"), 2, block);
		safe = safe && !IsInRange(GetMapPos(currentMap, "positions", "team3"), 2, block);
		safe = safe && !IsInRange(GetMapPos(currentMap, "positions", "team4"), 2, block);

		Set<String> gens = cfgm.GetMaps().getConfigurationSection("Maps." + currentMap + ".gen").getKeys(false);

		for (String g : gens) {
			safe = safe && !IsInRange(GetMapPos(currentMap, "gen", g), 3, block);
		}

		Location topc = GetMapPos(currentMap, "positions", "pos1");
		Location botc = GetMapPos(currentMap, "positions", "pos2");

		if (block.getX() < topc.getX() && block.getX() > botc.getX()) {
			if (block.getY() < topc.getY() && block.getY() > botc.getY()) {
				if (block.getZ() < topc.getZ() && block.getZ() > botc.getZ()) {
					safe = safe && true;
				} else {
					safe = false;
				}
			} else {
				safe = false;
			}
		} else {
			safe = false;
		}

		return safe;
	}

	public Boolean IsInRange(Location center, int radius, Location test) {
		if (center.getWorld().equals(test.getWorld())) {

			Location topc = new Location(center.getWorld(), center.getX() + radius, center.getY() + radius,
					center.getZ() + radius);
			Location botc = new Location(center.getWorld(), center.getX() - radius, center.getY() - radius,
					center.getZ() - radius);

			if (debugMode) {
				// System.out.println("[Debug] Top Corner: " + topc);
				// System.out.println("[Debug] Bottom Corner: " + botc);
			}

			if (test.getX() < topc.getX() && test.getX() > botc.getX()) {
				if (test.getY() < topc.getY() && test.getY() > botc.getY()) {
					if (test.getZ() < topc.getZ() && test.getZ() > botc.getZ()) {
						return true;
					}
				}
			}
		} else {
			System.out.println("[Debug] Not same world");
		}
		return false;
	}

	public Location GetMapPos(String map, String par, String pos) {
		String w = cfgm.GetMaps().getString("Maps." + map + "." + par + "." + pos + ".world");
		int x = cfgm.GetMaps().getInt("Maps." + map + "." + par + "." + pos + ".x");
		int y = cfgm.GetMaps().getInt("Maps." + map + "." + par + "." + pos + ".y");
		int z = cfgm.GetMaps().getInt("Maps." + map + "." + par + "." + pos + ".z");
		double yaw = cfgm.GetMaps().getDouble("Maps." + map + "." + par + "." + pos + ".yaw");
		double pitch = cfgm.GetMaps().getDouble("Maps." + map + "." + par + "." + pos + ".pitch");
		World wo = getServer().getWorld(w);

		Location wa = new Location(wo, x, y, z, (float) yaw, (float) pitch);

		return wa;
	}

	public void startBBGame() {
		gameMode = "starting";
		sbman.waitStatus = "Waiting...";

		for (Player player : getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.AQUA + "Beacon Brawl game is starting! Type " + ChatColor.RED + "/bbjoin"
					+ ChatColor.AQUA + " to join! (/bbleave to leave)");
		}
	}

	public void JoinPlayer(Player player, Boolean mod) {
		bbplayers.add(player);

		if (!mod) {
			for (Player players : getServer().getOnlinePlayers()) {
				players.sendMessage(ChatColor.RED + player.getName() + ChatColor.GREEN + " joined the Beacon Brawl");
			}

			player.getInventory().clear();
			CustomInventory ci = new CustomInventory();
			player.getInventory().addItem(ci.createShopItem(Material.WOOL, 1, (byte) 14, "Join Red", "", ""));
			player.getInventory().addItem(ci.createShopItem(Material.WOOL, 1, (byte) 11, "Join Blue", "", ""));
			player.getInventory().addItem(ci.createShopItem(Material.WOOL, 1, (byte) 5, "Join Green", "", ""));
			player.getInventory().addItem(ci.createShopItem(Material.WOOL, 1, (byte) 4, "Join Yellow", "", ""));
			player.getInventory().addItem(ci.createShopItem(Material.EMPTY_MAP, 1, (byte) 0, "Vote Map", "", ""));

			Random rand = new Random();
			SetPlayerTeam(player, rand.nextInt(4) + 1);
		}

		sbman.UpdateWaitingScoreboard(currentMap);
		player.setScoreboard(sbman.waitScoreboard);

	}

	public void LeavePlayer(Player player) {
		if (!bbplayers.contains(player)) {
			player.sendMessage(ChatColor.RED + "You are not playing Beacon Brawl");
		} else {
			bbplayers.remove(player);
			if (red.contains(player)) {
				red.remove(player);
			}
			if (blue.contains(player)) {
				blue.remove(player);
			}
			if (green.contains(player)) {
				green.remove(player);
			}
			if (yellow.contains(player)) {
				yellow.remove(player);
			}

			player.getInventory().clear();

			// player.setDisplayName(ChatColor.RESET + player.getDisplayName());

			for (Player players : getServer().getOnlinePlayers()) {
				players.sendMessage(ChatColor.RED + player.getName() + ChatColor.GREEN + " left the Beacon Brawl");
			}

			sbman.UpdateWaitingScoreboard(currentMap);
			player.setScoreboard(sbman.BlankScoreboard());

			if (currentMap != "") {
				player.teleport(GetMapPos(currentMap, "positions", "spawn"));
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "Beacon Brawl has been Disabled");
	}

	public void TestEndGame() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Boolean red0 = red.size() == 0;
				Boolean blue0 = blue.size() == 0;
				Boolean green0 = green.size() == 0;
				Boolean yellow0 = yellow.size() == 0;

				if (red0 && blue0 && green0 && yellow0) {
					EndGame("Stalemate");
				} else if (!red0 && blue0 && green0 && yellow0) {
					EndGame("Red");
				} else if (red0 && !blue0 && green0 && yellow0) {
					EndGame("Blue");
				} else if (red0 && blue0 && !green0 && yellow0) {
					EndGame("Green");
				} else if (red0 && blue0 && green0 && !yellow0) {
					EndGame("Yellow");
				} else {
					TestEndGame();
				}
			}
		}.runTaskLater(this, 1);
	}

	public void EndGame(String winner) {

		ChatColor color = ChatColor.WHITE;
		if (winner.equals("Red")) {
			color = ChatColor.RED;
		}
		if (winner.equals("Blue")) {
			color = ChatColor.BLUE;
		}
		if (winner.equals("Green")) {
			color = ChatColor.GREEN;
		}
		if (winner.equals("Yellow")) {
			color = ChatColor.YELLOW;
		}

		for (Player bbp : bbplayers) {
			bbp.sendMessage(color + winner + ChatColor.GREEN + " won the game!");
		}
		@SuppressWarnings("unchecked")
		List<Player> bbpc = (List<Player>) ((ArrayList<Player>) bbplayers).clone();
		for (Player bbp : bbpc) {
			LeavePlayer(bbp);
			bbp.setGameMode(GameMode.SURVIVAL);
			Location loc = GetMapPos(currentMap, "positions", "spawn");
			bbp.teleport(loc);
			// bbp.sendTitle(ChatColor.DARK_GREEN + winner, ChatColor.GOLD + "won the
			// game!");
			SendTitle(bbp, winner, color, "won the game!", ChatColor.GOLD);
		}

		bbpc.clear();

		SetBeaconGood(1, Material.GLASS);
		SetBeaconGood(2, Material.GLASS);
		SetBeaconGood(3, Material.GLASS);
		SetBeaconGood(4, Material.GLASS);

		for (Block b : playerBlocks) {
			// b.getLocation().getBlock().setType(Material.AIR);
			SetBlock(b.getLocation(), Material.AIR);
		}
		playerBlocks.clear();

		KillShops();

		Location l = GetMapPos(currentMap, "positions", "beacon1");
		for (Entity e : l.getWorld().getEntities()) {
			if (e.getType().equals(EntityType.DROPPED_ITEM)) {
				e.remove();
			}
		}
		ri.clear();

		combatTags.clear();

		StopAllGenerators(currentMap);
		currentMap = "";
		gameMode = "off";
	}

	public void StartAllGenerators(String mapName) {
		// Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "1");
		java.util.Set<String> gens = cfgm.GetMaps().getConfigurationSection("Maps." + mapName + ".gen").getKeys(false);
		// Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "2");

		for (String gen : gens) {
			// gen.equals("");
			// Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + gen);
			int x = cfgm.GetMaps().getInt("Maps." + mapName + ".gen." + gen + ".x");
			int y = cfgm.GetMaps().getInt("Maps." + mapName + ".gen." + gen + ".y");
			int z = cfgm.GetMaps().getInt("Maps." + mapName + ".gen." + gen + ".z");
			World w = getServer().getWorld(cfgm.GetMaps().getString("Maps." + mapName + ".gen." + gen + ".world"));
			String speed = cfgm.GetMaps().getString("Maps." + mapName + ".gen." + gen + ".speed");
			Material type = Material
					.getMaterial(cfgm.GetMaps().getString("Maps." + mapName + ".gen." + gen + ".type").toUpperCase());

			StartGenerator(Long.parseLong(speed), type, w, x, y, z, mapName);
		}
	}

	public void StopAllGenerators(String mapName) {
		for (int i : genProcessID.get(mapName)) {
			Bukkit.getScheduler().cancelTask(i);
		}

		genProcessID.get(mapName).clear();
	}

	public void StartGenerator(long sec, Material type, World world, int x, int y, int z, String mapName) {
		BBMain m = this;

		int newGen = new BukkitRunnable() {

			@Override
			public void run() {
				Location loc = new Location(world, x + 0.5, y + 0.5, z + 0.5);

				int itemCount = 0;
				Collection<Entity> es = world.getNearbyEntities(loc, 1, 1, 1);
				for (Entity e : es) {
					if (e.getType() == EntityType.DROPPED_ITEM) {
						if (((Item) e).getItemStack().getType() == type) {
							itemCount += ((Item) e).getItemStack().getAmount();
						}
					}
				}

				if (itemCount < 20) {
					// Item i = world.dropItemNaturally(loc, new ItemStack(type));
					// i.setVelocity(new Vector(0, 0, 0));
					ri.add(new ResourceItem(m, loc, type));
				}
			}

		}.runTaskTimer(this, 0, 20 * sec).getTaskId();

		// genProcessID.add(newGen);
		genProcessID.get(mapName).add(newGen);
	}

	public void SetPlayerTeam(Player player, int tid) {
		if (red.contains(player)) {
			red.remove(player);
		}
		if (blue.contains(player)) {
			blue.remove(player);
		}
		if (green.contains(player)) {
			green.remove(player);
		}
		if (yellow.contains(player)) {
			yellow.remove(player);
		}

		if (tid == 1) {
			red.add(player);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Red Team!");
		}
		if (tid == 2) {
			blue.add(player);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Blue Team!");
		}
		if (tid == 3) {
			green.add(player);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Green Team!");
		}
		if (tid == 4) {
			yellow.add(player);
			player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Yellow Team!");
		}

		sbman.UpdateWaitingScoreboard(currentMap);
	}

	public String TallyVotes() {
		Dictionary<String, Integer> counts = new Hashtable<String, Integer>();
		mapVotes.elements().asIterator().forEachRemaining(s -> {
			// System.out.println(s);
			if (counts.get(s) == null) {
				counts.put(s, 0);
			}
			int cc = counts.get(s);
			counts.put(s, cc + 1);
		});

		Dictionary<String, String> winner = new Hashtable<String, String>();
		Dictionary<String, Integer> winnerC = new Hashtable<String, Integer>();

		winner.put("top", "");
		winnerC.put("top", 0);
//		String[] winner = { "" };
//		int[] winnerC = { 0 };

		counts.keys().asIterator().forEachRemaining(s -> {
			// System.out.println(s);
			// System.out.println(counts.get(s));
			if (counts.get(s) > winnerC.get("top")) {
				// System.out.println("test");
				winner.put("top", s);
				winnerC.put("top", counts.get(s));
				// System.out.println("test");
			}
		});
		String top = winner.get("top");
		// System.out.println(top);

		mapVotes = new Hashtable<Player, String>();

		return top;
	}

	public void BeginBBGame(String mapName) {

		currentMap = mapName;

		for (Player players : getServer().getOnlinePlayers()) {
			players.sendMessage(ChatColor.GOLD + "Beacon Brawl starts in 10 Seconds...");
			players.playSound(players.getLocation(), Sound.NOTE_SNARE_DRUM, 3.0f, 1f);
		}
		sbman.waitStatus = "Starting in 10...";
		sbman.UpdateWaitingScoreboard(currentMap);

		for (int i = 1; i < 10; i++) {
			int s = 10 - i;
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Player players : getServer().getOnlinePlayers()) {
						players.sendMessage(ChatColor.GOLD + "Beacon Brawl starts in " + s + " Seconds...");
						players.playSound(players.getLocation(), Sound.NOTE_SNARE_DRUM, 3.0f, 1f);
					}
					sbman.waitStatus = "Starting in " + s + "...";
					sbman.UpdateWaitingScoreboard(currentMap);
				}

			}.runTaskLaterAsynchronously(this, 20 * i);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				if (red.size() > 0) {
					sbman.DelayedGameStatus("red", ChatColor.DARK_GREEN + "" + "Alive", 0);
				} else {
					sbman.DelayedGameStatus("red", ChatColor.GRAY + "" + "Empty", 0);
				}
				if (blue.size() > 0) {
					sbman.DelayedGameStatus("blue", ChatColor.DARK_GREEN + "" + "Alive", 0);
				} else {
					sbman.DelayedGameStatus("blue", ChatColor.GRAY + "" + "Empty", 0);
				}
				if (green.size() > 0) {
					sbman.DelayedGameStatus("green", ChatColor.DARK_GREEN + "" + "Alive", 0);
				} else {
					sbman.DelayedGameStatus("green", ChatColor.GRAY + "" + "Empty", 0);
				}
				if (yellow.size() > 0) {
					sbman.DelayedGameStatus("yellow", ChatColor.DARK_GREEN + "" + "Alive", 0);
				} else {
					sbman.DelayedGameStatus("yellow", ChatColor.GRAY + "" + "Empty", 0);
				}

				sbman.UpdateGameScoreboard();

				for (Player bbp : bbplayers) {
					// bbp.sendTitle(ChatColor.RED + "FIGHT!", "");
					SendTitle(bbp, "FIGHT", ChatColor.RED, "", ChatColor.WHITE);
					bbp.getInventory().clear();
					bbp.getInventory().setArmorContents(
							new ItemStack[] { new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1),
									new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1) });
					bbp.getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
					// bbp.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE, 1));

					bbp.setGameMode(GameMode.SURVIVAL);

					// bbp.setScoreboard(sbman.gameScoreboard);
				}

				sbman.SetTeamColors(1);
				sbman.SetTeamColors(2);
				sbman.SetTeamColors(3);
				sbman.SetTeamColors(4);

				for (Player bbp : bbplayers) {
					bbp.setScoreboard(sbman.teamBoards[0]);
				}

				for (Player rbbp : red) {
					WarpPlayer(rbbp, "team1");
					rbbp.setScoreboard(sbman.teamBoards[0]);
					sbman.SetPlayersTeams(rbbp, 1);
				}
				for (Player rbbp : blue) {
					WarpPlayer(rbbp, "team2");
					rbbp.setScoreboard(sbman.teamBoards[1]);
					sbman.SetPlayersTeams(rbbp, 2);
				}
				for (Player rbbp : green) {
					WarpPlayer(rbbp, "team3");
					rbbp.setScoreboard(sbman.teamBoards[2]);
					sbman.SetPlayersTeams(rbbp, 3);
				}
				for (Player rbbp : yellow) {
					WarpPlayer(rbbp, "team4");
					rbbp.setScoreboard(sbman.teamBoards[3]);
					sbman.SetPlayersTeams(rbbp, 4);
				}

				combatTags.clear();

				StartAllGenerators(mapName);
				gameMode = "playing";
				gameTimer = 1200;
				GameTimerCount();
				TestEndGame();

				redTime = 1000;
				blueTime = 1000;
				greenTime = 1000;
				yellowTime = 1000;

				redAlarms = 0;
				blueAlarms = 0;
				greenAlarms = 0;
				yellowAlarms = 0;

				SetBeaconGood(1, Material.SLIME_BLOCK);
				SetBeaconGood(2, Material.SLIME_BLOCK);
				SetBeaconGood(3, Material.SLIME_BLOCK);
				SetBeaconGood(4, Material.SLIME_BLOCK);

				TestBeaconTime(1);
				TestBeaconTime(2);
				TestBeaconTime(3);
				TestBeaconTime(4);

				for (int i = 1; i <= 4; i++) {
					SpawnShop(i);
				}

				ResetChests();

			}

		}.runTaskLater(this, 200);
	}

	public void SpawnShop(int i) {
		String s = Integer.toString(i);
		Location vs = GetMapPos(currentMap, "positions", "shop" + s);

		Entity e = vs.getWorld().spawnEntity(vs, EntityType.VILLAGER);
		e.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Shop");
		e.setCustomNameVisible(true);
		net.minecraft.server.v1_8_R3.Entity se = ((CraftEntity) e).getHandle();

		NBTTagCompound tag = se.getNBTTag();
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		se.c(tag);
		tag.setInt("NoAI", 1);
		tag.setInt("Silent", 1);
		tag.setInt("Invulnerable", 1);
		se.f(tag);

		playerShops.add(e);

		e.teleport(vs);
	}

	public void KillShops() {
		for (Entity e : playerShops) {
			e.remove();
		}
		playerShops.clear();
	}

	public void ResetChests() {
		for (int i = 1; i <= 4; i++) {
			String s = Integer.toString(i);
			Location ch = GetMapPos(currentMap, "positions", "chest" + s);

			if (ch.getWorld().getBlockAt(ch).getType().equals(Material.CHEST)) {
				new BukkitRunnable() {
					public void run() {
						((Chest) ch.getWorld().getBlockAt(ch).getState()).getInventory().clear();
					}
				}.runTask(this);
			} else {
				getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error clearing chest" + s);
			}
		}
	}

	public void SetBeaconGood(int tid, Material block) {
		String stid = Integer.toString(tid);
//		getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + wo + " " + x + " " + y + " " + z);

		String wo = cfgm.GetMaps().getString("Maps." + currentMap + ".positions.beacon" + stid + ".world");
		int x = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + stid + ".x");
		int y = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + stid + ".y");
		int z = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + stid + ".z");
		World w = getServer().getWorld(wo);

		// getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + wo + " " + x
		// + " " + y + " " + z);

		SetBlock(new Location(w, x, y - 1, z), block);

	}

	public void WarpPlayer(Player player, String warp) {
		if (cfgm.GetMaps().getConfigurationSection("Maps." + currentMap + ".positions").getKeys(false).contains(warp)) {
			String w = cfgm.GetMaps().getString("Maps." + currentMap + ".positions." + warp + ".world");
			int x = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions." + warp + ".x");
			int y = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions." + warp + ".y");
			int z = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions." + warp + ".z");
			float yaw = (float) cfgm.GetMaps().getDouble("Maps." + currentMap + ".positions." + warp + ".yaw");
			float pitch = (float) cfgm.GetMaps().getDouble("Maps." + currentMap + ".positions." + warp + ".pitch");
			World wo = getServer().getWorld(w);

			Location wa = new Location(wo, x + 0.5, y + 0.5, z + 0.5, yaw, pitch);
			player.teleport(wa);

		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Warp " + warp + " doesn't exist!");
		}
	}

	public void BreakBeacon(Player player, Location bloc) {

		int tid = 0;
		if (red.contains(player)) {
			tid = 1;
		}
		if (blue.contains(player)) {
			tid = 2;
		}
		if (green.contains(player)) {
			tid = 3;
		}
		if (yellow.contains(player)) {
			tid = 4;
		}

		for (int i = 1; i <= 4; i++) {

			List<Player> team = null;
			String teamn = "";

			String w = cfgm.GetMaps().getString("Maps." + currentMap + ".positions.beacon" + i + ".world");
			int x = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + i + ".x");
			int y = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + i + ".y");
			int z = cfgm.GetMaps().getInt("Maps." + currentMap + ".positions.beacon" + i + ".z");
			World wo = getServer().getWorld(w);

			Location bl = new Location(wo, x, y, z);
			if (bloc.equals(bl)) {
				if (tid == i) {
					player.sendMessage(ChatColor.RED + "Why would you break your own beacon?");
				} else {
					// wo.getBlockAt(x, y - 1, z).setType(Material.STONE);
					SetBeaconGood(i, Material.STONE);
					if (i == 1) {
						if (redTime > 0) {
							redTime = 0;
							team = red;
							teamn = "Red";
						} else {
							player.sendMessage(ChatColor.RED + "This beacon is already dead!");
							return;
						}
					}
					if (i == 2) {
						if (blueTime > 0) {
							blueTime = 0;
							team = blue;
							teamn = "Blue";
						} else {
							player.sendMessage(ChatColor.RED + "This beacon is already dead!");
							return;
						}
					}
					if (i == 3) {
						if (greenTime > 0) {
							greenTime = 0;
							team = green;
							teamn = "Green";
						} else {
							player.sendMessage(ChatColor.RED + "This beacon is already dead!");
							return;
						}
					}
					if (i == 4) {
						if (yellowTime > 0) {
							yellowTime = 0;
							team = yellow;
							teamn = "Yellow";
						} else {
							player.sendMessage(ChatColor.RED + "This beacon is already dead!");
							return;
						}
					}

					sbman.DelayedGameStatus(teamn.toLowerCase(), ChatColor.DARK_RED + "" + ChatColor.BOLD + "BROKEN",
							0);
					sbman.DelayedGameStatus(teamn.toLowerCase(), ChatColor.DARK_RED + "" + team.size() + " players", 3);

					for (Player bbp : bbplayers) {
						bbp.sendMessage(ChatColor.DARK_AQUA + teamn + " can no longer respawn!");
						bbp.playSound(bbp.getLocation(), Sound.ANVIL_BREAK, 3.0f, 1f);
					}
					for (Player rbbp : team) {
						// rbbp.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Beacon DEAD",
						// ChatColor.RED + "You can no longer respawn!");
						SendTitle(rbbp, "Beacon DEAD", ChatColor.DARK_GRAY, "You can no longer respawn!",
								ChatColor.RED);
					}
				}
			}
		}
	}

	public void TestBeaconTime(int tid) {

		if (debugMode) {
			getServer().getConsoleSender()
					.sendMessage(ChatColor.ITALIC + "[DEBUG] Test Beacon Time " + Integer.toString(tid));
		}

		/*
		 * new BukkitRunnable() {
		 * 
		 * @SuppressWarnings("deprecation")
		 * 
		 * @Override public void run() { if (tid == 1) { redTime--; if (redTime <= 0) {
		 * SetBeaconGood(tid, Material.STONE);
		 * 
		 * for (Player bbp : bbplayers) { bbp.sendMessage(ChatColor.DARK_AQUA +
		 * "Red can no longer respawn!"); bbp.playSound(bbp.getLocation(),
		 * Sound.ANVIL_BREAK, 3.0f, 1f); } for (Player rbbp : red) {
		 * rbbp.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Beacon DEAD",
		 * ChatColor.RED + "You can no longer respawn!"); } } else {
		 * TestBeaconTime(tid); } } else if (tid == 2) { blueTime--; if (blueTime <= 0)
		 * { SetBeaconGood(tid, Material.STONE);
		 * 
		 * for (Player bbp : bbplayers) { bbp.sendMessage(ChatColor.DARK_AQUA +
		 * "Blue can no longer respawn!"); bbp.playSound(bbp.getLocation(),
		 * Sound.ANVIL_BREAK, 3.0f, 1f); } for (Player rbbp : blue) {
		 * rbbp.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Beacon DEAD",
		 * ChatColor.RED + "You can no longer respawn!"); } } else {
		 * TestBeaconTime(tid); } } else if (tid == 3) { greenTime--; if (greenTime <=
		 * 0) { SetBeaconGood(tid, Material.STONE);
		 * 
		 * for (Player bbp : bbplayers) { bbp.sendMessage(ChatColor.DARK_AQUA +
		 * "Green can no longer respawn!"); bbp.playSound(bbp.getLocation(),
		 * Sound.ANVIL_BREAK, 3.0f, 1f); } for (Player rbbp : green) {
		 * rbbp.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Beacon DEAD",
		 * ChatColor.RED + "You can no longer respawn!"); } } else {
		 * TestBeaconTime(tid); } } else if (tid == 4) { yellowTime--; if (yellowTime <=
		 * 0) { SetBeaconGood(tid, Material.STONE);
		 * 
		 * for (Player bbp : bbplayers) { bbp.sendMessage(ChatColor.DARK_AQUA +
		 * "Yellow can no longer respawn!"); bbp.playSound(bbp.getLocation(),
		 * Sound.ANVIL_BREAK, 3.0f, 1f); } for (Player rbbp : yellow) {
		 * rbbp.sendTitle(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Beacon DEAD",
		 * ChatColor.RED + "You can no longer respawn!"); } } else {
		 * TestBeaconTime(tid); } } } }.runTaskLaterAsynchronously(this, 20);
		 */
	}

	public void AddBeaconTime(Player player) {
		if (red.contains(player)) {
			if (redTime > 0) {
				player.sendMessage(ChatColor.GREEN + "Accepted Skull for +100 seconds.");
				redTime += 100;
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
			}
		}
		if (blue.contains(player)) {
			if (blueTime > 0) {
				player.sendMessage(ChatColor.GREEN + "Accepted Skull for +100 seconds.");
				blueTime += 100;
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
			}
		}
		if (green.contains(player)) {
			if (greenTime > 0) {
				player.sendMessage(ChatColor.GREEN + "Accepted Skull for +100 seconds.");
				greenTime += 100;
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
			}
		}
		if (yellow.contains(player)) {
			if (yellowTime > 0) {
				player.sendMessage(ChatColor.GREEN + "Accepted Skull for +100 seconds.");
				yellowTime += 100;
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
			}
		}
	}

	public void ReviveBeacon(Player player) {

		if (red.contains(player)) {
			if (player.getInventory().contains(Material.NETHER_STAR, red.size())) {
				player.getInventory().remove(new ItemStack(Material.NETHER_STAR, red.size()));
			} else {
				player.sendMessage(ChatColor.RED + "No star, no revival.");
				player.closeInventory();
			}
			if (redTime > -100) {
				player.sendMessage(ChatColor.GREEN + "Accepted Star for revival.");
				player.closeInventory();
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
				player.closeInventory();
				return;
			}
			for (Player bbp : bbplayers) {
				// bbp.sendTitle(ChatColor.RED + "", ChatColor.DARK_GREEN + "Red Team Revived
				// their beacon!");
				SendTitle(bbp, "", ChatColor.RED, "Red Team Revived their beacon!", ChatColor.DARK_GREEN);
				bbp.sendMessage(ChatColor.DARK_GREEN + "Red Team Revived their beacon!");
			}
			redTime += 100;
			SetBeaconGood(1, Material.SLIME_BLOCK);
			sbman.DelayedGameStatus("red", ChatColor.AQUA + "" + ChatColor.BOLD + "REVIVED", 0);
			sbman.DelayedGameStatus("red", ChatColor.DARK_GREEN + "" + "Alive", 3);
		}
		if (blue.contains(player)) {
			if (player.getInventory().contains(Material.NETHER_STAR, blue.size())) {
				player.getInventory().remove(new ItemStack(Material.NETHER_STAR, blue.size()));
			} else {
				player.sendMessage(ChatColor.RED + "No star, no revival.");
				player.closeInventory();
			}
			if (blueTime > -100) {
				player.sendMessage(ChatColor.GREEN + "Accepted Star for revival.");
				player.closeInventory();
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
				player.closeInventory();
				return;
			}
			for (Player bbp : bbplayers) {
				// bbp.sendTitle(ChatColor.RED + "", "Blue Team Revived their beacon!");
				SendTitle(bbp, "", ChatColor.RED, "Blue Team Revived their beacon!", ChatColor.DARK_GREEN);
			}
			blueTime += 100;
			SetBeaconGood(2, Material.SLIME_BLOCK);
			sbman.DelayedGameStatus("blue", ChatColor.AQUA + "" + ChatColor.BOLD + "REVIVED", 0);
			sbman.DelayedGameStatus("blue", ChatColor.DARK_GREEN + "" + "Alive", 3);
		}
		if (green.contains(player)) {
			if (player.getInventory().contains(Material.NETHER_STAR, green.size())) {
				player.getInventory().remove(new ItemStack(Material.NETHER_STAR, green.size()));
			} else {
				player.sendMessage(ChatColor.RED + "No star, no revival.");
				player.closeInventory();
			}
			if (greenTime > -100) {
				player.sendMessage(ChatColor.GREEN + "Accepted Star for revival.");
				player.closeInventory();
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
				player.closeInventory();
				return;
			}
			for (Player bbp : bbplayers) {
				// bbp.sendTitle(ChatColor.RED + "", "Green Team Revived their beacon!");
				SendTitle(bbp, "", ChatColor.RED, "Green Team Revived their beacon!", ChatColor.DARK_GREEN);
			}
			greenTime += 100;
			SetBeaconGood(3, Material.SLIME_BLOCK);
			sbman.DelayedGameStatus("green", ChatColor.AQUA + "" + ChatColor.BOLD + "REVIVED", 0);
			sbman.DelayedGameStatus("green", ChatColor.DARK_GREEN + "" + "Alive", 3);
		}
		if (yellow.contains(player)) {
			if (player.getInventory().contains(Material.NETHER_STAR, yellow.size())) {
				player.getInventory().remove(new ItemStack(Material.NETHER_STAR, yellow.size()));
			} else {
				player.sendMessage(ChatColor.RED + "No star, no revival.");
				player.closeInventory();
			}
			if (yellowTime > -100) {
				player.sendMessage(ChatColor.GREEN + "Accepted Star for revival.");
				player.closeInventory();
			} else {
				player.sendMessage(ChatColor.RED + "Beacon is dead.");
				player.closeInventory();
				return;
			}
			for (Player bbp : bbplayers) {
				// bbp.sendTitle(ChatColor.RED + "", "Yellow Team Revived their beacon!");
				SendTitle(bbp, "", ChatColor.RED, "Yellow Team Revived their beacon!", ChatColor.DARK_GREEN);
			}
			yellowTime += 100;
			SetBeaconGood(4, Material.SLIME_BLOCK);
			sbman.DelayedGameStatus("yellow", ChatColor.AQUA + "" + ChatColor.BOLD + "REVIVED", 0);
			sbman.DelayedGameStatus("yellow", ChatColor.DARK_GREEN + "" + "Alive", 3);
		}
	}

	public void SetBlock(Location loc, Material block) {
		new BukkitRunnable() {
			@Override
			public void run() {
				loc.getWorld().getBlockAt(loc).setType(block);
			}
		}.runTask(this);
	}

	public void SendTitle(Player player, String title, ChatColor titleColor, String subtitle, ChatColor subColor) {
		IChatBaseComponent chatTitle = ChatSerializer
				.a("{\"text\": \"" + title + "\",color:" + titleColor.name().toLowerCase() + "}");
		IChatBaseComponent chatSub = ChatSerializer
				.a("{\"text\": \"" + subtitle + "\",color:" + subColor.name().toLowerCase() + "}");

		PacketPlayOutTitle title_ = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle subtitle_ = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSub);
		PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title_);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle_);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}

	public String GetTeamColor(Player player) {
		if (red.contains(player)) {
			return ChatColor.RED + "";
		}
		if (blue.contains(player)) {
			return ChatColor.BLUE + "";
		}
		if (green.contains(player)) {
			return ChatColor.GREEN + "";
		}
		if (yellow.contains(player)) {
			return ChatColor.YELLOW + "";
		}

		return ChatColor.WHITE + "";
	}

	public void RespawnTimer(Player player, int time) {

		player.setGameMode(GameMode.SPECTATOR);

		new BukkitRunnable() {
			@Override
			public void run() {
				int t = time;
				t--;
				if (t <= 0) {
					if (red.contains(player)) {
						WarpPlayer(player, "team1");
					} else if (blue.contains(player)) {
						WarpPlayer(player, "team2");
					} else if (green.contains(player)) {
						WarpPlayer(player, "team3");
					} else if (yellow.contains(player)) {
						WarpPlayer(player, "team4");
					}
					player.setGameMode(GameMode.SURVIVAL);
					return;
				}
				SendTitle(player, "Respawning in " + t + "...", ChatColor.RED, "", ChatColor.RED);
				RespawnTimer(player, t);
			}
		}.runTaskLater(this, 20);
	}

	public void GameTimerCount() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (gameMode.equals("playing")) {
					gameTimer--;
					if (gameTimer == 0) {
						redTime = -100;
						blueTime = -100;
						greenTime = -100;
						yellowTime = -100;

						for (Player bbp : bbplayers) {
							SendTitle(bbp, "BEACON PURGE", ChatColor.DARK_RED, "Nobody has a beacon.", ChatColor.GRAY);
							bbp.playSound(bbp.getLocation(), Sound.CHICKEN_HURT, 3.0f, 1.0f);
						}
					}
					
					int min = Math.floorDiv(gameTimer, 60);
					int sec = (int)(gameTimer % 60f);
					String formatTime = min + ":" + String.format("%02d", sec);
					String formatTime2 = (5 + min) + ":" + String.format("%02d", -sec);
					
					if (gameTimer > 0) {
						sbman.timeTitle = ChatColor.DARK_RED + "Beacon Purge";
						sbman.dispTime = formatTime;
					} else {
						sbman.timeTitle = ChatColor.AQUA + "Game End";
						sbman.dispTime = formatTime2;
					}
					sbman.UpdateGameScoreboard();
					
					if (gameTimer <= -300) {
						red.clear();
						blue.clear();
						green.clear();
						yellow.clear();
					} else {
						GameTimerCount();
					}
				} else {
					gameTimer = 0;
				}
			}
		}.runTaskLater(this, 20);
	}

	/*
	 * public void TestResourceItems() { new BukkitRunnable() {
	 * 
	 * @Override public void run() { for (ResourceItem i : ri) { if (!i.killMe) {
	 * List<Entity> nes = i.i.getNearbyEntities(1, 1, 1); for (Entity ne : nes) { if
	 * (ne.getType().equals(EntityType.DROPPED_ITEM)) { Item ni = (Item) ne; //
	 * Boolean contains = false; for (ResourceItem ris : ri) { if (ris.i == ni) {
	 * ris.killMe = true; ris.i = null; i.count++; if (i.count > 20) { i.count = 20;
	 * } } } } } } } List<ResourceItem> ril = new ArrayList<ResourceItem>();
	 * ril.addAll(ri); for (ResourceItem ris : ril) { if (ris.killMe) {
	 * ri.remove(ris); } } } }.runTaskTimer(this, 1, 1); } /* public void
	 * ChangeName(Player player, String newName) { String ogName = player.getName();
	 * EntityPlayer ep = ((CraftPlayer) player).getHandle();
	 * 
	 * ep.displayName = newName; ep.listName = ChatSerializer.a("{\"text\": \"" +
	 * newName + "\",color:" + ChatColor.WHITE.name().toLowerCase() + "}");
	 * player.setDisplayName(newName);
	 * 
	 * for (Player p : Bukkit.getOnlinePlayers()) { if (p != player) ((CraftPlayer)
	 * p).getHandle().playerConnection.sendPacket(new
	 * PacketPlayOutNamedEntitySpawn(ep)); } ep.displayName = ogName; }
	 */
}
