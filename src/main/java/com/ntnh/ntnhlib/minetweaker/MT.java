package com.ntnh.ntnhlib.minetweaker;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MT {

    public static void loadMineTweakerScripts() {
        if (Loader.isModLoaded("MineTweaker3")) {
            try {
                // Get the resource URL for the minetweaker directory
                // Change ExampleMod.class to your actual main mod class
                URL resourceUrl = MT.class.getResource("/minetweaker");

                if (resourceUrl != null) {
                    // Convert URL to URI to handle spaces and special characters properly
                    URI uri = resourceUrl.toURI();
                    Path minetweakerPath;

                    // Handle both file system and JAR resources
                    if (uri.getScheme()
                        .equals("jar")) {
                        // Running from JAR
                        FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                        minetweakerPath = fileSystem.getPath("/minetweaker");
                    } else {
                        // Running from file system (development environment)
                        minetweakerPath = Paths.get(uri);
                    }

                    // Walk through the directory and find all .zs files
                    Files.walk(minetweakerPath)
                        .filter(
                            path -> path.toString()
                                .endsWith(".zs"))
                        .forEach(path -> {
                            try {
                                String scriptName = minetweakerPath.relativize(path)
                                    .toString();
                                String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

                                NBTTagCompound nbtData = new NBTTagCompound();
                                nbtData.setString("name", scriptName);
                                nbtData.setString("content", content);
                                FMLInterModComms.sendMessage("MineTweaker3", "addMineTweakerScript", nbtData);

                                System.out.println("Loaded MineTweaker script: " + scriptName);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
