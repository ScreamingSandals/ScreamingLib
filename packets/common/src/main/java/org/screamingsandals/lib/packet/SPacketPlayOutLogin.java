package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.Difficulty;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.WorldType;

public interface SPacketPlayOutLogin extends SPacket {
    void setEntityId(int entityId);

    void setGameMode(GameMode gameMode);

    void setDimension(int dimension);

    void setDifficulty(Difficulty difficulty);

    void setMaxPlayers(int maxPlayers);

    void setWorldType(WorldType worldType);

    void setReduceDebugInfo(boolean reduceDebugInfo);
}