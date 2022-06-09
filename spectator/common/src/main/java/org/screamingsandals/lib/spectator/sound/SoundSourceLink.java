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

package org.screamingsandals.lib.spectator.sound;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@ApiStatus.Internal
class SoundSourceLink implements SoundSource {
    private final String name;
    private SoundSource cache;

    private SoundSource obtainSource() {
        if (cache == null) {
            cache = SoundSource.soundSource(name);
        }
        return cache;
    }

    @Override
    @NotNull
    public String name() {
        return obtainSource().name();
    }

    @Override
    public Object raw() {
        return obtainSource().raw();
    }

    @Override
    public <T> T as(Class<T> type) {
        return obtainSource().as(type);
    }

    @Override
    public boolean equals(Object obj) {
        return obtainSource().equals(obj);
    }

    @Override
    public int hashCode() {
        return obtainSource().hashCode();
    }
}
