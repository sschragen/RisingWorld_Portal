package sra.risingworld.portal.ui;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.Unit;
import sra.risingworld.portal.PortalAsset;
import sra.risingworld.utils.ID3;


public class UIPortalName extends UIRow
{
	public  ID3 name;
	private PortalAsset resources;
	private UITexture[] digit = new UITexture[3];
	
	public UIPortalName (int height, Unit unit, ID3 name, PortalAsset resources)
	{
		super(height,unit);		
		this.resources = resources;
		for (int i=0;i<3;i++)
		{
			//digit[i] = new UITexture(20	, 20,Unit.Percent,this.resources.HUD_Icons[10]);
			digit[i] = new UITexture(64,64,Unit.Pixel,this.resources.HUD_Icons[10]);
			digit[i].style.maxHeight.set(64f);
			digit[i].style.maxWidth.set(64f);
			digit[i].style.height.set(100,Unit.Percent);
			digit[i].style.width.set(100,Unit.Percent);
		}
		style.alignItems.set(Align.Center);
		
		setName(name);
		//children.add(digit[0]);
		//children.add(digit[1]);
		///7children.add(digit[2]);
		
		for (int i=0;i<3;i++)
		{
			children.add(digit[i]);
			addChild (digit[i]);
		}
		
	}
	
	public void update()
	{
		for (int i=0;i<3;i++)
		{			
			int k = 10;
			if (this.name.getDigit(i) >= '0' & this.name.getDigit(i) <='9')
				k = this.name.getDigit(i) - '0';
			digit[i].setTexture(this.resources.HUD_Icons[k]);
			digit[i].updateStyle();
		}
		
		//digit[1].style.paddingLeft.set(4f);
		//digit[1].style.paddingRight.set(4f);
		updateStyle();
	}
	

	
	public void setName (ID3 name)
	{
		this.name = name;
		update();
		
	}
	public ID3 getName ()
	{
		return name;
	}
}
