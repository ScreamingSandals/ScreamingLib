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

package org.screamingsandals.lib.impl.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.PlayerChatEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.impl.utils.collections.CollectionLinkedToCollection;

import java.util.Collection;

@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class BukkitPlayerChatEvent implements PlayerChatEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final @NotNull AsyncPlayerChatEvent event;

    // Internal cache
    private @Nullable Collection<@NotNull Player> recipients;
    private @Nullable Player sender;

    @Override
    public @NotNull Collection<@NotNull Player> recipients() {
        if (recipients == null) {
            recipients = new CollectionLinkedToCollection<>(event.getRecipients(), playerWrapper -> playerWrapper.as(org.bukkit.entity.Player.class), BukkitPlayer::new);
        }
        return recipients;
    }

    @Override
    public @NotNull Player sender() {
        if (sender == null) {
            sender = new BukkitPlayer(event.getPlayer());
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
