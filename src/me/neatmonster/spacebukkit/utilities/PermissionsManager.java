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
package me.neatmonster.spacebukkit.utilities;

import me.neatmonster.spacebukkit.utilities.permissions.PermissionsConnector;
import me.neatmonster.spacebukkit.utilities.permissions.PermissionsExConnector;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Manages available permissions connectors.
 * @author drdanick
 */
public class PermissionsManager {

    private PermissionsConnector connector;

    /**
     * Construct a PermissionsManager with a given permissions connector.
     * @param connector
     */
    public PermissionsManager(PermissionsConnector connector) {
        this.connector = connector;
    }

    /**
     * Get the currently active permissions connector.
     * @return The PermissionsConnector associated with this manager.
     */
    public PermissionsConnector getCurrentPermissionsConnector() {
        return connector;
    }

    /**
     * Find an active supported bukkit permissions plugin and construct an appropriate connector.
     * 
     * Currently searches in the order of:
     * - PermissionsEX
     *
     * @return A PermissionsConnector representing an active bukkit permissions system. This method will
     * return null if no supported permissions plugins are found.
     */
    public static PermissionsConnector findConnector() {
        if(PermissionsEx.isAvailable()) return new PermissionsExConnector(PermissionsEx.getPermissionManager());

        return null;
    }

}
