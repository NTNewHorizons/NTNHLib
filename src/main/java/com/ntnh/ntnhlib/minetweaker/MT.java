package com.ntnh.ntnhlib.minetweaker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.Resources;
import com.ntnh.ntnhlib.ntnhlib;

import cpw.mods.fml.common.event.FMLInterModComms;

public class MT {

    public static void loadAllScripts() {
        try {
            // Try to get resources from the classpath
            Enumeration<URL> resources = ntnhlib.class.getClassLoader()
                .getResources("minetweaker/");

            if (resources.hasMoreElements()) {
                while (resources.hasMoreElements()) {
                    URL resourceUrl = resources.nextElement();
                    loadScriptsFromUrl(resourceUrl);
                }
            } else {
                // Fallback: try direct file access if resources aren't found
                File resourceDir = new File(
                    ntnhlib.class.getResource("/")
                        .toURI());
                File minetweakerDir = new File(resourceDir, "minetweaker");
                if (minetweakerDir.exists() && minetweakerDir.isDirectory()) {
                    loadScriptsFromFileSystem(minetweakerDir);
                }
            }
        } catch (Exception e) {
            ntnhlib.LOG.error("Error loading MineTweaker scripts", e);
        }
    }

    private static void loadScriptsFromUrl(URL resourceUrl) {
        try {
            if (resourceUrl.getProtocol()
                .equals("jar")) {
                // Handle JAR files
                String jarPath = resourceUrl.getPath()
                    .substring(
                        5,
                        resourceUrl.getPath()
                            .indexOf("!"));
                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName()
                            .startsWith("minetweaker/")
                            && entry.getName()
                                .endsWith(".zs")) {
                            String scriptName = entry.getName()
                                .substring("minetweaker/".length());
                            try {
                                URL scriptUrl = new URL("jar:" + jarPath + "!/" + entry.getName());
                                String content = Resources.toString(scriptUrl, StandardCharsets.UTF_8);
                                sendScript(scriptName, content);
                            } catch (IOException ignored) {}
                        }
                    }
                }
            } else {
                // Handle file system
                File dir = new File(resourceUrl.toURI());
                if (dir.exists() && dir.isDirectory()) {
                    loadScriptsFromFileSystem(dir);
                }
            }
        } catch (Exception e) {
            ntnhlib.LOG.error("Error loading scripts from URL: " + resourceUrl, e);
        }
    }

    private static void loadScriptsFromFileSystem(File dir) {
        File[] files = dir.listFiles((d, name) -> name.endsWith(".zs"));
        if (files != null) {
            for (File file : files) {
                try {
                    String scriptName = file.getName();
                    String content = Resources.toString(
                        file.toURI()
                            .toURL(),
                        StandardCharsets.UTF_8);
                    sendScript(scriptName, content);
                } catch (IOException ignored) {}
            }
        }
    }

    private static void sendScript(String name, String content) {
        NBTTagCompound nbtData = new NBTTagCompound();
        nbtData.setString("name", name);
        nbtData.setString("content", content);
        FMLInterModComms.sendMessage("MineTweaker3", "addMineTweakerScript", nbtData);
    }
}
