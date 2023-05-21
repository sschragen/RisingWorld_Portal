package sra.risingworld.portal;


import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.HashMap;
import net.risingworld.api.World;
import net.risingworld.api.database.Database;
import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import sra.risingworld.portal.PortalPrefab.PortalState;

public class PortalDatabase 
{
	private Database database;
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

		OpenDatabase();
		CreateDatabaseTable();
	}
	
	public boolean WriteToDatabase (PortalPrefab onePortal)
	{
		try
		{
			String SQL = "INSERT INTO `portals` ( "
					  
					+ "`name`, "
					+ "`destination`, " 
					+ "`owner`, " 
					+ "`state`, "
					+ "`position`, "
					+ "`rotation`) "
					+ "VALUES (" 
					+ onePortal.definition.name                + ", " 
					+ onePortal.definition.destination         + ", " 
					+ onePortal.definition.owner               + ", " 
					+ onePortal.definition.state.getValue()    + ", "
					+ "'" + onePortal.definition.position.toString() + "', " 
					+ "'" + onePortal.definition.rotation.toString() + "' ); "
					;
			database.executeUpdate(SQL);
			/*
			database.executeUpdate("INSERT INTO `portals` ( "
				+ "`name`, "
				+ "`destination`, " 
				+ "`owner`, " 
				+ "`state`, "
				+ "`position`, "
				+ "`rotation`) "
				+ "VALUES (`" 
				+ onePortal.definition.name                + "`, `" 
				+ onePortal.definition.destination         + "`, `" 
				+ onePortal.definition.owner               + "`, `" 
				+ onePortal.definition.state.getValue()    + "`, `"
				+ onePortal.definition.position.toString() + "`, `" 
				+ onePortal.definition.rotation.toString() + "`);"
			);
			*/
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
	
	public PortalPrefab LoadFromDatabase (int name)
	{
		PortalPrefab newPortal = null;
		try
		{
			ResultSet result = database.executeQuery("SELECT * FROM `portals` WHERE `name` = "+name+";");
			
			newPortal = new PortalPrefab(
    				plugin.theResources,
    				new Vector3f().fromString(result.getString("position")),
    				new Quaternion().fromString(result.getString("rotation"))
    		);    		
			newPortal.definition.name 	     	= result.getInt("name");
			newPortal.definition.destination 	= result.getInt("destination");
			newPortal.definition.owner       	= result.getLong("owner");            			    		
    		newPortal.definition.state 			= PortalState.valueOf(result.getInt("state"));//PortalPrefab.PortalState.ready;
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
 
	public HashMap<Integer, PortalPrefab> LoadFromDatabase ()
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
            		PortalPrefab newPortal = new PortalPrefab(
            				plugin.theResources,
            				new Vector3f().fromString(result.getString("position")),
            				new Quaternion().fromString(result.getString("rotation"))
            		);
            		
        			newPortal.definition.name 	     = result.getInt("name");
        			newPortal.definition.destination = result.getInt("destination");
        			newPortal.definition.owner       = result.getLong("owner");
            		newPortal.definition.state = PortalState.valueOf(result.getInt("state"));//PortalPrefab.PortalState.ready;
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
		try
		{
			database = plugin.getSQLiteConnection(World.getWorldFolder() + "/" + databaseName);
		}
		catch (Exception e) 
		{
		   DebugOut("Something went wrong (open Database).");
		   return false;
		}
		finally
		{
			DebugOut("Database is open.");
			DebugOut("Database Path : " + World.getWorldFolder());
			DebugOut("Database Name : " + databaseName);
			
		}
		return true;
	}

	public void CloseDatabase ()
	{
		database.close();
	}
	
 	private boolean CreateDatabaseTable ()
	{
		try
		{
			database.execute("CREATE TABLE IF NOT EXISTS `portals` ("
					 
				+ "		    `name`        INTEGER PRIMARY KEY, "
				+ " 		`destination` INTEGER, "
				+ "			`owner`       VARCHAR(255), "
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
            return false;
        }	
		finally
		{
			DebugOut ("Database succesfully created.");           
		}
		return true;
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
