package org.screamingsandals.lib.packet;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.GameMode;

import java.util.List;

public interface SClientboundPlayerInfoPacket extends SPacket {
    SClientboundPlayerInfoPacket setAction(Action action);

    SClientboundPlayerInfoPacket setPlayersData(List<PlayerInfoData> data);

    enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER;
    }

    @RequiredArgsConstructor
    @Getter
    final class PlayerInfoData {
        private final int latency;
        private final GameMode gameMode;
        private final Component displayName;
        private final GameProfile gameProfile;
    }
}
