package com.keremyurekli.mod.mixin.physics;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.physics.FluidPhysics;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;


//mc-fluid-physics
@Mixin(PistonBlock.class)
public abstract class PistonFixMixin extends FacingBlock {

    @Shadow
    @Final
    private boolean sticky;

    protected PistonFixMixin(Settings settings) {
        super(settings);
    }

//    @Inject(at = @At("HEAD"), method = "isMovable", cancellable = true)
//    private static void isMovable(BlockState state,
//                                  World world,
//                                  BlockPos pos,
//                                  Direction motionDir,
//                                  boolean canBreak,
//                                  Direction pistonDir,
//                                  CallbackInfoReturnable<Boolean> info) {
//        FluidState fluidState = state.getFluidState();
//        if (fluidState.getFluid() instanceof NEOFluid) {
//            BlockPos nextBlockPos = pos.offset(motionDir);
//            BlockState nextBlockState = world.getBlockState(nextBlockPos);
//            if (!(nextBlockState.isAir() ||
//                    nextBlockState.getFluidState().getFluid() instanceof NEOFluid ||
//                    nextBlockState.getPistonBehavior() == PistonBehavior.DESTROY)) {
//                info.setReturnValue(false);
//            }
//        }
//    }



    @Inject(at = @At("HEAD"), method = "move", cancellable = true)
    private void move(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = pos.offset(dir);
        if (!retract && world.getBlockState(blockPos).isOf(Blocks.PISTON_HEAD)) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NO_REDRAW | Block.FORCE_STATE);
        }

        PistonHandler pistonHandler = new PistonHandler(world, pos, dir, retract);
        if (!pistonHandler.calculatePush()) {
            cir.setReturnValue(false);
        } else {
            Set<BlockPos> blockPosSet = new HashSet<>();

            blockPosSet.addAll(pistonHandler.getBrokenBlocks());
            for (BlockPos blockPos3 : blockPosSet) {
                BlockState blockState2 = world.getBlockState(blockPos3);
                if (blockState2.getFluidState().getFluid() instanceof NEOFluid nf && !world.isClient()) {

                        //BlockEntity blockEntity = blockState2.hasBlockEntity() ? world.getBlockEntity(blockPos3) : null;
                        // dropStacks(blockState2, world, blockPos3, blockEntity);

                        BlockPos next = blockPos3.offset(dir);
                        int currentLevel = FluidPhysics.getFluidLevel(blockPos3, world);
                        if (world.getBlockState(next).canBucketPlace(nf)) {
                            Static.LOGGER.info("FORWARD FOR "+blockPos3);
//                        FluidPhysics.setFluidLevel(0,blockPos3,null,world);
                            world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), 11);
                            //world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos3, GameEvent.Emitter.of(blockState2));

                            FluidPhysics.addFluid(currentLevel, next, nf, world);

                            //world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                            //Static.LOGGER.info("Calling ADDFLUID " + blockPos3 + " with level " + currentLevel);
                        } else {
                            Static.LOGGER.info("SQUISH FOR "+blockPos3);
                            //world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS | Block.FORCE_STATE);
                            //world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos3, GameEvent.Emitter.of(blockState2));
                            //Static.LOGGER.info("Calling FluidPhysics.squish for " + blockPos3 + " with level " + currentLevel);
                            world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), 11);

                            FluidPhysics.squished(blockPos3, currentLevel, nf, world);
                        }


                }

            }

        }
    }
}

