package org.screamingsandals.lib.player;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.utils.BidirectionalConverter;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class PlayerUtils {

    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    private static PlayerUtils playerUtils = null;

    public static void init(@NotNull Supplier<PlayerUtils> playerUtilsSupplier) {
        if (playerUtils != null) {
            throw new UnsupportedOperationException("PlayerUtils are already initialized.");
        }

        playerUtils = playerUtilsSupplier.get();
        playerUtils.playerConverter.finish();
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerUtils.playerConverter.convert(player);
    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerUtils.playerConverter.convert(player, type);
    }

    public static void sendMessage(PlayerWrapper playerWrapper, String message) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        playerUtils.sendMessage0(playerWrapper, message);
    }

    public abstract void sendMessage0(PlayerWrapper playerWrapper, String message);

    public static void closeInventory(PlayerWrapper playerWrapper) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        playerUtils.closeInventory0(playerWrapper);
    }

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public static Container getPlayerInventory(PlayerWrapper playerWrapper) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerUtils.getPlayerInventory0(playerWrapper);
    }

    public abstract Container getPlayerInventory0(PlayerWrapper playerWrapper);

    public static Optional<Container> getOpenedInventory(PlayerWrapper playerWrapper) {
        if (playerUtils == null) {
            throw new UnsupportedOperationException("PlayerUtils aren't initialized yet.");
        }
        return playerUtils.getOpenedInventory0(playerWrapper);
    }

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);
}
