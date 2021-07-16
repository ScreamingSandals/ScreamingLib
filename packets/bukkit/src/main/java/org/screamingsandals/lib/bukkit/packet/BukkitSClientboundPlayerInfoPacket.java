package org.screamingsandals.lib.bukkit.packet;

import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.*;
import org.screamingsandals.lib.packet.SClientboundPlayerInfoPacket;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BukkitSClientboundPlayerInfoPacket extends BukkitSPacket implements SClientboundPlayerInfoPacket {

    public BukkitSClientboundPlayerInfoPacket() {
        super(ClientboundPlayerInfoPacketAccessor.getType());
    }

    @Override
    public SClientboundPlayerInfoPacket setAction(Action action) {
        if (action == null) {
            throw new UnsupportedOperationException("Action cannot be null!");
        }

        var nmsAction = Reflect.findEnumConstant(ClientboundPlayerInfoPacket_i_ActionAccessor.getType(), action.name().toUpperCase());
        packet.setField(ClientboundPlayerInfoPacketAccessor.getFieldAction(), nmsAction);
        return this;
    }

    @Override
    public SClientboundPlayerInfoPacket setPlayersData(List<PlayerInfoData> data) {
        final var nmsData = new ArrayList<>();
        data.forEach(playerInfoData -> {
            Object gameMode;

            if (GameTypeAccessor.getType() != null) {
                gameMode = Reflect.findEnumConstant(
                        GameTypeAccessor.getType(),
                        playerInfoData.getGameMode().name().toUpperCase()
                );
            } else {
                gameMode = Reflect.findEnumConstant(
                        WorldSettings_i_EnumGamemodeAccessor.getType(),
                        playerInfoData.getGameMode().name().toUpperCase()
                );
            }

            final var atomicRef = new AtomicReference<>();

            Reflect.constructor(
                    ClientboundPlayerInfoPacket_i_PlayerUpdateAccessor.getType(),
                    ClientboundPlayerInfoPacketAccessor.getType(),
                    ClassStorage.NMS.GameProfile,
                    int.class,
                    GameTypeAccessor.getType(),
                    ComponentAccessor.getType())
                    .ifPresentOrElse(constructor -> atomicRef.set(constructor.construct(packet.raw(), playerInfoData.getGameProfile(), playerInfoData.getLatency(), gameMode, ClassStorage.asMinecraftComponent(playerInfoData.getDisplayName()))), () -> {
                        atomicRef.set(Reflect
                                .constructor(ClientboundPlayerInfoPacket_i_PlayerUpdateAccessor.getType(), ClassStorage.NMS.GameProfile, int.class, GameTypeAccessor.getType(), ComponentAccessor.getType())
                                .construct(playerInfoData.getGameProfile(), playerInfoData.getLatency(), gameMode, ClassStorage.asMinecraftComponent(playerInfoData.getDisplayName())));
                    });

            nmsData.add(atomicRef.get());
        });
        packet.setField(ClientboundPlayerInfoPacketAccessor.getFieldEntries(), nmsData);
        return this;
    }
}
