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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import me.neatmonster.spacemodule.utilities.Utilities;

/**
 * Pings the Module to ensure it is functioning correctly
 */
public class PingListener extends Thread {
    public static final long PING_EVERY = 30000; // Thirty seconds
    public static final long REQUEST_BUFFER = 10000; // Ten seconds

    private long lastPluginPing;
    private long lastModuleResponse;

    private boolean lostModule;

    private PacketSendClass sender;
    private PacketReceiveClass receiver;

    private AtomicBoolean running = new AtomicBoolean(false);

    /**
     * Creates a new PingListener
     */
    public PingListener() {
        this.lostModule = false;
        this.lastPluginPing = 0;
        this.lastModuleResponse = 0;
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
        try {
            sender = new PacketSendClass();
            receiver = new PacketReceiveClass();
        } catch (SocketException e) {
            handleException(e,
                    "Unable to start the PingListener, socket error!");
        }
        sender.start();
        receiver.start();
        while (running.get()) {
            long now = System.currentTimeMillis();
            if (now - lastModuleResponse > PING_EVERY + REQUEST_BUFFER) {
                onModuleNotFound();
            }
        }
    }

    /**
     * Sends packets to the module
     */
    private class PacketSendClass extends Thread {
        private final DatagramSocket socket;

        /**
         * Creates a new PacketSendClass
         * 
         * @throws SocketException
         *             If the socket could not be created
         */
        public PacketSendClass() throws SocketException {
            socket = new DatagramSocket(2014);
        }

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            if (now - lastPluginPing > PING_EVERY) {
                try {
                    byte[] buffer = Utilities.longToBytes(now);
                    DatagramPacket packet = new DatagramPacket(buffer,
                            buffer.length, InetAddress.getLocalHost(), 2014);
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Receives packets from the module
     */
    private class PacketReceiveClass extends Thread {
        private final DatagramSocket socket;

        /**
         * Creates a new PacketReceiveClass
         * 
         * @throws SocketException
         *             If the socket could not be created
         */
        public PacketReceiveClass() throws SocketException {
            socket = new DatagramSocket(2014);
        }

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            if (now - lastPluginPing > PING_EVERY) {
                try {
                    byte[] buffer = new byte[65536];
                    DatagramPacket packet = new DatagramPacket(buffer,
                            buffer.length, InetAddress.getLocalHost(), 2014);
                    socket.receive(packet);
                    long sent = Utilities.bytesToLong(packet.getData());
                    if (lastModuleResponse < sent) {
                        lastModuleResponse = sent;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Shuts down the Ping Listener
     */
    public void shutdown() {
        this.running.set(false);
        try {
            sender.join(1000);
            receiver.join(1000);
        } catch (InterruptedException e) {
            handleException(e, "Could not shutdown the PingListener!");
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
        if (lostModule) {
            return;
        }
        System.err.println("[SpaceBukkit] Unable to ping the Module!");
        System.err
                .println("[SpaceBukkit] Please insure the correct ports are open");
        System.err
                .println("[SpaceBukkit] Please contact the forums (http://forums.xereo.net/) or IRC (#SpaceBukkit on irc.esper.net)");
        lostModule = true;
    }

}
