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

package org.screamingsandals.lib.spectator.title;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface TimesProvider {
    @NotNull Duration fadeIn();

    @NotNull Duration stay();

    @NotNull Duration fadeOut();

    @Contract(value = "_,_,_ -> new", pure = true)
    static @NotNull TimesProvider times(@NotNull Duration fadeIn, @NotNull Duration stay, @NotNull Duration fadeOut) {
        return new TimesProvider() {
            @Override
            public @NotNull Duration fadeIn() {
                return fadeIn;
            }

            @Override
            public @NotNull Duration stay() {
                return stay;
            }

            @Override
            public @NotNull Duration fadeOut() {
                return fadeOut;
            }
        };
    }
}
