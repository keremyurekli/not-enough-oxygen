package com.keremyurekli.mod.mixin;

import com.keremyurekli.mod.events.BlockBreakCallback;
import com.keremyurekli.mod.events.BlockBrokenCallback;
import com.keremyurekli.mod.events.BlockPlacedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockEventsMixin {

	@Inject(at = @At("RETURN"), method = "onBroken")
	private void onBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
		if (world.isClient()) return;
		BlockBrokenCallback.EVENT.invoker().broken(world,pos,state);
	}

	@Inject(at = @At("RETURN"), method = "onBreak")
	private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
		if (world.isClient()) return;
		BlockBreakCallback.EVENT.invoker().broke(world, pos, state, player);
	}

	@Inject(at = @At("RETURN"), method = "onPlaced")
	private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		if (world.isClient()) return;
		BlockPlacedCallback.EVENT.invoker().placed(world,pos,state,placer,itemStack);
	}
}