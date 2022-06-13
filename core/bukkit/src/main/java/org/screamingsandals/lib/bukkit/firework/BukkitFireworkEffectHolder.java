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

package org.screamingsandals.lib.bukkit.firework;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitFireworkEffectHolder extends BasicWrapper<FireworkEffect> implements FireworkEffectHolder {
    public BukkitFireworkEffectHolder(FireworkEffect.Type type) {
        this(FireworkEffect.builder().with(type).withColor(Color.WHITE).build());
    }

    public BukkitFireworkEffectHolder(FireworkEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getType().name();
    }

    @Override
    public List<org.screamingsandals.lib.spectator.Color> colors() {
        return wrappedObject.getColors()
                .stream()
                .map(color -> org.screamingsandals.lib.spectator.Color.rgb(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<org.screamingsandals.lib.spectator.Color> fadeColors() {
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
    public FireworkEffectHolder withColors(List<org.screamingsandals.lib.spectator.Color> colors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(colors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withFadeColors(List<org.screamingsandals.lib.spectator.Color> fadeColors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(fadeColors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withFlicker(boolean flicker) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(flicker)
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withTrail(boolean trail) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(trail)
                        .build()
        );
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof FireworkEffect.Type) {
            return wrappedObject.getType().equals(object);
        }
        if (object instanceof FireworkEffect || object instanceof FireworkEffectHolder) {
            return equals(object);
        }
        return equals(FireworkEffectHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == FireworkEffect.Type.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }
}
