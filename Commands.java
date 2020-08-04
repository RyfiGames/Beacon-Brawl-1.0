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
//import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
//import org.bukkit.Bukkit;
//import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.CommandExecute;

public class Commands extends CommandExecute implements Listener, CommandExecutor {

	// public String cmd1 = "i";
	// public String cmd2 = "menu";
	public List<String> bbcommands = new ArrayList<String>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		{
			add("startbb");
			add("bbjoin");
			add("bbleave");
			add("bblist");
			add("bbmap");
			add("bbstartgens");
			add("bbstopgens");
			add("bbbegingame");
			add("bbdebug");
			add("bbmaps");
			add("bbplayer");
		}
	};

	public BBMain main;

	public void GetMain(BBMain m) {
		main = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			String cname = cmd.getName().toLowerCase();
			switch (cname) {
			/*
			 * case "i": if (args.length != 0) { Material item =
			 * Material.getMaterial(args[0].toUpperCase()); if (item != null) { Inventory
			 * inv = ((Player) sender).getInventory(); inv.addItem(new ItemStack(item, 1));
			 * return true; } else { sender.sendMessage(ChatColor.DARK_RED + args[0] +
			 * ChatColor.GOLD + " is not a valid item"); return true; } } else {
			 * sender.sendMessage(ChatColor.DARK_RED + "Not enough arguments."); return
			 * true; } case "menu": Player player = (Player) sender; CustomInventory i = new
			 * CustomInventory(); i.newInventory(player); return true;
			 */
			case "startbb":
				Player player1 = (Player) sender;
				if (main.gameMode.equals("off")) {
					if (player1.hasPermission("BeaconBrawl.startgame")) {
						main.startBBGame();
					} else {
						player1.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					}
				} else {
					player1.sendMessage(ChatColor.RED + "Game is in progress...");
				}
				return true;
			case "bbjoin":
				Player player2 = (Player) sender;
				if (main.lockedPlayers.contains(player2)) {
					player2.sendMessage(ChatColor.LIGHT_PURPLE + "You are locked in Beacon Brawl jail! (No commands for you!)");
					return true;
				}
				if (main.gameMode.equals("starting")) {
					if (args.length > 0) {
						if (args[0].equals("mod") && sender.hasPermission("BeaconBrawl.mod")) {
							main.JoinPlayer(player2, true);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "No need for extra arguments.");
						}
					} else {
						main.JoinPlayer(player2, false);
					}
				} else if (main.gameMode.equals("off")) {
					player2.sendMessage(ChatColor.RED + "No game is in progress...");
				} else {
					player2.sendMessage(ChatColor.RED + "Game is in progress...");
				}
				return true;
			case "bbleave":
				Player player3 = (Player) sender;
				if (main.lockedPlayers.contains(player3)) {
					player3.sendMessage(ChatColor.LIGHT_PURPLE + "You are locked in Beacon Brawl jail! (No commands for you!)");
					return true;
				}
				if (main.gameMode.equals("starting")) {
					main.LeavePlayer(player3);
				} else if (main.gameMode.equals("off")) {
					player3.sendMessage(ChatColor.RED + "No game is in progress...");
				} else {
					player3.sendMessage(ChatColor.RED + "Game is in progress...");
				}
				return true;
			case "bblist":
				if (sender.hasPermission("BeaconBrawl.listplayers")) {
					String result = ChatColor.AQUA + "Players joined: " + ChatColor.GREEN;
					for (Player p : main.bbplayers) {
						result += p.getDisplayName() + ", ";
					}

					sender.sendMessage(result);
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				}
				return true;
			case "bbmap":
				if (sender.hasPermission("BeaconBrawl.admin")) {
					MapsCommands(sender, args);
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				}
				return true;
			case "bbstartgens":
				// Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "0");
				if (sender.hasPermission("BeaconBrawl.admin")) {
					if (args.length >= 1
							&& main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[0])) {
						main.StartAllGenerators(args[0]);
						sender.sendMessage(ChatColor.GREEN + "Started!");
					} else {
						sender.sendMessage(ChatColor.RED + "Map does not exist.");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				}
				return true;
			case "bbstopgens":
				if (sender.hasPermission("BeaconBrawl.admin")) {
					if (args.length >= 1
							&& main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[0])) {
						main.StopAllGenerators(args[0]);
						sender.sendMessage(ChatColor.GREEN + "Stopped!");
					} else {
						sender.sendMessage(ChatColor.RED + "Map does not exist.");
					}
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				}
				return true;
			case "bbbegingame":
				if (main.gameMode.equals("starting")) {
					if (sender.hasPermission("BeaconBrawl.startgame")) {
						if (args.length >= 1) {
							if (main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[0])) {
								for (Player p : main.bbplayers) {
									p.teleport(main.GetMapPos(args[0], "positions", "spawn"));
									p.sendMessage(ChatColor.BLUE + sender.getName() + " overruled the vote. The map is "
											+ ChatColor.LIGHT_PURPLE + args[0] + "!");
								}
								main.BeginBBGame(args[0]);
							} else {
								sender.sendMessage(ChatColor.RED + "Map does not exist!");
							}
						} else {
							String winMap = main.TallyVotes();
							if (!winMap.equals("")) {
								for (Player p : main.bbplayers) {
									p.teleport(main.GetMapPos(winMap, "positions", "spawn"));
									p.sendMessage(ChatColor.LIGHT_PURPLE + winMap + ChatColor.BLUE + " won the vote!");
								}
								main.BeginBBGame(winMap);
							} else {
								sender.sendMessage(ChatColor.RED + "No votes were cast!");
							}
						}
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "No game to start.");
				}
				return true;
			case "bbmaps":
				CustomInventory cui = new CustomInventory();
				if (!main.bbplayers.contains((Player) sender)) {
					cui.openMaps((Player) sender, main);
				} else {
					if (main.gameMode.equals("starting")) {
						cui.openMaps((Player) sender, main);
					} else {
						sender.sendMessage(ChatColor.RED + "Not available during gameplay.");
					}
				}
				return true;
			case "bbplayer":
				if (sender.hasPermission("BeaconBrawl.admin")) {
					PlayerCommands(sender, args);
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to do that!");
				}
				return true;
			case "bbdebug":
				if (sender.hasPermission("BeaconBrawl.debug")) {
					if (args.length == 3) {
						if (args[0].equals("beacons")) {
							if (main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[1])) {
								String cmt = main.currentMap;
								main.currentMap = args[1];
								if (args[2].equals("good")) {
									main.SetBeaconGood(1, Material.SLIME_BLOCK);
									main.SetBeaconGood(2, Material.SLIME_BLOCK);
									main.SetBeaconGood(3, Material.SLIME_BLOCK);
									main.SetBeaconGood(4, Material.SLIME_BLOCK);
								} else if (args[2].equals("dead")) {
									main.SetBeaconGood(1, Material.STONE);
									main.SetBeaconGood(2, Material.STONE);
									main.SetBeaconGood(3, Material.STONE);
									main.SetBeaconGood(4, Material.STONE);
								} else {
									sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
								}
								main.currentMap = cmt;
							} else {
								sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
							}
						} else if (args[0].equals("nameplayer")) {
							Player pl = main.getServer().getPlayer(args[1]);
							// main.ChangeName(pl, args[2]);
							pl.setPlayerListName(args[2]);
							pl.setCustomName(args[2]);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
						}
					} else if (args.length == 2) {
						if (args[0].equals("debugmode")) {
							if (args[1].equals("true")) {
								main.debugMode = true;
								sender.sendMessage(ChatColor.ITALIC + "Debug mode set to TRUE.");
							} else {
								main.debugMode = false;
								sender.sendMessage(ChatColor.ITALIC + "Debug mode set to FALSE.");
							}
						} else if (args[0].equals("spawnshop")) {
							int n = Integer.parseInt(args[1]);
							if (n > 0 && n < 5) {
								main.SpawnShop(n);
							}
						} else if (args[0].equals("killshops")) {
							main.KillShops();
						} else if (args[0].equals("setmap")) {
							main.currentMap = args[1];
						} else if (args[0].equals("setplaying")) {
							main.gameMode = "playing";
							main.bbplayers.add((Player) sender);
						} else if (args[0].equals("resetchests")) {
							main.ResetChests();
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
						}

					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Invalid arguments.");
					}
				}
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Only players can use this command");
			return true;
		}
		return false;
	}

	public void MapsCommands(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "No argument. (list, modify, create)");
			return;
		}

		switch (args[0]) {
		case "list":
			Set<String> maps = main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false);
			String result = ChatColor.AQUA + "Maps: " + ChatColor.GREEN;
			for (String s : maps) {
				result += s + ", ";
			}
			sender.sendMessage(result);
			break;
		case "modify":
			if (args.length >= 4) {
				Player player = (Player) sender;
				if (main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[1])) {
					if (args[2].equals("setpos")) {
						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".world",
								player.getLocation().getWorld().getName());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".x",
								player.getLocation().getBlockX());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".y",
								player.getLocation().getBlockY());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".z",
								player.getLocation().getBlockZ());

						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".yaw",
								(double) player.getLocation().getYaw());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".positions." + args[3] + ".pitch",
								(double) player.getLocation().getPitch());

						main.cfgm.SaveMaps();
						main.cfgm.ReloadMaps();

						sender.sendMessage(ChatColor.GREEN + "Created Pos for map " + args[1]);
					} else if (args[2].equals("setgen") && args.length == 6) {
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".world",
								player.getLocation().getWorld().getName());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".x",
								player.getLocation().getBlockX());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".y",
								player.getLocation().getBlockY());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".z",
								player.getLocation().getBlockZ());
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".speed", args[4]);
						main.cfgm.GetMaps().set("Maps." + args[1] + ".gen." + args[3] + ".type", args[5]);

						main.cfgm.SaveMaps();
						main.cfgm.ReloadMaps();

						sender.sendMessage(ChatColor.GREEN + "Created Iron Gen for map " + args[1]);
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid argument. (setpos NAME, setgen NAME SPEED TYPE)");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Map " + args[1] + " does not exist.");
				}
			} else if (args.length == 1) {
				sender.sendMessage(ChatColor.AQUA + "Modify types include (setpos): team1, team2, team3, team4");
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid arguments. (modify NAME <setpos/setgen> KEY)");
			}
			break;
		case "create":
			if (args.length >= 2) {
				if (!main.cfgm.GetMaps().getConfigurationSection("Maps").getKeys(false).contains(args[1])) {
					main.cfgm.GetMaps().set("Maps." + args[1], "");
					main.cfgm.SaveMaps();
					main.cfgm.ReloadMaps();
					sender.sendMessage(ChatColor.GREEN + "Created Map " + args[1]);
					main.genProcessID.put(args[1], new ArrayList<Integer>());
				} else {
					sender.sendMessage(ChatColor.RED
							+ "A map with this name already exists! Either modify it or delete it in the config!");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid argument. (create NAME)");
			}
			break;
		default:
			sender.sendMessage(ChatColor.RED + "Invalid argument. (list, modify, create) You entered " + args[0]);
			return;
		}
	}

	public void PlayerCommands(CommandSender sender, String[] args) {
		Boolean force = false;
		
		for (String s : args) {
			if (s.equals("force")) {
				force = true;
			}
		}
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Missing args.");
			return;
		}
		Player player2 = main.getServer().getPlayer(args[1]);
		if (player2 == null) {
			sender.sendMessage(ChatColor.DARK_RED + args[1] + " is not online.");
			return;
		}
		switch (args[0]) {
		case "join":
			if (main.gameMode.equals("starting") || force) {
				main.JoinPlayer(player2, false);
			} else if (main.gameMode.equals("off")) {
				sender.sendMessage(ChatColor.RED + "No game is in progress...");
			} else {
				sender.sendMessage(ChatColor.RED + "Game is in progress...");
			}
			return;
		case "leave":
			if (main.gameMode.equals("starting") || force) {
				main.LeavePlayer(player2);
			} else if (main.gameMode.equals("off")) {
				sender.sendMessage(ChatColor.RED + "No game is in progress...");
			} else {
				sender.sendMessage(ChatColor.RED + "Game is in progress...");
			}
			return;
		case "team":
			if ((main.gameMode.equals("starting") || force) && main.bbplayers.contains(player2) && args.length > 2) {
				try {
					int tid = Integer.parseInt(args[2]);
					main.SetPlayerTeam(player2, tid);
				} catch (Exception e) {
					sender.sendMessage("Please enter team number id (1,2,3,4)");
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "The game must be starting, the player must be joined, and you must pick a team");
			}
			return;
		/*case "vote":
			if (main.bbplayers.contains(player2) && args.length > 2) {
				main.mapVotes.put(player2, args[2]);
				sender.sendMessage(ChatColor.BLUE + player2.getName() + " voted to play " + ChatColor.LIGHT_PURPLE + args[2]);
			} else {
				sender.sendMessage("Player must be joined in a game. You must pick a map.");
			}
			return; */
		case "lock":
			main.lockedPlayers.add(player2);
			sender.sendMessage(ChatColor.LIGHT_PURPLE + player2.getName() + " has been temporarily locked.");
			return;
		case "unlock":
			if (main.lockedPlayers.contains(player2)) {
				main.lockedPlayers.remove(player2);
				sender.sendMessage(ChatColor.LIGHT_PURPLE + player2.getName() + " has been unlocked.");
				player2.sendMessage(ChatColor.LIGHT_PURPLE + "You are unlocked from Beacon Brawl jail!");
			} else {
				sender.sendMessage(ChatColor.RED + "Player was not locked.");
			}
			return;
		}
	}

}
