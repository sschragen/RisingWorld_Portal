package sra.risingworld.portal;

import net.risingworld.api.assets.AssetBundle;
import net.risingworld.api.assets.PrefabAsset;
import net.risingworld.api.assets.StyleSheetAsset;
import net.risingworld.api.assets.TextureAsset;

public class PortalAsset 
{
	private AssetBundle ibundle;
	public TextureAsset[] portalIcons    = new TextureAsset[5];
	public TextureAsset   HUD_Head;
	public TextureAsset   HUD_Background;
	public TextureAsset[] HUD_Icons = new TextureAsset[14];
	
	private AssetBundle bundle;
	public PrefabAsset[] runeAsset 		= new PrefabAsset[10];
	public PrefabAsset[] magicAsset 	= new PrefabAsset[2];
	//public PrefabAsset[] runeStone		= new PrefabAsset[3];
	public PrefabAsset   baseAsset;
	//public StyleSheetAsset ZielAsset;
	
	public PortalAsset (String path){
		bundle = AssetBundle.loadFromFile(path + "/resources/portal.bundle");
		ibundle= AssetBundle.loadFromFile(path + "/resources/icons.bundle");
		
		//ZielAsset = StyleSheetAsset.loadFromAssetBundle(bundle, "Canvas");
		HUD_Head  = TextureAsset.loadFromAssetBundle(bundle, "/Textures/Überschrift");
		HUD_Background = TextureAsset.loadFromAssetBundle(bundle, "/Textures/Portal Picture");
		
		HUD_Icons[0]  = TextureAsset.loadFromAssetBundle(ibundle, "An");
		HUD_Icons[1]  = TextureAsset.loadFromAssetBundle(ibundle, "Bet");
		HUD_Icons[2]  = TextureAsset.loadFromAssetBundle(ibundle, "Ylem");
		HUD_Icons[3]  = TextureAsset.loadFromAssetBundle(ibundle, "Wis");
		HUD_Icons[4]  = TextureAsset.loadFromAssetBundle(ibundle, "Sanct");
		HUD_Icons[5]  = TextureAsset.loadFromAssetBundle(ibundle, "Rel");
		HUD_Icons[6]  = TextureAsset.loadFromAssetBundle(ibundle, "Des");
		HUD_Icons[7]  = TextureAsset.loadFromAssetBundle(ibundle, "Jux");
		HUD_Icons[8]  = TextureAsset.loadFromAssetBundle(ibundle, "Quas");
		HUD_Icons[9]  = TextureAsset.loadFromAssetBundle(ibundle, "Tym");
		HUD_Icons[10] = TextureAsset.loadFromAssetBundle(ibundle, "Empty");
		HUD_Icons[11] = TextureAsset.loadFromAssetBundle(ibundle, "Löschen");
		HUD_Icons[12] = TextureAsset.loadFromAssetBundle(ibundle, "OK Rune");
		HUD_Icons[13] = TextureAsset.loadFromAssetBundle(ibundle, "Abbruch Rune");
		
		portalIcons[0] = TextureAsset.loadFromAssetBundle(ibundle, "Aufheben");
		portalIcons[1] = TextureAsset.loadFromAssetBundle(ibundle, "DialIn");
		portalIcons[2] = TextureAsset.loadFromAssetBundle(ibundle, "DialOut");
		portalIcons[3] = TextureAsset.loadFromAssetBundle(ibundle, "Runensack_2");
		portalIcons[4] = TextureAsset.loadFromAssetBundle(ibundle, "Icon_Abbruch");
		
		
		
		baseAsset = PrefabAsset.loadFromAssetBundle(bundle, "theBase");
		
		//runeStone[0] = PrefabAsset.loadFromAssetBundle(bundle, "RuneStone.001");
		//runeStone[1] = PrefabAsset.loadFromAssetBundle(bundle, "RuneStone.002");
		//runeStone[2] = PrefabAsset.loadFromAssetBundle(bundle, "RuneStone.003");
		
		runeAsset[0] = PrefabAsset.loadFromAssetBundle(bundle, "0_An");
		runeAsset[1] = PrefabAsset.loadFromAssetBundle(bundle, "1_Bet");
		runeAsset[2] = PrefabAsset.loadFromAssetBundle(bundle, "2_Ylem");
		runeAsset[3] = PrefabAsset.loadFromAssetBundle(bundle, "3_Wis");
		runeAsset[4] = PrefabAsset.loadFromAssetBundle(bundle, "4_Sanct");
		runeAsset[5] = PrefabAsset.loadFromAssetBundle(bundle, "5_Rel");
		runeAsset[6] = PrefabAsset.loadFromAssetBundle(bundle, "6_Des");
		runeAsset[7] = PrefabAsset.loadFromAssetBundle(bundle, "7_Jux");
		runeAsset[8] = PrefabAsset.loadFromAssetBundle(bundle, "8_Quas");
		runeAsset[9] = PrefabAsset.loadFromAssetBundle(bundle, "9_Tym");
		
		magicAsset[0] = PrefabAsset.loadFromAssetBundle(bundle, "Portal Effect");
		magicAsset[1] = PrefabAsset.loadFromAssetBundle(bundle, "RuneStone Effect");
		
		
	}
}
