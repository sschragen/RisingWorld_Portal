package sra.risingworld.portal;

import net.risingworld.api.Plugin;
import net.risingworld.api.Timer;
import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerEnterAreaEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.PlayerLeaveAreaEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Key;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.RaycastResult;

/**
 * @author Stephanus Schragen
 *
 */
public class PortalPlugin extends Plugin implements Listener 
{
	
	private String DebugPrefix = "Plugin : SRA.Portals - ";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	public PortalAsset theResources;
	private PortalPrefab InteractPortal = null;
	private MapNameToPortal mapNameToPortals;
	
	
	public Player player;
	private Timer KeyF_Timer;
	
	private PortalHUD portalHUD = null;	
	
	private void DebugOut (PortalPrefab.PortalDefinition value)
	{
		DebugOut ("name        : " + value.name);
		DebugOut ("destination : " + value.destination);
		DebugOut ("owner       : " + value.owner);
		//DebugOut ("postion     : " + value.position.toString());
		//DebugOut ("rotation    : " + value.rotation.toString());
	}
	
	
	
	public void EventHandlerKey_F_Pressed (Player player)
	{
		// prüfen ob wir ein Portal anschauen ?
		int layerMask = Layer.getBitmask(Layer.OBJECT);
		player.raycast(layerMask, (RaycastResult result) -> 
		{
			if (result != null) // Jupp machen wir
			{
				//int instanceID = result.getInstanceID();
				if( mapNameToPortals.ContainsKey((int)result.getObjectGlobalID()))
				//if( mapNameToPortals.ContainsKey(result.getInstanceID()))
				{
					InteractPortal = mapNameToPortals.GetById((int)result.getObjectGlobalID());
					//player.sendTextMessage("Portal ID " + InteractPortal + " hit by raycast.");
					KeyF_Timer = new Timer(1.5f,0f,1,() ->
					{
						KeyF_Timer.pause();
						String[] descr = new String[4];
						descr[0] = "Einstellungen";
						descr[1] = "Portal öffnen";
						descr[2] = "Portal Entfernen";
						descr[3] = "Abbruch";
						
						String[] over = new String[4];
						over[0] = "Einst";
						over[1] = "Portal öffnen";
						over[2] = "Portal Entfernen";
						over[3] = "Abbruch";
						
						TextureAsset[] icons = new TextureAsset[4];
						icons[0] = theResources.portalIcons[3];
						icons[1] = theResources.portalIcons[2];
						icons[2] = theResources.portalIcons[0];
						icons[3] = theResources.portalIcons[4];
							
						//player.showRadialMenu(icons, descr, null, true, CallbackRadialMenu);
						/*
						(Integer Choosen) -> 
						{
							
					    });
					    */
					});
					KeyF_Timer.start();							
				}
			}
			else
			{
				player.showStatusMessage("Not Hit.", 5);
				if (portalHUD != null)
				{
					player.showStatusMessage("HUD aus",5);
					player.setMouseCursorVisible(false);
					unregisterEventListener(portalHUD);
					player.removeUIElement(portalHUD);
					portalHUD = null;
				}						
			}
		});
	}

	
	
	
	public void EventHandlerKey_B_Pressed (Player player)
	{
		player.sendTextMessage("Player creates a new Portal");
		int layerMask = Layer.getBitmask(Layer.OBJECT, Layer.TERRAIN,Layer.CONSTRUCTION,Layer.DEFAULT);
	
		player.raycast(layerMask, (RaycastResult result) -> 
		{
			if (result != null) 
			{
				PortalPrefab newPortal = new PortalPrefab(theResources,result.getCollisionPoint(),new Quaternion());
				newPortal.definition.owner = player.getDbID();
				newPortal.definition.position = result.getCollisionPoint();
				//newPortal.definition.rotation = player.getRotation();
				newPortal.definition.rotation = new Quaternion ();
				newPortal.definition.name = mapNameToPortals.portalNames.Random();	
				newPortal.definition.state = PortalPrefab.PortalState.ready;
				newPortal.Update();				
				mapNameToPortals.CreateNewPortal(player);
				//player.addGameObject(newPortal);
				//mapNameToPortals.Add (newPortal);
				player.sendTextMessage("New Portal getID() = "+newPortal.getID());
				//thePortals.put(newPortal.getID(), newPortal);
				
				player.showStatusMessage("new Portal with Name " + newPortal.definition.name + "created.", 5);
			}
		});
	}
	
	@Override
	public void onLoad() 
	{
		DebugOut("gestartet.");		
		theResources = new PortalAsset(getPath());
		DebugOut("Resourcen wurden geladen.");
		
		mapNameToPortals = new MapNameToPortal(this,theResources);
	}

	@Override
	public void onEnable() 
	{
		registerEventListener(this);
	}
	
	@Override
	public void onDisable() 
	{
		unregisterEventListener(this);
		mapNameToPortals.Close ();		
		DebugOut("beendet.");

	}
	
	@EventMethod
	public void onEnterArea (PlayerEnterAreaEvent evt)
	{
		
	}

	@EventMethod
	public void onLeaveArea (PlayerLeaveAreaEvent evt)
	{
		
	}
	
	@EventMethod	
	public void onPlayerSpawns (PlayerSpawnEvent evt) 
	{
		player = evt.getPlayer();
		
		player.setListenForKeyInput(true);
		player.registerKeys(Key.B, Key.F, Key.R, Key.N);
		mapNameToPortals.AddAllPortals(player);
		
		player.showSuccessMessageBox("<color=red>Willkommen","Portals erfolgreich geladen.");
	}
	
	@EventMethod
	public void onKeyPressed (PlayerKeyEvent evt) 
	{	
		Player player = evt.getPlayer();
		if (evt.isPressed())
		{
			switch (evt.getKey())
			{
				case F :	{	
								EventHandlerKey_F_Pressed (player);
								break;
							}
				case B :    {
								EventHandlerKey_B_Pressed (player);
								break;
							}
				case R :	{	// ONLY FOR TEST AND DEBUG
								mapNameToPortals.RemoveAllPortals(player);
								DebugOut ("Database reset RESET !!!");
								break;
							}
				default:	{
								break;
							}
			}
		}
		else // Button was released
		{
			switch (evt.getKey())
			{
				case F :	{	
								EventHandlerKey_F_Released (player);
								break;
							}
				default:	{
								break;
							}
			}
		}
			
	}	
	
	public void EventHandlerKey_F_Released (Player player)
	{
		if (InteractPortal != null) 
		{
			if (KeyF_Timer.isActive() & !KeyF_Timer.isPaused())
			{
				KeyF_Timer.kill();
				player.sendTextMessage("Portal ID " + InteractPortal + " standard execute.");

				if (InteractPortal.definition.destination == -1)
				{
					if (portalHUD == null)
					{
						portalHUD = new PortalHUD(InteractPortal, mapNameToPortals.portalNames ,theResources);
						portalHUD.showHUD(player, (PortalHUDEvent result) -> {
							if (result.buttonClicked == "OK" )
							{
								InteractPortal.definition.destination = result.newDestination;
								InteractPortal.Update();
							};
							// HUD entfernen
							player.showStatusMessage("HUD aus",5);
							player.setMouseCursorVisible(false);
							unregisterEventListener(portalHUD);
							player.removeUIElement(portalHUD);
							portalHUD = null;
						});
						// Toggle
						// HUD aufbauen
						player.addUIElement(portalHUD);
						player.showStatusMessage("HUD an",5);
						player.setMouseCursorVisible(true);
						registerEventListener(portalHUD);
					}
					else
					{		
						// Toggle
						// HUD enfernen
						player.showStatusMessage("HUD aus",5);
						player.setMouseCursorVisible(false);
						unregisterEventListener(portalHUD);
						player.removeUIElement(portalHUD);
						portalHUD = null;
						//InteractPortal = null;
					}
				}
				else
				{
					if (InteractPortal.isMagicActive())
					{
						InteractPortal.hideMagic();
						player.sendTextMessage("Portal ID " + InteractPortal + " Magic aus.");
					}
					else
					{
						InteractPortal.showMagic();
						player.sendTextMessage("Portal ID " + InteractPortal + " Magic an.");
					}
				}
			}
			else //
			{
				
			}
		}
	}
	

} //EOF
