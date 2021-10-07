package org.screamingsandals.lib.npc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.impl.AbstractTouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class NPCImpl extends AbstractTouchableVisual<NPC> implements NPC {
    private static final GameModeHolder GAME_MODE = GameModeHolder.of("SURVIVAL");

    private final int id;
    /**
     * hologram that displays the name of the entity
     */
    private final Hologram hologram;
    private final Component displayName;
    private final List<SClientboundPlayerInfoPacket.Property> properties;
    private NPCSkin skin;
    private volatile boolean shouldLookAtPlayer;
    private final List<MetadataItem> metadata;

    public NPCImpl(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location);

        if (!Server.isServerThread()) {
            try {
                this.id = EntityMapper.getNewEntityIdSynchronously().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.id = EntityMapper.getNewEntityId();
        }

        setTouchable(touchable);
        this.displayName = AdventureHelper.toComponent("[NPC] " + uuid.toString().replace("-", "").substring(0, 10));
        this.hologram = HologramManager.hologram(location.clone().add(0.0D, 1.5D, 0.0D));
        this.metadata = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.shouldLookAtPlayer = false;
    }

    @Nullable
    @Override
    public List<TextEntry> getDisplayName() {
        return List.copyOf(hologram.getLines().values());
    }

    @Override
    public NPCSkin getSKin() {
        return skin;
    }

    @Override
    public NPC setDisplayName(List<Component> name) {
        for (int i = 0; i < name.size(); i++) {
            hologram.replaceLine(i, name.get(i));
        }
        return this;
    }

    @Override
    public int getEntityId() {
        return id;
    }

    @Override
    public NPC setSkin(NPCSkin skin) {
        final var playerInfoPacket = new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData());

        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(getFullDestroyPacket()::sendPacket);

        this.skin = skin;
        properties.removeIf(property -> property.name().equals("textures"));
        if (skin == null) {
            return this;
        }
        properties.add(new SClientboundPlayerInfoPacket.Property("textures", skin.getValue(), skin.getSignature()));

        playerInfoPacket.action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER);
        playerInfoPacket.data(getNPCInfoData());
        getViewers().forEach(playerInfoPacket::sendPacket);
        getViewers().forEach(this::sendSpawnPackets);

        Tasker.build(() -> {
            playerInfoPacket.action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER);
            playerInfoPacket.data(getNPCInfoData());
            getViewers().forEach(playerInfoPacket::sendPacket);
        }).delay(6L, TaskerTime.SECONDS).start();
        return this;
    }

    @Override
    public void lookAtPlayer(LocationHolder location, PlayerWrapper player) {
        final var direction = getLocation().setDirection(player.getLocation().subtract(getLocation()).toVector());
        new SClientboundMoveEntityPacket.Rot()
                .entityId(getEntityId())
                .yaw((byte) (direction.getYaw() * 256.0F / 360.0F))
                .pitch((byte) (direction.getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .sendPacket(player);

        new SClientboundRotateHeadPacket()
                .entityId(getEntityId())
                .headYaw(direction.getYaw())
                .sendPacket(player);
    }

    @Override
    public Component getName() {
        return displayName;
    }

    @Override
    public NPC setShouldLookAtPlayer(boolean shouldLook) {
        this.shouldLookAtPlayer = shouldLook;
        return this;
    }

    @Override
    public boolean shouldLookAtPlayer() {
        return shouldLookAtPlayer;
    }

    @Override
    public Hologram getHologram() {
        return hologram;
    }

    @Override
    public boolean hasId(int entityId) {
        return id == entityId;
    }

    @Override
    public NPC update() {
        if (isCreated()) {
            // TODO: update
        }
        return this;
    }

    @Override
    public NPC show() {
        if (visible) {
            return this;
        }
        visible = true;
        hologram.show();
        viewers.forEach(viewer -> onViewerAdded(viewer, false));
        return this;
    }

    @Override
    public NPC hide() {
        if (!visible) {
            return this;
        }
        visible = false;
        hologram.hide();
        viewers.forEach(viewer -> onViewerRemoved(viewer, false));
        return this;
    }

    @Override
    public NPC title(Component title) {
        hologram.title(title);
        return this;
    }

    @Override
    public NPC title(ComponentLike title) {
        hologram.title(title);
        return null;
    }

    @Override
    public void onViewerAdded(PlayerWrapper player, boolean checkDistance) {
        if (!player.isOnline()) {
            return;
        }

        hologram.onViewerAdded(player, checkDistance);
        sendSpawnPackets(player);
    }

    @Override
    public void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {
        if (!player.isOnline()) {
            return;
        }
        hologram.onViewerRemoved(player, checkDistance);
        getFullDestroyPacket().sendPacket(player);
        new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                .data(getNPCInfoData())
                .sendPacket(player);
    }

    @Override
    protected void update0() {

    }

    private void sendSpawnPackets(PlayerWrapper player) {
        new SClientboundSetPlayerTeamPacket()
                .teamKey(AdventureHelper.toLegacy(displayName))
                .mode(SClientboundSetPlayerTeamPacket.Mode.CREATE)
                .displayName(displayName)
                .collisionRule(SClientboundSetPlayerTeamPacket.CollisionRule.ALWAYS)
                .tagVisibility(SClientboundSetPlayerTeamPacket.TagVisibility.NEVER)
                .teamColor(NamedTextColor.BLACK)
                .teamPrefix(Component.empty())
                .teamSuffix(Component.empty())
                .friendlyFire(false)
                .seeInvisible(true)
                .entities(Collections.singletonList(AdventureHelper.toLegacy(displayName)))
                .sendPacket(player);

        new SClientboundPlayerInfoPacket()
                .action(SClientboundPlayerInfoPacket.Action.ADD_PLAYER)
                .data(getNPCInfoData())
                .sendPacket(player);

        // protocol < 550: sending metadata as part of AddPlayerPacket; protocol >= 550, packet lib will split this packet into to as supposed to be
        new SClientboundAddPlayerPacket()
                .entityId(id)
                .uuid(getUuid())
                .location(getLocation())
                .metadata(metadata)
                .sendPacket(player);

        Tasker.build(() -> {
            //remove npc from TabList
            new SClientboundPlayerInfoPacket()
                    .action(SClientboundPlayerInfoPacket.Action.REMOVE_PLAYER)
                    .data(getNPCInfoData())
                    .sendPacket(player);
        }).delay(6L, TaskerTime.SECONDS).start();

        new SClientboundMoveEntityPacket.Rot()
                .entityId(getEntityId())
                .yaw((byte) (getLocation().getYaw() * 256.0F / 360.0F))
                .pitch((byte) (getLocation().getPitch() * 256.0F / 360.0F))
                .onGround(true)
                .sendPacket(player);

        new SClientboundRotateHeadPacket()
                .entityId(getEntityId())
                .headYaw(getLocation().getYaw())
                .sendPacket(player);
    }

    private SClientboundRemoveEntitiesPacket getFullDestroyPacket() {
        return new SClientboundRemoveEntitiesPacket()
                .entityIds(new int[] { getEntityId() });
    }

    private List<SClientboundPlayerInfoPacket.PlayerInfoData> getNPCInfoData() {
        return Collections.singletonList(new SClientboundPlayerInfoPacket.PlayerInfoData(
                getUuid(),
                AdventureHelper.toLegacy(displayName),
                1,
                GAME_MODE,
                displayName,
                List.copyOf(properties)
        ));
    }
}
