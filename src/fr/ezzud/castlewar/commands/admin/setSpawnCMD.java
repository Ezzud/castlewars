package fr.ezzud.castlewar.commands.admin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import net.md_5.bungee.api.ChatColor;

public class setSpawnCMD {
	static Main plugin = Main.getInstance();
	
	public setSpawnCMD(Player player, String[] args) {
		if (player.hasPermission("castlewars.setspawn") || player.isOp()) {
			if(args.length < 2) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify team: team1, team2, spectators"));
				return;
			}
			if(args.length < 3) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify unit: soldier, king"));
				return;	
			}
			switch(args[1].toLowerCase()) {
				case "team1":
					if(!args[2].equalsIgnoreCase("soldier") && !args[2].equalsIgnoreCase("king")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify unit: soldier, king"));
						return;
					}
					Location loc = player.getLocation();
					String coords = String.valueOf(loc.getBlockX()) + "," + String.valueOf(loc.getBlockY()) + "," +  String.valueOf(loc.getBlockZ()) + "," +  String.valueOf(loc.getYaw() + "," +  String.valueOf(loc.getPitch()));
					plugin.getConfig().getConfigurationSection("team1").set(args[2].toLowerCase() + "_spawnpoint", coords);
					plugin.saveConfig();
					plugin.reloadConfig();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCoordinates of team " + args[1] + " set to &e" + coords));
					break;
				case "team2":
					if(!args[2].equalsIgnoreCase("soldier") && !args[2].equalsIgnoreCase("king")) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify unit: soldier, king"));
						return;
					}
					Location loc1 = player.getLocation();
					String coords1 = String.valueOf(loc1.getBlockX()) + "," + String.valueOf(loc1.getBlockY()) + "," +  String.valueOf(loc1.getBlockZ()) + "," +  String.valueOf(loc1.getYaw() + "," +  String.valueOf(loc1.getPitch()));
					plugin.getConfig().getConfigurationSection("team2").set(args[2].toLowerCase() + "_spawnpoint", coords1);
					plugin.saveConfig();
					plugin.reloadConfig();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCoordinates of team " + args[1] + " set to &e" + coords1));
					break;
				case "spectators":
					Location loc11 = player.getLocation();
					String coords11 = String.valueOf(loc11.getBlockX()) + "," + String.valueOf(loc11.getBlockY()) + "," +  String.valueOf(loc11.getBlockZ()) + "," +  String.valueOf(loc11.getYaw() + "," +  String.valueOf(loc11.getPitch()));
					plugin.getConfig().set("spectator_spawnpoint", coords11);
					plugin.saveConfig();
					plugin.reloadConfig();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCoordinates of team " + args[1] + " set to &e" + coords11));
					break;
				default:
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease specify valid team: team1, team2, spectators"));
					break;
			}
		}
	}

}
