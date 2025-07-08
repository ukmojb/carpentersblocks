package com.wdcftgg.carpentersblocks.carpentersblocks.util.states.factory;

import com.wdcftgg.carpentersblocks.carpentersblocks.renderer.Quad;
import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.block.PressurePlateUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.StateUtil;
import net.minecraft.util.EnumFacing;

public class PressurePlateState extends AbstractState {
	
	public PressurePlateState(CbTileEntity cbTileEntity) {
		super(cbTileEntity);
	}

	@Override
	protected void rotate() {
		StateUtil util = new StateUtil();
		PressurePlateUtil ppUtil = new PressurePlateUtil(_cbTileEntity);
		switch (ppUtil.getFacing()) {
			case DOWN:
				util.rotate(this, EnumFacing.NORTH, Quad.Rotation.HALF.getValue());
				break;
			case NORTH:
				util.rotate(this, EnumFacing.WEST, Quad.Rotation.QUARTER.getValue());
				break;
			case SOUTH:
				util.rotate(this, EnumFacing.EAST, Quad.Rotation.QUARTER.getValue());
				break;
			case WEST:
				util.rotate(this, EnumFacing.NORTH, Quad.Rotation.QUARTER.getValue());
				break;
			case EAST:
				util.rotate(this, EnumFacing.SOUTH, Quad.Rotation.QUARTER.getValue());
				break;
			default: {}
		}
	}

}
