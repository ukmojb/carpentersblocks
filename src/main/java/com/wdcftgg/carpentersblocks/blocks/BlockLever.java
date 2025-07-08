package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityButton;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityLadder;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityLever;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLever extends net.minecraft.block.BlockLever implements IHasModel, ITileEntityProvider {

    public BlockLever() {
        super();
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("lever");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".lever");


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLever();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityLever te = (TileEntityLever) worldIn.getTileEntity(pos);
        if (te != null) {
            if (!te.hasBlock()) {
                ItemStack itemStack = playerIn.getHeldItem(hand);
                if (itemStack.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                    Block block = itemBlock.getBlock();
                    int meta = itemStack.getMetadata();

                    if (block != this) {
                        itemStack.shrink(1);
                        te.setBlockAndMeta(block.getRegistryName().toString(), meta);
                        playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                        return true;
                    }
                }
            }
        }

        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
//            state = state.cycleProperty(POWERED);
//            worldIn.setBlockState(pos, state, 3);
            cycle(worldIn, pos, state);
            float f = ((Boolean)state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F;
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
            EnumFacing enumfacing = state.getValue(FACING).getFacing();
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this, false);
            return true;
        }
    }

    private void cycle(World world, BlockPos pos, IBlockState state) {
        TileEntityLever te = (TileEntityLever) world.getTileEntity(pos);
        String block = "";
        int meta = 0;
        if (te != null) {
            if (te.hasBlock()) {
                block = te.getBlock();
                meta = te.getMeta();
            }
            IBlockState newState = state.cycleProperty(POWERED);
            world.setBlockState(pos, newState, 2);
            TileEntityLever newte = (TileEntityLever) world.getTileEntity(pos);
            if (newte != null) {
                newte.setBlockAndMeta(block, meta);
            }
        }
    }
}
