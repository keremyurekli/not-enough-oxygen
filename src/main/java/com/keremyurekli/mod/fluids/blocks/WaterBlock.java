package com.keremyurekli.mod.fluids.blocks;

import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.physics.FluidPhysics;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WaterBlock extends FluidBlock {

    private FlowableFluid fluid;

    public WaterBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
        this.fluid = fluid;
    }
//    @Override
//    public void entityInside(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
//        if (world.getBlockState(entity.blockPosition().offset(0, (int) Math.floor(entity.getEyeHeight(entity.getPose())), 0)).getFluidState().getType().is(GCTags.OIL)) {
//            if (entity instanceof LivingEntity living) {
//                if (living instanceof Player player) {
//                    if (player.isCreative()) {
//                        return;
//                    }
//                }
//                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 3 * 20));
//            }
//        }
//    }


    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
            FluidState fluidState = state.getFluidState();
            Fluid fluid = fluidState.getFluid();
            if (fluid instanceof NEOFluid nf && !(newState.getFluidState().getFluid() instanceof NEOFluid)
            && !newState.isAir()) {
                int oldLevel = NEOFluids.isStill(nf) ? 8 : fluidState.getLevel();
                FluidPhysics.squished(pos,oldLevel,nf,world);
            }

//            Static.LOGGER.info("I THINK IT REPLACED");
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
    }
}
