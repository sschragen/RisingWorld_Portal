package sra.risingworld.portal.ui;

import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.UILabel;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Unit;

class UIWindowTitle extends UIElement
{
	public UILabel Headline;
	public UIWindowTitle (int height,Unit unit, String title)
	{
		style.flexDirection.set(FlexDirection.Column);
		style.flexGrow.set(1f); 
		//style.width.set(100,Unit.Percent);
		style.height.set(height,unit);
		
		Headline = new UILabel(title);
		Headline.style.alignSelf.set(Align.FlexStart); 
		Headline.style.fontSize.set(25);
		
		addChild (Headline);
	}
}