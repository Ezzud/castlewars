package fr.ezzud.castlewar.commands.players;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.gui.KitsGUI;
import fr.ezzud.castlewar.methods.GUIManager;

public class kitsCMD {
	public kitsCMD(Player player) {
		player.sendMessage("Opening GUI");
		new GUIManager(player).openInventory(player, new KitsGUI(player).getInventory());
	}

}
