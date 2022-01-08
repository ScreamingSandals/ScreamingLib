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

package org.screamingsandals.lib.sender;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.AdventureHelper;

public interface SForwardingAudience extends ForwardingAudience {
    @ApiStatus.OverrideOnly
    @NotNull
    Iterable<CommandSenderWrapper> audiences();

    default void sendMessage(String message) {
        sendMessage(AdventureHelper.toComponent(message));
    }

    default void sendMessage(SenderMessage senderMessage) {
        audiences().forEach(senderWrapper ->
            sendMessage(senderMessage.asComponent(senderWrapper))
        );
    }

    default void sendActionBar(SenderMessage senderMessage) {
        audiences().forEach(senderWrapper ->
                sendActionBar(senderMessage.asComponent(senderWrapper))
        );
    }

    default void sendPlayerListHeader(SenderMessage senderMessage) {
        audiences().forEach(senderWrapper ->
                sendPlayerListHeader(senderMessage.asComponent(senderWrapper))
        );
    }

    default void sendPlayerListFooter(SenderMessage senderMessage) {
        audiences().forEach(senderWrapper ->
                sendPlayerListFooter(senderMessage.asComponent(senderWrapper))
        );
    }

    default void sendPlayerListHeaderAndFooter(SenderMessage header, SenderMessage footer) {
        audiences().forEach(senderWrapper ->
                sendPlayerListHeaderAndFooter(header.asComponent(senderWrapper), footer.asComponent(senderWrapper))
        );
    }

    default void showTitle(TitleableSenderMessage title) {
        audiences().forEach(senderWrapper -> showTitle(title.asTitle(senderWrapper)));
    }

    default void showTitle(TitleableSenderMessage title, Title.Times times) {
        audiences().forEach(senderWrapper -> showTitle(title.asTitle(senderWrapper, times)));
    }
}
