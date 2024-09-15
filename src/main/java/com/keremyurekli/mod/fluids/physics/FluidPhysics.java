package com.keremyurekli.mod.fluids.physics;

import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.mixtures.Mixtures;
import com.keremyurekli.mod.util.DirectionUtil;
import com.keremyurekli.mod.util.Scheduler;
import com.keremyurekli.mod.util.annotations.Experimental;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

//from dihydrogen monoxide, thx
public class FluidPhysics {

    private static Set<BlockPos> notAvailable = new HashSet<>();

    @Experimental(reason = "Subject to change")
    public static void flow(WorldAccess world, BlockPos fluidPos, FluidState fluidState) {
        if (world.getBlockState(fluidPos).getBlock() instanceof FluidFillable) {
            return;
        }
        NEOFluid fluid = (NEOFluid) fluidState.getFluid();
        int baseLevel = getFluidLevel(fluidPos, world);
        if ((world.getBlockState(fluidPos.down()).canBucketPlace(fluid)) && (fluid instanceof NEOFluid && getFluidLevel(fluidPos.down(), world) != 8)) {//          notify all and redraw
            if (!notAvailable.contains(fluidPos.down())) {

                // Much faster than dihydrogen monoxide
                int howMuchCanItTake = 8 - getFluidLevel(fluidPos.down(),world);

                if (howMuchCanItTake > 0) {
                    int amountWillBeProcessed = 0;
                    if (baseLevel <= howMuchCanItTake) {
                        amountWillBeProcessed = baseLevel;
                    } else {
                        amountWillBeProcessed = howMuchCanItTake;
                    }
                    removeFluidLevel(amountWillBeProcessed,fluidPos,world);
                    addFluid(amountWillBeProcessed, fluidPos.down(), fluid, world);

                    //notAvailable.add(fluidPos);

                    if (world.getBlockState(fluidPos.up()).getFluidState().getFluid() instanceof NEOFluid nf) {//experimental
                        flow(world, fluidPos.up(), world.getBlockState(fluidPos.up()).getFluidState());
                    }
//                    Scheduler.atServerEnd(() -> {
//                        Scheduler.atServerEnd(() -> {
//                            Scheduler.atServerEnd(() -> {
//                                notAvailable.remove(fluidPos);
//                            });
//                        });
//                    });
                }
            }
        } else {
            if (baseLevel > 1) {

                ArrayList<BlockPos> finalList = calculateFlowableBlocks(fluidPos,world);
                if (!finalList.isEmpty()) {
                    if (youFlowedFrom.containsKey(fluidPos)) {
                        BlockPos iFlowedFromWhere = youFlowedFrom.get(fluidPos);
                        youFlowedFrom.remove(fluidPos);
                        if (finalList.contains(iFlowedFromWhere)) {
                            if (finalList.size() > 1) {
                                finalList.remove(iFlowedFromWhere);
                            }
                        }

                    }

                    equalizeFluid(finalList, fluidPos, world);
                }
            }

        }

        //world.updateNeighbors(fluidPos, world.getBlockState(fluidPos).getBlock());
    }

    public static ArrayList<BlockPos> calculateFlowableBlocks(BlockPos fluidPos, WorldAccess world) {
        NEOFluid fluid = (NEOFluid) world.getFluidState(fluidPos).getFluid();
        int baseLevel = getFluidLevel(fluidPos, world);

        ArrayList<BlockPos> blocks = new ArrayList<>(4);
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos pos = fluidPos.offset(dir);
            if (!world.getBlockState(pos).canBucketPlace(fluid) || getFluidLevel(pos, world) >= baseLevel) {
                //nah
            } else{
                blocks.add(pos);
            }
        }
//            blocks.forEach(pos -> {
//                if (!world.getBlockState(pos).canBucketPlace(fluid)) {
//                    blocks.remove(pos);
//                } else if (getFluidLevel(pos, world) >= baseLevel) {
//                    blocks.remove(pos);
//                }
//            });
//            blocks.removeIf(pos -> !world.getBlockState(pos).canBucketPlace(fluid));
//            blocks.removeIf(pos -> getFluidLevel(pos, world) >= baseLevel);

        HashMap<Integer, ArrayList<BlockPos>> priorities = new HashMap<>();
        blocks.forEach(pos -> {
            int level = getFluidLevel(pos, world);
            if (priorities.containsKey(level)) {
                ArrayList<BlockPos> list = priorities.get(level);
                list.add(pos);
            } else {
                ArrayList<BlockPos> list = new ArrayList<>();
                list.add(pos);
                priorities.put(level, list);
            }
        });
        ArrayList<BlockPos> finalList = new ArrayList<>();
        for (int i = 0; i < baseLevel; i++) {
            if (priorities.containsKey(i)) {
                ArrayList<BlockPos> list = priorities.get(i);
                Collections.shuffle(list);
                finalList.addAll(list);
            }
        }

        return finalList;
    }

    public static int getFluidLevel(BlockPos pos, WorldAccess world) {
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = blockState.getFluidState();
        Fluid fluid = fluidState.getFluid();
        if (fluid instanceof NEOFluid nf) {
            return NEOFluids.isStill(nf) ? 8 : fluidState.getLevel();
        }
        return 0;
    }

    public static void setFluidLevel(int level, BlockPos pos, NEOFluid what, WorldAccess world) {
        if (!world.getBlockState(pos).isAir()) {
                final BlockEntity blockEntity = world.getBlockState(pos).hasBlockEntity() ? world.getBlockEntity(pos) : null;
                Block.dropStacks(world.getBlockState(pos), world, pos, blockEntity);
        }
        if (level == 8) {
            if (!(world.getBlockState(pos).getBlock() instanceof FluidFillable)) { // Don't fill kelp etc
                world.setBlockState(pos, what.getDefaultState().getBlockState(), 11);
            }
        } else if (level == 0) {
            if (getFluidLevel(pos,world) > 0) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            }

        } else if (level < 8) {
            world.setBlockState(pos, what.getFlowing(level, false).getBlockState(), 11);
        } else {
            System.out.println("Can't set water >8 something went very wrong!");
        }
    }

    public static void addFluid(int howMuch, BlockPos to, NEOFluid what, WorldAccess world) {
        //Static.LOGGER.info("ADDING FLUID");
        int existingwater = getFluidLevel(to, world);
        int totalwater = existingwater + howMuch;
        if (totalwater > 8) {
            setFluidLevel(totalwater - 8, to.up(), what, world);
            setFluidLevel(8, to, what, world);
        } else {
            setFluidLevel(totalwater, to, what, world);
        }
    }

    public static void removeFluidLevel(int howMuch, BlockPos where, WorldAccess world) {
        //Static.LOGGER.info("ADDING FLUID");
        if (howMuch == 0) {
            // no need to do something
            return;
        }
        int existingwater = getFluidLevel(where, world);
        int remaining = existingwater - howMuch;
        if (existingwater > 0) {
            setFluidLevel(remaining, where, (NEOFluid) world.getFluidState(where).getFluid(), world);
        } else if (existingwater == 0){
            setFluidLevel(0, where, null, world);
        } else {
            //smaller than 0
            throw new IllegalArgumentException("Illegal removal attempt");
        }
    }


    public static void squished(BlockPos at, int howMuch, NEOFluid fluid, World world) {
        //Static.LOGGER.info("SQUISHED");
//            Static.LOGGER.info((blockState.getFluidState().getFluid() instanceof NEOFluid)+" "+"\n"
//            +blockState);
        BlockPos up = at.up();
        BlockState upState = world.getBlockState(up);

        ArrayList<BlockPos> blocks = new ArrayList<>(4);
        for (Direction dir : Direction.Type.HORIZONTAL) {
            blocks.add(at.offset(dir));
        }
        blocks.removeIf(pos -> !world.getBlockState(pos).canBucketPlace(fluid));

        if (blocks.isEmpty()) {
            if (upState.canBucketPlace(fluid)) {
                //Static.LOGGER.info("ADDING TOP");
                addFluid(howMuch, up, fluid, world);
            } else {
                Static.LOGGER.info("A fluid has been destroyed: " + fluid.name);
            }
        } else {
            Collections.shuffle(blocks);
            //Static.LOGGER.info("CALCULATED " + blocks.size()+ " WAYS");
            shareFluidBetween(blocks, howMuch, fluid, world);
        }


    }

    public static void shareFluidBetween(ArrayList<BlockPos> blocks, int howMuch, NEOFluid what, World world) {
        int[] fluidLevels = new int[blocks.size()];
        Arrays.fill(fluidLevels, 0);

        int current = 0;
        while (howMuch > 0) {
            howMuch--;
            fluidLevels[current] = fluidLevels[current] + 1;
            //Static.LOGGER.info("giving one to " + current);
            current++;
            if (current >= fluidLevels.length) {
                current = 0;
            }
        }
        for (BlockPos block : blocks) {
            int level = fluidLevels[blocks.indexOf(block)];
            if (level > 0) {
                addFluid(fluidLevels[blocks.indexOf(block)], block, what, world);
                //Static.LOGGER.info("PLACING TO " + block);
//                world.updateNeighbors(block,world.getBlockState(block).getBlock());
            } else {
                //Static.LOGGER.info("NOT PLACING TO " + block);
            }
        }
    }


    private static HashMap<BlockPos,BlockPos> youFlowedFrom = new HashMap<>();




    @Experimental(reason = "Subject to change")
    public static void equalizeFluid(ArrayList<BlockPos> blocks, BlockPos basePos, WorldAccess world) {

        //TODO: PRECALCULATE FLOWABLE BLOCKS, THEN EQUALIZE THEM ALL, miktar/akılabilecekalan, kalan önemli değil remove yap
        int centerwaterlevel = getFluidLevel(basePos, world);
        int[] waterlevels = new int[blocks.size()];
        Arrays.fill(waterlevels, -1);

        // mixture
        List<NEOFluid> mixtureFluids = new ArrayList<>();

        BlockState blockState = world.getBlockState(basePos);
        FluidState fluidState = blockState.getFluidState();
        mixtureFluids.add((NEOFluid) ((NEOFluid) fluidState.getFluid()).getStill());

        for (BlockPos block : blocks) {
            waterlevels[blocks.indexOf(block)] = getFluidLevel(block, world);

            if (world.getFluidState(block).getFluid() instanceof NEOFluid && getFluidLevel(block,world) > 0) {
                mixtureFluids.add((NEOFluid) ((NEOFluid) world.getFluidState(block).getFluid()).getStill());
            }
        }

        NEOFluid[] finalMixtureFluids = new NEOFluid[mixtureFluids.size()];
        for (int i = 0; i < mixtureFluids.size(); i++) {
//            Static.LOGGER.warn("Adding " + mixtureFluids.get(i).name + " to mixture!");
            finalMixtureFluids[i] = mixtureFluids.get(i);
        }
        NEOFluid finalFluid = Mixtures.result(finalMixtureFluids);
        // mixture



        if (blocks.size() == 1) {
            BlockPos flowedPos = blocks.getFirst();
            int flowedPosLevel = waterlevels[0];

            int div = (centerwaterlevel+flowedPosLevel)/2;
            int leftover = (centerwaterlevel+flowedPosLevel)%2;

            int amountWillBeProcessed = 0;

            int howMuchCanItTake = 8 - flowedPosLevel;

            if (howMuchCanItTake < div) {
                amountWillBeProcessed = howMuchCanItTake;
            } else {
                amountWillBeProcessed = div;
            }

            removeFluidLevel((amountWillBeProcessed),basePos,world);
            //TODO: Better implementation for mixtures, to addFluid()
            addFluid(amountWillBeProcessed,flowedPos,finalFluid,world);

            youFlowedFrom.put(flowedPos,basePos);

            return;
        }

//        Static.LOGGER.info("Result is " + finalFluid.name);

        int waterlevelsnum = waterlevels.length;
        int didnothings = 0;
        int waterlevel;

        boolean leftover = false;

        while (didnothings < waterlevelsnum) {
            didnothings = 0;
            for (int i = 0; i < waterlevels.length; i++) {
                waterlevel = waterlevels[i];
                if (waterlevel != -1) {
                    if ((centerwaterlevel >= (waterlevel + 2))) {
                        waterlevel += 1;
                        waterlevels[i] = waterlevel;
                        centerwaterlevel -= 1;
                    } else {
                        if (waterlevel > 0 && centerwaterlevel > waterlevel) {
                            leftover = true;
                        }
                        didnothings += 1;
                    }
                } else {
                    didnothings += 1;
                }
            }
        }
        if (leftover) {
            centerwaterlevel -= 1; // leftover

//            Random random = new Random();
//            int randomIndex = random.nextInt(waterlevels.length);

            int smallest = waterlevels[0];
            int smallestIndex = 0;
            for (int i = 1; i < waterlevels.length; i++) {
                if (waterlevels[i] < smallest) {
                    smallest = waterlevels[i];
                    smallestIndex = i;
                }
            }
            waterlevels[smallestIndex] += 1;
        }
        setFluidLevel(centerwaterlevel, basePos, finalFluid, world);
        for (BlockPos block : blocks) {
            setFluidLevel(waterlevels[blocks.indexOf(block)], block, finalFluid, world);
        }

    }


}
