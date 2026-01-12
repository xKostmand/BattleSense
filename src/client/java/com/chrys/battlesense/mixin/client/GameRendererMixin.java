package com.chrys.battlesense.mixin.client;

import com.chrys.battlesense.event.FreelookHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    /**
     * Intercepts the rendering of the first-person hand models.
     * If Freelook is active (which forces third-person visually anyway),
     * we ensure the hands don't render.
     */
    @Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    private void onRenderHand(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        if (FreelookHandler.isActive) {
            ci.cancel();
        }
    }
}