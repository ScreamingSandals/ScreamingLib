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

package org.screamingsandals.lib.impl.bukkit.firework;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.firework.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectType;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitFireworkEffect extends BasicWrapper<org.bukkit.FireworkEffect> implements FireworkEffect {
    public BukkitFireworkEffect(org.bukkit.FireworkEffect.@NotNull Type type) {
        this(org.bukkit.FireworkEffect.builder().with(type).withColor(Color.WHITE).build());
    }

    public BukkitFireworkEffect(@NotNull org.bukkit.FireworkEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getType().name();
    }

    @Override
    public @NotNull FireworkEffectType type() {
        return new BukkitFireworkEffectType(wrappedObject.getType());
    }

    @Override
    public @NotNull List<org.screamingsandals.lib.spectator.@NotNull Color> colors() {
        return wrappedObject.getColors()
                .stream()
                .map(color -> org.screamingsandals.lib.spectator.Color.rgb(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<org.screamingsandals.lib.spectator.@NotNull Color> fadeColors() {
        return wrappedObject.getFadeColors()
                .stream()
                .map(color -> org.screamingsandals.lib.spectator.Color.rgb(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean flicker() {
        return wrappedObject.hasFlicker();
    }

    @Override
    public boolean trail() {
        return wrappedObject.hasTrail();
    }

    @Override
    public @NotNull FireworkEffect withType(@NotNull FireworkEffectType type) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffect(
                org.bukkit.FireworkEffect.builder()
                        .with(type.as(org.bukkit.FireworkEffect.Type.class))
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public @NotNull FireworkEffect withColors(@NotNull List<org.screamingsandals.lib.spectator.@NotNull Color> colors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffect(
                org.bukkit.FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(colors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public @NotNull FireworkEffect withFadeColors(@NotNull List<org.screamingsandals.lib.spectator.@NotNull Color> fadeColors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffect(
                org.bukkit.FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(fadeColors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public @NotNull FireworkEffect withFlicker(boolean flicker) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffect(
                org.bukkit.FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(flicker)
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public @NotNull FireworkEffect withTrail(boolean trail) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffect(
                org.bukkit.FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(trail)
                        .build()
        );
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof org.bukkit.FireworkEffect.Type) {
            return wrappedObject.getType() == object;
        }
        if (object instanceof org.bukkit.FireworkEffect || object instanceof FireworkEffect) {
            return equals(object);
        }
        return equals(FireworkEffect.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type == org.bukkit.FireworkEffect.Type.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }
}
