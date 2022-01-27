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

package org.screamingsandals.lib.adventure.spectator.audience;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.UUID;

public class AdventureAudience extends BasicWrapper<net.kyori.adventure.audience.Audience> implements Audience {
    /*
     * ScreamingLib Audience should be CommandSenderWrapper
     */
    private final Audience.ForwardingSingle screamingLibAudience;

    protected AdventureAudience(net.kyori.adventure.audience.Audience wrappedObject, Audience.ForwardingSingle screamingLibAudience) {
        super(wrappedObject);
        this.screamingLibAudience = screamingLibAudience;
    }

    @Override
    public void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        wrappedObject.sendMessage(
                source == null ? Identity.nil() : Identity.identity(source),
                resolveComponent(message),
                messageType == MessageType.CHAT ? net.kyori.adventure.audience.MessageType.CHAT : net.kyori.adventure.audience.MessageType.SYSTEM
        );
    }

    protected Component resolveComponent(ComponentLike message) {
        var component = message instanceof AudienceComponentLike
                ? ((AudienceComponentLike) message).asComponent(screamingLibAudience != null ? screamingLibAudience : this)
                : message.asComponent();
        return component.as(Component.class);
    }
}
