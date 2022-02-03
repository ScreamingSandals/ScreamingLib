package org.screamingsandals.lib.bungee.spectator.audience.adapter;

import lombok.Data;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.spectator.audience.MessageType;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;

import java.util.UUID;

@Data
@Accessors(fluent = true)
public class BungeeAdapter implements Adapter {
    private final CommandSender sender;
    private final Audience owner;

    @Override
    public void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        var comp = message instanceof AudienceComponentLike ? ((AudienceComponentLike) message).asComponent(owner) : message.asComponent();
        sender.sendMessage(comp.as(BaseComponent.class));
    }
}
