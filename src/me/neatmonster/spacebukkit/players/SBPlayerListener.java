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

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class SBPlayerListener extends PlayerListener {

    public SBPlayerListener(final JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, this, Priority.Monitor, plugin);
        Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Priority.Monitor, plugin);
        Bukkit.getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Priority.Monitor, plugin);
    }

    @Override
    public void onPlayerChat(final PlayerChatEvent event) {
        PlayerLogger.addPlayerChat(event.getPlayer().getName(), event.getMessage());
    }

    @Override
    public void onPlayerJoin(final PlayerJoinEvent event) {
        PlayerLogger.addPlayerJoin(event.getPlayer().getName());
    }

    @Override
    public void onPlayerQuit(final PlayerQuitEvent event) {
        PlayerLogger.addPlayerQuit(event.getPlayer().getName());
    }
}