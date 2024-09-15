package com.keremyurekli.mod;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;
public class NotEnoughOxygenClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientRenderer.start();
	}
}