package com.keremyurekli.mod.fluids;

import com.keremyurekli.mod.blocks.NEOBlocks;
import com.keremyurekli.mod.items.NEOItems;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

import java.awt.*;
import java.util.Optional;

public abstract class WaterFluid extends NEOFluid {

    public WaterFluid() {
//        (FluidConstants.WATER_VISCOSITY/FluidConstants.VISCOSITY_RATIO)/2
        super(true,4,1, (FluidConstants.WATER_VISCOSITY/FluidConstants.VISCOSITY_RATIO)/2,100f,
                new NEOFluidSettings(2,100, MapColor.LIGHT_BLUE,new Color(75, 226, 255),false));
    }




    @Override
    public Item getBucketItem() {
        return null;
    }

    @Override
    public Fluid getStill() {
        return NEOFluids.WATER_STILL;
    }

    @Override
    public Fluid getFlowing() {
        return NEOFluids.WATER_FLOWING;
    }
    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return NEOBlocks.WATER.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }


    @Override
    public ParticleEffect getParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    public static class Flowing extends WaterFluid {

        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }


        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends WaterFluid {

        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }


        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }

//        @Override
//        public boolean isEmpty() {
//            return false;
//        }
    }
}
