package com.wdcftgg.somecarpentersblocks.blocks.te;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.ModBlocks;
import com.wdcftgg.somecarpentersblocks.network.MessageGarageDoor;
import com.wdcftgg.somecarpentersblocks.network.MessageSafe;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import static com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoor.FACING;

public class TileEntityGarageDoor extends TileEntity implements ITickable {

    private int block = 0;
    private int meta = 0;

    public TileEntityGarageDoor() {
        super();
    }

    @Override
    public void update() {

        PacketHandler.INSTANCE.sendToAllAround(new MessageGarageDoor(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
        
        if (!world.isRemote) {
            IBlockState blockState = world.getBlockState(this.pos);
            BlockGarageDoor block = (BlockGarageDoor) world.getBlockState(this.pos).getBlock();
            World worldIn = this.world;
//            Block target = blockState.getValue(BlockGarageDoor.OPEN) ? ModBlocks.GarageDoorBlock : Blocks.AIR;

            int i = 0;
            while (true) {
                i++;

                if (blockState.getValue(BlockGarageDoor.OPEN)) {
                    boolean pass = worldIn.getBlockState(pos.down(i)).getBlock() == ModBlocks.GarageDoorBlock;
                    if (pass) {
                        worldIn.setBlockToAir(pos.down(i));
                    } else break;

                } else {
                    boolean pass = worldIn.getBlockState(pos.down(i)).getBlock() == Blocks.AIR || worldIn.getBlockState(pos.down(i)).getBlock() == ModBlocks.GarageDoorBlock;
                    if (pass) {
                        if (worldIn.getBlockState(pos.down(i)).getBlock() == ModBlocks.GarageDoor) continue;
                        worldIn.setBlockState(pos.down(i), ModBlocks.GarageDoorBlock.getDefaultState().withProperty(FACING, blockState.getValue(FACING)));
                        TileEntityGarageDoorBlock te = (TileEntityGarageDoorBlock) worldIn.getTileEntity(pos.down(i));
                        if (te != null) te.setBlockAndMeta(this.getBlock(), this.getMeta());
                    } else {
                        break;
                    }
                }

                if (i >= 50) break;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        block = compound.getInteger("block");
        meta = compound.getInteger("meta");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("block", block);
        compound.setInteger("meta", meta);
        return super.writeToNBT(compound);
    }

    public IBlockState getiBlockState() {
        return Block.getBlockById(block).getStateFromMeta(meta);
    }

    public void setBlockAndMeta(int block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public boolean hasBlock(){
        return block != 0;
    }

    public int getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public boolean isOpen() {
        IBlockState blockState = world.getBlockState(this.pos);
        return blockState.getValue(BlockGarageDoor.OPEN);
    }
}
