package fr.ezzud.castlewar.commands.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import fr.ezzud.castlewar.Main;
import net.md_5.bungee.api.ChatColor;

public class helpCMD {
	static Main plugin = Main.getInstance();
	
	public helpCMD(Player player) {
		ConfigurationSection helpMSG = Main.messages.getConfigurationSection("commands.help");
		List<String> lines = new ArrayList<>();
		lines.add(ChatColor.translateAlternateColorCodes('&', helpMSG.getString("header").replaceAll("%version%", plugin.getDescription().getVersion())));
		for(String l : helpMSG.getStringList("noAdmin")) {
			lines.add(ChatColor.translateAlternateColorCodes('&', l));
		}
		if (player.hasPermission("castlewars.admin") || player.isOp()) {
			for(String l : helpMSG.getStringList("admin")) {
				lines.add(ChatColor.translateAlternateColorCodes('&', l));
			}
		}
		lines.add(ChatColor.translateAlternateColorCodes('&', helpMSG.getString("footer")));
		String[] strarray = new String[lines.size()];
		lines.toArray(strarray);
		player.sendMessage(strarray);

	}
}
