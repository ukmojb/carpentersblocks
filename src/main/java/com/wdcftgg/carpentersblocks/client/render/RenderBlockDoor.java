package com.wdcftgg.carpentersblocks.client.render;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.BlockButton;
import com.wdcftgg.carpentersblocks.blocks.BlockDoor;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityDoor;
import com.wdcftgg.carpentersblocks.client.model.door.ModelDoorBase;
import com.wdcftgg.carpentersblocks.client.model.door.ModelDoorDown;
import com.wdcftgg.carpentersblocks.client.model.door.ModelDoorUp;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderBlockDoor extends TileEntitySpecialRenderer<TileEntityDoor>
{
    private static final ResourceLocation UPTEXTURES = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/door_up.png");
    private static final ResourceLocation DOWNTEXTURES = new ResourceLocation(CarpentersBlocks.MODID + ":textures/model/door_down.png");
    private ModelDoorUp modelDoorUp = new ModelDoorUp();
    private ModelDoorDown modelDoorDown = new ModelDoorDown();
    public RenderBlockDoor(){
    }

    @Override
    public boolean isGlobalRenderer(TileEntityDoor te)
    {
        return true;
    }


    public void render(TileEntityDoor te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        World world = te.getWorld();
        IBlockState bed = world.getBlockState(te.getPos());
        BlockDoor.EnumDoorHalf HALF = world.getBlockState(te.getPos()).getValue(BlockDoor.HALF);

        boolean flag = te.getWorld() != null;
        BlockDoor.EnumHingePosition hinge = world.getBlockState(te.getPos()).getValue(BlockDoor.HINGE);
//        EnumDyeColor enumdyecolor = te != null ? te.getColor() : EnumDyeColor.RED;
        int face = flag ? te.getBlockMetadata() & 3 : 0;
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

        if (HALF != BlockDoor.EnumDoorHalf.LOWER) return;

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();

        int j = 0;
        float f1 = 0.0F;
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        if (i == 0)
        {
            j = 180;
            f1 = -(float) 13 / 16;
            f2 = 0;
            f3 = -((float) 13 / 16) - 1;
            f4 = -1;
        }

        if (i == 1)
        {
            j = 180;
            f1 = 1;
            f2 = 1;
            f3 = -1;
            f4 = -((float) 13 / 16) - 1;
        }


        if (i == 2)
        {
            j = 180;
            f1 = 1;
            f2 = 1;
            f3 = -(float) 3 / 16;
            f4 = -1;
        }

        if (i == 3)
        {
            j = 90;
            f1 = 1;
            f2 = 1;
            f3 = -1;
            f4 = -(float) 3 / 16;
        }

        if (i == 4)
        {
            j = 180;
            f1 = 0;
            f2 = ((float) 13 / 16) + 1;
            f3 = 0;
            f4 = 1;
        }

        if (i == 5)
        {
            j = 180;
            f1 = -(float) 13 / 16;
            f2 = 1;
            f3 = -1;
            f4 = -2 - (float) 10 / 16;
        }

        if (i == 6)
        {
            j = 90;
            f1 = (float) 0 / 16;
            f2 = (float) 3 / 16;
            f3 = (float) 10 / 16;
            f4 = -1;
        }

        if (i == 7)
        {
            j = 90;
            f1 = (float) 13 / 16;
            f2 = 1;
        }

        GlStateManager.translate(x, y, z);
        GlStateManager.translate((float)f1, (float)0, (float)f2);
        if (i == 3 || i == 2 || i == 1) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
        }
        if (i == 7 || i == 4 || i == 6 || i == 5) {
            GlStateManager.rotate((float)90, 0.0F, 1.0F, 0.0F);
        }

        if (hinge == BlockDoor.EnumHingePosition.LEFT && (i == 4)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-(float) 13 / 16, (float)0, (float)1);
        }

        if (hinge == BlockDoor.EnumHingePosition.LEFT && (i == 7)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0, (float)0, (float) 13 / 16);
        }

        if (hinge == BlockDoor.EnumHingePosition.LEFT && (i == 5)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-1, (float)0, (float) -3 - (float) 7 / 16);
        }

        if (hinge == BlockDoor.EnumHingePosition.LEFT && (i == 6)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(1 + (float) 7 / 16, (float)0, (float) -1);
        }

        if (hinge == BlockDoor.EnumHingePosition.LEFT && (i != 0)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate((float)f3, (float)0, (float)f4);
        }

        if (hinge == BlockDoor.EnumHingePosition.RIGHT && (i == 0)) {
            GlStateManager.rotate((float)180, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate((float)f3, (float)0, (float)f4);
        }

        this.renderPiece(x, y, z, face, alpha, te, modelDoorUp, modelDoorDown);

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

    private void renderPiece(double x, double y, double z, int face, float alpha, TileEntityDoor te, ModelDoorUp modelDoorUp, ModelDoorDown modelDoorDown)
    {
//        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;

//        GlStateManager.translate(x, y, z);

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
        renderDoor(modelDoorDown, te, DOWNTEXTURES);
        GlStateManager.translate(0, -1, 0);
        renderDoor(modelDoorUp, te, UPTEXTURES);

        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.popMatrix();
    }

    private void renderDoor(ModelDoorBase modelDoorBase, TileEntityDoor te, ResourceLocation resourceLocation) {
        renderOther(modelDoorBase, te, resourceLocation);
        renderWood(modelDoorBase, te, resourceLocation);
//        modelBedBase.render(Minecraft.getMinecraft().player, 0, 0, 0, 0, 0, 0.0625F);
    }

    private void renderOther(ModelDoorBase modelDoorBase, TileEntityDoor te, ResourceLocation resourceLocation) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        this.bindTexture(resourceLocation);
        ResourceLocation res;
        IBlockState iBlockState = te.getiBlockState();

        if (!te.hasBlock()) {
            res = null;
        } else {
            res = getResFromTexture(iBlockState);
        }

        if (res != null) {
            this.bindTexture(res);
        }
        modelDoorBase.renderOther(0.0625F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    private void renderWood(ModelDoorBase modelDoorBase, TileEntityDoor te, ResourceLocation resourceLocation) {

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        this.bindTexture(resourceLocation);

        modelDoorBase.renderWood(0.0625F);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }

}