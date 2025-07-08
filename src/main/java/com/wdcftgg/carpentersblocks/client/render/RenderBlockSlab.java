package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockSlab;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntitySlab;
import com.wdcftgg.carpentersblocks.client.model.ModelSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderBlockSlab extends TileEntitySpecialRenderer<TileEntitySlab> {

    private ModelSlab model = new ModelSlab();


    @Override
    public void render(TileEntitySlab te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

        World world = te.getWorld();
        IBlockState slab = world.getBlockState(te.getPos());

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

        this.renderPiece(x, y, z, face, alpha, te, slab);

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

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntitySlab te, IBlockState slab)
    {

        BlockSlab.EnumBlockFrostHalf half = slab.getValue(BlockSlab.HALF);

        if (!half.equals(BlockSlab.EnumBlockFrostHalf.FULL)) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.5, -1, 0.5);
            if (half.equals(BlockSlab.EnumBlockFrostHalf.TOP)) GlStateManager.translate(0, 0.5, 0);

            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();

            renderSlab(this.model, te);

            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.5, -1, 0.5);


            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();

            renderSlab(this.model, te);

            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.popMatrix();

            //

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.5, -0.5, 0.5);


            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();

            renderSlab(this.model, te);

            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.popMatrix();

        }

    }

    private void renderSlab(ModelSlab modelBase, TileEntitySlab te) {
        renderWood(modelBase, te);
    }

    private void renderWood(ModelSlab modelSlab, TileEntitySlab te) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        IBlockState iBlockState = te.getiBlockState();
        ResourceLocation res;

        if (!te.hasBlock()) {
            res = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/slab.png");
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelSlab.renderSlab(0.0625F);
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