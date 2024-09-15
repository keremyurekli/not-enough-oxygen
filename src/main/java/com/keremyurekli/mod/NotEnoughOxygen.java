package com.keremyurekli.mod;

import com.keremyurekli.mod.blocks.NEOBlocks;
import com.keremyurekli.mod.fluids.NEOFluidEvents;
import com.keremyurekli.mod.items.NEOItemGroup;
import com.keremyurekli.mod.fluids.NEOFluids;
import com.keremyurekli.mod.items.NEOItems;
import com.keremyurekli.mod.util.Scheduler;
import net.fabricmc.api.ModInitializer;

public class NotEnoughOxygen implements ModInitializer {

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	//public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		long startTime = System.currentTimeMillis();
		Static.LOGGER.info("Starting to initialize");

		Scheduler.init();

		NEOFluids.registerAll();
		NEOBlocks.registerAll();

		NEOFluids.registerFluidAttributes();

//		NEOBlockEntities.registerAll();

		NEOItems.registerAll();
		NEOFluidEvents.registerAll();

		NEOItemGroup.registerAll();

		Static.LOGGER.info("Initialization took {} ms", System.currentTimeMillis() - startTime);
	}
}