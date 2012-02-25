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

import org.bukkit.util.config.Configuration;

@SuppressWarnings("deprecation")
public class PlayerLogger {
    private static TreeMap<Long, String> chats    = new TreeMap<Long, String>();
    private static TreeMap<Long, String> joins    = new TreeMap<Long, String>();
    private static long                  lastJoin;
    private static long                  lastQuit;
    private static TreeMap<Long, String> messages = new TreeMap<Long, String>();
    private static TreeMap<Long, String> quits    = new TreeMap<Long, String>();

    public static void addPlayerChat(final String playerName, final String message) {
        if (chats.keySet().size() > 199)
            cleanPlayersChats();
        final long time = System.currentTimeMillis();
        chats.put(time, playerName);
        messages.put(time, message);
    }

    public static void addPlayerJoin(final String playerName) {
        lastJoin = System.currentTimeMillis();
        if (joins.keySet().size() > 199)
            cleanPlayersJoins();
        joins.put(System.currentTimeMillis(), playerName);
    }

    public static void addPlayerQuit(final String playerName) {
        lastQuit = System.currentTimeMillis();
        if (quits.keySet().size() > 199)
            cleanPlayersQuits();
        quits.put(System.currentTimeMillis(), playerName);
    }

    public static void cleanPlayersChats() {
        for (int x = chats.size() - 199; x > 0; x--) {
            chats.remove(chats.firstKey());
            messages.remove(messages.firstKey());
        }
    }

    public static void cleanPlayersJoins() {
        for (int x = joins.size() - 199; x > 0; x--)
            joins.remove(joins.firstKey());
    }

    public static void cleanPlayersQuits() {
        for (int x = quits.size() - 199; x > 0; x--)
            quits.remove(quits.firstKey());
    }

    public static String getCase(final String playerName) {
        final File file = new File("SpaceModule", "players.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        final Configuration configuration = new Configuration(file);
        configuration.load();
        final String result = configuration.getString(playerName.toLowerCase(), playerName);
        configuration.save();
        return result;
    }

    public static long getLastJoin() {
        return lastJoin;
    }

    public static long getLastQuit() {
        return lastQuit;
    }

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

    public static void setCase(final String playerName) {
        final File file = new File("SpaceModule", "players.yml");
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        final Configuration configuration = new Configuration(file);
        configuration.load();
        configuration.setProperty(playerName.toLowerCase(), playerName);
        configuration.save();
    }
}
