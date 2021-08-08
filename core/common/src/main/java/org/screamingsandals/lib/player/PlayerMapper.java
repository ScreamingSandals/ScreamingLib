package org.screamingsandals.lib.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.WorldHolder;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class PlayerMapper {
    protected final static String CONSOLE_NAME = "CONSOLE";

    protected final BidirectionalConverter<OfflinePlayerWrapper> offlinePlayerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<CommandSenderWrapper> senderConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper.Hand> handConverter = BidirectionalConverter.build();
    protected AudienceProvider provider = null;
    private static PlayerMapper playerMapper = null;

    public static void init(@NotNull Supplier<PlayerMapper> playerUtilsSupplier) {
        if (playerMapper != null) {
            throw new UnsupportedOperationException("PlayerMapper is already initialized.");
        }

        playerMapper = playerUtilsSupplier.get();
        playerMapper.offlinePlayerConverter
                .registerP2W(UUID.class, uuid -> new FinalOfflinePlayerWrapper(uuid, null))
                .registerW2P(UUID.class, OfflinePlayerWrapper::getUuid);
    }

    public static boolean isInitialized() {
        return playerMapper != null;
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

    public static void teleport(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        playerMapper.teleport0(wrapper, location, callback);
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

    public static List<PlayerWrapper> getPlayers() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayersOnServer0();
    }

    public static List<PlayerWrapper> getPlayers(WorldHolder holder) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getPlayers0(holder);
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

    public abstract List<PlayerWrapper> getPlayersOnServer0();

    public abstract List<PlayerWrapper> getPlayers0(WorldHolder holder);

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public abstract void sendMessage0(SenderWrapper playerWrapper, String message);

    public abstract Container getEnderChest0(PlayerWrapper playerWrapper);

    public abstract PlayerContainer getPlayerInventory0(PlayerWrapper playerWrapper);

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);

    public abstract LocationHolder getLocation0(PlayerWrapper playerWrapper);

    public abstract Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper);

    public abstract void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback);

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

    public abstract Locale getLocale0(SenderWrapper senderWrapper);

    public abstract GameModeHolder getGameMode0(PlayerWrapper player);

    public abstract void setGameMode0(PlayerWrapper player, GameModeHolder gameMode);

    public abstract boolean canBeStoredAsWrapped(Object wrapped);
}
