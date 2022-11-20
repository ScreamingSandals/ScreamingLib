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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

import java.util.Locale;

public interface SCauldronLevelChangeEvent extends SCancellableEvent, PlatformEventWrapper {

    @NotNull BlockHolder block();

    @Nullable EntityBasic entity();

    int oldLevel();

    @NotNull Reason reason();

    int newLevel();

    void newLevel(int newLevel);

    // TODO: holder?
    enum Reason {
        ARMOR_WASH,
        BANNER_WASH,
        BOTTLE_EMPTY,
        BOTTLE_FILL,
        BUCKET_EMPTY,
        BUCKET_FILL,
        EVAPORATE,
        EXTINGUISH,
        NATURAL_FILL,
        SHULKER_WASH,
        UNKNOWN;

        public static @NotNull Reason get(@NotNull String name) {
            try {
                return valueOf(name.toUpperCase(Locale.ROOT));
            } catch (Throwable ignored) {
                return UNKNOWN;
            }
        }
    }
}
