package com.wdcftgg.carpentersblocks.carpentersblocks.item;

import com.wdcftgg.carpentersblocks.carpentersblocks.api.ICarpentersHammer;
import com.wdcftgg.carpentersblocks.carpentersblocks.block.BlockCoverable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCarpentersHammer extends Item implements ICarpentersHammer {

    public ItemCarpentersHammer() {}
    
    @Override
    public void onHammerUse(World world, EntityPlayer entityPlayer, EnumHand hand) {
        entityPlayer.getHeldItem(hand).damageItem(1, entityPlayer);
    }

    @Override
    public boolean canUseHammer(World world, EntityPlayer entityPlayer, EnumHand hand) {
        return true;
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState blockState) {
    	return blockState.getBlock() instanceof BlockCoverable;
    }

}