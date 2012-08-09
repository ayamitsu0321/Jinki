package net.minecraft.src;

import net.minecraft.src.forge.*;
import net.minecraft.src.jinki.*;
import java.util.Map;

public class mod_Jinki extends BaseMod
{
	public static Item jinki;
	public static int jinkiID = 12300;//�o�^����Ƃ���-256
	public static int swordIcon = 96;//���̃e�N�X�`��
	public static int gunIcon = 97;//�e�̃e�N�X�`��
	public static String terrain;//�e�N�X�`���̃t�@�C���p�X
	public static IItemRenderer render;//�����_�[
	//
	public static Item jinki3D;
	public static int jinki3DID = 12301;//�o�^����Ƃ���-256
	public static String tex;
	public static IItemRenderer render3D;//�����_�[
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
		//���d�v�AForge��Item��ID��Render���Ăяo��
		MinecraftForgeClient.registerItemRenderer(jinki.shiftedIndex, render);
		//�e�N�X�`���̃t�@�C���֘A
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