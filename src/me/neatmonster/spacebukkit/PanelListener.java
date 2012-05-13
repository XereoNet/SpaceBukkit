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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.List;

import me.neatmonster.spacebukkit.events.RequestEvent;
import me.neatmonster.spacebukkit.utilities.Utilities;
import me.neatmonster.spacemodule.api.InvalidArgumentsException;
import me.neatmonster.spacemodule.api.UnhandledActionException;

import org.bukkit.Bukkit;
import org.json.simple.JSONValue;

public class PanelListener extends Thread {
    
    private boolean running = true;

    @SuppressWarnings("unchecked")
    private static Object interpret(final String string) throws InvalidArgumentsException, UnhandledActionException {
        final int indexOfMethod = string.indexOf("?method=");
        final int indexOfArguments = string.indexOf("&args=");
        final int indexOfKey = string.indexOf("&key=");
        final String method = string.substring(indexOfMethod + 8, indexOfArguments);
        final String argumentsString = string.substring(indexOfArguments + 6, indexOfKey);
        final List<Object> arguments = (List<Object>) JSONValue.parse(argumentsString);
        try {
            if (SpaceBukkit.getInstance().actionsManager.contains(method))
                return SpaceBukkit.getInstance().actionsManager.execute(method, arguments.toArray());
            else {
                final RequestEvent event = new RequestEvent(method, arguments.toArray());
                Bukkit.getPluginManager().callEvent(event);
                return JSONValue.toJSONString(event.getResult());
            }
        } catch (final InvalidArgumentsException e) {
            e.printStackTrace();
        } catch (final UnhandledActionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private final int    mode;
    private ServerSocket serverSocket = null;
    private Socket       socket;

    public PanelListener() {
        mode = 0;
        start();
    }

    public PanelListener(final Socket socket) {
        mode = 1;
        this.socket = socket;
        start();
    }

    public int getMode() {
        return mode;
    }

    @Override
    public void run() {
        if (mode == 0) {

            try {
                serverSocket = new ServerSocket(SpaceBukkit.getInstance().port);
            } catch(IOException e) {
                e.printStackTrace();
                return;
            }

            while (running && !serverSocket.isClosed()) {
                try {
                    final Socket clientSocket = serverSocket.accept();
                    new PanelListener(clientSocket);
                } catch (final Exception e) {
                        e.printStackTrace();
                }
            }
        } else {
            try {
                final BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String string = input.readLine();
                string = URLDecoder.decode(string, "UTF-8");
                string = string.substring(5, string.length() - 9);
                final PrintWriter output = new PrintWriter(socket.getOutputStream());
                if (string.startsWith("call") && string.contains("?method=") && string.contains("&args=")) {
                    final String method = string.substring(12, string.indexOf("&args="));
                    if (string.contains("&key=" + Utilities.crypt(method + SpaceBukkit.getInstance().salt))) {
                        final Object result = interpret(string);
                        if (result != null)
                            output.println(Utilities.addHeader(JSONValue.toJSONString(result)));
                        else
                            output.println(Utilities.addHeader(null));
                    } else
                        output.println(Utilities.addHeader("Incorrect Salt supplied. Access denied!"));
                } else if (string.startsWith("ping"))
                    output.println(Utilities.addHeader("Pong!"));
                else
                    output.println(Utilities.addHeader(null));
                output.flush();
                input.close();
                output.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer() throws IOException {
        running = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
