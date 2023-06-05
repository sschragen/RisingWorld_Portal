package sra.risingworld.utils;

import net.risingworld.api.Timer;
import net.risingworld.api.objects.Player;
import net.risingworld.api.Plugin;
import sra.risingworld.portal.PortalPrefab;
import sra.risingworld.portal.events.PortalRadialHUDEvent;

public class MenuTimer implements Runnable
{
	private Timer timer;

	private float interval = 1.5f;
	private float delay = 0;
	private int repetitions = 1;
	
	private Plugin plugin;
	private Player player;
	private PortalPrefab interactPortal;
	
	public MenuTimer(Plugin plugin, Player player) 
	{
		this.plugin = plugin;
		this.player = player;
		
		timer = new Timer (interval, delay, repetitions, this);
	}

	public void start (PortalPrefab interactPortal)
	{
		this.interactPortal = interactPortal;
		timer.start();
		PortalRadialHUDEvent newEvent = new PortalRadialHUDEvent(player, interactPortal,PortalRadialHUDEvent.Type.TimerStart);
		plugin.triggerEvent (newEvent);
	}
	
	public void stop ()
	{
		timer.kill();
		timer = new Timer (interval, delay, repetitions, this);
		PortalRadialHUDEvent newEvent = new PortalRadialHUDEvent(player, interactPortal,PortalRadialHUDEvent.Type.TimerStop);
		plugin.triggerEvent (newEvent);
	}
	
	public void abort ()
	{
		timer.kill();
		timer = new Timer (interval, delay, repetitions, this);
		PortalRadialHUDEvent newEvent = new PortalRadialHUDEvent(player, interactPortal,PortalRadialHUDEvent.Type.TimerAbort);
		plugin.triggerEvent (newEvent);
	}
	
	public boolean isActive()
	{
		return timer.isActive();
	}
	
	public boolean isPaused()
	{
		return timer.isPaused();
	}

	@Override
    public void run() 
	{
		stop();	
	}
}
