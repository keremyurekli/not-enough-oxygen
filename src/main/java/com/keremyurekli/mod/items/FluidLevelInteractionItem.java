package com.keremyurekli.mod.items;

import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.fluids.NEOFluid;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.fluids.physics.FluidPhysics;
import com.keremyurekli.mod.util.NEOTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidLevelInteractionItem extends NEOItem implements FluidModificationItem {

    //ONLY WORKS WITH NEOFLUIDS
//    private Fluid fluid = Fluids.EMPTY;TODO: CHANGE IT TO NBT

//    private final int maxLevel = 8;

//    private int currentLevel = 0; //1-8, 125ml per level

    public FluidLevelInteractionItem(@Nullable Fluid fluid, Settings settings) {
        super(settings);
//        if (fluid != null) {
//            this.fluid = fluid;
//        }

    }

//    private List<Text> getUpdatedTooltip(ItemStack stack) {
//        List<Text> list = new ArrayList<>();
////        int currentLevel = getCurrentLevel(stack)*125;
////        int max = stack.getMaxDamage();
//        list.add(Text.literal("DENEME 1 Kİ"));
//        list.add(Text.literal("DENEME 2 Kİ"));
////        list.add(Text.translatable(this.getTranslationKey()+".lore1",currentLevel,max));
////        list.add(Text.translatable(this.getTranslationKey()+".lore2"));
//
//        return list;
//    }

    @Override
    public void appendTooltip(ItemStack itemStack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int currentLevel = getCurrentLevel(itemStack)*125;
        int max = itemStack.getMaxDamage();
        String key = this.getTranslationKey();

        Text description = Text.translatable(key+".description").formatted(Formatting.AQUA);
        String fluidInfo;
        if (currentLevel > 0) {
            Text fluidName = Text.translatable("fluid."+getNBTFluidString(itemStack).replace(":","."));
            Text f = Text.translatable(key+".fluidinfo");
            fluidInfo = String.format(f.getString(),currentLevel,max,fluidName.getString());
        } else {
            fluidInfo = Text.translatable(key+".empty").getString();
        }
        Text tip = Text.translatable(key+".tip").formatted(Formatting.GRAY);

        tooltip.add(description);
        tooltip.add(Text.of(fluidInfo));
        tooltip.add(tip);
    }


    @Override
    public ItemStack getDefaultStack() {
        ItemStack defaultStack = new ItemStack(this);
        setNBTFluid(defaultStack, NEOFluids.EMPTY_FLUID);
        setFluidAmount(defaultStack, 0);
        return defaultStack;
    }

    private void setNBTFluid(ItemStack s, Fluid f) {
        NbtCompound data = new NbtCompound();
        data.putString(Static.NBT_BASE+"spongebucket.fluid", String.valueOf(NEOFluids.identifierMap.get(f)));
        NbtComponent component = NbtComponent.of(data);
        s.set(DataComponentTypes.CUSTOM_DATA,component);

    }

    private NEOFluid getNBTFluid(ItemStack s) {
        NbtComponent component = s.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound data = component.getNbt();
        String key = Static.NBT_BASE+"spongebucket.fluid";
        if (data.contains(key)) {
            Identifier id = Identifier.tryParse(data.getString(key));
            return (NEOFluid) Registries.FLUID.get(id);
        }
        return null;
    }
    private String getNBTFluidString(ItemStack s) {
        NbtComponent component = s.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound data = component.getNbt();
        String key = Static.NBT_BASE+"spongebucket.fluid";
        if (data.contains(key)) {
            return data.getString(key);
        }
        return "null";
    }


    //    private static boolean matchesType(FlowableFluid fluid1, FlowableFluid fluid2) {
//        return fluid1 == fluid2.getStill() || fluid1 == fluid2.getFlowing();
//    }

    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.isInCreativeMode() ? new ItemStack(NEOItems.SPONGE_AND_BUCKET) : stack;
    }

    private int getCurrentLevel(ItemStack s) {
        return (s.getMaxDamage() - s.getDamage()) / 125;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return false;
    }

    private void setFluidAmount(ItemStack s, int level) {// 0-8
        s.setDamage(dmg(s, level));
    }

    private void addFluid(ItemStack s, int level) {// 0-8
        setFluidAmount(s, ((s.getMaxDamage() - s.getDamage()) / 125) + level);
    }


    private int dmg(ItemStack s, int d) {
        return ((s.getMaxDamage() / 125) - d) * 125;
    }

    @Override
    public void onCraft(ItemStack stack, World world) {
//        super.onCraft(stack, world);
//        stack.setDamage(currentLevel*125);
//        stack.setDamage(dmg(0));
//        this.fluid = Fluids.EMPTY;
//        updateVisuals(stack);
//        stack = getDefaultStack();
        setNBTFluid(stack,NEOFluids.EMPTY_FLUID);
        setFluidAmount(stack, 0); //prevent crafting
//        Static.LOGGER.info("DAMAGING THE BUCKET");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) {
            return TypedActionResult.fail(itemStack);
        }
        BlockHitResult blockHitResult = NEOItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);

            if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(blockPos2, direction, itemStack)) {
                return TypedActionResult.fail(itemStack);
            }

            BlockPos targetPosition;
            FluidState targetFluidState;
            Fluid targetFluid;

            if (!world.getFluidState(blockPos).isEmpty()) {
                if (!world.getFluidState(blockPos2).isEmpty()) {
                    targetPosition = blockPos2;
                } else {
                    targetPosition = blockPos;
                }
            } else {
                targetPosition = blockPos2;
            }
            targetFluidState = world.getFluidState(targetPosition);
            targetFluid = targetFluidState.getFluid();
            if (user.isSneaking()) {
                NEOFluid bucketFluid = getNBTFluid(itemStack);
                if (bucketFluid.isEmpty()) {
                    // no shit
                } else {
                    int howMuchCanTargetTake = 8 - FluidPhysics.getFluidLevel(targetPosition,world);
                    int bucketFluidLevel = getCurrentLevel(itemStack);
                    int transactionAmount = 0;
                    if (bucketFluidLevel <= howMuchCanTargetTake) {
                        transactionAmount = bucketFluidLevel;
                    } else {
                        transactionAmount = howMuchCanTargetTake;
                    }

                    int sum = bucketFluidLevel - transactionAmount;

                    if (sum > 0) {
                        // leftover fluid
                    } else {
                        setNBTFluid(itemStack,NEOFluids.EMPTY_FLUID);
                    }
                    setFluidAmount(itemStack,sum);

                    FluidPhysics.addFluid(transactionAmount,targetPosition,bucketFluid,world);
                    playEmptyingSound(null,world,targetPosition);

                    return TypedActionResult.success(itemStack, world.isClient());
                }
//                user.sendMessage(Text.of("emptied : " + getNBTFluid(itemStack)));
//                setNBTFluid(itemStack,NEOFluids.EMPTY_FLUID);
//                setFluidAmount(itemStack,0);
//                user.sendMessage(Text.of("now"+getNBTFluid(itemStack)));
            } else {
                //TAKE FLUID
                if (targetFluidState.isEmpty()) {
                    //nothin
                } else if (targetFluid instanceof NEOFluid nf) {
                    //take
//                    setNBTFluid(itemStack,nf);
//                    System.out.println("Tried to set");
//                    getNBTFluid(itemStack);
//                    System.out.println("It is " + getNBTFluid(itemStack));
//                    setNBTFluid(itemStack,Fluids.EMPTY);
//                    System.out.println("Tried to set");
                    Fluid fluid = getNBTFluid(itemStack);
                    if (fluid == NEOFluids.EMPTY_FLUID || nf.matchesType(fluid)) {
                        //can take
                        int targetAmount = FluidPhysics.getFluidLevel(targetPosition, world);
                        int howMuchCanBucketTake = (itemStack.getMaxDamage() / 125) - getCurrentLevel(itemStack);
                        if (howMuchCanBucketTake > 0 && targetAmount > 0) {
                            int transactionAmount = 0;
                            if (targetAmount <= howMuchCanBucketTake) {
                                transactionAmount = targetAmount;
                            } else {
                                transactionAmount = howMuchCanBucketTake;
                            }

                            FluidPhysics.removeFluidLevel(transactionAmount, targetPosition, world);


                            if (fluid == NEOFluids.EMPTY_FLUID) {
                                setNBTFluid(itemStack,nf);
                            }
                            addFluid(itemStack, transactionAmount);
                            playFillingSound(null,world,targetPosition);

                            return TypedActionResult.success(itemStack, world.isClient());
                        }
                    } else {
                        //any other neofluid
                    }
                } else {
                    //other types of fluid
                }
//                return TypedActionResult.fail(itemStack);
            }

            return TypedActionResult.fail(itemStack);
        }
    }

    //no need for this, i think
    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return false;
    }
//    @Override
//    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
//        if (!(this.fluid instanceof FlowableFluid flowableFluid)) {
//            return false;
//        } else {
//            Block block;
//            boolean bl;
//            BlockState blockState;
//            boolean var10000;
//            label82:
//            {
//                blockState = world.getBlockState(pos);
//                block = blockState.getBlock();
//                bl = blockState.canBucketPlace(this.fluid);
//                label70:
//                if (!blockState.isAir() && !bl) {
//                    if (block instanceof FluidFillable fluidFillable && fluidFillable.canFillWithFluid(player, world, pos, blockState, this.fluid)) {
//                        break label70;
//                    }
//
//                    var10000 = false;
//                    break label82;
//                }
//
//                var10000 = true;
//            }
//
//            boolean bl2 = var10000;
//            if (!bl2) {
//                return hitResult != null && this.placeFluid(player, world, hitResult.getBlockPos().offset(hitResult.getSide()), null);
//            } else if (world.getDimension().ultrawarm() && this.fluid.isIn(FluidTags.WATER)) {
//                int i = pos.getX();
//                int j = pos.getY();
//                int k = pos.getZ();
//                world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
//
//                for (int l = 0; l < 8; l++) {
//                    world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
//                }
//
//                return true;
//            } else {
//                if (block instanceof FluidFillable fluidFillable && this.fluid == Fluids.WATER) {
//                    fluidFillable.tryFillWithFluid(world, pos, blockState, flowableFluid.getStill(false));
//                    this.playEmptyingSound(player, world, pos);
//                    return true;
//                }
//
//                if (!world.isClient && bl && !blockState.isLiquid()) {
//                    world.breakBlock(pos, true);
//                }
//
//                if (!world.setBlockState(pos, this.fluid.getDefaultState().getBlockState(), Block.NOTIFY_ALL_AND_REDRAW) && !blockState.getFluidState().isStill()) {
//                    return false;
//                } else {
//                    this.playEmptyingSound(player, world, pos);
//                    return true;
//                }
//            }
//        }
//    }

    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
//        SoundEvent soundEvent = this.fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
    protected void playFillingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
//        SoundEvent soundEvent = this.fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos);
    }
}
