package sra.risingworld.portal;

import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.callbacks.Callback;
import net.risingworld.api.objects.Player;

public class PortalRadialMenu implements Callback<Integer>
{
	private Player player;
	private boolean isOn = false;
	
	private String[] descr = new String[4];
	private String[] over = new String[4];
	private TextureAsset[] icons = new TextureAsset[4];
	
	private PortalPrefab interactPortal;
	private MapNameToPortal nameToPortals;
	
	public PortalRadialMenu (Player player,PortalAsset theResources)
	{
		this.player = player;
		interactPortal = null;
		nameToPortals = null;
		
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
	
	public void showRadialMenu (PortalPrefab interactPortal, MapNameToPortal portalMap)
	{
		player.showRadialMenu(icons, descr, null, true, this );
		isOn = true;
		this.interactPortal = interactPortal;
	};
	
	public void onCall (Integer choosen)
	{
		isOn = false;
		player.sendTextMessage("Choosen " + choosen);
		switch (choosen)
		{
			case 0: 
				// Einstellung
				
				// write to database
				break;
			case 1: 
				//dial out
				if (interactPortal != null)
					interactPortal.showMagic ();
					// and the other Portal
				break;
			case 2: 
				// entfernen
				nameToPortals.RemovePortal(interactPortal);
				//theDatabase.DeleteFromDatabase(interactPortal);
				//thePortals.remove(result.getObjectGlobalID());
				player.removeGameObject(interactPortal);
				
				// write to database
				
			case 3: 
				// abbruch
		}
		interactPortal = null;
	}
	
	
}