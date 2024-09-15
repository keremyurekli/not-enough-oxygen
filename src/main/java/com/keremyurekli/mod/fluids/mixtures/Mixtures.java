package com.keremyurekli.mod.fluids.mixtures;

import com.keremyurekli.mod.fluids.NEOFluid;

import java.util.Arrays;
import java.util.HashSet;

public class Mixtures {

    public static NEOFluid result(NEOFluid... fluids) {
        if (fluids.length == 1) {
            return (NEOFluid) fluids[0].getStill();
        }
        if (new HashSet<>(Arrays.asList(fluids)).size() == 1) {
            return (NEOFluid) fluids[0].getStill();
        }


        return null;
    }
}
