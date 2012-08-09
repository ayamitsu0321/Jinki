package net.minecraft.src.jinki;

import net.minecraft.src.*;
import org.lwjgl.input.Mouse;

public class ItemJinki extends Item implements IJinki
{
	/**
	 *  �e�N�X�`��
	 */
	protected int swordIcon = 0;
	protected int gunIcon = 1;
	
	public ItemJinki(int id/*, int num1, int num2*/)
	{
		super(id);
		//swordIcon = num1;
		//gunIcon = num2;
		maxStackSize = 1;
		setHasSubtypes(true);
		setMaxDamage(100);//�I���N���c�ʁA���ۂɂ�(max - 1)
	}
	
	/**
	 *  �����Ă��Ȃ��Ă������Ȃ��A�{����Render�Ŏg����
	 */
	public boolean isFull3D()
    {
        return true;
    }
	
	/**
	 * ���N���b�N�����Ƃ��Ƀo���b�g�����悤��
	 */
	public void onUpdate(ItemStack is, World world, Entity entity, int par4, boolean par5)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)entity;
			
			//�r�U���Ă�&���N���b�N
			if (player.swingProgressInt == -1 && Mouse.isButtonDown(0))
			{
				if (par5 && !isSword(is) && is.getItemDamage() < this.getMaxDamage())
	    		{
	    			float speed = 0.3F + ((float)getLevel(is) * 0.2F);
	    			EntityOBBullet bullet = new EntityOBBullet(world, player, speed * 2.0F);
	    			
	    			if (!world.isRemote)
		            {
		            	EnumOracleElement element = EnumOracleElement.FRAME;
		            	bullet.setBulletColor(element.getColor());
		            	bullet.element = element.getElement();
		            	is.damageItem(1, player);
		                world.spawnEntityInWorld(bullet);
		            }
	    		}
			}
		}
	}
	
	/**
	 *  �����e���ŃA�N�V�����𔻒�
	 */
	public EnumAction getItemUseAction(ItemStack is)
    {
    	return EnumAction.block;//isSword(is) ? EnumAction.block : EnumAction.bow ;
    }
	
	/**
	 * Action�̎�������
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }
	
	/**
	 *  �G�t�F�N�g
	 */
	/*public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}*/
	
	/**
	 * ���Ȃ�I���N���z��
	 */
	public boolean hitEntity(ItemStack is, EntityLiving hitEntity, EntityLiving attacker)
    {
    	if (isSword(is))
    	{
        	is.damageItem(-1, attacker);
    		return true;
    	}
    	
        return false;
    }
	
	/**
	 * �e�Ȃ�_���[�W0�A���Ȃ�2�A������Ƌ�����ItemStack���擾
	 */
	public int getDamageVsEntity(Entity par1Entity)
    {
    	ItemStack is = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();
    	return is.getItem() instanceof ItemJinki ? isSword(is) ? 2 + this.getLevel(is) : 0 : is.getItem().getDamageVsEntity(par1Entity) ;
    }
	
	/**
	 *  ���A�e�̐؂�ւ�
	 */
    public boolean onItemUseFirst(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int face) 
    {
    	if (Block.blocksList[world.getBlockId(x, y, z)].blockActivated(world, x, y, z, player))
    	{
        	return true;
    	}
    	
    	is.getTagCompound().setBoolean("isSword", !is.getTagCompound().getBoolean("isSword"));
    	
    	return true;
    }
	
	/**
	 *  �A�C�e���̎g�p�Ƃ������h��
	 */
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
    {
    	player.setItemInUse(is, this.getMaxItemUseDuration(is));
    	
    	return is;
    }
	
	/**
	 *  �e�̋����A����Ƃ΂�
	 */
	/*@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
    	if(!isSword(par1ItemStack) && par1ItemStack.getItemDamage() < this.getMaxDamage())
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

            EntityOBBullet var8 = new EntityOBBullet(par2World, par3EntityPlayer, var7 * 2.0F);

            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);

            if (!par2World.isRemote)
            {
            	EnumOracleElement element = EnumOracleElement.THUNDER;
            	var8.setBulletColor(element.getColor());
            	var8.element = element.getElement();
            	par1ItemStack.damageItem(1, par3EntityPlayer);
                par2World.spawnEntityInWorld(var8);
            }
        }
    }*/
	
	/**
	 *  �Ă��Ɓ[�Ɍ����e���̔���
	 */
	private boolean isSword(ItemStack is)
	{
		if (is.getTagCompound() == null)
		{
			is.setTagCompound(new NBTTagCompound());
			is.getTagCompound().setBoolean("isSword", true);
		}
		
		return is.getTagCompound().getBoolean("isSword");
	}
	
	protected int getLevel(ItemStack is)
	{
		if (is.getTagCompound() == null)
		{
			is.setTagCompound(new NBTTagCompound());
			is.getTagCompound().setInteger("level", 0);
		}
		
		return is.getTagCompound().getInteger("level") & 2;
	}

//IJinki����������
	/**
	 *  ItemStack����e�N�X�`���擾
	 */
	@Override
	public int getJinkiIcon(ItemStack is)
	{
		return isSword(is) ? swordIcon + (this.getLevel(is) * 2) : gunIcon + (this.getLevel(is) * 2) ;
	}
	
	/**
	 * ItemStack�����Z����F���擾
	 */
	@Override
	public int getColorFromItemStack(ItemStack is)
	{
		return 0xffffff;
	}
}