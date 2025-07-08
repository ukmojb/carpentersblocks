package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockBed;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityBed;
import com.wdcftgg.carpentersblocks.client.model.bed.ModelBedBase;
import com.wdcftgg.carpentersblocks.client.model.bed.a.ModelBedA1;
import com.wdcftgg.carpentersblocks.client.model.bed.a.ModelBedA2;
import com.wdcftgg.carpentersblocks.client.model.bed.b.ModelBedB1;
import com.wdcftgg.carpentersblocks.client.model.bed.b.ModelBedB2;
import com.wdcftgg.carpentersblocks.client.model.bed.c.ModelBedC1;
import com.wdcftgg.carpentersblocks.client.model.bed.c.ModelBedC2;
import com.wdcftgg.carpentersblocks.client.model.bed.d.ModelBedD1;
import com.wdcftgg.carpentersblocks.client.model.bed.d.ModelBedD2;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RenderBlockBed extends TileEntitySpecialRenderer<TileEntityBed>
{
    private static final List<ModelBedBase> HEADS;
    private static final List<ModelBedBase> FOOTS;
    private static final ResourceLocation HEADSTEXTURES = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/bed/bed1.png");
    private static final ResourceLocation FOOTSTEXTURES = new ResourceLocation(CarpentersBlocks.MODID + ":textures/blocks/bed/bed2.png");
    private ModelBedBase model;
    public RenderBlockBed(){
    }

    @Override
    public boolean isGlobalRenderer(TileEntityBed te)
    {
        return true;
    }

    public void render(TileEntityBed te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        World world = te.getWorld();
        IBlockState bed = world.getBlockState(te.getPos());
        BlockBed.EnumPartType OCCUPIED = world.getBlockState(te.getPos()).getValue(BlockBed.PART);

        boolean flag = te.getWorld() != null;
        boolean flag1 = flag ? te.isHeadPiece() : true;
//        EnumDyeColor enumdyecolor = te != null ? te.getColor() : EnumDyeColor.RED;
        int face = flag ? te.getBlockMetadata() & 3 : 0;
        int mode = 0;

        boolean checkX = false;
        boolean checkZ = false;

        if (face == EnumFacing.NORTH.getHorizontalIndex() || face == EnumFacing.SOUTH.getHorizontalIndex())
        {
            checkX = true;
        }
        else if (face == EnumFacing.WEST.getHorizontalIndex() || face == EnumFacing.EAST.getHorizontalIndex())
        {
            checkZ = true;
        }

        if (checkX) {
            BlockPos pos = te.getPos();
            BlockPos posE = pos.east();
            BlockPos posW = pos.west();

            IBlockState blockE = world.getBlockState(posE);
            IBlockState blockW = world.getBlockState(posW);

            if (isSameBed(blockE, bed)) {
                mode = 1;
            }

            if (isSameBed(blockW, bed)) {
                mode = mode != 0 ? 3 : 2;
            }
        }

        if (checkZ) {
            BlockPos pos = new BlockPos(x, y, z);
            BlockPos posN = pos.north();
            BlockPos posS = pos.south();

            IBlockState blockN = world.getBlockState(posN);
            IBlockState blockS = world.getBlockState(posS);

            if (isSameBed(blockN, bed)) {
                mode = 1;
            }

            if (isSameBed(blockS, bed)) {
                mode = 2;
            }
            if (isSameBed(blockS, bed) && isSameBed(blockN, bed)) {
                mode = 3;
            }
        }

        switch (OCCUPIED) {
            case FOOT:
                this.model = FOOTS.get(mode);
                break;
            case HEAD:
                this.model = HEADS.get(mode);
                break;
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
        else
        {
            ResourceLocation resourcelocation = getBedRes(OCCUPIED == BlockBed.EnumPartType.HEAD);

            if (resourcelocation != null)
            {
                this.bindTexture(resourcelocation);
            }
        }

        if (flag)
        {
            this.renderPiece(x, y, z, face, alpha, te);
        }
        else
        {
            GlStateManager.pushMatrix();
            this.renderPiece(x, y, z, face, alpha, te);
            this.renderPiece(x, y, z - 1.0D, face, alpha, te);
            GlStateManager.popMatrix();
        }

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
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

            return new ResourceLocation(prefix + ":textures/" + suffix + ".png");
        } catch (ArrayIndexOutOfBoundsException a){
            return null;
        }
    }

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntityBed te)
    {
//        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

        GlStateManager.translate(x, y, z);

        if (face == EnumFacing.NORTH.getHorizontalIndex())
        {
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            f1 = -0.5F;
            f2 = 0.5F;
        }
        else if (face == EnumFacing.SOUTH.getHorizontalIndex())
        {
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            f1 = 0.5F;
            f2 = -0.5F;
        }
        else if (face == EnumFacing.WEST.getHorizontalIndex())
        {
            GlStateManager.rotate(180, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = 0.5F;
            f2 = 0.5F;
        }
        else if (face == EnumFacing.EAST.getHorizontalIndex())
        {
            GlStateManager.rotate(180, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
            f1 = -0.5F;
            f2 = -0.5F;
        }



        GlStateManager.translate((float)f1, (float)-1.5, (float)f2);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();


        renderBed(this.model, te);


        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();
    }

    private void renderBed(ModelBedBase modelBedBase, TileEntityBed te) {
        renderWool(modelBedBase, te);
        renderWood(modelBedBase, te);
//        modelBedBase.render(Minecraft.getMinecraft().player, 0, 0, 0, 0, 0, 0.0625F);
    }

    private void renderWool(ModelBedBase modelBedBase, TileEntityBed te) {
        if (te.getColor() != EnumDyeColor.WHITE) {
            EnumDyeColor color = te.getColor();

            GlStateManager.color(color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2], 1);
        }
        modelBedBase.renderWooL(0.0625F);
        GlStateManager.color(1F, 1F, 1F, 1);
    }

    private void renderWood(ModelBedBase modelBedBase, TileEntityBed te) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        IBlockState iBlockState = te.getiBlockState();
        ResourceLocation res;

        if (!te.hasBlock()) {
            res = null;
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelBedBase.renderWood(0.0625F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }

    private boolean isSameBed(IBlockState nearBlockState, IBlockState bed) {

        if (nearBlockState.getBlock() == bed.getBlock()) {
            BlockBed.EnumPartType OCCUPIED1 = nearBlockState.getValue(BlockBed.PART);
            BlockBed.EnumPartType OCCUPIED2 = bed.getValue(BlockBed.PART);

            return Objects.equals(OCCUPIED1.toString(), OCCUPIED2.toString());
        }

        return false;
    }

    private ResourceLocation getBedRes(boolean isHead) {
        return isHead ? HEADSTEXTURES : FOOTSTEXTURES;
    }

    static
    {
        HEADS = new ArrayList<>();
        FOOTS = new ArrayList<>();

        HEADS.add(new ModelBedA1());
        HEADS.add(new ModelBedB1());
        HEADS.add(new ModelBedC1());
        HEADS.add(new ModelBedD1());

        FOOTS.add(new ModelBedA2());
        FOOTS.add(new ModelBedB2());
        FOOTS.add(new ModelBedC2());
        FOOTS.add(new ModelBedD2());

    }
}