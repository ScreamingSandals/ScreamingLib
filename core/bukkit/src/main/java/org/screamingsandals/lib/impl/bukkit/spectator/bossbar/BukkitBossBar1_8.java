/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bukkit.spectator.bossbar;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossFlag;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.impl.bukkit.utils.nms.entity.BossBarDragon;
import org.screamingsandals.lib.impl.bukkit.utils.nms.entity.BossBarWither;
import org.screamingsandals.lib.impl.bukkit.utils.nms.entity.FakeEntityNMS;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.bossbar.BossBarColor;
import org.screamingsandals.lib.spectator.bossbar.BossBarDivision;
import org.screamingsandals.lib.spectator.bossbar.BossBarFlag;
import org.screamingsandals.lib.spectator.bossbar.BossBarListener;
import org.screamingsandals.lib.spectator.bossbar.RegisteredListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BukkitBossBar1_8 implements BossBar {
    private final @NotNull List<@NotNull RegisteredListener> internalListeners = new ArrayList<>();
    private @NotNull Component title;
    private float progress;
    private @NotNull BossBarColor color;
    private @NotNull BossBarDivision division;
    private @NotNull List<@NotNull BossBarFlag> flags;
    private @NotNull BukkitBossBar1_8.@NotNull Backend backend;

    private final @NotNull FakeEntityNMS<?> bossbarEntity;
    private final boolean viaActive;
    private com.viaversion.viaversion.api.legacy.bossbar.BossBar viaBossBar; // can't be final (type may not exist, we don't want JVM to try initialising it)

    public BukkitBossBar1_8(@NotNull Component title, float progress, @NotNull BossBarColor color, @NotNull BossBarDivision division, @NotNull List<@NotNull BossBarFlag> flags, @NotNull BukkitBossBar1_8.@NotNull Backend backend) {
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.division = division;
        this.flags = flags;
        this.backend = backend;

        if (backend == Backend.ENDER_DRAGON) {
            bossbarEntity = new BossBarDragon(new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
        } else {
            bossbarEntity = new BossBarWither(new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
        }
        bossbarEntity.setVisible(true);

        boolean viaActive = false;
        if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            try {
                viaBossBar = Via.getAPI().legacyAPI().createLegacyBossBar("", 1, BossColor.PURPLE, BossStyle.SOLID);
                viaBossBar.show();
                viaActive = true;
            } catch (Throwable ignored) {
                // Too old ViaVersion is installed
            }
        }
        this.viaActive = viaActive;
    }

    public void addViewer(@NotNull Player player) {
        if (viaActive) {
            if (Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 107) {
                viaBossBar.addPlayer(player.getUniqueId());
                return;
            }
        }
        bossbarEntity.addViewer(player);
    }

    public void removeViewer(@NotNull Player player) {
        if (viaActive) {
            if (Via.getAPI().getPlayerVersion(player.getUniqueId()) >= 107) {
                viaBossBar.removePlayer(player.getUniqueId());
                return;
            }
        }
        bossbarEntity.removeViewer(player);
    }

    @Override
    public @NotNull Component title() {
        return this.title;
    }

    @Override
    public @NotNull BossBar title(@NotNull Component title) {
        var old = this.title;
        this.title = title;
        bossbarEntity.setCustomName(title);
        if (viaActive) {
            viaBossBar.setTitle(title.toLegacy());
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onTitleChanged(this, old, title));
        return this;
    }

    @Override
    public float progress() {
        return this.progress;
    }

    @Override
    public @NotNull BossBar progress(float progress) {
        var old = this.progress;
        if (Double.isNaN(progress) || progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }
        this.progress = progress;

        bossbarEntity.setHealth(progress);
        if (viaActive) {
            viaBossBar.setHealth(progress);
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onProgressChanged(this, old, this.progress));
        return this;
    }

    @Override
    public @NotNull @Unmodifiable List<@NotNull BossBarFlag> flags() {
        return List.copyOf(flags);
    }

    @Override
    public @NotNull BossBar flags(@NotNull List<@NotNull BossBarFlag> flags) {
        var old = this.flags;
        this.flags = flags;
        if (viaActive) {
            if (flags.contains(BossBarFlag.PLAY_BOSS_MUSIC)) {
                if (!viaBossBar.hasFlag(BossFlag.PLAY_BOSS_MUSIC)) {
                    viaBossBar.addFlag(BossFlag.PLAY_BOSS_MUSIC);
                }
            } else if (viaBossBar.hasFlag(BossFlag.PLAY_BOSS_MUSIC)) {
                viaBossBar.removeFlag(BossFlag.PLAY_BOSS_MUSIC);
            }

            if (flags.contains(BossBarFlag.DARKEN_SCREEN)) {
                if (!viaBossBar.hasFlag(BossFlag.DARKEN_SKY)) {
                    viaBossBar.addFlag(BossFlag.DARKEN_SKY);
                }
            } else if (viaBossBar.hasFlag(BossFlag.DARKEN_SKY)) {
                viaBossBar.removeFlag(BossFlag.DARKEN_SKY);
            }

            if (flags.contains(BossBarFlag.CREATE_WORLD_FOG)) {
                if (!viaBossBar.hasFlag(BossFlag.CREATE_FOG)) {
                    viaBossBar.addFlag(BossFlag.CREATE_FOG);
                }
            } else if (viaBossBar.hasFlag(BossFlag.CREATE_FOG)) {
                viaBossBar.removeFlag(BossFlag.CREATE_FOG);
            }
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onFlagsChanged(this, old, flags));
        return this;
    }

    @Override
    public @NotNull BossBarColor color() {
        return color;
    }

    @Override
    public @NotNull BossBar color(@NotNull BossBarColor color) {
        var old = this.color;
        this.color = color;
        if (viaActive) {
            try {
                viaBossBar.setColor(BossColor.valueOf(color.name()));
            } catch (Throwable ignored) {
            }
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onColorChanged(this, old, color));
        return this;
    }

    @Override
    public @NotNull BossBarDivision division() {
        return this.division;
    }

    @Override
    public @NotNull BossBar division(@NotNull BossBarDivision division) {
        var old = this.division;
        this.division = division;
        if (viaActive) {
            try {
                switch (division) {
                    case NO_DIVISION:
                        viaBossBar.setStyle(BossStyle.SOLID);
                        break;
                    case NOTCHED_6:
                        viaBossBar.setStyle(BossStyle.SEGMENTED_6);
                        break;
                    case NOTCHED_10:
                        viaBossBar.setStyle(BossStyle.SEGMENTED_10);
                        break;
                    case NOTCHED_12:
                        viaBossBar.setStyle(BossStyle.SEGMENTED_12);
                        break;
                    case NOTCHED_20:
                        viaBossBar.setStyle(BossStyle.SEGMENTED_20);
                        break;
                }
            } catch (Throwable ignored) {
            }
        }
        internalListeners.forEach(registeredListener -> registeredListener.listener().onDivisionChanged(this, old, division));
        return this;
    }

    @Override
    public @NotNull RegisteredListener addListener(@NotNull BossBarListener listener) {
        RegisteredListener registered = () -> listener;
        internalListeners.add(registered);
        return registered;
    }

    @Override
    public void removeListener(@NotNull RegisteredListener listener) {
        internalListeners.remove(listener);
    }

    @Override
    public @NotNull Object raw() {
        throw new UnsupportedOperationException("Not a wrapper!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        if (type.isInstance(this)) {
            return (T) this;
        }
        throw new UnsupportedOperationException("Not a wrapper!");
    }

    @Setter
    @Accessors(fluent = true)
    public static class BukkitBossBarBuilder implements org.screamingsandals.lib.spectator.bossbar.BossBar.Builder {
        private @NotNull Component title = Component.empty();
        private float progress;
        private @NotNull BossBarColor color = BossBarColor.PINK;
        private @NotNull BossBarDivision division = BossBarDivision.NO_DIVISION;
        private @Nullable Collection<@NotNull BossBarFlag> flags;
        private final @NotNull List<@NotNull BossBarListener> listeners = new ArrayList<>();

        @Override
        public @NotNull Builder flags(@NotNull Collection<@NotNull BossBarFlag> flags) {
            this.flags = flags;
            return this;
        }

        @Override
        public @NotNull Builder flags(@NotNull BossBarFlag @NotNull... flags) {
            this.flags = Arrays.asList(flags);
            return this;
        }

        @Override
        public @NotNull Builder listener(@NotNull BossBarListener listener) {
            listeners.add(listener);
            return this;
        }

        @Override
        public org.screamingsandals.lib.spectator.bossbar.@NotNull BossBar build() {
            var boss = new BukkitBossBar1_8(
                    title,
                    progress,
                    color,
                    division,
                    flags == null ? List.of() : List.copyOf(flags),
                    GlobalBossBarBackend1_8.getBackend()
            );
            listeners.forEach(boss::addListener);
            return boss;
        }
    }

    public enum Backend {
        WITHER,
        ENDER_DRAGON
    }
}
