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
import java.io.IOException;
import java.util.Timer;
import java.util.UUID;
import java.util.logging.Logger;

import com.drdanick.rtoolkit.EventDispatcher;
import com.drdanick.rtoolkit.event.ToolkitEventHandler;
import me.neatmonster.spacebukkit.actions.PlayerActions;
import me.neatmonster.spacebukkit.actions.ServerActions;
import me.neatmonster.spacebukkit.actions.SystemActions;
import me.neatmonster.spacebukkit.players.SBListener;
import me.neatmonster.spacebukkit.plugins.PluginsManager;
import me.neatmonster.spacebukkit.system.PerformanceMonitor;
import me.neatmonster.spacebukkit.utilities.PermissionsManager;
import me.neatmonster.spacemodule.SpaceModule;
import me.neatmonster.spacemodule.api.ActionsManager;
import me.neatmonster.spacertk.SpaceRTK;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 * Main class of the Plugin
 */
@SuppressWarnings("deprecation")
public class SpaceBukkit extends JavaPlugin {
    public static SpaceRTK     spaceRTK = null;
    private static SpaceBukkit spacebukkit;

    public static SpaceBukkit getInstance() {
        return spacebukkit;
    }

    public int                  port;
    public int                  rPort;
    public String               salt;
    
    public int                  maxJoins;
    public int                  maxMessages;
    public int                  maxQuits;

    public PluginsManager       pluginsManager;
    public ActionsManager       actionsManager;
    public PanelListener        panelListener;
    public PerformanceMonitor   performanceMonitor;

    private YamlConfiguration   configuration;
    public String               logTag = "[SpaceBukkit] ";

    private final Timer         timer  = new Timer();

    private EventDispatcher     edt;
    private ToolkitEventHandler eventHandler;
    private PingListener pingListener;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        performanceMonitor.infanticide();
        timer.cancel();
        pingListener.shutdown();
        try {
            if (panelListener != null)
                panelListener.stopServer();
        } catch (final Exception e) {
            getLogger().severe(logTag + e.getMessage());
        }
        edt.setRunning(false);
        synchronized (edt) {
            edt.notifyAll();
        }
        eventHandler.setEnabled(false);
        getLogger().info("----------------------------------------------------------");
        getLogger().info("|             SpaceBukkit is now disabled!               |");
        getLogger().info("----------------------------------------------------------");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        spacebukkit = this;
        configuration = YamlConfiguration.loadConfiguration(SpaceModule.CONFIGURATION);
        salt = configuration.getString("General.Salt", "<default>");
        if (salt.equals("<default>")) {
            salt = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            configuration.set("General.Salt", salt);
        }
        configuration.set("General.WorldContainer", Bukkit.getWorldContainer().getPath());
        port = configuration.getInt("SpaceBukkit.Port", 2011);
        rPort = configuration.getInt("SpaceRTK.Port", 2012);
        maxJoins = configuration.getInt("SpaceBukkit.maxJoins", 199);
        maxMessages = configuration.getInt("SpaceBukkit.maxMessages", 199);
        maxQuits = configuration.getInt("SpaceBukkit.maxQuits", 199);
        try {
            configuration.save(SpaceModule.CONFIGURATION);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(edt == null)
            edt = new EventDispatcher();

        if(!edt.isRunning()) {
            synchronized(edt) {
                edt.notifyAll();
            }
            edt.setRunning(true);
            Thread edtThread = new Thread(edt, "SpaceModule EventDispatcher");
            edtThread.setDaemon(true);
            edtThread.start();
        }

        if(eventHandler != null) {
            eventHandler.setEnabled(true);
            if(!eventHandler.isRunning())
                new Thread(eventHandler, "SpaceModule EventHandler").start();
        } else {
            eventHandler = new EventHandler();
            new Thread(eventHandler, "SpaceModule EventHandler").start();
        }

        new SBListener(this);
        pluginsManager = new PluginsManager();
        actionsManager = new ActionsManager();
        actionsManager.register(PlayerActions.class);
        actionsManager.register(ServerActions.class);
        actionsManager.register(SystemActions.class);
        panelListener = new PanelListener();
        performanceMonitor = new PerformanceMonitor();
        pingListener = new PingListener();
        pingListener.startup();
        timer.scheduleAtFixedRate(performanceMonitor, 0L, 1000L);
        getLogger().info("----------------------------------------------------------");
        getLogger().info("|        SpaceBukkit version "
                + this.getDescription().getVersion()
                + " is now enabled!         |");
        getLogger().info("----------------------------------------------------------");
    }

    /**
     * Gets the RTK event dispatcher
     * @return event dispatcher
     */
    public EventDispatcher getEdt() {
        return edt;
    }

    /**
     * Gets the RTK event handler
     * @return event handler
     */
    public ToolkitEventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Does...Nothing?
     */
    private class EventHandler extends ToolkitEventHandler {
        /**
         * Creates a new Event Handler
         */
        public EventHandler() {
            setEnabled(true);
        }
    }

}
