package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockButton;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityButton;
import com.wdcftgg.carpentersblocks.client.model.ModelButton;
import com.wdcftgg.carpentersblocks.client.model.ModelButtonPressed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBlockButton extends TileEntitySpecialRenderer<TileEntityButton> {

    private final ModelButton button = new ModelButton();
    private final ModelButtonPressed buttonPressed = new ModelButtonPressed();


    @Override
    public void render(TileEntityButton te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

//        GlStateManager.enableDepth();
//        GlStateManager.depthFunc(515);
//        GlStateManager.depthMask(true);
        int i;

        if (te.hasWorld())
        {
            Block block = te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof BlockButton && i == 0)
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

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }

        ModelBase model = te.isPower() ? this.buttonPressed : this.button;


        IBlockState iBlockState = te.getiBlockState();
        if (iBlockState != null) {

            ResourceLocation res;
            if (!te.hasBlock()) {
                res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/oak_planks.png");
            } else {
                res = getResFromTexture(iBlockState);
            }

            if (res != null) {
                GlStateManager.pushMatrix();
                Minecraft.getMinecraft().getTextureManager().bindTexture(res);

                GlStateManager.popMatrix();

            }
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

        if (i == 0 || i == 8)
        {
            j = 180;
        }

        if (i == 1 || i == 9)
        {
            j = 90;
        }

        if (i == 2 || i == 10)
        {
            j = -90;
        }

        if (i == 3 || i == 11)
        {
            j = 90;
        }

        if (i == 4 || i == 12)
        {
            j = -90;
        }

        if (i == 5 || i == 13)
        {
            j = 180;
        }

        if (i == 5 || i == 13) GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        if (i == 0 || i == 8) GlStateManager.rotate((float)j, 0.0F, 0.0F, 1.0F);
        if (i == 3 || i == 4 || i == 11 || i == 12) GlStateManager.rotate((float)j, 1.0F, 0.0F, 0.0F);
        if (i == 1 || i == 2 || i == 9 || i == 10) {
            GlStateManager.rotate((float)j, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate((float)90, 0, 1.0F, 0.0F);
        }
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);


        GlStateManager.translate(((float) 8 / 16), -((float) 6 / 16), ((float) 8 / 16));
        GlStateManager.scale(0.9F, 0.9F, 0.9F);

        renderButton(model);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    }

    private void renderButton(ModelBase modelBase) {
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

            ResourceLocation resourceLocation = new ResourceLocation(prefix + ":textures/" + suffix + ".png");

            resourceLocation = setModRes(prefix, suffix, resourceLocation);

            return resourceLocation;
        } catch (ArrayIndexOutOfBoundsException a){
            return null;
        }
    }

    public ResourceLocation setModRes(String modid, String name, ResourceLocation resourceLocation) {
//        System.out.println("-" + modid + "-");
//        if (Objects.equals(modid, "chisel")) {
////                System.out.println(name);
//            if (name.contains("wallvents-ctmh")) {
//                resourceLocation = new ResourceLocation(CarpentersBlocks.MODID + ":textures/block/wallvents-ctmh.png");
//            }
//        }

        return resourceLocation;
    }
}

