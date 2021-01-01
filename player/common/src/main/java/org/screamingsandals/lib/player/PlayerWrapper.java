package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Optional;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class PlayerWrapper implements Wrapper {
    private final String name;
    private final UUID uuid;

    public Container getPlayerInventory() {
        return PlayerUtils.getPlayerInventory(this);
    }

    public Optional<Container> getOpenedInventory() {
        return PlayerUtils.getOpenedInventory(this);
    }

    public void openInventory(Openable container) {
        container.openInventory(this);
    }

    public void closeInventory() {
        PlayerUtils.closeInventory(this);
    }

    public void sendMessage(String message) {
        PlayerUtils.sendMessage(this, message);
    }

    public <T> T as(Class<T> type) {
        return PlayerUtils.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((PlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
