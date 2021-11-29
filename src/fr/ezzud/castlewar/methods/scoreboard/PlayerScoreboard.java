package fr.ezzud.castlewar.methods.scoreboard;

import com.google.common.base.Preconditions;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerScoreboard {
	
	private ScoreboardProvider provider;
	
	private Player player;
	
	private Scoreboard scoreboard;
	private Objective objective;
	
	private boolean active;
	
	private int lastSentCount = -1;
	
	/**
	 * Constructor that initializes the Scoreboard.
	 */
	public PlayerScoreboard(ScoreboardProvider provider, Player player) {
		this.provider = provider;
		
		if (this.provider.getTitle(player) != null) {
			Preconditions.checkState(this.provider.getTitle(player).length() <= 32, "Title can not be more than 32");
		}
		
		this.player = player;
		
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		// Create Scoreboard teams here.
		
		this.objective = this.getOrCreateObjective(this.provider.getTitle(player));
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		this.player.setScoreboard(this.scoreboard);
		
		this.active = true;
		
		this.update();
	}
	
	public Scoreboard getBukkitScoreboard() {
		return this.scoreboard;
	}
	
	/**
	 * Changes the Default Provider.
	 * 
	 * @param provider - The new Provider class you want to use.
	 */
	public void setProvider(ScoreboardProvider provider) {
		this.provider = provider;
		
		if (provider != null) {
			this.update();
		} else {
			this.disappear();
		}
	}
	
	/**
	 * Clears and disables your whole Scoreboard.
	 */
	public void disappear() {
		if (this.active && this.scoreboard != null) {
			this.scoreboard.getTeams().forEach((team) -> team.unregister());
			this.scoreboard.getObjectives().forEach((obj) -> obj.unregister());
		}
	}
	
	/**
	 * Removes specified index.
	 * 
	 * @param index - The index you want to remove.
	 */
	public void remove(int index) {
		if (!this.active) return;
		
		String name = this.getNameForIndex(index);
		
		this.scoreboard.resetScores(name);
		
		Team team = getOrCreateTeam(ChatColor.stripColor(StringUtils.left(this.provider.getTitle(this.player), 14)) + index, index);
		team.unregister();
	}
	
	/**
	 * Updates all current Sidebar lines.
	 */
	public void update() {
		if (!this.active) 
			return;
		
		String title = this.provider.getTitle(this.player);
		List<ScoreboardText> lines = this.provider.getLines(this.player);
		if(Bukkit.getPlayer(this.player.getName()) == null) return;
		if(this.objective == null) return;
		if (this.objective.getDisplaySlot() == DisplaySlot.SIDEBAR) {
			this.objective.setDisplayName(title);
		}
		
		for (int i = 0; i < lines.size(); i++) {
			Team team = this.getOrCreateTeam(ChatColor.stripColor(StringUtils.left(title, 14)) + i, i);
			
			ScoreboardText text = lines.get(lines.size() - i - 1);
			
			team.setPrefix(text.getText());
			team.setSuffix(ChatColor.getLastColors(text.getText()) + text.getExtendedText());
			
			this.objective.getScore(this.getNameForIndex(i)).setScore(i + 1);
		}
		
		if (this.lastSentCount != -1) {
			for (int i = 0; i < this.lastSentCount - lines.size(); i++) {
				this.remove(lines.size() + i);
			}
		}
		
		this.lastSentCount = lines.size();
	}
	
	/**
	 * Gets a Scoreboard Team or creates a new 
	 * Scoreboard Team.
	 * 
	 * @param team - The team you want to get / create.
	 * @param i - The number, prevents duplication of teams.
	 * 
	 * @return The team
	 */
	public Team getOrCreateTeam(String team, int i) {
		Team value = this.scoreboard.getTeam(team);
		
		if (value == null) {
			value = this.scoreboard.registerNewTeam(team);
			value.addEntry(this.getNameForIndex(i));
		}
		
		return value;
	}
	
	/**
	 * Creates and / or gets a Scoreboard Objective.
	 * 
	 * @param objective - The displayName of the Objective.
	 * 
	 * @return The objective
	 */
	public Objective getOrCreateObjective(String objective) {
		Objective value = this.scoreboard.getObjective("dummyBoard");
		
		if (value == null) {
			value = this.scoreboard.registerNewObjective("dummyBoard", "dummy", "dummyBoard");
		}
		
		value.setDisplayName(objective);
		
		return value;
	}
	
	/**
	 * Gets custom name, that is not in use.
	 * It applies a ChatColor to make sure, you
	 * can duplicate Scoreboard Entries!
	 * 
	 * @param index - The index of the name.
	 *
	 * @return Color and index combined.
	 */
	public String getNameForIndex(int index) {
		return ChatColor.values()[index].toString() + ChatColor.RESET;
	}
	
	public ScoreboardProvider getProvider() {
		return this.provider;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
}