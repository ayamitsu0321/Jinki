package net.minecraft.src;

import net.minecraft.src.forge.*;

public class mod_Jinki extends BaseMod
{
	public static Item jinki;
	public static int jinkiID = 12300;//�o�^����Ƃ���-256
	public static int swordIcon = 96;//���̃e�N�X�`��
	public static int gunIcon = 97;//�e�̃e�N�X�`��
	public static String terrain;//�e�N�X�`���̃t�@�C���p�X
	public static IItemRenderer render;//�����_�[
	
	public void load()
	{
		terrain = "/jinki/terrain.png";
		render = new RenderJinki();
		jinki = new ItemJinki(jinkiID - 256, swordIcon, gunIcon).setItemName("Jinki");
		ModLoader.addName(jinki, "Jinki");
		//���d�v�AForge��Item��ID��Render���Ăяo��
		MinecraftForgeClient.registerItemRenderer(jinki.shiftedIndex, render);
		//�e�N�X�`���̃t�@�C���֘A
		MinecraftForgeClient.preloadTexture(terrain);
		jinki.setTextureFile(terrain);
	}
	
	public String getVersion()
	{
		return "pre_1";
	}
}