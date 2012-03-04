/*
 * This file is part of SpaceBukkit (http://spacebukkit.xereo.net/).
 * 
 * SpaceBukkit is free software: you can redistribute it and/or modify it under the terms of the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license as published by the Creative
 * Common organization, either version 3.0 of the license, or (at your option) any later version.
 * 
 * SpaceBukkit is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license for more details.
 * 
 * You should have received a copy of the Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA)
 * license along with this program. If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 */
package me.neatmonster.spacebukkit.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import me.neatmonster.spacebukkit.SpaceBukkit;
import me.neatmonster.spacebukkit.players.PlayerLogger;
import me.neatmonster.spacebukkit.utilities.ANSI;
import me.neatmonster.spacebukkit.utilities.PropertiesFile;
import me.neatmonster.spacebukkit.utilities.Utilities;
import me.neatmonster.spacemodule.api.Action;
import me.neatmonster.spacemodule.api.UnhandledActionException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

public class ServerActions {

    @Action(
            aliases = {"banIp", "bannedIpsAdd"})
    public boolean banIp(final String ip) {
        Bukkit.getServer().banIP(ip);
        return true;
    }

    @Action(
            aliases = {"broadcast", "broadcastMessage", "say", "tell"})
    public boolean broadcast(String message) {
        if (!message.equals("")) {
            try {
                message = URLDecoder.decode(message, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message = Utilities.color(message);
            Bukkit.getServer().broadcastMessage(message);
            try {
                PlayerLogger.addPlayerChat("Server", URLDecoder.decode(message, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"broadcastWithName", "sayWithName", "tellWithName"})
    public boolean broadcastWithName(final String name, final String message) {
        if (!name.equals("") && !message.equals("")) {
            String broadcast = ChatColor.WHITE + "[" + name + ChatColor.WHITE + "] " + message;
            try {
                broadcast = URLDecoder.decode(broadcast, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            broadcast = Utilities.color(broadcast);
            Bukkit.getServer().broadcastMessage(broadcast);
            try {
                PlayerLogger.addPlayerChat(name, URLDecoder.decode(message, "UTF-8"));
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"disablePluginsTemporarily"})
    public boolean disablePluginsTemporarily() {
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            if (!plugin.getDescription().getName().equalsIgnoreCase("SpaceBukkit")
                    && !plugin.getDescription().getName().equalsIgnoreCase("RemoteToolkitPlugin"))
                Bukkit.getPluginManager().disablePlugin(plugin);
        return true;
    }

    @Action(
            aliases = {"disablePluginTemporarily"})
    public boolean disablePluginTemporarily(final String pluginName) {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            Bukkit.getPluginManager().disablePlugin(plugin);
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"disableWhitelisting", "whitelistOff"})
    public boolean disableWhitelisting() {
        Bukkit.setWhitelist(false);
        return true;
    }

    @Action(
            aliases = {"editPropertiesFile", "propertiesFile"})
    public boolean editPropertiesFile(final String name, final String type, final String key, final String value) {
        if (new File(name + ".properties").exists()) {
            final PropertiesFile file = new PropertiesFile(name + ".properties");
            if (type.toLowerCase().equals("boolean"))
                file.setBoolean(key, Boolean.valueOf(value.toString()));
            else if (type.toLowerCase().equals("long"))
                file.setLong(key, Long.valueOf(value.toString()));
            else if (type.toLowerCase().equals("int"))
                file.setInt(key, Integer.valueOf(value.toString()));
            else if (type.toLowerCase().equals("string"))
                file.setString(key, value.toString());
            else if (type.toLowerCase().equals("double"))
                file.setDouble(key, Double.valueOf(value.toString()));
            file.save();
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"enablePluginTemporarily"})
    public boolean enablePluginTemporarily(final String pluginName) {
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            Bukkit.getPluginManager().enablePlugin(plugin);
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"enableWhitelisting", "whitelistOn"})
    public boolean enableWhitelisting() {
        Bukkit.setWhitelist(true);
        return true;
    }

    @Action(
            aliases = {"getBannedIPs", "bannedIps"})
    public String[] getBannedIPs() {
        final Set<String> ipsSet = Bukkit.getIPBans();
        final String[] ips = new String[ipsSet.size()];
        for (int a = 0; a < ipsSet.size(); a++)
            ips[a] = (String) ipsSet.toArray()[a];
        return ips;
    }

    @Action(
            aliases = {"getDisabledPlugins"})
    public List<String> getDisabledPlugins() {
        final List<String> disabledPluginsNames = new ArrayList<String>();
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            if (!plugin.isEnabled())
                disabledPluginsNames.add(plugin.getDescription().getName());
        return disabledPluginsNames;
    }

    @Action(
            aliases = {"getDynmapHost", "dynmapHost"})
    public String getDynmapHost() {
        final Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap != null)
            return dynmap.getConfig().getString("webserver-bindaddress", "0.0.0.0");
        else
            return "";
    }

    @Action(
            aliases = {"getDynmapPort", "dynmapPort"})
    public String getDynmapPort() {
        final Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap != null)
            return dynmap.getConfig().getString("webserver-port", "8123");
        else
            return "";
    }

    @Action(
            aliases = {"getItemName", "getName", "name"})
    public String getItemName(final int id) {
        String name = "";
        for (final String subname : Material.getMaterial(id).name().split("_"))
            name += subname.substring(0, 1).toUpperCase() + subname.substring(1, subname.length()).toLowerCase() + " ";
        return name.substring(0, name.length() - 1).replace("Tnt", "TNT");
    }

    @Action(
            aliases = {"getItems", "items"})
    public Map<Integer, String> getItems() {
        final LinkedHashMap<Integer, String> items = new LinkedHashMap<Integer, String>();
        for (final Material material : Material.values()) {
            String name = "";
            for (final String subname : material.name().split("_"))
                name += subname.substring(0, 1).toUpperCase() + subname.substring(1, subname.length()).toLowerCase()
                        + " ";
            items.put(material.getId(), name.substring(0, name.length() - 1).replace("Tnt", "TNT"));
        }
        return items;
    }

    @Action(
            aliases = {"getLatestChats", "latestChats"})
    public Map<Long, String> getLatestChats() {
        return PlayerLogger.getPlayersChats(50);
    }

    @Action(
            aliases = {"getLatestChatsWithLimit", "latestChatsWithLimit"})
    public Map<Long, String> getLatestChatsWithLimit(final int limit) {
        return PlayerLogger.getPlayersChats(limit);
    }

    @Action(
            aliases = {"getLatestConnections", "latestConnections"})
    public Map<Long, String> getLatestConnections() {
        return PlayerLogger.getPlayersJoins(50);
    }

    @Action(
            aliases = {"getLatestConnectionsWithLimit", "latestConnectionsWithLimit"})
    public Map<Long, String> getLatestConnectionsWithLimit(final int limit) {
        return PlayerLogger.getPlayersJoins(limit);
    }

    @Action(
            aliases = {"getLatestConsoleLogs", "latestConsoleLogs"})
    public Map<Integer, String> getLatestConsoleLogs() {
        return getLatestConsoleLogsWithLimit(50);
    }

    @Action(
            aliases = {"getLatestConsoleLogsWithLimit", "latestConsoleLogsWithLimit"})
    public TreeMap<Integer, String> getLatestConsoleLogsWithLimit(final int limit) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("server.log")));
            int size = 0;
            int loop = 0;
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
                size++;
            bufferedReader = new BufferedReader(new FileReader(new File("server.log")));
            final Map<Integer, String> lines = new HashMap<Integer, String>();
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                if (size - limit < 1) {
                    final char[] c = line.toCharArray();
                    final StringBuilder line_ = new StringBuilder(line);
                    int off = 0;
                    try {
                        for (int i = 0; i < c.length; i++)
                            if (c[i] == '[' && Character.isDigit(c[i + 1]))
                                if (Character.isDigit(c[i + 2]) && c[i + 3] == 'm') {
                                    line_.delete(i - (off + 1), i + 4 - off);
                                    off += 5;
                                } else if (c[i + 2] == 'm') {
                                    line_.delete(i - (off + 1), i + 3 - off);
                                    off += 4;
                                }
                    } catch (final IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    lines.put(loop++, ANSI.noANSI(line));
                }
                size--;
            }
            bufferedReader.close();
            return new TreeMap<Integer, String>(lines);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return new TreeMap<Integer, String>();
    }

    @Action(
            aliases = {"getLatestDeconnections", "latestDeconnections"})
    public Map<Long, String> getLatestDeconnections() {
        return PlayerLogger.getPlayersQuits(50);
    }

    @Action(
            aliases = {"getLatestDeconnectionsWithLimit", "latestDeconnectionsWithLimit"})
    public Map<Long, String> getLatestDeconnectionsWithLimit(final int limit) {
        return PlayerLogger.getPlayersQuits(limit);
    }

    @Action(
            aliases = {"getPluginInformations", "pluginInformations"})
    public LinkedHashMap<String, Object> getPluginInformations(final String pluginName) {
        final LinkedHashMap<String, Object> pluginInformations = new LinkedHashMap<String, Object>();
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            pluginInformations.put("Name", plugin.getDescription().getName());
            pluginInformations.put("IsEnabled", plugin.isEnabled());
            // pluginInformations.put("Commands", plugin.getDescription().getCommands());
            pluginInformations.put("Depend", plugin.getDescription().getDepend());
            try {
                pluginInformations.put("DataFolder", plugin.getDataFolder().getPath());
            } catch (final NullPointerException e) {
                pluginInformations.put("DataFolder", "<unknown>");
            }
            pluginInformations.put("SoftDepend", plugin.getDescription().getSoftDepend());
            pluginInformations.put("Authors", plugin.getDescription().getAuthors());
            pluginInformations.put("Description", plugin.getDescription().getDescription());
            pluginInformations.put("FullName", plugin.getDescription().getFullName());
            pluginInformations.put("Main", plugin.getDescription().getMain());
            if (plugin.getDescription().getPermissions() != null) {
                final LinkedList<LinkedHashMap<String, String>> permissions = new LinkedList<LinkedHashMap<String, String>>();
                for (final Permission permission : plugin.getDescription().getPermissions()) {
                    final LinkedHashMap<String, String> permissionInformations = new LinkedHashMap<String, String>();
                    permissionInformations.put("Name", permission.getName());
                    permissionInformations.put("Description", permission.getDescription());
                    permissionInformations.put("Default", permission.getDefault().name());
                    permissions.add(permissionInformations);
                }
                pluginInformations.put("Permissions", permissions);
            } else
                pluginInformations.put("Permissions", "[]");
            pluginInformations.put("Version", plugin.getDescription().getVersion());
            pluginInformations.put("Website", plugin.getDescription().getWebsite());
            pluginInformations.put("Bukget", SpaceBukkit.getInstance().pluginsManager.contains(pluginName));
            return pluginInformations;
        }
        return new LinkedHashMap<String, Object>();
    }

    @Action(
            aliases = {"getPlugins", "plugins"})
    public LinkedList<String> getPlugins() {
        final LinkedList<String> pluginsNames = new LinkedList<String>();
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            pluginsNames.add(plugin.getDescription().getName());
        return pluginsNames;
    }

    @Action(
            aliases = {"getServer", "server"})
    public Map<String, Object> getServer() {
        final LinkedHashMap<String, Object> serverInformations = new LinkedHashMap<String, Object>();
        final Server server = Bukkit.getServer();
        serverInformations.put("Name", server.getName());
        serverInformations.put("AllowFlight", server.getAllowFlight());
        serverInformations.put("AllowFlight", server.getAllowNether());
        serverInformations.put("DefaultGameMode", server.getDefaultGameMode().toString());
        serverInformations.put("MaxPlayers", server.getMaxPlayers());
        serverInformations.put("OnlineMode", server.getOnlineMode());
        serverInformations.put("Port", server.getPort());
        serverInformations.put("ServerId", server.getServerId());
        serverInformations.put("ServerName", server.getServerName());
        serverInformations.put("SpawnRadius", server.getSpawnRadius());
        serverInformations.put("UpdateFolder", server.getUpdateFolder());
        serverInformations.put("Version", server.getVersion());
        serverInformations.put("ViewDistance", server.getViewDistance());
        serverInformations.put("HasWhitelist", server.hasWhitelist());
        serverInformations.put("OnlinePlayers", server.getOnlinePlayers().length);
        return serverInformations;
    }

    @Action(
            aliases = {"getWorldInformations", "worldInformations"})
    public LinkedHashMap<String, Object> getWorldInformations(final String worldName) {
        final LinkedHashMap<String, Object> worldInformations = new LinkedHashMap<String, Object>();
        final World world = Bukkit.getWorld(worldName);
        if (world != null) {
            worldInformations.put("Name", world.getName());
            worldInformations.put("AllowAnimals", world.getAllowAnimals());
            worldInformations.put("AllowMonsters", world.getAllowMonsters());
            worldInformations.put("Difficulty", world.getDifficulty().toString());
            worldInformations.put("Environment", world.getEnvironment().toString());
            worldInformations.put("FullTime", world.getFullTime());
            worldInformations.put("KeepSpawnInMemory", world.getKeepSpawnInMemory());
            worldInformations.put("MaxHeight", world.getMaxHeight());
            worldInformations.put("PVP", world.getPVP());
            worldInformations.put("SeaLevel", world.getSeaLevel());
            worldInformations.put("Seed", world.getSeed());
            worldInformations.put("ThunderDuration", world.getThunderDuration());
            worldInformations.put("Time", world.getTime());
            worldInformations.put("FullTime", world.getFullTime());
            worldInformations.put("FormattedTime", Utilities.formatTime(world.getTime()));
            worldInformations.put("WeatherDuration", world.getWeatherDuration());
            return worldInformations;
        }
        return new LinkedHashMap<String, Object>();
    }

    @Action(
            aliases = {"getWorlds", "worlds"})
    public List<String> getWorlds() {
        final List<String> worldsNames = new ArrayList<String>();
        for (final World world : Bukkit.getWorlds())
            worldsNames.add(world.getName());
        return worldsNames;
    }

    @Action(
            aliases = {"isSchedulable", "schedulable"})
    public boolean isSchedulable(final String actionName) {
        try {
            return SpaceBukkit.getInstance().actionsManager.isSchedulable(actionName);
        } catch (final UnhandledActionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Action(
            aliases = {"reload", "reloadServer"})
    public boolean reload() {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SpaceBukkit.getInstance(), new Runnable() {
            @Override
            public void run() {
                Bukkit.reload();
            }
        }, 20L);
        return true;
    }

    @Action(
            aliases = {"saveMap", "save"})
    public boolean saveMap() {
        for (final World world : Bukkit.getWorlds())
            world.save();
        return true;
    }

    @Action(
            aliases = {"saveOff"})
    public boolean saveOff() {
        for (final World world : Bukkit.getWorlds())
            world.setAutoSave(false);
        return true;
    }

    @Action(
            aliases = {"saveOn"})
    public boolean saveOn() {
        for (final World world : Bukkit.getWorlds())
            world.setAutoSave(true);
        return true;
    }

    @Action(
            aliases = {"setWorldWeather", "worldWeather", "weather"})
    public boolean setWorldWeather(final String worldName, final Boolean storm, final Boolean thunder) {
        final World world = Bukkit.getWorld(worldName);
        if (world == null)
            return false;
        world.setStorm(storm);
        world.setThundering(thunder);
        return true;
    }

    @Action(
            aliases = {"unbanIp", "bannedIpsRemove"})
    public boolean unbanIp(final String ip) {
        if (!ip.equals("")) {
            Bukkit.getServer().unbanIP(ip);
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"wasThereAConnection", "connections"})
    public boolean wasThereAConnection(final int time) {
        return !(PlayerLogger.getLastJoin() == 0 && PlayerLogger.getLastQuit() == 0 || PlayerLogger.getLastJoin() < System
                .currentTimeMillis() - time * 1000
                && PlayerLogger.getLastQuit() < System.currentTimeMillis() - time * 1000);
    }
}
