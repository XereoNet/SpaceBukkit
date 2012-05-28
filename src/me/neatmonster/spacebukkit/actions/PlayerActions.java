/*
 * This file is part of SpaceBukkit (http://spacebukkit.xereo.net/).
 *
 * SpaceBukkit is free software: you can redistribute it and/or modify it under the terms of the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license as published by the Creative
 * Common organization, either version 3.0 of the license, or (at your option) any later version.
 *
 * SpaceBukkit is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA) license for more details.
 *
 * You should have received a copy of the Attribution-NonCommercial-ShareAlike Unported (CC BY-NC-SA)
 * license along with this program. If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 */
package me.neatmonster.spacebukkit.actions;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.neatmonster.spacebukkit.players.PlayerLogger;
import me.neatmonster.spacebukkit.utilities.Utilities;
import me.neatmonster.spacemodule.api.Action;
import net.minecraft.server.ServerConfigurationManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Actions handler for any Player-related actions
 */
public class PlayerActions {

    /**
     * Throws an egg
     * @param playerName Player to throw the egg
     * @return If successful
     */
    @Action(
            aliases = {"throwEgg", "egg"},
            schedulable = false)
    public boolean throwEgg(final String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.launchProjectile(Egg.class);
            return true;
        }
        return false;
    }

    /**
     * Adds a player to the whitelist
     * @param playerName Player to add to whitelist
     * @return If successful
     */
    @Action(
            aliases = {"addToWhitelist", "whitelistAdd"})
    public boolean addToWhitelist(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setWhitelisted(true);
        PlayerLogger.setCase(playerName);
        return true;
    }

    /**
     * Bans a player
     * @param playerName  Player to ban
     * @return If successful
     */
    @Action(
            aliases = {"ban", "banPlayer", "bannedAdd"})
    public boolean ban(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setBanned(true);
        final Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null)
            onlinePlayer.kickPlayer("You have been banned!");
        PlayerLogger.setCase(playerName);
        return true;
    }

    /**
     * Sends a message to a player
     * @param playerName Player to send the message too
     * @param message Message to send to the player
     * @return If successful
     */
    @Action(
            aliases = {"chat", "talk"},
            schedulable = false)
    public Object chat(final String playerName, final String message) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.chat(Utilities.color(message));
            return true;
        }
        return false;
    }

    /**
     * Clears a players inventory slot
     * @param playerName Player to clear slot
     * @param slotNumber number of slot to clear
     * @return If successful
     */
    @Action(
            aliases = {"clearInventorySlot", "clearPlayerInventorySlot"},
            schedulable = false)
    public boolean clearInventorySlot(final String playerName, final int slotNumber) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final PlayerInventory inventory = player.getInventory();
            final int size = inventory.getSize();
            if (slotNumber == 103)
                inventory.clear(size + 3);
            else if (slotNumber == 102)
                inventory.clear(size + 2);
            else if (slotNumber == 101)
                inventory.clear(size + 1);
            else if (slotNumber == 100)
                inventory.clear(size + 0);
            else
                inventory.clear(slotNumber);
            return true;
        }
        return false;
    }

    /**
     * DeOP's a player
     * @param playerName Player to DeOP
     * @return If successful
     */
    @Action(
            aliases = {"deop", "deopPlayer"})
    public boolean deop(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setOp(false);
        final Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null)
            onlinePlayer.sendMessage("You are no longer OP!");
        return true;
    }

    /**
     * Gets all the banned players
     * @return All banned players
     */
    @Action(
            aliases = {"getBanned", "banned"})
    public List<String> getBanned() {
        try {
            final ServerConfigurationManager scm = ((CraftServer) Bukkit.getServer()).getHandle();
            final Method method = scm.getClass().getDeclaredMethod("l");
            method.invoke(scm);
        } catch (final Exception e) {}
        final List<String> playersNames = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getBannedPlayers())
            playersNames.add(PlayerLogger.getCase(player.getName()));
        return playersNames;
    }

    /**
     * Gets a players inventory
     * @param playerName Inventory to get
     * @return Players inventory
     */
    @Action(
            aliases = {"getInventory", "inventory"},
            schedulable = false)
    public LinkedHashMap<Integer, Map<String, Object>> getInventory(final String playerName) {
        final LinkedHashMap<Integer, Map<String, Object>> playerInventory = new LinkedHashMap<Integer, Map<String, Object>>();
        final Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return new LinkedHashMap<Integer, Map<String, Object>>();
        }
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            playerInventory.put(i, inv.getItem(i) == null ? new HashMap<String, Object>() : player
                    .getInventory().getItem(i).serialize());
        }
        playerInventory.put(100, inv.getBoots() == null ? new HashMap<String, Object>() : player
                .getInventory().getBoots().serialize());
        playerInventory.put(101, inv.getLeggings() == null ? new HashMap<String, Object>() : player
                .getInventory().getLeggings().serialize());
        playerInventory.put(102, inv.getChestplate() == null ? new HashMap<String, Object>() : player
                .getInventory().getChestplate().serialize());
        playerInventory.put(103, inv.getHelmet() == null ? new HashMap<String, Object>() : player
                .getInventory().getHelmet().serialize());
        return playerInventory;
    }

    /**
     * Gets the item of a player at the specified slot
     * @param playerName Player of item to get
     * @param slot Slot to get the item at
     * @return Item at the slot
     */
    @Action(
            aliases = {"getItem", "getItemAt"},
            schedulable = false)
    public Map<String, Object> getItem(final String playerName, final int slot) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player == null)
            return new HashMap<String, Object>();
        final ItemStack itemStack = player.getInventory().getItem(slot);
        if (itemStack == null)
            return new HashMap<String, Object>();
        return itemStack.serialize();
    }

    /**
     * Gets all the OPed players
     * @return All OPed players
     */
    @Action(
            aliases = {"getOps", "ops"})
    public List<String> getOPs() {
        final List<String> playerNames = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getOperators())
            playerNames.add(PlayerLogger.getCase(player.getName()));
        return playerNames;
    }

    /**
     * Gets basic information about a player
     * @param playerName Player to get information about
     * @return Basic information about a player
     */
    @Action(
            aliases = {"getPlayerInformations", "playerInformations"},
            schedulable = false)
    public LinkedHashMap<String, Object> getPlayerInformations(final String playerName) {
        final LinkedHashMap<String, Object> playerInformations = new LinkedHashMap<String, Object>();
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            playerInformations.put("Name", player.getName());
            playerInformations.put("DisplayName", Utilities.stripColor(player.getDisplayName()));
            playerInformations.put("EntityId", player.getEntityId());
            playerInformations.put("World", player.getLocation().getWorld().getName());
            playerInformations.put("X", player.getLocation().getX());
            playerInformations.put("Y", player.getLocation().getY());
            playerInformations.put("Z", player.getLocation().getZ());
            playerInformations.put("Exhaustion", player.getExhaustion());
            playerInformations.put("Experience", player.getExp());
            playerInformations.put("FoodLevel", player.getFoodLevel());
            playerInformations.put("GameMode", player.getGameMode().toString());
            playerInformations.put("Health", player.getHealth());
            playerInformations.put("Level", player.getLevel());
            playerInformations.put("RemainingAir", player.getRemainingAir());
            playerInformations.put("TotalExperience", player.getTotalExperience());
            return playerInformations;
        }
        return new LinkedHashMap<String, Object>();
    }

    /**
     * Gets all online players
     * @return All online players
     */
    @Action(
            aliases = {"getPlayers", "getOnlinePlayers", "players"})
    public List<String> getPlayers() {
        final List<String> playersNames = new ArrayList<String>();
        for (final Player player : Bukkit.getOnlinePlayers())
            playersNames.add(player.getName());
        return playersNames;
    }

    /**
     * Gets all whitelisted players 
     * @return All whitelisted players
     */
    @Action(
            aliases = {"getWhitelisted", "getWhitelist", "whitelist"})
    public List<String> getWhitelisted() {
        Bukkit.reloadWhitelist();
        final List<String> playerNames = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getWhitelistedPlayers())
            playerNames.add(PlayerLogger.getCase(player.getName()));
        return playerNames;
    }

    /**
     * Gives a player an item
     * @param playerName Player to give an item too
     * @param aID Id of the item to give
     * @param aAmount Amount of the item to give
     * @return If successful
     */
    @Action(
            aliases = {"giveItem", "give"},
            schedulable = false)
    public boolean giveItem(final String playerName, final int aID, final int aAmount, final byte data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final ItemStack stack = new ItemStack(aID, aAmount, (short) 0, data);
            final PlayerInventory inventory = player.getInventory();
            final int maxStackSize = stack.getMaxStackSize();
            if (stack.getAmount() <= maxStackSize)
                inventory.addItem(stack);
                final int amount = stack.getAmount();
                final int quotient = amount / maxStackSize;
                final int remainder = amount % maxStackSize;
                for (int i = 0; i < quotient; i++)
                    inventory.addItem(new ItemStack(aID, maxStackSize, (short) 0, data));
                if (remainder > 0)
                    inventory.addItem(new ItemStack(aID, remainder, (short) 0, data));
                return true;
            }
        return false;
    }

    /**
     * Drops and item at a players location
     * @param playerName Player whos location is to use
     * @param id Id of item to drop
     * @param amount Amount of item to drop
     * @return If successful
     */
    @Action(
            aliases = {"giveItemDrop", "giveDrop"},
            schedulable = false)
    public boolean giveItemDrop(final String playerName, final int id, final int amount, final byte data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final ItemStack stack = new ItemStack(id, amount, (short) 0, data);
            player.getWorld().dropItemNaturally(player.getLocation(), stack);
            return true;
        }
        return false;
    }

    /**
     * Checks if a player has a permission
     * @param playerName Player to check
     * @param permission Permission to check
     * @return If the player has the permission
     */
    @Action(
            aliases = {"hasPermission", "permission"},
            schedulable = false)
    public boolean hasPermission(final String playerName, final String permission) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null && !permission.equals(""))
            return player.hasPermission(permission);
        return false;
    }

    /**
     * Kicks a player
     * @param playerName Player to kick
     * @param message Reason to kick the player
     * @return If successful
     */
    @Action(
            aliases = {"kick", "kickPlayer"},
            schedulable = false)
    public boolean kick(final String playerName, final String message) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.kickPlayer(message);
            return true;
        }
        return false;
    }

    /**
     * Kills a player
     * @param playerName Player to kill
     * @return If successful
     */
    @Action(
            aliases = {"kill", "killPlayer"},
            schedulable = false)
    public boolean killPlayer(final String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final EntityDamageEvent event = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(event);
            player.setHealth(0);
            return true;
        }
        return false;
    }

    /**
     * Sends a player a message
     * @param playerName Player to message
     * @param message Message to send
     * @return If successful
     */
    @Action(
            aliases = {"message", "sendMessage", "msg", "pm"},
            schedulable = false)
    public boolean message(final String playerName, String message) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            try {
                message = URLDecoder.decode(message, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message = Utilities.color(message);
            if (!message.isEmpty()) {
                player.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    /**
     * OP's a player
     * @param playerName Player to OP
     * @return If successful
     */
    @Action(
            aliases = {"op", "opPlayer"})
    public boolean op(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setOp(true);
        final Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null)
            onlinePlayer.sendMessage("You are now OP!");
        PlayerLogger.setCase(playerName);
        return true;
    }

    /**
     * Preforms a command as a player
     * @param playerName Player to preform the command as
     * @param command Command to preform, without the '/' in front of it
     * @return If successful
     */
    @Action(
            aliases = {"performCommand", "playerCommand"},
            schedulable = false)
    public boolean performCommand(final String playerName, final String command) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            if (!command.equals(""))
                return player.performCommand(command);
        return false;
    }

    /**
     * Removes a player from the whitelist
     * @param playerName Player to remove from the whitelist
     * @return If successful
     */
    @Action(
            aliases = {"removeFromWhitelist", "whitelistRemove"})
    public boolean removeFromWhitelist(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setWhitelisted(false);
        return true;
    }

    /**
     * Removes an item from a players inventory
     * @param playerName Player to remove the item from
     * @param id Id of the item to remove
     * @return If successful
     */
    @Action(
            aliases = {"removeInventoryItem", "remove"},
            schedulable = false)
    public boolean removeInventoryItem(final String playerName, final int id) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.getInventory().removeItem(new ItemStack(id));
            return true;
        }
        return false;
    }

    /**
     * Sets the food level of a player
     * @param playerName Player to set the food level of
     * @param foodLevel New food level of player
     * @return If successful
     */
    @Action(
            aliases = {"setFoodLevel", "foodLevel", "food"},
            schedulable = false)
    public boolean setFoodLevel(final String playerName, final int foodLevel) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null) {
            player.setFoodLevel(foodLevel);
            return true;
        }
        return false;
    }

    /**
     * Sets the game mode of a player
     * 0 = Survival
     * 1 = Creative
     * @param playerName Player to set the game mode of
     * @param gameMode Game mode to set
     * @return If successful
     */
    @Action(
            aliases = {"setGameMode", "gameMode"},
            schedulable = false)
    public boolean setGameMode(final String playerName, final int gameMode) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null) {
            if (gameMode == 1)
                player.setGameMode(GameMode.CREATIVE);
            else
                player.setGameMode(GameMode.SURVIVAL);
            return true;
            }
        return false;
    }

    /**
     * Sets the health of a player
     * @param playerName Player to set the health of
     * @param health New health of player
     * @return If successful
     */
    @Action(
            aliases = {"setHealth", "health"},
            schedulable = false)
    public boolean setHealth(final String playerName, final int health) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null) {
            player.setHealth(health);
            return true;
            } 
        return false;
    }

    /**
     * Sets a slot of a players inventory
     * @param playerName Player to set
     * @param slotNumber Slot number of the players inventory to set
     * @param id Id to slot to set
     * @param amount Amount of slot to set
     * @param damage Damage of slot to set
     * @param data Data of slot to set
     * @return If successful
     */
    @Action(
            aliases = {"setInventorySlot", "setInventory"},
            schedulable = false)
    public boolean setInventorySlot(final String playerName, final int slotNumber, final int id, final int amount, final short damage, final byte data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final PlayerInventory inventory = player.getInventory();
            final ItemStack stack = new ItemStack(id, amount, damage, data);
            if (slotNumber == 103)
                inventory.setHelmet(stack);
            else if (slotNumber == 102)
                inventory.setChestplate(stack);
            else if (slotNumber == 101)
                inventory.setLeggings(stack);
            else if (slotNumber == 100)
                inventory.setBoots(stack);
            else
                inventory.setItem(slotNumber, stack);
            return true;
            }
        return false;
    }

    /**
     * Throws a snowball as a player
     * @param playerName Player to throw the snowball as
     * @return If successful
     */
    @Action(
            aliases = {"throwSnowball", "snowball"},
            schedulable = false)
    public boolean snowball(final String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.launchProjectile(Snowball.class);
            return true;
        }
        return false;
    }

    /**
     * Teleports a player to a location
     * @param playerName Player to teleport
     * @param worldName World to teleport the player to
     * @param x X to teleport the player too
     * @param y Y to teleport the player too
     * @param z Z to teleport the player too
     * @return If successful
     */
    @Action(
            aliases = {"teleport"},
            schedulable = false)
    public boolean teleport(final String playerName, final String worldName, final double x, final double y,
            final double z) {
        final Player player = Bukkit.getPlayer(playerName);
        final World world = Bukkit.getWorld(worldName);
        if (player != null && world != null) {
            player.teleport(new Location(world, x, y, z));
            return true;
        }
        return false;
    }

    /**
     * Unbans a player
     * @param playerName Player to unban
     * @return If successful
     */
    @Action(
            aliases = {"unban", "unbanPlayer", "bannedRemove"})
    public boolean unban(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setBanned(false);
        return true;
    }

    /**
     * Updates an inventory slot 
     * @param playerName Player whos inventory is to update
     * @param slotNumber Slot number of the players inventory to update
     * @param id Id of the slot to set
     * @param amount Amount of the slot to set
     * @param damage Damage of the slot to set
     * @param data Data of the slot to set
     * @return If successful
     */
    @Action(
            aliases = {"updatePlayerInventorySlot", "update"},
            schedulable = false)
    public boolean updateInventorySlot(final String playerName, final int slotNumber, final int id, final int amount, final short damage, final byte data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.getInventory().setItem(slotNumber, new ItemStack(id, amount, damage, data));
            return true;
        }
        return false;
    }
}
