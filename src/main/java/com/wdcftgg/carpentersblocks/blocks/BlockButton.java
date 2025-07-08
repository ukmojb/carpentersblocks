package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityButton;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockButton extends net.minecraft.block.BlockButton implements IHasModel, ITileEntityProvider {
    protected BlockButton() {
        super(true);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("button");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".button");


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos)
    {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    @Override
    protected void playReleaseSound(World worldIn, BlockPos pos)
    {
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityButton te = (TileEntityButton) worldIn.getTileEntity(pos);
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
//            if (!te.hasColor()) {
//                ItemStack itemStack = playerIn.getHeldItem(hand);
//                if (DyeUtils.isDye(itemStack)) {
//
//                    float[] afloat = DyeUtils.colorFromStack(itemStack).get().getColorComponentValues();
//                    int r = (int)(afloat[0] * 255.0F);
//                    int g = (int)(afloat[1] * 255.0F);
//                    int b = (int)(afloat[2] * 255.0F);
//
//                    Color color = new Color(r, g, b);
//                    itemStack.shrink(1);
//                    playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
//                    return true;
//                }
//            }
        }


        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            return true;
        }
        else
        {
            cycle(true, worldIn, pos, state);
//            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            this.playClickSound(playerIn, worldIn, pos);
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            return true;
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (((Boolean)state.getValue(POWERED)).booleanValue())
            {
                this.checkPressed(state, worldIn, pos);
            }
        }
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (!((Boolean)state.getValue(POWERED)).booleanValue())
            {
                this.checkPressed(state, worldIn, pos);
            }

        }
    }

    private void checkPressed(IBlockState state, World worldIn, BlockPos pos)
    {
        List<? extends Entity> list = worldIn.<Entity>getEntitiesWithinAABB(EntityArrow.class, state.getBoundingBox(worldIn, pos).offset(pos));
        boolean flag = !list.isEmpty();
        boolean flag1 = ((Boolean)state.getValue(POWERED)).booleanValue();

        if (flag && !flag1)
        {
            cycle(true, worldIn, pos, state);
//            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            this.playClickSound((EntityPlayer)null, worldIn, pos);
        }

        if (!flag && flag1)
        {
            cycle(false, worldIn, pos, state);
//            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
            this.notifyNeighbors(worldIn, pos, (EnumFacing)state.getValue(FACING));
            worldIn.markBlockRangeForRenderUpdate(pos, pos);
            this.playReleaseSound(worldIn, pos);
        }

        if (flag)
        {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }

    private void cycle(boolean power, World world, BlockPos pos, IBlockState state) {
        TileEntityButton te = (TileEntityButton) world.getTileEntity(pos);
        String block = "";
        int meta = 0;
        int rgb = 0;
        if (te != null) {
            if (te.hasBlock()) {
                block = te.getBlock();
                meta = te.getMeta();
            }
            IBlockState newState = state.withProperty(POWERED, power);
            state = newState;
            world.setBlockState(pos, state, 3);
            TileEntityButton newte = (TileEntityButton) world.getTileEntity(pos);
            if (newte != null) {
                newte.setBlockAndMeta(block, meta);
            }
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing)
    {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        TileEntityButton te = (TileEntityButton) worldIn.getTileEntity(pos);
        if (te != null && !worldIn.isRemote && te.hasBlock()) {
            if (te.hasBlock())
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Objects.requireNonNull(Block.getBlockFromName(te.getBlock())), 1, te.getMeta()));
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityButton();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
}
