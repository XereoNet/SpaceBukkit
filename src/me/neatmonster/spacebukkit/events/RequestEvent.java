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
package me.neatmonster.spacebukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a object is interpreted
 */
public class RequestEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    /**
     * Gets a list of Event Handlers
     * @return Event Handlers
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Object[] arguments;
    private String   method;

    private Object   result = null;

    /**
     * Creates a new RequestEvent
     * @param method Method that is being called
     * @param arguments Arguments for that method
     */
    public RequestEvent(final String method, final Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Gets the arguments of the method
     * @return Arguments
     */
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the method being called
     * @return Method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Gets the result of the method with the specified arguments.  May be null
     * @return Result
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets the arguments used
     * @param arguments New arguments
     */
    public void setArguments(final Object[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Sets the method used
     * @param method New method
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * Sets the result
     * @param result New result
     */
    public void setResult(final Object result) {
        this.result = result;
    }
}
