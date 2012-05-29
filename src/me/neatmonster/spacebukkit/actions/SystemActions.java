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

import java.io.File;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import me.neatmonster.spacebukkit.SpaceBukkit;
import me.neatmonster.spacemodule.api.Action;

/**
 * Actions handler for any System-related events
 */
public class SystemActions {

    /**
     * Gets the Operating System of the server machine
     * @return Operation System
     */
    @Action(
            aliases = {"getArch", "arch"})
    public String getArch() {
        return ManagementFactory.getOperatingSystemMXBean().getArch();
    }

    /**
     * Gets the CPU Frequency
     * @return CPU Frequency
     */
    @Action(
            aliases = {"getCpuFrequency", "cpuFrequency"})
    public double getCpuFrequency() {
        return SpaceBukkit.getInstance().performanceMonitor.getCpuFrequency() / 1000000000D;
    }

    /**
     * Gets the current CPU Usage
     * @return CPU Usage
     */
    @Action(
            aliases = {"getCpuUsage", "cpuUsage"})
    public float getCpuUsage() {
        return SpaceBukkit.getInstance().performanceMonitor.getCpuUsage() * 100F;
    }

    /**
     * Gets the amount of Daemon threads currently created
     * @return Dameon threads created
     */
    @Action(
            aliases = {"getDaemonThreads", "daemonThreads"})
    public int getDaemonThreads() {
        return ManagementFactory.getThreadMXBean().getDaemonThreadCount();
    }

    /**
     * Gets how much disk space is free on the server
     * @return Disk space that is used
     */
    @Action(
            aliases = {"getDiskFreeSpace", "spaceFree"})
    public long getDiskFreeSpace() {
        return new File(".").getFreeSpace();
    }

    /**
     * Gets the total disk space on the server
     * @return Disk space
     */
    @Action(
            aliases = {"getDiskSize", "spaceSize"})
    public long getDiskSize() {
        return new File(".").getTotalSpace();
    }

    /**
     * Gets the total disk usage on the server
     * @return Disk space that is free
     */
    @Action(
            aliases = {"getDiskUsage", "spaceUsed"})
    public long getDiskUsage() {
        return new File(".").getTotalSpace() - new File(".").getFreeSpace();
    }

    /**
     * Gets the list of garbage collectors
     * @return Garbage collectors
     */
    @Action(
            aliases = {"getGarbageCollectors", "garbageCollectors"})
    public LinkedList<LinkedHashMap<String, Object>> getGarbageCollectors() {
        final LinkedList<LinkedHashMap<String, Object>> garbageCollectors = new LinkedList<LinkedHashMap<String, Object>>();
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
            final LinkedHashMap<String, Object> gc = new LinkedHashMap<String, Object>();
            gc.put("Name", garbageCollector.getName());
            gc.put("CollectionCount", garbageCollector.getCollectionCount());
            gc.put("CollectionTime", garbageCollector.getCollectionTime());
            garbageCollectors.push(gc);
        }
        return garbageCollectors;
    }

    /**
     * Gets the input arguments used to start the toolkit
     * @return Input arguments
     */
    @Action(
            aliases = {"getInputArguments", "inputArguments"})
    public List<String> getInputArguments() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    /**
     * Gets how much memory is currently free on the server
     * @return Memory free
     */
    @Action(
            aliases = {"getJavaMemoryFree", "memoryFree"})
    public long getJavaMemoryFree() {
        return Math.round((float)Runtime.getRuntime().freeMemory() / 1048576.0f);
    }

    /**
     * Gets how much memory will be used by the Java VM
     * @return Memory used by the Java VM
     */
    @Action(
            aliases = {"getJavaMemoryMax", "memoryMax"})
    public long getJavaMemoryMax() {
        return Math.round((float)Runtime.getRuntime().maxMemory() / 1048576.0f);
    }

    /**
     * Gets how much memory is allocated to the Java process
     * @return Memory allocated
     */
    @Action(
            aliases = {"getJavaMemoryTotal", "memoryTotal"})
    public long getJavaMemoryTotal() {
        return Math.round((float)Runtime.getRuntime().totalMemory() / 1048576.0f);
    }

    /**
     * Gets how much memory is currently being used by the Java VM
     * @return Memory used
     */
    @Action(
            aliases = {"getJavaMemoryUsage", "memoryUsage"})
    public long getJavaMemoryUsage() {
        return getJavaMemoryTotal() - getJavaMemoryFree();
    }

    /**
     * Gets how many threads are currently loaded
     * @return Loaded threads
     */
    @Action(
            aliases = {"getLiveThreads", "liveThreads"})
    public int getLiveThreads() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    /**
     * Gets how many classes are currently loaded
     * @return Loaded classes
     */
    @Action(
            aliases = {"getLoadedClasses", "loadedClasses"})
    public int getLoadedClasses() {
        return ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
    }

    /**
     * Gets how many CPU's are on the server
     * @return CPU's
     */
    @Action(
            aliases = {"getNumCpus", "numCpus"})
    public int getNumCpus() {
        return SpaceBukkit.getInstance().performanceMonitor.getNumCpus();
    }

    /**
     * Gets the name of the OS the server is running on
     * @return OS Name
     */
    @Action(
            aliases = {"getOsName", "osName"})
    public String getOsName() {
        return SpaceBukkit.getInstance().performanceMonitor.getOsName();
    }

    /**
     * Gets how many peak threads are currently active
     * @return Peak threads
     */
    @Action(
            aliases = {"getPeakThreads", "peakThreads"})
    public int getPeakThreads() {
        return ManagementFactory.getThreadMXBean().getPeakThreadCount();
    }

    /**
     * Gets the amount of physical memory on the server that is free
     * @return Physical memory free
     */
    @Action(
            aliases = {"getPhysicalMemoryFree", "physicalMemoryFree"})
    public long getPhysicalMemoryFree() {
        return Math.round((float)SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryFree() / 1048576.0f);
    }

    /**
     * Gets the total amount of physical memory on the server
     * @return Physical total memory
     */
    @Action(
            aliases = {"getPhysicalMemoryTotal", "physicalMemoryTotal"})
    public long getPhysicalMemoryTotal() {
        return Math.round((float)SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryTotal() / 1048576.0f);
    }

    /**
     * Gets the amount of physical memory usage on the server
     * @return Physical memory usage
     */
    @Action(
            aliases = {"getPhysicalMemoryUsage", "physicalMemoryUsage"})
    public long getPhysicalMemoryUsage() {
        return (SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryTotal() - SpaceBukkit
                .getInstance().performanceMonitor.getPhysicalMemoryFree()) / 1048576;
    }

    /**
     * Gets the PID of the process
     * @return PID
     */
    @Action(
            aliases = {"getPid", "pid"})
    public int getPid() {
        return SpaceBukkit.getInstance().performanceMonitor.getPid();
    }

    /**
     * Gets the clock rate of the CPU
     * @return Clock rate
     */
    @Action(
            aliases = {"getTicks", "ticks"})
    public double getTicks() {
        return SpaceBukkit.getInstance().performanceMonitor.getClockRate();
    }

    /**
     * Gets how many total classes there are
     * @return Total classes
     */
    @Action(
            aliases = {"getTotalClasses", "totalClasses"})
    public long getTotalClasses() {
        return ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
    }

    /**
     * Gets how many total threads there are
     * @return Total threads
     */
    @Action(
            aliases = {"getTotalThreads", "totalThreads"})
    public long getTotalThreads() {
        return ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
    }

    /**
     * Gets how many unloaded classes there are
     * @return Unloaded classes
     */
    @Action(
            aliases = {"getUnloadedClasses", "unloadedClasses"})
    public long getUnloadedClasses() {
        return ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount();
    }

    /**
     * Gets how long the server has been running
     * @return Uptime
     */
    @Action(
            aliases = {"getUpTime", "upTime"})
    public long getUpTime() {
        return SpaceBukkit.getInstance().performanceMonitor.getUptime();
    }

    /**
     * Runs the garbage collector
     * @return If successful
     */
    @Action(
            aliases = {"runGarbageCollector", "garbageCollector"})
    public boolean runGarbageCollector() {
        System.gc();
        return true;
    }
}
