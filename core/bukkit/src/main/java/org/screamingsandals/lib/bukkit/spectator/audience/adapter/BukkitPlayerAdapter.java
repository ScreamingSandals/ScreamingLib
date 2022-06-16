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

package org.screamingsandals.lib.bukkit.spectator.audience.adapter;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

public class BukkitPlayerAdapter extends BukkitAdapter implements PlayerAdapter {
    public BukkitPlayerAdapter(PlayerAudience owner, Player commandSender) {
        super(owner, commandSender);
    }

    @Override
    public PlayerAudience owner() {
        return (PlayerAudience) super.owner();
    }

    @Override
    public Player commandSender() {
        return (Player) super.commandSender();
    }

    @Override
    public void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        if (source != null && Reflect.hasMethod(commandSender().spigot(), "sendMessage", ChatMessageType.class, UUID.class, BaseComponent.class)) {
            commandSender().spigot().sendMessage(messageType == MessageType.SYSTEM ? ChatMessageType.SYSTEM : ChatMessageType.CHAT, source, comp.as(BaseComponent.class));
        } else {
            commandSender().spigot().sendMessage(messageType == MessageType.SYSTEM ? ChatMessageType.SYSTEM : ChatMessageType.CHAT, comp.as(BaseComponent.class));
        }
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        commandSender().spigot().sendMessage(ChatMessageType.ACTION_BAR, comp.as(BaseComponent.class));
    }

    @Override
    public void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        var headerC = header instanceof AudienceComponentLike ? ((AudienceComponentLike) header).asComponent(owner()) : header.asComponent();
        var footerC = footer instanceof AudienceComponentLike ? ((AudienceComponentLike) footer).asComponent(owner()) : footer.asComponent();
        if (Reflect.hasMethod(commandSender(), "setPlayerListHeaderFooter", BaseComponent.class, BaseComponent.class)) {
            commandSender().setPlayerListHeaderFooter(headerC.as(BaseComponent.class), footerC.as(BaseComponent.class));
        } else {
            try {
                // TODO: try to send components using nms
                commandSender().setPlayerListHeaderFooter(headerC.toLegacy(), footerC.toLegacy());
            } catch (Throwable ignored) {
                // TODO: method is too new, fallback to NMS
            }
        }
    }

    @Override
    public void showTitle(@NotNull Title title) {
        if (Reflect.has("com.destroystokyo.paper.Title")) {
            commandSender().sendTitle(
                    com.destroystokyo.paper.Title.builder()
                            .title(title.title().as(BaseComponent.class))
                            .subtitle(title.subtitle().as(BaseComponent.class))
                            .fadeIn((int) (title.fadeIn().toMillis() / 50))
                            .stay((int) (title.stay().toMillis() / 50))
                            .fadeOut((int) (title.fadeOut().toMillis() / 50))
                            .build()
            );
            return;
        }
        // TODO: try NMS here
        try {
            commandSender().sendTitle(title.title().toLegacy(), title.subtitle().toLegacy(), (int) (title.fadeIn().toMillis() / 50), (int) (title.stay().toMillis() / 50), (int) (title.fadeOut().toMillis() / 50));
            return;
        } catch (Throwable ignored) {
        }
        try {
            commandSender().sendTitle(title.title().toLegacy(), title.subtitle().toLegacy());
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void showTitle(@NotNull TitleableAudienceComponentLike title, @Nullable TimesProvider times) {
        showTitle(title.asTitle(owner(), times));
    }

    @Override
    public void clearTitle() {
        commandSender().resetTitle();
    }

    @Override
    public void showBossBar(@NotNull BossBar bossBar) {
        var bukkitBoss = bossBar.as(org.bukkit.boss.BossBar.class);
        bukkitBoss.addPlayer(commandSender());
        if (!bukkitBoss.isVisible()) {
            bukkitBoss.setVisible(true);
        }
    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {
        bossBar.as(org.bukkit.boss.BossBar.class).removePlayer(commandSender());
    }

    @Override
    public void playSound(@NotNull SoundStart sound) {
        try {
            // TODO: 1.19: add seed if implemented by Spigot API
            commandSender().playSound(commandSender().getLocation(), sound.soundKey().asString(), sound.source().as(SoundCategory.class), sound.volume(), sound.pitch());
        } catch (Throwable ignored) {
            commandSender().playSound(commandSender().getLocation(), sound.soundKey().asString(), sound.volume(), sound.pitch());
        }
    }

    @Override
    public void playSound(@NotNull SoundStart sound, double x, double y, double z) {
        var location = new Location(commandSender().getWorld(), x, y, z); // I'm not sure if these locations are absolute or relative
        try {
            // TODO: 1.19: add seed if implemented by Spigot API
            commandSender().playSound(location, sound.soundKey().asString(), sound.source().as(SoundCategory.class), sound.volume(), sound.pitch());
        } catch (Throwable ignored) {
            commandSender().playSound(location, sound.soundKey().asString(), sound.volume(), sound.pitch());
        }
    }

    @Override
    public void stopSound(@NotNull SoundStop sound) {
        try {
            if (sound.soundKey() != null && sound.source() != null) {
                try {
                    commandSender().stopSound(sound.soundKey().asString(), sound.source().as(SoundCategory.class));
                } catch (Throwable ignored) {
                    commandSender().stopSound(sound.soundKey().asString()); // fallback to normal sound stop if SoundCategory doesn't work
                }
            } else if (sound.soundKey() != null) {
                commandSender().stopSound(sound.soundKey().asString());
            } else if (sound.source() != null) {
                // looks like we don't have method for this one :(
            } else {
                commandSender().stopAllSounds();
            }
        } catch (Throwable ignored) {
            // Too old Bukkit version
        }
    }

    @Override
    public void openBook(@NotNull Book book) {
        // TODO: https://github.com/ScreamingSandals/SimpleInventories/blob/master/core/src/main/java/org/screamingsandals/simpleinventories/utils/BookUtils.java
    }
}
