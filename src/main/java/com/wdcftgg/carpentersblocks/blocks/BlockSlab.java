package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityLadder;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntitySlab;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockSlab extends Block implements IHasModel, ITileEntityProvider {
    public static final PropertyEnum<EnumBlockFrostHalf> HALF = PropertyEnum.create("half", EnumBlockFrostHalf.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockSlab() {
        super(Material.WOOD, MapColor.BLUE);
        this.setDefaultState(blockState.getBaseState().withProperty(HALF, EnumBlockFrostHalf.BOTTOM));
        this.useNeighborBrightness = true;
        this.setCreativeTab(ModCreativeTab.Tab);
        setHardness(1.6F);
        setResistance(6.0F);

        setTranslationKey(CarpentersBlocks.MODID + ".slab");
        setRegistryName("slab");

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));

    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(HALF).equals(EnumBlockFrostHalf.FULL);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return (state.getValue(HALF).equals(EnumBlockFrostHalf.BOTTOM) && face == EnumFacing.DOWN)
                || (state.getValue(HALF).equals(EnumBlockFrostHalf.TOP) && face == EnumFacing.UP)
                || state.getValue(HALF).equals(EnumBlockFrostHalf.FULL);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = getStateFromMeta(meta);
        return state.getValue(HALF).equals(EnumBlockFrostHalf.FULL) ? state
                : (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D)
                ? state.withProperty(HALF, EnumBlockFrostHalf.BOTTOM)
                : state.withProperty(HALF, EnumBlockFrostHalf.TOP));
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return state.getValue(HALF).equals(EnumBlockFrostHalf.FULL) ? 2 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(HALF).equals(EnumBlockFrostHalf.FULL);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumBlockFrostHalf half = state.getValue(HALF);
        switch (half) {
            case TOP:
                return AABB_TOP_HALF;
            case BOTTOM:
                return AABB_BOTTOM_HALF;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return state.getValue(HALF).equals(EnumBlockFrostHalf.FULL)
                || state.getValue(HALF).equals(EnumBlockFrostHalf.TOP);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (state.getValue(HALF) == EnumBlockFrostHalf.FULL) {
            return BlockFaceShape.SOLID;
        } else if (face == EnumFacing.UP && state.getValue(HALF) == EnumBlockFrostHalf.TOP) {
            return BlockFaceShape.SOLID;
        } else {
            return face == EnumFacing.DOWN && state.getValue(HALF) == EnumBlockFrostHalf.BOTTOM ? BlockFaceShape.SOLID
                    : BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
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
            if (!heldItem.isEmpty()
                    && ((state.getValue(HALF).equals(EnumBlockFrostHalf.TOP) && state.getValue(HALF).equals(EnumBlockFrostHalf.BOTTOM))
                    || (state.getValue(HALF).equals(EnumBlockFrostHalf.BOTTOM) && facing.equals(EnumFacing.UP)))) {
                if (heldItem.getItem() == Item.getItemFromBlock(this)) {
                    worldIn.setBlockState(pos, state.withProperty(HALF, EnumBlockFrostHalf.FULL));
                    if (!playerIn.capabilities.isCreativeMode)
                        heldItem.setCount(heldItem.getCount() - 1);
                    SoundType soundtype = this.getSoundType(state, worldIn, pos, playerIn);
                    worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
                            (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HALF, EnumBlockFrostHalf.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF).ordinal();
    }

    /**
     *
     */
    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySlab();
    }

    public enum EnumBlockFrostHalf implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom"),
        FULL("full");

        private final String name;
        EnumBlockFrostHalf(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public static EnumBlockFrostHalf byMetadata(int metadata) {
            if (metadata < 0 || metadata >= values().length) {
                metadata = 0;
            }
            return values()[metadata];
        }
    }
}