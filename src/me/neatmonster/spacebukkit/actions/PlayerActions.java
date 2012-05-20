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
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

public class PlayerActions {

    @Action(
            aliases = {"throwEgg", "egg"},
            schedulable = false)
    public static boolean throwEgg(final String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            player.launchProjectile(Egg.class);
            return true;
        }
        return false;
    }

    @Action(
            aliases = {"addToWhitelist", "whitelistAdd"})
    public boolean addToWhitelist(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setWhitelisted(true);
        PlayerLogger.setCase(playerName);
        return true;
    }

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

    @Action(
            aliases = {"clearInventorySlot", "clearPlayerInventorySlot"},
            schedulable = false)
    public boolean clearInventorySlot(final String playerName, final int slotNumber) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            final PlayerInventory inventory = Bukkit.getPlayer(playerName).getInventory();
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

    @Action(
            aliases = {"deop", "deopPlayer"})
    public boolean deop(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setOp(false);
        final Player onlinePlayer = Bukkit.getPlayer(playerName);
        if (onlinePlayer != null)
            onlinePlayer.sendMessage("You are no longer OP!");
        return true;
    }

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

    @Action(
            aliases = {"getInventory", "inventory"},
            schedulable = false)
    public LinkedHashMap<Integer, Map<String, Object>> getInventory(final String playerName) {
        final LinkedHashMap<Integer, Map<String, Object>> playerInventory = new LinkedHashMap<Integer, Map<String, Object>>();
        final Player player = Bukkit.getPlayer(playerName);
        if (player == null)
            return new LinkedHashMap<Integer, Map<String, Object>>();
        for (int i = 0; i < player.getInventory().getSize(); i++)
            playerInventory.put(i, player.getInventory().getItem(i) == null ? new HashMap<String, Object>() : player
                    .getInventory().getItem(i).serialize());
        playerInventory.put(100, player.getInventory().getBoots() == null ? new HashMap<String, Object>() : player
                .getInventory().getBoots().serialize());
        playerInventory.put(101, player.getInventory().getLeggings() == null ? new HashMap<String, Object>() : player
                .getInventory().getLeggings().serialize());
        playerInventory.put(102, player.getInventory().getChestplate() == null ? new HashMap<String, Object>() : player
                .getInventory().getChestplate().serialize());
        playerInventory.put(103, player.getInventory().getHelmet() == null ? new HashMap<String, Object>() : player
                .getInventory().getHelmet().serialize());
        return playerInventory;
    }

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

    @Action(
            aliases = {"getOps", "ops"})
    public List<String> getOPs() {
        final List<String> playerNames = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getOperators())
            playerNames.add(PlayerLogger.getCase(player.getName()));
        return playerNames;
    }

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

    @Action(
            aliases = {"getPlayers", "getOnlinePlayers", "players"})
    public List<String> getPlayers() {
        final List<String> playersNames = new ArrayList<String>();
        for (final Player player : Bukkit.getOnlinePlayers())
            playersNames.add(player.getName());
        return playersNames;
    }

    @Action(
            aliases = {"getWhitelisted", "getWhitelist", "whitelist"})
    public List<String> getWhitelisted() {
        Bukkit.reloadWhitelist();
        final List<String> playerNames = new ArrayList<String>();
        for (final OfflinePlayer player : Bukkit.getWhitelistedPlayers())
            playerNames.add(PlayerLogger.getCase(player.getName()));
        return playerNames;
    }

    @Action(
            aliases = {"giveItem", "give"},
            schedulable = false)
    public boolean giveItem(final String playerName, final int aID, final int aAmount) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final ItemStack stack = new ItemStack(aID, aAmount);
                final PlayerInventory inventory = player.getInventory();
                if (stack.getAmount() <= 64)
                    inventory.addItem(stack);
                final int id = stack.getTypeId();
                final int amount = stack.getAmount();
                final short durability = stack.getDurability();
                final Byte data = stack.getData() != null ? stack.getData().getData() : null;
                final int quotient = amount / 64;
                final int remainder = amount % 64;
                for (int i = 0; i < quotient; i++)
                    inventory.addItem(new ItemStack(id, 64, durability, data));
                if (remainder > 0)
                    inventory.addItem(new ItemStack(id, remainder, durability, data));
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"giveItemDrop", "giveDrop"},
            schedulable = false)
    public boolean giveItemDrop(final String playerName, final int id, final int amount) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final ItemStack stack = new ItemStack(id, amount);
                player.getWorld().dropItemNaturally(player.getLocation(), stack);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"giveItemDropWithData", "giveDropData"},
            schedulable = false)
    public boolean giveItemDropWithData(final String playerName, final int id, final int amount, final short data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final ItemStack stack = new ItemStack(id, amount, data);
                player.getWorld().dropItemNaturally(player.getLocation(), stack);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"giveItemWithData", "giveData"},
            schedulable = false)
    public boolean giveItemWithData(final String playerName, final int aID, final int aAmount, final short aData) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final ItemStack stack = new ItemStack(aID, aAmount, aData);
                final PlayerInventory inventory = player.getInventory();
                if (stack.getAmount() <= 64)
                    inventory.addItem(stack);
                final int id = stack.getTypeId();
                final int amount = stack.getAmount();
                final short durability = stack.getDurability();
                final Byte data = stack.getData() != null ? stack.getData().getData() : null;
                final int quotient = amount / 64;
                final int remainder = amount % 64;
                for (int i = 0; i < quotient; i++)
                    inventory.addItem(new ItemStack(id, 64, durability, data));
                if (remainder > 0)
                    inventory.addItem(new ItemStack(id, remainder, durability, data));
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"hasPermission", "permission"},
            schedulable = false)
    public boolean hasPermission(final String playerName, final String permission) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null && !permission.equals(""))
            return player.hasPermission(permission);
        return false;
    }

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

    @Action(
            aliases = {"removeFromWhitelist", "whitelistRemove"})
    public boolean removeFromWhitelist(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setWhitelisted(false);
        return true;
    }

    @Action(
            aliases = {"removeInventoryItem", "remove"},
            schedulable = false)
    public boolean removeInventoryItem(final String playerName, final int id) {
        try {
            Bukkit.getPlayer(playerName).getInventory().removeItem(new ItemStack(id));
            return true;
        } catch (final NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Action(
            aliases = {"setFoodLevel", "foodLevel", "food"},
            schedulable = false)
    public boolean setFoodLevel(final String playerName, final int foodLevel) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null)
            try {
                player.setFoodLevel(foodLevel);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setGameMode", "gameMode"},
            schedulable = false)
    public boolean setGameMode(final String playerName, final int gameMode) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null)
            try {
                if (gameMode == 1)
                    player.setGameMode(GameMode.CREATIVE);
                else
                    player.setGameMode(GameMode.SURVIVAL);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setHealth", "health"},
            schedulable = false)
    public boolean setHealth(final String playerName, final int health) {
        final Player player = Bukkit.getServer().getPlayer(playerName);
        if (player != null)
            try {
                player.setHealth(health);
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setInventorySlot", "setInventory"},
            schedulable = false)
    public boolean setInventorySlot(final String playerName, final int slotNumber, final int id, final int amount) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack stack = new ItemStack(id, amount);
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
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setInventorySlotWithDamage", "setInventoryDamage"},
            schedulable = false)
    public boolean setInventorySlotWithDamage(final String playerName, final int slotNumber, final int id,
            final int amount, final short damage) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack stack = new ItemStack(id, amount, damage);
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
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setInventorySlotWithDataAndDamage", "setInventoryDataDamage"},
            schedulable = false)
    public boolean setInventorySlotWithDataAndDamage(final String playerName, final int slotNumber, final int id,
            final int amount, final int data, final short damage) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack stack = new MaterialData(id, (byte) data).toItemStack(amount);
                stack.setDurability(damage);
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
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"setPlayerInventorySlotWithData", "setInventoryData"},
            schedulable = false)
    public boolean setPlayerInventorySlotWithData(final String playerName, final int slotNumber, final int id,
            final int amount, final int data) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                final PlayerInventory inventory = player.getInventory();
                final ItemStack stack = new MaterialData(id, (byte) data).toItemStack(amount);
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
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

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

    @Action(
            aliases = {"teleport"},
            schedulable = false)
    public boolean teleport(final String playerName, final String worldName, final double x, final double y,
            final double z) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player != null)
            try {
                player.teleport(new Location(Bukkit.getWorld(worldName), x, y, z));
                return true;
            } catch (final Exception e) {
                e.printStackTrace();
            }
        return false;
    }

    @Action(
            aliases = {"unban", "unbanPlayer", "bannedRemove"})
    public boolean unban(final String playerName) {
        Bukkit.getOfflinePlayer(playerName).setBanned(false);
        return true;
    }

    @Action(
            aliases = {"updatePlayerInventorySlot", "update"},
            schedulable = false)
    public boolean updateInventorySlot(final String playerName, final int slotNumber, final int amount) {
        try {
            final ItemStack stack = Bukkit.getPlayer(playerName).getInventory().getItem(slotNumber);
            stack.setAmount(amount);
            Bukkit.getPlayer(playerName).getInventory().setItem(slotNumber, stack);
            return true;
        } catch (final NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }
}
