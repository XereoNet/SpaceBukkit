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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import me.neatmonster.spacebukkit.SpaceBukkit;

/**
 * Used for accessing and creating .[properties] files, reads them as utf-8, saves as utf-8.
 * Internationalization is key importance especially for character codes.
 *
 * @author Nijikokun
 * @version 1.0.4, %G%
 */
public final class PropertiesFile {
    private final String              fileName;
    private final List<String>        lines = new ArrayList<String>();
    private final Map<String, String> props = new HashMap<String, String>();

    /**
     * Creates or opens a properties file using specified filename
     *
     * @param fileName
     */
    public PropertiesFile(final String fileName) {
        this.fileName = fileName;
        final File file = new File(fileName);

        if (file.exists())
            try {
                load();
            } catch (final IOException ex) {
                SpaceBukkit.getInstance().getLogger().severe("[PropertiesFile] Unable to load " + fileName + "!");
            }
        else
            save();
    }

    /**
     * Checks to see if the .[properties] file contains the given <code>key</code>.
     *
     * @param var
     *            The key we are going to be checking the existance of.
     * @return <code>Boolean</code> - True if the <code>key</code> exists, false if it cannot be found.
     */
    public boolean containsKey(final String var) {
        for (final String line : lines) {
            if (line.trim().length() == 0)
                continue;

            if (line.charAt(0) == '#')
                continue;

            if (line.contains("=")) {
                final int delimPosition = line.indexOf('=');
                final String key = line.substring(0, delimPosition);

                if (key.equals(var))
                    return true;
            } else
                continue;
        }

        return false;
    }

    /**
     * Returns the value of the <code>key</code> given in a Boolean,
     * however we do not set a string if no <code>key</code> is found.
     *
     * @see #getProperty(String var)
     * @param key
     *            The <code>key</code> we will retrieve the property from, if no <code>key</code> is found default to
     *            false
     */
    public boolean getBoolean(final String key) {
        if (containsKey(key))
            return Boolean.parseBoolean(getProperty(key));

        return false;
    }

    /**
     * Returns the boolean value of a key
     *
     * @see #setBoolean(String key, boolean value)
     * @param key
     *            The key that we will be grabbing the value from, if no value is found set and return
     *            <code>value</code>
     * @param value
     *            The default value that we will be setting if no prior <code>key</code> is found.
     * @return <code>Boolean</code> - Either we will return the default value or a prior existing value depending on
     *         existance.
     */
    public boolean getBoolean(final String key, final boolean value) {
        if (containsKey(key))
            return Boolean.parseBoolean(getProperty(key));

        setBoolean(key, value);
        return value;
    }

    /**
     * Returns the value of the <code>key</code> given in a Double,
     * however we do not set a string if no <code>key</code> is found.
     *
     * @see #getProperty(String var)
     * @param key
     *            The <code>key</code> we will retrieve the property from, if no <code>key</code> is found default to
     *            0.0
     */
    public double getDouble(final String key) {
        if (containsKey(key))
            return Double.parseDouble(getProperty(key));

        return 0;
    }

    /**
     * Returns the double value of a key
     *
     * @see #setDouble(String key, double value)
     * @param key
     *            The key that we will be grabbing the value from, if no value is found set and return
     *            <code>value</code>
     * @param value
     *            The default value that we will be setting if no prior <code>key</code> is found.
     * @return <code>Double</code> - Either we will return the default value or a prior existing value depending on
     *         existance.
     */
    public double getDouble(final String key, final double value) {
        if (containsKey(key))
            return Double.parseDouble(getProperty(key));

        setDouble(key, value);
        return value;
    }

    /**
     * Returns the value of the <code>key</code> given in a Integer,
     * however we do not set a string if no <code>key</code> is found.
     *
     * @see #getProperty(String var)
     * @param key
     *            The <code>key</code> we will retrieve the property from, if no <code>key</code> is found default to 0
     */
    public int getInt(final String key) {
        if (containsKey(key))
            return Integer.parseInt(getProperty(key));

        return 0;
    }

    /**
     * Returns the int value of a key
     *
     * @see #setInt(String key, int value)
     * @param key
     *            The key that we will be grabbing the value from, if no value is found set and return
     *            <code>value</code>
     * @param value
     *            The default value that we will be setting if no prior <code>key</code> is found.
     * @return <code>Integer</code> - Either we will return the default value or a prior existing value depending on
     *         existance.
     */
    public int getInt(final String key, final int value) {
        if (containsKey(key))
            return Integer.parseInt(getProperty(key));

        setInt(key, value);
        return value;

    }

    /**
     * Returns the value of the <code>key</code> given in a Long,
     * however we do not set a string if no <code>key</code> is found.
     *
     * @see #getProperty(String var)
     * @param key
     *            The <code>key</code> we will retrieve the property from, if no <code>key</code> is found default to 0L
     */
    public long getLong(final String key) {
        if (containsKey(key))
            return Long.parseLong(getProperty(key));

        return 0;
    }

    /**
     * Returns the long value of a key
     *
     * @see #setLong(String key, long value)
     * @param key
     *            The key that we will be grabbing the value from, if no value is found set and return
     *            <code>value</code>
     * @param value
     *            The default value that we will be setting if no prior <code>key</code> is found.
     * @return <code>Long</code> - Either we will return the default value or a prior existing value depending on
     *         existance.
     */
    public long getLong(final String key, final long value) {
        if (containsKey(key))
            return Long.parseLong(getProperty(key));

        setLong(key, value);
        return value;
    }

    /**
     * Checks to see if this <code>key</code> exists in the .[properties] file.
     *
     * @param var
     *            The key we are grabbing the value of.
     * @return <code>java.lang.String</code> - True if the <code>key</code> exists, false if it cannot be found.
     */
    public String getProperty(final String var) {
        for (final String line : lines) {
            if (line.trim().length() == 0)
                continue;
            if (line.charAt(0) == '#')
                continue;

            if (line.contains("=")) {
                final int delimPosition = line.indexOf('=');
                final String key = line.substring(0, delimPosition).trim();
                final String value = line.substring(delimPosition + 1);

                if (key.equals(var))
                    return value;
            } else
                continue;
        }

        return "";
    }

    /**
     * Returns the value of the <code>key</code> given as a <code>String</code>,
     * however we do not set a string if no <code>key</code> is found.
     *
     * @see #getProperty(java.lang.String)
     * @param key
     *            The <code>key</code> we will retrieve the property from, if no <code>key</code> is found default to ""
     *            or empty.
     */
    public String getString(final String key) {
        if (containsKey(key))
            return getProperty(key);

        return "";
    }

    /**
     * Returns the value of the <code>key</code> given as a <code>String</code>.
     * If it is not found, it will invoke saving the default <code>value</code> to the properties file.
     *
     * @see #setString(java.lang.String, java.lang.String)
     * @see #getProperty(java.lang.String)
     * @param key
     *            The key that we will be grabbing the value from, if no value is found set and return
     *            <code>value</code>
     * @param value
     *            The default value that we will be setting if no prior <code>key</code> is found.
     * @return java.lang.String Either we will return the default value or a prior existing value depending on
     *         existance.
     */
    public String getString(final String key, final String value) {
        if (containsKey(key))
            return getProperty(key);

        setString(key, value);
        return value;
    }

    /**
     * Checks the existance of a <code>key</code>.
     *
     * @see #containsKey(java.lang.String)
     * @param key
     *            The <code>key</code> in question of existance.
     * @return <code>Boolean</code> - True for existance, false for <code>key</code> found.
     */
    public boolean keyExists(final String key) {
        try {
            return containsKey(key) ? true : false;
        } catch (final Exception ex) {
            return false;
        }
    }

    /**
     * The loader for property files, it reads the file as UTF8 or converts the string into UTF8.
     * Used for simple runthrough's, loading, or reloading of the file.
     *
     * @throws IOException
     */
    public void load() throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
        String line;

        // Clear the file & unwritten properties
        lines.clear();
        props.clear();

        // Begin reading the file.
        while ((line = reader.readLine()) != null) {
            line = new String(line.getBytes(), "UTF-8");
            char c = 0;
            int pos = 0;

            while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (line.length() - pos == 0 || line.charAt(pos) == '#' || line.charAt(pos) == '!') {
                lines.add(line);
                continue;
            }

            final int start = pos;
            final boolean needsEscape = line.indexOf('\\', pos) != -1;
            final StringBuffer key = needsEscape ? new StringBuffer() : null;

            while (pos < line.length() && !Character.isWhitespace(c = line.charAt(pos++)) && c != '=' && c != ':')
                if (needsEscape && c == '\\') {
                    if (pos == line.length()) {
                        line = reader.readLine();

                        if (line == null)
                            line = "";

                        pos = 0;

                        while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
                    } else {
                        c = line.charAt(pos++);

                        switch (c) {
                        case 'n':
                            key.append('\n');
                            break;
                        case 't':
                            key.append('\t');
                            break;
                        case 'r':
                            key.append('\r');
                            break;
                        case 'u':
                            if (pos + 4 <= line.length()) {
                                final char uni = (char) Integer.parseInt(line.substring(pos, pos + 4), 16);
                                key.append(uni);
                                pos += 4;
                            }

                            break;
                        default:
                            key.append(c);
                            break;
                        }
                    }
                } else if (needsEscape)
                    key.append(c);

            final boolean isDelim = c == ':' || c == '=';
            String keyString;

            if (needsEscape)
                keyString = key.toString();
            else if (isDelim || Character.isWhitespace(c))
                keyString = line.substring(start, pos - 1);
            else
                keyString = line.substring(start, pos);

            while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                pos++;

            if (!isDelim && (c == ':' || c == '=')) {
                pos++;

                while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                    pos++;
            }

            // Short-circuit if no escape chars found.
            if (!needsEscape) {
                lines.add(line);
                continue;
            }

            // Escape char found so iterate through the rest of the line.
            final StringBuilder element = new StringBuilder(line.length() - pos);
            while (pos < line.length()) {
                c = line.charAt(pos++);
                if (c == '\\') {
                    if (pos == line.length()) {
                        line = reader.readLine();

                        if (line == null)
                            break;

                        pos = 0;
                        while (pos < line.length() && Character.isWhitespace(c = line.charAt(pos)))
                            pos++;
                        element.ensureCapacity(line.length() - pos + element.length());
                    } else {
                        c = line.charAt(pos++);
                        switch (c) {
                        case 'n':
                            element.append('\n');
                            break;
                        case 't':
                            element.append('\t');
                            break;
                        case 'r':
                            element.append('\r');
                            break;
                        case 'u':
                            if (pos + 4 <= line.length()) {
                                final char uni = (char) Integer.parseInt(line.substring(pos, pos + 4), 16);
                                element.append(uni);
                                pos += 4;
                            }
                            break;
                        default:
                            element.append(c);
                            break;
                        }
                    }
                } else
                    element.append(c);
            }
            lines.add(keyString + "=" + element.toString());
        }

        reader.close();
    }

    /**
     * Remove a key from the file if it exists.
     * This will save() which will invoke a load() on the file.
     *
     * @see #save()
     * @param var
     *            The <code>key</code> that will be removed from the file
     */
    public void removeKey(final String var) {
        Boolean changed = false;

        if (props.containsKey(var)) {
            props.remove(var);
            changed = true;
        }

        try {
            for (int i = 0; i < lines.size(); i++) {
                final String line = lines.get(i);

                if (line.trim().length() == 0)
                    continue;

                if (line.charAt(0) == '#')
                    continue;

                if (line.contains("=")) {
                    final int delimPosition = line.indexOf('=');
                    final String key = line.substring(0, delimPosition).trim();

                    if (key.equals(var)) {
                        lines.remove(i);
                        changed = true;
                    }
                } else
                    continue;
            }
        } catch (final ConcurrentModificationException concEx) {
            removeKey(var);
            return;
        }

        // Save on change
        if (changed)
            save();
    }

    /**
     * Returns a Map of all <code>key=value</code> properties in the file as
     * <code>&lt;key (java.lang.String), value (java.lang.String)></code> <br />
     * <br />
     * Example:
     * <blockquote>
     *
     * <pre>
     * PropertiesFile settings = new PropertiesFile(&quot;settings.properties&quot;);
     * Map&lt;String, String&gt; mappedSettings;
     *
     * try {
     *     mappedSettings = settings.returnMap();
     * } catch (Exception ex) {
     *     log.info(&quot;Failed mapping settings.properties&quot;);
     * }
     * </pre>
     *
     * </blockquote>
     *
     * @return <code>map</code> - Simple Map HashMap of the entire <code>key=value</code> as
     *         <code>&lt;key (java.lang.String), value (java.lang.String)></code>
     * @throws Exception
     *             If the properties file doesn't exist.
     */
    public Map<String, String> returnMap() throws Exception {
        final Map<String, String> map = new HashMap<String, String>();
        final BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0)
                continue;

            if (line.charAt(0) == '#')
                continue;

            if (line.contains("=")) {
                final int delimPosition = line.indexOf('=');
                final String key = line.substring(0, delimPosition).trim();
                final String value = line.substring(delimPosition + 1).trim();
                map.put(key, value);
            } else
                continue;
        }

        reader.close();
        return map;
    }

    /**
     * Writes out the <code>key=value</code> properties that were changed into
     * a .[properties] file in UTF8.
     *
     * @see #load()
     */
    public void save() {
        OutputStream os = null;

        try {
            os = new FileOutputStream(new File(fileName));
        } catch (final FileNotFoundException ex) {
            SpaceBukkit.getInstance().getLogger().severe("[PropertiesFile] Unable to open " + fileName + "!");
        }

        PrintStream ps = null;
        try {
            ps = new PrintStream(os, true, "UTF-8");
        } catch (final UnsupportedEncodingException ex) {
            SpaceBukkit.getInstance().getLogger().severe("[PropertiesFile] Unable to write to " + fileName + "!");
        }

        // Keep track of properties that were set
        final List<String> usedProps = new ArrayList<String>();

        for (final String line : lines) {
            if (line.trim().length() == 0) {
                ps.println(line);
                continue;
            }

            if (line.charAt(0) == '#') {
                ps.println(line);
                continue;
            }

            if (line.contains("=")) {
                final int delimPosition = line.indexOf('=');
                final String key = line.substring(0, delimPosition).trim();

                if (props.containsKey(key)) {
                    final String value = props.get(key);
                    ps.println(key + "=" + value);
                    usedProps.add(key);
                } else
                    ps.println(line);
            } else
                ps.println(line);
        }

        // Add any new properties
        for (final Map.Entry<String, String> entry : props.entrySet())
            if (!usedProps.contains(entry.getKey()))
                ps.println(entry.getKey() + "=" + entry.getValue());

        // Exit that stream
        ps.flush();
        ps.close();
        try {
            os.flush();
            os.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Reload
        try {
            props.clear();
            lines.clear();
            load();
        } catch (final IOException ex) {
            SpaceBukkit.getInstance().getLogger().severe("[PropertiesFile] Unable to load " + fileName + "!");
        }
    }

    /**
     * Save the value given as a <code>boolean</code> on the specified key.
     *
     * @see #save()
     * @param key
     *            The <code>key</code> that we will be addressing the <code>value</code> to.
     * @param value
     *            The <code>value</code> we will be setting inside the <code>.[properties]</code> file.
     */
    public void setBoolean(final String key, final boolean value) {
        props.put(key, String.valueOf(value));

        save();
    }

    /**
     * Save the value given as a <code>double</code> on the specified key.
     *
     * @see #save()
     * @param key
     *            The <code>key</code> that we will be addressing the <code>value</code> to.
     * @param value
     *            The <code>value</code> we will be setting inside the <code>.[properties]</code> file.
     */
    public void setDouble(final String key, final double value) {
        props.put(key, String.valueOf(value));

        save();
    }

    /**
     * Save the value given as a <code>int</code> on the specified key.
     *
     * @see #save()
     * @param key
     *            The <code>key</code> that we will be addressing the <code>value</code> to.
     * @param value
     *            The <code>value</code> we will be setting inside the <code>.[properties]</code> file.
     */
    public void setInt(final String key, final int value) {
        props.put(key, String.valueOf(value));

        save();
    }

    /**
     * Save the value given as a <code>long</code> on the specified key.
     *
     * @see #save()
     * @param key
     *            The <code>key</code> that we will be addressing the <code>value</code> to.
     * @param value
     *            The <code>value</code> we will be setting inside the <code>.[properties]</code> file.
     */
    public void setLong(final String key, final long value) {
        props.put(key, String.valueOf(value));

        save();
    }

    /**
     * Save the value given as a <code>String</code> on the specified key.
     *
     * @see #save()
     * @param key
     *            The <code>key</code> that we will be addressing the <code>value</code> to.
     * @param value
     *            The <code>value</code> we will be setting inside the <code>.[properties]</code> file.
     */
    public void setString(final String key, final String value) {
        props.put(key, value);

        save();
    }
}
