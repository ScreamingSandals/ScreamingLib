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

package org.screamingsandals.lib.impl.bukkit.event.entity;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.entity.EntityPortalEvent;

public class BukkitEntityPortalEvent extends BukkitEntityTeleportEvent implements EntityPortalEvent {
    public BukkitEntityPortalEvent(@NotNull org.bukkit.event.entity.EntityPortalEvent event) {
        super(event);
    }

    @Override
    public @NotNull org.bukkit.event.entity.EntityPortalEvent event() {
        return (org.bukkit.event.entity.EntityPortalEvent) super.event();
    }

    @Override
    public int searchRadius() {
        return event().getSearchRadius();
    }

    @Override
    public void searchRadius(int searchRadius) {
        event().setSearchRadius(searchRadius);
    }
}
