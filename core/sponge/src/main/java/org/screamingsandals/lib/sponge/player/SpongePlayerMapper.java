package org.screamingsandals.lib.sponge.player;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.SystemSubject;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class SpongePlayerMapper extends PlayerMapper {

    public static void init() {
        PlayerMapper.init(SpongePlayerMapper::new);
    }

    public SpongePlayerMapper() {
        offlinePlayerConverter
                .registerP2W(User.class, user -> new FinalOfflinePlayerWrapper(user.getUniqueId(), user.getName()))
                .registerP2W(ServerPlayer.class, serverPlayer -> offlinePlayerConverter.convert(serverPlayer.getUser()))
                .registerP2W(GameProfile.class, gameProfile -> new FinalOfflinePlayerWrapper(gameProfile.getUniqueId(), gameProfile.getName().orElse(null)))
                .registerW2P(User.class, offlinePlayerWrapper ->
                        Sponge.getServer().getUserManager().get(offlinePlayerConverter.convert(offlinePlayerWrapper, GameProfile.class)).orElse(null)
                )
                .registerW2P(ServerPlayer.class, offlinePlayerWrapper -> Sponge.getServer().getPlayer(offlinePlayerWrapper.getUuid()).orElse(null))
                .registerW2P(GameProfile.class, offlinePlayerWrapper -> {
                    try {
                        return Sponge.getServer().getGameProfileManager().getProfile(offlinePlayerWrapper.getUuid()).get();
                    } catch (InterruptedException | ExecutionException ignored) {
                    }
                    return null;
                });
        playerConverter
                .registerP2W(ServerPlayer.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(ServerPlayer.class, playerWrapper -> Sponge.getServer().getPlayer(playerWrapper.getUuid()).orElse(null))
                .registerP2W(EntityHuman.class, entityHuman -> playerConverter.convert(entityHuman.as(ServerPlayer.class)))
                .registerW2P(EntityHuman.class, playerWrapper -> EntityMapper.<EntityHuman>wrapEntity(playerWrapper.as(ServerPlayer.class)).orElse(null));
        senderConverter
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
                        final var player = Sponge.getServer().getPlayer(wrapper.getName()).orElse(null);
                        if (player == null) {
                            return null;
                        }
                        return PlayerMapper.wrapPlayer(player);
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
        playerWrapper.as(ServerPlayer.class).sendMessage(AdventureHelper.toComponent(message));
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
    public CommandSenderWrapper getConsoleSender0() {
        return senderConverter.convert(Sponge.getSystemSubject());
    }

    @Override
    public List<PlayerWrapper> getPlayersOnServer0() {
        return Sponge.getServer().getOnlinePlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerWrapper> getPlayers0(WorldHolder holder) {
        return holder.as(ServerWorld.class).getPlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void closeInventory0(PlayerWrapper playerWrapper) {
        playerWrapper.as(ServerPlayer.class).closeInventory();
    }

    @Override
    public Container getEnderChest0(PlayerWrapper playerWrapper) {
        //TODO:
        return null;
    }

    @Override
    public PlayerContainer getPlayerInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.<PlayerContainer>wrapContainer(playerWrapper.as(ServerPlayer.class).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(ServerPlayer.class).getOpenInventory().orElse(null));
    }

    @Override
    public LocationHolder getLocation0(PlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(ServerPlayer.class).getLocation()).orElseThrow();
    }

    @Override
    public Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper) {
        return Optional.empty(); // TODO
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
    public void kick0(PlayerWrapper wrapper, Component message) {
        wrapper.as(ServerPlayer.class).kick(message);
    }

    @Override
    public Audience getAudience0(CommandSenderWrapper wrapper) {
        if (wrapper.getType() == SenderWrapper.Type.PLAYER) {
            return Sponge.getServer().getPlayer(wrapper.as(PlayerWrapper.class).getName()).orElseThrow();
        }
        return Sponge.getSystemSubject();
    }

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(Subject.class).hasPermission(((SimplePermission) permission).getPermissionString());
            } else {
                return ((SimplePermission) permission).isDefaultAllowed();
            }
        } else if (permission instanceof AndPermission) {
            return ((AndPermission) permission).getPermissions().stream().allMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof OrPermission) {
            return ((OrPermission) permission).getPermissions().stream().anyMatch(permission1 -> hasPermission0(wrapper, permission1));
        } else if (permission instanceof PredicatePermission) {
            return permission.hasPermission(wrapper);
        }
        return false;
    }

    @Override
    public boolean isPermissionSet0(CommandSenderWrapper wrapper, Permission permission) {
        if (permission instanceof SimplePermission) {
            var subject = wrapper.as(Subject.class);
            return subject.getPermissionValue(subject.getActiveContexts(), ((SimplePermission) permission).getPermissionString()) != Tristate.UNDEFINED;
        }
        return true;
    }

    @Override
    public boolean isOp0(Operator wrapper) {
        return false; // TODO: check if op exists in Sponge
    }

    @Override
    public void setOp0(Operator wrapper, boolean op) {
        // TODO: check if op exists in Sponge
    }

    @Override
    public long getFirstPlayed0(OfflinePlayerWrapper playerWrapper) {
        return 0; // TODO
    }

    @Override
    public long getLastPlayed0(OfflinePlayerWrapper playerWrapper) {
        return 0; // TODO
    }

    @Override
    public boolean isBanned0(OfflinePlayerWrapper playerWrapper) {
        return false; // TODO
    }

    @Override
    public boolean isWhitelisted0(OfflinePlayerWrapper playerWrapper) {
        return false; // TODO
    }

    @Override
    public boolean isOnline0(OfflinePlayerWrapper playerWrapper) {
        return Sponge.getServer().getPlayer(playerWrapper.getUuid()).isPresent();
    }

    @Override
    public void setWhitelisted0(OfflinePlayerWrapper playerWrapper, boolean whitelisted) {
        // TODO
    }

    @Override
    public OfflinePlayerWrapper getOfflinePlayer0(UUID uuid) {
        return Sponge.getServer().getUserManager().get(uuid)
                .map(user -> new FinalOfflinePlayerWrapper(user.getUniqueId(), user.getName()))
                .orElseGet(() -> new FinalOfflinePlayerWrapper(uuid, null));
    }

    @Override
    public Locale getLocale0(SenderWrapper senderWrapper) {
        return Locale.US; // TODO
    }

    @Override
    public GameMode getGameMode0(PlayerWrapper player) {
        //TODO
        return null;
    }

    @Override
    public void setGameMode0(PlayerWrapper player, GameMode gameMode) {
        //TODO
    }

    @Override
    public boolean canBeStoredAsWrapped(Object wrapped) {
        return false;
    }
}
