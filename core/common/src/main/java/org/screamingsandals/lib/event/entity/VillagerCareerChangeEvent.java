/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.villager.Profession;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.CancellableEvent;

public interface VillagerCareerChangeEvent extends CancellableEvent, PlatformEvent {

    @NotNull Entity entity();

    @NotNull Profession profession();

    void profession(@NotNull Profession profession);

    @NotNull ChangeReason changeReason();

    /**
     * Reasons for the villager's profession changing.
     */
    // TODO: holder?
    enum ChangeReason {

        /**
         * Villager lost their job due to too little experience.
         */
        LOSING_JOB,
        /**
         * Villager gained employment.
         */
        EMPLOYED
    }
}
