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

package org.screamingsandals.lib.spectator.bossbar;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;

public enum BossBarFlag implements Wrapper {
    DARKEN_SCREEN,
    PLAY_BOSS_MUSIC,
    CREATE_WORLD_FOG;

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == String.class) {
            return (T) name();
        }
        throw new UnsupportedOperationException("Can't unwrap to anything else than String!");
    }
}
