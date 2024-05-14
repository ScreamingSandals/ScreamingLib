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

package org.screamingsandals.lib.impl.bukkit.spectator;

import org.bukkit.SoundCategory;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.spectator.audience.adapter.BukkitAdapter;
import org.screamingsandals.lib.impl.bukkit.spectator.audience.adapter.BukkitConsoleAdapter;
import org.screamingsandals.lib.impl.bukkit.spectator.audience.adapter.BukkitPlayerAdapter;
import org.screamingsandals.lib.impl.bukkit.spectator.bossbar.BukkitBossBar1_8;
import org.screamingsandals.lib.impl.bukkit.spectator.bossbar.BukkitBossBar1_9;
import org.screamingsandals.lib.impl.bukkit.spectator.sound.BukkitDummySoundSource;
import org.screamingsandals.lib.impl.bukkit.spectator.sound.BukkitSoundSource;
import org.screamingsandals.lib.impl.bukkit.spectator.sound.BukkitSoundStart;
import org.screamingsandals.lib.impl.bukkit.spectator.sound.BukkitSoundStop;
import org.screamingsandals.lib.impl.bukkit.spectator.title.BukkitTitle;
import org.screamingsandals.lib.impl.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.impl.spectator.SpectatorBackend;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.audience.ConsoleAudience;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;

import java.util.Locale;


// I don't think we have to implement backend for raw CraftBukkit without md_5's retarded library, who uses raw CraftBukkit in 2023 anyway
public class SpigotBackend extends AbstractBungeeBackend {
    private SpectatorBackend adventureBackend;

    public SpigotBackend() {
        if (BukkitFeature.NBT_LONG_ARRAYS.isSupported()) {
            snbtSerializer = SNBTSerializer.builder()
                    .shouldSaveLongArraysDirectly(true)
                    .build();
        }
        if (BukkitFeature.ADVENTURE.isSupported()) {
            adventureBackend = SpigotBackendAdventureExtension.initAdventureBackend(snbtSerializer);
        }
    }

    public boolean hasAdventure() {
        return adventureBackend != null;
    }

    @Override
    public BossBar.@NotNull Builder bossBar() {
        if (adventureBackend != null) {
            return adventureBackend.bossBar();
        }

        if (BukkitFeature.MODERN_BOSSBARS.isSupported()) {
            return new BukkitBossBar1_9.BukkitBossBarBuilder();
        } else {
            return new BukkitBossBar1_8.BukkitBossBarBuilder();
        }
    }

    @Override
    public SoundStart.@NotNull Builder soundStart() {
        if (adventureBackend != null) {
            return adventureBackend.soundStart();
        }

        return new BukkitSoundStart.BukkitSoundStartBuilder();
    }

    @Override
    public SoundStop.@NotNull Builder soundStop() {
        if (adventureBackend != null) {
            return adventureBackend.soundStop();
        }

        return new BukkitSoundStop.BukkitSoundStartBuilder();
    }

    @Override
    public @NotNull SoundSource soundSource(@NotNull String source) {
        if (adventureBackend != null) {
            return adventureBackend.soundSource(source);
        }

        if (!BukkitFeature.SOUND_CATEGORY.isSupported()) {
            return new BukkitDummySoundSource(); // WHAT????
        }

        try {
            return new BukkitSoundSource(SoundCategory.valueOf(source.toUpperCase(Locale.ROOT)));
        } catch (Throwable t) {
            return new BukkitSoundSource(SoundCategory.NEUTRAL);
        }
    }

    @Override
    public @Nullable Component fromPlatform(@NotNull Object platformObject) {
        if (adventureBackend != null) {
            try {
                return adventureBackend.fromPlatform(platformObject);
            } catch (IllegalArgumentException ignored) {
                // Adventure-Backend cannot provide a Component for the specified platformObject, let's try Bungee-Backend
            }
        }

        return super.fromPlatform(platformObject);
    }

    @Override
    public Title.@NotNull Builder title() {
        if (adventureBackend != null) {
            return adventureBackend.title();
        }

        return new BukkitTitle.BukkitTitleBuilder();
    }

    @Override
    public Book.@NotNull Builder book() {
        if (adventureBackend != null) {
            return adventureBackend.book();
        }

        return new BukkitBook.BukkitBookBuilder();
    }

    @Override
    public @NotNull Component empty() {
        if (adventureBackend != null) {
            return adventureBackend.empty();
        }

        return super.empty();
    }

    @Override
    public @NotNull Component newLine() {
        if (adventureBackend != null) {
            return adventureBackend.newLine();
        }

        return super.newLine();
    }

    @Override
    public @NotNull Component space() {
        if (adventureBackend != null) {
            return adventureBackend.space();
        }

        return super.space();
    }

    @Override
    public BlockNBTComponent.@NotNull Builder blockNBT() {
        if (adventureBackend != null) {
            return adventureBackend.blockNBT();
        }

        return super.blockNBT();
    }

    @Override
    public EntityNBTComponent.@NotNull Builder entityNBT() {
        if (adventureBackend != null) {
            return adventureBackend.entityNBT();
        }

        return super.entityNBT();
    }

    @Override
    public KeybindComponent.@NotNull Builder keybind() {
        if (adventureBackend != null) {
            return adventureBackend.keybind();
        }

        return super.keybind();
    }

    @Override
    public ScoreComponent.@NotNull Builder score() {
        if (adventureBackend != null) {
            return adventureBackend.score();
        }

        return super.score();
    }

    @Override
    public SelectorComponent.@NotNull Builder selector() {
        if (adventureBackend != null) {
            return adventureBackend.selector();
        }

        return super.selector();
    }

    @Override
    public StorageNBTComponent.@NotNull Builder storageNBT() {
        if (adventureBackend != null) {
            return adventureBackend.storageNBT();
        }

        return super.storageNBT();
    }

    @Override
    public TextComponent.@NotNull Builder text() {
        if (adventureBackend != null) {
            return adventureBackend.text();
        }

        return super.text();
    }

    @Override
    public TranslatableComponent.@NotNull Builder translatable() {
        if (adventureBackend != null) {
            return adventureBackend.translatable();
        }

        return super.translatable();
    }

    @Override
    public @NotNull Color rgb(int red, int green, int blue) {
        if (adventureBackend != null) {
            return adventureBackend.rgb(red, green, blue);
        }

        return super.rgb(red, green, blue);
    }

    @Override
    public @Nullable Color named(@NotNull String name) {
        if (adventureBackend != null) {
            return adventureBackend.named(name);
        }

        return super.named(name);
    }

    @Override
    public @NotNull Color hexOrName(@NotNull String hex) {
        if (adventureBackend != null) {
            return adventureBackend.hexOrName(hex);
        }

        return super.hexOrName(hex);
    }

    @Override
    public @NotNull Color nearestNamedTo(@Nullable Color color) {
        if (adventureBackend != null) {
            return adventureBackend.nearestNamedTo(color);
        }

        return super.nearestNamedTo(color);
    }

    @Override
    public ClickEvent.@NotNull Builder clickEvent() {
        if (adventureBackend != null) {
            return adventureBackend.clickEvent();
        }

        return super.clickEvent();
    }

    @Override
    public HoverEvent.@NotNull Builder hoverEvent() {
        if (adventureBackend != null) {
            return adventureBackend.hoverEvent();
        }

        return super.hoverEvent();
    }

    @Override
    public EntityContent.@NotNull Builder entityContent() {
        if (adventureBackend != null) {
            return adventureBackend.entityContent();
        }

        return super.entityContent();
    }

    @Override
    public ItemContent.@NotNull Builder itemContent() {
        if (adventureBackend != null) {
            return adventureBackend.itemContent();
        }

        return super.itemContent();
    }

    @Override
    public @NotNull Component fromLegacy(@NotNull String legacy) {
        if (adventureBackend != null) {
            return adventureBackend.fromLegacy(legacy);
        }

        return super.fromLegacy(legacy);
    }

    @Override
    public @NotNull Component fromLegacy(@NotNull String legacy, char colorChar) {
        if (adventureBackend != null) {
            return adventureBackend.fromLegacy(legacy, colorChar);
        }

        return super.fromLegacy(legacy, colorChar);
    }

    @Override
    public @NotNull Component fromJson(@NotNull String json) {
        if (adventureBackend != null) {
            return adventureBackend.fromJson(json);
        }

        return super.fromJson(json);
    }

    @SuppressWarnings("unchecked")
    public <A extends Adapter> @NotNull A adapter(@NotNull CommandSender wrapper, @NotNull org.bukkit.command.CommandSender sender) {
        if (adventureBackend != null) {
            return SpigotBackendAdventureExtension.adapter(wrapper, sender);
        }

        if (sender instanceof Player && wrapper instanceof PlayerAudience) {
            return (A) new BukkitPlayerAdapter((PlayerAudience) wrapper, (Player) sender);
        } else if (sender instanceof ConsoleCommandSender && wrapper instanceof ConsoleAudience) {
            return (A) new BukkitConsoleAdapter((ConsoleAudience) wrapper, (ConsoleCommandSender) sender);
        } else {
            return (A) new BukkitAdapter(wrapper, sender);
        }
    }
}
