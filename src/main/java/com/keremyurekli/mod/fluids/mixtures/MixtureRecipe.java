package com.keremyurekli.mod.fluids.mixtures;

import com.keremyurekli.mod.fluids.NEOFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class MixtureRecipe {

    public NEOFluid f1;
    public NEOFluid f2;
    public NEOFluid result;

    public MixtureRecipe(NEOFluid f1, NEOFluid f2, NEOFluid result) {
        this.f1 = f1;
        this.f2 = f2;
        this.result = result;
    }

    public void onMix(WorldAccess world, BlockPos at) {

    }
}
