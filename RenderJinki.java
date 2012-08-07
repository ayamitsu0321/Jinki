package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.forge.IItemRenderer;
import net.minecraft.src.forge.IItemRenderer.ItemRenderType;
import net.minecraft.src.forge.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import java.util.Random;
import java.lang.reflect.Modifier;

public class RenderJinki implements IItemRenderer
{
	Random random = new Random();
	RenderManager renderManager = RenderManager.instance;;
	RenderEngine renderEngine = ModLoader.getMinecraftInstance().renderEngine;;
	public boolean inColor = true;
	public float zLevel = 0.0F;
	Minecraft mc = ModLoader.getMinecraftInstance();
	
	public RenderJinki()
	{
	}
	
	//IJinkiを実装してるならこのRenderを使う
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return isImplemented(item.getItem().getClass(), IJinki.class);
	}
	
	//ここらへんは気にしないで、しぬから
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return  !(type == ItemRenderType.EQUIPPED || type == ItemRenderType.FIRST_PERSON_MAP || helper == ItemRendererHelper.ENTITY_ROTATION || type == ItemRenderType.INVENTORY);
	}
	
	//Renderの本体、ItemRenderTypeでどこでRenderしてるか判定に使う
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		//IJinkiを継承してるか
		if (item != null && isImplemented(item.getItem().getClass(), IJinki.class))
		{
			GL11.glPushMatrix();
			IJinki jinki = (IJinki)item.getItem();
			int icon = jinki.getJinkiIcon(item);
			this.loadTexture(item.getItem().getTextureFile());
			
			if (type == ItemRenderType.ENTITY)
			{
				//なにもない
			}
			
			if (type == ItemRenderType.EQUIPPED)//手に持ってるとき
			{
				//RenderPlayer
				Tessellator var4 = Tessellator.instance;
				int tex = icon;
				float var6 = ((float)(tex % 16 * 16) + 0.0F) / 256.0F;
	            float var7 = ((float)(tex % 16 * 16) + 15.99F) / 256.0F;
	            float var8 = ((float)(tex / 16 * 16) + 0.0F) / 256.0F;
	            float var9 = ((float)(tex / 16 * 16) + 15.99F) / 256.0F;
				
				int color = jinki.getColorFromItemStack(item);//色取得
				float var26 = (float)(color >> 16 & 255) / 255.0F;
                float var10 = (float)(color >> 8 & 255) / 255.0F;
                float var11 = (float)(color & 255) / 255.0F;
				
				if (inColor)
				{
					GL11.glColor4f(var26, var10, var11, 1.0F);//乗算
				}
				
				//draw
				renderItemIn2D(var4, var7, var8, var6, var9);
				
				//エフェクトをもってるなら
				if (item.hasEffect())
				{
					GL11.glDepthFunc(GL11.GL_EQUAL);
	                GL11.glDisable(GL11.GL_LIGHTING);
	                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("%blur%/misc/glint.png"));
	                GL11.glEnable(GL11.GL_BLEND);
	                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
	                float var13 = 0.76F;
	                GL11.glColor4f(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
	                GL11.glMatrixMode(GL11.GL_TEXTURE);
	                GL11.glPushMatrix();
	                float var14 = 0.125F;
	                GL11.glScalef(var14, var14, var14);
	                float var15 = (float)(System.currentTimeMillis() % 3000L) / 3000.0F * 8.0F;
	                GL11.glTranslatef(var15, 0.0F, 0.0F);
	                GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
	                this.renderItemIn2D(var4, 0.0F, 0.0F, 1.0F, 1.0F);
	                GL11.glPopMatrix();
	                GL11.glPushMatrix();
	                GL11.glScalef(var14, var14, var14);
	                var15 = (float)(System.currentTimeMillis() % 4873L) / 4873.0F * 8.0F;
	                GL11.glTranslatef(-var15, 0.0F, 0.0F);
	                GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
	                this.renderItemIn2D(var4, 0.0F, 0.0F, 1.0F, 1.0F);
	                GL11.glPopMatrix();
	                GL11.glMatrixMode(GL11.GL_MODELVIEW);
	                GL11.glDisable(GL11.GL_BLEND);
	                GL11.glEnable(GL11.GL_LIGHTING);
	                GL11.glDepthFunc(GL11.GL_LEQUAL);
				}
			}
			else if (type == ItemRenderType.INVENTORY)//GUIのとこ
			{
				//RenderItem#drawItemIntoGui(fontrenderer, renderengine, itemid, itemdamage, iconindex, x, y)
				int var8;
				
				int color = jinki.getColorFromItemStack(item);//色取得
                float r = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float b = (float)(color & 255) / 255.0F;

                if (this.inColor)
                {
                    GL11.glColor4f(r, g, b, 1.0F);//乗算
                }

				//draw
                this.renderTexturedQuad(0, 0, icon % 16 * 16, icon / 16 * 16, 16, 16);
			}
			else//それ以外、EntityItemやら
			{
				int color = jinki.getColorFromItemStack(item);//色取得
                float r = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float b = (float)(color & 255) / 255.0F;

                if (this.inColor)
                {
                    GL11.glColor4f(r, g, b, 1.0F);//乗算
                }
				
				//draw
				func_40267_a(icon, item.stackSize);
			}
			
			GL11.glPopMatrix();
		}
	}
	
	protected void loadTexture(String texture)
    {
        renderEngine.bindTexture(renderEngine.getTexture(texture));
    }
	
	//from RenderItem、EntityItemのRender
	private void func_40267_a(int par1, int par2)
    {
        Tessellator var3 = Tessellator.instance;
        float var4 = (float)(par1 % 16 * 16 + 0) / 256.0F;
        float var5 = (float)(par1 % 16 * 16 + 16) / 256.0F;
        float var6 = (float)(par1 / 16 * 16 + 0) / 256.0F;
        float var7 = (float)(par1 / 16 * 16 + 16) / 256.0F;
        float var8 = 1.0F;
        float var9 = 0.5F;
        float var10 = 0.25F;

        for (int var11 = 0; var11 < par2; ++var11)
        {
            GL11.glPushMatrix();

            if (var11 > 0)
            {
                float var12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float var13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float var14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                GL11.glTranslatef(var12, var13, var14);
            }

            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            var3.startDrawingQuads();
            var3.setNormal(0.0F, 1.0F, 0.0F);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
            var3.addVertexWithUV((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
            var3.addVertexWithUV((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
            var3.draw();
            GL11.glPopMatrix();
        }
    }
	
	//手にもってるときのRender
	private void renderItemIn2D(Tessellator par1Tessellator, float par2, float par3, float par4, float par5)
    {
        float var6 = 1.0F;
        float var7 = 0.0625F;
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, 1.0F);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)par2, (double)par5);
        par1Tessellator.addVertexWithUV((double)var6, 0.0D, 0.0D, (double)par4, (double)par5);
        par1Tessellator.addVertexWithUV((double)var6, 1.0D, 0.0D, (double)par4, (double)par3);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, (double)par2, (double)par3);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, -1.0F);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, (double)(0.0F - var7), (double)par2, (double)par3);
        par1Tessellator.addVertexWithUV((double)var6, 1.0D, (double)(0.0F - var7), (double)par4, (double)par3);
        par1Tessellator.addVertexWithUV((double)var6, 0.0D, (double)(0.0F - var7), (double)par4, (double)par5);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, (double)(0.0F - var7), (double)par2, (double)par5);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        int var8;
        float var9;
        float var10;
        float var11;

        for (var8 = 0; var8 < 16; ++var8)
        {
            var9 = (float)var8 / 16.0F;
            var10 = par2 + (par4 - par2) * var9 - 0.001953125F;
            var11 = var6 * var9;
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)par3);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (var8 = 0; var8 < 16; ++var8)
        {
            var9 = (float)var8 / 16.0F;
            var10 = par2 + (par4 - par2) * var9 - 0.001953125F;
            var11 = var6 * var9 + 0.0625F;
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, (double)(0.0F - var7), (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 1.0D, 0.0D, (double)var10, (double)par3);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, 0.0D, (double)var10, (double)par5);
            par1Tessellator.addVertexWithUV((double)var11, 0.0D, (double)(0.0F - var7), (double)var10, (double)par5);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (var8 = 0; var8 < 16; ++var8)
        {
            var9 = (float)var8 / 16.0F;
            var10 = par5 + (par3 - par5) * var9 - 0.001953125F;
            var11 = var6 * var9 + 0.0625F;
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)par2, (double)var10);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, -1.0F, 0.0F);

        for (var8 = 0; var8 < 16; ++var8)
        {
            var9 = (float)var8 / 16.0F;
            var10 = par5 + (par3 - par5) * var9 - 0.001953125F;
            var11 = var6 * var9;
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, 0.0D, (double)par4, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, 0.0D, (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV(0.0D, (double)var11, (double)(0.0F - var7), (double)par2, (double)var10);
            par1Tessellator.addVertexWithUV((double)var6, (double)var11, (double)(0.0F - var7), (double)par4, (double)var10);
        }

        par1Tessellator.draw();
    }
	
	//GUIの中のItemのRender
	public void renderTexturedQuad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + par6) * var8));
        var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + par6) * var8));
        var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + 0) * var8));
        var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + 0) * var8));
        var9.draw();
    }
	
	//Interfaceを実装してるか
	public static boolean isImplemented(Class<?> clazz, Class<?> intrfc)
	{
        if (clazz == null || intrfc == null)
		{
            return false;
        }
        
        if (!clazz.isInterface() && intrfc.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()))
		{
            return true;
        }
		
        return false;
    }
}