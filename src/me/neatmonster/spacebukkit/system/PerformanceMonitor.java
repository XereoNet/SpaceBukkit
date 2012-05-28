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
package me.neatmonster.spacebukkit.system;

import java.util.TimerTask;

import me.neatmonster.spacebukkit.SpaceBukkit;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;

/**
 * Performance Monitor
 */
public class PerformanceMonitor extends TimerTask {
    private double      clockRate = 0D;
    private JavaSysMon  monitor   = null;

    private CpuTimes    now       = null;
    private CpuTimes    previous  = null;
    private final World world     = Bukkit.getWorlds().get(0);

    /**
     * Creates a new Performance Monitor
     */
    public PerformanceMonitor() {
        monitor = new JavaSysMon();
        if (!monitor.supportedPlatform()) {
            final SpaceBukkit spaceBukkit = SpaceBukkit.getInstance();
            spaceBukkit.logger.severe(spaceBukkit.logTag + "Performance monitoring unsupported!");
            monitor = null;
        } else
            now = monitor.cpuTimes();
    }

    /**
     * Gets the clock rate of the CPU
     * @return Clock rate
     */
    public double getClockRate() {
        return clockRate;
    }

    /**
     * Gets the CPU Frequency
     * @return CPU Frequency
     */
    public long getCpuFrequency() {
        if (monitor != null)
            return monitor.cpuFrequencyInHz();
        return 0L;
    }

    /**
     * Gets the current CPU usage
     * @return CPU Usage
     */
    public float getCpuUsage() {
        if (monitor != null && previous != null && now != null)
            return now.getCpuUsage(previous);
        return 0F;
    }

    /**
     * Gets the number of CPU's
     * @return Number of CPU's
     */
    public int getNumCpus() {
        if (monitor != null)
            return monitor.numCpus();
        return 0;
    }

    /**
     * Gets the name of the OS
     * @return OS Name
     */
    public String getOsName() {
        if (monitor != null)
            return monitor.osName();
        return "";
    }

    /**
     * Gets how much Physical Memory is Free
     * @return Physical Memory free
     */
    public long getPhysicalMemoryFree() {
        if (monitor != null)
            return monitor.physical().getFreeBytes();
        return 0L;
    }

    /**
     * Gets how much Physical Memory there is total
     * @return Physical Memory total
     */
    public long getPhysicalMemoryTotal() {
        if (monitor != null)
            return monitor.physical().getTotalBytes();
        return 0L;
    }

    /**
     * Gets the PID of the server
     * @return PID
     */
    public int getPid() {
        if (monitor != null)
            return monitor.currentPid();
        return 0;
    }

    /**
     * Gets the Uptime of the server, in seconds
     * @return Uptime of the server
     */
    public long getUptime() {
        if (monitor != null)
            return monitor.uptimeInSeconds();
        return 0L;
    }

    /**
     * Stops the monitor
     */
    public void infanticide() {
        if (monitor != null)
            monitor.infanticide();
    }
    
    @Override
    public void run() {
        final long startMillis = System.currentTimeMillis();
        final long startTicks = world.getFullTime();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                final long endMillis = System.currentTimeMillis();
                final long endTicks = world.getFullTime();
                final long elapsedMillis = endMillis - startMillis;
                final long elapsedTicks = endTicks - startTicks;
                clockRate = elapsedTicks / (elapsedMillis / 1000D);
            }
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(SpaceBukkit.getInstance(), task, 100);
        if (monitor != null) {
            previous = now;
            now = monitor.cpuTimes();
        }
    }
}
