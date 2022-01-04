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

package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@UtilityClass
public class BossBarUtils {
    public final Class<?> NATIVE_BOSSBAR_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "bossbar", "BossBar"));
    public final Class<?> NATIVE_BOSSBAR_OVERLAY_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "bossbar", "BossBar$Overlay"));
    public final Class<?> NATIVE_BOSSBAR_FLAG_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "bossbar", "BossBar$Flag"));
    public final Class<?> NATIVE_BOSSBAR_COLOR_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "bossbar", "BossBar$Color"));

    private final WeakHashMap<BossBar, Object> weakMap = new WeakHashMap<>();

    public Object bossBarToPlatform(BossBar bossBar) {
        if (NATIVE_BOSSBAR_CLASS.isInstance(bossBar)) {
            return bossBar;
        }
        return bossBarToPlatform(
                bossBar,
                NATIVE_BOSSBAR_CLASS,
                ComponentUtils.NATIVE_COMPONENT_CLASS,
                ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic(),
                NATIVE_BOSSBAR_FLAG_CLASS,
                NATIVE_BOSSBAR_OVERLAY_CLASS,
                NATIVE_BOSSBAR_COLOR_CLASS
        );
    }

    public Object bossBarToPlatform(BossBar bossBar, Class<?> bossBarClass, Class<?> componentClass, Object componentSerializer, Class<?> flagsClass, Class<?> overlayClass, Class<?> colorClass) {
        var nativeAdventure = componentSerializer == ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic();

        if (nativeAdventure && weakMap.containsKey(bossBar)) {
            return weakMap.get(bossBar);
        }

        var platformBossBar = Reflect
                .getMethod(bossBarClass, "name", componentClass)
                .invokeStaticResulted(ComponentUtils.componentToPlatform(bossBar.name(), componentSerializer));

        platformBossBar
                .getMethod("progress", float.class)
                .invoke(bossBar.progress());

        platformBossBar
                .getMethod("color", colorClass)
                .invoke(colorToPlatform(bossBar.color(), colorClass));

        platformBossBar
                .getMethod("overlay", overlayClass)
                .invoke(overlayToPlatform(bossBar.overlay(), overlayClass));

        platformBossBar
                .getMethod("flags", Set.class)
                .invoke(bossBar
                        .flags()
                        .stream()
                        .map(flag -> flagToPlatform(flag, flagsClass))
                        .collect(Collectors.toSet())
                );

        if (nativeAdventure) {
            weakMap.put(bossBar, platformBossBar.raw());
        }

        // Mirroring changes from the original to the transformed
        bossBar.addListener(new BossBar.Listener() {
            @Override
            public void bossBarNameChanged(@NotNull BossBar bar, @NotNull Component oldName, @NotNull Component newName) {
                platformBossBar
                        .getMethod("name", componentClass)
                        .invoke(ComponentUtils.componentToPlatform(newName, componentSerializer));
            }

            @Override
            public void bossBarProgressChanged(@NotNull BossBar bar, float oldProgress, float newProgress) {
                platformBossBar
                        .getMethod("progress", float.class)
                        .invoke(newProgress);
            }

            @Override
            public void bossBarColorChanged(@NotNull BossBar bar, BossBar.@NotNull Color oldColor, BossBar.@NotNull Color newColor) {
                platformBossBar
                        .getMethod("color", colorClass)
                        .invoke(colorToPlatform(newColor, colorClass));
            }

            @Override
            public void bossBarOverlayChanged(@NotNull BossBar bar, BossBar.@NotNull Overlay oldOverlay, BossBar.@NotNull Overlay newOverlay) {
                platformBossBar
                        .getMethod("overlay", overlayClass)
                        .invoke(overlayToPlatform(newOverlay, overlayClass));
            }

            @Override
            public void bossBarFlagsChanged(@NotNull BossBar bar, @NotNull Set<BossBar.Flag> flagsAdded, @NotNull Set<BossBar.Flag> flagsRemoved) {
                platformBossBar
                        .getMethod("addFlags", Iterable.class)
                        .invoke(flagsAdded
                                .stream()
                                .map(flag -> flagToPlatform(flag, flagsClass))
                                .collect(Collectors.toSet())
                        );
                platformBossBar
                        .getMethod("removeFlags", Iterable.class)
                        .invoke(flagsRemoved
                                .stream()
                                .map(flag -> flagToPlatform(flag, flagsClass))
                                .collect(Collectors.toSet())
                        );
            }
        });

        // TODO: Mirroring changes from transformed to original

        return platformBossBar.raw();
    }

    @SuppressWarnings("unchecked")
    public BossBar bossBarFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar) {
            return (BossBar) platformObject;
        }
        return null; // TODO
    }

    public Object colorToPlatform(BossBar.Color color) {
        if (NATIVE_BOSSBAR_COLOR_CLASS.isInstance(color)) {
            return color;
        }
        return colorToPlatform(color, NATIVE_BOSSBAR_COLOR_CLASS);
    }

    public Object colorToPlatform(BossBar.Color color, Class<?> colorClass) {
        return Reflect.findEnumConstant(colorClass, color.name());
    }

    public BossBar.Color colorFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Color) {
            return (BossBar.Color) platformObject;
        }
        return BossBar.Color.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }

    public Object overlayToPlatform(BossBar.Overlay overlay) {
        if (NATIVE_BOSSBAR_OVERLAY_CLASS.isInstance(overlay)) {
            return overlay;
        }
        return overlayToPlatform(overlay, NATIVE_BOSSBAR_OVERLAY_CLASS);
    }

    public Object overlayToPlatform(BossBar.Overlay overlay, Class<?> overlayClass) {
        return Reflect.findEnumConstant(overlayClass, overlay.name());
    }

    public BossBar.Overlay overlayFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Overlay) {
            return (BossBar.Overlay) platformObject;
        }
        return BossBar.Overlay.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }

    public Object flagToPlatform(BossBar.Flag flag) {
        if (NATIVE_BOSSBAR_FLAG_CLASS.isInstance(flag)) {
            return flag;
        }
        return flagToPlatform(flag, NATIVE_BOSSBAR_FLAG_CLASS);
    }

    public Object flagToPlatform(BossBar.Flag flag, Class<?> flagClass) {
        return Reflect.findEnumConstant(flagClass, flag.name());
    }

    public BossBar.Flag flagFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Flag) {
            return (BossBar.Flag) platformObject;
        }
        return BossBar.Flag.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }
}
