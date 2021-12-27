package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.audience.MessageType;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

@Data
public final class MessageTypeWrapper implements Wrapper {
    private final MessageType messageType;

    @NotNull
    public MessageType asMessageType() {
        return messageType;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) messageType.name();
        } else if (type.isInstance(messageType)) {
            return (T) messageType;
        }

        return (T) Reflect.findEnumConstant(AdventureUtils.NATIVE_MESSAGE_TYPE_CLASS, messageType.name());
    }
}
