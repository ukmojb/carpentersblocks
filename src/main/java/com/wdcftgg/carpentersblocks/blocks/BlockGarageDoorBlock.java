package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityGarageDoorBlock;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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

import static com.wdcftgg.carpentersblocks.blocks.BlockGarageDoor.OPEN;

public class BlockGarageDoorBlock extends Block implements IHasModel, ITileEntityProvider {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public TileEntityGarageDoorBlock tileEntityGarageDoorBlock = null;
    protected static final AxisAlignedBB EAST_CLOSE_AABB = new AxisAlignedBB((double) 2/16, 0.0D, 0.0D, (double) 4/16, 1D, 1.0D);
    protected static final AxisAlignedBB WEST_CLOSE_AABB = new AxisAlignedBB((double) 12/16, 0.0D, 0.0D, (double) 14/16, 1.0D, 1.0D);
    protected static final AxisAlignedBB SOUTH_CLOSE_AABB = new AxisAlignedBB(0.0D, 0.0D, (double) 2/16, 1.0D, 1.0D, (double) 4/16);
    protected static final AxisAlignedBB NORTH_CLOSE_AABB = new AxisAlignedBB(0.0D, 0.0D, (double) 12/16, 1.0D, 1.0D, (double) 14/16);

    public BlockGarageDoorBlock() {
        super(Material.WOOD);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setRegistryName("garage_door_block");
        this.setTranslationKey(CarpentersBlocks.MODID + ".garage_door");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));


        ModBlocks.BLOCKS.add(this);
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityGarageDoorBlock();
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb;

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


        return axisalignedbb;
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

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | getMetaForFacing((EnumFacing)state.getValue(FACING));

        return i;
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

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }


    public boolean isFullCube(IBlockState state)
    {
        return false;
    }


    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }


    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i = 0;
        while (true) {
            i++;
            boolean pass = worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoorBlock || (worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoor && !worldIn.getBlockState(pos.up(i)).getValue(OPEN));
            boolean pass1 = worldIn.getBlockState(pos.down(i)).getBlock() == ModBlocks.GarageDoorBlock;

            if (pass) spdestroyBlock(worldIn, pos.up(i));
            if (pass1) spdestroyBlock(worldIn, pos.down(i));
            if (i >= 66) break;
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {

        TileEntityGarageDoorBlock doorBlock = (TileEntityGarageDoorBlock) worldIn.getTileEntity(pos);
        if (doorBlock != null) {
            if (!doorBlock.hasBlock()) {
                ItemStack itemStack1 = playerIn.getHeldItem(hand);
                if (itemStack1.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock1 = (ItemBlock) itemStack1.getItem();

                    Block block1 = itemBlock1.getBlock();
                    int meta1 = itemStack1.getMetadata();

                    if (block1 != this) {

                        if (block1.isOpaqueCube(block1.getStateFromMeta(meta1))) {

                            doorBlock.setBlockAndMeta(block1.getRegistryName().toString(), meta1);
                            playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                            int s = 0;
                            while (true) {
                                s++;
                                boolean pass = worldIn.getBlockState(pos.up(s)).getBlock() == ModBlocks.GarageDoorBlock;
                                boolean pass1 = (worldIn.getBlockState(pos.up(s)).getBlock() == ModBlocks.GarageDoor && !worldIn.getBlockState(pos.up(s)).getValue(OPEN));

                                if (pass1) {
                                    IBlockState blockState = worldIn.getBlockState(pos.up(s));

                                    TileEntityGarageDoor te = (TileEntityGarageDoor) worldIn.getTileEntity(pos.up(s));
                                    if (te != null) {
//                                    System.out.println("has TileEntityGarageDoor");
                                        ItemStack itemStack = playerIn.getHeldItem(hand);
                                        if (itemStack.getItem() instanceof ItemBlock) {
//                                    System.out.println("itemStack.getItem() instanceof ItemBlock");

                                            ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                                            Block block = itemBlock.getBlock();
                                            int meta = itemStack.getMetadata();


                                            itemStack1.shrink(1);
                                            te.addBlock(block.getRegistryName().toString(), meta, s);
                                            return true;
                                        }
                                    }

                                    break;
                                }

                                if (s >= 66) break;
//                            if (!pass) continue;
                            }

                            return true;
                        }
                    }
                }
            }
        }



        int i = 0;
        while (true) {
            i++;
            boolean pass = worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoorBlock;
            boolean pass1 = (worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoor && !worldIn.getBlockState(pos.up(i)).getValue(OPEN));

            if (pass1) {
                IBlockState blockState = worldIn.getBlockState(pos.up(i));
                BlockGarageDoor block2 = (BlockGarageDoor) worldIn.getBlockState(pos.up(i)).getBlock();

                block2.cycleProperty(OPEN, blockState, pos.up(i), worldIn);
                break;
            }

            if (i >= 66) break;
            if (!pass) continue;
        }

        return true;
    }


    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockPowered(pos);
            if (flag)
            {
                int i = 0;
                while (true) {
                    i++;
                    boolean pass = worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoorBlock;
                    boolean pass1 = (worldIn.getBlockState(pos.up(i)).getBlock() == ModBlocks.GarageDoor && !worldIn.getBlockState(pos.up(i)).getValue(OPEN));

                    if (pass1) {
                        IBlockState blockState = worldIn.getBlockState(pos.up(i));
                        BlockGarageDoor block2 = (BlockGarageDoor) worldIn.getBlockState(pos.up(i)).getBlock();

                        block2.cycleProperty(OPEN, blockState, pos.up(i), worldIn);
                        break;
                    }



                    if (i >= 66) break;

//                    if (!pass) continue;
                }
            }
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
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {

    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    public void spdestroyBlock(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == ModBlocks.GarageDoorBlock) {
            world.setBlockToAir(pos);
        } else {
            world.destroyBlock(pos, true);
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.withProperty(FACING, facing);
        }
        else
        {
            iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        }


        return iblockstate;
    }


    @Override
    public void registerModels() {
        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {

        if (tileEntityGarageDoorBlock != null) {
            if (tileEntityGarageDoorBlock.hasBlock()) {
                return EnumBlockRenderType.INVISIBLE;
            }
        }
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

}