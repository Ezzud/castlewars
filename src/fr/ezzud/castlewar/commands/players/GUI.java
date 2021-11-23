package fr.ezzud.castlewar.commands.players;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.gui.TeamGUI;
import fr.ezzud.castlewar.methods.GUIManager;

public class GUI {
	public GUI(Player player) {
		player.sendMessage("Opening GUI");
		new GUIManager(player).openInventory(player, new TeamGUI(player).getInventory());
	}

}
