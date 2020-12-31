package org.screamingsandals.lib.sponge.player;

import org.screamingsandals.lib.player.PlayerUtils;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SpongePlayerUtils extends PlayerUtils {
    public static void init() {
        PlayerUtils.init(SpongePlayerUtils::new);
    }

    public SpongePlayerUtils() {

        /* NOTE: Converter needs null, so don't blame me because you see orElse(null) */

        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> Sponge.getServer().getPlayer(playerWrapper.getUuid()).orElse(null));
    }

    @Override
    public void sendMessage0(PlayerWrapper playerWrapper, String message) {
        playerWrapper.as(Player.class).sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
    }
}
