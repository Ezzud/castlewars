package fr.ezzud.castlewar.methods;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.scoreboard.ScoreboardProvider;
import fr.ezzud.castlewar.methods.scoreboard.ScoreboardText;
import net.md_5.bungee.api.ChatColor;


public class SBDisplay extends ScoreboardProvider {
	YamlConfiguration messages = Main.messages;
	@Override
	public String getTitle(Player p) {
		if(GameStateManager.GameState == true) {
			if(inATeam.checkTeam(p.getName()) == true) {
				if(inATeam.isKing(p.getName()) == false) {
					String msg = messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.king").getString("title"), p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), 0, 0, 0);
					return ChatColor.translateAlternateColorCodes('&', msg);
				} else {
					String msg = messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.soldier").getString("title"), p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), 0, 0, 0);
					return ChatColor.translateAlternateColorCodes('&', msg);
				}				
			} else {
				String msg = messagesFormatter.formatScoreboardSpec(messages.getConfigurationSection("scoreboard.spectator").getString("title"), p, 0,0,0);
				return ChatColor.translateAlternateColorCodes('&', msg);	
			}

		} else {
			String msg = messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.lobby").getString("title"), p, Bukkit.getOnlinePlayers().size(), null, 0, 0, 0);
			return ChatColor.translateAlternateColorCodes('&', msg);
		}
	}
	
	@Override
	public List<ScoreboardText> getLines(Player p) {
		if(GameStateManager.GameState == true) {
			List<ScoreboardText> lines = new ArrayList<>();
			if(inATeam.checkTeam(p.getName()) == true) {
				if(inATeam.isKing(p.getName()) == true) {
					
					long date = new Date().getTime() - GameStateManager.startDate.getTime();
					int seconds = (int) (date / 1000) % 60 ;
					int minutes = (int) ((date / (1000*60)) % 60);
					int hours   = (int) ((date / (1000*60*60)) % 24);
					List<String> lst = messages.getConfigurationSection("scoreboard.king").getStringList("lines");
					for(String g : lst) {
						lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(g, p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), hours, minutes, seconds))));
					}
					if (lines.size() >= 4 && lines.size() < 14) {
						lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.king").getString("footer"), p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), hours, minutes, seconds))));
					}
				} else {
					long date = new Date().getTime() - GameStateManager.startDate.getTime();
					int seconds = (int) (date / 1000) % 60 ;
					int minutes = (int) ((date / (1000*60)) % 60);
					int hours   = (int) ((date / (1000*60*60)) % 24);
					List<String> lst = messages.getConfigurationSection("scoreboard.soldier").getStringList("lines");
					for(String g : lst) {
						lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(g, p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), hours, minutes, seconds))));
					}
					if (lines.size() >= 4 && lines.size() < 14) {
						lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.soldier").getString("footer"), p, Bukkit.getOnlinePlayers().size(), new CastleTeam(inATeam.whichTeam(p.getName())), hours, minutes, seconds))));
					}
				}
				return lines;				
			} else {
				long date = new Date().getTime() - GameStateManager.startDate.getTime();
				int seconds = (int) (date / 1000) % 60 ;
				int minutes = (int) ((date / (1000*60)) % 60);
				int hours   = (int) ((date / (1000*60*60)) % 24);
				List<String> lst = messages.getConfigurationSection("scoreboard.spectator").getStringList("lines");
				for(String g : lst) {
					lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboardSpec(g, p, hours, minutes, seconds))));
				}
				if (lines.size() >= 4 && lines.size() < 14) {
					lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboardSpec(messages.getConfigurationSection("scoreboard.spectator").getString("footer"), p, hours, minutes, seconds))));
				}
				return lines;
			}
			
		} else {
			List<ScoreboardText> lines = new ArrayList<>();
			List<String> lst = messages.getConfigurationSection("scoreboard.lobby").getStringList("lines");
			for(String g : lst) {
				lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(g, p, Bukkit.getOnlinePlayers().size(), null, 0,0,0))));
			}
			if (lines.size() >= 4 && lines.size() < 14) {
				lines.add(new ScoreboardText(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatScoreboard(messages.getConfigurationSection("scoreboard.lobby").getString("footer"), p, Bukkit.getOnlinePlayers().size(), null, 0,0,0))));
			}
			return lines;
		}

	}
	
	
}