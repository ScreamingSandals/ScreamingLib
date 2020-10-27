package org.screamingsandals.lib.core.wrapper.sender;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class VelocityWrapper extends AbstractSender<Player> implements PlayerWrapper<Player> {

    public VelocityWrapper(Player instance) {
        super(instance, instance);
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public String getAddress() {
        return instance.getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public void kick(Component reason) {
        instance.disconnect(reason);
    }

    @Override
    public void sendMessage(Component message) {
        sendMessage(Identity.identity(getUuid()), message);
    }

    @Override
    public void sendMessage(Identity identity, Component message) {
        instance.sendMessage(identity, message);
    }

    @Override
    public Audience getAudience() {
        return audience;
    }

    @Override
    public boolean hasPermission(String permission) {
        return instance.hasPermission(permission);
    }
}
