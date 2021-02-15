package org.screamingsandals.lib.sender;

import net.kyori.adventure.audience.ForwardingAudience;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.Wrapper;

public interface CommandSenderWrapper extends Wrapper, ForwardingAudience.Single {

    Type getType();

    void sendMessage(String message);

    default boolean hasPermission(String permission) {
        return hasPermission(SimplePermission.of(permission));
    }

    boolean hasPermission(Permission permission);

    default boolean isPermissionSet(String permission) {
        return isPermissionSet(SimplePermission.of(permission));
    }

    boolean isPermissionSet(Permission permission);

    String getName();

    enum Type {
        PLAYER,
        CONSOLE
    }
}
