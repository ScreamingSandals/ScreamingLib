package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.packet.SPacketPlayOutLogin;
import org.screamingsandals.lib.utils.Difficulty;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.WorldType;
import org.screamingsandals.lib.utils.reflect.Reflect;

public class BukkitSPacketPlayOutLogin extends BukkitSPacket implements SPacketPlayOutLogin {
    public BukkitSPacketPlayOutLogin() {
        super(ClassStorage.NMS.PacketPlayOutLogin);
    }

    @Override
    public void setEntityId(int entityId) {
        packet.setField("a", entityId);
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        if (gameMode == null) {
            throw new UnsupportedOperationException("GameMode cannot be null!");
        }
        packet.setField("b", Reflect.findEnumConstant(ClassStorage.NMS.EnumGamemode, gameMode.name().toUpperCase()));
    }

    @Override
    public void setDimension(int dimension) {
        packet.setField("c", dimension);
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            throw new UnsupportedOperationException("Difficulty cannot be null!");
        }
        packet.setField("c", Reflect.findEnumConstant(ClassStorage.NMS.EnumDifficulty, difficulty.name().toUpperCase()));
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        packet.setField("d", maxPlayers);
    }

    @Override
    public void setWorldType(WorldType worldType) {
        if (worldType == null) {
            throw new UnsupportedOperationException("WorldType cannot be null!");
        }
        var nmsWorldType = Reflect.getField(ClassStorage.NMS.WorldType, worldType.getName().toUpperCase());
        if (nmsWorldType == null) {
            throw new UnsupportedOperationException("Failed to find NMS WorldType for type: " + worldType.getName());
        }
    }

    @Override
    public void setReduceDebugInfo(boolean reduceDebugInfo) {
        packet.setField("h", reduceDebugInfo);
    }
}
