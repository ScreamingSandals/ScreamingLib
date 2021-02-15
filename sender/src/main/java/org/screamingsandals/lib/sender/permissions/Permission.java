package org.screamingsandals.lib.sender.permissions;

import org.screamingsandals.lib.sender.CommandSenderWrapper;

public interface Permission {
    default boolean hasPermission(CommandSenderWrapper commandSender) {
        return commandSender.hasPermission(this);
    }

    default boolean isPermissionSet(CommandSenderWrapper commandSender) {
        return commandSender.isPermissionSet(this);
    }
}
