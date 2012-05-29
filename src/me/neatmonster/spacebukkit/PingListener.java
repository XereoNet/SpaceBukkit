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

import me.neatmonster.spacertk.SpaceRTK;

public class PingListener extends Thread {
    public static final long PING_EVERY = 1 * 1000;
    public static final long REQUEST_BUFFER = 5 * 100;

    private final Socket pluginSocket;
    private final Socket moduleSocket;

    private long lastPluginPing;
    private long lastModuleResponse;

    private AtomicBoolean running = new AtomicBoolean(false);

    public PingListener() throws IOException {
        this.pluginSocket = new Socket(InetAddress.getLocalHost(),
                SpaceRTK.getInstance().port);
        this.moduleSocket = new Socket(InetAddress.getLocalHost(), 2010); // TODO
                                                                          // config
                                                                          // value?
    }

    public void startup() {
        this.running.set(true);
        this.start();
    }

    @Override
    public void run() {
        ObjectOutputStream moduleStream = null;
        ObjectInputStream rtkStream = null;
        try {
            moduleStream = new ObjectOutputStream(
                    moduleSocket.getOutputStream());
            rtkStream = new ObjectInputStream(pluginSocket.getInputStream());
        } catch (IOException e) {
            handleException(e);
        }
        while (running.get()) {
            long now = System.currentTimeMillis();
            String input = null;
            try {
                input = me.neatmonster.spacemodule.utilities.Utilities
                        .readString(rtkStream);
            } catch (IOException e) {
                handleException(e);
            }
            if (input != null) {
                parse(input);
            }
            if (now - lastPluginPing > PING_EVERY) {
                try {
                    me.neatmonster.spacemodule.utilities.Utilities.writeString(
                            moduleStream, "PLUGIN-PING");
                    moduleStream.flush();
                    lastPluginPing = now;
                } catch (IOException e) {
                    handleException(e);
                }
            }
            if (now - lastModuleResponse > PING_EVERY + REQUEST_BUFFER) {
                onModuleNotFound();
            }
        }
    }

    public void parse(String input) {
        long now = System.currentTimeMillis();
        if (input.equalsIgnoreCase("PING")) {
            lastModuleResponse = now;
        } else {
            System.err.println("[SpaceBukkit] Unknown input! '" + input
                    + "'.  Please report this to the developers");
        }
    }

    public void shutdown() {
        try {
            this.running.set(false);
            pluginSocket.close();
            moduleSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleException(Exception e) {
        shutdown();
        System.err.println("[SpaceBukkit] Ping Listener Error! Error message:");
        e.printStackTrace();
    }

    public void onModuleNotFound() {
        System.err.println("[SpaceBukkit] Unable to ping SpaceModule!");
        System.err
                .println("[SpaceBukkit] Please insure the correct ports are open");
        System.err
                .println("[SpaceBukkit] Please contact the forums (http://forums.xereo.net/) or IRC (#SpaceBukkit on irc.esper.net)");
    }

}
