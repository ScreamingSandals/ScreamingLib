package org.screamingsandals.lib.bukkit.player;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.bukkit.player.listener.*;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.builder.ItemFactory;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.PlayerContainer;
import org.screamingsandals.lib.player.*;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.sender.Operator;
import org.screamingsandals.lib.sender.permissions.*;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(dependsOn = {
        EventManager.class
})
public class BukkitPlayerMapper extends PlayerMapper {
    public static void init(Plugin plugin, Controllable controllable) {
        PlayerMapper.init(() -> new BukkitPlayerMapper(plugin, controllable));
    }

    public BukkitPlayerMapper(Plugin plugin, Controllable controllable) {
        controllable.enable(() -> {
            provider = BukkitAudiences.create(plugin);
            registerListeners(plugin);
        });

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
                .registerW2P(EntityHuman.class, playerWrapper -> EntityMapper.<EntityHuman>wrapEntity(playerWrapper.as(Player.class)).orElse(null));
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
    public void teleport0(PlayerWrapper wrapper, LocationHolder location, Runnable callback) {
        PaperLib.teleportAsync(wrapper.as(Player.class), location.as(Location.class))
                .thenAccept(result -> {
                    if (result) {
                        callback.run();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
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
    public GameMode getGameMode0(PlayerWrapper player) {
        return GameMode.convert(player.as(Player.class).getGameMode().name());
    }

    @Override
    public void setGameMode0(PlayerWrapper player, GameMode gameMode) {
        player.as(Player.class).setGameMode(org.bukkit.GameMode.valueOf(gameMode.name()));
    }

    @Override
    public boolean canBeStoredAsWrapped(Object wrapped) {
        return wrapped instanceof Player;
    }

    private void registerListeners(Plugin plugin) {
        new AsyncPlayerPreLoginEventListener(plugin);
        new AsyncPlayerChatEventListener(plugin);
        new PlayerJoinEventListener(plugin);
        new PlayerLeaveEventListener(plugin);
        new PlayerBlockPlaceEventListener(plugin);
        new PlayerBlockBreakEventListener(plugin);
        new PlayerMoveEventListener(plugin);
        new PlayerTeleportEventListener(plugin);
        if (Reflect.has("org.bukkit.event.entity.EntityPickupItemEvent")) {
            new PlayerPickupItemListener(plugin);
        } else {
            new LegacyPlayerPickupItemListener(plugin);
        }
        new PlayerChangeWorldEventListener(plugin);
        new PlayerRightClickedEntityListener(plugin);
        new PlayerClickedBlockListener(plugin);
        new PlayerSignChangeEventListener(plugin);
        new PlayerDeathEventListener(plugin);
        new PlayerRespawnEventListener(plugin);
        new PlayerCommandPreprocessEventListener(plugin);
        new PlayerInventoryClickEventListener(plugin);
        new PlayerFoodLevelChangeListener(plugin);
        new PlayerCraftItemEventListener(plugin);
        new PlayerDamageEventListener(plugin);
        new PlayerProjectileLaunchEventListener(plugin);
        new PlayerDropItemEventListener(plugin);
        new PlayerBedEnterEventListener(plugin);
        new PlayerAnimationEventListener(plugin);
        new PlayerInteractEntityEventListener(plugin);
        new PlayerArmorStandManipulateEventListener(plugin);
        new PlayerBedLeaveEventListener(plugin);
        new PlayerBucketEventListener(plugin);
        new PlayerCommandSendEventListener(plugin);
        new PlayerEggThrowEventListener(plugin);
        new PlayerExpChangeEventListener(plugin);
        new PlayerFishEventListener(plugin);
        new PlayerGameModeChangeEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerHarvestBlockEvent"))
            new PlayerHarvestBlockEventListener(plugin);
        new PlayerInteractEventListener(plugin);
        new PlayerItemConsumeEventListener(plugin);
        new PlayerItemDamageEventListener(plugin);
        new PlayerItemHeldEventListener(plugin);
        new PlayerItemMendEventListener(plugin);
        new PlayerKickEventListener(plugin);
        new PlayerLevelChangeEventListener(plugin);
        new PlayerLocaleChangeEventListener(plugin);
        new PlayerLoginEventListener(plugin);
        new PlayerPortalEventListener(plugin);
        new PlayerShearEntityEventListener(plugin);
        if (Reflect.has("org.bukkit.event.player.PlayerSwapHandItemsEvent"))
            new PlayerSwapHandItemsEventListener(plugin);
        new PlayerToggleFlightEventListener(plugin);
        new PlayerToggleSneakEventListener(plugin);
        new PlayerToggleSprintEventListener(plugin);
        new PlayerUnleashEntityEventListener(plugin);
        new PlayerVelocityEventListener(plugin);
    }
}
