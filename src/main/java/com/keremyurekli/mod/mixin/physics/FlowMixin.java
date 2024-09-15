package com.keremyurekli.mod.mixin.physics;


import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.physics.FluidPhysics;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public class FlowMixin {

    @Inject(at = @At("HEAD"), method = "canFlowThrough", cancellable = true)
    private void canFlowThrough(BlockView world, Fluid fluid, BlockPos pos, BlockState state, Direction face, BlockPos fromPos, BlockState fromState, FluidState fluidState, CallbackInfoReturnable<Boolean> c) {
        if (fluid instanceof NEOFluid) {
            c.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "canFlow", cancellable = true)
    private void canFlow(BlockView world, BlockPos fluidPos, BlockState fluidBlockState, Direction flowDirection, BlockPos flowTo, BlockState flowToBlockState, FluidState fluidState, Fluid fluid, CallbackInfoReturnable<Boolean> c) {
        if (fluid instanceof NEOFluid) {
            c.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "tryFlow", cancellable = true)
    private void tryFlow(World world, BlockPos fluidPos, FluidState state, CallbackInfo c) {
        if (state.getFluid() instanceof NEOFluid) {
            FluidPhysics.flow(world, fluidPos, state);
            c.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getUpdatedState", cancellable = true)
    private void getUpdatedState(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> c) {
        FluidState fluidstate = state.getFluidState();
        Fluid fluid = fluidstate.getFluid();
        if (fluid instanceof NEOFluid neofl) {
            if (NEOFluids.isFlowing(neofl)) {
                c.setReturnValue(neofl.getFlowing(state.getFluidState().getLevel(), false));
            }
        }
    }
}