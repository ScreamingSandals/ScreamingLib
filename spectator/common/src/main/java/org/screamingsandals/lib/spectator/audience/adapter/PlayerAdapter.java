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

package org.screamingsandals.lib.spectator.audience.adapter;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;

@ApiStatus.Internal
public interface PlayerAdapter extends Adapter {
    @Override
    PlayerAudience owner();

    void sendActionBar(@NotNull ComponentLike message);

    void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer);

    void showTitle(@NotNull Title title);

    void showTitle(@NotNull TitleableAudienceComponentLike title, @Nullable TimesProvider times);

    void clearTitle();

    void showBossBar(@NotNull BossBar bossBar);

    void hideBossBar(@NotNull BossBar bossBar);

    void playSound(@NotNull SoundStart sound);

    void playSound(@NotNull SoundStart sound, double x, double y, double z);

    void stopSound(@NotNull SoundStop sound);

    void openBook(@NotNull Book book);
}
