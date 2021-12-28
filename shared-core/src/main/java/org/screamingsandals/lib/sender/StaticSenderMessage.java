package org.screamingsandals.lib.sender;

import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class StaticSenderMessage implements SenderMessage {
    private final Component component;

    @Override
    public @NotNull Component asComponent(@Nullable CommandSenderWrapper wrapper) {
        return component;
    }

    @Override
    public @NotNull List<Component> asComponentList(@Nullable CommandSenderWrapper wrapper) {
        return List.of(component);
    }

    @Override
    public @NotNull Component asComponent() {
        return component;
    }
}