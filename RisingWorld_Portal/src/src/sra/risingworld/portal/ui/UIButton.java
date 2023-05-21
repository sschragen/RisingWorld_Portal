package sra.risingworld.portal.ui;

import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.StyleColor;
import net.risingworld.api.ui.style.Unit;

public class UIButton extends UITexture {
	
	public UIButton (int width,int height,Unit unit,TextureAsset texture)
	{
		super (width,height,unit,texture);
		setClickable(true);
		
		//style.marginLeft.set(4f);
		//style.marginRight.set(4f);
		//style.alignSelf.set(Align.Auto);
		
	};
	
	public void SetBackground (TextureAsset texture)
	{
		super.setTexture(texture);
		updateStyle();
	};

}
