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

package org.screamingsandals.lib.bungee.spectator;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.event.BungeeClickEvent;
import org.screamingsandals.lib.bungee.spectator.event.BungeeHoverEvent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeEntityContent;
import org.screamingsandals.lib.bungee.spectator.event.hover.BungeeItemContent;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.reflect.Reflect;

public abstract class AbstractBungeeBackend implements SpectatorBackend {
    @Getter
    private static final BidirectionalConverter<BungeeComponent> additionalComponentConverter = BidirectionalConverter.build();
    @Override
    public Component empty() {
        // We can't use NoArgsConstructor because it's too new
        return new BungeeComponent(new TextComponent(""));
    }

    @Override
    public BlockNBTComponent.Builder blockNBT() {
        return null; // TODO
    }

    @Override
    public EntityNBTComponent.Builder entityNBT() {
        return null; // TODO
    }

    @Override
    public KeybindComponent.Builder keybind() {
        return null; // TODO
    }

    @Override
    public ScoreComponent.Builder score() {
        return null; // TODO
    }

    @Override
    public SelectorComponent.Builder selector() {
        return null; // TODO
    }

    @Override
    public StorageNBTComponent.Builder storageNBT() {
        return null; // TODO
    }

    @Override
    public org.screamingsandals.lib.spectator.TextComponent.Builder text() {
        return new BungeeTextComponent.BungeeTextBuilder(new TextComponent(""));
    }

    @Override
    public org.screamingsandals.lib.spectator.TranslatableComponent.Builder translatable() {
        return new BungeeTranslatableContent.BungeeTranslatableBuilder(new TranslatableComponent(""));
    }

    @Override
    public Color rgb(int red, int green, int blue) {
        int combined = red << 16 | green << 8 | blue;
        //noinspection ConstantConditions - stfu idea
        if (ChatColor.ALL_CODES.contains("Xx")) {
            return new BungeeColor(ChatColor.of(String.format("#%06X", (0xFFFFFF & combined))));
        } else {
            switch (combined) {
                case 0x0000AA:
                    return new BungeeColor(ChatColor.DARK_BLUE);
                case 0x00AA00:
                    return new BungeeColor(ChatColor.DARK_GREEN);
                case 0x00AAAA:
                    return new BungeeColor(ChatColor.DARK_AQUA);
                case 0xAA0000:
                    return new BungeeColor(ChatColor.DARK_RED);
                case 0xAA00AA:
                    return new BungeeColor(ChatColor.DARK_PURPLE);
                case 0xFFAA00:
                    return new BungeeColor(ChatColor.GOLD);
                case 0xAAAAAA:
                    return new BungeeColor(ChatColor.GRAY);
                case 0x555555:
                    return new BungeeColor(ChatColor.DARK_GRAY);
                case 0x5555FF:
                    return new BungeeColor(ChatColor.BLUE);
                case 0x55FF55:
                    return new BungeeColor(ChatColor.GREEN);
                case 0x55FFFF:
                    return new BungeeColor(ChatColor.AQUA);
                case 0xFF5555:
                    return new BungeeColor(ChatColor.RED);
                case 0xFF55FF:
                    return new BungeeColor(ChatColor.LIGHT_PURPLE);
                case 0xFFFF55:
                    return new BungeeColor(ChatColor.YELLOW);
                case 0xFFFFFF:
                    return new BungeeColor(ChatColor.WHITE);
                default:
                    return new BungeeColor(ChatColor.BLACK);
            }
        }
    }

    @Override
    public Color named(String name) {
        return new BungeeColor(ChatColor.of(name.toLowerCase()));
    }

    @Override
    public ClickEvent.Builder clickEvent() {
        return new BungeeClickEvent.BungeeClickBuilder();
    }

    @Override
    public HoverEvent.Builder hoverEvent() {
        return new BungeeHoverEvent.BungeeHoverEventBuilder();
    }

    @Override
    public EntityContent.Builder entityContent() {
        if (Reflect.has("net.md_5.bungee.api.chat.hover.content.Entity")) {
            return new BungeeEntityContent.BungeeEntityContentBuilder();
        } else {
            return null; // TODO: legacy
        }
    }

    @Override
    public ItemContent.Builder itemContent() {
        if (Reflect.has("net.md_5.bungee.api.chat.hover.content.Item")) {
            return new BungeeItemContent.BungeeItemContentBuilder();
        } else {
            return null; // TODO: legacy
        }
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable BaseComponent component) {
        if (component == null) {
            return null;
        }

        // TODO:

        // NBTComponent doesn't exist for some reason

        // ScoreComponent added in a3b44aa612c629955195b4697641de1b1665a587 (Feb 2018 (1.12), but existed in MC 1.8+)
        // SelectorComponent added in the same commit

        // KeybindComponent added in fbc5f514e28dbfc3f85cb936ad95b1a979086ab6 (1.12 released on June, this is from Nov of the same year)

        if (component instanceof TranslatableComponent) {
            return new BungeeTranslatableContent((TranslatableComponent) component);
        }

        if (component instanceof TextComponent) {
            return new BungeeTextComponent((TextComponent) component);
        }

        return new BungeeComponent(component);
    }
}
