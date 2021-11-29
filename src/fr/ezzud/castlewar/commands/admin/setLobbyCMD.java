package fr.ezzud.castlewar.commands.admin;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class setLobbyCMD {
	static Main plugin = Main.getInstance();
	YamlConfiguration messages = Main.messages;
	public setLobbyCMD(Player player) {
		if (player.hasPermission("castlewars.admin.setlobby") || player.isOp()) {
			Location loc = player.getLocation();
			String coords = String.valueOf(loc.getBlockX()) + "," + String.valueOf(loc.getBlockY()) + "," +  String.valueOf(loc.getBlockZ()) + "," +  String.valueOf(loc.getYaw() + "," +  String.valueOf(loc.getPitch()));
			plugin.getConfig().set("lobby_spawnpoint", coords);
			plugin.saveConfig();
			if(!plugin.getConfig().getString("game_world").equalsIgnoreCase(player.getWorld().getName())) {
				plugin.getConfig().set("game_world",player.getWorld().getName());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.setlobby").getString("world_changed")).replaceAll("%world%", player.getWorld().getName())));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.setlobby").getString("world_unchanged"))));
			}
			plugin.saveConfig();
			plugin.reloadConfig();
			
		}
	}

}
