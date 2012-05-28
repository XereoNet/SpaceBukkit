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

import java.util.LinkedList;
import java.util.List;

/**
 * Set of ANSI color strings
 */
public class ANSI {

    /**
     * Represents Background Black
     */
    public static final String BACKGROUND_BLACK   = "\u001B[40m";
    /**
     * Represents Background Blue
     */
    public static final String BACKGROUND_BLUE    = "\u001B[44m";
    /**
     * Represents Background Cyan
     */
    public static final String BACKGROUND_CYAN    = "\u001B[46m";
    /**
     * Represents Background Green
     */
    public static final String BACKGROUND_GREEN   = "\u001B[42m";
    /**
     * Represents Background Magenta
     */
    public static final String BACKGROUND_MAGENTA = "\u001B[45m";
    /**
     * Represents Background Red
     */
    public static final String BACKGROUND_RED     = "\u001B[41m";
    /**
     * Represents Background White
     */
    public static final String BACKGROUND_WHITE   = "\u001B[47m";
    /**
     * Represents Background Yellow
     */
    public static final String BACKGROUND_YELLOW  = "\u001B[43m";
    /**
     * Represents Black
     */
    public static final String BLACK              = "\u001B[30m";
    /**
     * Represents Blink
     */
    public static final String BLINK              = "\u001B[5m";
    /**
     * Represents Blue
     */
    public static final String BLUE               = "\u001B[34m";
    /**
     * Represents Cyan
     */
    public static final String CYAN               = "\u001B[36m";
    /**
     * Represents Green
     */
    public static final String GREEN              = "\u001B[32m";
    /**
     * Represents High Intensity
     */
    public static final String HIGH_INTENSITY     = "\u001B[1m";
    /**
     * Represents Invisible Text
     */
    public static final String INVISIBLE_TEXT     = "\u001B[8m";
    /**
     * Represents Italics
     */
    public static final String ITALIC             = "\u001B[3m";
    /**
     * Represents Low Intensity
     */
    public static final String LOW_INTESITY       = "\u001B[2m";
    /**
     * Represents Magenta
     */
    public static final String MAGENTA            = "\u001B[35m";
    /**
     * Represents Rapid Blink
     */
    public static final String RAPID_BLINK        = "\u001B[6m";
    /**
     * Represents Red
     */
    public static final String RED                = "\u001B[31m";
    /**
     * Represents Reverse Video
     */
    public static final String REVERSE_VIDEO      = "\u001B[7m";
    /**
     * Represents Sane
     */
    public static final String SANE               = "\u001B[0m";
    /**
     * Represents Underline
     */
    public static final String UNDERLINE          = "\u001B[4m";
    /**
     * Represents White
     */
    public static final String WHITE              = "\u001B[37m";
    /**
     * Represents Yellow
     */
    public static final String YELLOW             = "\u001B[33m";

    /**
     * Converts all colors from ANSI to Non-ANSI
     * @param string Input
     * @return Non ANSI string
     */
    public static String noANSI(String string) {
        final List<String> characters = new LinkedList<String>();
        characters.add(SANE);
        characters.add(HIGH_INTENSITY);
        characters.add(LOW_INTESITY);
        characters.add(ITALIC);
        characters.add(UNDERLINE);
        characters.add(BLINK);
        characters.add(RAPID_BLINK);
        characters.add(REVERSE_VIDEO);
        characters.add(INVISIBLE_TEXT);
        characters.add(BLACK);
        characters.add(RED);
        characters.add(GREEN);
        characters.add(YELLOW);
        characters.add(BLUE);
        characters.add(MAGENTA);
        characters.add(CYAN);
        characters.add(WHITE);
        characters.add(BACKGROUND_BLACK);
        characters.add(BACKGROUND_RED);
        characters.add(BACKGROUND_GREEN);
        characters.add(BACKGROUND_YELLOW);
        characters.add(BACKGROUND_BLUE);
        characters.add(BACKGROUND_MAGENTA);
        characters.add(BACKGROUND_CYAN);
        characters.add(BACKGROUND_WHITE);
        for (final String ansi : characters)
            string = string.replace(ansi, "");
        return string;
    }
}
