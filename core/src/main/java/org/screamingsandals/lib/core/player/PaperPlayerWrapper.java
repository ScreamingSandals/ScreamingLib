package org.screamingsandals.lib.core.player;

import lombok.Data;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
public class PaperPlayerWrapper implements PlayerWrapper<Player> {
    private final transient Player instance;

    @Override
    public String getName() {
        return instance.getName();
    }

    @Override
    public UUID getUuid() {
        return instance.getUniqueId();
    }

    @Override
    public String getAddress() {
        final var address = instance.getAddress();

        if (address == null) {
            return "";
        }

        return address.getAddress().getHostAddress();
    }

    @Override
    public void kick(TextComponent reason) {
        instance.kickPlayer(reason.toLegacyText());
    }

    @Override
    public void sendMessage(TextComponent message) {
        instance.sendMessage(message);
    }
}
