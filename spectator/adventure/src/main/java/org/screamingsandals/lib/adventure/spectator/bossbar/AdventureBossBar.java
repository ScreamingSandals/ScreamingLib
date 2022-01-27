package org.screamingsandals.lib.adventure.spectator.bossbar;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.bossbar.*;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AdventureBossBar extends BasicWrapper<net.kyori.adventure.bossbar.BossBar> implements BossBar {
    public AdventureBossBar(net.kyori.adventure.bossbar.BossBar wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public Component title() {
        return AdventureBackend.wrapComponent(wrappedObject.name());
    }

    @Override
    @NotNull
    public BossBar title(@NotNull Component title) {
        wrappedObject.name(title.as(net.kyori.adventure.text.Component.class));
        return this;
    }

    @Override
    public float progress() {
        return wrappedObject.progress();
    }

    @Override
    @NotNull
    public BossBar progress(float progress) {
        wrappedObject.progress(progress);
        return this;
    }

    @Override
    @NotNull
    @Unmodifiable
    public List<BossBarFlag> flags() {
        return BossBarUtils.convertFlags(wrappedObject.flags());
    }

    @Override
    @NotNull
    public BossBar flags(@NotNull List<BossBarFlag> flags) {
        wrappedObject.flags(BossBarUtils.convertFlags(flags));
        return this;
    }

    @Override
    @NotNull
    public BossBarColor color() {
        return BossBarUtils.convertColor(wrappedObject.color());
    }

    @Override
    @NotNull
    public BossBar color(@NotNull BossBarColor color) {
        wrappedObject.color(BossBarUtils.convertColor(color));
        return this;
    }

    @Override
    @NotNull
    public BossBarDivision division() {
        return BossBarUtils.convertDivision(wrappedObject.overlay());
    }

    @Override
    @NotNull
    public BossBar division(@NotNull BossBarDivision division) {
        wrappedObject.overlay(BossBarUtils.convertDivision(division));
        return this;
    }

    @Override
    @NotNull
    public RegisteredListener addListener(@NotNull BossBarListener listener) {
        var adventureListener = new AdventureBossBarListener(listener);
        wrappedObject.addListener(adventureListener);
        return adventureListener;
    }

    public void removeListener(@NotNull RegisteredListener listener) {
        Preconditions.checkArgument(listener instanceof AdventureBossBarListener, "Unknown listener type");
        wrappedObject.removeListener((AdventureBossBarListener) listener);
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    public static class AdventureBossBarBuilder implements BossBar.Builder {
        @NotNull
        private Component title = Component.empty();
        private float progress;
        @NotNull
        private BossBarColor color = BossBarColor.PURPLE;
        @NotNull
        private BossBarDivision division = BossBarDivision.NO_DIVISION;
        @NotNull
        private Collection<BossBarFlag> flags;
        private final List<BossBarListener> listeners = new ArrayList<>();

        @Override
        @NotNull
        public Builder flags(@NotNull Collection<BossBarFlag> flags) {
            this.flags = flags;
            return this;
        }

        @Override
        public @NotNull Builder flags(@NotNull BossBarFlag... flags) {
            this.flags = Arrays.asList(flags);
            return this;
        }

        @Override
        public @NotNull Builder listener(@NotNull BossBarListener listener) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener);
            }
            return this;
        }

        @Override
        @NotNull
        public BossBar build() {
            var bossBar = new AdventureBossBar(net.kyori.adventure.bossbar.BossBar.bossBar(
                    title.as(net.kyori.adventure.text.Component.class),
                    progress,
                    BossBarUtils.convertColor(color),
                    BossBarUtils.convertDivision(division),
                    BossBarUtils.convertFlags(flags)
            ));
            listeners.forEach(bossBar::addListener);
            return bossBar;
        }
    }
}
