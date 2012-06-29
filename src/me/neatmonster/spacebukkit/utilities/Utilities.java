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
package me.neatmonster.spacebukkit.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import me.neatmonster.spacebukkit.SpaceBukkit;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * Various Utility methods
 */
public class Utilities {

    /**
     * Adds the HTTP header to a string
     * @param string String to add to
     * @return String with the header
     * @throws UnsupportedEncodingException If the encoding is not UTF-8
     */
    public static String addHeader(final String string) throws UnsupportedEncodingException {
        String finishedString = "";
        String byteLengthOfFinishedString = "";
        final String newLine = "\r\n";
        if (string != null) {
            byteLengthOfFinishedString = Integer.toString(string.getBytes("UTF8").length);
            finishedString = finishedString + "HTTP/1.1 200 OK" + newLine;
            finishedString = finishedString + "Content-Language:en" + newLine;
            finishedString = finishedString + "Content-Length:" + byteLengthOfFinishedString + newLine;
            finishedString = finishedString + "Content-Type:text/plain; charset=utf-8" + newLine;
            finishedString = finishedString + newLine;
            finishedString = finishedString + string;
        } else {
            finishedString = finishedString + "HTTP/1.1 500 Internal Server Error" + newLine;
            finishedString = finishedString + "Content-Language:en" + newLine;
            finishedString = finishedString + "Content-Length:0" + newLine;
            finishedString = finishedString + "Content-Type:text/html; charset=utf-8" + newLine;
            finishedString = finishedString + newLine;
        }
        return finishedString;
    }

    /**
     * Converts a String from text colors to color
     * @param message Message to color
     * @return String with color codes
     */
    public static String color(String message) {
        int index = 0;
        while (true) {
            index = message.indexOf("$", index);
            if (index >= 0 && index < message.length() - 1) {
                final String letter = message.substring(index + 1, index + 2);
                String replace = "$" + letter;
                if (letter.equals("0"))
                    replace = ChatColor.BLACK.toString();
                else if (letter.equals("1"))
                    replace = ChatColor.DARK_BLUE.toString();
                else if (letter.equals("2"))
                    replace = ChatColor.DARK_GREEN.toString();
                else if (letter.equals("3"))
                    replace = ChatColor.DARK_AQUA.toString();
                else if (letter.equals("4"))
                    replace = ChatColor.DARK_RED.toString();
                else if (letter.equals("5"))
                    replace = ChatColor.DARK_PURPLE.toString();
                else if (letter.equals("6"))
                    replace = ChatColor.GOLD.toString();
                else if (letter.equals("7"))
                    replace = ChatColor.GRAY.toString();
                else if (letter.equals("8"))
                    replace = ChatColor.DARK_GRAY.toString();
                else if (letter.equals("9"))
                    replace = ChatColor.BLUE.toString();
                else if (letter.equals("a"))
                    replace = ChatColor.GREEN.toString();
                else if (letter.equals("b"))
                    replace = ChatColor.AQUA.toString();
                else if (letter.equals("c"))
                    replace = ChatColor.RED.toString();
                else if (letter.equals("d"))
                    replace = ChatColor.LIGHT_PURPLE.toString();
                else if (letter.equals("e"))
                    replace = ChatColor.YELLOW.toString();
                else if (letter.equals("f"))
                    replace = ChatColor.WHITE.toString();
                message = message.substring(0, index) + replace + message.substring(index + 2);
                index += 1;
            } else
                break;
            index += 1;
        }
        return message;
    }

    /**
     * Encrypts a string with 
     * @param string String to encrypt
     * @return String encrypted with hex
     * @throws NoSuchAlgorithmException If SHA-256 or UTF-8 is not supported
     */
    public static String crypt(final String string) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        byte[] input = null;
        try {
            input = digest.digest(string.getBytes("UTF-8"));
            final StringBuffer hexString = new StringBuffer();
            for (final byte element : input) {
                final String hex = Integer.toHexString(0xFF & element);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "UnsupportedEncodingException";
    }

    /**
     * Formats time from long to a readable String
     * @param time Raw time input
     * @return Readable time
     */
    public static String formatTime(final long time) {
        final int hours = (int) ((time / 1000 + 8) % 24);
        final int minutes = (int) (60 * (time % 1000) / 1000);
        return String.format("%02d:%02d (%d:%02d %s)", hours, minutes, hours % 12 == 0 ? 12 : hours % 12, minutes,
                hours < 12 ? "am" : "pm");
    }

    /**
     * Sends a method to the panel from the plugin
     * @param method Method to send
     * @param arguments Arguments to that method
     * @return Result of the method
     */
    public static String sendMethod(final String method, final String arguments) {
        try {
            final URL url = new URL("http://localhost:" + SpaceBukkit.getInstance().rPort + "/call?method=" + method
                    + "&args=" + arguments + "&key=" + Utilities.crypt(method + SpaceBukkit.getInstance().salt));
            final URLConnection connection = url.openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuffer.append(line);
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (final ConnectException e) {
            SpaceBukkit.getInstance().getLogger().severe("----------------------------------------------------------");
            SpaceBukkit.getInstance().getLogger().severe("| SpaceRTK cannot be reached, please make sure you have  |");
            SpaceBukkit.getInstance().getLogger().severe("| RemoteToolkit installed, SpaceRTK placed in /toolkit   |");
            SpaceBukkit.getInstance().getLogger().severe("| /modules. Otherwise report this issue on our issues    |");
            SpaceBukkit.getInstance().getLogger().severe("| tracker (http://bit.ly/spacebukkitissues).             |");
            SpaceBukkit.getInstance().getLogger().severe("----------------------------------------------------------");
            e.printStackTrace();
            try {
                SpaceBukkit.getInstance().getLogger().severe("http://localhost:" + SpaceBukkit.getInstance().rPort + "/call?method=" + method
                        + "&args=" + arguments + "&key=" + Utilities.crypt(SpaceBukkit.getInstance().salt));
            } catch (final NoSuchAlgorithmException e_) {
                e_.printStackTrace();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Strips a string of any color codes
     * @param string String to strip
     * @return String without color codes
     */
    public static String stripColor(String string) {
        string = ChatColor.stripColor(string);
        int index = 0;
        while ((index = string.indexOf('&', index)) >= 0) {
            final char char_ = string.charAt(index + 1);
            if (char_ == '&')
                string = string.substring(0, index) + string.substring(index + 1);
            else
                string = string.substring(0, index) + string.substring(index + 2);
            index++;
        }
        return string;
    }
    
    /**
     * Serializes an item with the extra data "id" which contains the item id
     * @param stack Stack to serialize
     * @return Serialized item
     */
    public static Map<String, Object> serializeItem(ItemStack stack) {
        Map<String, Object> result = stack.serialize();
        result.put("type", stack.getType().toString());
        result.put("id", stack.getTypeId());
        result.put("amount", stack.getAmount());
        result.put("data", stack.getData().getData());
        return result;
    }
}
