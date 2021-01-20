package org.screamingsandals.lib.player;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class PlayerMapper {
    protected final static String CONSOLE_NAME = "CONSOLE";

    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<SenderWrapper> senderConverter = BidirectionalConverter.build();
    private static PlayerMapper playerMapper = null;

    public static void init(@NotNull Supplier<PlayerMapper> playerUtilsSupplier) {
        if (playerMapper != null) {
            throw new UnsupportedOperationException("PlayerUtils are already initialized.");
        }

        playerMapper = playerUtilsSupplier.get();
        playerMapper.playerConverter.finish();
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.playerConverter.convert(player);
    }

    public static <T> SenderWrapper wrapSender(T sender) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.senderConverter.convert(sender);
    }

    public static <T> T convertSenderWrapper(SenderWrapper wrapper, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.senderConverter.convert(wrapper, type);
    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.playerConverter.convert(player, type);
    }

    public static void sendMessage(SenderWrapper playerWrapper, String message) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        playerMapper.sendMessage0(playerWrapper, message);
    }

    public abstract void sendMessage0(SenderWrapper playerWrapper, String message);

    public static void closeInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        playerMapper.closeInventory0(playerWrapper);
    }

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public static Container getPlayerInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.getPlayerInventory0(playerWrapper);
    }

    public abstract Container getPlayerInventory0(PlayerWrapper playerWrapper);

    public static Optional<Container> getOpenedInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.getOpenedInventory0(playerWrapper);
    }

    public static LocationHolder getLocation(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerMapper.getLocation0(wrapper);
    }

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);

    public abstract LocationHolder getLocation0(PlayerWrapper playerWrapper);

    public static boolean isInitialized() {
        return playerMapper != null;
    }
}
