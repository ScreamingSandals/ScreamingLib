package org.screamingsandals.lib.sender;

import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.title.Title;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Locale;

public interface CommandSenderWrapper extends Wrapper, ForwardingAudience.Single, Operator {

    Type getType();

    void sendMessage(String message);

    default void sendMessage(SenderMessage senderMessage) {
        sendMessage(senderMessage.asComponent(this));
    }

    default void sendActionBar(SenderMessage senderMessage) {
        sendActionBar(senderMessage.asComponent(this));
    }

    default void sendPlayerListHeader(SenderMessage senderMessage) {
        sendPlayerListHeader(senderMessage.asComponent(this));
    }

    default void sendPlayerListFooter(SenderMessage senderMessage) {
        sendPlayerListFooter(senderMessage.asComponent(this));
    }

    default void sendPlayerListHeaderAndFooter(SenderMessage header, SenderMessage footer) {
        sendPlayerListHeaderAndFooter(header.asComponent(this), footer.asComponent(this));
    }

    default void showTitle(TitleableSenderMessage title) {
        showTitle(title.asTitle(this));
    }

    default void showTitle(TitleableSenderMessage title, Title.Times times) {
        showTitle(title.asTitle(this, times));
    }

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
