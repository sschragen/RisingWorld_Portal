package sra.risingworld.portal.ui;

import net.risingworld.api.assets.TextureAsset;
import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.ScaleMode;
import net.risingworld.api.ui.style.Unit;

public class UITexture extends UIElement
{
	private TextureAsset texture;
	public UITexture (int height,int width,Unit unit, TextureAsset texture )
	{
		this.texture = texture;
		
		style.width.set(width,unit);
		style.height.set(height,unit);
		
		//style.backgroundImageScaleMode.set(ScaleMode.ScaleToFit);
		style.backgroundImage.set(this.texture);	
	}
	
	public void setTexture (TextureAsset texture)
	{
		this.texture = texture;
		style.backgroundImage.set(this.texture);
		
	}
	public void update ()
	{
		
	}
}
