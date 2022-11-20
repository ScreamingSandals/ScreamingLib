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

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemMeta;
import org.screamingsandals.lib.item.ItemTagKeys;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.spectator.*;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
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
                @NotNull Object headerComponent;
                @NotNull Object footerComponent;
                if (Component.empty().equals(headerC)) {
                    String clearString;
                    if (Server.isVersion(1, 15)) {
                        clearString = "{\"text\": \"\"}";
                    } else {
                        clearString = "{\"translate\": \"\"}";
                    }
                    headerComponent = ClassStorage.asMinecraftComponent(clearString);
                } else {
                    headerComponent = ClassStorage.asMinecraftComponent(headerC);
                }
                if (Component.empty().equals(footerC)) {
                    String clearString;
                    if (Server.isVersion(1, 15)) {
                        clearString = "{\"text\": \"\"}";
                    } else {
                        clearString = "{\"translate\": \"\"}";
                    }
                    footerComponent = ClassStorage.asMinecraftComponent(clearString);
                } else {
                    footerComponent = ClassStorage.asMinecraftComponent(footerC);
                }

                Object packet;
                if (ClientboundTabListPacketAccessor.getConstructor1() != null) {
                    packet = Reflect.construct(ClientboundTabListPacketAccessor.getConstructor1(), headerComponent, footerComponent);
                } else {
                    packet = Reflect.construct(ClientboundTabListPacketAccessor.getConstructor0());
                    Reflect.setField(packet, ClientboundTabListPacketAccessor.getFieldHeader(), headerComponent);
                    Reflect.setField(packet, ClientboundTabListPacketAccessor.getFieldFooter(), footerComponent);
                }
                ClassStorage.sendNMSConstructedPacket(commandSender(), packet);
            } catch (Throwable ignored) {
                try {
                    commandSender().setPlayerListHeaderFooter(headerC.toLegacy(), footerC.toLegacy());
                } catch (Throwable ignored2) {
                    // method is too new
                }
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
        try {
            var t = ClassStorage.asMinecraftComponent(title.title());
            var s = ClassStorage.asMinecraftComponent(title.subtitle());
            if (ClientboundSetTitlesAnimationPacketAccessor.getType() != null) {
                // 1.17+
                var times = Reflect.construct(ClientboundSetTitlesAnimationPacketAccessor.getConstructor0(), title.fadeIn(), title.stay(), title.fadeOut());
                ClassStorage.sendNMSConstructedPacket(commandSender(), times);

                var titleP = Reflect.construct(ClientboundSetTitleTextPacketAccessor.getConstructor0(), t);
                ClassStorage.sendNMSConstructedPacket(commandSender(), titleP);

                var subtitleP = Reflect.construct(ClientboundSetSubtitleTextPacketAccessor.getConstructor0(), s);
                ClassStorage.sendNMSConstructedPacket(commandSender(), subtitleP);
                return;
            } else if (ClientboundSetTitlesPacketAccessor.getType() != null) {
                // 1.8.8 - 1.16.5
                var times = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor0(), title.fadeIn(), title.stay(), title.fadeOut());
                ClassStorage.sendNMSConstructedPacket(commandSender(), times);

                var titleP = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor1(), ClientboundSetTitlesPacket_i_TypeAccessor.getFieldTITLE(), t);
                ClassStorage.sendNMSConstructedPacket(commandSender(), titleP);

                var subtitleP = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor1(), ClientboundSetTitlesPacket_i_TypeAccessor.getFieldSUBTITLE(), s);
                ClassStorage.sendNMSConstructedPacket(commandSender(), subtitleP);
                return;
            }
        } catch (Throwable ignored) {
        }
        // Hello Glowstone ;)
        try {
            commandSender().sendTitle(title.title().toLegacy(), title.subtitle().toLegacy(), (int) (title.fadeIn().toMillis() / 50), (int) (title.stay().toMillis() / 50), (int) (title.fadeOut().toMillis() / 50));
            return;
        } catch (Throwable ignored) {
        }
        commandSender().sendTitle(title.title().toLegacy(), title.subtitle().toLegacy());
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
        var player = commandSender();
        var pages = new ArrayList<StringTag>();
        for (var page : book.pages()) {
            pages.add(new StringTag(page.toJavaJson()));
        }
        var item = ItemFactory.builder()
                .type(ItemTypeHolder.of("minecraft:written_book"))
                .tag(CompoundTag.EMPTY
                        .with(ItemTagKeys.TITLE, book.title().toJavaJson())
                        .with(ItemTagKeys.AUTHOR, book.author().toJavaJson())
                        .with(ItemTagKeys.PAGES, pages)
                        .with(ItemTagKeys.RESOLVED, (byte) 1)
                )
                .build();

        if (item == null) {
            return;
        }

        var itemInHand = player.getItemInHand();
        player.setItemInHand(item.as(ItemStack.class));

        try {
            if (!Server.isVersion(1, 13)) {
                var bytebuf = Reflect.construct(FriendlyByteBufAccessor.getConstructor0(), Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1));
                var packet = Reflect.construct(ClientboundCustomPayloadPacketAccessor.getConstructor0(), "MC|BOpen", bytebuf);
                ClassStorage.sendNMSConstructedPacket(player, packet);
            } else if (!Server.isVersion(1, 13, 1)) {
                var bytebuf = Reflect.construct(FriendlyByteBufAccessor.getConstructor0(), Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1));
                var location = Reflect.construct(ResourceLocationAccessor.getConstructor0(), "minecraft:book_open");
                var packet = Reflect.construct(ClientboundCustomPayloadPacketAccessor.getConstructor1(), location, bytebuf);
                ClassStorage.sendNMSConstructedPacket(player, packet);
            } else {
                var packet = Reflect.construct(ClientboundOpenBookPacketAccessor.getConstructor0(), InteractionHandAccessor.getFieldMAIN_HAND());
                ClassStorage.sendNMSConstructedPacket(player, packet);
            }
        } catch (Throwable ignored) {
        } finally {
            player.setItemInHand(itemInHand);
        }
    }
}
