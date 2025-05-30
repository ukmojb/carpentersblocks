package com.wdcftgg.somecarpentersblocks.blocks;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntityGarageDoor;
import com.wdcftgg.somecarpentersblocks.blocks.te.TileEntitySafe;
import com.wdcftgg.somecarpentersblocks.init.ModCreativeTab;
import com.wdcftgg.somecarpentersblocks.items.ModItems;
import com.wdcftgg.somecarpentersblocks.network.MessageSafe;
import com.wdcftgg.somecarpentersblocks.network.PacketHandler;
import com.wdcftgg.somecarpentersblocks.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

public class BlockSafe extends BlockChest implements IHasModel {
    public BlockSafe() {
        super(Type.BASIC);
        this.setCreativeTab(ModCreativeTab.Tab);
        this.setRegistryName("safe");
        this.setHardness(2.5F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(SomeCarpentersBlocks.MODID + ".safe");


        ModBlocks.BLOCKS.add(this);
        ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySafe();
    }

    @Override
    public void registerModels() {
        SomeCarpentersBlocks.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return NOT_CONNECTED_AABB;
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


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntitySafe te = (TileEntitySafe) worldIn.getTileEntity(pos);
        if (te != null && !worldIn.isRemote) {
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Objects.requireNonNull(Block.getBlockFromName(te.getBlock())), 1, te.getMeta()));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    public ILockableContainer getContainer(World worldIn, BlockPos pos, boolean allowBlocking)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (!(tileentity instanceof TileEntityChest))
        {
            return null;
        }
        else
        {
            ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;

            if (!allowBlocking && this.isBlocked(worldIn, pos))
            {
                return null;
            }
            else
            {

                return ilockablecontainer;
            }
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntitySafe te = (TileEntitySafe) worldIn.getTileEntity(pos);
        if (te != null) {
            if (!te.hasBlock()) {
                ItemStack itemStack = playerIn.getHeldItem(hand);
                if (itemStack.getItem() instanceof ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                    Block block = itemBlock.getBlock();
                    int meta = itemStack.getMetadata();

                    itemStack.shrink(1);
//                    System.out.println(Block.getIdFromBlock(block));
//                    System.out.println(meta);
                    te.setBlockAndMeta(block.getRegistryName().toString(), meta);
//                        PacketHandler.INSTANCE.sendToAll(new MessageSafe(Block.getIdFromBlock(block), meta, pos));
                    playerIn.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
                    return true;
                }
            }
        }


        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
    {
        int i = this.material == Material.IRON ? 1037 : 1007;
        worldIn.playEvent(player, i, pos, 0);
    }

    private boolean isBlocked(World worldIn, BlockPos pos)
    {
        return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
    }


    private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
    {
        return false;
    }

    private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos)
    {
        for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 2), (double)(pos.getZ() + 1))))
        {
            EntityOcelot entityocelot = (EntityOcelot)entity;

            if (entityocelot.isSitting())
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}
