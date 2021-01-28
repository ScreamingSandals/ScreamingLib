package org.screamingsandals.lib.sponge.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.world.server.ServerLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SpongePlayerMapper extends PlayerMapper {

    public static void init() {
        PlayerMapper.init(SpongePlayerMapper::new);
    }

    public SpongePlayerMapper() {
        playerConverter
                .registerP2W(ServerPlayer.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(ServerPlayer.class, playerWrapper -> Sponge.getServer().getPlayer(playerWrapper.getUuid()).orElse(null));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
                        final var player = Sponge.getServer().getPlayer(wrapper.getName()).orElse(null);
                        if (player == null) {
                            return null;
                        }
                        return new PlayerWrapper(player.getName(), player.getUniqueId());
                    }
                    return null;
                })
                .registerW2P(Subject.class, wrapper -> {
                    switch (wrapper.getType()) {
                        case PLAYER:
                            return Sponge.getServer().getPlayer(wrapper.getName()).orElse(null);
                        case CONSOLE:
                            return Sponge.getSystemSubject();
                        default:
                            return null;
                    }
                })
                .registerP2W(SystemSubject.class, sender -> new SenderWrapper(CONSOLE_NAME, SenderWrapper.Type.CONSOLE))
                .registerP2W(Subject.class, sender -> {
                    if (sender instanceof ServerPlayer) {
                        final var player = (ServerPlayer) sender;
                        return new SenderWrapper(player.getName(), SenderWrapper.Type.PLAYER);
                    }
                    return new SenderWrapper(CONSOLE_NAME, SenderWrapper.Type.CONSOLE);
                });

    }

    @Override
    public void sendMessage0(SenderWrapper playerWrapper, String message) {
        playerWrapper.as(ServerPlayer.class).sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return Sponge.getServer().getPlayer(name).map(playerConverter::convert);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return Sponge.getServer().getPlayer(uuid).map(playerConverter::convert);
    }

    @Override
    public SenderWrapper getConsoleSender0() {
        return senderConverter.convert(Sponge.getSystemSubject());
    }

    @Override
    public List<PlayerWrapper> getPlayers0() {
        return Sponge.getServer().getOnlinePlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
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

    @Override
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        if (wrapper.as(ServerPlayer.class).setLocation(location.as(ServerLocation.class))) {
            Sponge.getServer().getScheduler().submit(
                    Task.builder()
                            .execute(callback)
                            .build());
        }
    }

    @Override
    protected Audience getAudience(SenderWrapper wrapper, AudienceProvider provider) {
        if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
            return Sponge.getServer().getPlayer(wrapper.getName()).orElseThrow();
        }
        return Sponge.getSystemSubject();
    }
}
