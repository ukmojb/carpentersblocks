package com.wdcftgg.carpentersblocks.carpentersblocks.block;

import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.StateMap;

public interface IStateImplementor {

	public String getStateDescriptor(CbTileEntity cbTileEntity);
	
	public StateMap getStateMap();
	
}
