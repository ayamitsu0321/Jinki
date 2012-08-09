package net.minecraft.src.jinki;

import net.minecraft.src.*;
import java.util.List;
import java.util.Random;

public abstract class EntityOracleBulletBase extends Entity
{
	protected EntityLiving shootingEntity;//このEntityを撃ったEntityLiving
	private double damage = 2.0D;//ダメージ
	private int ticksInAir = 0;//空中にいる時間、一定こえたらsetDead()
	private int color = 0xffffff;//れんだー色
	public boolean isRainbow = false;//虹色かどうか
	/*public static final String[] elements = new String[]//属性の配列、TODO:専用のクラスつくって管理
	{
		"normal", "frame", "ice", "thunder", "poison"
	};*/
	public String element = "normal";//elements[0];//初期値はnormal
	
	public EntityOracleBulletBase(World world)
	{
		super(world);
	}
	
	public EntityOracleBulletBase(World world, double x, double y, double z)
	{
		super(world);
		this.setSize(0.25F, 0.25F);
		this.setPosition(x, y, z);
		this.yOffset = 0.0F;
	}
	
	public EntityOracleBulletBase(World world, EntityLiving shooter, EntityLiving target, float speed)
	{
		super(world);
		this.shootingEntity = shooter;
		this.posY = shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D;
		double varX = target.posX - shooter.posX;
		double varY = target.posY + (double)target.getEyeHeight() - 0.699999988079071D - this.posY;
		double varZ = target.posZ - shooter.posZ;
		double var1 = (double)MathHelper.sqrt_double(varX * varX + varZ * varZ);
		
		if (var1 >= 1.0E-7D)
		{
			float var2 = (float)(Math.atan2(varZ, varX) * 180.0D / Math.PI) - 90.0F;
            float var3 = (float)(-(Math.atan2(varY, var1) * 180.0D / Math.PI));
            double var4 = varX / var1;
            double var5 = varZ / var1;
            this.setLocationAndAngles(shooter.posX + var4, this.posY, shooter.posZ + var5, var2, var3);
            this.yOffset = 0.0F;
            float var6 = (float)var1 * 0.2F;
            this.setArrowHeading(varX, varY + (double)var6, varZ, speed);
		}
	}
	
	public EntityOracleBulletBase(World world, EntityLiving shooter, float speed)
	{
		super(world);
		this.shootingEntity = shooter;
		this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, speed * 1.5F);
	}
	
	public void setArrowHeading(double x, double y, double z, float speed)
    {
        float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= (double)f;
        y /= (double)f;
        z /= (double)f;
        x *= (double)speed;
        y *= (double)speed;
        z *= (double)speed;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        float f1 = MathHelper.sqrt_double(x * x + z * z);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f1) * 180.0D / Math.PI);
    }
	
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, (double)f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
	}
	
	@Override
    public void onUpdate()
	{
		lastTickPosX = posX;
        lastTickPosY = posY;
        lastTickPosZ = posZ;
		super.onUpdate();
		posX += motionX;
        posY += motionY;
        posZ += motionZ;
        setPosition(posX, posY, posZ);
		
		if (++ticksInAir > 100)
		{
			this.setDead();
		}
		
		Vec3D var1 = Vec3D.createVector(posX, posY, posZ);
		Vec3D var2 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition var3 = worldObj.rayTraceBlocks_do_do(var1, var2, true, false);
		var1 = Vec3D.createVector(posX, posY, posZ);
		var2 = Vec3D.createVector(posX + motionX, posY + motionY, posZ + motionZ);
		
		if (var3 != null)
		{
			var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
		}
		
		if (worldObj != null)
		{
			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double lastDistance = 0.0D;
			
			if (!list.isEmpty())
			{
				for (int i = 0; i < list.size(); i++)
				{
					Entity entity1 = (Entity)list.get(i);
					
					if (entity1 == null || entity1 != null && entity1 == shootingEntity || entity1 instanceof EntityOracleBulletBase)
					{
						continue;
					}
					
					float range = 0.3F;
					AxisAlignedBB var4 = entity1.boundingBox.expand(range, range, range);
					MovingObjectPosition var5 = var4.calculateIntercept(var1, var2);
					
					if (var5 == null)
					{
						continue;
					}
					
					double distance = var1.distanceTo(var5.hitVec);
					
					if (distance < lastDistance || lastDistance == 0.0D)
					{
						entity = entity1;
						lastDistance = distance;
					}
				}
				
				if (entity != null)
				{
					var3 = new MovingObjectPosition(entity);
				}
			}
		}
		
		if (var3 != null)
		{
			onImpact(var3);
		}
	}
	
	protected abstract void onImpact(MovingObjectPosition movingobjectposition);
	
	public void setRainbow()
	{
		isRainbow = true;
	}
	
	public void setBulletColor(int i)
	{
		color = i;
	}
	
	public int getBulletColor()
	{
		return color;
	}
	
	public int getTick()
	{
		return ticksInAir;
	}
	
	public void setDamage(double par1)
    {
        this.damage = par1;
    }
	
	public double getDamage()
    {
        return this.damage;
    }
	
	public Random getRNG()
	{
		return rand;
	}
	
	@Override
	protected void entityInit() {}
	@Override
    public boolean isInRangeToRenderVec3D(Vec3D vec3d)
	{
		return true;
	}
	@Override
	public boolean isInRangeToRenderDist(double par1)
	{
		return true;
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}
	/*@Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}*/
	@Override
	public boolean canAttackWithItem()
    {
        return false;
    }
}