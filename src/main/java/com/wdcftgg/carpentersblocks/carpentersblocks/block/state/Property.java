package com.wdcftgg.carpentersblocks.carpentersblocks.block.state;

import com.wdcftgg.carpentersblocks.carpentersblocks.util.states.factory.AbstractState;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Property {

	public static final IProperty[] _listedProperties = new IProperty[] { };
	public static final List<IUnlistedProperty> _unlistedProperties;
	public static final IUnlistedProperty ATTR_BLOCKSTATE;
	public static final IUnlistedProperty ATTR_MAP;
	public static final IUnlistedProperty<EnumFacing> FACING;
	public static final IUnlistedProperty<Block> BLOCK_TYP;
	public static final IUnlistedProperty<BlockPos> BLOCK_POS;
	public static final IUnlistedProperty<Integer> CB_METADATA;
	public static final IUnlistedProperty<Boolean[]> RENDER_FACE;
	public static final IUnlistedProperty<AbstractState> CB_STATE;

	static {
		_unlistedProperties = new ArrayList<IUnlistedProperty>();
		_unlistedProperties.add(ATTR_BLOCKSTATE = new UnlistedProperty(IBlockState.class, "cbAttrBlockState"));
		_unlistedProperties.add(ATTR_MAP = new UnlistedProperty(Map.class, "cbAttrMap"));
		_unlistedProperties.add(FACING = new UnlistedProperty(EnumFacing.class, "cbFacing"));
		_unlistedProperties.add(BLOCK_TYP = new UnlistedProperty(Block.class, "cbBlockTyp"));
		_unlistedProperties.add(BLOCK_POS = new UnlistedProperty(BlockPos.class, "cbBlockPos"));
		_unlistedProperties.add(CB_METADATA = new UnlistedProperty(Integer.class, "cbMetadata"));
		_unlistedProperties.add(RENDER_FACE = new UnlistedProperty(Boolean[].class, "cbRenderFace"));
		_unlistedProperties.add(CB_STATE = new UnlistedProperty(AbstractState.class, "cbState"));
	}
	
	public static class UnlistedProperty implements IUnlistedProperty {

		private Class _class;
		private String _name;

		public UnlistedProperty(Class clazz, String name) {
			_class = clazz;
			_name = name;
		}
		
		@Override
		public String getName() {
			return _name;
		}

		@Override
		public boolean isValid(Object value) {
			return true;
		}

		@Override
		public Class getType() {
			return _class;
		}

		@Override
		public String valueToString(Object value) {
			return value.toString();
		}
		
	}

}