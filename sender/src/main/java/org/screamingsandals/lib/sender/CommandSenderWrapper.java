package org.screamingsandals.lib.sender;

import net.kyori.adventure.audience.ForwardingAudience;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Locale;

public interface CommandSenderWrapper extends Wrapper, ForwardingAudience.Single, Operator {

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

    Locale getLocale();

    enum Type {
        PLAYER,
        CONSOLE
    }
}
