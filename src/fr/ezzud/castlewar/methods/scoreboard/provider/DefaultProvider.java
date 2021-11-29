package fr.ezzud.castlewar.methods.scoreboard.provider;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.methods.SBDisplay;
import fr.ezzud.castlewar.methods.scoreboard.ScoreboardProvider;
import fr.ezzud.castlewar.methods.scoreboard.ScoreboardText;


public class DefaultProvider extends ScoreboardProvider {
	
	private HashMap<ProviderType, ScoreboardProvider> defaultProvider;
	
	public DefaultProvider() {
		this.defaultProvider = new HashMap<>();
		
		this.defaultProvider.put(ProviderType.EXAMPLE, new SBDisplay());
	}
	
	@Override
	public String getTitle(Player p) {
		return this.defaultProvider.get(ProviderType.EXAMPLE).getTitle(p);
	}
	
	@Override
	public List<ScoreboardText> getLines(Player p) {
		return this.defaultProvider.get(ProviderType.EXAMPLE).getLines(p);
	}
	
	public enum ProviderType {
		EXAMPLE;
	}
	
}