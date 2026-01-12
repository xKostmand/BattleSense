package com.chrys.battlesense.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class FreelookHandler {

    // Toggle State
    public static boolean isActive = false;

    // The "Virtual" Camera Angles
    public static float cameraYaw = 0f;
    public static float cameraPitch = 0f;

    // The Keybind (Default: Left Alt)
    public static KeyBinding keyBinding;

    public static void register() {
        keyBinding = new KeyBinding(
                "Freelook",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "BattleSense"
        );
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Logic: When key is HELD, mode is active
        if (keyBinding.isPressed()) {
            if (!isActive) {
                // JUST ENABLED: Sync virtual camera to real player
                isActive = true;
                cameraYaw = client.player.getYaw();
                cameraPitch = client.player.getPitch();
            }
        } else {
            if (isActive) {
                // JUST DISABLED: Reset
                isActive = false;
            }
        }
    }
}
