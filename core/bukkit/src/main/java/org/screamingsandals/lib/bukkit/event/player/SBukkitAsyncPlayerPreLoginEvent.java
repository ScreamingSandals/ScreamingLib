package org.screamingsandals.lib.bukkit.event.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.event.player.SAsyncPlayerPreLoginEvent;
import org.screamingsandals.lib.utils.adventure.ComponentObjectLink;

import java.net.InetAddress;
import java.util.UUID;


@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitAsyncPlayerPreLoginEvent implements SAsyncPlayerPreLoginEvent {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final AsyncPlayerPreLoginEvent event;

    @Override
    public UUID getUuid() {
        return event.getUniqueId();
    }

    @Override
    public InetAddress getAddress() {
        return event.getAddress();
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public Result getResult() {
        return Result.valueOf(event.getLoginResult().name());
    }

    @Override
    public void setResult(Result result) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.valueOf(result.name()));
    }

    @Override
    public Component getMessage() {
        return ComponentObjectLink.processGetter(event, "kickMessage", event::getKickMessage);
    }

    @Override
    public void setMessage(Component message) {
        ComponentObjectLink.processSetter(event, "kickMessage", event::setKickMessage, message);
    }

    @Override
    public void setMessage(ComponentLike message) {
        setMessage(message.asComponent());
    }
}
