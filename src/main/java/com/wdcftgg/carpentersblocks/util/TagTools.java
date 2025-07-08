package com.wdcftgg.carpentersblocks.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TagTools {

    public static NBTTagCompound saveItemStackToNBT(ItemStack stack) {
        NBTTagCompound stackTag = new NBTTagCompound();
        NBTTagCompound tag = new NBTTagCompound();
        stackTag.setString("itemId", stack.getItem().getRegistryName().toString());
        if (stack.getTagCompound() != null) {
            stackTag.setTag("itemNbt", stack.getTagCompound());
        }
        stackTag.setInteger("meta", stack.getMetadata());
        stackTag.setInteger("count", stack.getCount());
        stackTag.setInteger("damage", stack.getItemDamage());
        tag.setTag("item", stackTag);
        return tag;
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound tag, String key) {
        if (tag.hasKey(key)) {
            NBTTagCompound nbt = tag.getCompoundTag(key).getCompoundTag("item");
            Item item = Item.getByNameOrId(nbt.getString("itemId"));
            if (item != null) {
                ItemStack itemStack = new ItemStack(item, nbt.getInteger("count"), nbt.getInteger("meta"));
                itemStack.setItemDamage(nbt.getInteger("damage"));

                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }
}
