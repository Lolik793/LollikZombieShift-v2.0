package com.lollik.zombieshift;

import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameType;
import net.minecraft.particles.ParticleTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ZombieTransformation {
    private static final Map<UUID, Boolean> transformedPlayers = new HashMap<>();
    private static final Map<UUID, ZombieEntity> zombieCopies = new HashMap<>();
    private static final Map<UUID, GameType> previousGameModes = new HashMap<>();

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

        // Сохраняем предыдущий режим игры
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            previousGameModes.put(playerId, serverPlayer.gameMode.getGameModeForPlayer());
            serverPlayer.setGameMode(GameType.CREATIVE);
        }

        // Создаем обычного зомби - ресурспак сам заменит его на призрака
        ZombieEntity zombie = new ZombieEntity(player.level);
        zombie.copyPosition(player);
        zombie.setCustomName(player.getDisplayName());
        zombie.setCustomNameVisible(true);
        zombie.setInvulnerable(true);
        zombie.setSilent(true);
        zombie.setNoAi(true);

        player.level.addFreshEntity(zombie);
        zombieCopies.put(playerId, zombie);

        // Делаем игрока невидимым и призрачным
        player.setInvisible(true);
        player.setNoGravity(true);
        player.noClip = true;
        player.setInvulnerable(true);

        // Эффекты превращения
        for (int i = 0; i < 15; i++) {
            player.level.addParticle(ParticleTypes.SOUL, 
                player.getX() + (Math.random() - 0.5) * 2,
                player.getY() + Math.random() * 2,
                player.getZ() + (Math.random() - 0.5) * 2,
                0, 0.1, 0);
        }

        player.sendMessage(new StringTextComponent("§2Ты превратился в призрака! Лети сквозь стены!"), playerId);
    }

    private static void disableZombieForm(PlayerEntity player) {
        UUID playerId = player.getUUID();
        transformedPlayers.put(playerId, false);

        // Удаляем зомби-копию
        ZombieEntity zombie = zombieCopies.remove(playerId);
        if (zombie != null) {
            zombie.remove();
        }

        // Возвращаем нормальное состояние
        player.setInvisible(false);
        player.setNoGravity(false);
        player.noClip = false;
        player.setInvulnerable(false);

        // Возвращаем режим игры
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            GameType previousMode = previousGameModes.get(playerId);
            if (previousMode != null) {
                serverPlayer.setGameMode(previousMode);
            }
        }

        // Эффекты обратного превращения
        for (int i = 0; i < 15; i++) {
            player.level.addParticle(ParticleTypes.SMOKE,
                player.getX() + (Math.random() - 0.5),
                player.getY() + Math.random(),
                player.getZ() + (Math.random() - 0.5),
                0, 0.1, 0);
        }

        player.sendMessage(new StringTextComponent("§cТы снова стал человеком!"), playerId);
    }

    // Обновляем позицию зомби каждый тик
    public static void updateZombiePositions() {
        for (Map.Entry<UUID, ZombieEntity> entry : zombieCopies.entrySet()) {
            PlayerEntity player = findPlayerByUUID(entry.getKey());
            if (player != null) {
                entry.getValue().copyPosition(player);
            }
        }
    }

    private static PlayerEntity findPlayerByUUID(UUID playerId) {
        // Эта логика будет на клиенте
        return null;
    }

    public static boolean isZombie(PlayerEntity player) {
        return transformedPlayers.getOrDefault(player.getUUID(), false);
    }
}
