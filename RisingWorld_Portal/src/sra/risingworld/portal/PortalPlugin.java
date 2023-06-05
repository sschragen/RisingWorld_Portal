package sra.risingworld.portal;

/*
 * ToDo
 * 
 * 
 *                                     		- portal bauen mit drehen
 * - das Teleporten an sich
 * 
 * - löschen eines Portals noch nicht implementiert / Event -> an spieler -> database
 * - Spechiern von Dst Änderung in database
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */

import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.PlayerDisconnectEvent;
import net.risingworld.api.events.player.PlayerEnterAreaEvent;
import net.risingworld.api.events.player.PlayerKeyEvent;
import net.risingworld.api.events.player.PlayerLeaveAreaEvent;
import net.risingworld.api.events.player.PlayerSpawnEvent;
import net.risingworld.api.objects.Area;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Key;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.RaycastResult;
import net.risingworld.api.utils.Vector3f;
import sra.risingworld.portal.PortalPrefab.PortalState;
import sra.risingworld.portal.events.*;
import sra.risingworld.utils.MenuTimer;

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
	private PortalAsset 	theResources;
	private PortalMap 		portalMap;
	//private Player 			player;
	private PlayerActionMap playerActionMap;
	
	public boolean isValidDestination(PortalPrefab interactPortal)
	{
		// Ziel muss ein bestehendes Portal sein
		// und
		// Ziel darf nicht Ursprung sein
		
		return portalMap.IsNameInMap(interactPortal.definition.name) && (interactPortal.definition.dest == interactPortal.definition.name);

	}
	
	@Override public void onLoad() 
	{
		
		
		DebugOut("gestartet.");		
		theResources = new PortalAsset(getPath());
		DebugOut("Resourcen wurden geladen.");
		
		portalMap = new PortalMap(this,theResources);
		registerEventListener(portalMap);
		
		playerActionMap = new PlayerActionMap();
	}
	@Override public void onEnable() 
	{
		registerEventListener(this);
	}
	@Override public void onDisable() 
	{
		unregisterEventListener(this);
		portalMap.Close ();		
		DebugOut("beendet.");

	}
	
	@EventMethod public void onEnterArea (PlayerEnterAreaEvent evt)
	{
		Player player = evt.getPlayer();
		Area a = evt.getArea();
		
		String s = a.getName();
		
		if (s != null)
		{
			player.sendTextMessage("Enter Area : " + s );
			String[] words = s.split(" ");
			if (words.length == 3)
			{ 
				if (words[0].equals("Portal") )
				{
					player.sendTextMessage("Enter Portal Area");
					Integer name = Integer.valueOf(words[3]);
					player.sendTextMessage("Enter Portal " + name);
					PortalPrefab act = portalMap.GetByName(name);
					player.sendTextMessage("Enter Portal " + name);
					if (act.definition.state== PortalPrefab.PortalState.activeOut)
					{
						player.shake(1, 2);
						PortalPrefab dst = portalMap.GetByName(act.definition.dest);
						player.setPosition(dst.getLocalPosition());
					}
					else player.shake(.5f, 0.5f);
				}
			}
		}
		else 
			player.sendTextMessage("Enter Area has no name");
		player.sendTextMessage("Enter Area Event ... Exit" );
	}
	@EventMethod public void onLeaveArea (PlayerLeaveAreaEvent evt)
	{
		Player player = evt.getPlayer();
		Area a = evt.getArea();
		//player.sendTextMessage("Leave Area Event ..." + a.getName());
	}
	@EventMethod public void onPlayerSpawns (PlayerSpawnEvent evt) 
	{
		Player player = evt.getPlayer();		
		PlayerAction playerAction = playerActionMap.Put(player);
		playerAction.KeyF_Timer = new MenuTimer(this, player);
		playerAction.portalHUD  = new PortalHUD (this, player, portalMap ,theResources);
		playerAction.radialHUD  = new PortalRadialMenu(this, player, portalMap, theResources);
		
		player.setListenForKeyInput(true);
		player.registerKeys(Key.B, Key.F, Key.R, Key.H);
		portalMap.theMap.forEach((key,value) -> 
		{
			player.addGameObject(value);
			player.addGameObject(value.area3d);
		});
		
		player.showSuccessMessageBox("<color=red>Willkommen","Portals erfolgreich geladen.");
	}
	@EventMethod public void onPlayerDisconnect (PlayerDisconnectEvent evt)
	{
		Player player = evt.getPlayer();
		playerActionMap.Remove(player);
	}
	
	@EventMethod public void onPortalRadialHUD (PortalRadialHUDEvent evt)
	{
		Player player = evt.GetPlayer();
		PlayerAction playerAction = playerActionMap.Get(player);
		switch (evt.GetMessage())
		{
			case TimerStart : 
				// evt. mal einen Timerbalken zeigen
				//player.sendTextMessage("Timer Start");
				break;
			case TimerStop :
				// evtl den Timerbalken entfernen
				//jetzt das RadialMenu zeigen
				
				playerAction.radialHUD.showRadialMenu(playerAction.KeyF_InteractPortal);
				//player.sendTextMessage("Timer Stop");
				
				break;
			case TimerAbort :
				// evtl den Timerbalken entfernen
				// schauen wir immer noch auf das portal ??
				int layerMask = Layer.getBitmask(Layer.OBJECT);
				player.raycast(layerMask, (RaycastResult result) -> 
				{   // haben wir etwas getroffen ?
					if (result != null) 
					{	// prüfe ob das Object ein Portal ist ?
						if( portalMap.ContainsKey((int)result.getObjectGlobalID()))
						{	// ist ein Portal ?
							player.sendTextMessage("ist ein Portal");
							PortalPrefab actPortal = portalMap.GetById((int)result.getObjectGlobalID());
							if (actPortal == playerAction.KeyF_InteractPortal)
							{	// ist es noch dasselbe wie bei Timer Start ?						
								if (portalMap.IsValid(actPortal.definition.dest))
								{	// Portal zum teleportieren öffnen
									player.sendTextMessage("Sende Evt ÖffnePortal");
									PortalPrefab dstPortal = portalMap.GetByName(actPortal.definition.dest);
									PortalTeleportEvent newPortalTeleportEvent = new PortalTeleportEvent(actPortal,dstPortal, PortalTeleportEvent.Type.OPEN);
									triggerEvent(newPortalTeleportEvent);
								}
								else
								{	// Portal Einstellung öffnen
									player.sendTextMessage("Sende Evt ShowUI");
									PortalShowUIEvent newShowUIEvent = new PortalShowUIEvent(player,actPortal);
									triggerEvent(newShowUIEvent);
								}
							}
						}
					}
				});				
				player.sendTextMessage("Timer Abort");
				break;

			default :
				DebugOut ("onPortalRadialHUD - unhandled Event.");
				break;
		}
	}
	
	@EventMethod public void onKeyPressed (PlayerKeyEvent evt) 
	{		
		Player player = evt.getPlayer();
		PlayerAction playerAction = playerActionMap.Get(player);
		boolean keyPressed = evt.isPressed();
		int layerMask;
		switch (evt.getKey())
		{
			case H :
				// Test
				Area[] a = Server.getAllAreas();
				DebugOut ("Anzahl an Aereas : "+a.length);
				Server
				break;
			case F :				
				// Interact with a Portal
				// do we look at a Portal ?
				layerMask = Layer.getBitmask(Layer.OBJECT);
				player.raycast(layerMask, (RaycastResult result) -> 
				{
					if (result != null) 
					{	// 
						if (keyPressed)
						{	// prüfe ob das Object ein Portal ist
							player.sendTextMessage("Object hit");
							if( portalMap.ContainsKey((int)result.getObjectGlobalID()))
							{	// taste gedrückt
								// yes we do Start the Timer before we show the RadialMenu
								player.sendTextMessage("Portal hit");
								PortalPrefab interactPortal = portalMap.GetById((int)result.getObjectGlobalID());
								
								if ( (player.getDbID() == interactPortal.definition.owner) || player.isAdmin() )
								{
									playerAction.KeyF_InteractPortal = interactPortal;
									playerAction.KeyF_Trigger = true;
									// start a Timer 
									playerAction.KeyF_Timer.start(playerAction.KeyF_InteractPortal);
								}
								else
								{
									player.showStatusMessage("Not allowed.", 5);
								}
							}
						}
						else // taste losgelassen
						{ 
							if (playerAction.KeyF_Timer.isActive()) 
							{
								playerAction.KeyF_Timer.abort();
							}
						}
					}
					
				});
				break;
						
			case B :      
				// später über CustomItem implementieren
				if (evt.isPressed())
				{
					layerMask = Layer.getBitmask(Layer.OBJECT, Layer.TERRAIN,Layer.CONSTRUCTION,Layer.DEFAULT);
					player.raycast(layerMask, (RaycastResult result) -> 
					{
						if (result != null) 
						{
							PortalDefinition newPortalDef = new PortalDefinition();
								newPortalDef.name 			= portalMap.portalNames.Random();
								newPortalDef.dest		 	= -1;
								newPortalDef.owner       	= player.getDbID();   
								newPortalDef.areaID			= null;
								newPortalDef.position 		= result.getCollisionPoint();
								newPortalDef.rotation 		= new Quaternion();
								newPortalDef.state 			= PortalPrefab.PortalState.ready;;
							
							PortalPrefab newPortal = new PortalPrefab(
								theResources,
								newPortalDef								
							);
							newPortal.Update();	
																								
							PortalMapEvent newPortalMapEvent = new PortalMapEvent(PortalMapEvent.Type.SPAWN,newPortal);
							triggerEvent(newPortalMapEvent);	
							
						}
					});
					
				}
				break;
	
			case R :	
				// ONLY FOR TEST AND DEBUG
				if (evt.isPressed())
				{
					Player[] players = Server.getAllPlayers();
					
					portalMap.theMap.forEach((key,value) -> 
					{
						int i=0;
						while (i<players.length)
						{
							players[i].removeGameObject(value);
							players[i].removeGameObject(value.area3d);
							i++;
						}
					});
					
					playerActionMap.Clear ();
					
					PortalMapEvent newPortalMapEvent = new PortalMapEvent(PortalMapEvent.Type.RESET);
					triggerEvent (newPortalMapEvent);
					DebugOut ("Database reset RESET triggered !!!");
				}
				break;
	
			default:	
				break;
						
		}		
	}		

	@EventMethod public void onPortalShowUIEvent (PortalShowUIEvent evt)
	{
		Player player = evt.GetPlayer();
		player.sendTextMessage("PortalShowUIEvent Event ");
		PlayerAction playerAction = playerActionMap.Get(player);
		playerAction.portalHUD.showHUD(evt.getPortal());		
	}

} //EOF
