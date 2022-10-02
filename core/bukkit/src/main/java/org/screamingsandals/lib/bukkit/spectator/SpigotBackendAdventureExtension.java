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
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.adventure.spectator.audience.adapter.AdventureAdapter;
import org.screamingsandals.lib.adventure.spectator.audience.adapter.AdventureConsoleAdapter;
import org.screamingsandals.lib.adventure.spectator.audience.adapter.AdventurePlayerAdapter;
import org.screamingsandals.lib.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.spectator.SpectatorBackend;
import org.screamingsandals.lib.spectator.audience.ConsoleAudience;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;

// let's trick the bukkit's class loader a little
class SpigotBackendAdventureExtension {
    static SpectatorBackend initAdventureBackend(@NotNull SNBTSerializer snbtSerializerLocal) {
        var adventureBackend = new AdventureBackend() {
            {
                snbtSerializer = snbtSerializerLocal;
            }
        };

        // TODO: Fix for upcoming paper versions
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

        AdventureBackend.getAdditionalColorConverter()
                .registerW2P(ChatColor.class, adventureColor -> ChatColor.of(adventureColor.toString()));

        AdventureBackend.getAdditionalClickEventConverter()
                .registerW2P(net.md_5.bungee.api.chat.ClickEvent.class, adventureClickEvent -> {
                    net.md_5.bungee.api.chat.ClickEvent.Action bungeeAction;
                    try {
                        bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(adventureClickEvent.action().name());
                    } catch (Throwable throwable) {
                        bungeeAction = net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL;
                    }
                    return new net.md_5.bungee.api.chat.ClickEvent(bungeeAction, adventureClickEvent.value());
                });

        AdventureBackend.getAdditionalHoverEventConverter()
                .registerW2P(net.md_5.bungee.api.chat.HoverEvent.class, adventureHoverEvent ->
                        new net.md_5.bungee.api.chat.HoverEvent(
                                net.md_5.bungee.api.chat.HoverEvent.Action.valueOf(adventureHoverEvent.action().name()),
                                adventureHoverEvent.content().as(Content.class)
                        )
                );

        AdventureBackend.getAdditionalItemContentConverter()
                .registerW2P(Item.class, adventureItemContent -> {
                    var nbt = adventureItemContent.tag();
                    return new Item(adventureItemContent.id().asString(), adventureItemContent.count(), nbt != null ? ItemTag.ofNbt(snbtSerializerLocal.serialize(nbt)) : null);
                });

        AdventureBackend.getAdditionalEntityContentConverter()
                .registerW2P(Entity.class, adventureEntityContent -> {
                    var name = adventureEntityContent.name();
                    return new Entity(adventureEntityContent.type().asString(), adventureEntityContent.id().toString(), name != null ? name.as(BaseComponent.class) : null);
                });

        AbstractBungeeBackend.getAdditionalComponentConverter()
                .registerW2P(
                        net.kyori.adventure.text.Component.class,
                        component -> gson.deserialize(ComponentSerializer.toString(component.as(BaseComponent.class)))
                );

        AbstractBungeeBackend.getAdditionalColorConverter()
                .registerW2P(TextColor.class, bungeeColor -> TextColor.color(bungeeColor.red(), bungeeColor.green(), bungeeColor.blue()));

        AbstractBungeeBackend.getAdditionalClickEventConverter()
                .registerW2P(net.kyori.adventure.text.event.ClickEvent.class, bungeeClickEvent ->
                        net.kyori.adventure.text.event.ClickEvent.clickEvent(
                                net.kyori.adventure.text.event.ClickEvent.Action.valueOf(bungeeClickEvent.action().name()),
                                bungeeClickEvent.value()
                        )
                );

        AbstractBungeeBackend.getAdditionalHoverEventConverter()
                .registerW2P(net.kyori.adventure.text.event.HoverEvent.class, bungeeHoverEvent -> {
                    switch (bungeeHoverEvent.action()) {
                        case SHOW_ENTITY:
                            return net.kyori.adventure.text.event.HoverEvent.showEntity(bungeeHoverEvent.content().as(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class));
                        case SHOW_ITEM:
                            return net.kyori.adventure.text.event.HoverEvent.showItem(bungeeHoverEvent.content().as(net.kyori.adventure.text.event.HoverEvent.ShowItem.class));
                        default:
                            return net.kyori.adventure.text.event.HoverEvent.showText(bungeeHoverEvent.content().as(net.kyori.adventure.text.Component.class));
                    }
                });

        AbstractBungeeBackend.getAdditionalItemContentConverter()
                .registerW2P(net.kyori.adventure.text.event.HoverEvent.ShowItem.class, bungeeItemContent -> {
                    //noinspection PatternValidation
                    var id = bungeeItemContent.id();
                    var tag = bungeeItemContent.tag();
                    BinaryTagHolder value = null;
                    if (tag != null) {
                        try {
                            value = BinaryTagHolder.binaryTagHolder(snbtSerializerLocal.serialize(tag));
                        } catch (Throwable ignored) {
                            // Old Adventure
                            value = BinaryTagHolder.of(snbtSerializerLocal.serialize(tag));
                        }
                    }
                    //noinspection PatternValidation
                    return net.kyori.adventure.text.event.HoverEvent.ShowItem.of(
                            Key.key(id.namespace(), id.value()),
                            bungeeItemContent.count(),
                            value
                    ) ;
                });

        AbstractBungeeBackend.getAdditionalEntityContentConverter()
                .registerW2P(net.kyori.adventure.text.event.HoverEvent.ShowEntity.class, bungeeEntityContent -> {
                    //noinspection PatternValidation
                    var type = bungeeEntityContent.type();
                    var name = bungeeEntityContent.name();
                    //noinspection PatternValidation
                    return net.kyori.adventure.text.event.HoverEvent.ShowEntity.of(
                            Key.key(type.namespace(), type.value()),
                            bungeeEntityContent.id(),
                            name != null ? name.as(net.kyori.adventure.text.Component.class) : null
                    );
                });

        return adventureBackend;
    }

    static <A extends Adapter> A adapter(CommandSenderWrapper wrapper, CommandSender sender) {
        if (sender instanceof Player && wrapper instanceof PlayerAudience) {
            return (A) new AdventurePlayerAdapter(sender, (PlayerAudience) wrapper);
        } else if (sender instanceof ConsoleCommandSender && wrapper instanceof ConsoleAudience) {
            return (A) new AdventureConsoleAdapter(sender, (ConsoleAudience) wrapper);
        } else {
            return (A) new AdventureAdapter(sender, wrapper);
        }
    }
}
