package sra.risingworld.portal;

import java.util.HashMap;
import java.util.Map;

import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.worldelements.Area3D;
import net.risingworld.api.worldelements.Prefab;
import net.risingworld.api.utils.Utils.ChunkUtils;
import net.risingworld.api.utils.Utils.MathUtils;
import net.risingworld.api.Plugin;
import net.risingworld.api.Server;
import net.risingworld.api.World;
import net.risingworld.api.objects.Area;
import net.risingworld.api.objects.world.Chunk;

public class PortalPrefab extends Prefab 
{
	
	private String DebugPrefix = "Plugin : SRA.Portals.Prefab - ";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	@SuppressWarnings("unchecked")
	public enum PortalState
	{
		placing (0),
		placed (1),
		ready (2),
		activeIn(3),
		activeOut(4);
		
		private int value;
		@SuppressWarnings("rawtypes")
		private static Map map = new HashMap<>();
		
	    private PortalState(int value) 
	    {
	        this.value = value;
	    }
	    static 
	    {
	        for (PortalState pageType : PortalState.values()) {
	            map.put(pageType.value, pageType);
	        }
	    }
	    public static PortalState valueOf(int pageType) {
	        return (PortalState) map.get(pageType);
	    }
	    
	    public int getValue() {
	        return value;
	    }
	}
	
	public enum MagicColor
	{
		Green (0),
		Red (1);
		
		private final int value;
	    private MagicColor(int value) {
	        this.value = value;
	    }
	    public int getValue() {
	        return value;
	    }
	}
	
	
	
	public PortalDefinition definition = new PortalDefinition();
	public TeleportTimer timer = null;
	
	private PortalAsset resources;
	private Prefab theMagic[] = new Prefab[4];
	
	private Prefab theRunes[] = new Prefab[3];
	//private Prefab theRuneStones[] = new Prefab[3];
	
	private Vector3f[] position = new Vector3f[4];
	private Quaternion [] rotation = new Quaternion[4];
	private Quaternion quat = new Quaternion();
	
	public Area area;
	public Area3D area3d;
	
	public PortalPrefab (PortalAsset resources, PortalDefinition newPortalDef)
	{
		super(resources.baseAsset);
		this.resources = resources;
		
		definition = newPortalDef;
		
		setLayer("",Layer.OBJECT,true);
		setLocalScale(2f,2f,2f);
		
		setLocalPosition(definition.position);
		setLocalRotation(definition.rotation);
		
		position[0] = new Vector3f (0,0,0);		
		position[1] = new Vector3f ( 2.5f,0f,   0f);
		position[2] = new Vector3f (   0f,0f,-2.5f);
		position[3] = new Vector3f (-2.5f,0f,   0f);
		rotation[0] = new Quaternion(0,0,0,0);
		rotation[1] = quat.fromAngles(0, MathUtils.degreeToRadian(-90), 0);
		rotation[2] = quat.fromAngles(0, 0 , 0);
		rotation[3] = quat.fromAngles(0, MathUtils.degreeToRadian(90), 0);	
	}
	
 	public void createArea ()
 	{
 		Vector3f absPos = this.getLocalPosition();
		DebugOut("Base Position : " + absPos.toString());
		Vector3f a = absPos.add(new Vector3f(-2,-.2f,-2));
		Vector3f b = absPos.add(new Vector3f(2,8,2));
		area = new Area(a,b);
		area.setNameVisible(true);
		area.setName("Portal : " + this.definition.name);
		definition.areaID = area.getID();
		//area.setAttribute("Portal", (Integer)this.definition.name);
		DebugOut(" Area Created : " + area.toString());
		area3d = new Area3D (area);
		setColliderVisible(true);
		
		Server.addArea(area);
 	};
	
  	public void Update ()
 	{
 		setLocalPosition (definition.position);
 		setLocalRotation (definition.rotation);
 		if (definition.state.getValue() >= 2)
 			showRunes();
 	}
 	
	public boolean isMagicActive ()
	{
		if ( definition.state == PortalState.activeIn)  return true;
		if ( definition.state == PortalState.activeOut) return true;
		return false;
	}
	public void hideMagic ()
	{
		for (int i=0; i<4; i++)
		{
			if (theMagic[i] != null)
			{	
				DebugOut ("Magic "+i+ " off.");
				removeChild(theMagic[i]);
				theMagic[i] = null;
				
			}			
		}
		definition.state = PortalState.ready;
	}
	public void showMagicIn ()
	{
		//hideMagic();
		DebugOut ("Portal : " + definition.name + " on.");
		theMagic[0] = new Prefab(resources.magicAsset[0]);
		addChild (theMagic[0]);
		for (int i=1;i<4;i++)
		{
			//DebugOut ("Magic "+i+ " on.");
			theMagic[i] = new Prefab(resources.magicAsset[1]);
			theMagic[i].setLocalPosition(position[i]);
			addChild (theMagic[i]);
			
		}	
		definition.state = PortalState.activeIn;
	}
	public void showMagicOut ()
	{
		DebugOut ("Portal : " + definition.name + " on (DST).");
		theMagic[0] = new Prefab(resources.magicAsset[0]);
		addChild (theMagic[0]);
		for (int i=1;i<4;i++)
		{
			//DebugOut ("Magic "+i+ " on.");
			theMagic[i] = new Prefab(resources.magicAsset[1]);
			theMagic[i].setLocalPosition(position[i]);
			addChild (theMagic[i]);
			
		}
		definition.state = PortalState.activeOut;
	}

	public void showRunes ()
	{		
		int digit;
		String s = String.format("%03d", definition.name);
		
		hideRunes ();		
		for (int i=0;i<3;i++)
		{			
			digit = (s.charAt(i) - '0');				
			theRunes[i] = new Prefab(resources.runeAsset[digit]);	
			theRunes[i].setLocalPosition(position[i+1]);
			theRunes[i].setLocalRotation(rotation[i+1]);
			addChild (theRunes[i]);
		}
	}
	public void hideRunes ()
	{		
		for (int i=0;i<3;i++)
		{
			if (theRunes[i] != null)
			{
				removeChild(theRunes[i]);
				theRunes[i] = null;
			}
		}	
	}
	
	/**
	* Calculate surface for terrain for x and y coordinates
	*
	* Note: This function ignore object's from ground level like construction elements and so on!. Just terrain level.
	*
	* @param x coordinates X
	* @param z coordinates Z
	* @return Z coordinate - height of terrain surface
	*/
	public float getSurfaceLevel(float x, float z) 
	{
		// calculate chunk position.
		int cx=ChunkUtils.getChunkPositionX(x);
		int cz=ChunkUtils.getChunkPositionZ(z);
		// Calculate block position in chunk
		int bx=ChunkUtils.getBlockPositionX(x, cx);
		int bz=ChunkUtils.getBlockPositionZ(z, cz);
		//log.debug(" Global position (x,z): {} {} | Chunk position (x,z) : {} {} | Block position (x,z): {} {}",x,z,cx,cz,bx,bz);
		// Get chunk
		Chunk chunk= World.getChunk(cx, cz);
		// return height (Y axis) including water
		// First try: i need only a block so i use function getLODSurfaceLevel() but follow line generate exception unhandled and crash server !
		//return chunk.getLODSurfaceLevel(bx, bz, true);
		// second try - read all chunk terrain height 32x32
		float[] terrain = chunk.getLODTerrain();	// read only block what i need
		return terrain[Chunk.getTerrainIndex(bx, bz)];
	}
	
}
