/**
 * 
 */
package sra.risingworld.portal;

import net.risingworld.api.Plugin;
import net.risingworld.api.callbacks.Callback;
import net.risingworld.api.events.EventMethod;
import net.risingworld.api.events.Listener;
import net.risingworld.api.events.player.ui.PlayerUIElementClickEvent;
import net.risingworld.api.objects.Player;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Justify;
import net.risingworld.api.ui.style.Pivot;
import net.risingworld.api.ui.style.Unit;
import net.risingworld.api.utils.ColorRGBA;
import sra.risingworld.portal.ui.*;
import sra.risingworld.utils.ID3;

/**
 * @author Stephanus Schragen
 *
 */
public class PortalHUD extends UIWindow implements Listener, Callback<Integer>
{
	private String DebugPrefix = "Plugin : SRA.Portals - PortalHUD - ";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	private PortalPrefab portal;
	private PortalAsset resources;
	
	private ID3 newDestination;
	//private UID3 idList;
	private PortalMap portalMap;
	
	private UIPortalName 	ui_portalName;// = new UIPortalName(33,Unit.Percent,-1,resources.HUD_Icons);
	private UIButton 		ui_clear;
	private char num[][] = 
		{
			{  '7','8',  '9'},
			{  '4','5',  '6'},
			{  '1','2',  '3'},
			{'9'+4,'0','9'+3}
		};
	private UIButton [][]	ui_numPad = new UIButton[4][3];
	private UIRow []     ui_num = new UIRow[4];
	
	private int inputDigit = 0;
	
	private Player player ;	
	private Plugin plugin;
	
	@EventMethod
 	public void onClick(PlayerUIElementClickEvent evt)
	{
		Player player = evt.getPlayer();
		UIElement clickedUI = evt.getUIElement();
		if (clickedUI == ui_clear)
		{
			clearDigits();
			
			ui_portalName.setBorderColor(ColorRGBA.Red);
			
			evt.getPlayer().showStatusMessage("clear clicked", 5);
			System.out.println("@onClick : received");
			
		}
		else 
		{
			for (int i=0;i<4;i++)
			{
				for (int j=0;j<3;j++)
				{
					if (clickedUI == ui_numPad[i][j] )
					{
						switch ( num[i][j]-'0')
						{
							case 0,1,2,3,4,5,6,7,8,9 :
								if (inputDigit==0)
								{
									clearDigits();
								}								
								
								ui_portalName.name.setDigit(inputDigit,  num[i][j] ); 
								//ui_portalName.setName(newDestination);
								ui_portalName.update();
								evt.getPlayer().showStatusMessage("num clicked '"+num[i][j]+"'" , 5);
								evt.getPlayer().sendTextMessage("Destination : '"+ ui_portalName.name.toString() +"'");
								
								inputDigit++;
								
								if (inputDigit==3) 
								{
									inputDigit=0;
									
									if ( isDestinationInList() )
									{
										portal.definition.dest = ui_portalName.name.toInt();
										ui_numPad[3][2].setOpacity(1f);
										ui_numPad[3][2].updateStyle();
										ui_portalName.setBorderColor(ColorRGBA.Green);
										ui_portalName.updateStyle();
										player.sendTextMessage("found");
									}
									else 
									{
										ui_portalName.setBorderColor(ColorRGBA.Red);
										ui_portalName.updateStyle();
										ui_numPad[3][2].setOpacity(.4f);
										ui_numPad[3][2].updateStyle();
									};
								}
								else 
								{
									ui_portalName.setBorderColor(ColorRGBA.Red);
									ui_portalName.updateStyle();
									ui_numPad[3][2].setOpacity(.4f);
									ui_numPad[3][2].updateStyle();
								}
								break;
							case 13 :
								//abbruch
								evt.getPlayer().showStatusMessage("cancel clicked", 5);
								onCall(0);
								break;
							case 12 :
								//ok
								evt.getPlayer().showStatusMessage("ok clicked", 5);
								newDestination = ui_portalName.getName();
								if ( newDestination.isValid())
								{
									//portal.definition.destination = name.toInt();
									onCall(1);
								}
								break;
						}
						
				
						
					}
				}
			}
		}
	}
	
	private void clearDigits ()
	{
		ui_portalName.setName(new ID3(""));
		ui_numPad[3][2].setOpacity(.6f);
		ui_numPad[3][2].update();
		
		//newDestination.setDigit(0, 10);
		//digit[0].setTexture(resources.HUD_Icons[10]); //.style.backgroundImage.set();
		//digit[1].setTexture(resources.HUD_Icons[10]); //.style.backgroundImage.set(resources.HUD_Icons[10]);
		//digit[2].setTexture(resources.HUD_Icons[10]); //.style.backgroundImage.set(resources.HUD_Icons[10]);			
		
		//digit[0].updateStyle();
		//digit[1].updateStyle();
		//digit[2].updateStyle();
	}
	
	private boolean isDestinationInList()
	{
		String name = ui_portalName.getName().toString();
		int i;
		try
		{
			i = Integer.parseInt(name);
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
		System.out.println("New Destination : "+i);
		System.out.println("Liste : ");
		//idList.DebugOut();
		
		return ( i != portal.definition.name && portalMap.IsNameInMap(i) );//  idList.isUsed(i));
		
	}

	public PortalHUD (Plugin plugin, Player player, PortalMap portalMap,PortalAsset resources)
	{	
		super(40,40,Unit.Percent,"Portal Destination");
		setPosition(50, 50, true);
		setPivot(Pivot.MiddleCenter);
		
		this.plugin    = plugin;
		this.player    = player;
		this.resources = resources;
		this.portalMap = portalMap;
		
		canvas.style.flexDirection.set(FlexDirection.Row);
		
		ui_portalName = new UIPortalName(20,Unit.Percent,this.resources);
		ui_clear = new UIButton (64,64,Unit.Pixel,resources.HUD_Icons[11]);
		
		ui_clear.style.justifyContent.set(Justify.Center);
		ui_clear.style.alignContent.set(Align.Center); 
		ui_clear.style.alignSelf.set(Align.Center);
		
		DebugOut("Start HUD with Dest:"+newDestination); 
		
		UIColumn col[] = new UIColumn[2];
		col[0] = new UIColumn(50,Unit.Percent);
		col[0].children.add(ui_portalName);
		col[0].children.add(ui_clear);
		
		col[0].update();
		//col[0].addChild(ui_clear);
		
		col[1] = new UIColumn(50,Unit.Percent);
		for (int i=0;i<4;i++)
		{
			ui_num[i] = new UIRow(20,Unit.Percent);
			for (int j=0;j<3;j++)
			{
				ui_numPad[i][j] = new UIButton(64,64,Unit.Pixel,this.resources.HUD_Icons[ num[i][j]-'0' ]);
				ui_numPad[i][j].style.justifyContent.set(Justify.Center);
				ui_numPad[i][j].style.alignContent.set(Align.Center); 
				ui_numPad[i][j].style.alignSelf.set(Align.Center);
				ui_num[i].children.add(ui_numPad[i][j]);
			}
			ui_num[i].update();
			col[1].children.add(ui_num[i]);
		}
		col[1].update();
		canvas.addChild(col[0]);
		canvas.addChild(col[1]); 
		
		//canvas.addChild (HudLeft());
		//canvas.addChild (HudRight());
		
		
	}	

	public void showHUD (PortalPrefab portal)
	{
		this.portal = portal;
		inputDigit = 0;
		
		if (portal.definition.dest == -1)
			newDestination = new ID3("   ");
		else
			newDestination = new ID3 (portal.definition.dest);
		
		ui_portalName.setName(newDestination);
		
		if ( isDestinationInList() )
		{
			portal.definition.dest = ui_portalName.name.toInt();
			ui_numPad[3][2].setOpacity(1f);
			ui_numPad[3][2].updateStyle();
			ui_portalName.setBorderColor(ColorRGBA.Green);
			ui_portalName.updateStyle();
		}
		else
		{
			ui_portalName.setBorderColor(ColorRGBA.Red);
			ui_portalName.updateStyle();
			ui_numPad[3][2].setOpacity(.4f);
			ui_numPad[3][2].updateStyle();
		}
		update();
		
		player.addUIElement(this);	
		player.setMouseCursorVisible(true);
		plugin.registerEventListener(this);
		
	}

	@Override
	public void onCall(Integer clicked) 
	{
		// TODO Auto-generated method stub
		PortalHUDEvent newEvent;
		switch (clicked)
		{
			case 0: // Abbruch
				newEvent = new PortalHUDEvent(player, portal, PortalHUDEvent.Type.CLOSE);
				plugin.triggerEvent(newEvent);
				break;
				
			case 1: // OK
				portal.definition.dest = newDestination.toInt();
				newEvent = new PortalHUDEvent(player, portal, PortalHUDEvent.Type.OK);
				plugin.triggerEvent(newEvent);
				break;
				
			default :
				newEvent = new PortalHUDEvent(player, portal,PortalHUDEvent.Type.CLOSE);
				plugin.triggerEvent(newEvent);
				break;
		}
		plugin.unregisterEventListener(this);
		player.removeUIElement(this);
		player.setMouseCursorVisible(false);	
	}


}
