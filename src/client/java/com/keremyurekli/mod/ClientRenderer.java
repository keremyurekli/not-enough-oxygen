package com.keremyurekli.mod;

import com.keremyurekli.mod.blocks.NEOBlock;
import com.keremyurekli.mod.blocks.NEOBlocks;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.WaterFluid;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.awt.*;

public class ClientRenderer {


    public static void start() {
        BlockRenderLayerMap.INSTANCE.putBlock(NEOBlocks.FLUID_PUMP_SMALL, RenderLayer.getCutout());



        NEOFluids.STILLS.forEach(neoFluid -> {
            FluidRenderHandlerRegistry.INSTANCE.register(neoFluid.getStill(), neoFluid.getFlowing(), new SimpleFluidRenderHandler(
                    SimpleFluidRenderHandler.WATER_STILL,
                    SimpleFluidRenderHandler.WATER_FLOWING,//flowing
                    SimpleFluidRenderHandler.WATER_OVERLAY,
                    (int) rgbToHexadecimal(neoFluid.settings.COLOR)
            ));
        });
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), NEOFluids.FLUIDS.toArray(new NEOFluid[0]));
    }

    public static long rgbToHexadecimal(Color color) {
        return Long.parseLong(Integer.toHexString(color.getRGB()).substring(2),16);
    }
}
