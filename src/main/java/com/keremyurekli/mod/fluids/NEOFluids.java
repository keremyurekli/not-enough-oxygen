package com.keremyurekli.mod.fluids;

import com.keremyurekli.mod.Static;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NEOFluids {


    public static void registerAll() {

    }

    public static ArrayList<NEOFluid> FLUIDS = new ArrayList<>();

    public static ArrayList<NEOFluid> STILLS = new ArrayList<>();

    public static ArrayList<NEOFluid> FLOWINGS = new ArrayList<>();

    public static HashMap<NEOFluid,Identifier> identifierMap = new HashMap<>();

    public static NEOFluid EMPTY_FLUID = register(Static.Fluid.EMPTY_FLUID, new EmptyFluid(), false);

    public static NEOFluid WATER_STILL = register(Static.Fluid.WATER_STILL, new WaterFluid.Still(), true);
    public static NEOFluid WATER_FLOWING = register(Static.Fluid.WATER_FLOWING, new WaterFluid.Flowing(), false);


    public static NEOFluid register(String name, NEOFluid f, boolean still) {
        Identifier id = Static.id(name);
        f.name = name;

        FLUIDS.add(f);
        if (still) {
             STILLS.add(f);
        } else {
            FLOWINGS.add(f);
        }
        identifierMap.put(f,id);
        return Registry.register(Registries.FLUID, id, f);
    }


    public static boolean isStill(NEOFluid fluid) {
        return STILLS.contains(fluid);
    }

    public static boolean isFlowing(NEOFluid fluid) {
        return FLOWINGS.contains(fluid);
    }



    public static void registerFluidAttributes() {
        //removed temporarily?
//        FluidVariantAttributes.register(WATER_FLOWING, new SmartFluidAttribute(
//                Text.translatable(NEOBlocks.WATER.getTranslationKey()),
//                10000,
//                15,
//                FluidConstants.WATER_TEMPERATURE,
//                false
//        ));

    }

}
