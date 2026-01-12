package com.ntnh.ntnhlib.minetweaker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.Resources;
import com.ntnh.ntnhlib.ntnhlib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MT {

    static {
        if (Loader.isModLoaded("MineTweaker3")) {
            loadAllScripts();
        }
    }

    private static void loadAllScripts() {
        try {
            URL resourceDir = ntnhlib.class.getResource("/minetweaker/");
            if (resourceDir != null) {
                File dir = new File(resourceDir.toURI());
                if (dir.exists() && dir.isDirectory()) {
                    File[] files = dir.listFiles((d, name) -> name.endsWith(".zs"));
                    if (files != null) {
                        List<String> scriptNames = new ArrayList<>();
                        for (File file : files) {
                            scriptNames.add(file.getName());
                        }
                        for (String scriptName : scriptNames) {
                            try {
                                String resourcePath = "/minetweaker/" + scriptName;
                                URL resource = ntnhlib.class.getResource(resourcePath);

                                if (resource != null) {
                                    String content = Resources.toString(resource, StandardCharsets.UTF_8);
                                    sendScript(scriptName, content);
                                }
                            } catch (IOException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendScript(String name, String content) {
        NBTTagCompound nbtData = new NBTTagCompound();
        nbtData.setString("name", name);
        nbtData.setString("content", content);
        FMLInterModComms.sendMessage("MineTweaker3", "addMineTweakerScript", nbtData);
    }
}
