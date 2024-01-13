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

package org.screamingsandals.lib.impl.bukkit.spectator.audience.adapter;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.spectator.bossbar.BukkitBossBar1_8;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.item.ItemTagKeys;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.item.builder.ItemStackFactory;
import org.screamingsandals.lib.nbt.CompoundTag;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Book;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.TitleableAudienceComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.spectator.bossbar.BossBar;
import org.screamingsandals.lib.spectator.sound.SoundStart;
import org.screamingsandals.lib.spectator.sound.SoundStop;
import org.screamingsandals.lib.spectator.title.TimesProvider;
import org.screamingsandals.lib.spectator.title.Title;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;

public class BukkitPlayerAdapter extends BukkitAdapter implements PlayerAdapter {
    public BukkitPlayerAdapter(@NotNull PlayerAudience owner, @NotNull Player commandSender) {
        super(owner, commandSender);
    }

    @Override
    public @NotNull PlayerAudience owner() {
        return (PlayerAudience) super.owner();
    }

    @Override
    public @NotNull Player commandSender() {
        return (Player) super.commandSender();
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        commandSender().spigot().sendMessage(comp.as(BaseComponent.class));
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner()) : message.asComponent();
        if (BukkitFeature.BUNGEECORD_CHAT_SEND_MESSAGE_WITH_CHAT_MESSAGE_TYPE.isSupported() && BukkitFeature.HEX_COLORS.isSupported()) {
            // thanks to MC-119145 amd md_5, this method didn't work correctly till 1.16
            commandSender().spigot().sendMessage(ChatMessageType.ACTION_BAR, comp.as(BaseComponent.class));
        } else if (ClientboundSetTitlesPacket_i_TypeAccessor.getFieldACTIONBAR() != null) {
            // 1.11-1.16.5: Use Title packet to avoid MC-119145
            var titleP = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor1(), ClientboundSetTitlesPacket_i_TypeAccessor.getFieldACTIONBAR(), ClassStorage.asMinecraftComponent(comp));
            ClassStorage.sendNMSConstructedPacket(commandSender(), titleP);
        } else {
            // 1.8.8-1.10.2
            var components = new BaseComponent[] {new TextComponent(comp.toLegacy())}; // due to MC-119145, we have to convert it to legacy despite the packet accepts json

            var packet = Reflect.construct(PacketPlayOutChatAccessor.getConstructor0(), null, (byte) 2);
            Reflect.setField(packet, "components", components); // Spigot field, no mapping
            ClassStorage.sendNMSConstructedPacket(commandSender(), packet);
        }
    }

    @Override
    public void sendPlayerListHeaderFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        var headerC = header instanceof AudienceComponentLike ? ((AudienceComponentLike) header).asComponent(owner()) : header.asComponent();
        var footerC = footer instanceof AudienceComponentLike ? ((AudienceComponentLike) footer).asComponent(owner()) : footer.asComponent();
        if (BukkitFeature.PLAYER_SET_PLAYER_LIST_HEADER_FOOTER_COMPONENT.isSupported()) {
            commandSender().setPlayerListHeaderFooter(headerC.as(BaseComponent.class), footerC.as(BaseComponent.class));
        } else {
            try {
                @NotNull Object headerComponent;
                @NotNull Object footerComponent;
                if (Component.empty().equals(headerC)) {
                    String clearString;
                    if (BukkitFeature.EMPTY_COMPONENT_1_15.isSupported()) {
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
                    if (BukkitFeature.EMPTY_COMPONENT_1_15.isSupported()) {
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
            } catch (Throwable ignored) {  // will it ever crash? our reflect library is kinda safe
                if (BukkitFeature.PLAYER_SET_PLAYER_LIST_HEADER_FOOTER_TEXT.isSupported()) {
                    commandSender().setPlayerListHeaderFooter(headerC.toLegacy(), footerC.toLegacy());
                } // method is too new
            }
        }
    }

    @Override
    public void showTitle(@NotNull Title title) {
        if (BukkitFeature.DESTROYSTOKYO_TITLE.isSupported()) {
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
                var times = Reflect.construct(ClientboundSetTitlesAnimationPacketAccessor.getConstructor0(), (int) title.fadeIn().toMillis() / 50, (int) title.stay().toMillis() / 50, (int) title.fadeOut().toMillis() / 50);
                ClassStorage.sendNMSConstructedPacket(commandSender(), times);

                var titleP = Reflect.construct(ClientboundSetTitleTextPacketAccessor.getConstructor0(), t);
                ClassStorage.sendNMSConstructedPacket(commandSender(), titleP);

                var subtitleP = Reflect.construct(ClientboundSetSubtitleTextPacketAccessor.getConstructor0(), s);
                ClassStorage.sendNMSConstructedPacket(commandSender(), subtitleP);
                return;
            } else if (ClientboundSetTitlesPacketAccessor.getType() != null) {
                // 1.8.8 - 1.16.5
                var times = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor0(), (int) title.fadeIn().toMillis() / 50, (int) title.stay().toMillis() / 50, (int) title.fadeOut().toMillis() / 50);
                ClassStorage.sendNMSConstructedPacket(commandSender(), times);

                var titleP = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor1(), ClientboundSetTitlesPacket_i_TypeAccessor.getFieldTITLE(), t);
                ClassStorage.sendNMSConstructedPacket(commandSender(), titleP);

                var subtitleP = Reflect.construct(ClientboundSetTitlesPacketAccessor.getConstructor1(), ClientboundSetTitlesPacket_i_TypeAccessor.getFieldSUBTITLE(), s);
                ClassStorage.sendNMSConstructedPacket(commandSender(), subtitleP);
                return;
            }
        } catch (Throwable ignored) { // will it ever crash? our reflect library is kinda safe
        }
        // Hello Glowstone ;)
        if (BukkitFeature.VERBOSE_TITLE_METHOD.isSupported()) {
            commandSender().sendTitle(title.title().toLegacy(), title.subtitle().toLegacy(), (int) (title.fadeIn().toMillis() / 50), (int) (title.stay().toMillis() / 50), (int) (title.fadeOut().toMillis() / 50));
            return;
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
        if (bossBar instanceof BukkitBossBar1_8) {
            ((BukkitBossBar1_8) bossBar).addViewer(commandSender());
        } else {
            var bukkitBoss = bossBar.as(org.bukkit.boss.BossBar.class);
            bukkitBoss.addPlayer(commandSender());
            if (!bukkitBoss.isVisible()) {
                bukkitBoss.setVisible(true);
            }
        }
    }

    @Override
    public void hideBossBar(@NotNull BossBar bossBar) {
        if (bossBar instanceof BukkitBossBar1_8) {
            ((BukkitBossBar1_8) bossBar).removeViewer(commandSender());
        } else {
            bossBar.as(org.bukkit.boss.BossBar.class).removePlayer(commandSender());
        }
    }

    @Override
    public void playSound(@NotNull SoundStart sound) {
        var s = sound.soundKey().asString();
        if (!BukkitFeature.FLATTENING.isSupported()) {
            if (s.startsWith("minecraft:")) {
                s = s.substring(10);
            } // what to do with custom namespaces?
        }
        if (BukkitFeature.SOUND_CATEGORY.isSupported()) {
            // TODO: 1.19: add seed if implemented by Spigot API
            commandSender().playSound(commandSender().getLocation(), s, sound.source().as(SoundCategory.class), sound.volume(), sound.pitch());
        } else {
            commandSender().playSound(commandSender().getLocation(), s, sound.volume(), sound.pitch());
        }
    }

    @Override
    public void playSound(@NotNull SoundStart sound, double x, double y, double z) {
        var s = sound.soundKey().asString();
        if (!BukkitFeature.FLATTENING.isSupported()) {
            if (s.startsWith("minecraft:")) {
                s = s.substring(10);
            } // what to do with custom namespaces?
        }
        var location = new Location(commandSender().getWorld(), x, y, z); // I'm not sure if these locations are absolute or relative
        if (BukkitFeature.SOUND_CATEGORY.isSupported()) {
            // TODO: 1.19: add seed if implemented by Spigot API
            commandSender().playSound(location, s, sound.source().as(SoundCategory.class), sound.volume(), sound.pitch());
        } else {
            commandSender().playSound(location, s, sound.volume(), sound.pitch());
        }
    }

    @Override
    public void stopSound(@NotNull SoundStop sound) {
        if (BukkitFeature.STOP_SOUND.isSupported()) {
            var key = sound.soundKey();
            var source = sound.source();
            if (key != null && source != null) {
                var s = key.asString();
                if (!BukkitFeature.FLATTENING.isSupported()) {
                    if (s.startsWith("minecraft:")) {
                        s = s.substring(10);
                    } // what to do with custom namespaces?
                }

                if (BukkitFeature.SOUND_CATEGORY.isSupported()) {
                    commandSender().stopSound(s, sound.source().as(SoundCategory.class));
                } else {
                    commandSender().stopSound(s); // fallback to normal sound stop if SoundCategory doesn't work
                }
            } else if (key != null) {
                var s = key.asString();
                if (!BukkitFeature.FLATTENING.isSupported()) {
                    if (s.startsWith("minecraft:")) {
                        s = s.substring(10);
                    } // what to do with custom namespaces?
                }

                commandSender().stopSound(s);
            } else if (source != null) {
                // looks like we don't have method for this one :(
            } else if (BukkitFeature.STOP_ALL_SOUNDS.isSupported()) {
                commandSender().stopAllSounds();
            }
        } else {
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
        var item = ItemStackFactory.builder()
                .type(ItemType.of("minecraft:written_book"))
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
            if (BukkitFeature.MODERN_OPEN_BOOK_PACKET.isSupported()) {
                var packet = Reflect.construct(ClientboundOpenBookPacketAccessor.getConstructor0(), InteractionHandAccessor.getFieldMAIN_HAND());
                ClassStorage.sendNMSConstructedPacket(player, packet);
            } else if (BukkitFeature.MODERN_OPEN_BOOK_PLUGIN_MESSAGE.isSupported()) {
                var bytebuf = Reflect.construct(FriendlyByteBufAccessor.getConstructor0(), Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1));
                var location = Reflect.construct(ResourceLocationAccessor.getConstructor0(), "minecraft:book_open");
                var packet = Reflect.construct(ClientboundCustomPayloadPacketAccessor.getConstructor1(), location, bytebuf);
                ClassStorage.sendNMSConstructedPacket(player, packet);
            } else {
                var bytebuf = Reflect.construct(FriendlyByteBufAccessor.getConstructor0(), Unpooled.buffer(256).setByte(0, (byte) 0).writerIndex(1));
                var packet = Reflect.construct(ClientboundCustomPayloadPacketAccessor.getConstructor0(), "MC|BOpen", bytebuf);
                ClassStorage.sendNMSConstructedPacket(player, packet);
            }
        } catch (Throwable ignored) {
        } finally {
            player.setItemInHand(itemInHand);
        }
    }
}
