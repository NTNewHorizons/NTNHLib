package com.ntnh.ntnhlib.minetweaker;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.reflect.ClassPath;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;
import java.util.Set;

public class MT {
    public static void loadMineTweakerScripts() {
        if (Loader.isModLoaded("MineTweaker3")) {
            try {
                ClassLoader classLoader = MT.class.getClassLoader();  // Use MT.class here
                ClassPath classPath = ClassPath.from(classLoader);
                Set<ClassPath.ResourceInfo> resources = classPath.getResources();

                for (ClassPath.ResourceInfo resource : resources) {
                    String path = resource.getResourceName();
                    if (path.startsWith("minetweaker/") && path.endsWith(".zs")) {
                        try {
                            String data = Resources.toString(resource.url(), Charsets.UTF_8);
                            String scriptName = path.substring("minetweaker/".length());  // Use relative path as name to avoid conflicts

                            NBTTagCompound nbtData = new NBTTagCompound();
                            nbtData.setString("name", scriptName);
                            nbtData.setString("content", data);
                            FMLInterModComms.sendMessage("MineTweaker3", "addMineTweakerScript", nbtData);
                        } catch (IOException ex) {
                            ex.printStackTrace();  // Handle individual file errors without stopping
                        }
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();  // For ClassPath initialization errors
            }
        }
    }
}