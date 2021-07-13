package org.screamingsandals.lib.bukkit.packet;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.packet.SPacketPlayOutPlayerInfo;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BukkitSPacketPlayOutPlayerInfo extends BukkitSPacket implements SPacketPlayOutPlayerInfo {

    public BukkitSPacketPlayOutPlayerInfo() {
        super(ClassStorage.NMS.PacketPlayOutPlayerInfo);
    }

    @Override
    public SPacketPlayOutPlayerInfo setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }
        var nmsAction = Reflect.findEnumConstant(ClassStorage.NMS.EnumPlayerInfoAction, action.name().toUpperCase());
        packet.setField("a,field_179770_a,f_132717_", nmsAction);
        return this;
    }

    @Override
    public SPacketPlayOutPlayerInfo setPlayersData(List<PlayerInfoData> data) {
        final var nmsData = new ArrayList<>();
        data.forEach(playerInfoData -> {
            var gameMode = Reflect.findEnumConstant(
                    ClassStorage.NMS.EnumGamemode,
                    playerInfoData.getGameMode().name().toUpperCase()
            );

            final var atomicRef = new AtomicReference<>();

            Reflect.constructor(ClassStorage.NMS.PlayerInfoData, ClassStorage.NMS.PacketPlayOutPlayerInfo, ClassStorage.NMS.GameProfile, int.class, ClassStorage.NMS.EnumGamemode, ClassStorage.NMS.IChatBaseComponent)
                    .ifPresentOrElse(constructor -> atomicRef.set(constructor.construct(packet.raw(), playerInfoData.getGameProfile(), playerInfoData.getLatency(), gameMode, ClassStorage.asMinecraftComponent(playerInfoData.getDisplayName()))), () -> {
                            atomicRef.set(Reflect
                                    .constructor(ClassStorage.NMS.PlayerInfoData, ClassStorage.NMS.GameProfile, int.class, ClassStorage.NMS.EnumGamemode, ClassStorage.NMS.IChatBaseComponent)
                                                .construct(playerInfoData.getGameProfile(), playerInfoData.getLatency(), gameMode, ClassStorage.asMinecraftComponent(playerInfoData.getDisplayName())));
                    });

            nmsData.add(atomicRef.get());
        });
        packet.setField("b,field_179769_b,f_132718_", nmsData);
        return this;
    }
}
