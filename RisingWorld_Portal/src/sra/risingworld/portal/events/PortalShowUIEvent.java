package sra.risingworld.portal.events;

import net.risingworld.api.events.Event;
import net.risingworld.api.objects.Player;
import sra.risingworld.portal.PortalPrefab;

public class PortalShowUIEvent extends Event
{
	private PortalPrefab portal;
	private Player player;
	
	public PortalShowUIEvent (Player player, PortalPrefab portal)
	{
		this.portal = portal;
		this.player = player;
	}
	
	public Player GetPlayer()
	{
		return player;
	}
	
	public PortalPrefab getPortal()
	{
		return portal;
	}
}

