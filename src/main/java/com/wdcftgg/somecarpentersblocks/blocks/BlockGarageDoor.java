package com.wdcftgg.somecarpentersblocks.blocks;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoorBlock;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.somecarpentersblocks.init.ModCreativeTab;
import com.wdcftgg.somecarpentersblocks.items.ModItems;
import com.wdcftgg.somecarpentersblocks.util.IHasModel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockGarageDoor extends Block implements IHasModel,ITileEntityProvider
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool OPEN = PropertyBool.create("open");
    protected static final AxisAlignedBB EAST_CLOSE_AABB = new AxisAlignedBB((double) 2/16, 0.0D, 0.0D, (double) 4/16, 1D, 1.0D);
    protected static final AxisAlignedBB WEST_CLOSE_AABB = new AxisAlignedBB((double) 12/16, 0.0D, 0.0D, (double) 14/16, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_CLOSE_AABB = new AxisAlignedBB(0.0D, 0.0D, (double) 2/16, 1.0D, 1.0D, (double) 4/16);
    protected static final AxisAlignedBB NORTH_CLOSE_AABB = new AxisAlignedBB(0.0D, 0.0D, (double) 12/16, 1.0D, 1.0D, (double) 14/16);
    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB((double) 2/16, 0.5D, 0.0D, (double) 4/16, 1D, 1.0D);
    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB((double) 12/16, 0.5D, 0.0D, (double) 14/16, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.5D, (double) 2/16, 1.0D, 1.0D, (double) 4/16);
    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.5D, (double) 12/16, 1.0D, 1.0D, (double) 14/16);

    protected BlockGarageDoor()
    {
        super(Material.WOOD);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("garage_door");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(SomeCarpentersBlocks.MODID + ".garage_door");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.valueOf(true)));

        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityGarageDoor();
    }

    /**
     * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess, BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb;

        if (((Boolean)state.getValue(OPEN)).booleanValue())
        {
            switch ((EnumFacing)state.getValue(FACING))
            {
                case NORTH:
                default:
                    axisalignedbb = NORTH_OPEN_AABB;
                    break;
                case SOUTH:
                    axisalignedbb = SOUTH_OPEN_AABB;
                    break;
                case WEST:
                    axisalignedbb = WEST_OPEN_AABB;
                    break;
                case EAST:
                    axisalignedbb = EAST_OPEN_AABB;
            }
        }
        else
        {
            switch ((EnumFacing)state.getValue(FACING))
            {
                case NORTH:
                default:
                    axisalignedbb = NORTH_CLOSE_AABB;
                    break;
                case SOUTH:
                    axisalignedbb = SOUTH_CLOSE_AABB;
                    break;
                case WEST:
                    axisalignedbb = WEST_CLOSE_AABB;
                    break;
                case EAST:
                    axisalignedbb = EAST_CLOSE_AABB;
            }
        }

        return axisalignedbb;
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
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Determines if an entity can path through this block
     */
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return !((Boolean)worldIn.getBlockState(pos).getValue(OPEN)).booleanValue();
    }

    /**
     * Called when the block is right clicked by a player.
     */
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityGarageDoor te = (TileEntityGarageDoor) worldIn.getTileEntity(pos);
        if (te != null) {
            if (!te.hasBlock()) {
                ItemStack itemStack = playerIn.getHeldItem(hand);
                if (itemStack.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                    Block block = itemBlock.getBlock();
                    int meta = itemStack.getMetadata();

                    itemStack.shrink(1);
                    te.setBlockAndMeta(Block.getIdFromBlock(block), meta);
                    playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                    return true;
                }
            }
        }

        if (this.material == Material.IRON)
        {
            return false;
        }
        else
        {
            int block = 0;
            int meta = 0;
            if (te != null) {
                if (te.hasBlock()) {
                    block = te.getBlock();
                    meta = te.getMeta();
                }
                state = state.cycleProperty(OPEN);
                worldIn.setBlockState(pos, state, 2);
                TileEntityGarageDoor newte = (TileEntityGarageDoor) worldIn.getTileEntity(pos);
                if (newte != null) {
                    newte.setBlockAndMeta(block, meta);
                }
                this.playSound(playerIn, worldIn, pos, ((Boolean)state.getValue(OPEN)).booleanValue());
            }
            return true;
        }
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
    {
        if (p_185731_4_)
        {
            int i = this.material == Material.IRON ? 1037 : 1007;
            worldIn.playEvent(player, i, pos, 0);
        }
        else
        {
            int j = this.material == Material.IRON ? 1036 : 1013;
            worldIn.playEvent(player, j, pos, 0);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i = 0;
        while (true) {
            i++;

            boolean pass = worldIn.getBlockState(pos.down(i)).getBlock() == ModBlocks.GarageDoorBlock;
            if (pass) {
                worldIn.setBlockToAir(pos.down(i));
            } else break;

            if (i >= 50) break;
        }

        TileEntityGarageDoor te = (TileEntityGarageDoor) worldIn.getTileEntity(pos);
        if (te != null && !worldIn.isRemote) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Block.getBlockById(te.getBlock())));
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockPowered(pos);

            if (flag || blockIn.getDefaultState().canProvidePower())
            {
                boolean flag1 = ((Boolean)state.getValue(OPEN)).booleanValue();

                if (flag1 != flag)
                {
                    worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                    this.playSound((EntityPlayer)null, worldIn, pos, flag);
                }
            }
        }

        this.checkAndDropBlock(worldIn, pos, state);
    }


    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState blockState = worldIn.getBlockState(pos.up());
        if (!blockState.isFullBlock() || !blockState.isFullCube())
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, Boolean.valueOf(false));
        }
        else
        {
            iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, Boolean.valueOf(false));
        }

        if (worldIn.isBlockPowered(pos))
        {
            iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(true));
        }

        return iblockstate;
    }

    /**
     * Check whether this Block can be placed at pos, while aiming at the specified side of an adjacent block
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState blockState = worldIn.getBlockState(pos.up());
        return blockState.getBlock() != Blocks.AIR && blockState.isFullBlock();
    }

    protected static EnumFacing getFacing(int meta)
    {
        switch (meta & 3)
        {
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.SOUTH;
            case 2:
                return EnumFacing.WEST;
            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    protected static int getMetaForFacing(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return 0;
            case SOUTH:
                return 1;
            case WEST:
                return 2;
            case EAST:
            default:
                return 3;
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, Boolean.valueOf((meta & 4) != 0));
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
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | getMetaForFacing((EnumFacing)state.getValue(FACING));

        if (((Boolean)state.getValue(OPEN)).booleanValue())
        {
            i |= 4;
        }

        return i;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
     * fine.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, OPEN});
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
        return face == EnumFacing.UP && !((Boolean)state.getValue(OPEN)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        if (state.getValue(OPEN))
        {
            IBlockState down = world.getBlockState(pos.down());
            if (down.getBlock() == net.minecraft.init.Blocks.LADDER)
                return down.getValue(BlockLadder.FACING) == state.getValue(FACING);
        }
        return false;
    }

    @Override
    public void registerModels() {
        SomeCarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    public static enum DoorHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private DoorHalf(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}