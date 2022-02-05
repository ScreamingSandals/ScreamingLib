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

package org.screamingsandals.lib.spectator;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

@ApiStatus.Internal
public interface SpectatorBackend {
    Component empty();

    Component newLine();

    Component space();

    Component fromLegacy(String legacy);

    Component fromLegacy(String legacy, char colorChar);

    Component fromJson(String json);

    BlockNBTComponent.Builder blockNBT();

    EntityNBTComponent.Builder entityNBT();

    KeybindComponent.Builder keybind();

    ScoreComponent.Builder score();

    SelectorComponent.Builder selector();

    StorageNBTComponent.Builder storageNBT();

    TextComponent.Builder text();

    TranslatableComponent.Builder translatable();

    Color rgb(int red, int green, int blue);

    Color named(String name);

    Color hexOrName(String hex);

    BossBar.Builder bossBar();

    SoundStart.Builder soundStart();

    SoundStop.Builder soundStop();

    SoundSource soundSource(String source);

    Title.Builder title();

    Book.Builder book();

    ClickEvent.Builder clickEvent();

    HoverEvent.Builder hoverEvent();

    EntityContent.Builder entityContent();

    ItemContent.Builder itemContent();
}
