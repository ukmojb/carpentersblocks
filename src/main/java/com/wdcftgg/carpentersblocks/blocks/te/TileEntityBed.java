package com.wdcftgg.carpentersblocks.blocks.te;

import com.wdcftgg.carpentersblocks.network.MessageCustomRenderBase;
import com.wdcftgg.carpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class TileEntityBed extends net.minecraft.tileentity.TileEntityBed implements ICustomRenderBase, ITickable {

    private String block = "";
    private int meta = 0;
    private EnumDyeColor bedColor = EnumDyeColor.WHITE;

    @Override
    public void update() {
        PacketHandler.INSTANCE.sendToAllAround(new MessageCustomRenderBase(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        block = compound.getString("block");
        meta = compound.getInteger("meta");
        if (compound.hasKey("bedColor"))
        {
            this.bedColor = EnumDyeColor.byMetadata(compound.getInteger("bedColor"));
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("block", block);
        compound.setInteger("meta", meta);
        compound.setInteger("bedColor", this.bedColor.getMetadata());
        return super.writeToNBT(compound);
    }

    public IBlockState getiBlockState() {
        if (block.isEmpty()) {
            return Blocks.AIR.getDefaultState();
        }
        return Block.getBlockFromName(block).getStateFromMeta(meta);
    }

    public void setBlockAndMeta(String block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public boolean hasBlock(){
        return block != "";
    }

    public String getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared ()
    {
        return 65536.0D;
    }

    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }


    @Override
    public EnumDyeColor getColor()
    {
        return this.bedColor;
    }

    @Override
    public void setColor(EnumDyeColor color)
    {
        this.bedColor = color;
        this.markDirty();
    }
}
