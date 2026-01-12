package com.chrys.battlesense;

import com.chrys.battlesense.event.FreelookHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BattleSenseClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// 1. Register the Keybinding
		FreelookHandler.register();

		// 2. Register the Tick Loop
		// "Every time the client finishes a tick, run this code."
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			FreelookHandler.tick();
		});
	}
}