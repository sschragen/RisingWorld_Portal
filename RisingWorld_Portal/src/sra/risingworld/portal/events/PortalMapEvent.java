package sra.risingworld.portal.events;

import net.risingworld.api.events.Event;
import sra.risingworld.portal.PortalPrefab;

public class PortalMapEvent  extends Event
{
	public enum Type
	{
		LOAD,
		SAVE,
		RESET,
		SPAWN,
		DELETE,
		UPDATE;
	};
	
	private PortalPrefab portal;
	private Type message;
	
	public PortalMapEvent (Type message)
	{
		this.portal = null;
		this.message = message;
	}
	
	public PortalMapEvent (Type message, PortalPrefab portal)
	{
		this.portal = portal;
		this.message = message;
	}
	
	public PortalPrefab getPortal()
	{
		return portal;
	}
	
	public Type getMessage ()
	{
		return message;
	}
}; //EOF



