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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.event.PlatformEvent;

public interface PlayerEggThrowEvent extends PlayerEvent, PlatformEvent {

    @NotNull BasicEntity eggEntity();

    boolean hatching();

    void hatching(boolean hatching);

    @NotNull EntityTypeHolder hatchType();

    void hatchType(@NotNull EntityTypeHolder hatchType);

    /**
     * //from bukkit
     * Get the number of mob hatches from the egg. By default, the number will
     * be the number the server would've done
     * <ul>
     * <li>7/8 chance of being 0
     * <li>31/256 ~= 1/8 chance to be 1
     * <li>1/256 chance to be 4
     * </ul>
     *
     * @return The number of mobs going to be hatched by the egg
     */
    byte hatchesNumber();

    void hatchesNumber(byte numHatches);
}
