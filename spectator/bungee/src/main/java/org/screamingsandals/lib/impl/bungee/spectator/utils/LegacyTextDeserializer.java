/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.utils;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bungee.spectator.BungeeChatFeature;

import java.util.ArrayList;

// Based on: https://github.com/SpigotMC/BungeeCord/blob/131125c7d219a0f3cd71121544c1e121c96583fb/chat/src/main/java/net/md_5/bungee/api/chat/TextComponent.java
// Without URL handling: that should not be part of the contract
@UtilityClass
public class LegacyTextDeserializer {
    public static @NotNull BaseComponent @NotNull [] fromLegacyText(@NotNull String message, @NotNull ChatColor defaultColor) {
        ArrayList<BaseComponent> components = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == ChatColor.COLOR_CHAR) {
                if (++i >= message.length()) {
                    break;
                }
                c = message.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c += 32;
                }
                ChatColor format;
                if (c == 'x' && i + 12 < message.length() && BungeeChatFeature.RGB_COLORS.isSupported()) {
                    StringBuilder hex = new StringBuilder("#");
                    for (int j = 0; j < 6; j++) {
                        hex.append(message.charAt(i + 2 + (j * 2)));
                    }
                    try {
                        format = ChatColor.of(hex.toString());
                    } catch (IllegalArgumentException ex) {
                        format = null;
                    }
                    i += 12;
                } else {
                    format = ChatColor.getByChar(c);
                }
                if (format == null) {
                    continue;
                }
                if (builder.length() > 0) {
                    TextComponent old = component;
                    component = new TextComponent(old);
                    old.setText(builder.toString());
                    builder = new StringBuilder();
                    components.add(old);
                }
                if (format == ChatColor.BOLD) {
                    component.setBold(true);
                } else if (format == ChatColor.ITALIC) {
                    component.setItalic(true);
                } else if (format == ChatColor.UNDERLINE) {
                    component.setUnderlined(true);
                } else if (format == ChatColor.STRIKETHROUGH) {
                    component.setStrikethrough(true);
                } else if (format == ChatColor.MAGIC) {
                    component.setObfuscated(true);
                } else {
                    if (format == ChatColor.RESET) {
                        format = defaultColor;
                    }
                    component = new TextComponent();
                    component.setColor(format);
                    component.setReset(true);
                }
                continue;
            }
            builder.append(c);
        }

        component.setText(builder.toString());
        components.add(component);
        return components.toArray(BaseComponent[]::new);
    }
}
