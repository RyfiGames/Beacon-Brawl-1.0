/* Copyright Notice
 ********************************************************************************
 * Copyright (C) Ryan Magilton - All Rights Reserved                            *
 * Unauthorized copying of this file, via any medium is strictly prohibited     *
 * without explicit permission                                                  *
 * Written by Ryan Magilton <ramagilton18@hotmail.net>, July 2019               *
 ********************************************************************************/

package me.RyfiMagicman.BeaconBrawl;

import java.util.Dictionary;
import java.util.Hashtable;

public class ShopPrice {
	
	public int count;
	public int type;
	public String st;
	
	public ShopPrice (String data) {
		try {
			String[] split = data.split(" ");
			count = Integer.parseInt(split[0]);
			switch (split[1]) {
			case "Iron":
				type = 1;
				return;
			case "Gold":
				type = 2;
				return;
			case "Diamond":
				type = 3;
				return;
			case "Emerald":
				type = 4;
				return;
			case "Redstone":
				type = 5;
				return;
			case "Heads":
				type = 6;
				return;
			}
		} catch (Exception e) {
			System.out.println("ERROR - Could not parse ShopPrice " + data);
		}
	}
	
	public static Dictionary<String, ShopPrice> price = new Hashtable<String, ShopPrice>();
	
}