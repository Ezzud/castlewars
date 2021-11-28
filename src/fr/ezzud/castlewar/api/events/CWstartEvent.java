package fr.ezzud.castlewar.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.ezzud.castlewar.api.CastlePlayer;
import fr.ezzud.castlewar.api.CastleTeam;

public class CWstartEvent extends Event {
	private CastlePlayer kingTeam1;
	private CastlePlayer kingTeam2;
	private CastleTeam team1;
	private CastleTeam team2;
	
	public CWstartEvent(CastleTeam team1, CastleTeam team2, CastlePlayer kingTeam1, CastlePlayer kingTeam2) {
		this.kingTeam1 = kingTeam1;
		this.kingTeam2 = kingTeam2;
		this.team1 = team1;
		this.team2 = team2;
	}
	
	public CastleTeam getTeam1() {
		return team1;
	}
	
	public CastlePlayer getKingTeam1() {
		return kingTeam1;
	}
	
	public CastleTeam getTeam2() {
		return team2;
	}
	
	public CastlePlayer getKingTeam2() {
		return kingTeam2;
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