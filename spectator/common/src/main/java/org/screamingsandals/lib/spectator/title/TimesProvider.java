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

package org.screamingsandals.lib.spectator.title;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public interface TimesProvider {
    @NotNull
    Duration fadeIn();

    @NotNull
    Duration stay();

    @NotNull
    Duration fadeOut();

    @NotNull
    static TimesProvider times(Duration fadeIn, Duration stay, Duration fadeOut) {
        return new TimesProvider() {
            @Override
            @NotNull
            public Duration fadeIn() {
                return fadeIn;
            }

            @Override
            @NotNull
            public Duration stay() {
                return stay;
            }

            @Override
            @NotNull
            public Duration fadeOut() {
                return fadeOut;
            }
        };
    }
}
