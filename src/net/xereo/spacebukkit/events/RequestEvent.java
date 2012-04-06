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
package net.xereo.spacebukkit.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RequestEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Object[] arguments;
    private String   method;

    private Object   result = null;

    public RequestEvent(final String method, final Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public String getMethod() {
        return method;
    }

    public Object getResult() {
        return result;
    }

    public void setArguments(final Object[] arguments) {
        this.arguments = arguments;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public void setResult(final Object result) {
        this.result = result;
    }
}
