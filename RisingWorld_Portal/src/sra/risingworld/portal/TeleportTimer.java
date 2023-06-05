package sra.risingworld.portal;

import net.risingworld.api.Timer;

public class TeleportTimer extends Timer implements Runnable 
{
	private PortalPrefab act;
	private PortalPrefab dst;
	
	public TeleportTimer (PortalPrefab act, PortalPrefab dst)
	{
		super (30f,30f,1,null);
		
		this.act = act;
		this.dst = dst;
		act.showMagicOut();
		dst.showMagicIn();
		
		super.setTask (this);
		
	}

	@Override public void run() 
	{
		act.hideMagic();
		dst.hideMagic();
	}
}