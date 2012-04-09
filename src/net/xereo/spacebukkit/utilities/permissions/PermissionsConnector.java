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
package net.xereo.spacebukkit.utilities.permissions;

import java.util.List;

/**
 * Abstract API for accessing bukkit permissions.
 * @author drdanick
 */
public interface PermissionsConnector {


    /**
     * Get the name of the permissions plugin supported by some connector.
     * @return the name of the permissions plugin supported by some connector.
     */
    public String getPermissionsPluginName();

    /**
     * Get the version of the permissions plugin some connector was written for.
     * @return The version of the permissions plugin some connector was written for.
     */
    public String getPermissionsPluginVersion();

    /**
     * Get a full list of users under a permissions plugin.
     * @return The full list of users under the permissions plugin.
     */
    public List<String> getUserNames();

    /**
     * Get a full list of users under a world.
     * @param world the world to get users under.
     * @return The full list of users under a world.
     */
    public List<String> getUserNames(String world);

    /**
     * Get a list of all permission groups.
     * @return A list of permission groups.
     */
    public List<String> getGroupNames();

    /**
     * Get a list of all permission groups under a world.
     * @param world the world to get groups under.
     * @return A list of permission groups under a world.
     */
    public List<String> getGroupNames(String world);

    /**
     * Get a list of users given their parent group.
     * @param groupName The group the users belong to.
     * @return A list of usernames in a given group.
     */
    public List<String> getGroupUsers(String groupName);

    /**
     * Get a list of users given their parent group and world name.
     * @param groupName The group the users belong to.
     * @param world the name of the world the group belongs to.
     * @return A list of usernames in a given group.
     */
    public List<String> getGroupUsers(String groupName, String world);

    /**
     * Get a list of permissions directly under a given group name.
     * @param groupName the group to list permissions under.
     * @return The list of permissions under groupName.
     */
    public List<String> getGroupPermissions(String groupName);

    /**
     * Get a list of permissions directly under a given group name in a given world.
     * @param groupName the group to list permissions under.
     * @param world the name of the world the group belongs to.
     * @return The list of permissions under groupName.
     */
    public List<String> getGroupPermissions(String groupName, String world);

    /**
     * Get a list of all permissions, both direct and inherited, under a given group name.
     * @param groupName the group to list permissions under.
     * @return The list of permissions under groupName.
     * Each element of the returned list will be a string in the form "world:permission", where 'world' is
     * the world name the permission belongs to.
     */
    public List<String> getAllGroupPermissions(String groupName);

    /**
     * Get a list of permissions given a user name.
     * @param userName The name of the user to list permissions for.
     * @return The list of permissions this user has.
     * Each element of the returned list will be a string in the form "world:permission", where 'world' is
     * the world name the permission belongs to.
     */
    public List<String> getUserPermissions(String userName);

    /**
     * Get a list of usernames that have a given permission.
     * @param permission
     * @return the list of users with a given permission.
     * Each element of the returned list will be in the form "world:group:permission" where 'world' is
     * the world name the permission belongs to, and 'group' is the group the permission belongs to.
     */
    public List<String> getUsersWithPermission(String permission);

    /**
     * Check if a user has a given permission.
     * @param username The user to check for a permission.
     * @param permission The permission to check.
     * @param world the name of the world the permission is under.
     * @return true if the user has the permission, false otherwise.
     */
    public boolean userHasPermission(String username, String permission, String world);

    /**
     * Get a list of worlds a user has a given permission.
     * @param username The user to check for a permission.
     * @param permission The permission to check.
     * @return A list of worlds the user has a permission under.
     */
    public List<String> getWorldsUserHasPermission(String username, String permission);

}
