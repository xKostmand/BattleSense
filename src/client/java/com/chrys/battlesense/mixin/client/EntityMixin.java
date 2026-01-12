package com.chrys.battlesense.mixin.client;

import com.chrys.battlesense.event.FreelookHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 1. Target "Entity" because that is where 'changeLookDirection' is defined.
@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void onTurn(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        // 2. IMPORTANT: Only run this logic for the YOUR player (ClientPlayerEntity)
        // Since we are targeting "Entity", this code technically runs for Cows, Pigs, etc.
        // The check below ensures we only hijack the camera for the local player.
        if ((Object) this instanceof ClientPlayerEntity) {

            if (FreelookHandler.isActive) {
                // 3. Update Virtual Camera
                FreelookHandler.cameraYaw += (float) cursorDeltaX * 0.15f;
                FreelookHandler.cameraPitch += (float) cursorDeltaY * 0.15f;

                // Clamp Pitch (-90 to 90)
                FreelookHandler.cameraPitch = Math.max(-90.0F, Math.min(90.0F, FreelookHandler.cameraPitch));

                // 4. Stop the real player from turning
                ci.cancel();
            }
        }
    }
}