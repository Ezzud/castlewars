package fr.ezzud.castlewar.commands.players;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.gui.TeamGUI;
import fr.ezzud.castlewar.methods.managers.GUIManager;

public class teamCMD {
	public teamCMD(Player player) {
		new GUIManager(player).openInventory(player, new TeamGUI(player).getInventory());
	}

}
