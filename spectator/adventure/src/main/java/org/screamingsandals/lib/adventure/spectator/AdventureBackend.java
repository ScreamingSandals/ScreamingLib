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

package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureEntityContent;
import org.screamingsandals.lib.adventure.spectator.event.hover.AdventureItemContent;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

public class AdventureBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        return new AdventureComponent(net.kyori.adventure.text.Component.empty());
    }

    @Override
    public org.screamingsandals.lib.spectator.BlockNBTComponent.Builder blockNBT() {
        return new AdventureBlockNBTComponent.AdventureBlockNBTBuilder(net.kyori.adventure.text.Component.blockNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.EntityNBTComponent.Builder entityNBT() {
        return new AdventureEntityNBTComponent.AdventureEntityNBTBuilder(net.kyori.adventure.text.Component.entityNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.KeybindComponent.Builder keybind() {
        return new AdventureKeybindComponent.AdventureKeybindBuilder(net.kyori.adventure.text.Component.keybind());
    }

    @Override
    public org.screamingsandals.lib.spectator.ScoreComponent.Builder score() {
        return new AdventureScoreComponent.AdventureScoreBuilder(net.kyori.adventure.text.Component.score());
    }

    @Override
    public org.screamingsandals.lib.spectator.SelectorComponent.Builder selector() {
        return new AdventureSelectorComponent.AdventureSelectorBuilder(net.kyori.adventure.text.Component.selector());
    }

    @Override
    public org.screamingsandals.lib.spectator.StorageNBTComponent.Builder storageNBT() {
        return new AdventureStorageNBTComponent.AdventureStorageNBTBuilder(net.kyori.adventure.text.Component.storageNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.TextComponent.Builder text() {
        return new AdventureTextComponent.AdventureTextBuilder(net.kyori.adventure.text.Component.text());
    }

    @Override
    public org.screamingsandals.lib.spectator.TranslatableComponent.Builder translatable() {
        return new AdventureTranslatableComponent.AdventureTranslatableBuilder(net.kyori.adventure.text.Component.translatable());
    }

    @Override
    public Color rgb(int red, int green, int blue) {
        return new AdventureColor(TextColor.color(red, green, blue));
    }

    @Override
    public Color named(String name) {
        var value = NamedTextColor.NAMES.value(name.toLowerCase());
        if (value != null) {
            return new AdventureColor(value);
        }
        var hex = TextColor.fromCSSHexString(name);
        if (hex != null) {
            return new AdventureColor(hex);
        }
        return null;
    }

    @Override
    public BossBar.Builder bossBar() {
        return null; // TODO
    }

    @Override
    public SoundStart.Builder soundStart() {
        return null; // TODO
    }

    @Override
    public SoundStop.Builder soundStop() {
        return null; // TODO
    }

    @Override
    public SoundSource soundSource(String source) {
        return null; // TODO
    }

    @Override
    public Title.Builder title() {
        return null; // TODO
    }

    @Override
    public Book.Builder book() {
        return null; // TODO
    }

    @Override
    public ClickEvent.Builder clickEvent() {
        return new AdventureClickEvent.AdventureClickEventBuilder();
    }

    @Override
    public HoverEvent.Builder hoverEvent() {
        return new AdventureHoverEvent.AdventureHoverEventBuilder();
    }

    @Override
    public EntityContent.Builder entityContent() {
        return new AdventureEntityContent.AdventureEntityContentBuilder();
    }

    @Override
    public ItemContent.Builder itemContent() {
        return new AdventureItemContent.AdventureItemContentBuilder();
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable net.kyori.adventure.text.Component component) {
        if (component == null) {
            return null;
        }

        if (component instanceof StorageNBTComponent) {
            return new AdventureStorageNBTComponent((StorageNBTComponent) component);
        }

        if (component instanceof EntityNBTComponent) {
            return new AdventureEntityNBTComponent((EntityNBTComponent) component);
        }

        if (component instanceof BlockNBTComponent) {
            return new AdventureBlockNBTComponent((BlockNBTComponent) component);
        }

        if (component instanceof TranslatableComponent) {
            return new AdventureTranslatableComponent((TranslatableComponent) component);
        }

        if (component instanceof SelectorComponent) {
            return new AdventureSelectorComponent((SelectorComponent) component);
        }

        if (component instanceof ScoreComponent) {
            return new AdventureScoreComponent((ScoreComponent) component);
        }

        if (component instanceof KeybindComponent) {
            return new AdventureKeybindComponent((KeybindComponent) component);
        }

        if (component instanceof TextComponent) {
            return new AdventureTextComponent((TextComponent) component);
        }

        return new AdventureComponent(component);
    }
}
