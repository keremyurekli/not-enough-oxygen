package com.keremyurekli.mod.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BlockPlacedCallback {


    Event<BlockPlacedCallback> EVENT = EventFactory.createArrayBacked(BlockPlacedCallback.class,
            (listeners) -> (World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) -> {
                for (BlockPlacedCallback listener : listeners) {
                    ActionResult result = listener.placed(world,pos,state,placer,itemStack);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult placed(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack);

}
