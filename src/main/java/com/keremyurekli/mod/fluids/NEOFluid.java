package com.keremyurekli.mod.fluids;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class NEOFluid extends FlowableFluid {

    private boolean randomTicks;
    private int flowSpeed;
    private int levelDecrease;
    private int tickRate;
    private float blastResistance;

    public NEOFluidSettings settings;

    public String name = "no_name";

    public NEOFluid(boolean randomTicks, int flowSpeed, int levelDecrease, int tickRate, float blastResistance, NEOFluidSettings s) {
        super();
        this.randomTicks = randomTicks;
        this.flowSpeed = flowSpeed;
        this.levelDecrease = levelDecrease;
        this.tickRate = tickRate;
        this.blastResistance = blastResistance;

        this.settings = s;
    }

    public NEOFluid() {
//        this.settings = settings;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return true;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return this.levelDecrease;//1
    }


    @Override
    public int getTickRate(WorldView worldView) {
        return this.tickRate;//5
    }


    @Override
    protected float getBlastResistance() {
        return this.blastResistance;//100f
    }


    @Override
    protected boolean hasRandomTicks() {
        return this.randomTicks;
    }


    @Override
    protected int getMinFlowDownDistance(
            WorldView world,
            BlockPos pos,
            int i,
            Direction direction,
            BlockState state,
            BlockPos fromPos,
            Short2ObjectMap<Pair<BlockState, FluidState>> stateCache,
            Short2BooleanMap flowDownCache
    ) {
        return super.getMinFlowDownDistance(world,pos,i,direction,state,fromPos,stateCache,flowDownCache);
    }

    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return Integer.MAX_VALUE;
    }


}
