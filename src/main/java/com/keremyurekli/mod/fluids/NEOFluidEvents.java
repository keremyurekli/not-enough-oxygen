package com.keremyurekli.mod.fluids;

import com.keremyurekli.mod.events.BlockBreakCallback;
import com.keremyurekli.mod.events.BlockBrokenCallback;
import com.keremyurekli.mod.events.BlockPlacedCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class NEOFluidEvents {

    public static void registerAll() {
        blockPlaceEvent();
        blockBreakEvent();
        blockBrokenEvent();

//        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//            System.out.println("hit thing");
//
//            world.setBlockState(hitResult.getBlockPos(), NEOFluids.WATER.getDefaultState());
//            world.breakBlock(hitResult.getBlockPos(),false);
//            return ActionResult.SUCCESS;
//        });
    }

    public static void onBlockPlace(World world, BlockPos pos, BlockState state, Entity placer, ItemStack itemStack) {
        world.getPlayers().forEach(playerEntity -> {
//            playerEntity.sendMessage(Text.literal(String.valueOf(state)));
//            playerEntity.sendMessage(Text.literal("Place"));
        });
    }

    public static void onBlockBreak(WorldAccess world, BlockPos pos, BlockState state, Entity placer) {
        world.getPlayers().forEach(playerEntity -> {
//            playerEntity.sendMessage(Text.literal("Break"));
        });
    }

    public static void onBlockBroken(WorldAccess world, BlockPos pos, BlockState state) {
        world.getPlayers().forEach(playerEntity -> {
//            playerEntity.sendMessage(Text.literal("Broken"));
        });
    }

    private static void blockPlaceEvent() {
        BlockPlacedCallback.EVENT.register((world,pos,state,placer,itemStack)->{
           onBlockPlace(world,pos,state,placer,itemStack);
            return ActionResult.PASS;
        });
    }
    private static void blockBreakEvent() {
        BlockBreakCallback.EVENT.register((world, pos, state, player)->{
            onBlockBreak(world,pos,state,player);
            return ActionResult.PASS;
        });
    }
    private static void blockBrokenEvent() {
        BlockBrokenCallback.EVENT.register((world,pos,state)->{
            onBlockBroken(world,pos,state);
            return ActionResult.PASS;
        });
    }

}
