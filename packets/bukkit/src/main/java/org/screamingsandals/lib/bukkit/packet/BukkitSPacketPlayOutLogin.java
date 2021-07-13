package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.packet.SPacketPlayOutLogin;
import org.screamingsandals.lib.utils.Difficulty;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.WorldType;
import org.screamingsandals.lib.utils.reflect.Reflect;

// TODO: legacy support
public class BukkitSPacketPlayOutLogin extends BukkitSPacket implements SPacketPlayOutLogin {

    public BukkitSPacketPlayOutLogin() {
        super(ClassStorage.NMS.PacketPlayOutLogin);
    }

    @Override
    public SPacketPlayOutLogin setEntityId(int entityId) {
        if (Version.isVersion(1, 17)) {
            packet.setField("b,f_132360_", entityId);
        } else {
            packet.setField("a,field_149206_a", entityId);
        }
        return this;
    }

    @Override
    public SPacketPlayOutLogin setGameMode(GameMode gameMode) {
        if (gameMode == null) {
            throw new UnsupportedOperationException("GameMode cannot be null!");
        }
        if (Version.isVersion(1, 17)) {
            packet.setField("e,f_132363_", Reflect.findEnumConstant(ClassStorage.NMS.EnumGamemode, gameMode.name().toUpperCase()));
        } else {
            packet.setField("d,field_149205_c", Reflect.findEnumConstant(ClassStorage.NMS.EnumGamemode, gameMode.name().toUpperCase()));
        }
        return this;
    }

    //TODO: 1.17 support
    @Override
    public SPacketPlayOutLogin setDimension(int dimension) {
        packet.setField("c", dimension);
        return this;
    }

    @Override
    public SPacketPlayOutLogin setDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            throw new UnsupportedOperationException("Difficulty cannot be null!");
        }
        packet.setField("c", Reflect.findEnumConstant(ClassStorage.NMS.EnumDifficulty, difficulty.name().toUpperCase()));
        return this;
    }

    @Override
    public SPacketPlayOutLogin setMaxPlayers(int maxPlayers) {
        packet.setField("d", maxPlayers);
        return this;
    }

    @Override
    public SPacketPlayOutLogin setWorldType(WorldType worldType) {
        if (worldType == null) {
            throw new UnsupportedOperationException("WorldType cannot be null!");
        }
        var nmsWorldType = Reflect.getField(ClassStorage.NMS.WorldType, worldType.getName().toUpperCase());
        if (nmsWorldType == null) {
            throw new UnsupportedOperationException("Failed to find NMS WorldType for type: " + worldType.getName());
        }
        return this;
    }

    @Override
    public SPacketPlayOutLogin setReduceDebugInfo(boolean reduceDebugInfo) {
        packet.setField("h", reduceDebugInfo);
        return this;
    }
}
