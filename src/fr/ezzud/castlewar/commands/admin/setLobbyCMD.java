package fr.ezzud.castlewar.commands.admin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import net.md_5.bungee.api.ChatColor;

public class setLobbyCMD {
	static Main plugin = Main.getInstance();
	
	public setLobbyCMD(Player player) {
		if (player.hasPermission("castlewars.setspawn") || player.isOp()) {
			Location loc = player.getLocation();
			String coords = String.valueOf(loc.getBlockX()) + "," + String.valueOf(loc.getBlockY()) + "," +  String.valueOf(loc.getBlockZ()) + "," +  String.valueOf(loc.getYaw() + "," +  String.valueOf(loc.getPitch()));
			plugin.getConfig().set("lobby_spawnpoint", coords);
			plugin.saveConfig();
			plugin.getConfig().set("game_world",player.getWorld().getName());
			plugin.saveConfig();
			plugin.reloadConfig();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCoordinates of lobby  set to &e" + coords));
		}
	}

}
