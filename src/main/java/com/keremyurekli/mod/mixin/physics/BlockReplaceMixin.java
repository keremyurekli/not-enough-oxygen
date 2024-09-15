package com.keremyurekli.mod.mixin.physics;


import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.physics.FluidPhysics;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class BlockReplaceMixin {

//    @Inject(at = @At("HEAD"), method = "onBlockChanged")
//    public void onBlockChanged(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
//        if (oldBlock.getFluidState().getFluid() instanceof NEOFluid oldFluid &&
//                !(newBlock.getFluidState().getFluid() instanceof NEOFluid)) {
//            int oldLevel = NEOFluids.isStill(oldFluid) ? 8 : oldBlock.getFluidState().getLevel();
//            FluidPhysics.squished(pos,oldLevel,oldFluid,(World) (Object) this);
//            Static.LOGGER.info("I THINK IT REPLACED");
//        }
//    }

}
