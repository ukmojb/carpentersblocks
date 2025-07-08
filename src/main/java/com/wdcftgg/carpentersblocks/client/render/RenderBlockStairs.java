package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockStairs;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityStairs;
import com.wdcftgg.carpentersblocks.client.model.stair.ModelStair_1;
import com.wdcftgg.carpentersblocks.client.model.stair.ModelStair_2;
import com.wdcftgg.carpentersblocks.client.model.stair.ModelStair_3;
import com.wdcftgg.carpentersblocks.client.model.stair.ModelStairsBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.block.BlockStairs.EnumShape.INNER_LEFT;

public class RenderBlockStairs extends TileEntitySpecialRenderer<TileEntityStairs> {

    private final ModelStairsBase modelStair_1 = new ModelStair_1();
    private final ModelStairsBase modelStair_2 = new ModelStair_2();
    private final ModelStairsBase modelStair_3 = new ModelStair_3();
    private ModelStairsBase model = null;


    @Override
    public void render(TileEntityStairs te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {

        World world = te.getWorld();

        IBlockState stairs = world.getBlockState(te.getPos());
        BlockStairs blockStairs = (BlockStairs) stairs.getBlock();
        net.minecraft.block.BlockStairs.EnumHalf HALF = world.getBlockState(te.getPos()).getValue(BlockStairs.HALF);
        net.minecraft.block.BlockStairs.EnumShape SHAPE = BlockStairs.getStairsShape(stairs, world, te.getPos());

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

        this.model = getModel(stairs, world, te.getPos());
//        this.model = facing.equals(EnumFacing.UP) ? torchGround : torchWall;

        ResourceLocation res = getRes(stairs, world, te.getPos());

        this.renderPiece(x, y, z, face, alpha, te, res, world);

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

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntityStairs te, ResourceLocation res, World world)
    {
//        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        GlStateManager.translate(x, y, z);

        if (face == EnumFacing.NORTH.getHorizontalIndex())
        {
            //south
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = -0.5F;
            f2 = -0.5F;
        }
        else if (face == EnumFacing.SOUTH.getHorizontalIndex())
        {
            //east
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            f1 = -0.5F;
            f2 = 0.5F;
        }
        else if (face == EnumFacing.WEST.getHorizontalIndex())
        {
            //west
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            f1 = 0.5F;
            f2 = -0.5F;
        }
        else if (face == EnumFacing.EAST.getHorizontalIndex())
        {
            //north
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = 0.5F;
            f2 = 0.5F;
        }



        GlStateManager.translate((float)f1, (float)-1.5, (float)f2);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        IBlockState stairs = world.getBlockState(te.getPos());

        net.minecraft.block.BlockStairs.EnumHalf HALF = world.getBlockState(te.getPos()).getValue(BlockStairs.HALF);
        net.minecraft.block.BlockStairs.EnumShape SHAPE = BlockStairs.getStairsShape(stairs, world, te.getPos());
        EnumFacing FACE = world.getBlockState(te.getPos()).getValue(BlockStairs.FACING);

        if (SHAPE.equals(INNER_LEFT)) {
            GlStateManager.rotate(-90, 0.0F, 1.0F, 0.0F);
        }
        if (HALF.equals(net.minecraft.block.BlockStairs.EnumHalf.TOP)) {
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0, -2, 0);
        }


        if (this.model != null) renderStairs(this.model, te, res);


        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    private void renderStairs(ModelStairsBase modelBase, TileEntityStairs te, ResourceLocation res) {
        renderWood(modelBase, te, res);
    }

    private void renderWood(ModelStairsBase modelStairsBase, TileEntityStairs te, ResourceLocation res1) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        IBlockState iBlockState = te.getiBlockState();
        ResourceLocation res;

        if (!te.hasBlock()) {
            res = res1;
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelStairsBase.renderStairs(0.0625F);
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

    private ModelStairsBase getModel(IBlockState blockState, World world, BlockPos pos) {
        net.minecraft.block.BlockStairs.EnumShape SHAPE = BlockStairs.getStairsShape(blockState, world, pos);

//        System.out.println(SHAPE.getName());
        if (SHAPE != net.minecraft.block.BlockStairs.EnumShape.STRAIGHT) {
            if (SHAPE.getName().contains("inner")) {
                return modelStair_2;
            }
            if (SHAPE.getName().contains("outer")) {
                return modelStair_3;
            }
        } else {
            return modelStair_1;
        }

        return null;
    }

    private ResourceLocation getRes(IBlockState blockState, World world, BlockPos pos) {
        net.minecraft.block.BlockStairs.EnumShape SHAPE = BlockStairs.getStairsShape(blockState, world, pos);

        if (SHAPE != net.minecraft.block.BlockStairs.EnumShape.STRAIGHT) {
            if (SHAPE.getName().contains("inner")) {
                return new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/stairs_2.png");
            }
            if (SHAPE.getName().contains("outer")) {
                return new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/stairs_3.png");
            }
        }

        return new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/stairs_1.png");
    }
}