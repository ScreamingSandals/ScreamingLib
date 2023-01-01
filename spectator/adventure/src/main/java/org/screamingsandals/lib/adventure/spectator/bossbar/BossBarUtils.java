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

package org.screamingsandals.lib.adventure.spectator.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.bossbar.BossBarColor;
import org.screamingsandals.lib.spectator.bossbar.BossBarDivision;
import org.screamingsandals.lib.spectator.bossbar.BossBarFlag;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BossBarUtils {
    public static BossBarColor convertColor(BossBar.Color color) {
        try {
            return BossBarColor.valueOf(color.name());
        } catch (Throwable t) {
            return BossBarColor.PURPLE;
        }
    }

    public static BossBar.Color convertColor(BossBarColor color) {
        return net.kyori.adventure.bossbar.BossBar.Color.valueOf(color.name());
    }

    public static BossBarDivision convertDivision(BossBar.Overlay overlay) {
        try {
            return BossBarDivision.valueOf(overlay.name());
        } catch (Throwable t) {
            return BossBarDivision.NO_DIVISION;
        }
    }

    public static BossBar.Overlay convertDivision(BossBarDivision division) {
        if (division == BossBarDivision.NO_DIVISION) {
            return net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS;
        } else {
            return net.kyori.adventure.bossbar.BossBar.Overlay.valueOf(division.name());
        }
    }

    public static List<BossBarFlag> convertFlags(@Nullable Set<BossBar.Flag> flags) {
        return flags == null ? List.of() : flags.stream()
                .map(flag -> BossBarFlag.valueOf(flag.name()))
                .collect(Collectors.toUnmodifiableList());
    }

    public static Set<BossBar.Flag> convertFlags(@Nullable Collection<BossBarFlag> flags) {
        return flags == null ? Set.of() : flags.stream()
                .map(bossBarFlag -> net.kyori.adventure.bossbar.BossBar.Flag.valueOf(bossBarFlag.name()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
