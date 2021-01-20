package org.screamingsandals.lib.sponge.player;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Optional;

public class SpongePlayerUtils extends PlayerUtils {
    public static void init() {
        PlayerUtils.init(SpongePlayerUtils::new);
    }

    public SpongePlayerUtils() {
        /* NOTE: Converter needs null, so don't blame me because you see orElse(null) */
        playerConverter
                .registerP2W(ServerPlayer.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(ServerPlayer.class, playerWrapper -> Sponge.getServer().getPlayer(playerWrapper.getUuid()).orElse(null));
    }

    @Override
    public void sendMessage0(SenderWrapper playerWrapper, String message) {
        //TODO: fix this
        playerWrapper.as(ServerPlayer.class).sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public void closeInventory0(PlayerWrapper playerWrapper) {
        playerWrapper.as(ServerPlayer.class).closeInventory();
    }

    @Override
    public Container getPlayerInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(ServerPlayer.class).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(ServerPlayer.class).getOpenInventory().orElse(null));
    }

    @Override
    public LocationHolder getLocation0(PlayerWrapper playerWrapper) {
        return LocationMapping.resolve(playerWrapper.as(ServerPlayer.class).getLocation()).orElseThrow();
    }
}
