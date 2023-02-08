/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.velocity.proxy.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.event.NoAutoCancellable;
import org.screamingsandals.lib.proxy.PendingConnection;
import org.screamingsandals.lib.proxy.event.SPlayerLoginEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Optional;

public class VelocityPlayerLoginEvent extends BasicWrapper<LoginEvent> implements SPlayerLoginEvent, NoAutoCancellable {
    protected VelocityPlayerLoginEvent(@NotNull LoginEvent wrappedObject) {
        super(wrappedObject);
    }

    private @Nullable PendingConnection player;
    private @Nullable net.kyori.adventure.text.Component componentForCancelling;

    @Override
    public @NotNull PendingConnection getPlayer() {
        if (player == null) {
            var pending = wrappedObject.getPlayer();
            player = new PendingConnection(
                    pending.getUsername(),
                    pending.getProtocolVersion().getProtocol(),
                    pending.getRemoteAddress(),
                    false,
                    pending.getUniqueId(),
                    pending.isOnlineMode()
            );
        }
        return player;
    }

    @Override
    public @NotNull Component getCancelMessage() {
        return wrappedObject.getResult().getReasonComponent().or(() -> Optional.ofNullable(componentForCancelling)).map(AdventureBackend::wrapComponent).orElse(Component.empty());
    }

    @Override
    public void setCancelMessage(@NotNull Component cancelMessage) {
        if (!wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(ResultedEvent.ComponentResult.denied(cancelMessage.as(net.kyori.adventure.text.Component.class)));
        } else {
            componentForCancelling = cancelMessage.as(net.kyori.adventure.text.Component.class);
        }
    }

    @Override
    public boolean cancelled() {
        return !wrappedObject.getResult().isAllowed();
    }

    @Override
    public void cancelled(boolean cancel) {
        if (cancel && wrappedObject.getResult().isAllowed()) {
            wrappedObject.setResult(ResultedEvent.ComponentResult.denied(componentForCancelling != null ? componentForCancelling : net.kyori.adventure.text.Component.empty()));
        } else if (!cancel && !wrappedObject.getResult().isAllowed()) {
            componentForCancelling = wrappedObject.getResult().getReasonComponent().orElse(null);
            wrappedObject.setResult(ResultedEvent.ComponentResult.allowed());
        }
    }
}
