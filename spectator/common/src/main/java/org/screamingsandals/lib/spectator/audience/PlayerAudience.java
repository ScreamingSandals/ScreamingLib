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

package org.screamingsandals.lib.spectator.audience;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

public interface PlayerAudience extends Audience {
    void sendActionBar(@NotNull ComponentLike message);

    default void sendRichActionBar(@NotNull String message) {
        sendActionBar(Component.fromMiniMessage(message));
    }

    default void sendPlainActionBar(@NotNull String message) {
        sendActionBar(Component.text(message));
    }

    default void sendPlainActionBar(@NotNull String message, @NotNull Color color) {
        sendActionBar(Component.text(message, color));
    }

    default void sendPlayerListHeader(@NotNull ComponentLike header) {
        sendPlayerListHeaderFooter(header, Component.empty());
    }

    default void sendRichPlayerListHeader(@NotNull String header) {
        sendPlayerListHeaderFooter(Component.fromMiniMessage(header), Component.empty());
    }

    default void sendPlainPlayerListHeader(@NotNull String header) {
        sendPlayerListHeaderFooter(Component.text(header), Component.empty());
    }

    default void sendPlainPlayerListHeader(@NotNull String header, @NotNull Color color) {
        sendPlayerListHeaderFooter(Component.text(header, color), Component.empty());
    }

    default void sendPlayerListFooter(@NotNull ComponentLike footer) {
        sendPlayerListHeaderFooter(Component.empty(), footer);
    }

    default void sendRichPlayerListFooter(@NotNull String footer) {
        sendPlayerListHeaderFooter(Component.empty(), Component.fromMiniMessage(footer));
    }

    default void sendPlainPlayerListFooter(@NotNull String footer) {
        sendPlayerListHeaderFooter(Component.empty(), Component.text(footer));
    }

    default void sendPlainPlayerListFooter(@NotNull String footer, @NotNull Color color) {
        sendPlayerListHeaderFooter(Component.empty(), Component.text(footer, color));
    }

    void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer);

    default void sendRichPlayerListHeaderFooter(@NotNull String header, @NotNull String footer) {
        sendPlayerListHeaderFooter(Component.fromMiniMessage(header), Component.fromMiniMessage(footer));
    }

    default void sendPlainPlayerListHeaderFooter(@NotNull String header, @NotNull String footer) {
        sendPlayerListHeaderFooter(Component.text(header), Component.text(footer));
    }

    default void sendPlainPlayerListHeaderFooter(@NotNull String header, @NotNull String footer, @NotNull Color color) {
        sendPlayerListHeaderFooter(Component.text(header, color), Component.text(footer, color));
    }

    default void sendPlainPlayerListHeaderFooter(@NotNull String header, @NotNull Color headerColor, @NotNull String footer, @NotNull Color footerColor) {
        sendPlayerListHeaderFooter(Component.text(header, headerColor), Component.text(footer, footerColor));
    }

    default void showTitle(@NotNull Title.Builder title) {
        showTitle(title.build());
    }

    void showTitle(@NotNull Title title);

    void clearTitle();

    void showBossBar(@NotNull BossBar bossBar);

    void hideBossBar(@NotNull BossBar bossBar);

    default void playSound(@NotNull SoundStart.Builder sound) {
        playSound(sound.build());
    }

    void playSound(@NotNull SoundStart sound);

    default void playSound(@NotNull SoundStart.Builder sound, double x, double y, double z) {
        playSound(sound.build(), x, y, z);
    }

    void playSound(@NotNull SoundStart sound, double x, double y, double z);

    // TODO: Emitter??? it's relatively new in Adventure

    default void stopSound(@NotNull SoundStart.Builder sound) {
        stopSound(sound.build());
    }

    default void stopSound(@NotNull SoundStart sound) {
        stopSound(SoundStop.namedSourced(sound.soundKey(), sound.source()));
    }

    default void stopSound(@NotNull SoundStop.Builder sound) {
        stopSound(sound.build());
    }

    void stopSound(@NotNull SoundStop sound);

    default void openBook(@NotNull Book.Builder book) {
        openBook(book.build());
    }

    void openBook(@NotNull Book book);

    interface ForwardingToMulti extends PlayerAudience, Audience.ForwardingToMulti {
        @NotNull
        @ApiStatus.OverrideOnly
        Iterable<? extends PlayerAudience> audiences();

        @Override
        default void sendActionBar(@NotNull ComponentLike message) {
            audiences().forEach(audience -> audience.sendActionBar(message));
        }

        @Override
        default void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
            audiences().forEach(audience -> audience.sendPlayerListHeaderFooter(header, footer));
        }

        @Override
        default void showTitle(@NotNull Title title) {
            audiences().forEach(audience -> audience.showTitle(title));
        }

        @Override
        default void clearTitle() {
            audiences().forEach(PlayerAudience::clearTitle);
        }

        @Override
        default void showBossBar(@NotNull BossBar bossBar) {
            audiences().forEach(audience -> audience.showBossBar(bossBar));
        }

        @Override
        default void hideBossBar(@NotNull BossBar bossBar) {
            audiences().forEach(audience -> audience.hideBossBar(bossBar));
        }

        @Override
        default void playSound(@NotNull SoundStart sound) {
            audiences().forEach(audience -> audience.playSound(sound));
        }

        @Override
        default void playSound(@NotNull SoundStart sound, double x, double y, double z) {
            audiences().forEach(audience -> audience.playSound(sound, x, y, z));
        }

        @Override
        default void stopSound(@NotNull SoundStop sound) {
            audiences().forEach(audience -> audience.stopSound(sound));
        }

        @Override
        default void openBook(@NotNull Book book) {
            audiences().forEach(audience -> audience.openBook(book));
        }
    }

     interface ForwardingToSingle extends PlayerAudience, Audience.ForwardingToSingle {
        @NotNull
        @ApiStatus.OverrideOnly
        PlayerAudience audience();

        @Override
        default void sendActionBar(@NotNull ComponentLike message) {
            audience().sendActionBar(message);
        }

        @Override
        default void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
            audience().sendPlayerListHeaderFooter(header, footer);
        }

        @Override
        default void showTitle(@NotNull Title title) {
            audience().showTitle(title);
        }

        @Override
        default void clearTitle() {
            audience().clearTitle();
        }

        @Override
        default void showBossBar(@NotNull BossBar bossBar) {
            audience().showBossBar(bossBar);
        }

        @Override
        default void hideBossBar(@NotNull BossBar bossBar) {
            audience().hideBossBar(bossBar);
        }

        @Override
        default void playSound(@NotNull SoundStart sound) {
            audience().playSound(sound);
        }

        @Override
        default void playSound(@NotNull SoundStart sound, double x, double y, double z) {
            audience().playSound(sound, x, y, z);
        }

        @Override
        default void stopSound(@NotNull SoundStop sound) {
            audience().stopSound(sound);
        }

        @Override
        default void openBook(@NotNull Book book) {
            audience().openBook(book);
        }
    }

    @ApiStatus.NonExtendable // don't extend it guys
    interface ForwardingToAdapter extends PlayerAudience, Audience.ForwardingToAdapter {
        @Override
        @NotNull
        @ApiStatus.OverrideOnly
        PlayerAdapter adapter();

        @Override
        default void sendActionBar(@NotNull ComponentLike message) {
            adapter().sendActionBar(message);
        }

        @Override
        default void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
            adapter().sendPlayerListHeaderFooter(header, footer);
        }

        @Override
        default void showTitle(@NotNull Title title) {
            adapter().showTitle(title);
        }

        @Override
        default void clearTitle() {
            adapter().clearTitle();
        }

        @Override
        default void showBossBar(@NotNull BossBar bossBar) {
            adapter().showBossBar(bossBar);
        }

        @Override
        default void hideBossBar(@NotNull BossBar bossBar) {
            adapter().hideBossBar(bossBar);
        }

        @Override
        default void playSound(@NotNull SoundStart sound) {
            adapter().playSound(sound);
        }

        @Override
        default void playSound(@NotNull SoundStart sound, double x, double y, double z) {
            adapter().playSound(sound, x, y, z);
        }

        @Override
        default void stopSound(@NotNull SoundStop sound) {
            adapter().stopSound(sound);
        }

        @Override
        default void openBook(@NotNull Book book) {
            adapter().openBook(book);
        }
    }
}
