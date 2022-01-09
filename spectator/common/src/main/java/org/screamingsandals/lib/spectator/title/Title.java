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

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.Spectator;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.time.Duration;

public interface Title extends Wrapper, RawValueHolder, TimesProvider {
    static Title.Builder builder() {
        return Spectator.getBackend().title();
    }

    Component title();

    Component subtitle();

    interface Builder {
        Builder title(Component title);

        Builder subtitle(Component subtitle);

        Builder fadeIn(Duration fadeIn);

        Builder stay(Duration stay);

        Builder fadeOut(Duration fadeOut);

        default Builder times(TimesProvider times) {
            fadeIn(times.fadeIn());
            stay(times.stay());
            fadeOut(times.fadeOut());
            return this;
        }

        Title build();
    }
}
