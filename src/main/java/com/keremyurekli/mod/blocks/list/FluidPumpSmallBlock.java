package com.keremyurekli.mod.blocks.list;

import com.keremyurekli.mod.blocks.NEOBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class FluidPumpSmallBlock extends NEOBlock {
    public FluidPumpSmallBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.1f, 0.1f, 0.1f, 0.9f, 0.9f, 0.9f);
    }
}
