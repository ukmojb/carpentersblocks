package com.wdcftgg.somecarpentersblocks.blocks.te;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.ModBlocks;
import com.wdcftgg.somecarpentersblocks.network.MessageGarageDoor;
import com.wdcftgg.somecarpentersblocks.network.MessageSafe;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoor.FACING;
import static com.wdcftgg.somecarpentersblocks.blocks.BlockGarageDoor.OPEN;

public class TileEntityGarageDoor extends TileEntity implements ITickable {

    private Integer time = 0;
    private String block = "";
    private int meta = 0;
    private Map<Integer, String> blockmap = new HashMap<>();
    private Map<Integer, Integer> matemap = new HashMap<>();

    public TileEntityGarageDoor() {
        super();
    }

    @Override
    public void update() {

        PacketHandler.INSTANCE.sendToAllAround(new MessageGarageDoor(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
        
        if (!world.isRemote) {

            if (time >= 0) {
                setTime(time - 1);
            }

//            System.out.println(blockmap.toString());
//            System.out.println(matemap.toString());
            IBlockState blockState = world.getBlockState(this.pos);
            BlockGarageDoor block = (BlockGarageDoor) world.getBlockState(this.pos).getBlock();
            World worldIn = this.world;
//            Block target = blockState.getValue(BlockGarageDoor.OPEN) ? ModBlocks.GarageDoorBlock : Blocks.AIR;

            if (time == 0) {
                TileEntityGarageDoor te = (TileEntityGarageDoor) worldIn.getTileEntity(pos);


                int v = 0;
                int power = 0;
                while (true) {
                    v++;

                    power = Math.max(worldIn.getRedstonePowerFromNeighbors(pos.down(v)), power);

                    if (v >= 50) break;
                }


                if (power == 0) {
                    block.cycleProperty(OPEN, blockState, pos, world);
                } else {
                    setTime(30);
                }
            }


            int i = 0;
            while (true) {
                i++;

                if (blockState.getValue(OPEN)) {
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
                        if (te != null) {
                            if (blockmap.get(i) != null && matemap.get(i) != null) {
//                                System.out.println(i + "-" + blockmap.get(i) + "--" + matemap.get(i));
                                te.setBlockAndMeta(blockmap.get(i), matemap.get(i));
                            }
                        }

                    } else {
                        break;
                    }
                }

                if (i >= 50) break;
            }
        }
    }

    public void addBlock(String block, Integer mate, Integer num) {
//        System.out.println("addBlock");
        blockmap.put(num, block);
        matemap.put(num, mate);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        time = compound.getInteger("time");
        block = compound.getString("block");
        meta = compound.getInteger("meta");
        NBTTagList list = compound.getTagList("mapInt", Constants.NBT.TAG_COMPOUND);
        NBTTagList list2 = compound.getTagList("mapBlock", Constants.NBT.TAG_COMPOUND);
        NBTTagList list3 = compound.getTagList("mapMate", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound intTag = list.getCompoundTagAt(i);
            NBTTagCompound blockTag = list2.getCompoundTagAt(i);
            NBTTagCompound mateTag = list3.getCompoundTagAt(i);
            blockmap.put(intTag.getInteger("int"), blockTag.getString("block"));
            matemap.put(intTag.getInteger("int"), mateTag.getInteger("mate"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        compound.setInteger("time", time);
        compound.setString("block", block);
        compound.setInteger("meta", meta);

        NBTTagList list = new NBTTagList();
        NBTTagList list2 = new NBTTagList();
        NBTTagList list3 = new NBTTagList();
        for (int i = 0; i < blockmap.size(); i++) {
            NBTTagCompound intTag = new NBTTagCompound();
            intTag.setInteger("int", (Integer) blockmap.keySet().toArray()[i]);
            list.appendTag(intTag);

            NBTTagCompound blockTag = new NBTTagCompound();
            blockTag.setString("block", (String) blockmap.values().toArray()[i]);
            list2.appendTag(blockTag);

            NBTTagCompound mateTag = new NBTTagCompound();
            mateTag.setInteger("mate", (Integer) matemap.values().toArray()[i]);
            list3.appendTag(mateTag);
        }
        compound.setTag("mapInt", list);
        compound.setTag("mapBlock", list2);
        compound.setTag("mapMate", list3);
        return super.writeToNBT(compound);
    }

    public IBlockState getiBlockState() {
        if (block == "") {
            return Blocks.AIR.getDefaultState();
        }
        return Block.getBlockFromName(block).getStateFromMeta(meta);
//        return Block.getBlockById(block).getStateFromMeta(meta);
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

    public boolean isOpen() {
        IBlockState blockState = world.getBlockState(this.pos);
        return blockState.getValue(OPEN);
    }

    public Map<Integer, String> getBlockmap() {
        return blockmap;
    }

    public Map<Integer, Integer> getMatemap() {
        return matemap;
    }

    public void setTwoMap(Map<Integer, String> blockmap, Map<Integer, Integer> matemap) {
        this.blockmap = blockmap;
        this.matemap = matemap;
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
    {
        if (p_185731_4_)
        {
            int i =  1007;
            worldIn.playEvent(player, i, pos, 0);
        }
        else
        {
            int j =  1013;
            worldIn.playEvent(player, j, pos, 0);
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
