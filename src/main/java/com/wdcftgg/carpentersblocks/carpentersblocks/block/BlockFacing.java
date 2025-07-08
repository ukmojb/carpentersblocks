package com.wdcftgg.carpentersblocks.carpentersblocks.block;

import com.wdcftgg.carpentersblocks.carpentersblocks.block.state.Property;
import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;
import java.util.Random;

public abstract class BlockFacing extends BlockCoverable {

    public BlockFacing(Material material) {
        super(material);
    }
    
    public abstract void setFacing(CbTileEntity cbTileEntity, EnumFacing facing);
    
    public abstract EnumFacing getFacing(CbTileEntity cbTileEntity);

    /**
     * Checks to see if you can place this block can be placed on that side of a block: TileEntityLever overrides
     */
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing facing) {
        if (canAttachToFacing(facing)) {
        	return world.isSideSolid(blockPos.offset(facing.getOpposite()), facing);
        } else {
            return false;
        }
    }
    
    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public IBlockState getStateForPlacement(World world, BlockPos blockPos, EnumFacing facing, float hitX, float hitY, float hitZ, int metadata, EntityLivingBase entityLivingBase) {
    	return this.getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }
    
    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);
        if (!ignoreSidePlacement()) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                EnumFacing facing = (EnumFacing) blockState.getProperties().get(BlockDirectional.FACING);
                setFacing(cbTileEntity, facing);
            }
        }
        world.notifyNeighborsOfStateChange(blockPos, this, false);
    }

    /**
     * Whether side block placed against influences initial direction of block.
     *
     * @return <code>true</code> if initial placement direction ignored
     */
    protected boolean ignoreSidePlacement() {
        return false;
    }

    @Override
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world) {
        return 20;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborChange(IBlockAccess blockAccess, BlockPos blockPos, BlockPos neighborBlockPos) {
        super.onNeighborChange(blockAccess, blockPos, neighborBlockPos);
        World world = (World)blockAccess;
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null && !canPlaceBlockOnSide(world, blockPos, getFacing(cbTileEntity)) && !canFloat()) {
            //destroyBlock(world, blockPos, true);
        }
    }

    /**
     * Notifies relevant blocks of a change in power output.
     *
     * @param  world
     * @param  x
     * @param  y
     * @param  z
     * @return nothing
     */
    public void notifyBlocksOfPowerChange(World world, IBlockState blockState, BlockPos blockPos) {
    	// Strong power change
        world.neighborChanged(blockPos, this, blockPos);

        // Weak power change
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                EnumFacing facing = getFacing(cbTileEntity);
                BlockPos pos = blockPos.offset(facing.getOpposite());
                world.neighborChanged(pos, this, pos);
            } else {
            	world.notifyNeighborsOfStateChange(blockPos, this, false);
            }
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        int power = super.getWeakPower(blockState, blockAccess, blockPos, facing);
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
            if (cbTileEntity != null) {
                power = Math.max(power, getPowerOutput(cbTileEntity));
            }
        }
        return power;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        int power = super.getStrongPower(blockState, blockAccess, blockPos, facing);
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
            if (cbTileEntity != null) {
                if (facing.equals(getFacing(cbTileEntity))) {
                    power = Math.max(power, getPowerOutput(cbTileEntity));
                }
            }
        }
        return power;
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        if (canProvidePower(blockState)) {
            notifyBlocksOfPowerChange(world, blockState, blockPos);
        }
        super.breakBlock(world, blockPos, blockState);
    }

    /**
     * Gets block-specific power level from 0 to 15.
     *
     * @param  cbTileEntity  the {@link CbTileEntity}
     * @return the power output
     */
    public int getPowerOutput(CbTileEntity cbTileEntity) {
    	return 0;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    public boolean canAttachToFacing(EnumFacing facing) {
        return true;
    }

    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     */
    public boolean canFloat() {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, EnumFacing axis) {
        if (Arrays.asList(getRotationAxes()).contains(axis)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                EnumFacing facing = getFacing(cbTileEntity);
                //return data.setFacing(cbTileEntity, facing.rotateAround(axis.getAxis()));
            }
        }
        return false;
    }

    /**
     * Get supported axes of rotation.
     *
     * @return an array of axes
     */
    protected EnumFacing.Axis[] getRotationAxes() {
        return new EnumFacing.Axis[] { EnumFacing.Axis.Y };
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState blockState, Random rand) {
    	if (canProvidePower(blockState)) {
    		CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
		        world.notifyNeighborsOfStateChange(blockPos, this, false);
		        world.notifyNeighborsOfStateChange(blockPos.offset(this.getFacing(cbTileEntity).getOpposite()), this, false);
            }
    	}
    }
    
    @Override
    public IBlockState getExtendedState(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
		IBlockState outBlockState = super.getExtendedState(blockState, blockAccess, blockPos);
    	CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	EnumFacing facing = this.getFacing(cbTileEntity);
        	return ((IExtendedBlockState)outBlockState).withProperty(Property.FACING, facing);
        }
        return outBlockState;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(
        	this,
        	new IProperty[] { BlockDirectional.FACING },
        	Property._unlistedProperties.toArray(new IUnlistedProperty[Property._unlistedProperties.size()]));
    }

}
