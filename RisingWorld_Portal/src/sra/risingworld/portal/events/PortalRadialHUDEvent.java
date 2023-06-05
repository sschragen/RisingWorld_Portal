package sra.risingworld.portal.events;

import net.risingworld.api.events.Event;
import net.risingworld.api.objects.Player;
import sra.risingworld.portal.PortalPrefab;

public class PortalRadialHUDEvent extends Event 
{
	public enum Type
	{
		TimerStart,
		TimerAbort,
		TimerStop,	
		Choosen;
		//ShowHUD;
		
		static Type get(Integer i) {
	        return values()[i];
	    }
	}
	
	private PortalPrefab portal;
	private Player player;
	private Type message;
	private int choosen;

	public PortalRadialHUDEvent (Player player, PortalPrefab portal,Type message)
	{
		this.portal = portal;
		this.player = player;
		this.message = message;
	}
	public PortalRadialHUDEvent (Player player, PortalPrefab portal,int choosen)
	{
		this.portal = portal;
		this.player = player;
		this.message = Type.Choosen;
		this.choosen = choosen;
	}
	
	public PortalPrefab GetPortal()
	{
		return portal;
	}
	
	public Player GetPlayer()
	{
		return player;
	}
	
	public int GetChoosen()
	{
		return choosen;
	}
	
	public Type GetMessage ()
	{
		return message;
	}
}
