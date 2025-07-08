package com.wdcftgg.carpentersblocks.carpentersblocks.util.states.factory;

import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.BlockRegistry;

public class StateFactory {
	
	public static AbstractState getState(CbTileEntity cbTileEntity) {
		if (BlockRegistry.blockCarpentersPressurePlate.equals(cbTileEntity.getBlockType())) {
			return new PressurePlateState(cbTileEntity);
		}
		return null;
	}
	
}