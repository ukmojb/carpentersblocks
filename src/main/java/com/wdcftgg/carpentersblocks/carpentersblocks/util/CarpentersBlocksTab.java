package com.wdcftgg.carpentersblocks.carpentersblocks.util;

import com.wdcftgg.carpentersblocks.carpentersblocks.util.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CarpentersBlocksTab extends CreativeTabs {

    public CarpentersBlocksTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemRegistry.itemCarpentersHammer);
    }
}
