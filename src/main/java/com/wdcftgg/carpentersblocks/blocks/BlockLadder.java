package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityLadder;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLadder extends net.minecraft.block.BlockLadder implements IHasModel, ITileEntityProvider {

    public BlockLadder() {
        super();
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("ladder");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".ladder");


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing facing = state.getValue(BlockLadder.FACING);

        IBlockState nearBlockState = source.getBlockState(pos.offset(facing.getOpposite()));
        Block nearBlock = nearBlockState.getBlock();

        if (nearBlock.equals(Blocks.AIR) || !nearBlockState.isFullBlock()) {
            switch ((EnumFacing)state.getValue(FACING))
            {
                case NORTH:
                case SOUTH:
                    return new AxisAlignedBB(0.0D, 0.0D, (float) 6 / 16, 1.0D, 1.0D, (float) 10 / 16);
                case WEST:
                case EAST:
                    return new AxisAlignedBB((float) 6 / 16, 0.0D, 0, (float) 10 / 16, 1.0D, 1.0D);
                default:
                    return LADDER_EAST_AABB;
            }
        }

        switch ((EnumFacing)state.getValue(FACING))
        {
            case NORTH:
                return LADDER_NORTH_AABB;
            case SOUTH:
                return LADDER_SOUTH_AABB;
            case WEST:
                return LADDER_WEST_AABB;
            case EAST:
            default:
                return LADDER_EAST_AABB;
        }
    }

    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityLadder();
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {

    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (facing.getAxis().isHorizontal() && this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing))
        {
            return this.getDefaultState().withProperty(FACING, facing);
        }
        else
        {
//            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
//            {
//                if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing))
//                {
                    return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
//                }
//            }
//
//
//
//            return this.getDefaultState();
        }
    }

    private boolean canAttachTo(World p_193392_1_, BlockPos p_193392_2_, EnumFacing p_193392_3_)
    {
        IBlockState iblockstate = p_193392_1_.getBlockState(p_193392_2_);
        boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
        return !flag && iblockstate.getBlockFaceShape(p_193392_1_, p_193392_2_, p_193392_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityLadder te = (TileEntityLadder) worldIn.getTileEntity(pos);
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

            ItemStack held = playerIn.getHeldItem(hand);
            if (held.isEmpty() || held.getItem() != Item.getItemFromBlock(this)) {
                return false;
            }

            BlockPos.MutableBlockPos searchPos = new BlockPos.MutableBlockPos(pos);

            for (int i = 1; i <= 256; i++) {
                searchPos.setPos(pos.getX(), pos.getY() + i, pos.getZ());
                IBlockState aboveState = worldIn.getBlockState(searchPos);

                if (aboveState.getBlock() == this) {
                    continue;
                } else if (worldIn.isAirBlock(searchPos)) {
                    if (!worldIn.isRemote) {

                        IBlockState newState = this.getDefaultState()
                                .withProperty(FACING, state.getValue(FACING));
                        worldIn.setBlockState(searchPos.toImmutable(), newState, 3);


                        if (!playerIn.isCreative()) {
                            held.shrink(1);
                        }
                    }
                    return true;
                } else {

                    break;
                }
            }

        }


        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }
}
