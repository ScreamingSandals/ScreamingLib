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

package org.screamingsandals.lib.bukkit.spectator;

import io.papermc.paper.text.PaperComponents;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.SoundCategory;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.bukkit.spectator.bossbar.BukkitBossBar;
import org.screamingsandals.lib.bukkit.spectator.sound.BukkitDummySoundSource;
import org.screamingsandals.lib.bukkit.spectator.sound.BukkitSoundSource;
import org.screamingsandals.lib.bukkit.spectator.sound.BukkitSoundStart;
import org.screamingsandals.lib.bukkit.spectator.sound.BukkitSoundStop;
import org.screamingsandals.lib.bukkit.spectator.title.BukkitTitle;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.event.ClickEvent;
import org.screamingsandals.lib.spectator.event.HoverEvent;
import org.screamingsandals.lib.spectator.event.hover.EntityContent;
import org.screamingsandals.lib.spectator.event.hover.ItemContent;
import org.screamingsandals.lib.spectator.sound.SoundSource;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.reflect.Reflect;


// I don't think we have to implement backend for raw CraftBukkit without md_5's retarded library, who uses raw CraftBukkit in 2022 anyways
public class SpigotBackend extends AbstractBungeeBackend {
    private AdventureBackend adventureBackend;

    public SpigotBackend() {
        if (Reflect.has("net.kyori.adventure.Adventure")) {
            adventureBackend = new AdventureBackend();

            var gson = PaperComponents.gsonSerializer();

            AdventureBackend.getAdditionalComponentConverter()
                    .registerW2P(
                            BaseComponent[].class,
                            component -> ComponentSerializer.parse(gson.serialize(component.as(net.kyori.adventure.text.Component.class)))
                    )
                    .registerW2P(
                            BaseComponent.class,
                            component -> {
                                var arr = component.as(BaseComponent[].class);
                                if (arr.length == 0) {
                                    return new net.md_5.bungee.api.chat.TextComponent("");
                                } else if (arr.length == 1) {
                                    return arr[0];
                                } else {
                                    return new net.md_5.bungee.api.chat.TextComponent(arr);
                                }
                            }
                    );

            AbstractBungeeBackend.getAdditionalComponentConverter()
                    .registerW2P(
                            net.kyori.adventure.text.Component.class,
                            component -> gson.deserialize(ComponentSerializer.toString(component.as(BaseComponent.class)))
                    );
        }
    }

    public boolean hasAdventure() {
        return adventureBackend != null;
    }

    @Override
    public BossBar.Builder bossBar() {
        if (adventureBackend != null) {
            return adventureBackend.bossBar();
        }

        return new BukkitBossBar.BukkitBossBarBuilder();
    }

    @Override
    public SoundStart.Builder soundStart() {
        if (adventureBackend != null) {
            return adventureBackend.soundStart();
        }

        return new BukkitSoundStart.BukkitSoundStartBuilder();
    }

    @Override
    public SoundStop.Builder soundStop() {
        if (adventureBackend != null) {
            return adventureBackend.soundStop();
        }

        return new BukkitSoundStop.BukkitSoundStartBuilder();
    }

    @Override
    public SoundSource soundSource(String source) {
        if (adventureBackend != null) {
            return adventureBackend.soundSource(source);
        }

        if (!Reflect.has("org.bukkit.SoundCategory")) {
            return new BukkitDummySoundSource(); // WHAT????
        }

        try {
            return new BukkitSoundSource(SoundCategory.valueOf(source.toUpperCase()));
        } catch (Throwable t) {
            return new BukkitSoundSource(SoundCategory.NEUTRAL);
        }
    }

    @Override
    public Title.Builder title() {
        if (adventureBackend != null) {
            return adventureBackend.title();
        }

        return new BukkitTitle.BukkitTitleBuilder();
    }

    @Override
    public Book.Builder book() {
        if (adventureBackend != null) {
            return adventureBackend.book();
        }

        return new BukkitBook.BukkitBookBuilder();
    }

    @Override
    public Component empty() {
        if (adventureBackend != null) {
            return adventureBackend.empty();
        }

        return super.empty();
    }

    @Override
    public BlockNBTComponent.Builder blockNBT() {
        if (adventureBackend != null) {
            return adventureBackend.blockNBT();
        }

        return super.blockNBT();
    }

    @Override
    public EntityNBTComponent.Builder entityNBT() {
        if (adventureBackend != null) {
            return adventureBackend.entityNBT();
        }

        return super.entityNBT();
    }

    @Override
    public KeybindComponent.Builder keybind() {
        if (adventureBackend != null) {
            return adventureBackend.keybind();
        }

        return super.keybind();
    }

    @Override
    public ScoreComponent.Builder score() {
        if (adventureBackend != null) {
            return adventureBackend.score();
        }

        return super.score();
    }

    @Override
    public SelectorComponent.Builder selector() {
        if (adventureBackend != null) {
            return adventureBackend.selector();
        }

        return super.selector();
    }

    @Override
    public StorageNBTComponent.Builder storageNBT() {
        if (adventureBackend != null) {
            return adventureBackend.storageNBT();
        }

        return super.storageNBT();
    }

    @Override
    public TextComponent.Builder text() {
        if (adventureBackend != null) {
            return adventureBackend.text();
        }

        return super.text();
    }

    @Override
    public TranslatableComponent.Builder translatable() {
        if (adventureBackend != null) {
            return adventureBackend.translatable();
        }

        return super.translatable();
    }

    @Override
    public Color rgb(int red, int green, int blue) {
        if (adventureBackend != null) {
            return adventureBackend.rgb(red, green, blue);
        }

        return super.rgb(red, green, blue);
    }

    @Override
    public Color named(String name) {
        if (adventureBackend != null) {
            return adventureBackend.named(name);
        }

        return super.named(name);
    }

    @Override
    public ClickEvent.Builder clickEvent() {
        if (adventureBackend != null) {
            return adventureBackend.clickEvent();
        }

        return super.clickEvent();
    }

    @Override
    public HoverEvent.Builder hoverEvent() {
        if (adventureBackend != null) {
            return adventureBackend.hoverEvent();
        }

        return super.hoverEvent();
    }

    @Override
    public EntityContent.Builder entityContent() {
        if (adventureBackend != null) {
            return adventureBackend.entityContent();
        }

        return super.entityContent();
    }

    @Override
    public ItemContent.Builder itemContent() {
        if (adventureBackend != null) {
            return adventureBackend.itemContent();
        }

        return super.itemContent();
    }

    @Override
    public Component fromLegacy(String legacy) {
        if (adventureBackend != null) {
            return adventureBackend.fromLegacy(legacy);
        }

        return super.fromLegacy(legacy);
    }
}
