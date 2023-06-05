package sra.risingworld.portal;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;

import net.risingworld.api.Server;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.objects.Player;
import sra.risingworld.portal.events.PortalMapEvent;
import sra.risingworld.portal.events.PortalTeleportEvent;
import sra.risingworld.utils.UID3;


public class PortalMap implements Listener
{
	private String DebugPrefix = "Plugin : SRA.Portals - Class : PortalMap::";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	
	
	public HashMap<Integer, PortalPrefab>  theMap;
	
	public  UID3 portalNames = new UID3();
	private PortalDatabase database;
	private PortalPlugin plugin;
	private PortalAsset resources;
	//private List<TeleportTimer> TimerList = new ArrayList<TeleportTimer>(0);
	
	@EventMethod public void onPortalMapEvent (PortalMapEvent evt)
	{
		PortalMapEvent.Type message = evt.getMessage();
		PortalPrefab portal = evt.getPortal();
		Player[] players = Server.getAllPlayers();
		switch (message)
		{
			case SPAWN :
				portal.createArea();
				
				int k=0;
				while (k<players.length)
				{
					players[k].addGameObject(portal);
					players[k].addGameObject(portal.area3d);
					k++;
				}
				theMap.put(portal.getID(), portal);
				database.WriteToDatabase(portal);
				Server.addArea(portal.area);
				break;
				
			case DELETE :
				int i = 0;
				while (i<players.length)
				{
					players[i].removeGameObject(portal);
					players[i].removeGameObject(portal.area3d);
					//players[i].showStatusMessage("new Portal with Name " + evt.getPortal().definition.name + "deleted..", 5);
					i++;
				}		
				Server.removeArea(portal.area);
				theMap.remove(portal.getID());
				database.DeleteFromDatabase(portal);
				break;
				
			case UPDATE :
				database.WriteToDatabase(portal);
				break;
				
			case LOAD :
				theMap = database.LoadFromDatabase (this.resources);
				break;
				
			case SAVE :
				database.WriteToDatabase(theMap);
				break;
				
			case RESET :
				
				theMap.forEach((key,value) ->
				{
					int j = 0;
					while (j<players.length)
					{
						players[j].removeGameObject(value);
						players[j].removeGameObject(value.area3d);
						//players[i].showStatusMessage("new Portal with Name " + evt.getPortal().definition.name + "deleted..", 5);
						j++;
					}	
					Server.removeArea(value.area);
				});
				theMap.clear();
				database.ClearDatabase();
				break;
		
			default:
				break;
		
		}
	}
	
	@EventMethod public void onPortalTeleportEvent (PortalTeleportEvent evt)
	{	
		PortalTeleportEvent.Type message = evt.getMessage();
		PortalPrefab act = evt.getActPortal();
		PortalPrefab dst = evt.getDstPortal();
		
		switch (message)
		{
			case OPEN :
				if  (  (act.definition.state == PortalPrefab.PortalState.ready))
				{
					act.timer = new TeleportTimer (act,dst);
					act.timer.start();
					
					//TimerList.add(newTimer);
				}
				break;
				
			case CLOSE :
				
				break;
				
			case RUNE_SHOW :
				
				break;
				
			case RUNE_HIDE :
	
				break;
		
			default:
				break;
		
		}
	}


	public void printAllPortals ()
	{	
		theMap.forEach((key,value) -> 
		{
			DebugOut ("Portal  : " + value.definition.name);
			DebugOut ("-> Dest : " + value.definition.dest);
			
		});
	};
	
	public void printAllIDs (Player player)
	{
		theMap.forEach((key,value) -> {
			player.sendTextMessage("Key in Map : " + key);
		});
	}
	
	public void PrintAllAreas ()
	{
		theMap.forEach((key,value)->
		{
			DebugOut (value.area.toString());
			DebugOut (value.area.getName());
		});
	};
	
	public PortalMap(PortalPlugin plugin,PortalAsset resources)
	{
		this.resources = resources;
		this.plugin = plugin;
		
		database = new PortalDatabase (this.plugin);
		theMap = database.LoadFromDatabase (this.resources);	
		plugin.registerEventListener(this);
	}
	public void Close ()
	{
		plugin.unregisterEventListener(this);
		database.WriteToDatabase(theMap);
		database.CloseDatabase();
	}	
	public int Size()
	{
		return theMap.size();
	}
	public boolean IsValid (int a)
	{
		boolean ret = true;
		
		//if (a == b) ret = false;
		if ( !IsNameInMap(a) ) ret = false; 
		
		
		return ret;
	}
	
	public boolean IsNameInMap (int name)
	{
		DebugOut("Portal Name in Map : "+name);
		for (PortalPrefab value : theMap.values()) 
		{
			DebugOut(">"+value);
			if (value.definition.name == name)
			{	
				DebugOut("found!");
				return true;
			}
		}
		DebugOut("not found");
		return false;
	} 
	
	public boolean ContainsKey (int key)
	{
		return theMap.containsKey(key);
	} 
	
	public PortalPrefab GetByName (int name)
	{
		for (PortalPrefab value : theMap.values()) {
			if (value.definition.name == name)
				return value;
		}
		return null;
	}
	
	public PortalPrefab GetById (int objID)
	{
		return theMap.get(objID);
	}
	
	public Integer GetKeyByName (int name)
	{
		for(Entry<Integer, PortalPrefab> entry: theMap.entrySet()) 
		{
		      if(entry.getValue().definition.name == name) 
		      {
		        return entry.getKey();
		      }
		}
		return null;		
	}
} // EOF
