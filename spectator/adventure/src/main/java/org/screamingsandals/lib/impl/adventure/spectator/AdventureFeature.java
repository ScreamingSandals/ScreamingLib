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

import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class AdventureFeature {
    public static final @NotNull PlatformFeature PLAIN_TEXT_COMPONENT_SERIALIZER = PlatformFeature.of(() -> Reflect.has("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer"));
    public static final @NotNull PlatformFeature NBT_SEPARATOR = PlatformFeature.of(() -> Reflect.hasMethod(NBTComponent.class, "separator"));
    public static final @NotNull PlatformFeature SELECTOR_SEPARATOR = PlatformFeature.of(() -> Reflect.hasMethod(SelectorComponent.class, "separator"));
    public static final @NotNull PlatformFeature TRANSLATABLE_FALLBACK = PlatformFeature.of(() -> Reflect.hasMethod(TranslatableComponent.class, "fallback"));
    public static final @NotNull PlatformFeature TAB_HEADER_FOOTER_SENDING = PlatformFeature.of(() -> Reflect.hasMethod(Audience.class, "sendPlayerListHeaderAndFooter", Component.class, Component.class));
    public static final @NotNull PlatformFeature SOUND_SEED = PlatformFeature.of(() -> Reflect.hasMethod(Sound.class, "seed"));
}
