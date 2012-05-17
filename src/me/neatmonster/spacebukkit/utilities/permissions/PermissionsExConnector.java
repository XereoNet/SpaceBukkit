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
package me.neatmonster.spacebukkit.utilities.permissions;

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
    private String version = "1.19.2";

    public PermissionsExConnector(PermissionManager pexManager) {
        this.pexManager = pexManager;
    }

    public String getPermissionsPluginName() {
        return "PermissionsEx";
    }

    public String getPermissionsPluginVersion() {
        return version;
    }

    @Override
    public List<String> getUserNames() {
        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : pexManager.getUsers()) {
            userNames.add(u.getName());
        }

        return userNames;
    }

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

    @Override
    public List<String> getGroupNames() {
        List<String> groupNames = new ArrayList<String>();
        for(PermissionGroup g : pexManager.getGroups()) {
            groupNames.add(g.getName());
        }

        return groupNames;
    }

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

    @Override
    public List<String> getGroupUsers(String groupName, String world) {
        List<String> userNames = new ArrayList<String>();
        for(PermissionUser u : pexManager.getUsers(groupName, world)) {
            userNames.add(u.getName());
        }

        return userNames;
    }

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

    @Override
    public List<String> getGroupPermissions(String groupName, String world) {
        PermissionGroup group = pexManager.getGroup(groupName);
        if(group == null)
            return null;

        List<String> permissions = new ArrayList<String>();
        permissions.addAll(Arrays.asList(group.getPermissions(world)));

        return permissions;
    }

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

    @Override
    public boolean userHasPermission(String username, String permission, String world) {
        return pexManager.has(username, permission, world);
    }
    
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
