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

package org.screamingsandals.lib.impl.bungee.spectator.audience.adapter;

import lombok.Data;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;

@Data
@Accessors(fluent = true)
public class BungeeAdapter implements Adapter {
    private final @NotNull CommandSender sender;
    private final @NotNull Audience owner;

    @Override
    public void sendMessage(@NotNull ComponentLike message) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner) : message.asComponent();
        sender.sendMessage(comp.as(BaseComponent.class));
    }
}
