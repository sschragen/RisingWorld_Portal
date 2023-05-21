package sra.risingworld.portal.ui;

import java.util.List;
import java.util.ArrayList;

import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Justify;
import net.risingworld.api.ui.style.Unit;

public class UIRow extends UIElement
{
	public List<UIElement> children = new ArrayList<>();
	
	public UIRow (int height, Unit unit )
	{	
		style.flexDirection.set(FlexDirection.Row);
		style.flexGrow.set(1f); 
		style.width.set(100,Unit.Percent);
		style.height.set(height,unit);
		style.justifyContent.set(Justify.SpaceAround);
		style.alignContent.set(Align.Center); 
		style.paddingTop.set(4f);
		style.paddingBottom.set(4f);
		style.paddingLeft.set(4f);
		style.paddingRight.set(4f);	
		style.borderLeftWidth.set(2f);
		style.borderRightWidth.set(2f);
	}
	
	public void update ()
	{
		removeAllChilds();
		for (int i=0;i<children.size();i++)
		{
			addChild (children.get(i));
		}	
	}
}
