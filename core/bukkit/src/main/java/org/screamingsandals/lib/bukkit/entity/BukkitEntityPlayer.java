package org.screamingsandals.lib.bukkit.entity;

import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class BukkitEntityPlayer extends BukkitEntityHuman implements PlayerWrapper {
    protected BukkitEntityPlayer(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean isSprinting() {
        return PlayerMapper.isSprinting(this);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        PlayerMapper.setSprinting(this, sprinting);
    }

    @Override
    public boolean isFlying() {
        return PlayerMapper.isFlying(this);
    }

    @Override
    public void setFlying(boolean flying) {
        PlayerMapper.setFlying(this, flying);
    }

    @Override
    public boolean isAllowFlight() {
        return PlayerMapper.isAllowFlight(this);
    }

    @Override
    public void setAllowFlight(boolean flying) {
        PlayerMapper.setAllowFlight(this, flying);
    }

    @Override
    public boolean isSneaking() {
        return PlayerMapper.isSneaking(this);
    }

    @Override
    public void setSneaking(boolean sneaking) {
        PlayerMapper.setSneaking(this, sneaking);
    }

    @Override
    public int getPing() {
        return PlayerMapper.getPing(this);
    }

    @Override
    @Nullable
    public Component getPlayerListName() {
        return PlayerMapper.getPlayerListName(this);
    }

    public
    @Override void setPlayerListName(@Nullable Component component) {
        PlayerMapper.setPlayerListName(this, component);
    }

    @Override
    public void setPlayerListName(@Nullable ComponentLike component) {
        PlayerMapper.setPlayerListName(this, component != null ? component.asComponent() : null);
    }

    @Override
    @NotNull
    public Component getDisplayName() {
        return PlayerMapper.getDisplayName(this);
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        PlayerMapper.setDisplayName(this, component);
    }

    @Override
    public void setDisplayName(@Nullable ComponentLike component) {
        PlayerMapper.setDisplayName(this, component != null ? component.asComponent() : null);
    }

    @Override
    public void sendMessage(String message) {
        PlayerMapper.sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return PlayerMapper.hasPermission(this, permission);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return PlayerMapper.isPermissionSet(this, permission);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public Locale getLocale() {
        return PlayerMapper.getLocale(this);
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public boolean isOp() {
        return wrappedObject.isOp();
    }

    @Override
    public void setOp(boolean op) {
        wrappedObject.setOp(op);
    }

    @Override
    public Container getEnderChest() {
        return PlayerMapper.getEnderChest(this);
    }

    @Override
    public PlayerContainer getPlayerInventory() {
        return PlayerMapper.getPlayerInventory(this);
    }

    @Override
    public Optional<Container> getOpenedInventory() {
        return PlayerMapper.getOpenedInventory(this);
    }

    @Override
    public void openInventory(Openable container) {
        container.openInventory(this);
    }

    @Override
    public void closeInventory() {
        PlayerMapper.closeInventory(this);
    }

    @Override
    public void kick(Component message) {
        PlayerMapper.kick(this, message);
    }

    @Override
    public void kick(ComponentLike message) {
        PlayerMapper.kick(this, message.asComponent());
    }

    @Override
    public GameModeHolder getGameMode() {
        return PlayerMapper.getGameMode(this);
    }

    @Override
    public void setGameMode(@NotNull GameModeHolder gameMode) {
        PlayerMapper.setGameMode(this, gameMode);
    }

    @Override
    public int getLevel() {
        return PlayerMapper.getLevel(this);
    }

    @Override
    public float getExp() {
        return PlayerMapper.getExp(this);
    }

    @Override
    public void setLevel(int level) {
        PlayerMapper.setLevel(this, level);
    }

    @Override
    public void setExp(float exp) {
        PlayerMapper.setExp(this, exp);
    }

    @Override
    public void forceUpdateInventory() {
        PlayerMapper.forceUpdateInventory(this);
    }

    @Override
    public Optional<LocationHolder> getBedLocation() {
        return PlayerMapper.getBedLocation(this);
    }

    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(getName());
    }

    @Override
    public long getFirstPlayed() {
        return PlayerMapper.getFirstPlayed(this);
    }

    @Override
    public long getLastPlayed() {
        return PlayerMapper.getLastPlayed(this);
    }

    @Override
    public boolean isBanned() {
        return PlayerMapper.isBanned(this);
    }

    @Override
    public boolean isWhitelisted() {
        return PlayerMapper.isWhitelisted(this);
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        PlayerMapper.setWhitelisted(this, whitelisted);
    }

    @Override
    public boolean isOnline() {
        return PlayerMapper.isOnline(this);
    }

    @Override
    public Optional<WeatherHolder> getPlayerWeather() {
        return PlayerMapper.getWeather(this);
    }

    @Override
    public void setPlayerWeather(@Nullable WeatherHolder weather) {
        PlayerMapper.setWeather(this, weather);
    }

    @Override
    public long getPlayerTime() {
        return PlayerMapper.getTime(this);
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        PlayerMapper.setTime(this, time, relative);
    }

    @Override
    public void resetPlayerTime() {
        PlayerMapper.resetTime(this);
    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {
        PlayerMapper.sendParticle(this, particle, location);
    }

    @Override
    public Channel getChannel() {
        return PlayerMapper.getChannel(this);
    }
}
