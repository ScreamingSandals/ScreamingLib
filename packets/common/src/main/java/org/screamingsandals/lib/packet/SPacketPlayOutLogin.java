package org.screamingsandals.lib.packet;

import org.screamingsandals.lib.utils.Difficulty;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.WorldType;

public interface SPacketPlayOutLogin extends SPacket {

    SPacketPlayOutLogin setEntityId(int entityId);

    SPacketPlayOutLogin setGameMode(GameMode gameMode);

    SPacketPlayOutLogin setDimension(int dimension);

    SPacketPlayOutLogin setDifficulty(Difficulty difficulty);

    SPacketPlayOutLogin setMaxPlayers(int maxPlayers);

    SPacketPlayOutLogin setWorldType(WorldType worldType);

    SPacketPlayOutLogin setReduceDebugInfo(boolean reduceDebugInfo);
}
