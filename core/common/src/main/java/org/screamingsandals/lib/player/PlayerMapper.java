package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;

@AbstractService
public abstract class PlayerMapper {
    protected final BidirectionalConverter<OfflinePlayerWrapper> offlinePlayerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper> specialPlayerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<PlayerWrapper.Hand> handConverter = BidirectionalConverter.build();
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
        handConverter
                .registerP2W(PlayerWrapper.Hand.class, e -> e);
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        try {
            var sender = playerMapper.wrapSender0(player);

            if (sender instanceof PlayerWrapper) {
                return (PlayerWrapper) sender;
            }
        } catch (UnsupportedOperationException ignored) {}

        return playerMapper.specialPlayerConverter.convert(player);
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
        return playerMapper.wrapSender0(sender);
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

    public static <T> T convertOfflinePlayer(OfflinePlayerWrapper player, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.offlinePlayerConverter.convert(player, type);
    }

    public static Optional<LocationHolder> getBedLocation(OfflinePlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getBedLocation0(wrapper);
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

    public static SenderWrapper getConsoleSender() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        return playerMapper.getConsoleSender0();
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

    public static BidirectionalConverter<PlayerWrapper> UNSAFE_getPlayerConverter() {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        
        return playerMapper.specialPlayerConverter;
    }

    // abstract methods for implementations

    protected abstract <T> CommandSenderWrapper wrapSender0(T sender);

    public abstract SenderWrapper getConsoleSender0();

    public abstract Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper);

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

    public abstract Optional<PlayerWrapper> getPlayerExact0(String name);
}
