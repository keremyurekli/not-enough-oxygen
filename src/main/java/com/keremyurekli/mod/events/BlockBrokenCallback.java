package com.keremyurekli.mod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public interface BlockBrokenCallback {


    Event<BlockBrokenCallback> EVENT = EventFactory.createArrayBacked(BlockBrokenCallback.class,
            (listeners) -> (WorldAccess world, BlockPos pos, BlockState state) -> {
                for (BlockBrokenCallback listener : listeners) {
                    ActionResult result = listener.broken(world,pos,state);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult broken(WorldAccess world, BlockPos pos, BlockState state);

}
