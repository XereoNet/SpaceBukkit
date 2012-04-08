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

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * PermissionsConnector implementation for PermissionsEX.
 * @author drdanick
 */
public class PermissionsExConnector implements PermissionsConnector {

    private PermissionManager pexManager;

    public PermissionsExConnector(PermissionManager pexManager) {
        this.pexManager = pexManager;
    }

    /**
     * Get a full list of users under a permissions plugin.
     * @return The full list of users under the permissions plugin.
     */
    @Override
    public List<String> getUserNames() {
        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : pexManager.getUsers()) {
            userNames.add(u.getName());
        }

        return userNames;
    }

    /**
     * Get a full list of users under a world.
     * Note: This implementation runs in O(nm), with an average worst case of O(N^2).
     * @param world the world to get users under.
     * @return The full list of users under a world.
     */
    @Override
    public List<String> getUserNames(String world) {
        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : pexManager.getUsers()) {
            String[] worlds = u.getWorlds();
            for(String w : worlds) {
                if(w.equalsIgnoreCase(world)) {
                    userNames.add(u.getName());
                    break;
                }
            }
        }

        return userNames;
    }

    /**
     * Get a list of all permission groups.
     * @return A list of permission groups.
     */
    @Override
    public List<String> getGroupNames() {
        List<String> groupNames = new ArrayList<String>();
        for(PermissionGroup g : pexManager.getGroups()) {
            groupNames.add(g.getName());
        }

        return groupNames;
    }

    /**
     * Get a list of all permission groups under a world.
     * Note: This implementation runs in O(nm), with an average worst case of O(N^2).
     * @param world the world to get groups under.
     * @return A list of permission groups under a world.
     */
    @Override
    public List<String> getGroupNames(String world) {
        List<String> groups = new ArrayList<String>();
        for(PermissionGroup g : pexManager.getGroups()) {

            String[] worlds = g.getWorlds();
            for(String w : worlds) {
                if(w.equalsIgnoreCase(world)) {

                }
            }
        }

        return groups;
    }

    /**
     * Get a list of users given their parent group.
     * @param groupName The group the users belong to.
     * @return A list of usernames in a given group.
     */
    @Override
    public List<String> getGroupUsers(String groupName) {
        PermissionGroup group = pexManager.getGroup(groupName);
        if(group == null)
            return null;

        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : group.getUsers()) {
            userNames.add(u.getName());
        }
        return userNames;
    }

    /**
     * Get a list of users belonging to a group under a given world.
     *
     * @param groupName The group the users belong to.
     * @param world     the name of the world the group belongs to.
     * @return A list of user names in a given group.
     */
    @Override
    public List<String> getGroupUsers(String groupName, String world) {
        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : pexManager.getUsers(groupName, world)) {
            userNames.add(u.getName());
        }

        return userNames;
    }

    /**
     * Get a list of permissions directly under a given group name.
     * Note: This implementation runs in O(nm), with an average worst case of O(n^2).
     * @param groupName the group to list permissions under.
     * @return The list of permissions under groupName.
     *         Each element of the returned list will be a string in the form "world:permission", where 'world' is
     *         the world name the permission belongs to.
     */
    @Override
    public List<String> getGroupPermissions(String groupName) {
        PermissionGroup group = pexManager.getGroup(groupName);
        if(group == null)
            return null;

        String[] worlds = group.getWorlds();
        List<String> permissions = new ArrayList<String>();
        for(String w : worlds) {
            for(String p : group.getPermissions(w)) {
                permissions.add(w + ":" + p);
            }
        }

        return permissions;
    }

    /**
     * Get a list of permissions directly under a given group name in a given world.
     *
     * @param groupName the group to list permissions under.
     * @param world     the name of the world the group belongs to.
     * @return The list of permissions under groupName, null if the group does not exist.
     */
    @Override
    public List<String> getGroupPermissions(String groupName, String world) {
        PermissionGroup group = pexManager.getGroup(groupName);
        if(group == null)
            return null;

        List<String> permissions = new ArrayList<String>();
        permissions.addAll(Arrays.asList(group.getPermissions(world)));

        return permissions;
    }

    /**
     * Get a list of all permissions, both direct and inherited, under a given group name.
     *
     * @param groupName the group to list permissions under.
     * @return The list of permissions under groupName.
     *         Each element of the returned list is a string in the form "world:permission", where 'world' is
     *         the world name the permission belongs to.
     */
    @Override
    public List<String> getAllGroupPermissions(String groupName) {
        PermissionGroup group = pexManager.getGroup(groupName);
        if(group == null)
            return null;

        List<String> permissions = new ArrayList<String>();
        for(Map.Entry<String, String[]> e : group.getAllPermissions().entrySet()) {
            for(String p : e.getValue()) {
                permissions.add(e.getKey() + ":" + p);
            }
        }

        return permissions;
    }

    /**
     * Get a list of permissions given a user name.
     *
     * @param userName The name of the user to list permissions for.
     * @return The list of permissions this user has.
     *         Each element of the returned list will be a string in the form "world:permission", where 'world' is
     *         the world name the permission belongs to.
     */
    @Override
    public List<String> getUserPermissions(String userName) {
        PermissionUser user = pexManager.getUser(userName);
        if(user == null)
            return null;

        List<String> permissions = new ArrayList<String>();
        for(Map.Entry<String,String[]> e : user.getAllPermissions().entrySet()) {
            for(String p : e.getValue()) {
                permissions.add(e.getKey() + ":" + p);
            }
        }
        return permissions;
    }

    /**
     * Get a list of user names that have a given permission.
     * Note: This is not a particularly efficient algorithm, and has an average worst case of O(n^3).
     * @param permission permission to get users under.
     * @return the list of users with a given permission.
     *         Each element of the returned list will be in the form "world:group:user" where 'world' is
     *         the world name the permission belongs to, and 'group' is the group the permission belongs to.
     */
    @Override
    public List<String> getUsersWithPermission(String permission) {
        PermissionUser[] allUsers = pexManager.getUsers();
        List<String> permissionUsers = new ArrayList<String>();

        for(PermissionUser u : allUsers) {
            for(String w : u.getWorlds()) {
                for(PermissionGroup g : u.getGroups(w)) {
                    if(g.has(permission))
                        permissionUsers.add(w + ":" + g.getName() + ":" + u.getName());
                }
            }
        }

        return permissionUsers;
    }

    /**
     * Check if a user has a given permission.
     *
     * @param username   The user to check for a permission.
     * @param permission The permission to check.
     * @param world      the name of the world the permission is under.
     * @return true if the user has the permission, false otherwise.
     */
    @Override
    public boolean userHasPermission(String username, String permission, String world) {
        return pexManager.has(username, permission, world);
    }

    /**
     * Get a list of worlds a user has a given permission.
     *
     * @param username   The user to check for a permission.
     * @param permission The permission to check.
     * @return A list of worlds the user has a permission under, null if the user does not exist.
     */
    @Override
    public List<String> getWorldsUserHasPermission(String username, String permission) {
        PermissionUser user = pexManager.getUser(username);
        if(user == null)
            return null;

        List<String> worlds = new ArrayList<String>();
        if(user.has(permission))
            worlds.add("default");
        for(String w : user.getWorlds()) {
            if(user.has(permission, w))
                worlds.add(w);
        }

        return worlds;
    }

}
