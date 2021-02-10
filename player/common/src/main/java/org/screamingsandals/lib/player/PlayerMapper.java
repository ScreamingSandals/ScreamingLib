package org.screamingsandals.lib.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.world.LocationHolder;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@AbstractService
public abstract class PlayerMapper {
    protected final static String CONSOLE_NAME = "CONSOLE";

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
    }

    public static boolean isInitialized() {
        return playerMapper != null;
    }

    public static <T> PlayerWrapper wrapPlayer(T player) {
        if (playerMapper == null) {
            throw new UnsupportedOperationException("PlayerMapper isn't initialized yet.");
        }
        final var converted = playerMapper.playerConverter.convert(player);
        converted.setWrappedPlayer(new WeakReference<>(player));
        return converted;
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

    public static Container getPlayerInventory(PlayerWrapper playerWrapper) {
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
        return playerMapper.getPlayers0();
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

    public abstract List<PlayerWrapper> getPlayers0();

    public abstract void closeInventory0(PlayerWrapper playerWrapper);

    public abstract void sendMessage0(SenderWrapper playerWrapper, String message);

    public abstract Container getPlayerInventory0(PlayerWrapper playerWrapper);

    public abstract Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper);

    public abstract LocationHolder getLocation0(PlayerWrapper playerWrapper);

    public abstract void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback);

    public abstract void kick0(PlayerWrapper wrapper, Component message);

    public abstract Audience getAudience0(CommandSenderWrapper wrapper);
}
