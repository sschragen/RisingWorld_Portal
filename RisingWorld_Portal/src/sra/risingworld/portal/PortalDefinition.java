package sra.risingworld.portal;

import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import sra.risingworld.portal.PortalPrefab.PortalState;

public class PortalDefinition
{
	public int  		name;
	public int  		dest = -1;
	public long 		owner;
	public Long			areaID;
	public Vector3f 	position = new Vector3f();
	public Quaternion 	rotation = new Quaternion();	
	public PortalState  state = PortalState.placing;
}//EOF