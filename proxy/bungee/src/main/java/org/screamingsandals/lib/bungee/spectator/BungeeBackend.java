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
