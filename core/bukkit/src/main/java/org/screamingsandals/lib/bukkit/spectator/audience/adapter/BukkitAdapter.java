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

import lombok.Data;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

@Data
@Accessors(fluent = true)
public class BukkitAdapter implements Adapter {
    private final Audience owner;
    private final CommandSender commandSender;

    @Override
    public void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner) : message.asComponent();
        if (Reflect.hasMethod(owner, "spigot")) {
            if (source != null && Reflect.hasMethod(commandSender.spigot(), "sendMessage", UUID.class, BaseComponent.class)) {
                commandSender.spigot().sendMessage(source, comp.as(BaseComponent.class));
            } else {
                commandSender.spigot().sendMessage(comp.as(BaseComponent.class));
            }
        } else {
            commandSender.sendMessage(comp.toLegacy());
        }
    }
}
