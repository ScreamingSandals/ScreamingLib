package org.screamingsandals.lib.event.player;

import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class SPlayerTeleportEvent extends SPlayerMoveEvent {
    private final ImmutableObjectLink<TeleportCause> cause;

    public SPlayerTeleportEvent(ImmutableObjectLink<PlayerWrapper> player, ObjectLink<LocationHolder> currentLocation, ObjectLink<LocationHolder> newLocation, ImmutableObjectLink<TeleportCause> cause) {
        super(player, currentLocation, newLocation);
        this.cause = cause;
    }

    public TeleportCause getCause() {
        return cause.get();
    }

    //from bukkit
    // TODO: holder
    public enum TeleportCause {
        ENDER_PEARL,
        /**
         * Indicates the teleportation was caused by a player executing a
         * command
         */
        COMMAND,
        /**
         * Indicates the teleportation was caused by a plugin
         */
        PLUGIN,
        /**
         * Indicates the teleportation was caused by a player entering a
         * Nether portal
         */
        NETHER_PORTAL,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * portal
         */
        END_PORTAL,
        /**
         * Indicates the teleportation was caused by a player teleporting to a
         * Entity/Player via the spectator menu
         */
        SPECTATE,
        /**
         * Indicates the teleportation was caused by a player entering an End
         * gateway
         */
        END_GATEWAY,
        /**
         * Indicates the teleportation was caused by a player consuming chorus
         * fruit
         */
        CHORUS_FRUIT,
        /**
         * Indicates the teleportation was caused by an event not covered by
         * this enum
         */
        UNKNOWN;

        public static List<TeleportCause> VALUES = Arrays.asList(values());

        public static Optional<TeleportCause> getByName(String name) {
            return VALUES.stream()
                    .filter(next -> next.name().equals(name.toUpperCase(Locale.ROOT)))
                    .findFirst();
        }
    }
}
