package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;

import java.util.Locale;

/**
 * <p>A sender wrapper.</p>
 */
public interface SenderWrapper extends CommandSenderWrapper {
    /**
     * <p>Attempts to dispatch a command as this sender.</p>
     *
     * @param command the command
     */
    void tryToDispatchCommand(String command);

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean hasPermission(Permission permission) {
        return PlayerMapper.hasPermission(this, permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isPermissionSet(Permission permission) {
        return PlayerMapper.isPermissionSet(this, permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Locale getLocale() {
        return Locale.US;
    }
}