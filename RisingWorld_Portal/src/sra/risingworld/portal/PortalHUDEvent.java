package sra.risingworld.portal;

import net.risingworld.api.events.Event;
import net.risingworld.api.objects.Player;

public class PortalHUDEvent extends Event
{
	public enum Type
	{
		OK,
		CLOSE;
	}
	
	private Player player;
	private PortalPrefab portal;
	private int newDestination;
	private Type message;
	
	public PortalHUDEvent (Player player, PortalPrefab portal,  Type message)
	{
		this.message = message;
		this.player = player;
		this.portal = portal;
	}
	
	public Player GetPlayer ()
	{
		return player;
	}
	
	public Type GetMessage ()
	{
		return message;
	}
	
	public PortalPrefab GetPortal ()
	{
		return portal;
	}
	
	public int GetNewDestination ()
	{
		return newDestination;
	}
}
