package com.wdcftgg.carpentersblocks.init;

import com.google.common.collect.Sets;
import com.wdcftgg.carpentersblocks.CarpentersBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;

import java.io.File;
import java.util.Collections;
import java.util.Set;

public class ModLootList {

        private static final Set<ResourceLocation> LOOT_TABLES = Sets.<ResourceLocation>newHashSet();
        private static final Set<ResourceLocation> READ_ONLY_LOOT_TABLES = Collections.<ResourceLocation>unmodifiableSet(LOOT_TABLES);
        public static final ResourceLocation EMPTY = register("empty");
        //public static final ResourceLocation M_O_B = register("render/mor_orbital_beacon");


        private static ResourceLocation register(String id)
        {
            return register(new ResourceLocation(CarpentersBlocks.MODID, "loot_table/" + id));
        }

        public static ResourceLocation register(ResourceLocation id)
        {
            if (LOOT_TABLES.add(id))
            {
                return id;
            }
            else
            {
                throw new IllegalArgumentException(id + " is already a registered modded loot table");
            }
        }

        /**
         * An unmodifiable set is returned
         */
        public static Set<ResourceLocation> getAll()
        {
            return READ_ONLY_LOOT_TABLES;
        }

        public static boolean test()
        {
            LootTableManager loottablemanager = new LootTableManager((File)null);

            for (ResourceLocation resourcelocation : READ_ONLY_LOOT_TABLES)
            {
                if (loottablemanager.getLootTableFromLocation(resourcelocation) == LootTable.EMPTY_LOOT_TABLE)
                {
                    return false;
                }
            }

            return true;
        }

}
