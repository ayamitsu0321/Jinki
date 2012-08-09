package net.minecraft.src.jinki;

import net.minecraft.src.*;

public enum EnumOracleElement
{
	NORMAL("normal", 0xffffff),
	FRAME("frame", 0xff4f2f),
	ICE("ice", 0x2f4fff),
	WIND("wind", 0x2fff4f),
	THUNDER("thunder", 0x8f6f2f),
	POISON("poison", 0x8f2f6f);
	
	private final String element;
	private final int color;
	
	private EnumOracleElement(String str, int i)
	{
		element = str;
		color = i;
	}
	
	public String getElement()
	{
		return element;
	}
	
	public int getColor()
	{
		return color;
	}
}