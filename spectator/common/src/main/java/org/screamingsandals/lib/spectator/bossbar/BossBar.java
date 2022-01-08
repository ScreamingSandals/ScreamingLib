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

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Collection;
import java.util.List;

public interface BossBar extends Wrapper, RawValueHolder {
    Component title();

    BossBar title(Component title);

    float progress();

    BossBar progress(float progress);

    List<BossBarFlag> flags();

    BossBar flags(List<BossBarFlag> flags);

    BossBarColor color();

    BossBar color(BossBarColor color);

    BossBarDivision division();

    BossBar division(BossBarDivision division);

    BossBar addListener(BossBarListener listener);

    BossBar removeListener(BossBarListener listener);

    interface Builder {
        Builder title(Component title);

        Builder progress(float progress);

        Builder color(BossBarColor color);

        Builder division(BossBarDivision division);

        Builder flags(Collection<BossBarFlag> flags);

        Builder flags(BossBarFlag... flags);

        Builder listener(BossBarListener listener);

        BossBar build();
    }
}
