package com.ntnh.ntnhlib.minetweaker;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.Resources;
import com.ntnh.ntnhlib.ntnhlib;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MT {

    static {
        if (Loader.isModLoaded("MineTweaker3")) {
            // Автоматически загружаем все скрипты из minetweaker/
            loadAllScripts();
        }
    }

    private static void loadAllScripts() {
        try {
            // Пытаемся получить список файлов через ресурсы
            String[] defaultScripts = { "recipes.zs", "ore_processing.zs", "tools.zs", "machines.zs", "mobs.zs" };

            for (String scriptName : defaultScripts) {
                try {
                    String resourcePath = "/minetweaker/" + scriptName;
                    URL resource = ntnhlib.class.getResource(resourcePath);

                    if (resource != null) {
                        String content = Resources.toString(resource, StandardCharsets.UTF_8);
                        sendScript(scriptName, content);
                    }
                } catch (IOException ignored) {
                    // Файл не существует - пропускаем
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
