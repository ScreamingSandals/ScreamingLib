/*
 * Copyright 2023 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.bungee.event;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEventListener<E extends Event> implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowest(@NotNull E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.LOWEST);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLow(@NotNull E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.LOW);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNormal(@NotNull E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.NORMAL);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHigh(@NotNull E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.HIGH);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighest(@NotNull E event) {
        onFire(event, org.screamingsandals.lib.event.EventPriority.HIGHEST);
    }

    protected abstract void onFire(@NotNull E chatEvent, org.screamingsandals.lib.event.@NotNull EventPriority eventPriority);
}
