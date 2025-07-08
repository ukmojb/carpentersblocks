package com.wdcftgg.carpentersblocks.blocks.te;

import com.wdcftgg.carpentersblocks.network.MessageCustomRenderBase;
import com.wdcftgg.carpentersblocks.network.MessagePotDirt;
import com.wdcftgg.carpentersblocks.network.PacketHandler;
import com.wdcftgg.carpentersblocks.util.TagTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.HashMap;
import java.util.Map;

public class TileEntityFlowerPot extends TileEntity implements ICustomRenderBase, ITickable {

    private String block = "";
    private int meta = 0;
    private boolean hasSoil = false;
    private ItemStack itemStack = ItemStack.EMPTY;
    public static Map<String, Integer> plantList = new HashMap<>();

    @Override
    public void update() {
        PacketHandler.INSTANCE.sendToAllAround(new MessageCustomRenderBase(block, meta, this.pos), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
        PacketHandler.INSTANCE.sendToAllAround(new MessagePotDirt(block, meta, this.pos, hasSoil, itemStack), new NetworkRegistry.TargetPoint(this.world.provider.getDimension(), (double)this.pos.getX(), (double)this.pos.getY(), (double)this.pos.getZ(), 256.0D));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("block", block);
        compound.setInteger("meta", meta);
        compound.setBoolean("hasSoil", hasSoil);
        compound.setTag("itemStack", TagTools.saveItemStackToNBT(itemStack));
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        block = compound.getString("block");
        meta = compound.getInteger("meta");
        hasSoil = compound.getBoolean("hasSoil");
        itemStack = TagTools.loadItemStackFromNBT(compound, "itemStack");
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

    public boolean hasSoil() {
        return hasSoil;
    }

    public void setHasSoil(boolean hasSoil) {
        this.hasSoil = hasSoil;
    }

    public ItemStack getPlant() {
        return itemStack;
    }
    public boolean hasPlant() {
        return itemStack != ItemStack.EMPTY;
    }

    public void setPlant(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    static {
        for (int i = 0; i < 6; i++) {
            plantList.put("minecraft:sapling", i);
        }
        for (int i = 0; i < 4; i++) {
            plantList.put("minecraft:leaves", i);
        }
        for (int i = 1; i < 3; i++) {
            plantList.put("minecraft:tallgrass", i);
        }
        for (int i = 0; i < 9; i++) {
            plantList.put("minecraft:red_flower", i);
        }
        for (int i = 0; i < 6; i++) {
            plantList.put("minecraft:double_plant", i);
        }
        plantList.put("minecraft:leaves2", 1);
        plantList.put("minecraft:yellow_flower", 0);
        plantList.put("minecraft:red_mushroom", 0);
        plantList.put("minecraft:brown_mushroom", 0);
        plantList.put("minecraft:vine", 0);
        plantList.put("minecraft:waterlily", 0);
        plantList.put("minecraft:cactus", 0);
    }
}
