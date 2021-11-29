package fr.ezzud.castlewar.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.ezzud.castlewar.api.CastleKit;

public class CWkitChangeEvent extends Event {
	private CastleKit newKit;
	private CastleKit oldKit;

	public CWkitChangeEvent(CastleKit oldKit, CastleKit newKit) {
		this.oldKit = oldKit;
		this.newKit = newKit;
	}
	public CastleKit getOldKit() {
		return this.oldKit;
	}
	
	public CastleKit getNewKit() {
		return this.newKit;
	}
	
	private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
