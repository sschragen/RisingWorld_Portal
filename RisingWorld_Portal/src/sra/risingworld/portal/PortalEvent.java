package sra.risingworld.portal;

import net.risingworld.api.events.Event;

public class PortalEvent extends Event
{
	public enum PortalMsgType
	{
		PORTAL_BUILD,
	}
	private String message;
	private PortalMsgType type;
	
	public PortalEvent (PortalMsgType type,String message)
	{
		this.message = message;
		this.type = type;
	}
	
	public String getMessage()
	{
		return message;
	}
	public PortalMsgType getType ()
	{
		return type;
	}
}


