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

package org.screamingsandals.lib.signs;

import lombok.experimental.ExtensionMethod;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.event.player.SPlayerBlockBreakEvent;
import org.screamingsandals.lib.event.player.SPlayerUpdateSignEvent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;
import org.screamingsandals.lib.block.state.SignHolder;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ServiceDependencies
@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public abstract class AbstractSignManager {
    private final List<ClickableSign> signs = new LinkedList<>();
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
                    var clickableSign = new ClickableSign(loc, name);
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

    public boolean isSignRegistered(SignLocation location) {
        return signs.stream()
                .anyMatch(sign -> sign.getLocation().equals(location));
    }

    public Optional<ClickableSign> getSign(SignLocation location) {
        return signs.stream()
                .filter(sign -> sign.getLocation().equals(location))
                .findFirst();
    }

    public List<ClickableSign> getSignsForKey(String key) {
        return signs.stream()
                .filter(sign -> sign.getKey().equals(key))
                .collect(Collectors.toList());
    }

    public void unregisterSign(SignLocation location) {
        signs.stream()
                .filter(sign -> sign.getLocation().equals(location))
                .findFirst()
                .ifPresent(sign -> {
                    signs.remove(sign);
                    modified = true;
                });
    }

    public boolean registerSign(SignLocation location, Component key) {
        var normalizedKey = normalizeKey(key);
        if (normalizedKey.isPresent()) {
            var sign = new ClickableSign(location, normalizedKey.get());
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
    public void onRightClick(SPlayerInteractEvent event) {
        if (event.action() == SPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK
                && event.clickedBlock() != null) {
            var state = event.clickedBlock().<BlockStateHolder>getBlockState().orElseThrow();
            if (state instanceof SignHolder) {
                var location = new SignLocation(state.getLocation());
                var sign = getSign(location);
                if (sign.isPresent()) {
                    if (!isAllowedToUse(event.player())) {
                        return;
                    }

                    onClick(event.player(), sign.get());
                }
            }
        }
    }

    @OnEvent
    public void onBreak(SPlayerBlockBreakEvent event) {
        if (event.cancelled()) {
            return;
        }

        var player = event.player();
        var state = event.block().<BlockStateHolder>getBlockState().orElseThrow();
        if (state instanceof SignHolder) {
            var location = new SignLocation(state.getLocation());
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
    public void onEdit(SPlayerUpdateSignEvent event) {
        if (event.cancelled()) {
            return;
        }

        var player = event.player();
        if (isAllowedToEdit(player) && isFirstLineValid(event.line(0))) {
            if (registerSign(new SignLocation(event.block().getLocation()), event.line(1))) {
                player.sendMessage(signCreatedMessage(player));
            } else {
                player.sendMessage(signCannotBeCreatedMessage(player));
                event.cancelled(true);
                event.block().breakNaturally();
            }
        }
    }

    protected abstract boolean isAllowedToUse(PlayerWrapper player);

    protected abstract boolean isAllowedToEdit(PlayerWrapper player);

    protected abstract Optional<String> normalizeKey(Component key);

    protected abstract void updateSign(ClickableSign sign);

    protected abstract void onClick(PlayerWrapper player, ClickableSign sign);

    protected abstract boolean isFirstLineValid(Component firstLine);

    protected abstract Component signCreatedMessage(PlayerWrapper player);

    protected abstract Component signCannotBeCreatedMessage(PlayerWrapper player);

    protected abstract Component signCannotBeDestroyedMessage(PlayerWrapper player);

    protected abstract ConfigurationLoader getLoader();
}
