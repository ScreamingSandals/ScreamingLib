package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.utils.Wrapper;

@Data
@RequiredArgsConstructor
public class SenderWrapper implements Wrapper, CommandSenderWrapper {
    private final String name;
    private final Type type;

    public void sendMessage(String message) {
        PlayerMapper.sendMessage(this, message);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return PlayerMapper.hasPermission(this, permission);
    }

    @Override
    public boolean isPermissionSet(Permission permission) {
        return PlayerMapper.isPermissionSet(this, permission);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PlayerMapper.convertSenderWrapper(this, type);
    }

    @Override
    @NonNull
    public Audience audience() {
        return PlayerMapper.getAudience(this);
    }

    @Override
    public boolean isOp() {
        return PlayerMapper.isOp(this);
    }

    @Override
    public void setOp(boolean op) {
        PlayerMapper.setOp(this, op);
    }
}