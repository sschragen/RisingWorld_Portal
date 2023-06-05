package sra.risingworld.portal;


import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.HashMap;

import net.risingworld.api.Server;
import net.risingworld.api.World;
import net.risingworld.api.database.Database;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import sra.risingworld.portal.PortalPrefab.PortalState;

public class PortalDatabase 
{
	private Database database = null;
	static String databaseName = "SRAPortals.db";
	
	private PortalPlugin plugin;
	
	private String DebugPrefix = "Plugin : SRA.Portals - Database Class - ";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	public PortalDatabase (PortalPlugin plugin)
	{
		this.plugin = plugin;

		if (!OpenDatabase())
			CreateDatabaseTables();
	}
	
	public boolean WriteToDatabase (PortalPrefab onePortal)
	{
		try
		{
			String SQL = "INSERT INTO `portals` ( "
					  
					+ "`name`, "
					+ "`destination`, " 
					+ "`owner`, "
					+ "`areaid` "
					+ "`state`, "
					+ "`position`, "
					+ "`rotation`) "
					+ "VALUES (" 
					+ onePortal.definition.name                + ", " 
					+ onePortal.definition.dest                + ", " 
					+ onePortal.definition.owner               + ", " 
					+ onePortal.definition.areaID			   + ", "
					+ onePortal.definition.state.getValue()    + ", "
					+ "'" + onePortal.definition.position.toString() + "', " 
					+ "'" + onePortal.definition.rotation.toString() + "' ); "
					;
			database.executeUpdate(SQL);
		}
		catch(Exception err) 
		{
            DebugOut("An error has occurred while writing one portal to database " + onePortal.definition.name);
            DebugOut(err.getLocalizedMessage());
            return false;
		}
		finally
		{
			DebugOut ("One Portal written to database : " + onePortal.definition.name);
		}
		return true;
	}

	public void WriteToDatabase (HashMap<Integer, PortalPrefab> thePortals)
	{
		thePortals.forEach( (key,value) -> 
		{		
			WriteToDatabase (value);	            
		});
	}
	
	public PortalPrefab LoadFromDatabase (int name, PortalAsset theResources)
	{
		PortalPrefab newPortal = null;
		try
		{
			ResultSet result = database.executeQuery("SELECT * FROM `portals` WHERE `name` = "+name+";");
			
			PortalDefinition newPortalDef = new PortalDefinition();
				newPortalDef.name 			= result.getInt("name");
				newPortalDef.dest		 	= result.getInt("destination");
				newPortalDef.owner       	= result.getLong("owner");   
				newPortalDef.areaID			= result.getLong("areaid");
				newPortalDef.position 		= new Vector3f().fromString(result.getString("position"));
				newPortalDef.rotation 		= new Quaternion().fromString(result.getString("rotation"));
				newPortalDef.state 			= PortalState.valueOf(result.getInt("state"));
				
			newPortal = new PortalPrefab(
    				theResources,
    				newPortalDef    				
    		);    		
    		newPortal.setLayer(Layer.OBJECT);
		}
		catch(SQLException e)
		{
            //Just print the whole stack trace, it's easier for debugging...
        	DebugOut ("Could not load Portal "+name+" from database.");
		}
		finally
		{
			DebugOut ("Portal "+name+" succsessfully loaded from database.");
		}
		return newPortal;
	}
 
	public HashMap<Integer, PortalPrefab> LoadFromDatabase (PortalAsset theResources)
	{
		HashMap<Integer, PortalPrefab> thePortals = new HashMap<Integer, PortalPrefab>();
		int anzahl = 0;
		try
		{
			ResultSet result = database.executeQuery("SELECT * FROM `portals`;");
			{				
            	//Iterate through all rows
            	while(result.next())
            	{
            		anzahl++;      
            		PortalDefinition newPortalDef = new PortalDefinition();
	            		newPortalDef.name     = result.getInt("name");
	            		newPortalDef.dest     = result.getInt("destination");
	            		newPortalDef.owner    = result.getLong("owner");
	            		newPortalDef.areaID   = result.getLong("areaid");
	            		newPortalDef.position = new Vector3f().fromString(result.getString("position"));
	            		newPortalDef.rotation = new Quaternion().fromString(result.getString("rotation"));
	            		newPortalDef.state    = PortalState.valueOf(result.getInt("state"));
            		
            		PortalPrefab newPortal = new PortalPrefab(
        				theResources,
        				newPortalDef
            		);
            		newPortal.setLayer(Layer.OBJECT);
            		
            		//player.addGameObject(newPortal); 
					thePortals.put(newPortal.getID(),newPortal);
					newPortal.Update();
					DebugOut ("portal "+ newPortal.definition.name +" created");
            	}
            	DebugOut(anzahl + " Portals loaded fron Database.");
			}
		}
        //If an exception occurs (or more precisely, we only check if an SQLException occurs), we catch it here
        catch(SQLException e)
		{
            //Just print the whole stack trace, it's easier for debugging...
        	DebugOut ("Tried to load all Portals but failed !");
            DebugOut (e.getLocalizedMessage());
            //e.printStackTrace();
        }	
		finally
		{
			DebugOut ("All Portals read from database. Portals loaded : " + anzahl);
			DebugOut ("Now there are "+ thePortals.size()+" in the HashMap." );
			
		}
		return thePortals;
	}

	public boolean DeleteFromDatabase (PortalPrefab onePortal) 
	{
		try
		{
			database.executeUpdate("DELETE FROM `portals` WHERE name = "+onePortal.definition.name+";");
		}
		catch(Exception err) 
		{
            DebugOut("An error has occurred. deleting one Record " + onePortal.definition.name);
            return false;
		}
		finally
		{
			DebugOut ("SQL OK : ");
            DebugOut (onePortal.definition.name + "deleted.");
		};
		return true;
	}
	
	private boolean OpenDatabase ()
	{
		boolean ret = true;
		try
		{
			database = plugin.getSQLiteConnection(World.getWorldFolder() + "/" + databaseName);
		}
		catch (Exception e) 
		{
		   DebugOut("Something went wrong (open Database).");
		   ret = false;
		}
		finally
		{
			DebugOut("Database is open.");
			DebugOut("Database Path : " + World.getWorldFolder());
			DebugOut("Database Name : " + databaseName);
			
		}
		return ret;
	}
	public void CloseDatabase ()
	{
		database.close();
	}
	private boolean CreateDatabaseTables ()
	{
 		boolean ret = true;
		try
		{
			database.execute("CREATE TABLE IF NOT EXISTS `portals` ("
					 
				+ "		    `name`        INTEGER PRIMARY KEY, "
				+ " 		`destination` INTEGER, "
				+ "			`owner`       VARCHAR(255), "
				+ "         `areaid`      VARCHAR(255), "
				+ "         `state`       INTEGER, "
				+ "			`position`    VARCHAR(255), "
		        + "         `rotation`    VARCHAR(255) " 
				+ ");"
			);
		}
		catch(Exception err)
		{
            //Just print the whole stack trace, it's easier for debugging...
        	DebugOut ("SQL Error while creating the database");
            ret = false;
        }	
		finally
		{
			DebugOut ("Database succesfully created.");           
		}
		return ret;
	}
	public boolean ClearDatabase ()
 	{
 		try
 		{
 			database.execute("DELETE FROM `portals`;");
 		}
 		catch (Exception err)
 		{
 			DebugOut ("Could not clear the database");
 			return false;
 		}
 		finally
 		{
 			DebugOut ("Database cleared.");
 		}
 		return true;
 	};
}
