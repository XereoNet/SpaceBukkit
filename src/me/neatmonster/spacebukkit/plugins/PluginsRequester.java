/*
 * This file is part of SpaceBukkit (http://spacebukkit.xereo.net/).
 *
 * SpaceBukkit is free software: you can redistribute it and/or modify it under the terms of the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license as published by the Creative
 * Common organization, either version 3.0 of the license, or (at your option) any later version.
 *
 * SpaceRTK is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license for more details.
 *
 * You should have received a copy of the Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA)
 * license along with this program. If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 */
package me.neatmonster.spacebukkit.plugins;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import me.neatmonster.spacebukkit.SpaceBukkit;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 * Requests the plugins from BukGet
 */
public class PluginsRequester implements Runnable {

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            final URLConnection connection = new URL("http://bukget.org/api/plugins").openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuffer.append(line);
            bufferedReader.close();
            PluginsManager.pluginsNames = (JSONArray) JSONValue.parse(stringBuffer.toString());
            SpaceBukkit.getInstance().logger.info(SpaceBukkit.getInstance().logTag + "Database contains "
                    + PluginsManager.pluginsNames.size() + " plugins.");
        } catch (final Exception e) {
            e.printStackTrace();
        }
        PluginsManager.pluginsNames = new ArrayList<String>();
    }
}
