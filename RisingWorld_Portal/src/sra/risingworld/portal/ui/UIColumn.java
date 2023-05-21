package sra.risingworld.portal.ui;

import java.util.ArrayList;
import java.util.List;

import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.Align;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Justify;
import net.risingworld.api.ui.style.Unit;

public class UIColumn extends UIElement
{
	public List<UIElement> children = new ArrayList<>();
	
	public UIColumn (int width, Unit unit )
	{	
		style.flexDirection.set(FlexDirection.Column);
		style.flexGrow.set(1f); 
		style.width.set(width,Unit.Percent);
		style.height.set(100,unit);
		style.justifyContent.set(Justify.SpaceAround);
		style.alignContent.set(Align.Center); 
		style.paddingTop.set(4f);
		style.paddingBottom.set(4f);
		style.paddingLeft.set(4f);
		style.paddingRight.set(4f);			
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
