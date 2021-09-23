package org.screamingsandals.lib.bukkit.player;

import io.netty.channel.Channel;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.particle.BukkitParticleConverter;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.item.builder.ItemFactory;
import org.screamingsandals.lib.nms.accessors.ConnectionAccessor;
import org.screamingsandals.lib.nms.accessors.ServerGamePacketListenerImplAccessor;
import org.screamingsandals.lib.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.adventure.AdventureUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnEnable;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BukkitPlayerMapper extends PlayerMapper {
    public BukkitPlayerMapper() {
        offlinePlayerConverter
                .registerP2W(OfflinePlayer.class, offlinePlayer -> new FinalOfflinePlayerWrapper(offlinePlayer.getUniqueId(), offlinePlayer.getName()))
                .registerP2W(PlayerWrapper.class, playerWrapper -> new FinalOfflinePlayerWrapper(playerWrapper.getUuid(), playerWrapper.getName()))
                .registerW2P(OfflinePlayer.class, offlinePlayerWrapper -> Bukkit.getOfflinePlayer(offlinePlayerWrapper.getUuid()))
                .registerW2P(PlayerWrapper.class, offlinePlayerWrapper -> getPlayer0(offlinePlayerWrapper.getUuid()).orElse(null));
        playerConverter
                .registerP2W(Player.class, player -> new PlayerWrapper(player.getName(), player.getUniqueId()))
                .registerW2P(Player.class, playerWrapper -> {
                    if (playerWrapper.getWrappedPlayer() != null) {
                        final var maybePlayer = playerWrapper.getWrappedPlayer().get();
                        if (maybePlayer != null) {
                            return (Player) maybePlayer;
                        }
                    }

                    return Bukkit.getPlayer(playerWrapper.getUuid());
                })
                .registerP2W(EntityHuman.class, entityHuman -> playerConverter.convert(entityHuman.as(Player.class)))
                .registerW2P(EntityHuman.class, playerWrapper -> EntityMapper.<EntityHuman>wrapEntity(playerWrapper.as(Player.class)).orElse(null))
                .registerW2P(OfflinePlayer.class, playerWrapper -> Bukkit.getOfflinePlayer(playerWrapper.getUuid()));
        senderConverter
                .registerW2P(Player.class, wrapper -> {
                    if (wrapper.getType() != CommandSenderWrapper.Type.PLAYER) {
                        return null;
                    }

                    if (wrapper instanceof PlayerWrapper) {
                        final var wrapped = ((PlayerWrapper) wrapper).getWrappedPlayer().get();
                        if (wrapped != null) {
                            return (Player) wrapped;
                        }
                    }

                    return Bukkit.getPlayer(wrapper.getName());
                })
                .registerP2W(Player.class, player -> new SenderWrapper(player.getName(), CommandSenderWrapper.Type.PLAYER))
                .registerW2P(PlayerWrapper.class, wrapper -> {
                    if (wrapper.getType() == CommandSenderWrapper.Type.PLAYER) {
                        final var player = Bukkit.getPlayer(wrapper.getName());
                        if (player == null) {
                            return null;
                        }
                        return PlayerMapper.wrapPlayer(player);
                    }
                    return null;
                })
                .registerP2W(PlayerWrapper.class, wrapper -> wrapper)
                .registerW2P(CommandSender.class, wrapper -> {
                    switch (wrapper.getType()) {
                        case PLAYER:
                            return Bukkit.getPlayer(wrapper.getName());
                        case CONSOLE:
                            return Bukkit.getConsoleSender();
                        default:
                            return null;
                    }
                })
                .registerP2W(CommandSender.class, sender -> {
                    if (sender instanceof Player) {
                        return PlayerMapper.wrapPlayer(sender);
                    }
                    return new SenderWrapper(sender.getName(), CommandSenderWrapper.Type.CONSOLE);
                });
        handConverter
                .registerW2P(EquipmentSlot.class, wrapper -> {
                    if (wrapper == PlayerWrapper.Hand.OFF) {
                        return EquipmentSlot.OFF_HAND;
                    }
                    return EquipmentSlot.HAND;
                })
                .registerP2W(EquipmentSlot.class, hand -> {
                    if (hand == EquipmentSlot.OFF_HAND) {
                        return PlayerWrapper.Hand.OFF;
                    }
                    return PlayerWrapper.Hand.MAIN;
                });
    }

    @OnEnable
    public void onEnable(Plugin plugin) {
        provider = BukkitAudiences.create(plugin);
    }

    @Override
    public void sendMessage0(SenderWrapper wrapper, String message) {
        wrapper.as(CommandSender.class).sendMessage(message);
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(String name) {
        return playerConverter.convertOptional(Bukkit.getPlayer(name));
    }

    @Override
    public Optional<PlayerWrapper> getPlayer0(UUID uuid) {
        return playerConverter.convertOptional(Bukkit.getPlayer(uuid));
    }

    @Override
    public CommandSenderWrapper getConsoleSender0() {
        return senderConverter.convert(Bukkit.getConsoleSender());
    }

    @Override
    public List<PlayerWrapper> getPlayersOnServer0() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayerWrapper> getPlayers0(WorldHolder holder) {
        return holder.as(World.class).getPlayers()
                .stream()
                .map(playerConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void closeInventory0(PlayerWrapper playerWrapper) {
        playerWrapper.as(Player.class).closeInventory();
    }

    @Override
    public Container getEnderChest0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getEnderChest()).orElseThrow();
    }

    @Override
    public PlayerContainer getPlayerInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.<PlayerContainer>wrapContainer(playerWrapper.as(Player.class).getInventory()).orElseThrow();
    }

    @Override
    public Optional<Container> getOpenedInventory0(PlayerWrapper playerWrapper) {
        return ItemFactory.wrapContainer(playerWrapper.as(Player.class).getOpenInventory().getTopInventory());
    }

    @Override
    public LocationHolder getLocation0(PlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(Player.class).getLocation()).orElseThrow();
    }

    @Override
    public Optional<LocationHolder> getBedLocation0(OfflinePlayerWrapper playerWrapper) {
        return LocationMapper.resolve(playerWrapper.as(OfflinePlayer.class).getBedSpawnLocation());
    }

    @Override
    public CompletableFuture<Void> teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback, boolean forceCallback) {
        return teleport0(wrapper, location)
                .thenAccept(result -> {
                    if (result || forceCallback) {
                        callback.run();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    @Override
    public CompletableFuture<Boolean> teleport0(PlayerWrapper wrapper, LocationHolder location) {
        return PaperLib.teleportAsync(wrapper.as(Player.class), location.as(Location.class));
    }

    @Override
    public void kick0(PlayerWrapper wrapper, Component message) {
        wrapper.as(Player.class).kickPlayer(AdventureHelper.toLegacy(message));
    }

    @Override
    public Audience getAudience0(CommandSenderWrapper wrapper) {
        final var audiences = (BukkitAudiences) provider;
        if (wrapper.getType() == CommandSenderWrapper.Type.CONSOLE) {
            return audiences.console();
        }

        return audiences.player(wrapper.as(Player.class).getUniqueId());
    }

    @Override
    public boolean hasPermission0(CommandSenderWrapper wrapper, Permission permission) {
        if (isOp0(wrapper)) {
            return true;
        }

        if (permission instanceof SimplePermission) {
            if (isPermissionSet0(wrapper, permission)) {
                return wrapper.as(CommandSender.class).hasPermission(((SimplePermission) permission).getPermissionString());
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
            return wrapper.as(CommandSender.class).isPermissionSet(((SimplePermission) permission).getPermissionString());
        }
        return true;
    }

    @Override
    public boolean isOp0(Operator wrapper) {
        return wrapper.as(CommandSender.class).isOp();
    }

    @Override
    public void setOp0(Operator wrapper, boolean op) {
        wrapper.as(CommandSender.class).setOp(op);
    }

    @Override
    public long getFirstPlayed0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getFirstPlayed();
    }

    @Override
    public long getLastPlayed0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).getLastPlayed();
    }

    @Override
    public boolean isBanned0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isBanned();
    }

    @Override
    public boolean isWhitelisted0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isWhitelisted();
    }

    @Override
    public boolean isOnline0(OfflinePlayerWrapper playerWrapper) {
        return playerWrapper.as(OfflinePlayer.class).isOnline();
    }

    @Override
    public void setWhitelisted0(OfflinePlayerWrapper playerWrapper, boolean whitelisted) {
        playerWrapper.as(OfflinePlayer.class).setWhitelisted(whitelisted);
    }

    @Override
    public OfflinePlayerWrapper getOfflinePlayer0(UUID uuid) {
        return offlinePlayerConverter.convert(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public Locale getLocale0(SenderWrapper senderWrapper) {
        return senderWrapper.asOptional(Player.class)
                .map(player -> {
                    if (Reflect.hasMethod(player, "getLocale")) {
                        return player.getLocale();
                    }
                    return null;
                })
                .map(s -> {
                    try {
                        return LocaleUtils.toLocale(s);
                    } catch (IllegalArgumentException ignored) {
                        return null;
                    }
                })
                .orElse(Locale.US);
    }

    @Override
    public GameModeHolder getGameMode0(PlayerWrapper player) {
        return GameModeHolder.of(player.as(Player.class).getGameMode());
    }

    @Override
    public void setGameMode0(PlayerWrapper player, GameModeHolder gameMode) {
        player.as(Player.class).setGameMode(gameMode.as(org.bukkit.GameMode.class));
    }

    @Override
    public Component getDisplayName0(PlayerWrapper player) {
        var bukkitPlayer = player.as(Player.class);
        return AdventureUtils
                .get(bukkitPlayer, "displayName")
                .ifPresentOrElseGet(classMethod ->
                                classMethod.invokeInstanceResulted(bukkitPlayer).as(Component.class),
                        () -> AdventureHelper.toComponent(bukkitPlayer.getDisplayName()));
    }

    @Override
    public void setDisplayName0(PlayerWrapper player, Component component) {
        var bukkitPlayer = player.as(Player.class);
        AdventureUtils
                .get(bukkitPlayer, "displayName", Component.class)
                .ifPresentOrElse(classMethod -> classMethod.invokeInstance(bukkitPlayer, component),
                        () -> bukkitPlayer.setDisplayName(AdventureHelper.toLegacyNullableResult(component)));
    }

    @Override
    public boolean canBeStoredAsWrapped(Object wrapped) {
        return wrapped instanceof Player;
    }

    @Override
    public int getLevel0(PlayerWrapper player) {
        return player.as(Player.class).getLevel();
    }

    @Override
    public float getExp0(PlayerWrapper player) {
        return player.as(Player.class).getExp();
    }

    @Override
    public void setLevel0(PlayerWrapper player, int level) {
        player.as(Player.class).setLevel(level);
    }

    @Override
    public void setExp0(PlayerWrapper player, float exp) {
        player.as(Player.class).setExp(exp);
    }

    @Override
    public int getPing0(PlayerWrapper player) {
        final Object handle = ClassStorage.getHandle(player.as(Player.class));
        return (int) Objects.requireNonNullElse(Reflect.getField(handle, ServerPlayerAccessor.getFieldLatency()), 0);
    }

    @Override
    public boolean isSprinting0(PlayerWrapper player) {
        return player.as(Player.class).isSprinting();
    }

    @Override
    public void setSprinting0(PlayerWrapper player, boolean sprinting) {
        player.as(Player.class).setSprinting(sprinting);
    }

    @Override
    public Optional<WeatherHolder> getWeather0(PlayerWrapper player) {
        return WeatherHolder.ofOptional(player.as(Player.class).getPlayerWeather());
    }

    @Override
    public void setWeather0(PlayerWrapper player, @Nullable WeatherHolder weather) {
        if (weather == null) {
            player.as(Player.class).resetPlayerWeather();
        } else {
            player.as(Player.class).setPlayerWeather(weather.as(WeatherType.class));
        }
    }

    @Override
    public long getTime0(PlayerWrapper player) {
        return player.as(Player.class).getPlayerTime();
    }

    @Override
    public void setTime0(PlayerWrapper player, long time, boolean relative) {
        player.as(Player.class).setPlayerTime(time, relative);
    }

    @Override
    public void resetTime0(PlayerWrapper player) {
        player.as(Player.class).resetPlayerTime();
    }

    @Override
    public void sendParticle0(PlayerWrapper player, ParticleHolder particle, LocationHolder location) {
        player.as(Player.class).spawnParticle(
                particle.particleType().as(Particle.class),
                location.as(Location.class),
                particle.count(),
                particle.offset().getX(),
                particle.offset().getY(),
                particle.offset().getZ(),
                particle.particleData(),
                particle.specialData() != null ? BukkitParticleConverter.convertParticleData(particle.specialData()) : null
                // hey bukkit api, where's the last argument?
        );
    }

    @Override
    public Channel getChannel0(PlayerWrapper player) {
        return Reflect.getFieldResulted(ClassStorage.getHandle(player.as(Player.class)), ServerPlayerAccessor.getFieldConnection())
                .getFieldResulted(ServerGamePacketListenerImplAccessor.getFieldConnection())
                .getFieldResulted(ConnectionAccessor.getFieldChannel())
                .as(Channel.class);
    }
}
