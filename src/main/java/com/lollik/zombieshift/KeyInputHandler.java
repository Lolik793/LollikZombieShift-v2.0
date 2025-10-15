package com.lollik.zombieshift;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class KeyInputHandler {
    private static final String CATEGORY = "key.category.zombieshift";
    private static final KeyBinding ZOMBIE_KEY = new KeyBinding(
        "key.zombieshift.transform",
        GLFW.GLFW_KEY_R,
        CATEGORY
    );

    public static void register() {
        ClientRegistry.registerKeyBinding(ZOMBIE_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ZOMBIE_KEY.isDown()) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                ZombieTransformation.toggleZombieForm(player);
            }
        }
    }
}
