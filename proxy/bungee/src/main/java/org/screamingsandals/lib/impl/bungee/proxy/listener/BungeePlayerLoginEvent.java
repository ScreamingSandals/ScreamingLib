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

package org.screamingsandals.lib.impl.bungee.proxy.listener;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.AbstractBungeeBackend;
import org.screamingsandals.lib.proxy.PendingConnection;
import org.screamingsandals.lib.proxy.event.PlayerLoginEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeePlayerLoginEvent extends BasicWrapper<LoginEvent> implements PlayerLoginEvent {
    protected BungeePlayerLoginEvent(@NotNull LoginEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable PendingConnection player;


    @Override
    public @NotNull PendingConnection getPlayer() {
        if (player == null) {
            var pendingConnection = wrappedObject.getConnection();
            player = new PendingConnection(
                    pendingConnection.getName(),
                    pendingConnection.getVersion(),
                    pendingConnection.getVirtualHost(),
                    pendingConnection.isLegacy(),
                    pendingConnection.getUniqueId(),
                    pendingConnection.isOnlineMode()
            );
        }
        return player;
    }

    @Override
    public @NotNull Component getCancelMessage() {
        var cancelMessage = wrappedObject.getCancelReasonComponents();
        if (cancelMessage == null || cancelMessage.length == 0) {
            return Component.empty();
        }

        return AbstractBungeeBackend.wrapComponent(new TextComponent(cancelMessage));
    }

    @Override
    public void setCancelMessage(@NotNull Component cancelMessage) {
        wrappedObject.setCancelReason(cancelMessage.as(BaseComponent.class));
    }

    @Override
    public boolean cancelled() {
        return wrappedObject.isCancelled();
    }

    @Override
    public void cancelled(boolean cancel) {
        wrappedObject.setCancelled(cancel);
    }
}
