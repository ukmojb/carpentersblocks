package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityFlowerPot;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockFlowerPot extends Block implements IHasModel,ITileEntityProvider {
//    public static final PropertyEnum<EnumFlowerType> CONTENTS = PropertyEnum.<EnumFlowerType>create("contents", EnumFlowerType.class);
    protected static final AxisAlignedBB FLOWER_POT_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);

    public BlockFlowerPot()
    {
        super(Material.CIRCUITS);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("flower_pot");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".flower_pot");


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));

    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName()
    {
        return I18n.translateToLocal("item.flowerPot.name");
    }

    /**
     * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess, BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FLOWER_POT_AABB;
    }

    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     * @deprecated call via {@link IBlockState#isOpaqueCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        TileEntityFlowerPot te = this.getTileEntity(worldIn, pos);
        if (te != null && !worldIn.isRemote) {
            if (facing.equals(EnumFacing.UP) && (!te.hasPlant() || !te.hasSoil())) {
                if (te.hasSoil()) {
                    if (TileEntityFlowerPot.plantList.containsKey(itemstack.getItem().getRegistryName().toString())) {
                        if (TileEntityFlowerPot.plantList.get(itemstack.getItem().getRegistryName().toString()).equals(itemstack.getMetadata())) {
                            te.setPlant(itemstack);
                            playerIn.playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                            te.markDirty();
                            worldIn.notifyBlockUpdate(pos, state, state, 3);
                            itemstack.shrink(1);
                        }
                    }

                    return true;
                } else {
                    if (itemstack.getItem() instanceof ItemBlock) {
                        ItemBlock itemBlock = (ItemBlock) itemstack.getItem();
                        if (itemBlock.getBlock().equals(Blocks.DIRT)) {
                            te.setHasSoil(true);
                            te.markDirty();
                            playerIn.playSound(SoundEvents.BLOCK_GRAVEL_BREAK, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                            itemstack.shrink(1);

                        }
                    }
                }
            } else {
                if (!te.hasBlock()) {
                    ItemStack itemStack = playerIn.getHeldItem(hand);
                    if (itemStack.getItem() instanceof ItemBlock) {
                        ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                        Block block = itemBlock.getBlock();
                        int meta = itemStack.getMetadata();

                        if (block != this) {
                            te.setBlockAndMeta(block.getRegistryName().toString(), meta);
                            itemStack.shrink(1);
                            playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

//    private boolean canBePotted(ItemStack stack)
//    {
//        Block block = Block.getBlockFromItem(stack.getItem());
//
//        if (block != Blocks.YELLOW_FLOWER && block != Blocks.RED_FLOWER && block != Blocks.CACTUS && block != Blocks.BROWN_MUSHROOM && block != Blocks.RED_MUSHROOM && block != Blocks.SAPLING && block != Blocks.DEADBUSH)
//        {
//            int i = stack.getMetadata();
//            return block == Blocks.TALLGRASS && i == BlockTallGrass.EnumType.FERN.getMeta();
//        }
//        else
//        {
//            return true;
//        }
//    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) && (downState.isTopSolid() || downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID);
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        IBlockState downState = worldIn.getBlockState(pos.down());
        if (!downState.isTopSolid() && downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) != BlockFaceShape.SOLID)
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
    }



    @Nullable
    private TileEntityFlowerPot getTileEntity(World worldIn, BlockPos pos)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof TileEntityFlowerPot ? (TileEntityFlowerPot)tileentity : null;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {

        return new TileEntityFlowerPot();
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     * <p>
     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
     * does not fit the other descriptions and will generally cause other things not to connect to the face.
     *
     * @return an approximation of the form of the given face
     * @deprecated call via {@link IBlockState#getBlockFaceShape(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
