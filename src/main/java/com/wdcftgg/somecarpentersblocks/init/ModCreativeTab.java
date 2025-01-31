package com.wdcftgg.somecarpentersblocks.init;

import com.wdcftgg.somecarpentersblocks.SomeCarpentersBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModCreativeTab {
    public static final CreativeTabs Tab = new CreativeTabs(CreativeTabs.getNextID(), SomeCarpentersBlocks.MODID + "_tab") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Items.ARROW);
        }
    };
}
