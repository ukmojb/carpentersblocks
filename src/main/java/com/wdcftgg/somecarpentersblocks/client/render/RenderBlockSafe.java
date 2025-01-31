package com.wdcftgg.somecarpentersblocks.client.render;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.BlockSafe;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.somecarpentersblocks.client.model.ModelSafe;
import com.wdcftgg.somecarpentersblocks.client.model.ModelSafeOpen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.Calendar;


public class RenderBlockSafe extends TileEntitySpecialRenderer<TileEntitySafe>
{
    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation(SomeCarpentersBlocks.MODID, "textures/model/safe.png");
    private static final ResourceLocation TEXTURE_NORMAL_OPEN = new ResourceLocation(SomeCarpentersBlocks.MODID, "textures/model/safe_open.png");
    private static final ResourceLocation TEXTURE_LIGHT = new ResourceLocation(SomeCarpentersBlocks.MODID, "textures/model/light.png");
    private static final ResourceLocation TEXTURE_HANDLE = new ResourceLocation(SomeCarpentersBlocks.MODID, "textures/model/handle.png");
    private final ModelSafe simpleChest = new ModelSafe();
    private final ModelSafeOpen simpleChestOpen = new ModelSafeOpen();


    @Override
    public void render(TileEntitySafe te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;

        if (te.hasWorld())
        {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockSafe && i == 0)
            {
//                ((BlockSafe)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }

//            te.checkForAdjacentChests();
        }
        else
        {
            i = 0;
        }

//            ModelChest modelchest;
        ModelBase modelchest = te.numPlayersUsing > 0 ? this.simpleChestOpen : this.simpleChest;



        if (destroyStage >= 0)
        {
            GlStateManager.pushMatrix();
//            GL11.glDisable(GL11.GL_CULL_FACE);
            renderDestoy(DESTROY_STAGES[destroyStage], x, y, z);
//            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.popMatrix();
        }




        IBlockState iBlockState = te.getiBlockState();
        if (iBlockState != null && te.hasBlock()) {
            ResourceLocation res = getResFromTexture(iBlockState);
            if (res != null) {


                GlStateManager.enableBlend();
                Minecraft.getMinecraft().getTextureManager().bindTexture(res);
                GlStateManager.disableBlend();

                int u = 0;

                if (i == 2)
                {
                    u = -90;
                }

                if (i == 3)
                {
                    u = 90;
                }

                if (i == 4)
                {
                    u = 0;
                }

                if (i == 5)
                {
                    u = 180;
                }


//                renderHandle(x, y, z, u);

                randerLight(x, y, z, u);

                renderFourFace(res, x, y, z, u);


            } else {
                this.bindTexture(te.numPlayersUsing > 0 ? TEXTURE_NORMAL_OPEN : TEXTURE_NORMAL);
            }
        } else {
            this.bindTexture(te.numPlayersUsing > 0 ? TEXTURE_NORMAL_OPEN : TEXTURE_NORMAL);
        }



        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);

        int j = 0;

        if (i == 2)
        {
            j = 180;
        }

        if (i == 3)
        {
            j = 0;
        }

        if (i == 4)
        {
            j = 90;
        }

        if (i == 5)
        {
            j = -90;
        }

        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);


        GlStateManager.translate(((float) 8 / 16), -((float) 6 / 16), ((float) 8 / 16));
        GlStateManager.scale(0.9F, 0.9F, 0.9F);

        renderSafe(modelchest);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);



    }



    private void renderSafe(ModelBase modelBase) {
        modelBase.render(Minecraft.getMinecraft().player, 0, 0, 0, 0, 0, 0.0625F);
    }

    private ResourceLocation getResFromTexture(IBlockState state) {
        try {

            String s = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state).getParticleTexture().toString();
            s = s.substring(25, (s.indexOf("',")));

            if(s == null || s.isEmpty() || s.equals("missingno") || s.equals("missing"))
                return null;

            String[] split = s.split(":");

            String prefix = "";
            String suffix = "";

            if(split.length == 2) {
                prefix = split[0];
                suffix = split[1];
            } else {
                prefix = "minecraft";
                suffix = s;
            }

            return new ResourceLocation(prefix + ":textures/" + suffix + ".png");
        } catch (ArrayIndexOutOfBoundsException a){
            return null;
        }
    }

    private void renderHandle(double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.enableBlend();
        GlStateManager.translate(x + (double) 32.8 / 16, y + ((double) 9.3 / 16), z + (double) 13 / 16 + 1.001);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 1, (double) 4 / 16, (double) 1 / 16);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_HANDLE);
        Tessellator tessellator2 = Tessellator.getInstance();
        BufferBuilder buffer2 = tessellator2.getBuffer();
        buffer2.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        //左右
        buffer2.pos(-1, 0, 0).tex(0,0).endVertex();
        buffer2.pos(1, 0, 0).tex(1,0).endVertex();
        buffer2.pos(1, -2, 0).tex(1,1).endVertex();
        buffer2.pos(-1, -2, 0).tex(0,1).endVertex();

//                buffer2.pos(-1, 0, -1.001).tex(0,0).endVertex();
//                buffer2.pos(1, 0, -1.001).tex(1,0).endVertex();
//                buffer2.pos(1, -2, -1.001).tex(1,1).endVertex();
//                buffer2.pos(-1, -2, -1.001).tex(0,1).endVertex();

        //前后
//
        buffer2.pos(-2.001, 0.028, -0.47).tex(0,0).endVertex();
        buffer2.pos(-2.001, 0.028, 0.4).tex(1,0).endVertex();
        buffer2.pos(-2.001, -0.87, 0.4).tex(1,1).endVertex();
        buffer2.pos(-2.001, -0.87, -0.47).tex(0,1).endVertex();
        tessellator2.draw();


        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void randerLight(double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.translate(x + (0.5), y + ((double) 15 / 16), z + 0.5);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 7.22 / 16, (double) 6.22 / 16, (double) 2.22 / 16);
        this.bindTexture(TEXTURE_LIGHT);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);


        buffer.pos(-1.001, -(double) 3.2 / 16, -(double) 44.5 / 16).tex(0,0).endVertex();
        buffer.pos(-1.001, -(double) 3.2 / 16, -(double) 18.5 / 16).tex(1,0).endVertex();
        buffer.pos(-1.001, -(double) 34.5 / 16, -(double) 18.5 / 16).tex(1,1).endVertex();
        buffer.pos(-1.001, -(double) 34.5 / 16, -(double) 44.5 / 16).tex(0,1).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }


    private void renderFourFace(ResourceLocation res, double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.translate(x + (0.5), y + ((double) 15 / 16), z + 0.5);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 7.22 / 16, (double) 7.22 / 16, (double) 7.22 / 16);
        this.bindTexture(res);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        //上下
        buffer.pos(-1, -0.026, 1).tex(0,1).endVertex();
        buffer.pos(1, -0.026, 1).tex(0,0).endVertex();
        buffer.pos(1, -0.026, -1).tex(1,0).endVertex();
        buffer.pos(-1, -0.026, -1).tex(1,1).endVertex();

        buffer.pos(1, -(double) 32.5 / 16, -1).tex(0,1).endVertex();
        buffer.pos(-1, -(double) 32.5 / 16, -1).tex(0,0).endVertex();
        buffer.pos(-1, -(double) 32.5 / 16, 1).tex(1,0).endVertex();
        buffer.pos(1, -(double) 32.5 / 16, 1).tex(1,1).endVertex();

        //左右
        buffer.pos(-1, -0.026, 1.001).tex(0,0).endVertex();
        buffer.pos(1, -0.026, 1.001).tex(1,0).endVertex();
        buffer.pos(1, -2.026, 1.001).tex(1,1).endVertex();
        buffer.pos(-1, -2.026, 1.001).tex(0,1).endVertex();

        buffer.pos(-1, -0.026, -1.001).tex(0,0).endVertex();
        buffer.pos(1, -0.026, -1.001).tex(1,0).endVertex();
        buffer.pos(1, -2.026, -1.001).tex(1,1).endVertex();
        buffer.pos(-1, -2.026, -1.001).tex(0,1).endVertex();

        //前后
        buffer.pos(1.001, -0.026, -1).tex(0,0).endVertex();
        buffer.pos(1.001, -0.026, 1).tex(1,0).endVertex();
        buffer.pos(1.001, -2.026, 1).tex(1,1).endVertex();
        buffer.pos(1.001, -2.026, -1).tex(0,1).endVertex();

        buffer.pos(-0.901, -0.026, -1).tex(0,0).endVertex();
        buffer.pos(-0.901, -0.026, 1).tex(1,0).endVertex();
        buffer.pos(-0.901, -2.026, 1).tex(1,1).endVertex();
        buffer.pos(-0.901, -2.026, -1).tex(0,1).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.disableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderDestoy(ResourceLocation res, double x, double y, double z) {
        GlStateManager.matrixMode(1890);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enablePolygonOffset();
        GlStateManager.matrixMode(1888);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(x + 0.5, y + ((double) 15.9 / 16), z + 0.5);
//        GlStateManager.scale((double) 7.22 / 16 + (0.5), (double) 7.22 / 16 + (0.5), (double) 7.22 / 16 + (0.5));
        GlStateManager.scale((double) 7.22 / 16, (double) 7.22 / 16, (double) 7.22 / 16);
        GlStateManager.doPolygonOffset(-2, 0);
        this.bindTexture(res);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        //上下
        buffer.pos(-1, -0.026, 1).tex(-0.5,0.5).endVertex();
        buffer.pos(1, -0.026, 1).tex(-0.5,-0.5).endVertex();
        buffer.pos(1, -0.026, -1).tex(0.5,-0.5).endVertex();
        buffer.pos(-1, -0.026, -1).tex(0.5,0.5).endVertex();

        buffer.pos(1, -(double) 32.5 / 16, -1).tex(-0.5,0.5).endVertex();
        buffer.pos(-1, -(double) 32.5 / 16, -1).tex(-0.5,-0.5).endVertex();
        buffer.pos(-1, -(double) 32.5 / 16, 1).tex(0.5,-0.5).endVertex();
        buffer.pos(1, -(double) 32.5 / 16, 1).tex(0.5,0.5).endVertex();

        //左右
        buffer.pos(-1, -0.026, 1.001).tex(-0.5,-0.5).endVertex();
        buffer.pos(1, -0.026, 1.001).tex(0.5,-0.5).endVertex();
        buffer.pos(1, -2.026, 1.001).tex(0.5,0.5).endVertex();
        buffer.pos(-1, -2.026, 1.001).tex(-0.5,0.5).endVertex();

        buffer.pos(-1, -0.026, -1.001).tex(-0.5,-0.5).endVertex();
        buffer.pos(1, -0.026, -1.001).tex(0.5,-0.5).endVertex();
        buffer.pos(1, -2.026, -1.001).tex(0.5,0.5).endVertex();
        buffer.pos(-1, -2.026, -1.001).tex(-0.5,0.5).endVertex();

        //前后
        buffer.pos(1.001, -0.026, -1).tex(0,0).endVertex();
        buffer.pos(1.001, -0.026, 1).tex(1,0).endVertex();
        buffer.pos(1.001, -2.026, 1).tex(1,1).endVertex();
        buffer.pos(1.001, -2.026, -1).tex(0,1).endVertex();

        buffer.pos(-0.901, -0.026, -1).tex(0,0).endVertex();
        buffer.pos(-0.901, -0.026, 1).tex(1,0).endVertex();
        buffer.pos(-0.901, -2.026, 1).tex(1,1).endVertex();
        buffer.pos(-0.901, -2.026, -1).tex(0,1).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableRescaleNormal();
        GlStateManager.matrixMode(1890);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(1888);
    }



}