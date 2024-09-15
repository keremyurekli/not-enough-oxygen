package com.keremyurekli.mod.util;

import com.keremyurekli.mod.Static;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class NEOTags {

    public static final TagKey<Block> BLOCKS = TagKey.of(RegistryKeys.BLOCK, Static.id(Static.Block.BLOCKS));

    public static final TagKey<Fluid> FLUIDS = TagKey.of(RegistryKeys.FLUID, Static.id(Static.Fluid.FLUIDS));

    public static final TagKey<Item> ITEMS = TagKey.of(RegistryKeys.ITEM, Static.id(Static.Item.ITEMS));

}
