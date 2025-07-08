package com.wdcftgg.carpentersblocks.carpentersblocks.block.data;

import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.RotationUtil;
import net.minecraft.util.EnumFacing.Axis;

public abstract class RotatableData {
	
	private static final int ROT_BITMASK = 0x3f;
	
	public static void rotate(CbTileEntity cbTileEntity, Axis axis) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		RotationUtil.Rotation rotation = RotationUtil.Rotation.get(cbMetadata).getNext(axis);
		setRotation(cbTileEntity, rotation);
	}
	
	public static void resetRotation(CbTileEntity cbTileEntity) {
		int data = RotationUtil.Rotation.X0_Y0_Z0.asInt();
		cbTileEntity.setCbMetadata(data);
	}
	
	public static RotationUtil.Rotation getRotation(CbTileEntity cbTileEntity) {
		return RotationUtil.Rotation.get(cbTileEntity.getCbMetadata());
	}
	
	public static void setRotation(CbTileEntity cbTileEntity, RotationUtil.Rotation rotation) {
		int cbMetadata = cbTileEntity.getCbMetadata();
		cbMetadata &= ~ROT_BITMASK;
		cbMetadata |= rotation.asInt();
        cbTileEntity.setCbMetadata(cbMetadata);
	}
	
}
