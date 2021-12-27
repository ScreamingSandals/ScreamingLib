package org.screamingsandals.lib.bukkit.entity;

import io.netty.channel.Channel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
        return ComponentObjectLink.processGetter(bukkitPlayer, "playerListName", bukkitPlayer::getPlayerListName);
    }

    public
    @Override void setPlayerListName(@Nullable Component component) {
        var bukkitPlayer = (Player) wrappedObject;
        ComponentObjectLink.processSetter(bukkitPlayer, "playerListName", bukkitPlayer::setPlayerListName, component);
    }

    @Override
    public void setPlayerListName(@Nullable ComponentLike component) {
        setPlayerListName(component != null ? component.asComponent() : null);
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        var bukkitPlayer = (Player) wrappedObject;
        return ComponentObjectLink.processGetter(bukkitPlayer, "displayName", bukkitPlayer::getDisplayName);
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        var bukkitPlayer = (Player) wrappedObject;
        ComponentObjectLink.processSetter(bukkitPlayer, "displayName", bukkitPlayer::setDisplayName, component);
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
    public Container getEnderChest() {
        return ContainerFactory.wrapContainer(((Player) wrappedObject).getEnderChest()).orElseThrow();
    }

    @Override
    public PlayerContainer getPlayerInventory() {
        return ContainerFactory.<PlayerContainer>wrapContainer(((Player) wrappedObject).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory() {
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
        ComponentObjectLink.processSetter(bukkitPlayer, "kick", bukkitPlayer::kickPlayer, message);
    }

    @Override
    public void kick(ComponentLike message) {
        kick(message.asComponent());
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
    public Optional<String> getLastName() {
        return Optional.ofNullable(getName());
    }

    @Override
    public Optional<WeatherHolder> getPlayerWeather() {
        return WeatherHolder.ofOptional(((Player) wrappedObject).getPlayerWeather());
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
    public Optional<EntityBasic> getSpectatorTarget() {
        var target = ((Player) wrappedObject).getSpectatorTarget();
        if (target == null) {
            return Optional.empty();
        }
        return EntityMapper.wrapEntity(target);
    }

    @Override
    public void setSpectatorTarget(@Nullable EntityBasic entity) {
        ((Player) wrappedObject).setSpectatorTarget(entity == null ? null : entity.as(Entity.class));
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
    @NotNull
    public Audience audience() {
        return BukkitCore.audiences().player((Player) wrappedObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (UnsupportedOperationException ignored) {
            return PlayerMapper.UNSAFE_getPlayerConverter().convert(this, type);
        }
    }
}
