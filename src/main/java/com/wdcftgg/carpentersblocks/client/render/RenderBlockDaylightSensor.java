package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockTorch;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityDaylightSensor;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityDaylightSensor;
import com.wdcftgg.carpentersblocks.client.model.ModelDaylightSensor;
import com.wdcftgg.carpentersblocks.client.model.torch.ModelTorchGround;
import com.wdcftgg.carpentersblocks.client.model.torch.ModelTorchWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderBlockDaylightSensor extends TileEntitySpecialRenderer<TileEntityDaylightSensor> {

    private ModelDaylightSensor model = new ModelDaylightSensor();


    @Override
    public void render(TileEntityDaylightSensor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

        World world = te.getWorld();
        IBlockState bed = world.getBlockState(te.getPos());

        boolean flag = te.getWorld() != null;
//        EnumDyeColor enumdyecolor = te != null ? te.getColor() : EnumDyeColor.RED;
        int face = flag ? te.getBlockMetadata() & 3 : 0;

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
//            ResourceLocation resourcelocation = getBedRes(OCCUPIED == BlockBed.EnumPartType.HEAD);
//
//            if (resourcelocation != null)
//            {
//                this.bindTexture(resourcelocation);
//            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();


        this.renderPiece(x, y, z, face, alpha, te);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();

    }

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntityDaylightSensor te)
    {
//        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.5, 0, -0.5);

//        if (face == EnumFacing.NORTH.getHorizontalIndex())
//        {
//            //weat
////            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
////            f1 = -0.5F;
////            f2 = 0.5F;
//
//            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
//            f1 = 0.5F;
//            f2 = -0.5F;
//        }
//        else if (face == EnumFacing.SOUTH.getHorizontalIndex())
//        {
//            //north
//            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
//            f1 = 0.5F;
//            f2 = 0.5F;
//        }
//        else if (face == EnumFacing.WEST.getHorizontalIndex())
//        {
//            //east
//            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
//            f1 = -0.5F;
//            f2 = 0.5F;
//        }
//        else if (face == EnumFacing.EAST.getHorizontalIndex())
//        {
//            //south
//            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
//            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
//            f1 = -0.5F;
//            f2 = -0.5F;
//        }



        GlStateManager.translate((float)f1, (float)-1.5, (float)f2);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();



        renderTorch(this.model, te);


        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();
    }

    private void renderTorch(ModelDaylightSensor modelBase, TileEntityDaylightSensor te) {
        renderOther(modelBase, te);
        renderWood(modelBase, te);
    }

    private void renderOther(ModelDaylightSensor modelBase, TileEntityDaylightSensor te) {
        ResourceLocation res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/daylight_sensor.png");
        this.bindTexture(res);
        modelBase.renderOther(0.0625F);
    }

    private void renderWood(ModelDaylightSensor modelBase, TileEntityDaylightSensor te) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        IBlockState iBlockState = te.getiBlockState();
        ResourceLocation res;

        if (!te.hasBlock()) {
            res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/daylight_sensor.png");
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelBase.renderWood(0.0625F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

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