package com.keremyurekli.mod.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

public class DirectionUtil {

    public static Direction directionToNeighbor(BlockPos base, BlockPos pos2) {
        Direction toReturn = null;
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos pos = base.offset(dir);
            if (pos.equals(pos2)) {
                //found the neighbor
                toReturn = dir;
                break;
            }

        }
        return toReturn;
    }

    public static List<BlockPos> fromDirectionList(BlockPos pos, List<Direction> list) {
        List<BlockPos> toReturn = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            toReturn.add(pos.offset(list.get(i)));
        }

        return toReturn;
    }
}
