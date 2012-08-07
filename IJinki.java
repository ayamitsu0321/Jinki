package net.minecraft.src;

public interface IJinki
{
	/**
	 *  テクスチャをItemStackから取得、RenderJinkiで使う
	 */
	public int getJinkiIcon(ItemStack is);
	
	/**
	 *  乗算する色ををItemStackから取得、RenderJinkiで使う
	 */
	public int getColorFromItemStack(ItemStack is);
}