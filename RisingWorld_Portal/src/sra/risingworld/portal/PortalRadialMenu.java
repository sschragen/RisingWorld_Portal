package sra.risingworld.portal;

import net.risingworld.api.Plugin;
import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.callbacks.Callback;
import net.risingworld.api.objects.Player;
import sra.risingworld.portal.events.*;

public class PortalRadialMenu implements Callback<Integer>
{
	private Player player;
	public boolean isOn = false;
	
	private String[] descr = new String[4];
	private String[] over = new String[4];
	private TextureAsset[] icons = new TextureAsset[4];
	
	private PortalPrefab interactPortal;
	private PortalMap portalMap;
	private Plugin plugin;
	
	public PortalRadialMenu (Plugin plugin, Player player, PortalMap portalMap, PortalAsset theResources)
	{
		this.player    = player;
		this.plugin    = plugin;	
		this.portalMap = portalMap; 
		interactPortal = null;
		
		descr[0] = "Einstellungen";
		descr[1] = "Portal öffnen";
		descr[2] = "Portal Entfernen";
		descr[3] = "Abbruch";
		
		over[0] = "Einst";
		over[1] = "Portal öffnen";
		over[2] = "Portal Entfernen";
		over[3] = "Abbruch";
		
		icons[0] = theResources.portalIcons[3];
		icons[1] = theResources.portalIcons[2];
		icons[2] = theResources.portalIcons[0];
		icons[3] = theResources.portalIcons[4];
	}
	
	public void showRadialMenu (PortalPrefab interactPortal)
	{
		player.showRadialMenu(icons, descr, null, true, this );
		isOn = true;		
		this.interactPortal = interactPortal;
	};
	
	public void onCall (Integer choosen)
	{
		isOn = false;
		switch (choosen)
		{
			case 0: 
				// Einstellung
				PortalShowUIEvent newShowUIEvent = new PortalShowUIEvent(player,interactPortal);
				plugin.triggerEvent(newShowUIEvent);
				break;
				
			case 1: 
				//dial out
				PortalPrefab dstPortal = portalMap.GetByName(interactPortal.definition.dest);
				PortalTeleportEvent newTeleportEvent = new PortalTeleportEvent(interactPortal,dstPortal, PortalTeleportEvent.Type.OPEN);
				plugin.triggerEvent(newTeleportEvent);
				break;
				
			case 2: 
				// entfernen
				PortalMapEvent newPortalMapEvent = new PortalMapEvent(PortalMapEvent.Type.DELETE,interactPortal);
				plugin.triggerEvent (newPortalMapEvent);
				
				break;
				
			case 3: 
				// abbruch
				break;
		}
		interactPortal = null;
	}	
	
}