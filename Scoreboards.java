/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

//import java.util.ArrayList;
//import java.util.Dictionary;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
//import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;

public class Scoreboards {

	BBMain main;
	ScoreboardManager manager;

//	public Map<String,Scoreboard> sbs = new HashMap<String,Scoreboard>();
//	public Map<String,List<Score>> scores = new HashMap<String,List<Score>>();

	public Scoreboard waitScoreboard;
	public Objective waitObjective;
	public String waitStatus = "Waiting...";

//	public Scoreboard gameScoreboard;
	public Objective[] gameObjective = new Objective[4];
	String redStatus;
	String blueStatus;
	String greenStatus;
	String yellowStatus;
	int redDelayedStatus;
	int blueDelayedStatus;
	int greenDelayedStatus;
	int yellowDelayedStatus;
	
	public String timeTitle = "";
	public String dispTime = "";

	public Scoreboard[] teamBoards = new Scoreboard[4];
	String[] teamPrefix = new String[4];

	public Scoreboards(BBMain m) {
		main = m;
		manager = Bukkit.getScoreboardManager();

		waitScoreboard = manager.getNewScoreboard();
		waitObjective = waitScoreboard.registerNewObjective("Wait", "");

//		gameScoreboard = manager.getNewScoreboard();
//		gameObjective = gameScoreboard.registerNewObjective("Game", "");

		teamBoards[0] = manager.getNewScoreboard();
		teamBoards[1] = manager.getNewScoreboard();
		teamBoards[2] = manager.getNewScoreboard();
		teamBoards[3] = manager.getNewScoreboard();
		
		teamPrefix[0] = ChatColor.RED + " ■";
		teamPrefix[1] = ChatColor.BLUE + " ■";
		teamPrefix[2] = ChatColor.GREEN + " ■";
		teamPrefix[3] = ChatColor.YELLOW + " ■";
		
		teamBoards[0].registerNewObjective("health", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
		teamBoards[1].registerNewObjective("health", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
		teamBoards[2].registerNewObjective("health", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
		teamBoards[3].registerNewObjective("health", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
		
	}

	public Scoreboard BlankScoreboard() {
		return manager.getNewScoreboard();
	}

	// Custom Stuff

	public void UpdateWaitingScoreboard(String map) {

		waitObjective.unregister();
		waitObjective = waitScoreboard.registerNewObjective("Wait", "");

		waitObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		waitObjective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Beacon Brawl");

		Score blank = waitObjective.getScore(" ");
		blank.setScore(15);

		Score status = waitObjective.getScore(ChatColor.WHITE + "Status: " + ChatColor.GREEN + waitStatus);
		status.setScore(14);

		if (map != "") {
			Score maps = waitObjective.getScore(ChatColor.WHITE + "Map: " + ChatColor.LIGHT_PURPLE + map);
			maps.setScore(14);
		}

		blank = waitObjective.getScore("  ");
		blank.setScore(13);

		Score players = waitObjective.getScore(ChatColor.WHITE + "" + ChatColor.BOLD + "Players: ");
		players.setScore(12);

		int sp = 11;

		String teampns = "";
		String testpns = "";
		for (Player p : main.red) {
			testpns = teampns + p.getName() + ", ";

			if (testpns.length() < 40) {
				teampns = testpns;
				testpns = "";
			} else {
				if (sp > 1) {
					String cut = teampns.substring(0, teampns.length() - 2);
					Score pls = waitObjective.getScore(ChatColor.RED + cut);
					pls.setScore(sp);
					teampns = "";

					teampns = p.getName() + ", ";
					testpns = "";
				}
				sp--;
			}
		}
		if (teampns.length() > 1) {
			String cut = teampns.substring(0, teampns.length() - 2);
			Score pls = waitObjective.getScore(ChatColor.RED + cut);
			pls.setScore(sp);
			//teampns = "";

			teampns = "";
			testpns = "";
			sp--;
		}

		for (Player p : main.blue) {
			testpns = teampns + p.getName() + ", ";

			if (testpns.length() < 40) {
				teampns = testpns;
				testpns = "";
			} else {
				if (sp > 1) {
					String cut = teampns.substring(0, teampns.length() - 2);
					Score pls = waitObjective.getScore(ChatColor.BLUE + cut);
					pls.setScore(sp);
					teampns = "";

					teampns = p.getName() + ", ";
					testpns = "";
				}
				sp--;
			}
		}
		if (teampns.length() > 1) {
			String cut = teampns.substring(0, teampns.length() - 2);
			Score pls = waitObjective.getScore(ChatColor.BLUE + cut);
			pls.setScore(sp);
			teampns = "";

			//teampns = testpns;
			testpns = "";
			sp--;
		}
		for (Player p : main.green) {
			testpns = teampns + p.getName() + ", ";

			if (testpns.length() < 40) {
				teampns = testpns;
				testpns = "";
			} else {
				if (sp > 1) {
					String cut = teampns.substring(0, teampns.length() - 2);
					Score pls = waitObjective.getScore(ChatColor.GREEN + cut);
					pls.setScore(sp);
					teampns = "";

					teampns = p.getName() + ", ";
					testpns = "";
				}
				sp--;
			}
		}
		if (teampns.length() > 1) {
			String cut = teampns.substring(0, teampns.length() - 2);
			Score pls = waitObjective.getScore(ChatColor.GREEN + cut);
			pls.setScore(sp);
			teampns = "";

			//teampns = testpns;
			testpns = "";
			sp--;
		}
		for (Player p : main.yellow) {
			testpns = teampns + p.getName() + ", ";

			if (testpns.length() < 40) {
				teampns = testpns;
				testpns = "";
			} else {
				if (sp > 1) {
					String cut = teampns.substring(0, teampns.length() - 2);
					Score pls = waitObjective.getScore(ChatColor.YELLOW + cut);
					pls.setScore(sp);
					teampns = "";

					teampns = p.getName() + ", ";
					testpns = "";
				}
				sp--;
			}
		}
		if (teampns.length() > 1) {
			String cut = teampns.substring(0, teampns.length() - 2);
			Score pls = waitObjective.getScore(ChatColor.YELLOW + cut);
			pls.setScore(sp);
			teampns = "";

			//teampns = testpns;
			testpns = "";
			sp--;
		}

		if (sp < 1) {
			sp--;
			Score pls = waitObjective.getScore(ChatColor.WHITE + "" + -sp + " More Lines");
			pls.setScore(1);
		}

	}

	public void UpdateGameScoreboard() {

		for (int i = 0; i < 4; i++) {
			
			if (gameObjective[i] != null) {
				gameObjective[i].unregister();
			}
			
			gameObjective[i] = teamBoards[i].registerNewObjective("Game", "");

			gameObjective[i].setDisplaySlot(DisplaySlot.SIDEBAR);
			gameObjective[i].setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Beacon Brawl");

			int line = 15;
			
			Score blank = gameObjective[i].getScore(" ");
			blank.setScore(line);
			line--;

			Score maps = gameObjective[i].getScore(ChatColor.WHITE + "Map: " + ChatColor.LIGHT_PURPLE + main.currentMap);
			maps.setScore(line);
			line--;
			
			if (!timeTitle.equals("")) {
				Score time = gameObjective[i].getScore(ChatColor.WHITE + "Next Event: " + timeTitle);
				time.setScore(line);
				line--;
				time = gameObjective[i].getScore(ChatColor.WHITE + "Time: " + ChatColor.GREEN + dispTime);
				time.setScore(line);
				line--;
			}
			
			blank = gameObjective[i].getScore("  ");
			blank.setScore(line);
			line--;

			Score rstatus = gameObjective[i].getScore(ChatColor.RED + "Red Team:    " + redStatus);
			rstatus.setScore(line);
			line--;

			Score bstatus = gameObjective[i].getScore(ChatColor.BLUE + "Blue Team:   " + blueStatus);
			bstatus.setScore(line);
			line--;

			Score gstatus = gameObjective[i].getScore(ChatColor.GREEN + "Green Team: " + greenStatus);
			gstatus.setScore(line);
			line--;

			Score ystatus = gameObjective[i].getScore(ChatColor.YELLOW + "Yellow Team: " + yellowStatus);
			ystatus.setScore(line);
			line--;

			blank = gameObjective[i].getScore("   ");
			blank.setScore(line);
			line--;
		}
	}

	public void DelayedGameStatus(String team, String status, int sec) {

		int taid = 0;
		if (sec == 0) {
			if (team.equals("red") && redDelayedStatus != 0) {
				Bukkit.getScheduler().cancelTask(redDelayedStatus);
			} else if (team.equals("blue") && blueDelayedStatus != 0) {
				Bukkit.getScheduler().cancelTask(blueDelayedStatus);
			} else if (team.equals("green") && greenDelayedStatus != 0) {
				Bukkit.getScheduler().cancelTask(greenDelayedStatus);
			} else if (team.equals("yellow") && yellowDelayedStatus != 0) {
				Bukkit.getScheduler().cancelTask(yellowDelayedStatus);
			}

			taid = new BukkitRunnable() {
				@Override
				public void run() {
					if (team.equals("red")) {
						redStatus = status;
						redDelayedStatus = 0;
					} else if (team.equals("blue")) {
						blueStatus = status;
						blueDelayedStatus = 0;
					} else if (team.equals("green")) {
						greenStatus = status;
						greenDelayedStatus = 0;
					} else if (team.equals("yellow")) {
						yellowStatus = status;
						yellowDelayedStatus = 0;
					}

					UpdateGameScoreboard();
				}
			}.runTask(main).getTaskId();
		} else {
			taid = new BukkitRunnable() {
				@Override
				public void run() {
					if (team.equals("red")) {
						redStatus = status;
						redDelayedStatus = 0;
					} else if (team.equals("blue")) {
						blueStatus = status;
						blueDelayedStatus = 0;
					} else if (team.equals("green")) {
						greenStatus = status;
						greenDelayedStatus = 0;
					} else if (team.equals("yellow")) {
						yellowStatus = status;
						yellowDelayedStatus = 0;
					}

					UpdateGameScoreboard();
				}
			}.runTaskLater(main, sec * 20).getTaskId();
		}

		if (team.equals("red")) {
			redDelayedStatus = taid;
		} else if (team.equals("blue")) {
			blueDelayedStatus = taid;
		} else if (team.equals("green")) {
			greenDelayedStatus = taid;
		} else if (team.equals("yellow")) {
			yellowDelayedStatus = taid;
		}
	}

	public void SetTeamColors(int tid) {
		Scoreboard tb = teamBoards[tid - 1];
		tb = manager.getNewScoreboard();
		tb.registerNewTeam("1");
		tb.registerNewTeam("2");
		tb.registerNewTeam("3");
		tb.registerNewTeam("4");

		tb.registerNewObjective("health", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		for (int i = 1; i <= 4; i++) {
			Team t = tb.getTeam(i + "");
			t.setPrefix(ChatColor.RED + "");
			if (i == tid) {
				t.setPrefix(ChatColor.GREEN + "");
			}
			t.setSuffix(teamPrefix[i - 1]);
		}
		
		teamBoards[tid - 1] = tb;
	}

	public void SetPlayersTeams(Player player, int tid) {
		for(Scoreboard b : teamBoards) {
			//System.out.println("test1");
			Team rt = b.getTeam(tid + "");
			//System.out.println("test2");
			if (rt == null) {
				//System.out.println("bad team");
			}
			if (player == null) {
				//System.out.println("bad boy");
			}
			rt.addEntry(player.getName());
			//System.out.println("test3");
		}
	}
	
}
