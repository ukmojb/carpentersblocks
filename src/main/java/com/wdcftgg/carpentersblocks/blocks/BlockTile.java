package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityTile;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTile extends Block implements IHasModel, ITileEntityProvider {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger STATE = PropertyInteger.create("state", 1, 3);

    protected static final AxisAlignedBB STATE_DOWN_AABB = new AxisAlignedBB(0.0D, 0, 0, 1, (double) 1/16, 1);
    protected static final AxisAlignedBB STATE_UP_AABB = new AxisAlignedBB(0.0D, (double) 15/16, 0, 1, 1, 1);

    protected static AxisAlignedBB EAST_AABB = new AxisAlignedBB(0, 0, 0, (double) 1/16, 1, 1);
    protected static AxisAlignedBB WEST_AABB = new AxisAlignedBB((double) 15/16, 0, 0, 1, 1, 1);
    protected static AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, (double) 1/16);
    protected static AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0, 0, (double) 15/16, 1, 1, 1);

    public BlockTile() {
        super(Material.WOOD);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("tile");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".tile");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(STATE, 1));


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityTile();
    }

    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb;
        
        if (state.getValue(STATE).intValue() == 2) {
            switch ((EnumFacing)state.getValue(FACING))
            {
                case NORTH:
                default:
                    axisalignedbb = NORTH_AABB;
                    break;
                case SOUTH:
                    axisalignedbb = SOUTH_AABB;
                    break;
                case WEST:
                    axisalignedbb = WEST_AABB;
                    break;
                case EAST:
                    axisalignedbb = EAST_AABB;
            }
        } else if (state.getValue(STATE).intValue() == 1){
            axisalignedbb = STATE_DOWN_AABB;
        } else {
            axisalignedbb = STATE_UP_AABB;
        }

        return axisalignedbb;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {

    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(STATE, 2);
        }
        if (facing == EnumFacing.UP) {
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST).withProperty(STATE, 1);
        }
        if (facing == EnumFacing.DOWN) {
            iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST).withProperty(STATE, 3);
        }

        return iblockstate;
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
    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        int facingValue = getMetaForFacing(facing);
//        switch (facing) {
//            case NORTH: facingValue = 0; break;
//            case SOUTH: facingValue = 1; break;
//            case WEST:  facingValue = 2; break;
//            case EAST:
//            default:    facingValue = 3; break;
//        }

        int stateValue = state.getValue(STATE) - 1;

        return (stateValue << 2) | facingValue;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        // 提取后2位作为朝向值 (0-3)
        int facingValue = meta & 0b0011;

        // 提取前2位作为状态值 (0-2)
        int stateValue = (meta >> 2) & 0b0011;

        // 将状态值转换为实际范围 (1-3)
        int actualState = Math.min(stateValue + 1, 3); // 确保不超过最大值

        // 创建默认状态并设置属性
        IBlockState state = this.getDefaultState()
                .withProperty(STATE, actualState);

        // 设置朝向
        EnumFacing facing = getFacing(facingValue);

        return state.withProperty(FACING, facing);
    }

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
        return new BlockStateContainer(this, new IProperty[] {FACING, STATE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityTile te = (TileEntityTile) worldIn.getTileEntity(pos);
//        if (te != null && !worldIn.isRemote) {
//            if (te.hasBlock())
//                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Objects.requireNonNull(Block.getBlockFromName(te.getBlock())), 1, te.getMeta()));
//        }
        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

}
