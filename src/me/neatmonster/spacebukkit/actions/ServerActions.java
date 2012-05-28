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
import java.util.Arrays;
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
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicesManager;

public class ServerActions {

    /**
     * Bans an IP
     * @param ip Ip to ban
     * @return If successful
     */
    @Action(
            aliases = {"banIp", "bannedIpsAdd"})
    public boolean banIp(final String ip) {
        Bukkit.getServer().banIP(ip);
        return true;
    }

    /**
     * Broadcasts a message
     * @param message Message to broadcast
     * @return If successful
     * @throws UnsupportedEncodingException If UTF-8 is not supported
     */
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

    /**
     * Broadcasts a message with a name
     * @param name Name to use
     * @param message Message to broadcast
     * @return If successful
     * @throws UnsupportedEncodingException If UTF-8 is not supported
     */
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

    /**
     * Disables all plugins temporarily
     * @return If successful
     */
    @Action(
            aliases = {"disablePluginsTemporarily"})
    public boolean disablePluginsTemporarily() {
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            if (!plugin.getDescription().getName().equalsIgnoreCase("SpaceBukkit")
                    && !plugin.getDescription().getName().equalsIgnoreCase("RemoteToolkitPlugin"))
                Bukkit.getPluginManager().disablePlugin(plugin);
        return true;
    }

    /**
     * Disables a plugin temporarily
     * @param pluginName Plugin to disable
     * @return If successful
     */
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

    /**
     * Disables the whitelist
     * @return If successful
     */
    @Action(
            aliases = {"disableWhitelisting", "whitelistOff"})
    public boolean disableWhitelisting() {
        Bukkit.setWhitelist(false);
        return true;
    }

    /**
     * Edits a property file
     * @param name Name of the file
     * @param type Type of value
     * @param key Key to edit
     * @param value Value to set as
     * @return If successful
     */
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

    /**
     * Enables a plugin temporarily
     * @param pluginName Plugin to enable
     * @return If successful
     */
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

    /**
     * Enables whitelisting
     * @return If successful
     */
    @Action(
            aliases = {"enableWhitelisting", "whitelistOn"})
    public boolean enableWhitelisting() {
        Bukkit.setWhitelist(true);
        return true;
    }

    /**
     * Gets all banned IP's
     * @return All banned IP's
     */
    @Action(
            aliases = {"getBannedIPs", "bannedIps"})
    public String[] getBannedIPs() {
        final Set<String> ipsSet = Bukkit.getIPBans();
        final String[] ips = new String[ipsSet.size()];
        for (int a = 0; a < ipsSet.size(); a++)
            ips[a] = (String) ipsSet.toArray()[a];
        return ips;
    }

    /**
     * Gets all disabled plugins
     * @return Disabled plugins
     */
    @Action(
            aliases = {"getDisabledPlugins"})
    public List<String> getDisabledPlugins() {
        final List<String> disabledPluginsNames = new ArrayList<String>();
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            if (!plugin.isEnabled())
                disabledPluginsNames.add(plugin.getDescription().getName());
        return disabledPluginsNames;
    }

    /**
     * Gets the Dynmap host
     * @return Dynmap host
     */
    @Action(
            aliases = {"getDynmapHost", "dynmapHost"})
    public String getDynmapHost() {
        final Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap != null)
            return dynmap.getConfig().getString("webserver-bindaddress", "0.0.0.0");
        else
            return "";
    }

    /**
     * Gets the Dynmap port
     * @return Dynmap port
     */
    @Action(
            aliases = {"getDynmapPort", "dynmapPort"})
    public String getDynmapPort() {
        final Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
        if (dynmap != null)
            return dynmap.getConfig().getString("webserver-port", "8123");
        else
            return "";
    }

    /**
     * Gets the name of an Item
     * @param id Id to get
     * @return Item name
     */
    @Action(
            aliases = {"getItemName", "getName", "name"})
    public String getItemName(final int id) {
        String name = "";
        for (final String subname : Material.getMaterial(id).name().split("_"))
            name += subname.substring(0, 1).toUpperCase() + subname.substring(1, subname.length()).toLowerCase() + " ";
        return name.substring(0, name.length() - 1).replace("Tnt", "TNT");
    }

    /**
     * Gets all item names
     * @return Item names
     */
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

    /**
     * Gets the latest chats
     * @return Latest chats
     */
    @Action(
            aliases = {"getLatestChats", "latestChats"})
    public Map<Long, String> getLatestChats() {
        return PlayerLogger.getPlayersChats(50);
    }

    /**
     * Gets the latest chat with a limit
     * @param limit Number of chats to include
     * @return Chats
     */
    @Action(
            aliases = {"getLatestChatsWithLimit", "latestChatsWithLimit"})
    public Map<Long, String> getLatestChatsWithLimit(final int limit) {
        return PlayerLogger.getPlayersChats(limit);
    }

    /**
     * Gets the latest joins
     * @return Latest joins
     */
    @Action(
            aliases = {"getLatestConnections", "latestConnections"})
    public Map<Long, String> getLatestConnections() {
        return PlayerLogger.getPlayersJoins(50);
    }

    /**
     * Gets the latest joins with a limit
     * @param limit Number of joins to include
     * @return Joins
     */
    @Action(
            aliases = {"getLatestConnectionsWithLimit", "latestConnectionsWithLimit"})
    public Map<Long, String> getLatestConnectionsWithLimit(final int limit) {
        return PlayerLogger.getPlayersJoins(limit);
    }

    /**
     * Gets the latest logs
     * @return Latest logs
     */
    @Action(
            aliases = {"getLatestConsoleLogs", "latestConsoleLogs"})
    public Map<Integer, String> getLatestConsoleLogs() {
        return getLatestConsoleLogsWithLimit(50);
    }

    /**
     * Gets the latest logs with a limit
     * @param limit Number of log lines to include
     * @return Log
     */
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

    /**
     * Gets the latest quits
     * @return Latest quits
     */
    @Action(
            aliases = {"getLatestDeconnections", "latestDeconnections"})
    public Map<Long, String> getLatestDeconnections() {
        return PlayerLogger.getPlayersQuits(50);
    }

    /**
     * Gets the latest quits with a limit
     * @param limit Number of quits to include
     * @return Quits
     */
    @Action(
            aliases = {"getLatestDeconnectionsWithLimit", "latestDeconnectionsWithLimit"})
    public Map<Long, String> getLatestDeconnectionsWithLimit(final int limit) {
        return PlayerLogger.getPlayersQuits(limit);
    }

    /**
     * Gets information about a plugin
     * @param pluginName Plugin to get
     * @return Information about a plugin
     */
    @Action(
            aliases = {"getPluginInformations", "pluginInformations"})
    public LinkedHashMap<String, Object> getPluginInformations(final String pluginName) {
        final LinkedHashMap<String, Object> pluginInformations = new LinkedHashMap<String, Object>();
        final Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            try {
                pluginInformations.put("Name", plugin.getDescription().getName());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("IsEnabled", plugin.isEnabled());
            } catch (final Exception e) {}
            pluginInformations.put("Commands", plugin.getDescription().getCommands());
            try {
                pluginInformations.put("Depend", plugin.getDescription().getDepend());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("DataFolder", plugin.getDataFolder().getPath());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("SoftDepend", plugin.getDescription().getSoftDepend());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Authors", plugin.getDescription().getAuthors());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Description", plugin.getDescription().getDescription());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("FullName", plugin.getDescription().getFullName());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Main", plugin.getDescription().getMain());
            } catch (final Exception e) {}
            try {
                if (plugin.getDescription().getPermissions() != null) {
                    final LinkedList<LinkedHashMap<String, String>> permissions = new LinkedList<LinkedHashMap<String, String>>();
                    for (final org.bukkit.permissions.Permission permission : plugin.getDescription().getPermissions()) {
                        final LinkedHashMap<String, String> permissionInformations = new LinkedHashMap<String, String>();
                        permissionInformations.put("Name", permission.getName());
                        permissionInformations.put("Description", permission.getDescription());
                        permissionInformations.put("Default", permission.getDefault().name());
                        permissions.add(permissionInformations);
                    }
                    pluginInformations.put("Permissions", permissions);
                } else
                    pluginInformations.put("Permissions", "[]");
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Version", plugin.getDescription().getVersion());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Website", plugin.getDescription().getWebsite());
            } catch (final Exception e) {}
            try {
                pluginInformations.put("Bukget", SpaceBukkit.getInstance().pluginsManager.contains(pluginName));
            } catch (final Exception e) {}
            return pluginInformations;
        }
        return new LinkedHashMap<String, Object>();
    }

    /**
     * Gets all the plugins on the server
     * @return All plugins
     */
    @Action(
            aliases = {"getPlugins", "plugins"})
    public LinkedList<String> getPlugins() {
        final LinkedList<String> pluginsNames = new LinkedList<String>();
        for (final Plugin plugin : Bukkit.getPluginManager().getPlugins())
            pluginsNames.add(plugin.getDescription().getName());
        return pluginsNames;
    }

    /**
     * Gets information about the server
     * @return Server information
     */
    @Action(
            aliases = {"getServer", "server"})
    public Map<String, Object> getServer() {
        final LinkedHashMap<String, Object> serverInformations = new LinkedHashMap<String, Object>();
        final Server server = Bukkit.getServer();
        serverInformations.put("Name", server.getName());
        serverInformations.put("AllowFlight", server.getAllowFlight());
        serverInformations.put("AllowNether", server.getAllowNether());
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

    /**
     * Gets information about a world
     * @param worldName World to get information about
     * @return Information about the world
     */
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

    /**
     * Gets all the worlds
     * @return All worlds
     */
    @Action(
            aliases = {"getWorlds", "worlds"})
    public List<String> getWorlds() {
        final List<String> worldsNames = new ArrayList<String>();
        for (final World world : Bukkit.getWorlds())
            worldsNames.add(world.getName());
        return worldsNames;
    }

    /**
     * Checks if an action is schedulable
     * @param actionName Action to check
     * @return If the action is schedulable
     * @throws UnhandledActionException If the action is not a valid action
     */
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

    /**
     * Reloads the server
     * @return If successful
     */
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

    /**
     * Saves all the maps
     * @return If successful
     */
    @Action(
            aliases = {"saveMap", "save"})
    public boolean saveMap() {
        for (final World world : Bukkit.getWorlds())
            world.save();
        return true;
    }

    /**
     * Turns auto saving off
     * @return If successful
     */
    @Action(
            aliases = {"saveOff"})
    public boolean saveOff() {
        for (final World world : Bukkit.getWorlds())
            world.setAutoSave(false);
        return true;
    }

    /**
     * Turns auto saving on
     * @return If successful
     */
    @Action(
            aliases = {"saveOn"})
    public boolean saveOn() {
        for (final World world : Bukkit.getWorlds())
            world.setAutoSave(true);
        return true;
    }

    /**
     * Sets a worlds weather
     * @param worldName World to set
     * @param storm If there is a storm
     * @param thunder If there is thunder
     * @return If successful
     */
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

    /**
     * Unbans an IP
     * @param ip Ip to unban
     * @return If successful
     */
    @Action(
            aliases = {"unbanIp", "bannedIpsRemove"})
    public boolean unbanIp(final String ip) {
        if (!ip.equals("")) {
            Bukkit.getServer().unbanIP(ip);
            return true;
        }
        return false;
    }

    /**
     * Checks if there was a connection at a certain time
     * @param time Time to check
     * @return If the was a connection at a time
     */
    @Action(
            aliases = {"wasThereAConnection", "connections"})
    public boolean wasThereAConnection(final int time) {
        return !(PlayerLogger.getLastJoin() == 0 && PlayerLogger.getLastQuit() == 0 || PlayerLogger.getLastJoin() < System
                .currentTimeMillis() - time * 1000
                && PlayerLogger.getLastQuit() < System.currentTimeMillis() - time * 1000);
    }

    /**
     * Checks if permissions are available
     * @return If permissions are avaiable
     */
    @Action(
            aliases = {"permissionsAvailable", "permsAvailable"})
    public boolean permissionsAvailable() {
        return Bukkit.getServicesManager().getRegistration(Permission.class) != null;
    }

    /**
     * Gets the permissions plugin name
     * @return Permissions plugin name
     */
    @Action(
            aliases = {"permissionsPluginName", "permsPluginName"})
    public String getPermissionsPluginName() {
        if (!(permissionsAvailable())) {
            return "NULL";
        }
        ServicesManager sm = Bukkit.getServicesManager();
        return sm.getRegistration(Permission.class).getProvider().getName();
    }

    /**
     * Gets the permissions plugin version
     * @return Permissions plugin version
     */
    @Action(
            aliases = {"permissionsPluginVersion", "permsPluginVersion"})
    public String getPermissionsPluginVersion() {
        if (!(permissionsAvailable())) {
            return "NULL";
        }

        ServicesManager sm = Bukkit.getServicesManager();
        Plugin p = Bukkit.getPluginManager().getPlugin(sm.getRegistration(Permission.class).getProvider().getName());
        if (p != null) {
            return p.getDescription().getVersion();
        }
        return "NULL";
    }

    /**
     * Get a full list of users under a permissions plugin.
     * @return The full list of users under the permissions plugin.
     */
    @Action(
            aliases = {"permUserNames", "getPermUserNames"})
    public List<String> getPermUserNames() {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (p.getPlayerGroups(player) != null && p.getPlayerGroups(player)[0] != null) {
                result.add(player.getName());
            }
        }
        return result;
    }

    /**
     * Get a full list of users under a world.
     * @param world the world to get users under.
     * @return The full list of users under a world.
     */
    @Action(
            aliases = {"permUserNamesForWorld", "getPermUserNamesForWorld"})
    public List<String> getPermUserNamesForWorld(String world) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            if (p.getPlayerGroups(world, playerName) != null && p.getPlayerGroups(world, playerName).length > 0) {
                result.add(playerName);
            }
        }
        return result;
    }

    /**
     * Get a list of all permission groups.
     * @return A list of permission groups.
     */
    @Action(
            aliases = {"permGroupNames", "getPermGroupNames"})
    public List<String> getPermGroupNames() {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        return Arrays.asList(p.getGroups());
    }

    /**
     * Get a list of all permission groups under a world.
     * @param world the world to get groups under.
     * @return A list of permission groups under a world.
     */
    @Action(
            aliases = {"permGroupNamesForWorld", "getPermGroupNamesForWorld"})
    public List<String> getPermGroupNamesForWorld(String world) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        return Arrays.asList(p.getGroups());
    }

    /**
     * Get a list of users given their parent group.
     * @param groupName The group the users belong to.
     * @return A list of usernames in a given group.
     */
    @Action(
            aliases = {"permGroupUsers", "getPermGroupUsers"})
    public List<String> getPermGroupUsers(String groupName) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (p.playerInGroup(player, groupName)) {
                result.add(player.getName());
            }
        }
        return result;
    }

    /**
     * Get a list of users given their parent group and world name.
     * @param groupName The group the users belong to.
     * @param world the name of the world the group belongs to.
     * @return A list of usernames in a given group.
     */
    @Action(
            aliases = {"permGroupUsersForWorld", "getPermGroupUsersForWorld"})
    public List<String> getPermGroupUsersForWorld(String groupName, String worldName) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            String playerName = player.getName();
            if (p.playerInGroup(worldName, playerName, groupName)) {
                result.add(playerName);
            }
        }
        return result;
    }

    /**
     * Get a list of permissions directly under a given group name.
     * @param groupName the group to list permissions under.
     * @return The list of permissions under groupName.
     */
    @Action(
            aliases = {"groupPerms", "getGroupPerms"})
    public List<String> getGroupPerms(String groupName) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (org.bukkit.permissions.Permission perm : Bukkit.getPluginManager().getPermissions()) {
            String permName = perm.getName();
            if (p.groupHas(Bukkit.getWorlds().get(0), groupName, permName)) {
                result.add(permName);
            }
        }
        return result;
    }

    /**
     * Get a list of permissions directly under a given group name in a given world.
     * @param groupName the group to list permissions under.
     * @param world the name of the world the group belongs to.
     * @return The list of permissions under groupName.
     */
    @Action(
            aliases = {"groupPermsForWorld", "getGroupPermsForWorld"})
    public List<String> getGroupPermsForWorld(String groupName, String world) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (org.bukkit.permissions.Permission perm : Bukkit.getPluginManager().getPermissions()) {
            String permName = perm.getName();
            if (p.groupHas(world, groupName, permName)) {
                result.add(permName);
            }
        }
        return result;
    }

    /**
     * Get a list of permissions given a user name.
     * @param userName The name of the user to list permissions for.
     * @return The list of permissions this user has.
     * Each element of the returned list will be a string in the form "world:permission", where 'world' is
     * the world name the permission belongs to.
     */
    @Action(
            aliases = {"userPerms", "getUserPerms"})
    public List<String> getUserPerms(String userName) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        Player ply = Bukkit.getPlayer(userName);
        if (ply == null) {
            return result;
        }
        for (org.bukkit.permissions.Permission perm : Bukkit.getPluginManager().getPermissions()) {
            String permName = perm.getName();
            if (p.has(ply, permName)) {
                result.add(permName);
            }
        }
        return result;
    }

    /**
     * Get a list of usernames that have a given permission.
     * @param permission
     * @return the list of users with a given permission.
     * Each element of the returned list will be in the form "world:group:permission" where 'world' is
     * the world name the permission belongs to, and 'group' is the group the permission belongs to.
     */
    @Action(
            aliases = {"usersWithPerm","usersWithPermission" , "getUsersWithPerm","getUsersWithPermission"})
    public List<String> getUsersWithPermission(String permission) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (p.has(player, permission)) {
                result.add(player.getName());
            }
        }
        return result;
    }

    /**
     * Check if a user has a given permission.
     * @param username The user to check for a permission.
     * @param permission The permission to check.
     * @param world the name of the world the permission is under.
     * @return true if the user has the permission, false otherwise.
     */
    @Action(
            aliases = {"userHasPerm", "userHasPermission"})
    public boolean userHasPermission(String userName, String permission, String world) {
        if (!(permissionsAvailable())) {
            return false;
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        return p.has(world, userName, permission);
    }

    /**
     * Get a list of worlds a user has a given permission.
     * @param username The user to check for a permission.
     * @param permission The permission to check.
     * @return A list of worlds the user has a permission under.
     */
    @Action(
            aliases = {"worldsUserHasPerm", "worldUserHasPermission", "getWorldsUserHasPerm", "getWorldUserHasPermission"})
    public List<String> getWorldsUserHasPermission(String userName, String permission) {
        if (!(permissionsAvailable())) {
            return new ArrayList<String>(0);
        }
        ServicesManager sm = Bukkit.getServicesManager();
        Permission p = sm.getRegistration(Permission.class).getProvider();
        List<String> result = new ArrayList<String>();
        for (World world : Bukkit.getWorlds()) {
            String name = world.getName();
            if (p.has(name, userName, permission)) {
                result.add(name);
            }
        }
        return result;
    }


}
