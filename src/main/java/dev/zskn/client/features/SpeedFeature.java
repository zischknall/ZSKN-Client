package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerAbilities;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class SpeedFeature extends Feature {

    private final EntityAttributeModifier walkModifier = new EntityAttributeModifier(UUID.randomUUID(), "speedhack", 2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public SpeedFeature() {
        super("Speed", GLFW.GLFW_KEY_K);
    }

    @Override
    public void onWorldTick(ClientWorld world) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            PlayerAbilities abilities = player.getAbilities();
            EntityAttributeInstance movementSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            assert movementSpeed != null;
            if (toggle) {
                float speedFlySpeed = 0.15f;
                if (abilities.getFlySpeed() != speedFlySpeed) {
                    abilities.setFlySpeed(speedFlySpeed);
                }

                if (!movementSpeed.hasModifier(walkModifier)) {
                    movementSpeed.addPersistentModifier(walkModifier);
                }
            } else {
                float defaultFlySpeed = 0.05f;
                if (abilities.getFlySpeed() != defaultFlySpeed) {
                    abilities.setFlySpeed(defaultFlySpeed);
                }

                if (movementSpeed.hasModifier(walkModifier)) {
                    movementSpeed.removeModifier(walkModifier.getId());
                }
            }
        }
    }
}
