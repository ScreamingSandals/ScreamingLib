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

package org.screamingsandals.lib.signs;

import lombok.experimental.ExtensionMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.PlayerInteractEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.event.player.PlayerBlockBreakEvent;
import org.screamingsandals.lib.event.player.PlayerUpdateSignEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.block.snapshot.SignBlockSnapshot;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ServiceDependencies
@ExtensionMethod(value = NullableExtension.class, suppressBaseMethods = false)
public abstract class AbstractSignManager {
    private final @NotNull List<@NotNull ClickableSign> signs = new LinkedList<>();
    private boolean modified;

    @OnEnable
    public void enable() {
        try {
            var config = getLoader().load();

            config.node("sign").childrenList().forEach(sign -> {
                var name = sign.node("name").getString();
                if (name == null || name.isBlank()) {
                    name = sign.node("game").getString("invalid"); // Compatibility with old BedWars sign.yml
                }
                try {
                    var loc = sign.node("location").get(SignLocation.class);
                    var clickableSign = new ClickableSign(Objects.requireNonNull(loc), name);
                    signs.add(clickableSign);
                    updateSign(clickableSign);
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            });
        } catch (ConfigurateException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isSignRegistered(@NotNull SignLocation location) {
        return signs.stream()
                .anyMatch(sign -> sign.getLocation().equals(location));
    }

    public @Nullable ClickableSign getSign(@NotNull SignLocation location) {
        return signs.stream()
                .filter(sign -> sign.getLocation().equals(location))
                .findFirst()
                .toNullable();
    }

    public @NotNull List<@NotNull ClickableSign> getSignsForKey(@NotNull String key) {
        return signs.stream()
                .filter(sign -> sign.getKey().equals(key))
                .collect(Collectors.toList());
    }

    public void unregisterSign(@NotNull SignLocation location) {
        signs.stream()
                .filter(sign -> sign.getLocation().equals(location))
                .findFirst()
                .ifPresent(sign -> {
                    signs.remove(sign);
                    modified = true;
                });
    }

    public boolean registerSign(@NotNull SignLocation location, @NotNull Component key) {
        var normalizedKey = normalizeKey(key);
        if (normalizedKey != null) {
            var sign = new ClickableSign(location, normalizedKey);
            signs.add(sign);
            modified = true;
            updateSign(sign);
            return true;
        }
        return false;
    }

    @OnPreDisable
    public void save() {
        save(false);
    }

    public void save(boolean force) {
        if (modified || force) {
            var config = getLoader().createNode();

            signs.forEach(sign -> {
                try {
                    var signNode = config.node("sign").appendListNode();
                    signNode.node("location").set(sign.getLocation());
                    signNode.node("name").set(sign.getKey());
                } catch (SerializationException e) {
                    e.printStackTrace();
                }
            });

            try {
                getLoader().save(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnEvent
    public void onRightClick(@NotNull PlayerInteractEvent event) {
        if (event.action() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                && event.clickedBlock() != null) {
            var state = event.clickedBlock().blockSnapshot().orElseThrow();
            if (state instanceof SignBlockSnapshot) {
                var location = new SignLocation(state.location());
                var sign = getSign(location);
                if (sign != null) {
                    if (!isAllowedToUse(event.player())) {
                        return;
                    }

                    onClick(event.player(), sign);
                }
            }
        }
    }

    @OnEvent
    public void onBreak(@NotNull PlayerBlockBreakEvent event) {
        if (event.cancelled()) {
            return;
        }

        var player = event.player();
        var state = event.block().blockSnapshot().orElseThrow();
        if (state instanceof SignBlockSnapshot) {
            var location = new SignLocation(state.location());
            if (isSignRegistered(location)) {
                if (isAllowedToEdit(player)) {
                    unregisterSign(location);
                } else {
                    player.sendMessage(signCannotBeDestroyedMessage(player));
                    event.cancelled(true);
                }
            }
        }
    }

    @OnEvent
    public void onEdit(@NotNull PlayerUpdateSignEvent event) {
        if (event.cancelled()) {
            return;
        }

        var player = event.player();
        if (isAllowedToEdit(player) && isFirstLineValid(event.line(0))) {
            if (registerSign(new SignLocation(event.block().location()), event.line(1))) {
                player.sendMessage(signCreatedMessage(player));
            } else {
                player.sendMessage(signCannotBeCreatedMessage(player));
                event.cancelled(true);
                event.block().breakNaturally();
            }
        }
    }

    protected abstract boolean isAllowedToUse(@NotNull Player player);

    protected abstract boolean isAllowedToEdit(@NotNull Player player);

    protected abstract @Nullable String normalizeKey(@NotNull Component key);

    protected abstract void updateSign(@NotNull ClickableSign sign);

    protected abstract void onClick(@NotNull Player player, @NotNull ClickableSign sign);

    protected abstract boolean isFirstLineValid(@NotNull Component firstLine);

    protected abstract @NotNull Component signCreatedMessage(@NotNull Player player);

    protected abstract @NotNull Component signCannotBeCreatedMessage(@NotNull Player player);

    protected abstract @NotNull Component signCannotBeDestroyedMessage(@NotNull Player player);

    protected abstract @NotNull ConfigurationLoader getLoader();
}
