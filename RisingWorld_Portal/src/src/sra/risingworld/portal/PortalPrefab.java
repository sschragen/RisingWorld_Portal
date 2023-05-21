package sra.risingworld.portal;

import net.risingworld.api.utils.Layer;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;
import net.risingworld.api.worldelements.Prefab;
import sra.risingworld.utils.ID3;
import net.risingworld.api.utils.Utils.ChunkUtils;
import net.risingworld.api.utils.Utils.MathUtils;

import java.util.HashMap;
import java.util.Map;

import net.risingworld.api.World;
import net.risingworld.api.objects.Area;
import net.risingworld.api.objects.world.Chunk;
import net.risingworld.api.worldelements.Area3D;

public class PortalPrefab extends Prefab{
	
	private String DebugPrefix = "Plugin : SRA.Portals.Prefab - ";
	private void DebugOut (String s)
	{
		System.out.println(DebugPrefix + s);
	}
	
	public enum PortalState
	{
		placing (0),
		placed (1),
		ready (2),
		active(3);
		
		private int value;
		private static Map map = new HashMap<>();
		
	    private PortalState(int value) {
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
	
	public class PortalDefinition
	{
		public int  		name;
		public int  		destination = -1;
		public long 		owner;
		public Vector3f 	position = new Vector3f();
		public Quaternion 	rotation = new Quaternion();	
		public PortalState  state = PortalState.placing;
	}
	
	public PortalDefinition definition = new PortalDefinition();
	
	private PortalAsset resources;
	private Prefab theMagic[] = new Prefab[4];
	
	private Prefab theRunes[] = new Prefab[3];
	//private Prefab theRuneStones[] = new Prefab[3];
	
	private Vector3f[] position = new Vector3f[4];
	private Quaternion [] rotation = new Quaternion[4];
	private Quaternion quat = new Quaternion();
	
 	public void Update ()
 	{
 		setLocalPosition (definition.position);
 		setLocalRotation (definition.rotation);
 		if (definition.state.getValue() >= 2)
 			showRunes();
 	}
	
	public PortalPrefab (PortalAsset resources,Vector3f pos, Quaternion rot)
	{
		super(resources.baseAsset);
		this.resources = resources;
		this.definition.position = pos;
		this.definition.rotation = rot;
		setLayer("",Layer.OBJECT,true);
		setLocalScale(2f,2f,2f);
		
		setLocalPosition(pos);
		setLocalRotation(rot);
		position[0] = new Vector3f (0,0,0);		
		position[1] = new Vector3f ( 2.5f,0f,   0f);
		position[2] = new Vector3f (   0f,0f,-2.5f);
		position[3] = new Vector3f (-2.5f,0f,   0f);
		rotation[0] = new Quaternion(0,0,0,0);
		rotation[1] = quat.fromAngles(0, MathUtils.degreeToRadian(-90), 0);
		rotation[2] = quat.fromAngles(0, 0 , 0);
		rotation[3] = quat.fromAngles(0, MathUtils.degreeToRadian(90), 0);
		
		Vector3f absPos = this.getLocalPosition();
		DebugOut("Base Position : " + absPos.toString());
	}
	
	public boolean isMagicActive ()
	{
		return ( definition.state.getValue() == PortalState.active.value);
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
				definition.state = PortalState.ready;
			}			
		}
	}
	
	public void showMagic ()
	{
		hideMagic();

		theMagic[0] = new Prefab(resources.magicAsset[0]);
		addChild (theMagic[0]);
		for (int i=1;i<4;i++)
		{
			DebugOut ("Magic "+i+ " on.");
			theMagic[i] = new Prefab(resources.magicAsset[1]);
			theMagic[i].setLocalPosition(position[i]);
			addChild (theMagic[i]);
			definition.state = PortalState.active;
		}		
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
