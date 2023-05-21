package sra.risingworld.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.RaycastResult;
import sra.risingworld.utils.UID3;


public class MapNameToPortal 
{
	private HashMap<Integer, PortalPrefab>  mapIdToPrefab;
	
	public  UID3 portalNames = new UID3();
	private PortalDatabase database;
	private PortalPlugin plugin;
	private PortalAsset resources;
	
	public MapNameToPortal(PortalPlugin plugin,PortalAsset resources )
	{
		this.resources = resources;
		this.plugin = plugin;

		database = new PortalDatabase (this.plugin);
		mapIdToPrefab = database.LoadFromDatabase ();
	}
	
	
	public void Close ()
	{
		database.WriteToDatabase(mapIdToPrefab);
		database.CloseDatabase();
	}
	
	public int Size()
	{
		return mapIdToPrefab.size();
	}
	public void Add (PortalPrefab newPortal)
	{
		mapIdToPrefab.put(newPortal.definition.name, newPortal);
		database.WriteToDatabase(newPortal);
	}
	public void AddAllPortals (Player player)
	{
		mapIdToPrefab.forEach((key,value) -> 
		{		
			player.addGameObject(value);            
		});		
	}
	public void RemoveAllPortals (Player player)
	{
		mapIdToPrefab.forEach((key,value) ->
		{
			player.removeGameObject(value);
		});
		mapIdToPrefab.clear();
		database.ClearDatabase();
	}
	public void RemovePortal (PortalPrefab portalPrefab)
	{
		mapIdToPrefab.remove(portalPrefab.definition.name);
		database.DeleteFromDatabase(portalPrefab);
	}
	
	public void CreateNewPortal (Player player)
	{
		int layerMask = Layer.getBitmask(Layer.OBJECT, Layer.TERRAIN,Layer.CONSTRUCTION,Layer.DEFAULT);
		player.raycast(layerMask, (RaycastResult result) -> 
		{
							
			PortalPrefab newPortal = new PortalPrefab(
					resources,
					result.getCollisionPoint(),
					new Quaternion()
				);
			
			player.addGameObject(newPortal);
			mapIdToPrefab.put(newPortal.getID(), newPortal);
			database.WriteToDatabase(newPortal);
			//triggerEvent (); // new Portal ...
		});
	}
	
	public boolean IsNameInMap (int name)
	{
		for (PortalPrefab value : mapIdToPrefab.values()) {
			if (value.definition.destination == name)
				return true;
		}
		return false;
	} 
	public boolean ContainsKey (int key)
	{
		return mapIdToPrefab.containsKey(key);
	} 
	
	public PortalPrefab GetByName (int name)
	{
		for (PortalPrefab value : mapIdToPrefab.values()) {
			if (value.definition.destination == name)
				return value;
		}
		return null;
	}
	public PortalPrefab GetById (int objID)
	{
		return mapIdToPrefab.get(objID);
	}
	public Integer GetKeyByName (int name)
	{
		for(Entry<Integer, PortalPrefab> entry: mapIdToPrefab.entrySet()) 
		{
		      if(entry.getValue().definition.name == name) 
		      {
		        return entry.getKey();
		      }
		}
		return null;		
	}
	public void Put (PortalPrefab newPortal)
	{
		if (IsNameInMap(newPortal.definition.name))
		{
			
		}
		else
		{
			
		}
	}
	
	

}
