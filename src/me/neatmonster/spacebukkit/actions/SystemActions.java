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

public class SystemActions {

    @Action(
            aliases = {"getArch", "arch"})
    public String getArch() {
        return ManagementFactory.getOperatingSystemMXBean().getArch();
    }

    @Action(
            aliases = {"getCpuFrequency", "cpuFrequency"})
    public double getCpuFrequency() {
        return SpaceBukkit.getInstance().performanceMonitor.getCpuFrequency() / 1000000000D;
    }

    @Action(
            aliases = {"getCpuUsage", "cpuUsage"})
    public float getCpuUsage() {
        return SpaceBukkit.getInstance().performanceMonitor.getCpuUsage() * 100F;
    }

    @Action(
            aliases = {"getDaemonThreads", "daemonThreads"})
    public int getDaemonThreads() {
        return ManagementFactory.getThreadMXBean().getDaemonThreadCount();
    }

    @Action(
            aliases = {"getDiskFreeSpace", "spaceFree"})
    public long getDiskFreeSpace() {
        return new File(".").getFreeSpace();
    }

    @Action(
            aliases = {"getDiskSize", "spaceSize"})
    public long getDiskSize() {
        return new File(".").getTotalSpace();
    }

    @Action(
            aliases = {"getDiskUsage", "spaceUsed"})
    public long getDiskUsage() {
        return new File(".").getTotalSpace() - new File(".").getFreeSpace();
    }

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

    @Action(
            aliases = {"getInputArguments", "inputArguments"})
    public List<String> getInputArguments() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    @Action(
            aliases = {"getJavaMemoryFree", "memoryFree"})
    public long getJavaMemoryFree() {
        return Math.round(Runtime.getRuntime().freeMemory() / 1048576);
    }

    @Action(
            aliases = {"getJavaMemoryMax", "memoryMax"})
    public long getJavaMemoryMax() {
        return Math.round(Runtime.getRuntime().maxMemory() / 1048576);
    }

    @Action(
            aliases = {"getJavaMemoryTotal", "memoryTotal"})
    public long getJavaMemoryTotal() {
        return Math.round(Runtime.getRuntime().totalMemory() / 1048576);
    }

    @Action(
            aliases = {"getJavaMemoryUsage", "memoryUsage"})
    public long getJavaMemoryUsage() {
        return getJavaMemoryTotal() - getJavaMemoryFree();
    }

    @Action(
            aliases = {"getLiveThreads", "liveThreads"})
    public int getLiveThreads() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    @Action(
            aliases = {"getLoadedClasses", "loadedClasses"})
    public int getLoadedClasses() {
        return ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
    }

    @Action(
            aliases = {"getNumCpus", "numCpus"})
    public int getNumCpus() {
        return SpaceBukkit.getInstance().performanceMonitor.getNumCpus();
    }

    @Action(
            aliases = {"getOsName", "osName"})
    public String getOsName() {
        return SpaceBukkit.getInstance().performanceMonitor.getOsName();
    }

    @Action(
            aliases = {"getPeakThreads", "peakThreads"})
    public int getPeakThreads() {
        return ManagementFactory.getThreadMXBean().getPeakThreadCount();
    }

    @Action(
            aliases = {"getPhysicalMemoryFree", "physicalMemoryFree"})
    public long getPhysicalMemoryFree() {
        return Math.round(SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryFree() / 1048576);
    }

    @Action(
            aliases = {"getPhysicalMemoryTotal", "physicalMemoryTotal"})
    public long getPhysicalMemoryTotal() {
        return Math.round(SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryTotal() / 1048576);
    }

    @Action(
            aliases = {"getPhysicalMemoryUsage", "physicalMemoryUsage"})
    public long getPhysicalMemoryUsage() {
        return Math.round((SpaceBukkit.getInstance().performanceMonitor.getPhysicalMemoryTotal() - SpaceBukkit
                .getInstance().performanceMonitor.getPhysicalMemoryFree()) / 1048576);
    }

    @Action(
            aliases = {"getPid", "pid"})
    public int getPid() {
        return SpaceBukkit.getInstance().performanceMonitor.getPid();
    }

    @Action(
            aliases = {"getTicks", "ticks"})
    public double getTicks() {
        return SpaceBukkit.getInstance().performanceMonitor.getClockRate();
    }

    @Action(
            aliases = {"getTotalClasses", "totalClasses"})
    public long getTotalClasses() {
        return ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
    }

    @Action(
            aliases = {"getTotalThreads", "totalThreads"})
    public long getTotalThreads() {
        return ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
    }

    @Action(
            aliases = {"getUnloadedClasses", "unloadedClasses"})
    public long getUnloadedClasses() {
        return ManagementFactory.getClassLoadingMXBean().getUnloadedClassCount();
    }

    @Action(
            aliases = {"getUpTime", "upTime"})
    public long getUpTime() {
        return SpaceBukkit.getInstance().performanceMonitor.getUptime();
    }

    @Action(
            aliases = {"runGarbageCollector", "garbageCollector"})
    public boolean runGarbageCollector() {
        System.gc();
        return true;
    }
}
