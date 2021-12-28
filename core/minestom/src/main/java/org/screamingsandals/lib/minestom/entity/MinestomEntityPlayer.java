package org.screamingsandals.lib.minestom.entity;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.ContainerFactory;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MinestomEntityPlayer extends MinestomEntityHuman implements PlayerWrapper {
    public MinestomEntityPlayer(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public boolean isSprinting() {
        return wrappedObject.isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        wrappedObject.setSprinting(sprinting);
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
        return ((Player) wrappedObject).isAllowFlying();
    }

    @Override
    public void setAllowFlight(boolean flying) {
        ((Player) wrappedObject).setAllowFlying(flying);
    }

    @Override
    public boolean isSneaking() {
        return wrappedObject.isSneaking();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        wrappedObject.setSneaking(sneaking);
    }

    @Override
    public int getPing() {
        return ((Player) wrappedObject).getLatency();
    }

    @Override
    public @Nullable Component getPlayerListName() {
        return null;
    }

    @Override
    public void setPlayerListName(@Nullable Component component) {

    }

    @Override
    public void setPlayerListName(@Nullable ComponentLike component) {

    }

    @Override
    public @NotNull Component getDisplayName() {
        return Objects.requireNonNullElseGet(((Player) wrappedObject).getDisplayName(), () -> AdventureHelper.toComponent(((Player) wrappedObject).getUsername()));
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        ((Player) wrappedObject).setDisplayName(component);
    }

    @Override
    public void setDisplayName(@Nullable ComponentLike component) {
        ((Player) wrappedObject).setDisplayName(component != null ? component.asComponent() : null);
    }

    @Override
    public Container getEnderChest() {
        throw new UnsupportedOperationException("Minestom does not support ender chests");
    }

    @Override
    public PlayerContainer getPlayerInventory() {
        return ContainerFactory.<PlayerContainer>wrapContainer(((Player) wrappedObject).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory() {
        if (((Player) wrappedObject).getOpenInventory() != null) {
            return ContainerFactory.wrapContainer(((Player) wrappedObject).getOpenInventory())
        }
        return Optional.empty();
    }

    @Override
    public void closeInventory() {
        ((Player) wrappedObject).closeInventory();
    }

    @Override
    public void kick(Component message) {
        ((Player) wrappedObject).kick(message);
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
        ((Player) wrappedObject).getInventory().update();
    }

    @Override
    public Optional<WeatherHolder> getPlayerWeather() {
        return Optional.empty();
    }

    @Override
    public void setPlayerWeather(@Nullable WeatherHolder weather) {

    }

    @Override
    public long getPlayerTime() {
        return 0;
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {

    }

    @Override
    public void resetPlayerTime() {

    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {

    }

    @Override
    public LocationHolder getCompassTarget() {
        return null;
    }

    @Override
    public void setCompassTarget(LocationHolder location) {

    }

    @Override
    public void restoreDefaultScoreboard() {

    }

    @Override
    public Optional<EntityBasic> getSpectatorTarget() {
        return Optional.empty();
    }

    @Override
    public void setSpectatorTarget(@Nullable EntityBasic entity) {

    }

    @Override
    public @NotNull Audience audience() {
        return null;
    }

    @Override
    public void tryToDispatchCommand(String command) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String getName() {
        return ((Player) wrappedObject).getUsername();
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUuid();
    }

    @Override
    public Optional<String> getLastName() {
        return Optional.empty();
    }
}
