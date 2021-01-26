package org.screamingsandals.lib.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.lib.event.EventPriority;

public abstract class AbstractEventListener<E extends Event> implements Listener {

    @EventHandler(priority = org.bukkit.event.EventPriority.LOWEST)
    public void onLowest(E event) {
        onFire(event, EventPriority.LOWEST);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.LOW)
    public void onLow(E event) {
        onFire(event, EventPriority.LOW);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.NORMAL)
    public void onNormal(E event) {
        onFire(event, EventPriority.NORMAL);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH)
    public void onHigh(E event) {
        onFire(event, EventPriority.HIGH);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onHighest(E event) {
        onFire(event, EventPriority.HIGHEST);
    }

    protected abstract void onFire(E event, EventPriority priority);
}
