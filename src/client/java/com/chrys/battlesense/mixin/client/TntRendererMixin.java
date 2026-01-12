package com.chrys.battlesense.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntityRenderer.class)
public abstract class TntRendererMixin extends EntityRenderer<TntEntity> {

    // Constructor required to match the superclass structure,
    // though it is technically ignored by the Mixin processor.
    protected TntRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("TAIL"))
    public void renderTimer(TntEntity tntEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        // 1. Get the fuse time (in ticks)
        int fuse = tntEntity.getFuse();

        // 2. Format the time to seconds (e.g., "3.50s")
        // We divide by 20.0f because there are 20 ticks in a second.
        float time = fuse / 20.0f;
        String timeString = String.format("%.2f", time);

        // 3. Determine color based on urgency
        MutableText label = Text.literal(timeString);

        // 4. Apply Styles (Color + Bold) based on urgency
        if (time < 1) {
            // Less than 1 second: RED + BOLD
            label.formatted(Formatting.RED, Formatting.BOLD);
        } else if (time < 2) {
            // Less than 2 seconds: YELLOW + BOLD
            label.formatted(Formatting.YELLOW, Formatting.BOLD);
        } else {
            // Normal: GREEN
            label.formatted(Formatting.GREEN);
        }

        // 5. Use the built-in method to render the name tag
        // renderLabelIfPresent handles the "billboard" effect (facing the player)
        // and positioning it above the entity's hitbox.
        this.renderLabelIfPresent(tntEntity, label, matrices, vertexConsumers, light, tickDelta);
    }
}