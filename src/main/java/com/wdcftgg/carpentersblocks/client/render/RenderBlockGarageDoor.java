package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockSafe;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityGarageDoor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class RenderBlockGarageDoor extends TileEntitySpecialRenderer<TileEntityGarageDoor> {

    @Override
    public void render(TileEntityGarageDoor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
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

        if (te.isOpen() ) {

            if (destroyStage >= 0)
            {
                GlStateManager.pushMatrix();
                renderOpenDestoy(DESTROY_STAGES[destroyStage], x, y, z);
                GlStateManager.popMatrix();
            }

        } else {

            if (destroyStage >= 0)
            {
                GlStateManager.pushMatrix();
                renderDestoy(DESTROY_STAGES[destroyStage], x, y, z);
                GlStateManager.popMatrix();
            }
        }


        IBlockState iBlockState = te.getiBlockState();
        if (iBlockState != null) {

            ResourceLocation res;
            if (!te.hasBlock()) {
                res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/oak_planks.png");
            } else {
                res = getResFromTexture(iBlockState);
            }

            if (res != null) {

                int u = 0;


                if (i == 7) {
                    u = 90;
                }

                if (i == 4) {
                    u = 180;
                }

                if (i == 6) {
                    u = -90;
                }

                if (i == 5) {
                    u = 0;
                }

                if (i == 2) {
                    u = -90;
                }

                if (i == 0) {
                    u = 180;
                }

                if (i == 3) {
                    u = 90;
                }

                if (te.isOpen()) {
                    renderOpenFace(res, x, y, z, u);
                } else {
                    renderFrontBackFace(res, x, y, z, u);
                    renderOtherFace(res, x, y, z, u);

                }
            }
        }




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

            ResourceLocation resourceLocation = new ResourceLocation(prefix + ":textures/" + suffix + ".png");

            resourceLocation = setModRes(prefix, suffix, resourceLocation);

            return resourceLocation;
        } catch (ArrayIndexOutOfBoundsException a){
            return null;
        }
    }

    public ResourceLocation setModRes(String modid, String name, ResourceLocation resourceLocation) {
//        System.out.println("-" + modid + "-");
        if (Objects.equals(modid, "chisel")) {
//                System.out.println(name);
            if (name.contains("wallvents-ctmh")) {
                resourceLocation = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/wallvents-ctmh.png");
            }
        }

        return resourceLocation;
    }


    private void renderFrontBackFace(ResourceLocation res, double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
        GlStateManager.enablePolygonOffset();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.translate(x + (0.5), y + ((double) 16 / 16), z + 0.5);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 8 / 16, (double) 8 / 16, (double) 8 / 16);
        GlStateManager.doPolygonOffset(-1, 0);
        this.bindTexture(res);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        //前
        buffer.pos(-1, 0, -0.495).tex(0, 0).endVertex(); // 左上
        buffer.pos(-(double) 6 / 16, 0, -0.495).tex((double) 5 / 16, 0).endVertex(); // 左中上
        buffer.pos(-(double) 6 / 16, -2, -0.495).tex((double) 5 / 16, 1).endVertex(); // 左中下
        buffer.pos(-1, -2, -0.495).tex(0, 1).endVertex(); // 左下

        buffer.pos((double) 6 / 16, 0, -0.495).tex((double) 11 / 16, 0).endVertex(); // 右中上
        buffer.pos(1, 0, -0.495).tex(1, 0).endVertex(); // 右上
        buffer.pos(1, -2, -0.495).tex(1, 1).endVertex(); // 右下
        buffer.pos((double) 6 / 16, -2, -0.495).tex((double) 11 / 16, 1).endVertex(); // 右中下

        buffer.pos(-(double) 6 / 16, 0, -0.495).tex((double) 5 / 16, 0).endVertex(); // 中上左
        buffer.pos((double) 6 / 16, 0, -0.495).tex((double) 11 / 16, 0).endVertex(); // 中上右
        buffer.pos((double) 6 / 16, -0.5, -0.495).tex((double) 11 / 16, (double) 4 / 16).endVertex(); // 中中右
        buffer.pos(-(double) 6 / 16, -0.5, -0.495).tex((double) 5 / 16, (double) 4 / 16).endVertex(); // 中中左

        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -0.495).tex((double) 5 / 16, (double) 6 / 16).endVertex(); // 中上左
        buffer.pos((double) 6 / 16, -(double) 12 / 16, -0.495).tex((double) 11 / 16, (double) 6 / 16).endVertex(); // 中上右
        buffer.pos((double) 6 / 16, -2, -0.495).tex((double) 11 / 16, (double) 16 / 16).endVertex(); // 中中右
        buffer.pos(-(double) 6 / 16, -2, -0.495).tex((double) 5 / 16, (double) 16 / 16).endVertex(); // 中中左
//
//        buffer.pos(-(double) 6 / 16, -2, -0.495).tex((double) 5 / 16, -(double) 0 / 16).endVertex(); // 中上左
//        buffer.pos((double) 6 / 16, -2, -0.495).tex((double) 11 / 16, -(double) 0 / 16).endVertex(); // 中上右
//        buffer.pos((double) 6 / 16, -(double) 12 / 16, -0.495).tex((double) 11 / 16, -(double) 16 / 16).endVertex(); // 中中右
//        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -0.495).tex((double) 5 / 16, -(double) 16 / 16).endVertex(); // 中中左

        //后
        buffer.pos(-1, 0, -0.75).tex(0, 0).endVertex(); // 左上
        buffer.pos(-(double) 6 / 16, 0, -0.75).tex(0.25, 0).endVertex(); // 左中上
        buffer.pos(-(double) 6 / 16, -2, -0.75).tex(0.25, 1).endVertex(); // 左中下
        buffer.pos(-1, -2, -0.75).tex(0, 1).endVertex(); // 左下

        buffer.pos((double) 6 / 16, 0, -0.75).tex(0.75, 0).endVertex(); // 右中上
        buffer.pos(1, 0, -0.75).tex(1, 0).endVertex(); // 右上
        buffer.pos(1, -2, -0.75).tex(1, 1).endVertex(); // 右下
        buffer.pos((double) 6 / 16, -2, -0.75).tex(0.75, 1).endVertex(); // 右中下

        buffer.pos(-(double) 6 / 16, 0, -0.75).tex(0.25, 0).endVertex(); // 中上左
        buffer.pos((double) 6 / 16, 0, -0.75).tex(0.75, 0).endVertex(); // 中上右
        buffer.pos((double) 6 / 16, -0.5, -0.75).tex(0.75, 0.25).endVertex(); // 中中右
        buffer.pos(-(double) 6 / 16, -0.5, -0.75).tex(0.25, 0.25).endVertex(); // 中中左

//        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -0.75).tex(0.75,  (double) 10 / 16).endVertex(); // 中上左
//        buffer.pos((double) 6 / 16, -(double) 12 / 16, -0.75).tex(0.25, (double) 10 / 16).endVertex(); // 中上右
//        buffer.pos((double) 6 / 16, -2, -0.75).tex(0.25, 0).endVertex(); // 中中右
//        buffer.pos(-(double) 6 / 16, -2, -0.75).tex(0.75, 0).endVertex(); // 中中左
        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -0.75).tex((double) 5 / 16, (double) 6 / 16).endVertex(); // 中上左
        buffer.pos((double) 6 / 16, -(double) 12 / 16, -0.75).tex((double) 11 / 16, (double) 6 / 16).endVertex(); // 中上右
        buffer.pos((double) 6 / 16, -2, -0.75).tex((double) 11 / 16, (double) 16 / 16).endVertex(); // 中中右
        buffer.pos(-(double) 6 / 16, -2, -0.75).tex((double) 5 / 16, (double) 16 / 16).endVertex(); // 中中左


        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
//        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderOtherFace(ResourceLocation res, double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
        GlStateManager.enablePolygonOffset();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.translate(x + (0.5), y + ((double) 16 / 16), z + 0.5);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 8 / 16, (double) 8 / 16, (double) 8 / 16);
        GlStateManager.doPolygonOffset((float) -0.5, 0);
        this.bindTexture(res);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        //上下
//        buffer.pos(-1, -0.026, 1).tex(0,1).endVertex();
//        buffer.pos(1, -0.026, 1).tex(0,0).endVertex();
//        buffer.pos(1, -0.026, -1).tex(1,0).endVertex();
//        buffer.pos(-1, -0.026, -1).tex(1,1).endVertex();

        buffer.pos(-1, 0.001 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos(1, 0.001 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, 0.001 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, 0.001 / 16, -(double) 8 / 16).tex(1,1).endVertex();

        buffer.pos(-1, -(double) 32.1 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos(1, -(double) 32.1 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, -(double) 32.1 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, -(double) 32.1 / 16, -(double) 8 / 16).tex(1,1).endVertex();


        //左右
        buffer.pos(1, 0, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, 0, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(1, -2, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(1, -2, -(double) 12 / 16).tex(0,1).endVertex();

        buffer.pos(-1, 0, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(-1, 0, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, -2, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(-1, -2, -(double) 12 / 16).tex(0,1).endVertex();

        //内部
        buffer.pos(0.375, -(double) 8 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(0.375, -(double) 8 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(0.375, -(double) 12 / 16, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(0.375, -(double) 12 / 16, -(double) 12 / 16).tex(0,1).endVertex();

        buffer.pos(-0.375, -(double) 8 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(-0.375, -(double) 8 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-0.375, -(double) 12 / 16, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(-0.375, -(double) 12 / 16, -(double) 12 / 16).tex(0,1).endVertex();

        buffer.pos(-(double) 6 / 16, -(double) 8 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos((double) 6 / 16, -(double) 8 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos((double) 6 / 16, -(double) 8 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-(double) 6 / 16, -(double) 8 / 16, -(double) 8 / 16).tex(1,1).endVertex();

        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos((double) 6 / 16, -(double) 12 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos((double) 6 / 16, -(double) 12 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -(double) 8 / 16).tex(1,1).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
//        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderOpenFace(ResourceLocation res, double x, double y, double z, int j) {
        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
        GlStateManager.enablePolygonOffset();
        GlStateManager.disableLighting();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.translate(x + (0.5), y + ((double) 16 / 16), z + 0.5);
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale((double) 8 / 16, (double) 8 / 16, (double) 8 / 16);
        GlStateManager.doPolygonOffset((float) -0.5, 0);
        this.bindTexture(res);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        //前后
        buffer.pos(-1, 0, -0.495).tex(0, 0).endVertex(); // 左上
        buffer.pos((double) 16 / 16, 0, -0.495).tex(1, 0).endVertex(); // 左中上
        buffer.pos((double) 16 / 16, -(double) 16 / 16, -0.495).tex(1, 1).endVertex(); // 左中下
        buffer.pos(-1, -(double) 16 / 16, -0.495).tex(0, 1).endVertex();

        buffer.pos(-1, 0, -0.75).tex(0, 0).endVertex(); // 左上
        buffer.pos((double) 16 / 16, 0, -0.75).tex(1, 0).endVertex(); // 左中上
        buffer.pos((double) 16 / 16, -(double) 16 / 16, -0.75).tex(1, 1).endVertex(); // 左中下
        buffer.pos(-1, -(double) 16 / 16, -0.75).tex(0, 1).endVertex();

        //上下
        buffer.pos(-1, 0.001 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos(1, 0.001 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, 0.001 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, 0.001 / 16, -(double) 8 / 16).tex(1,1).endVertex();

        buffer.pos(-1, -(double) 16.1 / 16, -(double) 12 / 16).tex(0,1).endVertex();
        buffer.pos(1, -(double) 16.1 / 16, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, -(double) 16.1 / 16, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, -(double) 16.1 / 16, -(double) 8 / 16).tex(1,1).endVertex();

        //左右
        buffer.pos(1, 0, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(1, 0, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(1, -1, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(1, -1, -(double) 12 / 16).tex(0,1).endVertex();

        buffer.pos(-1, 0, -(double) 12 / 16).tex(0,0).endVertex();
        buffer.pos(-1, 0, -(double) 8 / 16).tex(1,0).endVertex();
        buffer.pos(-1, -1, -(double) 8 / 16).tex(1,1).endVertex();
        buffer.pos(-1, -1, -(double) 12 / 16).tex(0,1).endVertex();

        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
//        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void renderDestoy(ResourceLocation res, double x, double y, double z) {
        GlStateManager.matrixMode(1890);
        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enablePolygonOffset();
        GlStateManager.matrixMode(1888);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(x + 0.5, y + ((double) 15.9 / 16), z + 0.5);
        GlStateManager.scale((double) 1.22 / 16, (double) 1.22 / 16, (double) 1.22 / 16);
        GlStateManager.doPolygonOffset(-1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
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
        buffer.pos(-0.901, -0.026, 1).tex(16,0).endVertex();
        buffer.pos(-0.901, -2.026, 1).tex(16,16).endVertex();
        buffer.pos(-0.901, -2.026, -1).tex(0,16).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
//        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableRescaleNormal();
        GlStateManager.matrixMode(1890);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(1888);
    }


    private void renderOpenDestoy(ResourceLocation res, double x, double y, double z) {
        GlStateManager.matrixMode(1890);
        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enablePolygonOffset();
        GlStateManager.matrixMode(1888);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate(x + 0.5, y + ((double) 15.9 / 16), z + 0.5);
//        GlStateManager.scale((double) 7.22 / 16 + (0.5), (double) 7.22 / 16 + (0.5), (double) 7.22 / 16 + (0.5));
        GlStateManager.scale((double) 7.22 / 16, (double) 7.22 / 16, (double) 7.22 / 16);
        GlStateManager.doPolygonOffset(-1, 0);
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
//        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableRescaleNormal();
        GlStateManager.matrixMode(1890);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(1888);
    }
}
