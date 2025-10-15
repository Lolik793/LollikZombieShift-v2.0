package com.lollik.zombieshift;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lollikzombieshift")
public class LollikZombieShift {
    
    public LollikZombieShift() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        KeyInputHandler.register();
    }
}
