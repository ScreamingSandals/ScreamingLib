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

package org.screamingsandals.lib.adventure.spectator.bossbar;

import lombok.Data;
import lombok.experimental.Accessors;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.bossbar.BossBarListener;
import org.screamingsandals.lib.spectator.bossbar.RegisteredListener;

import java.util.Set;

@Data
@Accessors(fluent = true)
public class AdventureBossBarListener implements BossBar.Listener, RegisteredListener {
    private final BossBarListener listener;

    public void bossBarNameChanged(final @NotNull BossBar bar, final @NotNull Component oldName, final @NotNull Component newName) {
        listener.onTitleChanged(new AdventureBossBar(bar), AdventureBackend.wrapComponent(oldName), AdventureBackend.wrapComponent(newName));
    }

    public void bossBarProgressChanged(final @NotNull BossBar bar, final float oldProgress, final float newProgress) {
        listener.onProgressChanged(new AdventureBossBar(bar), oldProgress, newProgress);
    }


    public void bossBarColorChanged(final @NotNull BossBar bar, final @NotNull BossBar.Color oldColor, final @NotNull BossBar.Color newColor) {
        listener.onColorChanged(new AdventureBossBar(bar), BossBarUtils.convertColor(oldColor), BossBarUtils.convertColor(newColor));
    }

    public void bossBarOverlayChanged(final @NotNull BossBar bar, final @NotNull BossBar.Overlay oldOverlay, final @NotNull BossBar.Overlay newOverlay) {
        listener.onDivisionChanged(new AdventureBossBar(bar), BossBarUtils.convertDivision(oldOverlay), BossBarUtils.convertDivision(newOverlay));
    }

    public void bossBarFlagsChanged(final @NotNull BossBar bar, final @NotNull Set<BossBar.Flag> flagsAdded, final @NotNull Set<BossBar.Flag> flagsRemoved) {
        listener.onFlagsChanged(new AdventureBossBar(bar), BossBarUtils.convertFlags(flagsAdded), BossBarUtils.convertFlags(flagsRemoved));
    }
}
