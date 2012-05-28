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
package me.neatmonster.spacebukkit.players;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import me.neatmonster.spacebukkit.SpaceBukkit;
import me.neatmonster.spacebukkit.utilities.PropertiesFile;

/**
 * Stores information about Player's actions
 */
public class PlayerLogger {
    private static TreeMap<Long, String> chats    = new TreeMap<Long, String>();
    private static TreeMap<Long, String> joins    = new TreeMap<Long, String>();
    private static long                  lastJoin;
    private static long                  lastQuit;
    private static TreeMap<Long, String> messages = new TreeMap<Long, String>();
    private static TreeMap<Long, String> quits    = new TreeMap<Long, String>();

    /**
     * Adds a chat to the cache
     * @param playerName Player to add
     * @param message Message to add
     */
    public static void addPlayerChat(final String playerName, final String message) {
        if (chats.keySet().size() > SpaceBukkit.getInstance().maxMessages)
            cleanPlayersChats();
        final long time = System.currentTimeMillis();
        chats.put(time, playerName);
        messages.put(time, message);
    }

    /**
     * Adds a join to the cache
     * @param playerName Player to add
     */
    public static void addPlayerJoin(final String playerName) {
        lastJoin = System.currentTimeMillis();
        if (joins.keySet().size() > SpaceBukkit.getInstance().maxJoins)
            cleanPlayersJoins();
        joins.put(System.currentTimeMillis(), playerName);
    }

    /**
     * Adds a quit to the cache
     * @param playerName Player to add
     */
    public static void addPlayerQuit(final String playerName) {
        lastQuit = System.currentTimeMillis();
        if (quits.keySet().size() > SpaceBukkit.getInstance().maxQuits)
            cleanPlayersQuits();
        quits.put(System.currentTimeMillis(), playerName);
    }

    /**
     * Clears all the player chats
     */
    public static void cleanPlayersChats() {
        for (int x = chats.size() - SpaceBukkit.getInstance().maxMessages; x > 0; x--) {
            chats.remove(chats.firstKey());
            messages.remove(messages.firstKey());
        }
    }

    /**
     * Clears all the player joins
     */
    public static void cleanPlayersJoins() {
        for (int x = joins.size() - SpaceBukkit.getInstance().maxJoins; x > 0; x--)
            joins.remove(joins.firstKey());
    }

    /**
     * Clears all the player quits
     */
    public static void cleanPlayersQuits() {
        for (int x = quits.size() - SpaceBukkit.getInstance().maxQuits; x > 0; x--)
            quits.remove(quits.firstKey());
    }

    /**
     * Gets the case of a player as assigned by the panel
     * @param playerName Player to get
     * @return Modified name of the player
     */
    public static String getCase(final String playerName) {
        try {
            final PropertiesFile propertiesFile = new PropertiesFile(
                    new File("SpaceModule", "players.properties").getPath());
            propertiesFile.load();
            final String savedPlayerName = propertiesFile.getString(playerName.toLowerCase());
            propertiesFile.save();
            if (savedPlayerName != null && !savedPlayerName.equals(""))
                return savedPlayerName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return playerName;
    }

    /**
     * Gets the last time a player joined the server
     * @return Last time a player joined
     */
    public static long getLastJoin() {
        return lastJoin;
    }

    /**
     * Gets the last time a player quit the server
     * @return Last time a player quit
     */
    public static long getLastQuit() {
        return lastQuit;
    }

    /**
     * Gets a map of player chats with a limit
     * @param limit Number of chats to include
     * @return Map of player chats
     */
    public static Map<Long, String> getPlayersChats(final int limit) {
        final TreeMap<Long, String> results = new TreeMap<Long, String>();
        int x = 0;
        for (final Long time : chats.descendingKeySet()) {
            if (x < limit)
                results.put(time, chats.get(time) + ": " + messages.get(time));
            x++;
        }
        return results;
    }

    /**
     * Gets a map of player joins with a limit
     * @param limit Number of joins to include
     * @return Map of player joins
     */
    public static Map<Long, String> getPlayersJoins(final int limit) {
        final TreeMap<Long, String> results = new TreeMap<Long, String>();
        int x = 0;
        for (final Long time : joins.descendingKeySet()) {
            if (x < limit)
                results.put(time, joins.get(time));
            x++;
        }
        return results;
    }

    /**
     * Gets a map of player quits with a limit
     * @param limit Number of quits to include
     * @return Map of player quits
     */
    public static Map<Long, String> getPlayersQuits(final int limit) {
        final TreeMap<Long, String> results = new TreeMap<Long, String>();
        int x = 0;
        for (final Long time : quits.descendingKeySet()) {
            if (x < limit)
                results.put(time, quits.get(time));
            x++;
        }
        return results;
    }

    /**
     * Sets the case of a player
     * @param playerName Player's new case
     */
    public static void setCase(final String playerName) {
        try {
            final PropertiesFile propertiesFile = new PropertiesFile(
                    new File("SpaceModule", "players.properties").getPath());
            propertiesFile.load();
            propertiesFile.setString(playerName.toLowerCase(), playerName);
            propertiesFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
