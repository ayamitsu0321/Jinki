package net.minecraft.src;

public class ItemJinki extends Item implements IJinki
{
	/**
	 *  テクスチャ
	 */
	protected int swordIcon;
	protected int gunIcon;
	
	public ItemJinki(int id, int num1, int num2)
	{
		super(id);
		swordIcon = num1;
		gunIcon = num2;
		maxStackSize = 1;
		setHasSubtypes(true);
		setMaxDamage(10000);
	}
	
	/**
	 *  あってもなくてもかわらない
	 */
	public boolean isFull3D()
    {
        return true;
    }
	
	/**
	 *  剣か銃かでアクションを判定
	 */
	public EnumAction getItemUseAction(ItemStack is)
    {
    	return isSword(is) ? EnumAction.block : EnumAction.bow ;
    }
	
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }
	
	/**
	 *  エフェクト
	 */
	/*public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}*/
	
	/**
	 *  剣、銃の切り替え
	 */
    public boolean onItemUseFirst(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int face) 
    {
    	if (Block.blocksList[world.getBlockId(x, y, z)].blockActivated(world, x, y, z, player))
    	{
        	return true;
    	}
    	
    	int j1 = isSword(is) ? 999 : 1000 ;
    	is.setItemDamage(j1);
    	return true;
    }
	
	/**
	 *  アイテムの使用
	 */
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
    {
    	player.setItemInUse(is, this.getMaxItemUseDuration(is));
    	return is;
    }
	
	/**
	 *  銃の挙動、矢をとばす
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
    	if(!isSword(par1ItemStack))
        {
            int var6 = this.getMaxItemUseDuration(par1ItemStack) - par4;
            float var7 = (float)var6 / 20.0F;
            var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

            if ((double)var7 < 0.1D)
            {
                return;
            }

            if (var7 > 1.0F)
            {
                var7 = 1.0F;
            }

            EntityArrow var8 = new EntityArrow(par2World, par3EntityPlayer, var7 * 2.0F);

            if (var7 == 1.0F)
            {
                var8.arrowCritical = true;
            }

            int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

            if (var9 > 0)
            {
                var8.setDamage(var8.getDamage() + (double)var9 * 0.5D + 0.5D);
            }

            int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (var10 > 0)
            {
                var8.setKnockbackStrength(var10);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                var8.setFire(100);
            }

            //par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);

            //if (!var5)
            //{
            //    par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
            //}
            //else
            //{
                var8.doesArrowBelongToPlayer = false;
            //}

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(var8);
            }
        }
    }
	
	/**
	 *  てきとーに剣か銃かの判定
	 */
	private boolean isSword(ItemStack is)
	{
		return (is.getItemDamage()) % 2 == 0;
	}
	
	/**
	 *  IJinki実装した分
	 */
	@Override
	public int getJinkiIcon(ItemStack is)
	{
		return isSword(is) ? swordIcon : gunIcon ;
	}
	
	@Override
	public int getColorFromItemStack(ItemStack is)
	{
		return 0xffffff;
	}
}