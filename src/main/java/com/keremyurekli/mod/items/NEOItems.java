package com.keremyurekli.mod.items;

import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.WaterFluid;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

public class NEOItems {

    public static void registerAll() {

    }
    public static List<NEOItem> ITEMS = new ArrayList<>();

    public static NEOItem MOD_LOGO = register(Static.Item.MOD_LOGO, new Item.Settings().rarity(Rarity.EPIC));

    public static NEOItem SPONGE_AND_BUCKET = registerFluidModificationItem(Static.Item.SPONGE_AND_BUCKET,
            new Item.Settings().rarity(Rarity.COMMON)
                    .maxDamage(1000));


    public static NEOItem register(String name, Item.Settings settings) {
        NEOItem item = new NEOItem(settings);
        Identifier id = Static.id(name);

        ITEMS.add(item);
        return Registry.register(Registries.ITEM, id, item);
    }

    public static NEOItem registerFluidModificationItem(String name, Item.Settings settings) {
        NEOItem item = new FluidLevelInteractionItem(null, settings);
        Identifier id = Static.id(name);

        ITEMS.add(item);
        return Registry.register(Registries.ITEM, id, item);
    }

}
