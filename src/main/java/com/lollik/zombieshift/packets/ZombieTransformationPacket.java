package com.lollik.zombieshift.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ZombieTransformationPacket {
    private final UUID playerId;
    private final boolean transformed;

    public ZombieTransformationPacket(UUID playerId, boolean transformed) {
        this.playerId = playerId;
        this.transformed = transformed;
    }

    public static void encode(ZombieTransformationPacket msg, PacketBuffer buffer) {
        buffer.writeUUID(msg.playerId);
        buffer.writeBoolean(msg.transformed);
    }

    public static ZombieTransformationPacket decode(PacketBuffer buffer) {
        return new ZombieTransformationPacket(buffer.readUUID(), buffer.readBoolean());
    }

    public static void handle(ZombieTransformationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Клиентская логика для синхронизации
        });
        ctx.get().setPacketHandled(true);
    }
}package com.lollik.zombieshift.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ZombieTransformationPacket {
    private final UUID playerId;
    private final boolean transformed;

    public ZombieTransformationPacket(UUID playerId, boolean transformed) {
        this.playerId = playerId;
        this.transformed = transformed;
    }

    public static void encode(ZombieTransformationPacket msg, PacketBuffer buffer) {
        buffer.writeUUID(msg.playerId);
        buffer.writeBoolean(msg.transformed);
    }

    public static ZombieTransformationPacket decode(PacketBuffer buffer) {
        return new ZombieTransformationPacket(buffer.readUUID(), buffer.readBoolean());
    }

    public static void handle(ZombieTransformationPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Клиентская логика для синхронизации
        });
        ctx.get().setPacketHandled(true);
    }
}
