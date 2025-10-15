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

        ZombieEntity zombie = new ZombieEntity(player.level);
        zombie.copyPosition(player);
        zombie.setCustomName(player.getDisplayName());
        zombie.setCustomNameVisible(true);
        zombie.setInvulnerable(true);
        zombie.setSilent(true);
        zombie.setNoAi(true);

        player.level.addFreshEntity(zombie);
        zombieCopies.put(playerId, zombie);

        player.setInvisible(true);
        player.setNoGravity(true);
        player.noClip = true;
        player.setInvulnerable(true);

        player.sendMessage(new StringTextComponent("§2Режим зомби активирован!"), playerId);
    }

    private static void disableZombieForm(PlayerEntity player) {
        UUID playerId = player.getUUID();
        transformedPlayers.put(playerId, false);

        ZombieEntity zombie = zombieCopies.remove(playerId);
        if (zombie != null) {
            zombie.remove();
        }

        player.setInvisible(false);
        player.setNoGravity(false);
        player.noClip = false;
        player.setInvulnerable(false);

        player.sendMessage(new StringTextComponent("§cРежим зомби деактивирован!"), playerId);
    }

    public static boolean isZombie(PlayerEntity player) {
        return transformedPlayers.getOrDefault(player.getUUID(), false);
    }
}
