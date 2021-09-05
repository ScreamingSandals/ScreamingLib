package org.screamingsandals.lib.proxy;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Locale;

@Data
@RequiredArgsConstructor
public class ProxiedSenderWrapper implements Wrapper, ForwardingAudience.Single, CommandSenderWrapper {
    private final String name;
    private final Type type;

    public void sendMessage(String message) {
        ProxiedPlayerMapper.sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return ProxiedPlayerMapper.hasPermission(this, permission);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return ProxiedPlayerMapper.isPermissionSet(this, permission);
    }

    @Override
    public Locale getLocale() {
        return null;
    }


    @Override
    public <T> T as(Class<T> type) {
        return ProxiedPlayerMapper.convertSenderWrapper(this, type);
    }

    @Override
    public @NotNull Audience audience() {
        return as(Audience.class);
    }

    @Override
    public boolean isOp() {
        return type == Type.CONSOLE; // No OP on proxies
    }

    @Override
    public void setOp(boolean op) {
        // No OP on proxies
    }
}