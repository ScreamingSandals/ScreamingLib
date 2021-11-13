package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;

import java.util.Locale;

public interface SenderWrapper extends CommandSenderWrapper {

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