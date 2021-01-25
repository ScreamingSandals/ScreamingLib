package org.screamingsandals.lib.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.function.Supplier;

@AbstractService
public abstract class PlayerMapper {
    protected final static String CONSOLE_NAME = "CONSOLE";

    protected final BidirectionalConverter<PlayerWrapper> playerConverter = BidirectionalConverter.build();
    protected final BidirectionalConverter<SenderWrapper> senderConverter = BidirectionalConverter.build();
    private static PlayerMapper playerMapper = null;
    private static AudienceProvider provider = null;

    public static void init(@NotNull Supplier<PlayerMapper> playerUtilsSupplier,
                            AudienceProvider audienceProvider) {
        if (playerMapper != null) {
            throw new UnsupportedOperationException("PlayerMapper is already initialized.");
        }

        playerMapper = playerUtilsSupplier.get();
        provider = audienceProvider;

        final var playerConverter = playerMapper.playerConverter
                .registerW2P(Audience.class, platform -> playerMapper.getAudience(platform, provider));
        final var senderConverter = playerMapper.senderConverter
                .registerW2P(Audience.class, platform -> playerMapper.getAudience(platform, provider));

        playerConverter.finish();
        senderConverter.finish();
    }

    public static boolean isInitialized() {
        return playerMapper != null;
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.playerConverter.convert(player);
    }

    public static <T> SenderWrapper wrapSender(T sender) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.senderConverter.convert(sender);
    }

    public static <T> T convertSenderWrapper(SenderWrapper wrapper, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.senderConverter.convert(wrapper, type);
    }

    public static <T> T convertPlayerWrapper(PlayerWrapper player, Class<T> type) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.playerConverter.convert(player, type);
    }

    public static void sendMessage(SenderWrapper playerWrapper, String message) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        playerMapper.sendMessage0(playerWrapper, message);
    }

    public static void closeInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        playerMapper.closeInventory0(playerWrapper);
    }

    public static Container getPlayerInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.getPlayerInventory0(playerWrapper);
    }

    public static Optional<Container> getOpenedInventory(PlayerWrapper playerWrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.getOpenedInventory0(playerWrapper);
    }

    public static LocationHolder getLocation(PlayerWrapper wrapper) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        return playerMapper.getLocation0(wrapper);
    }

    public static void teleport(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper is not initialized yet.");
        }
        playerMapper.teleport0(wrapper, location, callback);
    }

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public abstract void sendMessage0(SenderWrapper playerWrapper, String message);

    public abstract Container getPlayerInventory0(PlayerWrapper playerWrapper);

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);

    public abstract LocationHolder getLocation0(PlayerWrapper playerWrapper);

    public abstract void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback);

    protected abstract Audience getAudience(SenderWrapper wrapper, AudienceProvider provider);
}
