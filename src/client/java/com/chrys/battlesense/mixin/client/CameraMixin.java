package com.chrys.battlesense.mixin.client;

import com.chrys.battlesense.event.FreelookHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class CameraMixin {

    /**
     * TRICK 1: Force Third-Person Mode
     * The 'update' method takes a 'thirdPerson' boolean argument.
     * We modify that argument to be ALWAYS TRUE when freelook is active.
     */
    @ModifyVariable(method = "update", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private boolean forceThirdPerson(boolean thirdPerson) {
        if (FreelookHandler.isActive) {
            return true; // Force the math to use third-person logic
        }
        return thirdPerson; // Otherwise, use the real game setting
    }

    /**
     * TRICK 2: Force "Back" View (Not Front View)
     * The next argument is 'inverseView' (f5 front view). We force it to false.
     */
    @ModifyVariable(method = "update", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private boolean forceNoInverseView(boolean inverseView) {
        if (FreelookHandler.isActive) {
            return false; // Force it to look at the back of the player
        }
        return inverseView;
    }

    /**
     * TRICK 3: Hijack the Yaw Calculation
     * The camera calls 'entity.getYaw()' to figure out where to position itself.
     * We redirect that call to use OUR custom camera yaw.
     */
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getYaw(F)F"))
    private float onGetYaw(Entity instance, float tickDelta) {
        if (FreelookHandler.isActive) {
            return FreelookHandler.cameraYaw;
        }
        return instance.getYaw(tickDelta);
    }

    /**
     * TRICK 4: Hijack the Pitch Calculation
     * Same as above, but for pitch.
     */
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPitch(F)F"))
    private float onGetPitch(Entity instance, float tickDelta) {
        if (FreelookHandler.isActive) {
            return FreelookHandler.cameraPitch;
        }
        return instance.getPitch(tickDelta);
    }
}