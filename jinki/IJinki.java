package net.minecraft.src.jinki;

import net.minecraft.src.*;

public interface IJinki
{
	/**
	 * �e�N�X�`����ItemStack����擾�ARenderJinki�Ŏg��
	 */
	public int getJinkiIcon(ItemStack is);
	
	/**
	 * ��Z����F����ItemStack����擾�ARenderJinki�Ŏg��
	 */
	public int getColorFromItemStack(ItemStack is);
}