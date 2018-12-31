package org.sdoaj.bellarminecraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.sdoaj.bellarminecraft.eventhandlers.SpawnProtector;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, acceptableRemoteVersions = "*")
public class Main {
    public static final String MODID = "bellarminecraft";
    public static final String NAME = "Bellarminecraft";
    public static final String VERSION = "1.0";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new SpawnProtector());
    }
}
