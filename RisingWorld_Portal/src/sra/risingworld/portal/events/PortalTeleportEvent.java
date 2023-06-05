package sra.risingworld.portal.events;

import net.risingworld.api.events.Event;
import sra.risingworld.portal.PortalPrefab;

public class PortalTeleportEvent extends Event
{
	public enum Type
	{
		OPEN,
		CLOSE,
		RUNE_SHOW,
		RUNE_HIDE,
	};
	
	private PortalPrefab actPortal;
	private PortalPrefab dstPortal;
	private Type message;
	
	public PortalTeleportEvent (PortalPrefab actPortal,PortalPrefab dstPortal, Type message)
	{
		this.actPortal = actPortal;
		this.dstPortal = dstPortal;
		this.message = message;
	}
	
	public PortalPrefab getActPortal()
	{
		return actPortal;
	}
	
	public PortalPrefab getDstPortal ()
	{
		return dstPortal;
	}
	
	public Type getMessage ()
	{
		return message;
	}
}; //EOF



