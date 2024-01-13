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

package org.screamingsandals.lib.impl.adventure.spectator.audience.adapter;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;

public class AdventurePlayerAdapter extends AdventureAdapter implements PlayerAdapter {
    public AdventurePlayerAdapter(@NotNull Audience wrappedObject, @NotNull PlayerAudience owner) {
        super(wrappedObject, owner);
    }

    @Override
    public @NotNull PlayerAudience owner() {
        return (PlayerAudience) super.owner();
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        wrappedObject.sendActionBar(resolveComponent(message));
    }

    @Override
    public void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        if (AdventureFeature.TAB_HEADER_FOOTER_SENDING.isSupported()) {
            wrappedObject.sendPlayerListHeaderAndFooter(resolveComponent(header), resolveComponent(footer));
        } // Doesn't work in Adventure older than 4.3.0
    }

    @Override
    public void showTitle(@NotNull Title title) {
        wrappedObject.showTitle(title.as(net.kyori.adventure.title.Title.class));
    }

    @Override
    public void showTitle(@NotNull TitleableAudienceComponentLike title, @Nullable TimesProvider times) {
        showTitle(title.asTitle(owner(), times));
    }

    @Override
    public void clearTitle() {
        wrappedObject.clearTitle();
    }

    @Override
    public void showBossBar(@NotNull BossBar bossBar) {
        wrappedObject.showBossBar(bossBar.as(net.kyori.adventure.bossbar.BossBar.class));
    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {
        wrappedObject.hideBossBar(bossBar.as(net.kyori.adventure.bossbar.BossBar.class));
    }

    @Override
    public void playSound(@NotNull SoundStart sound) {
        wrappedObject.playSound(sound.as(Sound.class));
    }

    @Override
    public void playSound(@NotNull SoundStart sound, double x, double y, double z) {
        wrappedObject.playSound(sound.as(Sound.class), x, y, z);
    }

    @Override
    public void stopSound(@NotNull SoundStop sound) {
        wrappedObject.stopSound(sound.as(net.kyori.adventure.sound.SoundStop.class));
    }

    @Override
    public void openBook(@NotNull Book book) {
        wrappedObject.openBook(book.as(net.kyori.adventure.inventory.Book.class));
    }
}
