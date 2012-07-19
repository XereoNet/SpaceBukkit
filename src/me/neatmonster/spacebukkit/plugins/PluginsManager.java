/*
 * This file is part of SpaceBukkit (http://spacebukkit.xereo.net/).
 *
 * SpaceBukkit is free software: you can redistribute it and/or modify it under the terms of the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license as published by the Creative Common organization,
 * either version 3.0 of the license, or (at your option) any later version.
 *
 * SpaceBukkit is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Attribution-NonCommercial-ShareAlike
 * Unported (CC BY-NC-SA) license for more details.
 *
 * You should have received a copy of the Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license along with
 * this program. If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 */
package me.neatmonster.spacebukkit.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import me.neatmonster.spacebukkit.SpaceBukkit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages Plugins and interacts with BukGet
 */
public class PluginsManager {
    public static final File JARS_FILE = new File("SpaceModule" + File.separator + "SpaceBukkit",
        "jars.yml");
    
    public static List<String> pluginsNames = new ArrayList<String>();

    /**
     * Gets the Jar of a plugin
     * @param plugin
     * @return Jar File the plugin's code is contained in
     */
    public static File getJAR(final Plugin plugin) {
        Class<?> currentClass = plugin.getClass();
        while (!(currentClass.equals(JavaPlugin.class))) {
            currentClass = currentClass.getSuperclass();
        }
        try {
            final Class<?>[] methodArgs = {};
            final Method method = currentClass.getDeclaredMethod("getFile", methodArgs);
            method.setAccessible(true);
            final Object[] classArgs = {};
            return (File) method.invoke(plugin, classArgs);
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new PluginsManager
     */
    public PluginsManager() {
        new Thread(new PluginsRequester()).start();
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(JARS_FILE);
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin == null) {
        	continue;
            }
            final File jar = getJAR(plugin);
            if (jar != null)
                configuration.set(plugin.getDescription().getName().toLowerCase().replace(" ", ""),
                        jar.getName());
        }
        try {
            configuration.save(JARS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the manager knows a plugins JAR location
     * @param pluginName Plugin to check
     * @return If the manager knows where a plugins JAR is
     */
    public boolean contains(String pluginName) {
        pluginName = pluginName.toLowerCase();
        if (pluginsNames.contains(pluginName))
            return true;
        if (pluginsNames.contains(pluginName.replace(" ", "")))
            return true;
        if (pluginsNames.contains(pluginName.replace(" ", "_")))
            return true;
        if (pluginsNames.contains(pluginName.replace(" ", "-")))
            return true;
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(JARS_FILE);
            final File jar = getJAR(plugin);
            if (jar != null)
                configuration.set(plugin.getDescription().getName().toLowerCase().replace(" ", ""),
                        jar.getName());
            try {
                configuration.save(JARS_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
