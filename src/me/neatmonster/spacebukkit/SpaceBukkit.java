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
package me.neatmonster.spacebukkit;

import java.io.File;
import java.util.Timer;
import java.util.UUID;
import java.util.logging.Logger;

import me.neatmonster.spacebukkit.actions.PlayerActions;
import me.neatmonster.spacebukkit.actions.ServerActions;
import me.neatmonster.spacebukkit.actions.SystemActions;
import me.neatmonster.spacebukkit.players.SBPlayerListener;
import me.neatmonster.spacebukkit.players.SBServerListener;
import me.neatmonster.spacebukkit.plugins.PluginsManager;
import me.neatmonster.spacebukkit.system.PerformanceMonitor;
import me.neatmonster.spacemodule.api.ActionsManager;
import me.neatmonster.spacertk.SpaceRTK;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

@SuppressWarnings("deprecation")
public class SpaceBukkit extends JavaPlugin {
    public static SpaceRTK     spaceRTK = null;
    private static SpaceBukkit spacebukkit;

    public static SpaceBukkit getInstance() {
        return spacebukkit;
    }

    public int                port;
    public int                rPort;
    public String             salt;

    public PluginsManager     pluginsManager     = new PluginsManager();
    public ActionsManager     actionsManager     = new ActionsManager();
    public PanelListener      panelListener      = new PanelListener();
    public PerformanceMonitor performanceMonitor = new PerformanceMonitor();

    private Configuration     configuration;
    public Logger             logger             = Logger.getLogger("Minecraft");
    public String             logTag             = "[SpaceBukkit] ";

    private final Timer       timer              = new Timer();

    @Override
    public void onDisable() {
        performanceMonitor.infanticide();
        timer.cancel();
        try {
            if (panelListener != null)
                panelListener.stopServer();
        } catch (final Exception e) {
            logger.severe(logTag + e.getMessage());
        }
        logger.info("----------------------------------------------------------");
        logger.info("|             SpaceBukkit is now disabled!               |");
        logger.info("----------------------------------------------------------");
    }

    @Override
    public void onEnable() {
        spacebukkit = this;
        configuration = new Configuration(new File("SpaceModule", "configuration.yml"));
        configuration.load();
        salt = configuration.getString("General.Salt", "<default>");
        if (salt.equals("<default>")) {
            salt = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            configuration.setProperty("General.Salt", salt);
        }
        configuration.setProperty("General.WorldContainer", Bukkit.getWorldContainer().getPath());
        port = configuration.getInt("SpaceBukkit.Port", 2011);
        rPort = configuration.getInt("SpaceRTK.Port", 2012);
        configuration.save();
        new SBPlayerListener(this);
        new SBServerListener(this);
        actionsManager.register(PlayerActions.class);
        actionsManager.register(ServerActions.class);
        actionsManager.register(SystemActions.class);
        timer.scheduleAtFixedRate(performanceMonitor, 0L, 1000L);
        logger.info("----------------------------------------------------------");
        logger.info("|        SpaceBukkit version "
                + Bukkit.getPluginManager().getPlugin("SpaceBukkit").getDescription().getVersion()
                + " is now enabled!         |");
        logger.info("----------------------------------------------------------");
    }
}