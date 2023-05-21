package sra.risingworld.portal.ui;

import java.util.ArrayList;
import java.util.List;

import net.risingworld.api.ui.UIElement;
import net.risingworld.api.ui.style.FlexDirection;
import net.risingworld.api.ui.style.Unit;
import sra.risingworld.portal.ui.UIWindowTitle;

public class UIWindow extends UIElement
{
	public UIWindowTitle titleBar;
	public UIElement canvas;
	public List<UIElement> children = new ArrayList<>();
	
	public UIWindow (int width, int height, Unit unit, String title)
	{
		style.height.set(height,unit);
		style.width.set(width,unit);
		style.flexDirection.set(FlexDirection.Column);
		style.flexGrow.set(1f); 
		style.paddingTop.set(20f);
		style.paddingBottom.set(20f);
		style.paddingLeft.set(20f);
		style.paddingRight.set(20f);
		style.backgroundColor.set(0,0,0,.7f);
		
		titleBar = new UIWindowTitle(20,Unit.Pixel,title);	
		canvas = new UIElement();
		
		children.add (titleBar);
		children.add (canvas);
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
