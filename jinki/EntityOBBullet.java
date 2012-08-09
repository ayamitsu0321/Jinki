package net.minecraft.src.jinki;

import net.minecraft.src.*;

public class EntityOBBullet extends EntityOracleBulletBase
{
	public EntityOBBullet(World world)
	{
		super(world);
	}
	
	public EntityOBBullet(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}
	
	public EntityOBBullet(World world, EntityLiving shooter, EntityLiving target, float speed)
	{
		super(world, shooter, target, speed);
	}
	
	public EntityOBBullet(World world, EntityLiving shooter, float speed)
	{
		super(world, shooter, speed);
	}
	
	protected void onImpact(MovingObjectPosition movingobjectposition)
	{
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
		{
			hitTile(movingobjectposition);
		}
		
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.ENTITY)
		{
			hitEntity(movingobjectposition);
		}
	}
	
	protected void hitTile(MovingObjectPosition movingobjectposition)
	{
		if (worldObj.getBlockMaterial(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Material.water || worldObj.getBlockMaterial(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Material.lava)
		{
			if (element.equals("ice"))
			{
				if (worldObj.getBlockMaterial(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Material.water)
				{
					this.worldObj.setBlockWithNotify(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, Block.ice.blockID);
				}
				
				if (worldObj.getBlockMaterial(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Material.lava)
				{
					this.worldObj.setBlockWithNotify(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, Block.obsidian.blockID);
				}
				
				if (!this.worldObj.isRemote)
				{
					this.setDead();
				}
			}
		}
		else if (worldObj.getBlockMaterial(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) != Material.fire)
		{
			if (element.equals("frame"))
			{
				if (this.worldObj.getBlockId(movingobjectposition.blockX, movingobjectposition.blockY + 1, movingobjectposition.blockZ) == 0 && Block.fire.canPlaceBlockAt(this.worldObj, movingobjectposition.blockX, movingobjectposition.blockY + 1, movingobjectposition.blockZ))
				{
					this.worldObj.setBlockWithNotify(movingobjectposition.blockX, movingobjectposition.blockY + 1, movingobjectposition.blockZ, Block.fire.blockID);
				}
			}
			
			if (element.equals("thunder"))
			{
				//—‹
				EntityLightningBolt lightningbolt = new EntityLightningBolt(this.worldObj, movingobjectposition.blockX + 0.5D, movingobjectposition.blockY, movingobjectposition.blockZ  + 0.5D);
				worldObj.spawnEntityInWorld(lightningbolt);
			}
			
			if (!this.worldObj.isRemote)
			{
				this.setDead();
			}
		}
		
	}
	
	protected void hitEntity(MovingObjectPosition movingobjectposition)
	{
		Entity targetEntity = movingobjectposition.entityHit;
		float speed = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		int damageAmount = (int)Math.ceil((double)speed * this.getDamage());
		DamageSource var22 = null;

        if (this.shootingEntity == null)
        {
        	var22 = new EntityDamageSourceIndirect("arrow", this, this);
        }
        else
        {
        	var22 = new EntityDamageSourceIndirect("arrow", this, this.shootingEntity);
        }
		
		if (element.equals("frame"))
		{
			//‰Š
			targetEntity.setFire(5);
		}
		
		if (element.equals("thunder"))
		{
			//—‹
			EntityLightningBolt lightningbolt = new EntityLightningBolt(this.worldObj, targetEntity.posX, targetEntity.boundingBox.minY, targetEntity.posZ);
			worldObj.spawnEntityInWorld(lightningbolt);
		}
		
		if (targetEntity instanceof EntityLiving && element.equals("ice"))
		{
			//ˆÚ“®‘¬“x’áŒ¸
			((EntityLiving)targetEntity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10 * damageAmount, 0));
		}
		
		if (targetEntity instanceof EntityLiving && element.equals("poison"))
		{
			//“Å
			((EntityLiving)targetEntity).addPotionEffect(new PotionEffect(Potion.poison.id, 10 * damageAmount, 0));
		}
		
		if (targetEntity.attackEntityFrom(var22, damageAmount))
		{
			if (targetEntity instanceof EntityLiving)
            {
                ++((EntityLiving)targetEntity).arrowHitTempCounter;
            	//this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            }
		}
		
		if (!this.worldObj.isRemote)
		{
			this.setDead();
		}
	}
}