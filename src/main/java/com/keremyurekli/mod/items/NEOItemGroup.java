package com.keremyurekli.mod.items;

import com.keremyurekli.mod.Static;
import com.keremyurekli.mod.blocks.NEOBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NEOItemGroup {

    public static void registerAll() {
        Registry.register(Registries.ITEM_GROUP, MAIN_ITEM_GROUP_KEY, MAIN_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(MAIN_ITEM_GROUP_KEY).register(itemGroup -> {
            for (int i = 0; i < NEOItems.ITEMS.size(); i++) {
                itemGroup.add(NEOItems.ITEMS.get(i).asItem().getDefaultStack());
            }
            for (int i = 0; i < NEOBlocks.BLOCKS.size(); i++) {
                itemGroup.add(NEOBlocks.BLOCKS.get(i).asItem().getDefaultStack());
            }

        });
    }
    public static final RegistryKey<ItemGroup> MAIN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Static.MOD_ID, Static.MAIN_ITEM_GROUP));

    public static final ItemGroup MAIN_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(NEOItems.MOD_LOGO))
            .displayName(Text.literal(Static.MOD_NAME))
            .build();
}
