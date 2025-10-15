package com.lollik.zombieshift;

import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZombieTransformation {
    private static final Map<UUID, Boolean> transformedPlayers = new HashMap<>();
    private static final Map<UUID, ZombieEntity> zombieCopies = new HashMap<>();

    public static void toggleZombieForm(PlayerEntity player) {
        UUID playerId = player.getUUID();
        boolean isZombie = transformedPlayers.getOrDefault(playerId, false);

        if (!isZombie) {
            enableZombieForm(player);
        } else {
            disableZombieForm(player);
        }
    }

    private static void enableZombieForm(PlayerEntity player) {
        UUID playerId = player.getUUID();
        transformedPlayers.put(playerId, true);

        // Создаем зомби-копию для визуального эффекта (по желанию)
        ZombieEntity zombie = new ZombieEntity(player.level);
        zombie.copyPosition(player);
        zombie.setCustomName(player.getDisplayName());
        zombie.setCustomNameVisible(true);
        zombie.setInvulnerable(true);
        zombie.setSilent(true);
        zombie.setNoAi(true);
        zombie.setInvisible(true); // Делаем зомби невидимым, если хочешь только способности

        player.level.addFreshEntity(zombie);
        zombieCopies.put(playerId, zombie);

        // Даем способности, но игрок остается ВИДИМЫМ
        player.setNoGravity(true);  // Может летать
        player.noClip = true;       // Может проходить сквозь стены
        player.setInvulnerable(true); // Неуязвимость

        // Добавляем эффекты для красоты
        player.addEffect(new EffectInstance(Effects.NIGHT_VISION, 1000000, 0, false, false));
        
        // Визуальные эффекты вокруг игрока
        for (int i = 0; i < 10; i++) {
            player.level.addParticle(ParticleTypes.SOUL, 
                player.getX() + (Math.random() - 0.5) * 2,
                player.getY() + Math.random() * 2,
                player.getZ() + (Math.random() - 0.5) * 2,
                0, 0.1, 0);
        }

        player.sendMessage(new StringTextComponent("§2Режим зомби активирован! Лети сквозь стены!"), playerId);
    }

    private static void disableZombieForm(PlayerEntity player) {
        UUID playerId = player.getUUID();
        transformedPlayers.put(playerId, false);

        // Удаляем зомби-копию
        ZombieEntity zombie = zombieCopies.remove(playerId);
        if (zombie != null) {
            zombie.remove();
        }

        // Возвращаем нормальные свойства
        player.setNoGravity(false);
        player.noClip = false;
        player.setInvulnerable(false);
        
        // Убираем эффекты
        player.removeEffect(Effects.NIGHT_VISION);

        // Эффекты при возвращении
        for (int i = 0; i < 10; i++) {
            player.level.addParticle(ParticleTypes.SMOKE,
                player.getX() + (Math.random() - 0.5),
                player.getY() + Math.random(),
                player.getZ() + (Math.random() - 0.5),
                0, 0.1, 0);
        }

        player.sendMessage(new StringTextComponent("§cРежим зомби деактивирован!"), playerId);
    }

    public static boolean isZombie(PlayerEntity player) {
        return transformedPlayers.getOrDefault(player.getUUID(), false);
    }
}
