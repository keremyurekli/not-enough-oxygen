package com.keremyurekli.mod;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Static {

    String MOD_NAME = "Not Enough Oxygen";
    String MOD_ID = "not-enough-oxygen";
    Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    String MAIN_ITEM_GROUP = "main_item_group";

    String NBT_BASE = MOD_ID +".nbt.";

    static @NotNull Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }


    interface Block {
        String BLOCKS = "blocks";
        String FLUIDS = "fluid_blocks";

        // Debug
        String DEBUG_CUBE1 = "debug_cube1";
        String DEBUG_CUBE2 = "debug_cube2";

        String FLUID_PUMP_SMALL = "fluid_pump_small";

        String WATER = "water";
    }

    interface Fluid {
        String FLUIDS = "fluids";

        String EMPTY_FLUID = "empty_fluid";

        String WATER_STILL = "water_still";
        String WATER_FLOWING = "water_flowing";

    }

    interface Item {
        String ITEMS = "items";

        String MOD_LOGO = "mod_logo";

        String WATER_BUCKET = "water_bucket";

        String SPONGE_AND_BUCKET = "sponge_and_bucket";
    }

    interface Packet {
        String UPDATE_BLOCK = "update_block";
    }

    interface Entity {
        String FLUID_PUMP_SMALL_ENTITY = "fluid_pump_small_entity";
    }
}
