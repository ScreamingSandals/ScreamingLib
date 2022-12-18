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

package org.screamingsandals.lib.bukkit.entity;

import com.viaversion.viaversion.api.Via;
import io.netty.channel.Channel;
import lombok.experimental.ExtensionMethod;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.bukkit.BukkitCore;
import org.screamingsandals.lib.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.nms.accessors.ConnectionAccessor;
import org.screamingsandals.lib.nms.accessors.ServerGamePacketListenerImplAccessor;
import org.screamingsandals.lib.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.utils.extensions.NullableExtension;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.weather.WeatherHolder;
import protocolsupport.api.ProtocolSupportAPI;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@ExtensionMethod(value = {NullableExtension.class}, suppressBaseMethods = false)
public class BukkitEntityPlayer extends BukkitEntityHuman implements PlayerWrapper {
    public BukkitEntityPlayer(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean isSprinting() {
        return ((Player) wrappedObject).isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        ((Player) wrappedObject).setSprinting(sprinting);
    }

    @Override
    public boolean isFlying() {
        return ((Player) wrappedObject).isFlying();
    }

    @Override
    public void setFlying(boolean flying) {
        ((Player) wrappedObject).setFlying(flying);
    }

    @Override
    public boolean isAllowFlight() {
        return ((Player) wrappedObject).getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean flying) {
        ((Player) wrappedObject).setAllowFlight(flying);
    }

    @Override
    public boolean isSneaking() {
        return ((Player) wrappedObject).isSneaking();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        ((Player) wrappedObject).setSneaking(sneaking);
    }

    @Override
    public int getPing() {
        final Object handle = ClassStorage.getHandle(wrappedObject);
        return (int) Objects.requireNonNullElse(Reflect.getField(handle, ServerPlayerAccessor.getFieldLatency()), 0);
    }

    @Override
    @Nullable
    public Component getPlayerListName() {
        var bukkitPlayer = (Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(bukkitPlayer.playerListName());
        } else {
            return Component.fromLegacy(bukkitPlayer.getPlayerListName());
        }
    }

    public
    @Override void setPlayerListName(@Nullable Component component) {
        var bukkitPlayer = (Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            bukkitPlayer.playerListName(component == null ? null : component.as(net.kyori.adventure.text.Component.class));
        } else {
            bukkitPlayer.setPlayerListName(component == null ? null : component.toLegacy());
        }
    }

    @Override
    public void setPlayerListName(@Nullable ComponentLike component) {
        setPlayerListName(component != null ? component.asComponent() : null);
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        var bukkitPlayer = (Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(bukkitPlayer.displayName());
        } else {
            return Component.fromLegacy(bukkitPlayer.getDisplayName());
        }
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        var bukkitPlayer = (Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            bukkitPlayer.displayName(component == null ? null : component.as(net.kyori.adventure.text.Component.class));
        } else {
            bukkitPlayer.setDisplayName(component == null ? null : component.toLegacy());
        }
    }

    @Override
    public void setDisplayName(@Nullable ComponentLike component) {
        setDisplayName(component != null ? component.asComponent() : null);
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public void tryToDispatchCommand(String command) {
        Bukkit.dispatchCommand(wrappedObject, command);
    }

    @Override
    public Locale getLocale() {
        var bukkitPlayer = (Player) wrappedObject;
        var locale = Locale.US;
        if (Reflect.hasMethod(bukkitPlayer, "getLocale")) {
            var locale2 = bukkitPlayer.getLocale();
            try {
                locale = Locale.forLanguageTag(locale2);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return locale;
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public @NotNull Container getEnderChest() {
        return ContainerFactory.<Container>wrapContainer(((Player) wrappedObject).getEnderChest()).orElseThrow();
    }

    @Override
    public @NotNull PlayerContainer getPlayerInventory() {
        return ContainerFactory.<PlayerContainer>wrapContainer(((Player) wrappedObject).getInventory()).orElseThrow();
    }

    @Override
    public @Nullable Container getOpenedInventory() {
        return ContainerFactory.wrapContainer(((Player) wrappedObject).getOpenInventory().getTopInventory());
    }

    @Override
    public void openInventory(Openable container) {
        container.openInventory(this);
    }

    @Override
    public void closeInventory() {
        ((Player) wrappedObject).closeInventory();
    }

    @Override
    public void kick(Component message) {
        var bukkitPlayer = ((Player) wrappedObject);
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            bukkitPlayer.kick(message == null ? null : message.as(net.kyori.adventure.text.Component.class));
        } else {
            bukkitPlayer.kickPlayer(message == null ? null : message.toLegacy());
        }
    }

    @Override
    public void kick(ComponentLike message) {
        kick(message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(this) : message.asComponent());
    }

    @Override
    public GameModeHolder getGameMode() {
        return GameModeHolder.of(((Player) wrappedObject).getGameMode());
    }

    @Override
    public void setGameMode(@NotNull GameModeHolder gameMode) {
        ((Player) wrappedObject).setGameMode(gameMode.as(GameMode.class));
    }

    @Override
    public int getLevel() {
        return ((Player) wrappedObject).getLevel();
    }

    @Override
    public float getExp() {
        return ((Player) wrappedObject).getExp();
    }

    @Override
    public void setLevel(int level) {
        ((Player) wrappedObject).setLevel(level);
    }

    @Override
    public void setExp(float exp) {
        ((Player) wrappedObject).setExp(exp);
    }

    @Override
    public void forceUpdateInventory() {
        ((Player) wrappedObject).updateInventory();
    }

    @Override
    public @Nullable String getLastName() {
        return getName();
    }

    @Override
    public @Nullable WeatherHolder getPlayerWeather() {
        return WeatherHolder.ofNullable(((Player) wrappedObject).getPlayerWeather());
    }

    @Override
    public void setPlayerWeather(@Nullable WeatherHolder weather) {
        if (weather == null) {
            ((Player) wrappedObject).resetPlayerWeather();
        } else {
            ((Player) wrappedObject).setPlayerWeather(weather.as(WeatherType.class));
        }
    }

    @Override
    public long getPlayerTime() {
        return ((Player) wrappedObject).getPlayerTime();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        ((Player) wrappedObject).setPlayerTime(time, relative);
    }

    @Override
    public void resetPlayerTime() {
        ((Player) wrappedObject).resetPlayerTime();
    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {
        try {
            // 1.9.+
            ((Player) wrappedObject).spawnParticle(
                    particle.particleType().as(Particle.class),
                    location.as(Location.class),
                    particle.count(),
                    particle.offset().getX(),
                    particle.offset().getY(),
                    particle.offset().getZ(),
                    particle.particleData(),
                    particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null
                    // hey bukkit api, where's the last argument?
            );
        } catch (Throwable ignored) {
            // 1.8.8
            // TODO: implement for 1.8.8
        }
    }

    @Override
    public void setCompassTarget(LocationHolder location) {
        ((Player) wrappedObject).setCompassTarget(location.as(Location.class));
    }

    @Override
    public void restoreDefaultScoreboard() {
        ((Player) wrappedObject).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public @Nullable EntityBasic getSpectatorTarget() {
        var target = ((Player) wrappedObject).getSpectatorTarget();
        if (target == null) {
            return null;
        }
        return EntityMapper.wrapEntity(target);
    }

    @Override
    public void setSpectatorTarget(@Nullable EntityBasic entity) {
        ((Player) wrappedObject).setSpectatorTarget(entity == null ? null : entity.as(Entity.class));
    }

    @Override
    public int getTotalExperience() {
        return ((Player) wrappedObject).getTotalExperience();
    }

    @Override
    public void setTotalExperience(int exp) {
        ((Player) wrappedObject).setTotalExperience(exp);
    }

    @Override
    public void hidePlayer(PlayerWrapper player) {
        try {
            ((Player) wrappedObject).hidePlayer(BukkitCore.getPlugin(), player.as(Player.class));
        } catch (Throwable ignored) {
            ((Player) wrappedObject).hidePlayer(player.as(Player.class));
        }
    }

    @Override
    public void showPlayer(PlayerWrapper player) {
        try {
            ((Player) wrappedObject).showPlayer(BukkitCore.getPlugin(), player.as(Player.class));
        } catch (Throwable ignored) {
            ((Player) wrappedObject).showPlayer(player.as(Player.class));
        }
    }

    @Override
    public LocationHolder getCompassTarget() {
        return LocationMapper.wrapLocation(((Player) wrappedObject).getCompassTarget());
    }

    @Override
    public Channel getChannel() {
        return (Channel) Reflect.getFieldResulted(ClassStorage.getHandle(wrappedObject), ServerPlayerAccessor.getFieldConnection())
                .getFieldResulted(ServerGamePacketListenerImplAccessor.getFieldConnection())
                .getFieldResulted(ConnectionAccessor.getFieldChannel())
                .raw();
    }

    @Override
    public InetSocketAddress getAddress() {
        return ((Player) wrappedObject).getAddress();
    }

    @SuppressWarnings("unchecked") // Via Version
    @Override
    public int getProtocolVersion() {
        if (Reflect.has("com.viaversion.viaversion.api.Via")) {
            return Via.getAPI().getPlayerVersion(wrappedObject);
        }
        if (Reflect.has("protocolsupport.api.ProtocolSupportAPI")) {
            return ProtocolSupportAPI.getProtocolVersion((Player) wrappedObject).getId();
        }
        return Server.getProtocolVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (UnsupportedOperationException ignored) {
            return PlayerMapper.UNSAFE_getPlayerConverter().convert(this, type);
        }
    }

    @Override
    @NotNull
    public PlayerAdapter adapter() {
        return BukkitCore.getSpectatorBackend().adapter(this, wrappedObject);
    }
}
