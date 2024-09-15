package com.keremyurekli.mod.blocks;

import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.blocks.list.FluidPumpSmallBlock;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.blocks.WaterBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class NEOBlocks {


    public static void registerAll() {

    }

    public static List<Block> BLOCKS = new ArrayList<>();

    public static final NEOBlock DEBUG_CUBE1 = register(Static.Block.DEBUG_CUBE1, new NEOBlockSettings(BlockSoundGroup.ANVIL, true));

    public static final NEOBlock DEBUG_CUBE2 = register(Static.Block.DEBUG_CUBE2, new NEOBlockSettings(BlockSoundGroup.AMETHYST_BLOCK, true));


    public static final FluidPumpSmallBlock FLUID_PUMP_SMALL = (FluidPumpSmallBlock)
            register(Static.Block.FLUID_PUMP_SMALL,
            new NEOBlockSettings(BlockSoundGroup.AMETHYST_BLOCK, true), FluidPumpSmallBlock.class);



    public static final FluidBlock WATER = registerFluid(Static.Block.WATER, new WaterBlock(NEOFluids.WATER_STILL, NEOFluids.WATER_STILL.settings.getSettings()));

    public static NEOBlock register(String name, NEOBlockSettings settings) {
        NEOBlock block = settings.block();
        Identifier id = Static.id(name);
        if (settings.registerItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        BLOCKS.add(block);
        return Registry.register(Registries.BLOCK, id, block);
    }


    public static NEOBlock register(String name, NEOBlockSettings settings, Class<? extends NEOBlock> cl) {
        NEOBlock block = (NEOBlock) settings.block(cl);
        Identifier id = Static.id(name);
        if (settings.registerItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }
        BLOCKS.add(block);
        return Registry.register(Registries.BLOCK, id, block);
    }
//
//    public static NEOAnimatedBlock registerAnimated(String name, NEOBlockSettings settings) {
//        if (!settings.isAnimated) {
//            throw new IllegalArgumentException("Tried to use animation without animation capable settings!");
//        }
//        NEOAnimatedBlock block = settings.blockA();
//        Identifier id = Static.id(name);
//        if (settings.registerItem) {
//            BlockItem blockItem = new BlockItem(block, new Item.Settings());
//            Registry.register(Registries.ITEM, id, blockItem);
//        }
//        BLOCKS.add(block);
//        return Registry.register(Registries.BLOCK, id, block);
//    }

    private static FluidBlock registerFluid(String name, WaterBlock block) {
        Identifier id = Static.id(name);
        return Registry.register(Registries.BLOCK, id, block);
    }



}
