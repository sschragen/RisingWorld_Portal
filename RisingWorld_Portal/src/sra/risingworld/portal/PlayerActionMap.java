package sra.risingworld.portal;

import java.util.HashMap;
import net.risingworld.api.objects.Player;

public class PlayerActionMap 
{
	private HashMap<Integer,PlayerAction> playerActionMap = null;
	
	public PlayerActionMap ()
	{
		playerActionMap = new HashMap<Integer,PlayerAction>();
		
	}
	
	PlayerAction Get(Player player)
	{
		return playerActionMap.get(player.getID());
	}
	
	PlayerAction Put(Player player)
	{
		PlayerAction playerAction = new PlayerAction();
		playerActionMap.put(player.getID(),playerAction);
		return playerAction;
	}
	
	void Clear ()
	{
		playerActionMap.forEach((key,value) ->
		{
			value.KeyF_InteractPortal = null;
			
			value.KeyF_Timer = null;
			
			value.portalHUD = null;
		});
	}
	
	void Remove (Player player)
	{
		playerActionMap.remove(player.getID());
	}
	
	

}
