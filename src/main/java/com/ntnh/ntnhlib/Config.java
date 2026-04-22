package com.ntnh.ntnhlib;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Hello World";
    public static boolean debug = false;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");
        debug = configuration
            .getBoolean("debug", Configuration.CATEGORY_GENERAL, debug, "Enable debug features like test commands");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
