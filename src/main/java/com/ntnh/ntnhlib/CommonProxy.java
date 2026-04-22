package com.ntnh.ntnhlib;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        NTNHLib.LOG.info(Config.greeting);
        NTNHLib.LOG.info("I am NTNHLib at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        // Temporary test command to crash the server and test witty comments
        if (Config.debug) {
            event.registerServerCommand(new CommandBase() {

                @Override
                public String getCommandName() {
                    return "testcrash";
                }

                @Override
                public String getCommandUsage(ICommandSender sender) {
                    return "/testcrash - Crashes the server to test NTNHLib crash messages";
                }

                @Override
                public void processCommand(ICommandSender sender, String[] args) {
                    MinecraftForge.EVENT_BUS.register(new Object() {

                        @SubscribeEvent
                        public void onTick(TickEvent.ServerTickEvent event) {
                            if (event.phase == TickEvent.Phase.END) {
                                MinecraftForge.EVENT_BUS.unregister(this);
                                throw new Error(
                                    "Test crash initiated by /testcrash command - check crash report for custom witty comments!");
                            }
                        }
                    });
                }
            });
        }
    }
}
