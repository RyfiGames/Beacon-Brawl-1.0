/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl.Events;

import java.util.ArrayList;
import java.util.List;

//import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Red;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import me.RyfiMagicman.BeaconBrawl.BBMain;
import me.RyfiMagicman.BeaconBrawl.CombatTag;
import me.RyfiMagicman.BeaconBrawl.CustomInventory;
import net.md_5.bungee.api.ChatColor;

public class EventsClass implements Listener {

	public BBMain main;

	public void GetMain(BBMain m) {
		main = m;
	}

	@EventHandler
	public void InvClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		// ClickType click = event.getClick();
		Inventory open = event.getClickedInventory();
		ItemStack item = event.getCurrentItem();
		if (open == null) {
			return;
		}
		if (open.getType().equals(InventoryType.CRAFTING) && main.bbplayers.contains(player)) {
			event.setCancelled(true);
		}
		
		if (open.getName().equals(ChatColor.DARK_AQUA + "Test Inventory")) {

			event.setCancelled(true);

			if (item == null || !item.hasItemMeta()) {
				return;
			}

			if (item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Health")) {
				player.setHealth(20);
				player.closeInventory();
				CustomInventory ci = new CustomInventory();
				ci.newInventory(player);
			}
		}

		if (open.getName().equals(ChatColor.DARK_PURPLE + "" + ChatColor.UNDERLINE + "Beacon Brawl Shop")) {

			event.setCancelled(true);

			if (item == null || !item.hasItemMeta()) {
				return;
			}

			String picked = item.getItemMeta().getDisplayName();
			String s = ChatColor.RESET + "" + ChatColor.AQUA;
			if (picked.equals(s + "Sandstone")) {
				if (PriceCheck(player, 4, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 4) });
					player.getInventory().addItem(new ItemStack(Material.SANDSTONE, 16, (byte) 2));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Endstone")) {
				if (PriceCheck(player, 5, 2)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 5) });
					player.getInventory().addItem(new ItemStack(Material.ENDER_STONE, 16));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Wood")) {
				if (PriceCheck(player, 5, 3)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 5) });
					player.getInventory().addItem(new ItemStack(Material.WOOD, 16, (byte) 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Obsidian")) {
				if (PriceCheck(player, 30, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 30) });
					player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 8));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Wood Pick")) {
				if (PriceCheck(player, 10, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Stone Pick")) {
				if (PriceCheck(player, 10, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Iron Pick")) {
				if (PriceCheck(player, 10, 2)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Diamond Pick")) {
				if (PriceCheck(player, 10, 3)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 10) });
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_PICKAXE, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Leather")) {
				if (PriceCheck(player, 15, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 15) });
					player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET, 1));
					player.getInventory().addItem(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
					player.getInventory().addItem(new ItemStack(Material.LEATHER_LEGGINGS, 1));
					player.getInventory().addItem(new ItemStack(Material.LEATHER_BOOTS, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Chain")) {
				if (PriceCheck(player, 15, 2)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 15) });
					player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_HELMET, 1));
					player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
					player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
					player.getInventory().addItem(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Iron")) {
				if (PriceCheck(player, 15, 3)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 15) });
					player.getInventory().addItem(new ItemStack(Material.IRON_HELMET, 1));
					player.getInventory().addItem(new ItemStack(Material.IRON_CHESTPLATE, 1));
					player.getInventory().addItem(new ItemStack(Material.IRON_LEGGINGS, 1));
					player.getInventory().addItem(new ItemStack(Material.IRON_BOOTS, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Diamond")) {
				if (PriceCheck(player, 15, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 15) });
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_HELMET, 1));
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Wood Sword")) {
				if (PriceCheck(player, 10, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Stone Sword")) {
				if (PriceCheck(player, 10, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.STONE_SWORD, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Iron Sword")) {
				if (PriceCheck(player, 10, 2)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Diamond Sword")) {
				if (PriceCheck(player, 20, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 20) });
					player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Snowballs")) {
				if (PriceCheck(player, 10, 1)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.IRON_INGOT, 10) });
					player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 16));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Bow")) {
				if (PriceCheck(player, 10, 3)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 10) });
					player.getInventory().addItem(new ItemStack(Material.BOW, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Arrows")) {
				if (PriceCheck(player, 5, 2)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 5) });
					player.getInventory().addItem(new ItemStack(Material.ARROW, 32));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Ender Pearl")) {
				if (PriceCheck(player, 5, 5)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.REDSTONE, 5) });
					player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Gapple")) {
				if (PriceCheck(player, 8, 3)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.DIAMOND, 8) });
					player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Haste")) {
				if (PriceCheck(player, 15, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 15) });
					CustomInventory ci = new CustomInventory();
					ItemStack is = ci.createShopItem(Material.POTION, 1, 8261, "Haste", "Effect", "15 Emerald");
					PotionMeta meta = (PotionMeta) is.getItemMeta();
					meta.setMainEffect(PotionEffectType.FAST_DIGGING);
					is.setItemMeta(meta);
					player.getInventory().addItem(is);
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Jump Boost")) {
				if (PriceCheck(player, 20, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 20) });
					player.getInventory().addItem(new ItemStack(Material.POTION, 1, (byte) 43));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Invisibility")) {
				if (PriceCheck(player, 20, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 20) });
					player.getInventory().addItem(new ItemStack(Material.POTION, 1, (byte) 8238));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Alarm")) {
				if (PriceCheck(player, 10, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 10) });
					// player.getInventory().addItem(new ItemStack(Material.POTION, 1, (byte)2));
					player.sendMessage(ChatColor.GREEN + "If an enemy approaches your base you will now be notified");

					if (main.red.contains(player)) {
						main.redAlarms++;
						for (Player rbbp : main.red) {
							rbbp.sendMessage(ChatColor.GREEN + "Your team has " + main.redAlarms + " alarms.");
						}
					}
					if (main.blue.contains(player)) {
						main.blueAlarms++;
						for (Player rbbp : main.blue) {
							rbbp.sendMessage(ChatColor.GREEN + "Your team has " + main.blueAlarms + " alarms.");
						}
					}
					if (main.green.contains(player)) {
						main.greenAlarms++;
						for (Player rbbp : main.green) {
							rbbp.sendMessage(ChatColor.GREEN + "Your team has " + main.greenAlarms + " alarms.");
						}
					}
					if (main.yellow.contains(player)) {
						main.yellowAlarms++;
						for (Player rbbp : main.yellow) {
							rbbp.sendMessage(ChatColor.GREEN + "Your team has " + main.yellowAlarms + " alarms.");
						}
					}

				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Head")) {
				if (PriceCheck(player, 5, 5)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.REDSTONE, 5) });
					player.getInventory().addItem(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Dragon")) {
				if (PriceCheck(player, 50, 5)) {
					// player.getInventory().removeItem(new ItemStack[] {
					// new ItemStack(Material.REDSTONE, 50) });
					// player.getInventory().addItem(new ItemStack(Material.POTION, 1, (byte)9));
					player.sendMessage(ChatColor.GREEN + "(Coming Soon)");
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "Second Chance")) {
				if (PriceCheck(player, 10, 6)) {
					player.getInventory()
							.removeItem(new ItemStack[] { new ItemStack(Material.SKULL_ITEM, 10, (byte) 3) });
					player.getInventory().addItem(new ItemStack(Material.NETHER_STAR, 1));
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "God Pick")) {
				if (PriceCheck(player, 20, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 20) });
					ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE, 1);
					is.addEnchantment(Enchantment.DIG_SPEED, 2);
					player.getInventory().addItem(is);
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			} else if (picked.equals(s + "God Sword")) {
				if (PriceCheck(player, 50, 4)) {
					player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.EMERALD, 50) });
					ItemStack is = new ItemStack(Material.DIAMOND_SWORD, 1);
					is.addEnchantment(Enchantment.DAMAGE_ALL, 2);
					player.getInventory().addItem(is);
				} else {
					player.sendMessage(ChatColor.RED + "Insufficient Funds.");
					player.closeInventory();
				}
			}
		}

		if (open.getName().contains("Team Beacon")) {
			/*
			 * if (event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
			 * player.closeInventory(); main.AddBeaconTime(player);
			 * player.getInventory().removeItem(new ItemStack[] {new
			 * ItemStack(Material.SKULL_ITEM, 1, (byte) 3)}); } else
			 */
			if (item.getType().equals(Material.NETHER_STAR)) {
				main.ReviveBeacon(player);
			} else {
				event.setCancelled(true);
			}

		}

		if (open.getName().contains("Beacon Brawl Maps")) {
			event.setCancelled(true);
			if (item.getItemMeta() != null) {

				String mapName = ChatColor.stripColor(item.getItemMeta().getDisplayName());

				if (mapName != " ") {
					if (!main.bbplayers.contains(player)) {
						Location tp2 = main.GetMapPos(mapName, "positions", "spawn");
						player.teleport(tp2);
					} else {
						main.mapVotes.put(player, mapName);
						player.sendMessage(ChatColor.BLUE + "You voted to play " + ChatColor.LIGHT_PURPLE + mapName);
					}
				}
			}
		}

	}

	public Boolean PriceCheck(Player player, int amount, int currency) {
		Boolean good = false;
		switch (currency) {
		case 1:
			good = player.getInventory().contains(Material.IRON_INGOT, amount);
			break;
		case 2:
			good = player.getInventory().contains(Material.GOLD_INGOT, amount);
			break;
		case 3:
			good = player.getInventory().contains(Material.DIAMOND, amount);
			break;
		case 4:
			good = player.getInventory().contains(Material.EMERALD, amount);
			break;
		case 5:
			good = player.getInventory().contains(Material.REDSTONE, amount);
			break;
		case 6:
			good = player.getInventory().contains(Material.SKULL_ITEM, amount);
			break;
		}

		if (good) {
			player.playSound(player.getLocation(), Sound.VILLAGER_YES, 3.0f, 1f);
		} else {
			player.playSound(player.getLocation(), Sound.VILLAGER_NO, 3.0f, 1f);
		}

		return good;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (main.gameMode.equals("starting")) {
			player.sendMessage(ChatColor.AQUA + "Beacon Brawl game is starting! Type " + ChatColor.RED + "/bbjoin"
					+ ChatColor.AQUA + " to join! (/bbleave to leave)");
		} else if (main.gameMode.equals("playing")) {
			//player.sendMessage("A game is currently playing, /bbspec to spectate");
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (main.bbplayers.contains(event.getPlayer())) {
			main.LeavePlayer(event.getPlayer());
			if (main.gameMode.equals("playing")) {
				event.getPlayer().setGameMode(GameMode.SPECTATOR);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked().getType() == EntityType.VILLAGER
				&& event.getRightClicked().getCustomName().contains("Shop")) {
			CustomInventory i = new CustomInventory();
			i.openShop(player);

			player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 3.0f, 1f);

			event.setCancelled(true);

		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block bloc = event.getBlock();
		if (main.bbplayers.contains(player)) {
			if (!main.playerBlocks.contains(bloc)) {
				if (bloc.getType().equals(Material.BEACON)) {
					main.BreakBeacon(player, bloc.getLocation());
				} else {
					player.sendMessage(ChatColor.RED + "Please DON'T break the map.");
				}
				event.setCancelled(true);
			} else {
				main.playerBlocks.remove(bloc);
			}
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block bloc = event.getBlockPlaced();
		Location loc = bloc.getLocation();
		if (main.bbplayers.contains(player) && main.gameMode.equals("playing")) {
			if (main.TestBlockSafe(loc, main.currentMap)
					&& event.getBlockReplacedState().getType().equals(Material.AIR)) {
				main.playerBlocks.add(bloc);
			} else {
				player.sendMessage(ChatColor.RED + "You can not place blocks outside or near spawns, gens, or liquid!");
				event.setCancelled(true);
			}
		}
		if (main.bbplayers.contains(player) && main.gameMode.equals("starting")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity damaged = event.getEntity();
		if (damaged.getType().equals(EntityType.PLAYER) && main.bbplayers.contains((Player) damaged)) {
			if (((Player) damaged).getHealth() - event.getDamage() <= 0 || event.getCause().equals(DamageCause.VOID)) {

				if (event.getCause().equals(DamageCause.FALL)) {
					if (((Player) damaged).getHealth() > 19) {
						event.setCancelled(true);
						return;
					}
				}

			//	((Player) damaged).getInventory().setArmorContents(
			//			new ItemStack[] { new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1),
			//					new ItemStack(Material.AIR, 1), new ItemStack(Material.AIR, 1) });
				((Player) damaged).setHealth(20);

				List<CombatTag> myCTs = new ArrayList<CombatTag>();
				List<CombatTag> bCTs = new ArrayList<CombatTag>();

				for (CombatTag ct : main.combatTags) {

					if (main.debugMode) {
						System.out.println("[DEBUG] Combat Tag: " + ct.player.getDisplayName() + " "
								+ ct.hitter.getDisplayName() + " " + ct.timeAgo);
					}

					if (ct.valid()) {
						if (ct.player.equals((Player) damaged)) {
							myCTs.add(ct);
						}
					} else {
						bCTs.add(ct);
					}
				}

				CombatTag newestCT = null;
				String killer = "themself.";

				for (CombatTag ct : myCTs) {

					if (newestCT == null) {
						newestCT = ct;
					} else if (newestCT.timeAgo < ct.timeAgo) {
						main.combatTags.remove(newestCT);
						newestCT = ct;
					} else {
						bCTs.add(ct);
					}
				}

				if (newestCT != null) {
					newestCT.hitter.getInventory().addItem(new ItemStack(Material.SKULL_ITEM, 1, (byte) 3));
					StealItems(newestCT.player, newestCT.hitter);
					killer = main.GetTeamColor(newestCT.hitter) + newestCT.hitter.getName();
					main.combatTags.remove(newestCT);
				}

				for (CombatTag ct : bCTs) {
					main.combatTags.remove(ct);
				}

				((Player) damaged).getInventory().clear();
				((Player) damaged).getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
				//((Player) damaged).getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE, 1));

				String deathMessage =  main.GetTeamColor((Player) damaged) + ((Player) damaged).getName() + ChatColor.AQUA
						+ " died to " + killer;

				if (main.red.contains(((Player) damaged))) {
					if (main.redTime > 0) {
						if (event.getCause() == DamageCause.VOID) {
							Location loc = main.GetMapPos(main.currentMap, "positions", "team1");
							((Player) damaged).teleport(loc);
						}
						main.RespawnTimer((Player)damaged, 5);
						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage);
						}
					} else {
						main.red.remove((Player) damaged);
						((Player) damaged).setGameMode(GameMode.SPECTATOR);
						Location loc = main.GetMapPos(main.currentMap, "positions", "spawn");
						((Player) damaged).teleport(loc);
						// ((Player) damaged).sendTitle("", ChatColor.RED + "You can no longer
						// respawn");
						main.SendTitle((Player) damaged, "", ChatColor.RED, "You can no longer respawn", ChatColor.RED);
						((Player) damaged).playSound(((Player) damaged).getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);

						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage + ChatColor.RED + ChatColor.BOLD + " NO RESPAWN!");
						}
						main.sbman.DelayedGameStatus("red", ChatColor.DARK_RED + "" + main.red.size() + " players", 0);
						if (main.red.size() == 0) {
							main.sbman.DelayedGameStatus("red", ChatColor.GRAY + "" + ChatColor.BOLD + "DEAD", 0);
						}
					}
				}
				if (main.blue.contains(((Player) damaged))) {
					if (main.blueTime > 0) {
						if (event.getCause() == DamageCause.VOID) {
							Location loc = main.GetMapPos(main.currentMap, "positions", "team2");
							((Player) damaged).teleport(loc);
						}
						main.RespawnTimer((Player)damaged, 5);
						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage);
						}
					} else {
						main.blue.remove((Player) damaged);
						((Player) damaged).setGameMode(GameMode.SPECTATOR);
						Location loc = main.GetMapPos(main.currentMap, "positions", "spawn");
						((Player) damaged).teleport(loc);
						// ((Player) damaged).sendTitle("", ChatColor.RED + "You can no longer
						// respawn");
						main.SendTitle((Player) damaged, "", ChatColor.RED, "You can no longer respawn", ChatColor.RED);
						((Player) damaged).playSound(((Player) damaged).getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);

						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage + ChatColor.RED + ChatColor.BOLD + " NO RESPAWN!");
						}
						main.sbman.DelayedGameStatus("blue", ChatColor.DARK_RED + "" + main.blue.size() + " players",
								0);
						if (main.blue.size() == 0) {
							main.sbman.DelayedGameStatus("blue", ChatColor.GRAY + "" + ChatColor.BOLD + "DEAD", 0);
						}
					}
				}
				if (main.green.contains(((Player) damaged))) {
					if (main.greenTime > 0) {
						if (event.getCause() == DamageCause.VOID) {
							Location loc = main.GetMapPos(main.currentMap, "positions", "team3");
							((Player) damaged).teleport(loc);
						}
						main.RespawnTimer((Player)damaged, 5);
						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage);
						}
					} else {
						main.green.remove((Player) damaged);
						((Player) damaged).setGameMode(GameMode.SPECTATOR);
						Location loc = main.GetMapPos(main.currentMap, "positions", "spawn");
						((Player) damaged).teleport(loc);
						// ((Player) damaged).sendTitle("", ChatColor.RED + "You can no longer
						// respawn");
						main.SendTitle((Player) damaged, "", ChatColor.RED, "You can no longer respawn", ChatColor.RED);
						((Player) damaged).playSound(((Player) damaged).getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);

						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage + ChatColor.RED + ChatColor.BOLD + " NO RESPAWN!");
						}
						main.sbman.DelayedGameStatus("green", ChatColor.DARK_RED + "" + main.green.size() + " players",
								0);
						if (main.green.size() == 0) {
							main.sbman.DelayedGameStatus("green", ChatColor.GRAY + "" + ChatColor.BOLD + "DEAD", 0);
						}
					}
				}
				if (main.yellow.contains(((Player) damaged))) {
					if (main.yellowTime > 0) {
						if (event.getCause() == DamageCause.VOID) {
							Location loc = main.GetMapPos(main.currentMap, "positions", "team4");
							((Player) damaged).teleport(loc);
						}
						main.RespawnTimer((Player)damaged, 5);
						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage);
						}
					} else {
						main.yellow.remove((Player) damaged);
						((Player) damaged).setGameMode(GameMode.SPECTATOR);
						Location loc = main.GetMapPos(main.currentMap, "positions", "spawn");
						((Player) damaged).teleport(loc);
						// ((Player) damaged).sendTitle("", ChatColor.RED + "You can no longer
						// respawn");
						main.SendTitle((Player) damaged, "", ChatColor.RED, "You can no longer respawn", ChatColor.RED);
						((Player) damaged).playSound(((Player) damaged).getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);

						for (Player bbp : main.bbplayers) {
							bbp.sendMessage(deathMessage + ChatColor.RED + ChatColor.BOLD + " NO RESPAWN!");
						}
						main.sbman.DelayedGameStatus("yellow",
								ChatColor.DARK_RED + "" + main.yellow.size() + " players", 0);
						if (main.yellow.size() == 0) {
							main.sbman.DelayedGameStatus("yellow", ChatColor.GRAY + "" + ChatColor.BOLD + "DEAD", 0);
						}
					}
				}

				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		if (event.getEntityType().equals(EntityType.PLAYER) && event.getDamager().getType().equals(EntityType.PLAYER)) {
			Player hurt = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();

			if (main.bbplayers.contains(hurt) && main.bbplayers.contains(damager)) {
				if (main.red.contains(hurt) && main.red.contains(damager)) {
					damager.sendMessage(ChatColor.RED + "Let's not friendly fire ok?");
					event.setCancelled(true);
				} else if (main.blue.contains(hurt) && main.blue.contains(damager)) {
					damager.sendMessage(ChatColor.RED + "Let's not friendly fire ok?");
					event.setCancelled(true);
				} else if (main.green.contains(hurt) && main.green.contains(damager)) {
					damager.sendMessage(ChatColor.RED + "Let's not friendly fire ok?");
					event.setCancelled(true);
				} else if (main.yellow.contains(hurt) && main.yellow.contains(damager)) {
					damager.sendMessage(ChatColor.RED + "Let's not friendly fire ok?");
					event.setCancelled(true);
				} else {
					main.combatTags.add(new CombatTag(main, hurt, damager));
				}
			}
		}
	}

	public void StealItems(Player hurt, Player damager) {
		Material[] currency = new Material[] { Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND,
				Material.EMERALD, Material.REDSTONE };
		int[] ccounts = new int[currency.length];
		for (ItemStack stack : hurt.getInventory().getContents()) {
			for (int i = 0; i < currency.length; i++) {
				if (stack != null && stack.getType() == currency[i]) {
					ccounts[i] += stack.getAmount();
				}
			}
		}

		for (int i = 0; i < currency.length; i++) {
			ItemStack ni = new ItemStack(currency[i], ccounts[i]);
			if (ccounts[i] > 0) {
				damager.getInventory().addItem(ni);
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e) {
		if (main.debugMode) {
			System.out.println("[DEBUG] " + ((Player) e.getPlayer()).getDisplayName() + " opened a "
					+ e.getInventory().getType().toString());

			if (e.getInventory().getType().equals(InventoryType.CHEST)) {
				if (e.getInventory().getHolder() instanceof Chest) {
					Location cloc = ((Chest) e.getInventory().getHolder()).getBlock().getLocation();
					System.out.println("Chest Location: " + cloc.toString());
				}
			}
		}

		if (main.bbplayers.contains((Player) e.getPlayer())) {
			if (e.getInventory().getType().equals(InventoryType.BEACON)) {
				e.setCancelled(true);
				CustomInventory ci = new CustomInventory();

				int tt = 0;
				int tc = 0;
				if (main.red.contains((Player) e.getPlayer())) {
					tt = main.redTime;
					tc = main.red.size();
				}
				if (main.blue.contains((Player) e.getPlayer())) {
					tt = main.blueTime;
					tc = main.blue.size();
				}
				if (main.green.contains((Player) e.getPlayer())) {
					tt = main.greenTime;
					tc = main.green.size();
				}
				if (main.yellow.contains((Player) e.getPlayer())) {
					tt = main.yellowTime;
					tc = main.yellow.size();
				}

				ci.openBeacon((Player) e.getPlayer(), tt, tc);
			}
			Player player = (Player) e.getPlayer();
			if (e.getInventory().getType().equals(InventoryType.CHEST)) {
				if (e.getInventory().getHolder() instanceof Chest) {

					Location cloc = ((Chest) e.getInventory().getHolder()).getBlock().getLocation();
					Location c1 = main.GetMapPos(main.currentMap, "positions", "chest1");
					Location c2 = main.GetMapPos(main.currentMap, "positions", "chest2");
					Location c3 = main.GetMapPos(main.currentMap, "positions", "chest3");
					Location c4 = main.GetMapPos(main.currentMap, "positions", "chest4");

					Block cb = cloc.getWorld().getBlockAt(cloc);
					Block c1b = c1.getWorld().getBlockAt(c1);
					Block c2b = c1.getWorld().getBlockAt(c2);
					Block c3b = c1.getWorld().getBlockAt(c3);
					Block c4b = c1.getWorld().getBlockAt(c4);

					if (cb.equals(c1b) && !main.red.contains(player) && main.red.size() > 0) {
						e.setCancelled(true);
						player.sendMessage(ChatColor.RED + "Gentleman's Rule! Don't go through other people's stuff.");
					}
					if (cb.equals(c2b) && !main.blue.contains(player) && main.blue.size() > 0) {
						e.setCancelled(true);
						player.sendMessage(ChatColor.RED + "Gentleman's Rule! Don't go through other people's stuff.");
					}
					if (cb.equals(c3b) && !main.green.contains(player) && main.green.size() > 0) {
						e.setCancelled(true);
						player.sendMessage(ChatColor.RED + "Gentleman's Rule! Don't go through other people's stuff.");
					}
					if (cb.equals(c4b) && !main.yellow.contains(player) && main.yellow.size() > 0) {
						e.setCancelled(true);
						player.sendMessage(ChatColor.RED + "Gentleman's Rule! Don't go through other people's stuff.");
					}
				}
			}
		}
	}

	@EventHandler
	public void onPickTeam(PlayerInteractEvent event) {
		// try {
		if (main.gameMode.equals("starting") && main.bbplayers.contains(event.getPlayer())) {
			Player player = event.getPlayer();
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR) && event.getItem().getType().equals(Material.WOOL) || event.getItem().getType().equals(Material.EMPTY_MAP)) {
				if (main.lockedPlayers.contains(player)) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "You are locked in Beacon Brawl jail! (No commands for you!)");
					return;
				}
				if (event.getItem().getItemMeta().getDisplayName().contains("Join Red")) {
					main.SetPlayerTeam(player, 1);
				}
				if (event.getItem().getItemMeta().getDisplayName().contains("Join Blue")) {
					main.SetPlayerTeam(player, 2);
				}
				if (event.getItem().getItemMeta().getDisplayName().contains("Join Green")) {
					main.SetPlayerTeam(player, 3);
				}
				if (event.getItem().getItemMeta().getDisplayName().contains("Join Yellow")) {
					main.SetPlayerTeam(player, 4);
				}
				if (event.getItem().getItemMeta().getDisplayName().contains("Vote Map")) {
					event.setCancelled(true);
					CustomInventory cui = new CustomInventory();
					if (!main.bbplayers.contains(player)) {
						cui.openMaps(player, main);
					} else {
						if (main.gameMode.equals("starting")) {
							cui.openMaps(player, main);
						} else {
							player.sendMessage(ChatColor.RED + "Not available during gameplay.");
						}
					}
				}
			}
		}
		// } catch (Exception e) {

		// }
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (main.gameMode.equals("playing")) {
			Boolean onTeam = main.red.contains(event.getPlayer());
			onTeam = onTeam || main.blue.contains(event.getPlayer());
			onTeam = onTeam || main.green.contains(event.getPlayer());
			onTeam = onTeam || main.yellow.contains(event.getPlayer());

			if (main.bbplayers.contains(event.getPlayer()) && onTeam) {
				Player player = event.getPlayer();
				Location ploc = player.getLocation();
				Location b1 = main.GetMapPos(main.currentMap, "positions", "beacon1");
				Location b2 = main.GetMapPos(main.currentMap, "positions", "beacon2");
				Location b3 = main.GetMapPos(main.currentMap, "positions", "beacon3");
				Location b4 = main.GetMapPos(main.currentMap, "positions", "beacon4");

				if (main.debugMode) {
					if (main.IsInRange(b1, 10, ploc)) {
						System.out.println("[DEBUG] " + player.getDisplayName() + " has entered Red");
					}
					if (main.IsInRange(b2, 10, ploc)) {
						System.out.println("[DEBUG] " + player.getDisplayName() + " has entered Blue");
					}
					if (main.IsInRange(b3, 10, ploc)) {
						System.out.println("[DEBUG] " + player.getDisplayName() + " has entered Green");
					}
					if (main.IsInRange(b4, 10, ploc)) {
						System.out.println("[DEBUG] " + player.getDisplayName() + " has entered Yellow");
					}
				}

				if (main.IsInRange(b1, 10, ploc) && main.redAlarms > 0 && !main.red.contains(player)
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					main.redAlarms--;

					for (Player rbbp : main.red) {
						// rbbp.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "ALARM TRIGGERED",
						// ChatColor.GRAY + "An enemy is at your base!");
						main.SendTitle(rbbp, "ALARM TRIGGERED", ChatColor.RED, "An enemy is at your base!",
								ChatColor.GRAY);
						rbbp.playSound(rbbp.getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);
					}
					PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 200, 1);
					player.addPotionEffect(pe);
				}
				if (main.IsInRange(b2, 10, ploc) && main.blueAlarms > 0 && !main.blue.contains(player)
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					main.blueAlarms--;

					for (Player rbbp : main.blue) {
						// rbbp.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "ALARM TRIGGERED",
						// ChatColor.GRAY + "An enemy is at your base!");
						main.SendTitle(rbbp, "ALARM TRIGGERED", ChatColor.RED, "An enemy is at your base!",
								ChatColor.GRAY);
						rbbp.playSound(rbbp.getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);
					}
					PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 200, 1);
					player.addPotionEffect(pe);
				}
				if (main.IsInRange(b3, 10, ploc) && main.greenAlarms > 0 && !main.green.contains(player)
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					main.greenAlarms--;

					for (Player rbbp : main.green) {
						// rbbp.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "ALARM TRIGGERED",
						// ChatColor.GRAY + "An enemy is at your base!");
						main.SendTitle(rbbp, "ALARM TRIGGERED", ChatColor.RED, "An enemy is at your base!",
								ChatColor.GRAY);
						rbbp.playSound(rbbp.getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);
					}
					PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 200, 1);
					player.addPotionEffect(pe);
				}
				if (main.IsInRange(b4, 10, ploc) && main.yellowAlarms > 0 && !main.yellow.contains(player)
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					main.yellowAlarms--;

					for (Player rbbp : main.yellow) {
						// rbbp.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "ALARM TRIGGERED",
						// ChatColor.GRAY + "An enemy is at your base!");
						main.SendTitle(rbbp, "ALARM TRIGGERED", ChatColor.RED, "An enemy is at your base!",
								ChatColor.GRAY);
						rbbp.playSound(rbbp.getLocation(), Sound.BAT_DEATH, 3.0f, 1.0f);
					}
					PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS, 200, 1);
					player.addPotionEffect(pe);
				}
			}
		}
	}

}
