package com.wdcftgg.carpentersblocks.carpentersblocks.renderer;

import com.wdcftgg.carpentersblocks.carpentersblocks.block.state.Property;
import com.wdcftgg.carpentersblocks.carpentersblocks.tileentity.CbTileEntity;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.IConstants;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.RotationUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.attribute.*;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.attribute.AbstractAttribute.Key;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.block.BlockUtil;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.DesignHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.DyeHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.OverlayHandler;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.SpriteRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.*;

import static net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;

public class RenderPkg {

	public static ThreadLocal<IBlockState> _threadLocalBlockState = new ThreadLocal<>();
	public static ThreadLocal<VertexFormat> _threadLocalVertexFormat = new ThreadLocal<>();
    private long _rand;
    private AttributeHelper _cbAttrHelper;
    private BlockPos _blockPos;
    private Integer _cbMetadata;
    private QuadContainer _quadContainer;
    private BlockRenderLayer _uncoveredRenderLayer;
    private boolean _isSideCover;
    private double _sideDepth;
    private boolean _isSnowCover;
    private EnumAttributeLocation _location;
    private IModelState _modelState;
    private final static double SIDE_DEPTH = 1/16D;
    private final static double SNOW_SIDE_DEPTH = 1/8D;
    
    public RenderPkg(VertexFormat vertexFormat, IBlockState blockState, EnumFacing facing, long rand) {
    	_threadLocalBlockState.set(blockState);
    	_threadLocalVertexFormat.set(vertexFormat);
    	_rand = rand;
    	_blockPos = (BlockPos) getThreadedProperty(Property.BLOCK_POS);
    	_cbMetadata = (Integer) getThreadedProperty(Property.CB_METADATA);
    	Block block = Minecraft.getMinecraft().world.getBlockState(_blockPos).getBlock();
    	_uncoveredRenderLayer = block.getRenderLayer();
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute>) getThreadedProperty(Property.ATTR_MAP));
    	_quadContainer = new QuadContainer(EnumAttributeLocation.HOST);
    }
    
    public RenderPkg(VertexFormat vertexFormat, EnumFacing facing, long rand) {
    	_threadLocalVertexFormat.set(vertexFormat);
    	_cbMetadata = 0;
    	_rand = rand;
    	_quadContainer = new QuadContainer(EnumAttributeLocation.HOST);
    }
    
	public void addAll(Collection<Quad> collection) {
		for (Quad quad : collection) {
			if (quad != null) {
				_quadContainer.add(quad);
			}
		}
	}
    
    public void add(Quad quad) {
    	if (quad != null) {
    		_quadContainer.add(quad);
    	}
    }
    
	/**
	 * Rotates quads about facing axis.
	 * 
	 * @param facing defines the axis of rotation
	 * @param rotation the rotation enum
	 */
	public void rotate(RotationUtil.Rotation rotation) {
		_quadContainer.rotate(rotation);
	}
    
    public IBlockState appendAttributeBlockState(CbTileEntity cbTileEntity, IBlockState blockState, EnumAttributeLocation location, EnumAttributeType type) {
    	if (cbTileEntity.getAttributeHelper().hasAttribute(location, type)) {
    		ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, type)).getModel();
    		Block block = Block.getBlockFromItem(itemStack.getItem());
    		IBlockState attrBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, type);
    		return ((IExtendedBlockState)blockState).withProperty(Property.ATTR_BLOCKSTATE, attrBlockState);
    	}
    	return blockState;
    }
    
    public List<BakedQuad> getInventoryQuads() {
    	return _quadContainer.bakeQuads();
    }
    
    /**
     * Gets a final list of baked quads for rendering.
     * 
     * @return a list of baked quads
     */
    public List<BakedQuad> getQuads() {
    	List<BakedQuad> quads = new ArrayList<BakedQuad>();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();
        IBlockState hostBlockState = _cbAttrHelper.hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER) ?
        		BlockUtil.getAttributeBlockState(_cbAttrHelper, EnumAttributeLocation.HOST, EnumAttributeType.COVER) : null;
        boolean hasHostCoverOverride = _quadContainer.hasCoverOverride();

    	for (EnumAttributeLocation location : EnumAttributeLocation.values()) {

    		_location = location;
    		QuadContainer quadContainer = _quadContainer;
    		boolean hasLocationCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    		
    		if ((_isSideCover = !EnumAttributeLocation.HOST.equals(location)) && !hasLocationCover) {
    			continue;
    		}
    		boolean hasOverlay = _cbAttrHelper.hasAttribute(location, EnumAttributeType.OVERLAY);
	        boolean hasChiselDesign = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DESIGN_CHISEL);
	        
	    	// Set side cover depth
	        if (!EnumAttributeLocation.HOST.equals(location)) {
		    	_sideDepth = getSideCoverDepth(location);
		    	quadContainer = _quadContainer.toSideLocation(null, location, _sideDepth);
	        }
	        
	        Map<EnumFacing, List<Quad>> coverQuadMap = getCoveredQuads(quadContainer, location, renderLayer);
	        
	        // Find required render layers for cover
	    	boolean renderAttribute = false;
	    	IBlockState attributeState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
	    	if (attributeState != null) {
	    		renderAttribute = attributeState.getBlock().canRenderInLayer(attributeState, renderLayer);
	    	}
	    	boolean canRenderBaseQuads = quadContainer.getRenderLayers(renderAttribute).contains(renderLayer);
	        BlockRenderLayer outermostCoverRenderLayer = _uncoveredRenderLayer;
	        if (attributeState != null && hasLocationCover) {
	        	for (BlockRenderLayer layer : BlockRenderLayer.values()) {
	        		if (attributeState.getBlock().canRenderInLayer(attributeState, layer)) {
	        			if (layer.equals(renderLayer)) {
	        				canRenderBaseQuads = true;
	        			}
	        			outermostCoverRenderLayer = layer;
	        		}
	        	}
	        } else {
	        	canRenderBaseQuads |= renderLayer.equals(_uncoveredRenderLayer);
	        }
	        // Check quads for render layer overrides
	        if (!canRenderBaseQuads) {
	            for (EnumFacing facing : EnumFacing.VALUES) {
	            	if (coverQuadMap.containsKey(facing)) {
	            		for (Quad quad : coverQuadMap.get(facing)) {
	            			if (renderLayer.equals(quad.getRenderLayer())) {
	            				canRenderBaseQuads = true;
	            				break;
	            			}
	            		}
	            	}
	            	if (canRenderBaseQuads) {
	            		break;
	            	}
	            }
	        }
	        
	        // Find render layer for overlay (must be outermost)
	        BlockRenderLayer overlayRenderLayer = CUTOUT_MIPPED;
	        if (hasOverlay) {
	        	overlayRenderLayer = TRANSLUCENT;
	        } else if (CUTOUT_MIPPED.ordinal() < outermostCoverRenderLayer.ordinal()) {
	        	overlayRenderLayer = outermostCoverRenderLayer;
	        }
	        BlockRenderLayer chiselDesignRenderLayer = TRANSLUCENT;

	        // Render cover layer
            for (EnumFacing facing : EnumFacing.VALUES) {
            	if (coverQuadMap.containsKey(facing)) {
            		for (Quad quad : coverQuadMap.get(facing)) {
            			quads.add(quad.bake(location));
            		}
            	}
            }
	        
	        // Add chisel quads
	        if (hasChiselDesign && chiselDesignRenderLayer.equals(renderLayer)) {
        		String design = ((AttributeString)_cbAttrHelper.getAttribute(location, EnumAttributeType.DESIGN_CHISEL)).getModel();
        		TextureAtlasSprite chiselSprite = SpriteRegistry.sprite_design_chisel.get(DesignHandler.listChisel.indexOf(design));
        		for (EnumFacing facing : EnumFacing.VALUES) {
        			quads.addAll(getQuadsForSide(quadContainer, facing, chiselSprite, IConstants.DEFAULT_RGB, false));
        		}
	        }
	        
	        // Add overlay quads
	        if (hasOverlay && overlayRenderLayer.equals(renderLayer)) {
	        	Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
	        	int overlayColor = IConstants.DEFAULT_RGB;
	        	if (Overlay.GRASS.equals(overlay)) {
	        		IBlockState overlayBlockState = Blocks.GRASS.getDefaultState();
	        		overlayColor = blockColors.colorMultiplier(overlayBlockState, Minecraft.getMinecraft().world, _blockPos, ForgeHooksClient.getWorldRenderPass());
	        	}        	
	        	for (EnumFacing facing : EnumFacing.VALUES) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(overlay, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, overlayColor, false));
	        		}
	        	}
	        }
	        
	        // Add snow override
	        Key upKey = AbstractAttribute.generateKey(EnumAttributeLocation.UP, EnumAttributeType.COVER);
	        IBlockState upBlockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, upKey.getLocation(), upKey.getType());
	        if (EnumAttributeLocation.HOST.equals(location)
	        		&& _cbAttrHelper.hasAttribute(upKey)
	        		&& isSnowState(upBlockState)
	        		&& (hostBlockState != null && !isSnowState(hostBlockState))
	        		&& overlayRenderLayer.equals(renderLayer)) {
	        	for (EnumFacing facing : EnumFacing.VALUES) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(Overlay.SNOW, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, null, false));
	        		}
	        	}
	        }
    	}
    	
        return quads;
    }
    
    private boolean isSnowState(IBlockState blockState) {
    	return blockState.getBlock().equals(Blocks.SNOW) ||
    			blockState.getBlock().equals(Blocks.SNOW_LAYER);
    }
    
    private Map<EnumFacing, List<Quad>> getCoveredQuads(QuadContainer quadContainer, EnumAttributeLocation location, BlockRenderLayer renderLayer) {
    	Map<EnumFacing, List<Quad>> map = new HashMap<EnumFacing, List<Quad>>();
    	boolean canRenderCover = false;
    	boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
    	int dyeRgb = 0;
    	if (hasDye) {
    		dyeRgb = DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel());
    	}
    	boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    	BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
    	IBlockState blockState = null;
    	
    	// Discover cover quads to donate texture and tint color
    	Map<EnumFacing, List<BakedQuad>> bakedQuads = new HashMap<EnumFacing, List<BakedQuad>>();
    	if (hasCover) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(coverStack);
		    	blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
		    	canRenderCover = blockState.getBlock().canRenderInLayer(blockState, renderLayer);
	    		for (EnumFacing facing : EnumFacing.values()) {
		    		bakedQuads.put(facing, itemModel.getQuads(blockState, facing, _rand));
		    	}
	    	}
    	}
    	
    	int attrRgb = -1;
    	if (blockState != null) {
    		attrRgb = blockColors.colorMultiplier(blockState, Minecraft.getMinecraft().world, _blockPos, ForgeHooksClient.getWorldRenderPass());
    	}
    	
    	// Add quads
    	for (EnumFacing facing : EnumFacing.values()) {
    		map.put(facing, new ArrayList<Quad>());
    		for (Quad quad : quadContainer.getQuads(blockState, facing)) {
	    		if (quad.canCover()) { // Quad can be covered
	    			if (hasCover) {
	    				if (canRenderCover && bakedQuads.get(facing) != null) {
	    					for (BakedQuad bakedQuad : bakedQuads.get(facing)) {
	    						Quad newQuad = new Quad(quad);
	    						if (Blocks.GRASS.equals(blockState.getBlock())) {
    								if (isTintedGrassSprite(bakedQuad.getSprite())) {
    									newQuad.setRgb(attrRgb);
    								} else if (hasDye) {
    									newQuad.setRgb(dyeRgb);
    								}
    							} else if (bakedQuad.getTintIndex() > -1) {
    								if (hasDye) {
    									newQuad.setRgb(dyeRgb);
    								} else {
    									newQuad.setRgb(attrRgb);
    								}
	    				    	} else if (hasDye) {
	    				    		newQuad.setRgb(dyeRgb);
	    				    	}
    	    					newQuad.setSprite(bakedQuad.getSprite());
								map.get(facing).add(newQuad);
	    					}
	    				}
	    				continue;
	    			}
	    		}
	    		if (quad.getRenderLayer().equals(renderLayer)) {
	    			if (hasDye) {
	    				quad.setRgb(dyeRgb);
	    			}
	    			map.get(facing).add(quad);
	    		}
	    	}
    	}
    
    	return map;
    }
    
    private boolean isTintedGrassSprite(TextureAtlasSprite sprite) {
    	return "minecraft:blocks/grass_top".equalsIgnoreCase(sprite.getIconName()) || "minecraft:blocks/grass_side_overlay".equalsIgnoreCase(sprite.getIconName());
    }
    
    private List<BakedQuad> getQuadsForSide(QuadContainer quadContainer, EnumFacing facing, TextureAtlasSprite spriteOverride, Integer rgbOverride, boolean isCover) {
    	List<Quad> srcQuads = quadContainer.getQuads(null, facing);
    	List<Quad> destQuads = new ArrayList<Quad>(srcQuads.size());
    	for (Quad quad : srcQuads) {
    		Quad newQuad = new Quad(quad);
			if (quad.canCover() && spriteOverride != null) {
				newQuad.setSprite(spriteOverride);
			}
    		if (rgbOverride != null) {
    			newQuad.setRgb(rgbOverride);
    		}
    		destQuads.add(newQuad);
    	}
    	return quadContainer.bakeQuads(destQuads);
    }
    
    /**
     * While rendering raw quads, can be overridden to not draw
     * quads for side depending on block state or other factors.
     * 
     * @param facing the facing
     * @return <code>true</code> if side should render
     */
    protected boolean canRenderSide(EnumFacing facing) {
    	return true;
    }
    
    /**
     * Transforms BakedQuad into Quad.
     * 
     * @param bakedQuad the baked quad to transform
     * @return a new Quad
     */
    protected Quad transform(BakedQuad bakedQuad, EnumFacing facing) {
		int[] data = bakedQuad.getVertexData();
		float xMin = Float.MAX_VALUE;
		float xMax = Float.MIN_VALUE;
		float yMin = Float.MAX_VALUE;
		float yMax = Float.MIN_VALUE;
		float zMin = Float.MAX_VALUE;
		float zMax = Float.MIN_VALUE;
        for (int i = 0; i < 4; ++i) {
            float x = Float.intBitsToFloat(data[i * 7]);
            float y = Float.intBitsToFloat(data[i * 7 + 1]);
            float z = Float.intBitsToFloat(data[i * 7 + 2]);
            xMin = Math.min(xMin, x);
            yMin = Math.min(yMin, y);
            zMin = Math.min(zMin, z);
            xMax = Math.max(xMax, x);
            yMax = Math.max(yMax, y);
            zMax = Math.max(zMax, z);
        }
        switch (facing) {
	        case DOWN:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMin, zMax), new Vec3d(xMin, yMin, zMin), new Vec3d(xMax, yMin, zMin), new Vec3d(xMax, yMin, zMax));
	        case UP:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMin), new Vec3d(xMin, yMax, zMax), new Vec3d(xMax, yMax, zMax), new Vec3d(xMax, yMax, zMin));
	        case NORTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMax, yMax, zMin), new Vec3d(xMax, yMin, zMin), new Vec3d(xMin, yMin, zMin), new Vec3d(xMin, yMax, zMin));
	        case SOUTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMax), new Vec3d(xMin, yMin, zMax), new Vec3d(xMax, yMin, zMax), new Vec3d(xMax, yMax, zMax));
	        case WEST:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMin), new Vec3d(xMin, yMin, zMin), new Vec3d(xMin, yMin, zMax), new Vec3d(xMin, yMax, zMax));
	        case EAST:
	        default:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMax, yMax, zMax), new Vec3d(xMax, yMin, zMax), new Vec3d(xMax, yMin, zMin), new Vec3d(xMax, yMax, zMin));
        }
	}
    
    private double getSideCoverDepth(EnumAttributeLocation location) {
    	IBlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
    	if (EnumAttributeLocation.UP.equals(location) && blockState != null && isSnowState(blockState)) {
    		return SNOW_SIDE_DEPTH;
    	} else {
    		return SIDE_DEPTH;
    	}
    }
    
    protected List<Quad> transform(List<BakedQuad> quads) {
    	List<Quad> outQuads = new ArrayList<Quad>();
    	for (BakedQuad bakedQuad : quads) {
    		//outQuads.add(transform(bakedQuad));
    	}
    	return outQuads;
    }
    
    public Integer getData() {
    	return _cbMetadata;
    }
    
    public static Object getThreadedProperty(IProperty property) {
		IBlockState blockState = _threadLocalBlockState.get();
		if (blockState != null) {
			return blockState.getValue(property);
		}
		return null;
    }
    
	public static Object getThreadedProperty(IUnlistedProperty property) {
		IBlockState blockState = _threadLocalBlockState.get();
		if (blockState != null) {
			return ((IExtendedBlockState)blockState).getValue(property);
		}
		return null;
	}
	
}
