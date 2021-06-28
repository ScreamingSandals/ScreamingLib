package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutPlayerInfo;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;

public class BukkitSPacketPlayOutPlayerInfo extends BukkitSPacket implements SPacketPlayOutPlayerInfo {
    public BukkitSPacketPlayOutPlayerInfo(Class<?> packetClass) {
        super(packetClass);
    }

    @Override
    public void setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        var nmsAction = Reflect.findEnumConstant(ClassStorage.NMS.EnumPlayerInfoAction, action.name().toUpperCase());
        packet.setField("a,field_179770_a", nmsAction);
    }

    @Override
    public void setPlayersData(List<PlayerInfoData> data) {
        final var nmsData = new ArrayList<>();
        data.forEach(playerInfoData -> {
            var constructed = Reflect.constructor(
                    ClassStorage.NMS.PlayerInfoData,
                    ClassStorage.NMS.GameProfile,
                    int.class,
                    ClassStorage.NMS.EnumGamemode,
                    ClassStorage.NMS.IChatBaseComponent
                    ).construct(
                        playerInfoData.getGameProfile(),
                        playerInfoData.getLatency(),
                        Reflect.findEnumConstant(
                                ClassStorage.NMS.EnumGamemode,
                                playerInfoData.getGameMode().name().toUpperCase()
                        ), ClassStorage.asMinecraftComponent(playerInfoData.getDisplayName())
            );
            nmsData.add(constructed);
        });
        packet.setField("b,field_179769_b", nmsData);
    }
}
