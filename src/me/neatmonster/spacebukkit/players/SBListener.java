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
package me.neatmonster.spacebukkit.players;

import me.neatmonster.spacebukkit.SpaceBukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * SpaceBukkit Event Listener
 */
public class SBListener implements Listener {

    /**
     * Creates a new SBListener
     * @param plugin Plugin Instance
     */
    public SBListener(final SpaceBukkit plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
   
    /**
     * Called when a player chats
     * @param event Relevant event details
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(final PlayerChatEvent event) {
        PlayerLogger.addPlayerChat(event.getPlayer().getName(), event.getMessage());
    }

    /**
     * Called when a player joins
     * @param event Relevant event details
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        PlayerLogger.addPlayerJoin(event.getPlayer().getName());
    }

    /**
     * Called when a player quits
     * @param event Relevant event details
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        PlayerLogger.addPlayerQuit(event.getPlayer().getName());
    }

    /**
     * Called when a server performs a command
     * @param event Relevant event details
     */
    @EventHandler(ignoreCancelled = true)
    public void onServerCommand(final ServerCommandEvent event) {
        String cmd = event.getCommand();
        if (!cmd.equalsIgnoreCase("say")) {
            final String message = cmd.substring(4);
            PlayerLogger.addPlayerChat("Server", message);
        }
    }
}
