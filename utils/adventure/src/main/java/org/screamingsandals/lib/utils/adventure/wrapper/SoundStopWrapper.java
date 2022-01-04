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

package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.sound.SoundStop;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.SoundUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class SoundStopWrapper implements Wrapper {
    private final SoundStop soundStop;

    @NotNull
    public SoundStop asSoundStop() {
        return soundStop;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(soundStop)) {
            return (T) soundStop;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var keyClass = Reflect.getClassSafe(generalPackage + ".key.Key");
        var sourceClass = Reflect.getClassSafe(generalPackage + ".sound.Sound$Source");
        if (keyClass == null || sourceClass == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure is badly relocated!");
        }

        return (T) SoundUtils.stopSoundToPlatform(soundStop, type, keyClass, sourceClass);
    }
}
