package fr.ezzud.castlewar.commands.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.methods.messagesFormatter;

public class kingsCMD {
	static Main plugin = Main.getInstance();
	
	public kingsCMD(Player player) {
		ConfigurationSection config = Main.messages.getConfigurationSection("commands.kings");
		List<String> lines = new ArrayList<>();
		lines.add(messagesFormatter.formatTeamMessage(config.getString("format"), new CastleTeam("team1")));
		lines.add(messagesFormatter.formatTeamMessage(config.getString("format"), new CastleTeam("team2")));
		String[] strarray = new String[lines.size()];
		lines.toArray(strarray);
		player.sendMessage(strarray);

	}
}
