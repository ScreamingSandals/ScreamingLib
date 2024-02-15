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

package org.screamingsandals.lib.impl.bukkit.entity;

import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.bukkit.BukkitCore;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.impl.bukkit.particle.BukkitParticleConverter1_8;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.Entities;
import org.screamingsandals.lib.impl.nms.accessors.ClientboundLevelParticlesPacketAccessor;
import org.screamingsandals.lib.impl.nms.accessors.EnumParticleAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.particle.Particle;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.player.gamemode.GameMode;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.adapter.PlayerAdapter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.impl.world.Locations;
import org.screamingsandals.lib.world.weather.WeatherType;
import protocolsupport.api.ProtocolSupportAPI;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class BukkitPlayer extends BukkitHumanEntity implements Player {
    public BukkitPlayer(@NotNull org.bukkit.entity.Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean isSprinting() {
        return ((org.bukkit.entity.Player) wrappedObject).isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        ((org.bukkit.entity.Player) wrappedObject).setSprinting(sprinting);
    }

    @Override
    public boolean isFlying() {
        return ((org.bukkit.entity.Player) wrappedObject).isFlying();
    }

    @Override
    public void setFlying(boolean flying) {
        ((org.bukkit.entity.Player) wrappedObject).setFlying(flying);
    }

    @Override
    public boolean isAllowFlight() {
        return ((org.bukkit.entity.Player) wrappedObject).getAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean flying) {
        ((org.bukkit.entity.Player) wrappedObject).setAllowFlight(flying);
    }

    @SuppressWarnings("RedundantCast") // the cast here is not redundant (it is on modern paper, but that's it)
    @Override
    public boolean isSneaking() {
        return ((org.bukkit.entity.Player) wrappedObject).isSneaking();
    }

    @SuppressWarnings("RedundantCast") // the cast here is not redundant (it is on modern paper, but that's it)
    @Override
    public void setSneaking(boolean sneaking) {
        ((org.bukkit.entity.Player) wrappedObject).setSneaking(sneaking);
    }

    @Override
    public int getPing() {
        final Object handle = ClassStorage.getHandle(wrappedObject);
        return (int) Objects.requireNonNullElse(Reflect.getField(handle, ServerPlayerAccessor.FIELD_LATENCY.get()), 0);
    }

    @Override
    public @Nullable Component getPlayerListName() {
        var bukkitPlayer = (org.bukkit.entity.Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(bukkitPlayer.playerListName());
        } else {
            return Component.fromLegacy(bukkitPlayer.getPlayerListName());
        }
    }

    @Override
    public void setPlayerListName(@Nullable Component component) {
        var bukkitPlayer = (org.bukkit.entity.Player) wrappedObject;
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
    public @NotNull Component getDisplayName() {
        var bukkitPlayer = (org.bukkit.entity.Player) wrappedObject;
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            return AdventureBackend.wrapComponent(bukkitPlayer.displayName());
        } else {
            return Component.fromLegacy(bukkitPlayer.getDisplayName());
        }
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        var bukkitPlayer = (org.bukkit.entity.Player) wrappedObject;
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
    public @NotNull String getName() {
        return wrappedObject.getName();
    }

    @Override
    public void tryToDispatchCommand(@NotNull String command) {
        Bukkit.dispatchCommand(wrappedObject, command);
    }

    @Override
    public @NotNull Locale getLocale() {
        var bukkitPlayer = (org.bukkit.entity.Player) wrappedObject;
        var locale = Locale.US;
        if (BukkitFeature.PLAYER_GET_LOCALE.isSupported()) {
            var locale2 = bukkitPlayer.getLocale();
            try {
                locale = Locale.forLanguageTag(locale2);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return locale;
    }

    @Override
    public @NotNull UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public @NotNull Container getEnderChest() {
        return Preconditions.checkNotNull(ContainerFactory.<Container>wrapContainer(((org.bukkit.entity.Player) wrappedObject).getEnderChest()));
    }

    @Override
    public @NotNull PlayerContainer getPlayerInventory() {
        return Preconditions.checkNotNull(ContainerFactory.<PlayerContainer>wrapContainer(((org.bukkit.entity.Player) wrappedObject).getInventory()));
    }

    @Override
    public @Nullable Container getOpenedInventory() {
        return ContainerFactory.wrapContainer(((org.bukkit.entity.Player) wrappedObject).getOpenInventory().getTopInventory());
    }

    @Override
    public void openInventory(@NotNull Openable container) {
        container.openInventory(this);
    }

    @Override
    public void closeInventory() {
        ((org.bukkit.entity.Player) wrappedObject).closeInventory();
    }

    @Override
    public void kick(@Nullable Component message) {
        var bukkitPlayer = ((org.bukkit.entity.Player) wrappedObject);
        if (BukkitCore.getSpectatorBackend().hasAdventure()) {
            bukkitPlayer.kick(message == null ? null : message.as(net.kyori.adventure.text.Component.class));
        } else {
            bukkitPlayer.kickPlayer(message == null ? null : message.toLegacy());
        }
    }

    @Override
    public void kick(@Nullable ComponentLike message) {
        kick(message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(this) : (message != null ? message.asComponent() : null));
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return GameMode.of(((org.bukkit.entity.Player) wrappedObject).getGameMode());
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        ((org.bukkit.entity.Player) wrappedObject).setGameMode(gameMode.as(org.bukkit.GameMode.class));
    }

    @Override
    public int getLevel() {
        return ((org.bukkit.entity.Player) wrappedObject).getLevel();
    }

    @Override
    public float getExp() {
        return ((org.bukkit.entity.Player) wrappedObject).getExp();
    }

    @Override
    public void setLevel(int level) {
        ((org.bukkit.entity.Player) wrappedObject).setLevel(level);
    }

    @Override
    public void setExp(float exp) {
        ((org.bukkit.entity.Player) wrappedObject).setExp(exp);
    }

    @Override
    public void forceUpdateInventory() {
        ((org.bukkit.entity.Player) wrappedObject).updateInventory();
    }

    @Override
    public @Nullable String getLastName() {
        return getName();
    }

    @Override
    public @Nullable WeatherType getPlayerWeather() {
        return WeatherType.ofNullable(((org.bukkit.entity.Player) wrappedObject).getPlayerWeather());
    }

    @Override
    public void setPlayerWeather(@Nullable WeatherType weather) {
        if (weather == null) {
            ((org.bukkit.entity.Player) wrappedObject).resetPlayerWeather();
        } else {
            ((org.bukkit.entity.Player) wrappedObject).setPlayerWeather(weather.as(org.bukkit.WeatherType.class));
        }
    }

    @Override
    public long getPlayerTime() {
        return ((org.bukkit.entity.Player) wrappedObject).getPlayerTime();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        ((org.bukkit.entity.Player) wrappedObject).setPlayerTime(time, relative);
    }

    @Override
    public void resetPlayerTime() {
        ((org.bukkit.entity.Player) wrappedObject).resetPlayerTime();
    }

    @Override
    public void sendParticle(@NotNull Particle particle, @NotNull Location location) {
        if (!this.getLocation().getWorld().equals(location.getWorld())) {
            throw new IllegalArgumentException("The location of the sent particle is not in the correct world!");
        }

        if (BukkitFeature.PARTICLES_API.isSupported()) {
            // 1.9.+
            ((org.bukkit.entity.Player) wrappedObject).spawnParticle(
                    particle.particleType().as(org.bukkit.Particle.class),
                    location.as(org.bukkit.Location.class),
                    particle.count(),
                    particle.offset().getX(),
                    particle.offset().getY(),
                    particle.offset().getZ(),
                    particle.particleData(),
                    particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null
                    // hey bukkit api, where's the last argument? (the official implementation set this always to true when send to a specific player)
            );
        } else {
            // 1.8.8
            var enumParticle = particle.particleType().as(EnumParticleAccessor.TYPE.get());
            var packet = Reflect.construct(
                    ClientboundLevelParticlesPacketAccessor.CONSTRUCTOR_0.get(),
                    enumParticle,
                    particle.longDistance(),
                    (float) location.getX(),
                    (float) location.getY(),
                    (float) location.getZ(),
                    (float) particle.offset().getX(),
                    (float) particle.offset().getY(),
                    (float) particle.offset().getZ(),
                    (float) particle.particleData(),
                    particle.count(),
                    particle.specialData() != null ? BukkitParticleConverter1_8.convertParticleData(particle.specialData()) : new int[0]
            );
            ClassStorage.sendNMSConstructedPacket((org.bukkit.entity.Player) wrappedObject, packet);
        }
    }

    @Override
    public void setCompassTarget(@NotNull Location location) {
        ((org.bukkit.entity.Player) wrappedObject).setCompassTarget(location.as(org.bukkit.Location.class));
    }

    @Override
    public void restoreDefaultScoreboard() {
        ((org.bukkit.entity.Player) wrappedObject).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    @Override
    public @Nullable Entity getSpectatorTarget() {
        var target = ((org.bukkit.entity.Player) wrappedObject).getSpectatorTarget();
        if (target == null) {
            return null;
        }
        return Entities.wrapEntity(target);
    }

    @Override
    public void setSpectatorTarget(@Nullable Entity entity) {
        ((org.bukkit.entity.Player) wrappedObject).setSpectatorTarget(entity == null ? null : entity.as(org.bukkit.entity.Entity.class));
    }

    @Override
    public int getTotalExperience() {
        return ((org.bukkit.entity.Player) wrappedObject).getTotalExperience();
    }

    @Override
    public void setTotalExperience(int exp) {
        ((org.bukkit.entity.Player) wrappedObject).setTotalExperience(exp);
    }

    @Override
    public void hidePlayer(@NotNull Player player) {
        if (BukkitFeature.PLAYER_HIDE_API_PLUGIN_TICKET.isSupported()) {
            ((org.bukkit.entity.Player) wrappedObject).hidePlayer(BukkitCore.getPlugin(), player.as(org.bukkit.entity.Player.class));
        } else {
            ((org.bukkit.entity.Player) wrappedObject).hidePlayer(player.as(org.bukkit.entity.Player.class));
        }
    }

    @Override
    public void showPlayer(@NotNull Player player) {
        if (BukkitFeature.PLAYER_HIDE_API_PLUGIN_TICKET.isSupported()) {
            ((org.bukkit.entity.Player) wrappedObject).showPlayer(BukkitCore.getPlugin(), player.as(org.bukkit.entity.Player.class));
        } else {
            ((org.bukkit.entity.Player) wrappedObject).showPlayer(player.as(org.bukkit.entity.Player.class));
        }
    }

    @Override
    public @NotNull Location getCompassTarget() {
        return Locations.wrapLocation(((org.bukkit.entity.Player) wrappedObject).getCompassTarget());
    }

    @Override
    public @Nullable InetSocketAddress getAddress() {
        return ((org.bukkit.entity.Player) wrappedObject).getAddress();
    }

    @Override
    public void respawn() {
        ((org.bukkit.entity.Player) wrappedObject).spigot().respawn();
    }

    @SuppressWarnings("unchecked") // Via Version
    @Override
    public int getProtocolVersion() {
        if (Reflect.has("com.viaversion.viaversion.api.Via")) {
            int version = Via.getAPI().getPlayerVersion(wrappedObject);
            if (version != -1) {
                return version;
            }
        }
        if (Reflect.has("protocolsupport.api.ProtocolSupportAPI")) {
            int version = ProtocolSupportAPI.getProtocolVersion((org.bukkit.entity.Player) wrappedObject).getId();
            if (version != -1) {
                return version;
            }
        }
        if (BukkitFeature.PLAYER_PROTOCOL_VERSION.isSupported()) {
            int version = ((org.bukkit.entity.Player) wrappedObject).getProtocolVersion();
            if (version != -1) {
                return version;
            }
        }
        return Server.getProtocolVersion();
    }

    @Override
    public @Nullable Location getBedLocation() {
        return Locations.resolve(((org.bukkit.entity.Player) wrappedObject).getBedSpawnLocation());
    }

    @Override
    public long getFirstPlayed() {
        return ((org.bukkit.entity.Player) wrappedObject).getFirstPlayed();
    }

    @Override
    public long getLastPlayed() {
        return ((org.bukkit.entity.Player) wrappedObject).getLastPlayed();
    }

    @Override
    public boolean isBanned() {
        return ((org.bukkit.entity.Player) wrappedObject).isBanned();
    }

    @Override
    public boolean isWhitelisted() {
        return ((org.bukkit.entity.Player) wrappedObject).isWhitelisted();
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        ((org.bukkit.entity.Player) wrappedObject).setWhitelisted(whitelisted);
    }

    @Override
    public boolean isOp() {
        return wrappedObject.isOp();
    }

    @Override
    public boolean isOnline() {
        return ((org.bukkit.entity.Player) wrappedObject).isOnline();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> @NotNull T as(@NotNull Class<T> type) {
        try {
            return super.as(type);
        } catch (UnsupportedOperationException ignored) {
            return Players.UNSAFE_getPlayerConverter().convert(this, type);
        }
    }

    @Override
    public @NotNull PlayerAdapter adapter() {
        return BukkitCore.getSpectatorBackend().adapter(this, wrappedObject);
    }
}
