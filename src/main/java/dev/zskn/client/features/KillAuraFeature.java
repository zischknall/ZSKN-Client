package dev.zskn.client.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;

public class KillAuraFeature extends Feature {

    public KillAuraFeature() {
        super("KillAura", GLFW.GLFW_KEY_Z);
    }

    @Override
    public void onWorldTick(ClientWorld world) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (toggle && player != null) {
            Vec3d eyepos = player.getEyePos();
            if (!(player.getAttackCooldownProgress(0) >= 1.0F)) {
                return;
            }
            Vec3d vector = new Vec3d(4f, 4f, 4f);
            List<Entity> entities = world.getOtherEntities(player, new Box(eyepos.add(vector), eyepos.add(vector.negate())));
            List<Entity> filteredEntities = entities.stream().filter(entity -> (entity instanceof LivingEntity living && living.isAlive())).sorted(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(eyepos))).toList();
            if (filteredEntities.isEmpty()) {
                return;
            }
            Entity target = filteredEntities.get(0);
            if (target.squaredDistanceTo(eyepos) <= 36.0F) {
                MinecraftClient.getInstance().interactionManager.attackEntity(player, target);
            }
        }
    }
}
