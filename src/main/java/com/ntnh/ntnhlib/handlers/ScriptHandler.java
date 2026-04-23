package com.ntnh.ntnhlib.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.common.event.FMLInterModComms;

public class ScriptHandler {

    private static final String SCRIPT_ROOT = "assets/ntnhlib/scripts/";

    public static void loadAllScripts() {
        try {
            URL location = ScriptHandler.class.getProtectionDomain()
                .getCodeSource()
                .getLocation();
            File source = new File(location.toURI());

            if (source.isDirectory()) {
                File scriptsDir = new File(source, SCRIPT_ROOT.replace('/', File.separatorChar));
                loadFromDirectory(scriptsDir, scriptsDir);
            } else {
                loadFromJar(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadFromDirectory(File root, File current) throws IOException {
        if (current == null || !current.exists()) return;

        File[] files = current.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                loadFromDirectory(root, f);
            } else if (f.getName()
                .toLowerCase(Locale.ROOT)
                .endsWith(".zs")) {
                    String relative = root.toURI()
                        .relativize(f.toURI())
                        .getPath();
                    String scriptName = "ntnhlib/" + relative.replace('\\', '/');
                    String content = readUtf8(new FileInputStream(f));
                    send(scriptName, content);
                }
        }
    }

    private static void loadFromJar(File jarFile) throws IOException {
        JarFile jar = new JarFile(jarFile);
        try {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (entry.isDirectory()) continue;
                if (!name.startsWith(SCRIPT_ROOT)) continue;
                if (!name.toLowerCase(Locale.ROOT)
                    .endsWith(".zs")) continue;

                InputStream in = jar.getInputStream(entry);
                try {
                    String relative = name.substring(SCRIPT_ROOT.length());
                    String scriptName = "ntnhlib/" + relative;
                    String content = readUtf8(in);
                    send(scriptName, content);
                } finally {
                    in.close();
                }
            }
        } finally {
            jar.close();
        }
    }

    private static void send(String scriptName, String content) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("name", scriptName);
        tag.setString("content", content);
        FMLInterModComms.sendMessage("MineTweaker3", "addMineTweakerScript", tag);
    }

    private static String readUtf8(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int len;
            while ((len = reader.read(buf)) != -1) {
                sb.append(buf, 0, len);
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }
}
