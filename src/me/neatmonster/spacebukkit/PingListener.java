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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pings the RTK and Module to ensure they are functioning correctly
 */
public class PingListener extends Thread {
    public static final long PING_EVERY = 30000; // Thirty seconds
    public static final long REQUEST_BUFFER = 10000; // Ten seconds

    private final Socket moduleSocket;

    private long lastPluginPing;
    private long lastModuleResponse;
    
    private boolean lostModule;

    private AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Creates a new PingListener
     * 
     * @throws IOException
     *             If an exception is thrown
     */
    public PingListener() throws IOException {
        this.moduleSocket = new Socket(InetAddress.getLocalHost(), 2014);
        this.lostModule = false;
    }

    /**
     * Starts the Ping Listener
     */
    public void startup() {
        this.running.set(true);
        this.start();
    }

    @Override
    public void run() {
        while (running.get()) {
            long now = System.currentTimeMillis();
            boolean shouldRead = now - lastModuleResponse > PING_EVERY;
            if (shouldRead) {
                try {
                    ObjectInputStream stream = new ObjectInputStream(moduleSocket.getInputStream());
                    String input = me.neatmonster.spacemodule.utilities.Utilities.readString(stream);
                    if (input != null) {
                        parse(input);
                    }
                } catch (IOException e) {
                    // Do Nothing, as this means that there was no input sent
                }
            }
            if (now - lastPluginPing > PING_EVERY) {
                try {
                    ObjectOutputStream stream = new ObjectOutputStream(moduleSocket.getOutputStream());
                    me.neatmonster.spacemodule.utilities.Utilities.writeString(
                            stream, "PLUGIN-PING");
                    stream.flush();
                    lastPluginPing = now;
                } catch (IOException e) {
                    handleException(e, "Ping could not be sent to the Module!");
                }
            }
            if (!lostModule && now - lastModuleResponse > PING_EVERY + REQUEST_BUFFER) {
                onModuleNotFound();
                lostModule = true;
            }
        }
    }

    /**
     * Parses input from the Module
     * 
     * @param input
     *            Input from the Module
     */
    public void parse(String input) {
        long now = System.currentTimeMillis();
        if (input.equalsIgnoreCase("PING")) {
            lastModuleResponse = now;
        } else {
            System.err.println("[SpaceBukkit] Unknown input! '" + input
                    + "'.  Please report this to the developers");
        }
    }

    /**
     * Shuts down the Ping Listener
     */
    public void shutdown() {
        try {
            this.running.set(false);
            if (!(moduleSocket.isClosed())) {
                moduleSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when an exception is thrown
     * 
     * @param e
     *            Exception thrown
     */
    public void handleException(Exception e, String reason) {
        shutdown();
        System.err.println("[SpaceBukkit] Ping Listener Error!");
        System.err.println(reason);
        System.err.println("Error message:");
        e.printStackTrace();
    }

    /**
     * Called when the Module can't be found
     */
    public void onModuleNotFound() {
        System.err.println("[SpaceBukkit] Unable to ping the Module!");
        System.err
                .println("[SpaceBukkit] Please insure the correct ports are open");
        System.err
                .println("[SpaceBukkit] Please contact the forums (http://forums.xereo.net/) or IRC (#SpaceBukkit on irc.esper.net)");
    }

}
