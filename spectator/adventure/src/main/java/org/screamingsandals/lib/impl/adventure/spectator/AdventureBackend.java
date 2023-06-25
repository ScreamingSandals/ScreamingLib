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

package org.screamingsandals.lib.impl.adventure.spectator;

import lombok.Getter;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.adventure.spectator.bossbar.AdventureBossBar;
import org.screamingsandals.lib.impl.adventure.spectator.event.AdventureClickEvent;
import org.screamingsandals.lib.impl.adventure.spectator.event.AdventureHoverEvent;
import org.screamingsandals.lib.impl.adventure.spectator.event.hover.AdventureEntityContent;
import org.screamingsandals.lib.impl.adventure.spectator.event.hover.AdventureItemContent;
import org.screamingsandals.lib.impl.adventure.spectator.sound.AdventureSoundSource;
import org.screamingsandals.lib.impl.adventure.spectator.sound.AdventureSoundStart;
import org.screamingsandals.lib.impl.adventure.spectator.sound.AdventureSoundStop;
import org.screamingsandals.lib.impl.adventure.spectator.title.AdventureTitle;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.impl.spectator.SpectatorBackend;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

public class AdventureBackend implements SpectatorBackend {
    @Getter
    private static final @NotNull BidirectionalConverter<AdventureComponent> additionalComponentConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<AdventureClickEvent> additionalClickEventConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<AdventureHoverEvent> additionalHoverEventConverter = BidirectionalConverter.build();
    @Getter
    // not needed for legacy because we can never have native Adventure alongside with pre-1.16 Bungee component api
    private static final @NotNull BidirectionalConverter<AdventureEntityContent> additionalEntityContentConverter = BidirectionalConverter.build();
    @Getter
    // not needed for legacy because we can never have native Adventure alongside with pre-1.16 Bungee component api
    private static final @NotNull BidirectionalConverter<AdventureItemContent> additionalItemContentConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull BidirectionalConverter<AdventureColor> additionalColorConverter = BidirectionalConverter.build();
    @Getter
    private static final @NotNull LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .build();
    @Getter
    private static final @NotNull ComponentSerializer<net.kyori.adventure.text.Component, net.kyori.adventure.text.Component, String> jsonComponentSerializer;
    @Getter
    private static final @NotNull ComponentSerializer<net.kyori.adventure.text.Component, TextComponent, String> plainTextComponentSerializer;
    @Getter
    private static final @NotNull Component empty = wrapComponent(net.kyori.adventure.text.Component.empty());
    @Getter
    private static final @NotNull Component newLine = wrapComponent(net.kyori.adventure.text.Component.newline());
    @Getter
    private static final @NotNull Component space = wrapComponent(net.kyori.adventure.text.Component.space());
    @Getter
    protected static @NotNull SNBTSerializer snbtSerializer = SNBTSerializer.builder()
            .shouldSaveLongArraysDirectly(false)
            .build();
    @Getter
    protected static @NotNull Function<String, String> soundKeyNormalizer = s -> s;

    static {
        ComponentSerializer<net.kyori.adventure.text.Component, TextComponent, String> plainText;
        if (AdventureFeature.PLAIN_TEXT_COMPONENT_SERIALIZER.isSupported()) {
            plainText = PlainTextComponentSerializer.plainText();
        } else {
            // Adventure pre-4.8.0
            //noinspection UnstableApiUsage
            plainText = PlainComponentSerializer.plain();
        }
        plainTextComponentSerializer = plainText;

        ComponentSerializer<net.kyori.adventure.text.Component, net.kyori.adventure.text.Component, String> jsonComponent;
        if (AdventureFeature.JSON_COMPONENT_SERIALIZER.isSupported()) {
            jsonComponent = JSONComponentSerializer.json();
            if ("net.kyori.adventure.text.serializer.json.DummyJSONComponentSerializer".equals(jsonComponent.getClass().getName()) && Reflect.has("net.kyori.adventure.text.serializer.gson.GsonComponentSerializer")) {
                // For some reason, jsonComponent is the dummy implementation, meaning the ServiceLoader does not work correctly. Lets fallback to GsonComponentSerializer in that case if possible
                jsonComponent = GsonComponentSerializer.gson();
            }
        } else {
            // Adventure <= 4.13.1: No abstract JSON impl
            jsonComponent = GsonComponentSerializer.gson();
        }

        jsonComponentSerializer = jsonComponent;
    }

    @Override
    public @NotNull Component empty() {
        return empty;
    }

    @Override
    public @NotNull Component newLine() {
        return newLine;
    }

    @Override
    public @NotNull Component space() {
        return space;
    }

    @Override
    public @NotNull Component fromLegacy(@NotNull String legacy) {
        return legacy.isEmpty() ? empty : wrapComponent(legacyComponentSerializer.deserialize(legacy));
    }

    @Override
    public @NotNull Component fromLegacy(@NotNull String legacy, char colorCharacter) {
        if (legacy.isEmpty()) {
            return empty;
        }
        if (colorCharacter == '§') {
            return fromLegacy(legacy);
        }
        return new AdventureComponent(
                LegacyComponentSerializer.builder()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .character(colorCharacter)
                    .build()
                    .deserialize(legacy)
        );
    }

    @Override
    public @NotNull Component fromJson(@NotNull String json) {
        if (json.isEmpty()) {
            return empty;
        }
        return new AdventureComponent(jsonComponentSerializer.deserialize(json));
    }

    @Override
    public org.screamingsandals.lib.spectator.BlockNBTComponent.@NotNull Builder blockNBT() {
        return new AdventureBlockNBTComponent.AdventureBlockNBTBuilder(net.kyori.adventure.text.Component.blockNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.EntityNBTComponent.@NotNull Builder entityNBT() {
        return new AdventureEntityNBTComponent.AdventureEntityNBTBuilder(net.kyori.adventure.text.Component.entityNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.KeybindComponent.@NotNull Builder keybind() {
        return new AdventureKeybindComponent.AdventureKeybindBuilder(net.kyori.adventure.text.Component.keybind());
    }

    @Override
    public org.screamingsandals.lib.spectator.ScoreComponent.@NotNull Builder score() {
        return new AdventureScoreComponent.AdventureScoreBuilder(net.kyori.adventure.text.Component.score());
    }

    @Override
    public org.screamingsandals.lib.spectator.SelectorComponent.@NotNull Builder selector() {
        return new AdventureSelectorComponent.AdventureSelectorBuilder(net.kyori.adventure.text.Component.selector());
    }

    @Override
    public org.screamingsandals.lib.spectator.StorageNBTComponent.@NotNull Builder storageNBT() {
        return new AdventureStorageNBTComponent.AdventureStorageNBTBuilder(net.kyori.adventure.text.Component.storageNBT());
    }

    @Override
    public org.screamingsandals.lib.spectator.TextComponent.@NotNull Builder text() {
        return new AdventureTextComponent.AdventureTextBuilder(net.kyori.adventure.text.Component.text());
    }

    @Override
    public org.screamingsandals.lib.spectator.TranslatableComponent.@NotNull Builder translatable() {
        return new AdventureTranslatableComponent.AdventureTranslatableBuilder(net.kyori.adventure.text.Component.translatable());
    }

    @Override
    public @NotNull Color rgb(int red, int green, int blue) {
        return new AdventureColor(TextColor.color(red, green, blue));
    }

    @Override
    public @Nullable Color named(@NotNull String name) {
        // bri'ish
        if ("grey".equalsIgnoreCase(name)) {
            name = "gray";
        } else if ("dark_grey".equalsIgnoreCase(name)) {
            name = "dark_gray";
        }

        var value = NamedTextColor.NAMES.value(name.toLowerCase(Locale.ROOT));
        if (value != null) {
            return new AdventureColor(value);
        }
        return null;
    }

    @Override
    public @NotNull Color hexOrName(@NotNull String hexName) {
        var value = NamedTextColor.NAMES.value(hexName.toLowerCase(Locale.ROOT));
        if (value != null) {
            return new AdventureColor(value);
        }
        if (!hexName.startsWith("#")) {
            hexName = "#" + hexName;
        }
        var hex = TextColor.fromCSSHexString(hexName);
        return new AdventureColor(hex != null ? hex : NamedTextColor.WHITE);
    }

    @Override
    public @NotNull Color nearestNamedTo(@Nullable Color color) {
        return new AdventureColor(NamedTextColor.nearestTo(color != null ? color.as(TextColor.class) : NamedTextColor.WHITE));
    }

    @Override
    public BossBar.@NotNull Builder bossBar() {
        return new AdventureBossBar.AdventureBossBarBuilder();
    }

    @Override
    public SoundStart.@NotNull Builder soundStart() {
        return new AdventureSoundStart.AdventureSoundStartBuilder();
    }

    @Override
    public SoundStop.@NotNull Builder soundStop() {
        return new AdventureSoundStop.AdventureSoundStopBuilder();
    }

    @Override
    public @NotNull SoundSource soundSource(@NotNull String source) {
        var soundSource = Sound.Source.NAMES.value(source);
        return new AdventureSoundSource(Objects.requireNonNullElse(soundSource, Sound.Source.MASTER));
    }

    @Override
    public Title.@NotNull Builder title() {
        return new AdventureTitle.AdventureTitleBuilder();
    }

    @Override
    public Book.@NotNull Builder book() {
        return new AdventureBook.AdventureBookBuilder();
    }

    @Override
    public ClickEvent.@NotNull Builder clickEvent() {
        return new AdventureClickEvent.AdventureClickEventBuilder();
    }

    @Override
    public HoverEvent.@NotNull Builder hoverEvent() {
        return new AdventureHoverEvent.AdventureHoverEventBuilder();
    }

    @Override
    public EntityContent.@NotNull Builder entityContent() {
        return new AdventureEntityContent.AdventureEntityContentBuilder();
    }

    @Override
    public ItemContent.@NotNull Builder itemContent() {
        return new AdventureItemContent.AdventureItemContentBuilder();
    }

    @Contract("null -> null; !null -> !null")
    public static @Nullable Component wrapComponent(net.kyori.adventure.text.@Nullable Component component) {
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
