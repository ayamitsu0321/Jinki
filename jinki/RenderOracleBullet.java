package net.minecraft.src.jinki;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class RenderOracleBullet extends Render
{
	private static double vecBase = 0.2D;
	private static double[][] vertex = new double[][] {
		{ 0, vecBase * 0.5D, 0 },
		{ vecBase,  vecBase,  vecBase },
		{ vecBase,  vecBase, -vecBase },
		{-vecBase,  vecBase, -vecBase },
		{-vecBase,  vecBase,  vecBase },
		{ vecBase, -vecBase,  vecBase },
		{ vecBase, -vecBase, -vecBase },
		{-vecBase, -vecBase, -vecBase },
		{-vecBase, -vecBase,  vecBase },
		{ 0, -vecBase * 0.5D, 0 }
	};
	
	private static int face[][] = new int[][] {
		{0, 1, 2, 3},
		{0, 3, 4, 1},
		{1, 5, 6, 2},
		{4, 8, 5, 1},
		{3, 7, 8, 4},
		{2, 6, 7, 3},
		{6, 5, 9, 7},
		{9, 5, 8, 7},
	};
	
	public RenderOracleBullet() {}
	
	public void renderOracleBullet(EntityOracleBulletBase bullet, double x, double y, double z, float yaw, float partialTickTime)
	{
		Tessellator tessellator = Tessellator.instance;
		
		try
		{
			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        	GL11.glDisable(2896 /*GL_LIGHTING*/);
			GL11.glPushMatrix();
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(3042 /*GL_BLEND*/);
        	GL11.glBlendFunc(770, 1);
			//GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
			
			GL11.glTranslatef((float)x, (float)y, (float)z);
			GL11.glRotatef(bullet.rotationYaw + 45F, 0.0F, 1.0F, 0.0F);
        	GL11.glRotatef(-bullet.rotationPitch + 45F, 1.0F, 0.0F, 1.0F);
			this.drawBox(bullet, tessellator, 0, 0, 0);
			GL11.glDisable(3042 /*GL_BLEND*/);
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
			GL11.glEnable(2896 /*GL_LIGHTING*/);
        	GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		if (par1Entity instanceof EntityOracleBulletBase)
		{
			this.renderOracleBullet((EntityOracleBulletBase)par1Entity, par2, par4, par6, par8, par9);
		}
	}
	
	protected void drawBox(EntityOracleBulletBase bullet, Tessellator var1, double x, double y, double z)
	{
		var1.startDrawingQuads();
		int color = bullet.isRainbow ? this.getRainbow(bullet.getTick() * 20) : bullet.getBulletColor() ;
		float r = (float)(color >> 16 & 0xff) / 255F;
		float g = (float)(color >> 8 & 0xff) / 255F;
		float b = (float)(color & 0xff) / 255F;
		var1.setColorRGBA_F(r, g, b, 0.5F);
		//var1.setBrightness(0x0000ff);
		
		for (int i = 0; i < 4; i++)
		{
			float scale = 1.0F + 0.25F * (float)i;
			
			for (int j = 0; j < face.length; j++)
			{
				var1.addVertex(vertex[face[j][0]][0] * scale, vertex[face[j][0]][1] * scale, vertex[face[j][0]][2] * scale);
                var1.addVertex(vertex[face[j][1]][0] * scale, vertex[face[j][1]][1] * scale, vertex[face[j][1]][2] * scale);
                var1.addVertex(vertex[face[j][2]][0] * scale, vertex[face[j][2]][1] * scale, vertex[face[j][2]][2] * scale);
                var1.addVertex(vertex[face[j][3]][0] * scale, vertex[face[j][3]][1] * scale, vertex[face[j][3]][2] * scale);
			}
		}
		
		var1.draw();
	}
	
	public final int getRainbow(int num)
	{
		int r = num % 1536;
		int g = ((num % 1536) + 1024) % 1536;
		int b = ((num % 1536) + 512) % 1536;
		return 0x000000 + (fixColor(r) << 16) + (fixColor(g) << 8) + (fixColor(b));
	}
	
	public final int fixColor(int num)
	{
		int color = num >= 1024 ? 0 : num < 256 ? num : num > 768 ? (1023 - num) : 255;
		return color;
	}
}