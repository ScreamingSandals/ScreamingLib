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

package org.screamingsandals.lib.spectator.bossbar;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Component;

import java.util.List;

public interface BossBarListener {
    void onTitleChanged(@NotNull BossBar bar, @NotNull Component oldTitle, @NotNull Component newTItle);

    void onProgressChanged(@NotNull BossBar bar, float oldProgress, float newProgress);

    void onColorChanged(@NotNull BossBar bar, @NotNull BossBarColor oldColor, @NotNull BossBarColor newColor);

    void onDivisionChanged(@NotNull BossBar bar, @NotNull BossBarDivision oldDivision, @NotNull BossBarDivision newDivision);

    void onFlagsChanged(@NotNull BossBar bar, @NotNull List<BossBarFlag> addedFlags, @NotNull List<BossBarFlag> removedFlags);
}
