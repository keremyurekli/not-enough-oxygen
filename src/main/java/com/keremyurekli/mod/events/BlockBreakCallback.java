package com.keremyurekli.mod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public interface BlockBreakCallback {


    Event<BlockBreakCallback> EVENT = EventFactory.createArrayBacked(BlockBreakCallback.class,
            (listeners) -> (WorldAccess world, BlockPos pos, BlockState state, Entity player) -> {
                for (BlockBreakCallback listener : listeners) {
                    ActionResult result = listener.broke(world,pos,state,player);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult broke(WorldAccess world, BlockPos pos, BlockState state, Entity player);

}
