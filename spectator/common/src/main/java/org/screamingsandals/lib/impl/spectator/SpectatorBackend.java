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

package org.screamingsandals.lib.impl.spectator;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.BlockNBTComponent;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.EntityNBTComponent;
import org.screamingsandals.lib.spectator.KeybindComponent;
import org.screamingsandals.lib.spectator.ScoreComponent;
import org.screamingsandals.lib.spectator.SelectorComponent;
import org.screamingsandals.lib.spectator.StorageNBTComponent;
import org.screamingsandals.lib.spectator.TextComponent;
import org.screamingsandals.lib.spectator.TranslatableComponent;
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
    @NotNull Component empty();

    @NotNull Component newLine();

    @NotNull Component space();

    @NotNull Component fromLegacy(@NotNull String legacy);

    @NotNull Component fromLegacy(@NotNull String legacy, char colorChar);

    @NotNull Component fromJson(@NotNull String json);

    BlockNBTComponent.@NotNull Builder blockNBT();

    EntityNBTComponent.@NotNull Builder entityNBT();

    KeybindComponent.@NotNull Builder keybind();

    ScoreComponent.@NotNull Builder score();

    SelectorComponent.@NotNull Builder selector();

    StorageNBTComponent.@NotNull Builder storageNBT();

    TextComponent.@NotNull Builder text();

    TranslatableComponent.@NotNull Builder translatable();

    @NotNull Color rgb(int red, int green, int blue);

    @Nullable Color named(@NotNull String name);

    @NotNull Color hexOrName(@NotNull String hex);

    @NotNull Color nearestNamedTo(@Nullable Color color);

    BossBar.@NotNull Builder bossBar();

    SoundStart.@NotNull Builder soundStart();

    SoundStop.@NotNull Builder soundStop();

    @NotNull SoundSource soundSource(@NotNull String source);

    Title.@NotNull Builder title();

    Book.@NotNull Builder book();

    ClickEvent.@NotNull Builder clickEvent();

    HoverEvent.@NotNull Builder hoverEvent();

    EntityContent.@NotNull Builder entityContent();

    ItemContent.@NotNull Builder itemContent();
}
