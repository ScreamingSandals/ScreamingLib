/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.event.block;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.block.BlockHolder;

public interface SBlockDispenseEvent extends SCancellableEvent, PlatformEventWrapper {

    BlockHolder block();

    Item item();

    void item(Item item);

    Vector3D velocity();

    void velocity(Vector3D velocity);

    /*
     * Only in case when dispenser is used to attach armor to an entity
     */
    @Nullable
    EntityLiving receiver();
}
