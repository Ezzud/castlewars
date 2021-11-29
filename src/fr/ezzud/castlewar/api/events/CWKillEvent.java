package fr.ezzud.castlewar.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.ezzud.castlewar.api.CastlePlayer;

public class CWKillEvent extends Event {
	private CastlePlayer killer;
	private CastlePlayer victim;
	
	public CWKillEvent(CastlePlayer killer, CastlePlayer victim) {
		if(killer == null) {
			this.killer = null;
		} else {
			this.killer = killer;
		}
		
		this.victim = victim;
	}
	
	public CastlePlayer getKiller() {
		return killer;
	}
	
	public CastlePlayer getVictim() {
		return victim;
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