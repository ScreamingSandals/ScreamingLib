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

package org.screamingsandals.lib.bukkit.spectator.bossbar;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.bossbar.*;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitBossBar extends BasicWrapper<BossBar> implements org.screamingsandals.lib.spectator.bossbar.BossBar {
    private final List<RegisteredListener> internalListeners = new ArrayList<>();

    public BukkitBossBar(BossBar wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Component title() {
        return Component.fromLegacy(wrappedObject.getTitle());
    }

    @Override
    public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar title(@NotNull Component title) {
        var old = title();
        wrappedObject.setTitle(title.toLegacy());
        internalListeners.forEach(registeredListener -> registeredListener.listener().onTitleChanged(this, old, title));
        return this;
    }

    @Override
    public float progress() {
        return (float) wrappedObject.getProgress();
    }

    @Override
    public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar progress(float progress) {
        wrappedObject.setProgress(progress);
        internalListeners.forEach(registeredListener -> registeredListener.listener().onProgressChanged(this, (float) wrappedObject.getProgress(), progress));
        return this;
    }

    @Override
    public @NotNull @Unmodifiable List<BossBarFlag> flags() {
        return Arrays.stream(BossBarFlag.values())
                .filter(bossBarFlag -> {
                    try {
                        return wrappedObject.hasFlag(BarFlag.valueOf(bossBarFlag.name().replace("CREATE_WORLD_FOG", "CREATE_FOG")));
                    } catch (Throwable ignored) {
                        return false;
                    }
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar flags(@NotNull List<BossBarFlag> flags) {
        var added = new ArrayList<BossBarFlag>();
        var removed = new ArrayList<BossBarFlag>();
        for (BarFlag value : BarFlag.values()) {
            try {
                var v = value.name().replace("CREATE_FOG", "CREATE_WORLD_FOG");
                var b = flags.stream().filter(bossBarFlag -> bossBarFlag.name().equals(v)).findFirst();
                if (wrappedObject.hasFlag(value)) {
                    if (b.isEmpty()) {
                        wrappedObject.removeFlag(value);
                        try {
                            removed.add(BossBarFlag.valueOf(value.name().replace("CREATE_FOG", "CREATE_WORLD_FOG")));
                        } catch (Throwable ignored) {}
                    }
                } else if (b.isPresent()) {
                    wrappedObject.addFlag(value);
                    added.add(b.get());
                }
            } catch (Throwable ignored) {
                if (wrappedObject.hasFlag(value)) {
                    wrappedObject.removeFlag(value);
                    try {
                        removed.add(BossBarFlag.valueOf(value.name().replace("CREATE_FOG", "CREATE_WORLD_FOG")));
                    } catch (Throwable ignored2) {}
                }
            }
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onFlagsChanged(this, added, removed));
        return this;
    }

    @Override
    public @NotNull BossBarColor color() {
        try {
            return BossBarColor.valueOf(wrappedObject.getColor().name());
        } catch (Throwable ignored) {
            return BossBarColor.PURPLE;
        }
    }

    @Override
    public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar color(@NotNull BossBarColor color) {
        try {
            var old = color();
            wrappedObject.setColor(BarColor.valueOf(color.name()));
            internalListeners.forEach(registeredListener -> registeredListener.listener().onColorChanged(this, old, color));
        } catch (Throwable ignored) {
        }
        return this;
    }

    @Override
    public @NotNull BossBarDivision division() {
        try {
            var style = wrappedObject.getStyle().name().replace("SEGMENTED", "NOTCHED").replace("SOLID", "NO_DIVISION");
            return BossBarDivision.valueOf(style);
        } catch (Throwable ignored) {
            return BossBarDivision.NO_DIVISION;
        }
    }

    @Override
    public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar division(@NotNull BossBarDivision division) {
        try {
            var style = wrappedObject.getStyle().name().replace("NOTCHED", "SEGMENTED").replace("NO_DIVISION", "SOLID");
            var old = division();
            wrappedObject.setStyle(BarStyle.valueOf(style));
            internalListeners.forEach(registeredListener -> registeredListener.listener().onDivisionChanged(this, old, division));
        } catch (Throwable ignored) {
        }
        return this;
    }

    @Override
    @NotNull
    public RegisteredListener addListener(@NotNull BossBarListener listener) {
        RegisteredListener registered = () -> listener;
        internalListeners.add(registered);
        return registered;
    }

    @Override
    public void removeListener(@NotNull RegisteredListener listener) {
        internalListeners.remove(listener);
    }

    @Setter
    @Accessors(fluent = true)
    public static class BukkitBossBarBuilder implements org.screamingsandals.lib.spectator.bossbar.BossBar.Builder {
        private Component title = Component.empty();
        private float progress = 0;
        private BossBarColor color = BossBarColor.PINK;
        private BossBarDivision division = BossBarDivision.NO_DIVISION;
        private Collection<BossBarFlag> flags;
        private final List<BossBarListener> listeners = new ArrayList<>();

        @Override
        @NotNull
        public Builder flags(@NotNull Collection<BossBarFlag> flags) {
            this.flags = flags;
            return this;
        }

        @Override
        @NotNull
        public Builder flags(@NotNull BossBarFlag... flags) {
            this.flags = Arrays.asList(flags);
            return this;
        }

        @Override
        @NotNull
        public Builder listener(@NotNull BossBarListener listener) {
            listeners.add(listener);
            return this;
        }

        @Override
        @NotNull
        public org.screamingsandals.lib.spectator.bossbar.BossBar build() {
            var boss = new BukkitBossBar(Bukkit.createBossBar(
                    title.toLegacy(),
                    BarColor.PURPLE,
                    BarStyle.SOLID
            ));
            boss.color(color);
            boss.division(division);
            boss.progress(progress);
            if (flags != null && !flags.isEmpty()) {
                boss.flags(List.copyOf(flags));
            }
            listeners.forEach(boss::addListener);
            return boss;
        }
    }
}
