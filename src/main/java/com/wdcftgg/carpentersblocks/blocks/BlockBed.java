package com.wdcftgg.carpentersblocks.blocks;

import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import com.wdcftgg.carpentersblocks.blocks.te.TileEntityBed;
import com.wdcftgg.carpentersblocks.init.ModCreativeTab;
import com.wdcftgg.carpentersblocks.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.DyeUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class BlockBed extends net.minecraft.block.BlockBed {
    
    public static final PropertyEnum<EnumPartType> PART = PropertyEnum.<EnumPartType>create("part", EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
    protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);


    public BlockBed() {
        super();
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("bed");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(CarpentersBlocks.MODID + ".bed");

        this.setDefaultState(this.blockState.getBaseState().withProperty(PART, EnumPartType.FOOT).withProperty(OCCUPIED, Boolean.valueOf(false)));

        ModBlocks.BLOCKS.add(this);
//        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     * @deprecated call via {@link IBlockState#getMapColor(IBlockAccess, BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(PART) == EnumPartType.FOOT)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityBed)
            {
                EnumDyeColor enumdyecolor = ((TileEntityBed)tileentity).getColor();
                return MapColor.getBlockColor(enumdyecolor);
            }
        }

        return MapColor.CLOTH;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntityBed te = (TileEntityBed) worldIn.getTileEntity(pos);
        if (te != null) {
            if (te.getColor() == EnumDyeColor.WHITE && state.getValue(PART).equals(EnumPartType.FOOT)) {

                ItemStack itemStack = playerIn.getHeldItem(hand);
                if (DyeUtils.isDye(itemStack)) {
                    itemStack.shrink(1);
                    te.setColor(DyeUtils.colorFromStack(itemStack).get());

                    IBlockState nearState = worldIn.getBlockState(pos.offset(state.getValue(FACING)));
                    Block block1 = nearState.getBlock();
                    if (block1 instanceof BlockBed) {
                        BlockBed nearBed = (BlockBed) block1;
                        TileEntityBed nearTe = (TileEntityBed) worldIn.getTileEntity(pos.offset(state.getValue(FACING)));

                        if (nearTe != null) {
                            nearTe.setColor(DyeUtils.colorFromStack(itemStack).get());
                        }
                    }

                    playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                    return true;
                }
            } else if (!te.hasBlock() && state.getValue(PART).equals(EnumPartType.HEAD)) {

                ItemStack itemStack = playerIn.getHeldItem(hand);
                if (itemStack.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                    Block block = itemBlock.getBlock();
                    int meta = itemStack.getMetadata();

                    if (block != this) {
                        itemStack.shrink(1);
                        te.setBlockAndMeta(block.getRegistryName().toString(), meta);

                        IBlockState nearState = worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite()));
                        Block block1 = nearState.getBlock();
                        if (block1 instanceof BlockBed) {
                            BlockBed nearBed = (BlockBed) block1;
                            TileEntityBed nearTe = (TileEntityBed) worldIn.getTileEntity(pos.offset(state.getValue(FACING).getOpposite()));

                            if (nearTe != null) {
                                nearTe.setBlockAndMeta(block.getRegistryName().toString(), meta);
                            }
                        }

                        playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
//                    return true;
                    }
                }
            } else {
                if (!worldIn.isRemote) {
                    if (state.getValue(PART) != EnumPartType.HEAD) {
                        pos = pos.offset((EnumFacing) state.getValue(FACING));
                        state = worldIn.getBlockState(pos);

                        if (state.getBlock() != this) {
                            return true;
                        }
                    }

                    WorldProvider.WorldSleepResult sleepResult = worldIn.provider.canSleepAt(playerIn, pos);
                    if (sleepResult != WorldProvider.WorldSleepResult.BED_EXPLODES) {
                        if (sleepResult == WorldProvider.WorldSleepResult.DENY) return true;
                        if (((Boolean) state.getValue(OCCUPIED)).booleanValue()) {
                            EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

                            if (entityplayer != null) {
                                playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                                return true;
                            }

                            state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
                            worldIn.setBlockState(pos, state, 4);
                        }

                        EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);

                        if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK) {
                            state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
                            worldIn.setBlockState(pos, state, 4);
                            return true;
                        } else {
                            if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
                                playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                            } else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE) {
                                playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
                            } else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
                                playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
                            }

                            return true;
                        }
                    } else {
                        worldIn.setBlockToAir(pos);
                        BlockPos blockpos = pos.offset(((EnumFacing) state.getValue(FACING)).getOpposite());

                        if (worldIn.getBlockState(blockpos).getBlock() == this) {
                            worldIn.setBlockToAir(blockpos);
                        }

                        worldIn.newExplosion((Entity) null, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 5.0F, true, true);
                        return true;
                    }
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Nullable
    private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos)
    {
        for (EntityPlayer entityplayer : worldIn.playerEntities)
        {
            if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos))
            {
                return entityplayer;
            }
        }

        return null;
    }

    /**
     * @deprecated call via {@link IBlockState#isFullCube()} whenever possible. Implementing/overriding is fine.
     */
    public boolean isFullCube(IBlockState state)
    {
        return false;
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
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance * 0.5F);
    }

    /**
     * Called when an Entity lands on this Block. This method *must* update motionY because the entity will not do that
     * on its own
     */
    public void onLanded(World worldIn, Entity entityIn)
    {
        if (entityIn.isSneaking())
        {
            super.onLanded(worldIn, entityIn);
        }
        else if (entityIn.motionY < 0.0D)
        {
            entityIn.motionY = -entityIn.motionY * 0.6600000262260437D;

            if (!(entityIn instanceof EntityLivingBase))
            {
                entityIn.motionY *= 0.8D;
            }
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        if (state.getValue(PART) == EnumPartType.FOOT)
        {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this)
        {
            if (!worldIn.isRemote)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }

            worldIn.setBlockToAir(pos);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(PART) == EnumPartType.FOOT ? Items.AIR : ModItems.Bed;
    }

    /**
     * @deprecated call via {@link IBlockState#getBoundingBox(IBlockAccess,BlockPos)} whenever possible.
     * Implementing/overriding is fine.
     */
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BED_AABB;
    }

    /**
     * @deprecated call via {@link IBlockState#hasCustomBreakingProgress()} whenever possible. Implementing/overriding
     * is fine.
     */
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    /**
     * Returns a safe BlockPos to disembark the bed
     */
    @Nullable
    public static BlockPos getSafeExitLocation(World worldIn, BlockPos pos, int tries)
    {
        EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - enumfacing.getXOffset() * l - 1;
            int j1 = k - enumfacing.getZOffset() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (hasRoomForPlayer(worldIn, blockpos))
                    {
                        if (tries <= 0)
                        {
                            return blockpos;
                        }

                        --tries;
                    }
                }
            }
        }

        return null;
    }

    protected static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid() && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (state.getValue(PART) == EnumPartType.HEAD)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.Bed, 1, enumdyecolor.getMetadata()));
        }
    }

    public EnumPushReaction getPushReaction(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
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
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link IBlockState#getRenderType()} whenever possible. Implementing/overriding is fine.
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos;

        if (state.getValue(PART) == EnumPartType.FOOT)
        {
            blockpos = pos.offset((EnumFacing)state.getValue(FACING));
        }

        TileEntity tileentity = worldIn.getTileEntity(blockpos);
        EnumDyeColor enumdyecolor = tileentity instanceof TileEntityBed ? ((TileEntityBed)tileentity).getColor() : EnumDyeColor.RED;
        return new ItemStack(Items.BED, 1, enumdyecolor.getMetadata());
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPartType.FOOT)
        {
            BlockPos blockpos = pos.offset((EnumFacing)state.getValue(FACING));

            if (worldIn.getBlockState(blockpos).getBlock() == this)
            {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
    {
        if (state.getValue(PART) == EnumPartType.HEAD && te instanceof TileEntityBed)
        {
            TileEntityBed tileentitybed = (TileEntityBed)te;
            ItemStack itemstack = tileentitybed.getItemStack();
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {

        TileEntityBed te = (TileEntityBed) worldIn.getTileEntity(pos);
        if (te != null && !worldIn.isRemote && te.hasBlock()) {
            if (te.hasBlock() && state.getValue(PART).equals(EnumPartType.HEAD))
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Objects.requireNonNull(Block.getBlockFromName(te.getBlock())), 1, te.getMeta()));
        }


        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, EnumPartType.HEAD).withProperty(FACING, enumfacing).withProperty(OCCUPIED, Boolean.valueOf((meta & 4) > 0)) : this.getDefaultState().withProperty(PART, EnumPartType.FOOT).withProperty(FACING, enumfacing);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (state.getValue(PART) == EnumPartType.FOOT)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.offset((EnumFacing)state.getValue(FACING)));

            if (iblockstate.getBlock() == this)
            {
                state = state.withProperty(OCCUPIED, iblockstate.getValue(OCCUPIED));
            }
        }

        return state;
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

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

        if (state.getValue(PART) == EnumPartType.HEAD)
        {
            i |= 8;

            if (((Boolean)state.getValue(OCCUPIED)).booleanValue())
            {
                i |= 4;
            }
        }

        return i;
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
        return BlockFaceShape.UNDEFINED;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, PART, OCCUPIED});
    }

    @SideOnly(Side.CLIENT)
    public static boolean isHeadPiece(int metadata)
    {
        return (metadata & 8) != 0;
    }

//    public static enum EnumPartType implements IStringSerializable
//    {
//        HEAD("head"),
//        FOOT("foot");
//
//        private final String name;
//
//        private EnumPartType(String name)
//        {
//            this.name = name;
//        }
//
//        public String toString()
//        {
//            return this.name;
//        }
//
//        public String getName()
//        {
//            return this.name;
//        }
//    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBed();
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player)
    {
        return true;
    }


//    @Override
//    public void registerModels() {
//        CarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
//    }
}
