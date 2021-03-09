package org.screamingsandals.lib.utils.adventure;

import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.utils.reflect.Reflect;

@RequiredArgsConstructor
public final class A2AAudience implements Audience {
    private final Object platformAudience;

    @Override
    public void sendMessage(final @NonNull Identity source, final @NonNull Component message, final @NonNull MessageType type) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).sendMessage(source, message, type);
            return;
        }
        AdventureUtils
                .get(platformAudience, "sendMessage", Identity.class, Component.class, MessageType.class)
                .invoke(source, message, type);
    }

    @Override
    public void sendActionBar(final @NonNull Component message) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).sendActionBar(message);
            return;
        }
        AdventureUtils
                .get(platformAudience, "sendActionBar", Component.class)
                .invoke(message);
    }

    @Override
    public void sendPlayerListHeader(final @NonNull Component header) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).sendPlayerListHeader(header);
            return;
        }
        AdventureUtils
                .get(platformAudience, "sendPlayerListHeader", Component.class)
                .invoke(header);
    }

    @Override
    public void sendPlayerListFooter(final @NonNull Component footer) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).sendPlayerListFooter(footer);
            return;
        }
        AdventureUtils
                .get(platformAudience, "sendPlayerListFooter", Component.class)
                .invoke(footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(final @NonNull Component header, final @NonNull Component footer) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).sendPlayerListHeaderAndFooter(header, footer);
            return;
        }
        AdventureUtils
                .get(platformAudience, "sendPlayerListHeaderAndFooter", Component.class, Component.class)
                .invoke(header, footer);
    }

    @Override
    public void showTitle(final @NonNull Title title) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).showTitle(title);
            return;
        }
        AdventureUtils
                .get(platformAudience, "showTitle", Title.class)
                .invoke(title);
    }

    @Override
    public void clearTitle() {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).clearTitle();
            return;
        }
        Reflect.fastInvoke(platformAudience, "clearTitle");
    }

    @Override
    public void resetTitle() {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).resetTitle();
            return;
        }
        Reflect.fastInvoke(platformAudience, "resetTitle");
    }

    @Override
    public void showBossBar(final @NonNull BossBar bar) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).showBossBar(bar);
            return;
        }
        // it's audience from platform
    }

    @Override
    public void hideBossBar(final @NonNull BossBar bar) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).hideBossBar(bar);
            return;
        }
        // it's audience from platform
    }

    @Override
    public void playSound(final @NonNull Sound sound) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).playSound(sound);
            return;
        }
        AdventureUtils
                .get(platformAudience, "playSound", Sound.class)
                .invoke(sound);
    }

    @Override
    public void playSound(final @NonNull Sound sound, final double x, final double y, final double z) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).playSound(sound, x, y, z);
            return;
        }
        AdventureUtils
                .get(platformAudience, "playSound", Sound.class, float.class, float.class, float.class)
                .invoke(sound, x, y, z);
    }

    @Override
    public void stopSound(final @NonNull SoundStop stop) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).stopSound(stop);
            return;
        }
        AdventureUtils
                .get(platformAudience, "stopSound", SoundStop.class)
                .invoke(stop);
    }

    @Override
    public void openBook(final @NonNull Book book) {
        if (platformAudience instanceof Audience) {
            ((Audience) platformAudience).openBook(book);
            return;
        }
        AdventureUtils
                .get(platformAudience, "openBook", Book.class)
                .invoke(book);
    }
}
