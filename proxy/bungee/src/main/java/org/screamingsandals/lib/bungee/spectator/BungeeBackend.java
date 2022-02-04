package org.screamingsandals.lib.bungee.spectator;

import org.screamingsandals.lib.bungee.spectator.title.BungeeTitle;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

public class BungeeBackend extends AbstractBungeeBackend {
    @Override
    public BossBar.Builder bossBar() {
        return null; // TODO
    }

    @Override
    public SoundStart.Builder soundStart() {
        return null; // Does not exist on proxy
    }

    @Override
    public SoundStop.Builder soundStop() {
        return null; // Does not exist on proxy
    }

    @Override
    public SoundSource soundSource(String source) {
        return null; // Does not exist on proxy
    }

    @Override
    public Title.Builder title() {
        return new BungeeTitle.BungeeTitleBuilder();
    }

    @Override
    public Book.Builder book() {
        return null; // Does not exist on proxy
    }
}
