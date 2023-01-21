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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerChatEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerChatEvent implements SPlayerChatEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull AsyncPlayerChatEvent event;

    // Internal cache
    private @Nullable Collection<@NotNull PlayerWrapper> recipients;
    private @Nullable PlayerWrapper sender;

    @Override
    public @NotNull Collection<@NotNull PlayerWrapper> recipients() {
        if (recipients == null) {
            recipients = new CollectionLinkedToCollection<>(event.getRecipients(), playerWrapper -> playerWrapper.as(Player.class), BukkitEntityPlayer::new);
        }
        return recipients;
    }

    @Override
    public @NotNull PlayerWrapper sender() {
        if (sender == null) {
            sender = new BukkitEntityPlayer(event.getPlayer());
        }
        return sender;
    }

    @Override
    public @NotNull String message() {
        return event.getMessage();
    }

    @Override
    public void message(@NotNull String message) {
        event.setMessage(message);
    }

    @Override
    public @NotNull String format() {
        return event.getFormat();
    }

    @Override
    public void format(@NotNull String format) {
        event.setFormat(format);
    }
}
