package org.screamingsandals.lib.adventure.spectator.audience;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

public class AdventurePlayerAudience extends AdventureAudience implements PlayerAudience {
    protected AdventurePlayerAudience(Audience wrappedObject, org.screamingsandals.lib.spectator.audience.Audience.ForwardingSingle screamingLibAudience) {
        super(wrappedObject, screamingLibAudience);
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        wrappedObject.sendActionBar(resolveComponent(message));
    }

    @Override
    public void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        try {
            wrappedObject.sendPlayerListHeaderAndFooter(resolveComponent(header), resolveComponent(footer));
        } catch (Throwable ignored) {
            // Doesn't work in Adventure older than 4.3.0
        }
    }

    @Override
    public void showTitle(@NotNull Title title) {
        wrappedObject.showTitle(title.as(net.kyori.adventure.title.Title.class));
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
