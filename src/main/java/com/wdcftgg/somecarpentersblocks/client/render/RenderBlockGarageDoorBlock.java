package com.wdcftgg.somecarpentersblocks.client.render;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoorBlock;
import com.wdcftgg.somecarpentersblocks.blocks.BlockSafe;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoorBlock;
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

public class RenderBlockGarageDoorBlock extends TileEntitySpecialRenderer<TileEntityGarageDoorBlock> {
    @Override
    public void render(TileEntityGarageDoorBlock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

//        if (te.hasBlock()) return;

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
               i = te.getBlockMetadata();
            }
        }
        else
        {
            i = 0;
        }



        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }

        if (destroyStage < 0)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        }

        IBlockState iBlockState = te.getiBlockState();
        if (iBlockState != null) {

            ResourceLocation res;
            if (!te.hasBlock()) {
                res = new ResourceLocation(SomeCarpentersBlocks.MODID + ":textures/block/oak_planks.png");
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

                renderFrontBackFace(res, x, y, z, u);
                renderOtherFace(res, x, y, z, u);
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
                resourceLocation = new ResourceLocation(SomeCarpentersBlocks.MODID + ":textures/block/wallvents-ctmh.png");
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

        //后
        buffer.pos(-1, 0, -0.75).tex(0, 0).endVertex(); // 左上
        buffer.pos(-(double) 6 / 16, 0, -0.75).tex((double) 5 / 16, 0).endVertex(); // 左中上
        buffer.pos(-(double) 6 / 16, -2, -0.75).tex((double) 5 / 16, 1).endVertex(); // 左中下
        buffer.pos(-1, -2, -0.75).tex(0, 1).endVertex(); // 左下

        buffer.pos((double) 6 / 16, 0, -0.75).tex((double) 11 / 16, 0).endVertex(); // 右中上
        buffer.pos(1, 0, -0.75).tex(1, 0).endVertex(); // 右上
        buffer.pos(1, -2, -0.75).tex(1, 1).endVertex(); // 右下
        buffer.pos((double) 6 / 16, -2, -0.75).tex((double) 11 / 16, 1).endVertex(); // 右中下

        buffer.pos(-(double) 6 / 16, 0, -0.75).tex((double) 5 / 16, 0).endVertex(); // 中上左
        buffer.pos((double) 6 / 16, 0, -0.75).tex((double) 11 / 16, 0).endVertex(); // 中上右
        buffer.pos((double) 6 / 16, -0.5, -0.75).tex((double) 11 / 16, (double) 4 / 16).endVertex(); // 中中右
        buffer.pos(-(double) 6 / 16, -0.5, -0.75).tex((double) 5 / 16, (double) 4 / 16).endVertex(); // 中中左

//        buffer.pos((double) 6 / 16, -(double) 12 / 16, -0.75).tex((double) 5 / 16, (double) 9 / 16).endVertex(); // 中上左
//        buffer.pos(-(double) 6 / 16, -(double) 12 / 16, -0.75).tex((double) 11 / 16, (double) 9 / 16).endVertex(); // 中上右
//        buffer.pos(-(double) 6 / 16, -2, -0.75).tex((double) 11 / 16, -(double) 1 / 16).endVertex(); // 中中右
//        buffer.pos((double) 6 / 16, -2, -0.75).tex((double) 5 / 16, -(double) 1 / 16).endVertex(); // 中中左

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
}

