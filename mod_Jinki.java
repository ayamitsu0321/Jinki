package net.minecraft.src;

import net.minecraft.src.forge.*;
import net.minecraft.src.jinki.*;
import java.util.Map;

public class mod_Jinki extends BaseMod
{
	public static Item jinki;
	public static int jinkiID = 12300;//登録するときは-256
	public static int swordIcon = 96;//剣のテクスチャ
	public static int gunIcon = 97;//銃のテクスチャ
	public static String terrain;//テクスチャのファイルパス
	public static IItemRenderer render;//レンダー
	//
	public static Item jinki3D;
	public static int jinki3DID = 12301;//登録するときは-256
	public static String tex;
	public static IItemRenderer render3D;//レンダー
	public static boolean is3D = false;

	@Override
	public void addRenderer(Map<Class<? extends Entity>, Render> renderers)
    {
    	renderers.put(EntityOracleBulletBase.class, new RenderOracleBullet());
    }
	
	public void load()
	{
		terrain = "/jinki/terrain.png";
		render = new RenderJinki();
		jinki = new ItemJinki(jinkiID - 256/*, swordIcon, gunIcon*/).setItemName("Jinki");
		ModLoader.addName(jinki, "Jinki");
		//超重要、ForgeはItemのIDでRenderを呼び出す
		MinecraftForgeClient.registerItemRenderer(jinki.shiftedIndex, render);
		//テクスチャのファイル関連
		MinecraftForgeClient.preloadTexture(terrain);
		jinki.setTextureFile(terrain);
	}
	
	public void modsLoaded()
	{
		ModLoader.isModLoaded("mod_3DItems");
	}
	
	public String getVersion()
	{
		return "pre_1";
	}
}