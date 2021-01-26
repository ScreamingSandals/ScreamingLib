package org.screamingsandals.lib.bungee.event;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public abstract class AbstractEventListener<E extends Event> implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.HIGHEST);
    }

    protected abstract void onFire(E chatEvent, org.screamingsandals.lib.event.EventPriority eventPriority);
}
