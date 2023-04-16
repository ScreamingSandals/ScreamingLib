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

package org.screamingsandals.lib.impl.bungee.spectator;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class BungeeChatFeature {
    // Components
    public static final @NotNull PlatformFeature KEYBIND_COMPONENT = PlatformFeature.of(() -> Reflect.has("net.md_5.bungee.api.chat.KeybindComponent"));
    public static final @NotNull PlatformFeature SCORE_COMPONENT = PlatformFeature.of(() -> Reflect.has("net.md_5.bungee.api.chat.ScoreComponent"));
    public static final @NotNull PlatformFeature SELECTOR_COMPONENT = PlatformFeature.of(() -> Reflect.has("net.md_5.bungee.api.chat.SelectorComponent"));

    // Component parts
    public static final @NotNull PlatformFeature FONT = PlatformFeature.of(() -> Reflect.hasMethod(BaseComponent.class, "getFontRaw"));
    public static final @NotNull PlatformFeature TRANSLATABLE_FALLBACK = PlatformFeature.of(() -> Reflect.hasMethod(TranslatableComponent.class, "getFallback"));
    public static final @NotNull PlatformFeature MODERN_HOVER_CONTENTS = PlatformFeature.of(() -> Reflect.has("net.md_5.bungee.api.chat.hover.content.Item") && Reflect.has("net.md_5.bungee.api.chat.hover.content.Entity"));

    // Misc
    public static final @NotNull PlatformFeature RGB_COLORS = PlatformFeature.of(() -> !ChatColor.class.isEnum());
}
