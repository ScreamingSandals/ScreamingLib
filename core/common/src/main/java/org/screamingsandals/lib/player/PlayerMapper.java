package org.screamingsandals.lib.player;

import io.netty.channel.Channel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@AbstractService
public abstract class PlayerMapper {
    protected final BidirectionalConverter<OfflinePlayerWrapper> offlinePlayerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<CommandSenderWrapper> senderConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper.Hand> handConverter = BidirectionalConverter.build();
    protected final Map<UUID, Channel> channelCache = new HashMap<>();
    protected AudienceProvider provider;
    private static PlayerMapper playerMapper;

    protected PlayerMapper() {
        if (playerMapper != null) {
            throw new UnsupportedOperationException("PlayerMapper is already initialized.");
        }
        playerMapper = this;
    }

    @OnPostConstruct
    public void postConstruct() {
        offlinePlayerConverter
                .registerP2W(UUID.class, uuid -> new FinalOfflinePlayerWrapper(uuid, null))
                .registerW2P(UUID.class, OfflinePlayerWrapper::getUuid)
                .registerP2W(OfflinePlayerWrapper.class, e -> e);
        playerConverter
                .registerP2W(PlayerWrapper.class, e -> e);
        senderConverter
                .registerP2W(CommandSenderWrapper.class, e -> e);
        handConverter
                .registerP2W(PlayerWrapper.Hand.class, e -> e);
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        final var converted = playerMapper.playerConverter.convert(player);
        if (playerMapper.canBeStoredAsWrapped(player)) {
            converted.setWrappedPlayer(new WeakReference<>(player));
        }
        return converted;
    }

    public static <T> OfflinePlayerWrapper wrapOfflinePlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.offlinePlayerConverter.convert(player);
    }

    public static <T> CommandSenderWrapper wrapSender(T sender) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.senderConverter.convert(sender);
    }

    public static <T> PlayerWrapper.Hand wrapHand(T hand) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.handConverter.convert(hand);
    }

    public static <T> Optional<PlayerWrapper.Hand> resolveHand(T hand) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (hand == null) {
            return Optional.empty();
        }
        return playerMapper.handConverter.convertOptional(hand);
    }

    public static <T> T convertHand(PlayerWrapper.Hand hand, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.handConverter.convert(hand, type);
    }

    public static <T> T convertSenderWrapper(CommandSenderWrapper wrapper, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.senderConverter.convert(wrapper, type);
    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.playerConverter.convert(player, type);
    }

    public static <T> T convertOfflinePlayer(OfflinePlayerWrapper player, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.offlinePlayerConverter.convert(player, type);
    }

    public static void sendMessage(SenderWrapper playerWrapper, String message) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.sendMessage0(playerWrapper, message);
    }

    public static void closeInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.closeInventory0(playerWrapper);
    }

    public static Container getEnderChest(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getEnderChest0(playerWrapper);
    }

    public static PlayerContainer getPlayerInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayerInventory0(playerWrapper);
    }

    public static Optional<Container> getOpenedInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getOpenedInventory0(playerWrapper);
    }

    public static LocationHolder getLocation(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getLocation0(wrapper);
    }

    public static Optional<LocationHolder> getBedLocation(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getBedLocation0(wrapper);
    }

    public static CompletableFuture<Boolean> teleport(PlayerWrapper wrapper, LocationHolder location) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.teleport0(wrapper, location);
    }

    public static CompletableFuture<Void> teleport(PlayerWrapper wrapper, LocationHolder location, Runnable callback, boolean forceCallback) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.teleport0(wrapper, location, callback, forceCallback);
    }

    public static void kick(PlayerWrapper wrapper, Component message) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.kick0(wrapper, message);
    }

    public static Optional<PlayerWrapper> getPlayer(String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayer0(name);
    }

    public abstract Optional<PlayerWrapper> getPlayer0(String name);

    public static Optional<PlayerWrapper> getPlayer(UUID uuid) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayer0(uuid);
    }

    public abstract Optional<PlayerWrapper> getPlayer0(UUID uuid);

    public static Optional<PlayerWrapper> getPlayerExact(String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayerExact0(name);
    }

    public static CommandSenderWrapper getConsoleSender() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getConsoleSender0();
    }

    public static Audience getAudience(CommandSenderWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getAudience0(wrapper);
    }

    public static boolean hasPermission(CommandSenderWrapper wrapper, Permission permission) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.hasPermission0(wrapper, permission);
    }

    public static boolean isPermissionSet(CommandSenderWrapper wrapper, Permission permission) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isPermissionSet0(wrapper, permission);
    }

    public static boolean isOp(Operator wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isOp0(wrapper);
    }

    public static void setOp(Operator wrapper, boolean op) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setOp0(wrapper, op);
    }

    public static long getFirstPlayed(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getFirstPlayed0(wrapper);
    }

    public static long getLastPlayed(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getLastPlayed0(wrapper);
    }

    public static boolean isBanned(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isBanned0(wrapper);
    }

    public static boolean isWhitelisted(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isWhitelisted0(wrapper);
    }

    public static boolean isOnline(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isOnline0(wrapper);
    }

    public static void setWhitelisted(OfflinePlayerWrapper wrapper, boolean whitelisted) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setWhitelisted0(wrapper, whitelisted);
    }

    public static OfflinePlayerWrapper getOfflinePlayer(UUID uuid) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getOfflinePlayer0(uuid);
    }

    /**
     * This method may involve a blocking web request to get the UUID for the given name.
     *
     * @param name Name of the player
     * @return the offline player or empty optional if not found
     * @deprecated see {@link PlayerMapper#getOfflinePlayer(UUID)}
     */
    @Deprecated
    public static Optional<OfflinePlayerWrapper> getOfflinePlayer(String name) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getOfflinePlayer0(name);
    }

    public static Locale getLocale(SenderWrapper senderWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getLocale0(senderWrapper);
    }

    public static GameModeHolder getGameMode(PlayerWrapper player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getGameMode0(player);
    }

    public static void setGameMode(PlayerWrapper player, GameModeHolder gameMode) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setGameMode0(player, gameMode);
    }

    public static Component getDisplayName(PlayerWrapper player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getDisplayName0(player);
    }

    public static void setDisplayName(PlayerWrapper player, Component displayName) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setDisplayName0(player, displayName);
    }

    public static Component getPlayerListName(PlayerWrapper player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayerListName0(player);
    }

    public static void setPlayerListName(PlayerWrapper player, Component playerListName) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setPlayerListName0(player, playerListName);
    }

    public static int getLevel(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getLevel0(wrapper);
    }

    public static float getExp(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getExp0(wrapper);
    }

    public static void setLevel(PlayerWrapper wrapper, int level) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setLevel0(wrapper, level);
    }

    public static void setExp(PlayerWrapper wrapper, float exp) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setExp0(wrapper, exp);
    }

    public static int getPing(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPing0(wrapper);
    }

    public static boolean isSprinting(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isSprinting0(wrapper);
    }

    public static void setSprinting(PlayerWrapper wrapper, boolean sprinting) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setSprinting0(wrapper, sprinting);
    }

    public static boolean isFlying(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isFlying0(wrapper);
    }

    public static void setFlying(PlayerWrapper wrapper, boolean flying) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setFlying0(wrapper, flying);
    }

    public static boolean isSneaking(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isSneaking0(wrapper);
    }

    public static void setSneaking(PlayerWrapper wrapper, boolean sneaking) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setSneaking0(wrapper, sneaking);
    }

    public static boolean isAllowFlight(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.isAllowFlight0(wrapper);
    }

    public static void setAllowFlight(PlayerWrapper wrapper, boolean flight) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setAllowFlight0(wrapper, flight);
    }

    public static Optional<WeatherHolder> getWeather(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getWeather0(wrapper);
    }

    public static void setWeather(PlayerWrapper wrapper, @Nullable WeatherHolder weather) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setWeather0(wrapper, weather);
    }

    public static long getTime(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getTime0(wrapper);
    }

    public static void setTime(PlayerWrapper wrapper, long time, boolean relative) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.setTime0(wrapper, time, relative);
    }

    public static void resetTime(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.resetTime0(wrapper);
    }

    public static void sendParticle(PlayerWrapper player, ParticleHolder particle, LocationHolder location) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.sendParticle0(player, particle, location);
    }

    public static Channel getChannel(PlayerWrapper player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        if (player == null) {
            throw new UnsupportedOperationException("Invalid player provided!");
        }

        final var cachedChannel = playerMapper.channelCache.get(player.getUuid());
        if (cachedChannel == null) {
            final var newLookup = playerMapper.getChannel0(player);
            playerMapper.channelCache.put(player.getUuid(), newLookup);
            return newLookup;
        }
        return cachedChannel;
    }

    public static void forceUpdateInventory(PlayerWrapper player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.forceUpdateInventory0(player);
    }

    public static BidirectionalConverter<PlayerWrapper> UNSAFE_getPlayerConverter() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        
        return playerMapper.playerConverter;
    }

    public static BidirectionalConverter<CommandSenderWrapper> UNSAFE_getSenderConverter() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.senderConverter;
    }

    public abstract CommandSenderWrapper getConsoleSender0();

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public abstract void sendMessage0(SenderWrapper playerWrapper, String message);

    public abstract Container getEnderChest0(PlayerWrapper playerWrapper);

    public abstract PlayerContainer getPlayerInventory0(PlayerWrapper playerWrapper);

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);

    public abstract LocationHolder getLocation0(PlayerWrapper playerWrapper);

    public abstract Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper);

    public abstract CompletableFuture<Void> teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback, boolean forceCallback);

    public abstract CompletableFuture<Boolean> teleport0(PlayerWrapper wrapper, LocationHolder location);

    public abstract void kick0(PlayerWrapper wrapper, Component message);

    public abstract Audience getAudience0(CommandSenderWrapper wrapper);

    public abstract boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission);

    public abstract boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission);

    public abstract boolean isOp0(Operator wrapper);

    public abstract void setOp0(Operator wrapper, boolean op);

    public abstract long getFirstPlayed0(OfflinePlayerWrapper playerWrapper);

    public abstract long getLastPlayed0(OfflinePlayerWrapper playerWrapper);

    public abstract boolean isBanned0(OfflinePlayerWrapper playerWrapper);

    public abstract boolean isWhitelisted0(OfflinePlayerWrapper playerWrapper);

    public abstract boolean isOnline0(OfflinePlayerWrapper playerWrapper);

    public abstract void setWhitelisted0(OfflinePlayerWrapper playerWrapper, boolean whitelisted);

    public abstract OfflinePlayerWrapper getOfflinePlayer0(UUID uuid);

    public abstract Optional<OfflinePlayerWrapper> getOfflinePlayer0(String name);

    public abstract Locale getLocale0(SenderWrapper senderWrapper);

    public abstract GameModeHolder getGameMode0(PlayerWrapper player);

    public abstract void setGameMode0(PlayerWrapper player, GameModeHolder gameMode);

    public abstract Component getDisplayName0(PlayerWrapper player);

    public abstract void setDisplayName0(PlayerWrapper player, Component component);

    public abstract Component getPlayerListName0(PlayerWrapper player);

    public abstract void setPlayerListName0(PlayerWrapper player, Component playerListName);

    public abstract boolean canBeStoredAsWrapped(Object wrapped);

    public abstract int getLevel0(PlayerWrapper player);

    public abstract float getExp0(PlayerWrapper player);

    public abstract void setLevel0(PlayerWrapper player, int level);

    public abstract void setExp0(PlayerWrapper player, float exp);

    public abstract int getPing0(PlayerWrapper player);

    public abstract boolean isSprinting0(PlayerWrapper player);

    public abstract void setSprinting0(PlayerWrapper player, boolean sprinting);

    public abstract boolean isFlying0(PlayerWrapper player);

    public abstract void setFlying0(PlayerWrapper player, boolean flying);

    public abstract boolean isAllowFlight0(PlayerWrapper player);

    public abstract void setAllowFlight0(PlayerWrapper player, boolean allowFlight);

    public abstract boolean isSneaking0(PlayerWrapper player);

    public abstract void setSneaking0(PlayerWrapper player, boolean sneaking);

    public abstract Optional<WeatherHolder> getWeather0(PlayerWrapper player);

    public abstract void setWeather0(PlayerWrapper player, @Nullable WeatherHolder weather);

    public abstract long getTime0(PlayerWrapper player);

    public abstract void setTime0(PlayerWrapper player, long time, boolean relative);

    public abstract void resetTime0(PlayerWrapper player);

    public abstract void sendParticle0(PlayerWrapper player, ParticleHolder particle, LocationHolder location);

    public abstract Channel getChannel0(PlayerWrapper player);

    public abstract void forceUpdateInventory0(PlayerWrapper player);

    public abstract Optional<PlayerWrapper> getPlayerExact0(String name);
}
