package sra.risingworld.portal.ui;

import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Unit;
import sra.risingworld.portal.PortalAsset;
import sra.risingworld.utils.ID3;
import sra.risingworld.utils.UID3;

public class UINumRow extends UIRow
{
	//private ID3 name;
	//private PortalAsset resources;
	//private UITexture[] digit = new UITexture[3];
	
	public UINumRow (int height, Unit unit, UITexture[] key, PortalAsset resources)
	{
		super(height,unit);
		
		//this.resources = resources;
		for (int i=0;i<3;i++)
		{
			//digit[i] = key
			key[i].style.maxHeight.set(64f);
			key[i].style.maxWidth.set(64f);
			//digit[i].style.marginBottom.set(2f);
			//digit[i].style.marginTop.set(2f);
			//digit[i].style.marginLeft.set(2f);
			//digit[i].style.marginRight.set(2f);
			//digit[i].style.paddingTop.set(4f);
			//digit[i].style.paddingBottom.set(4f);
			//digit[i].style.paddingLeft.set(4f);
			//digit[i].style.paddingRight.set(4f);	
		}
		
		//style.marginBottom.set(8f);
		//style.marginTop.set(8f);
		//style.marginLeft.set(8f);
		//style.marginRight.set(8f);
		style.alignItems.set(Align.Center);
		
		
		children.add(key[0]);
		children.add(key[1]);
		children.add(key[2]);
		update();
		//addChilds(digit);
		//style.flexDirection.set(FlexDirection.Row);
		
		//for (int i=0;i<3;i++)
		//{
		//	digit[i] = new UITexture(100,100,Unit.Percent,this.icons[10]);
		//	addChild (digit[i]);
		//}
		
	}

}
