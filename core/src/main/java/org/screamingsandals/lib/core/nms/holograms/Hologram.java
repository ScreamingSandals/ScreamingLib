package org.screamingsandals.lib.core.nms.holograms;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.core.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.core.nms.utils.ClassStorage;
import org.screamingsandals.lib.core.nms.utils.Version;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.screamingsandals.lib.core.nms.utils.ClassStorage.NMS.*;

@Data
public class Hologram {
    public static final int VISIBILITY_DISTANCE_SQUARED = 4096;

    private List<Player> viewers = new ArrayList<>();
    private List<String> lines = new ArrayList<>();
    private Location location;
    private List<ArmorStandNMS> entities = new ArrayList<>();
    private boolean touchable;
    private List<TouchHandler> handlers = new ArrayList<>();
    private HologramManager manager;

    public Hologram(HologramManager manager, List<Player> players, Location location, List<String> lines) {
        this(manager, players, location, lines, false);
    }

    public Hologram(HologramManager manager, List<Player> players, Location location, List<String> lines, boolean touchable) {
        this.manager = manager;
        this.lines.addAll(lines);
        this.location = location;
        this.touchable = touchable;

        updateEntities();
        addViewers(players);
    }

    public int getLinesCount() {
        return lines.size();
    }

    public boolean hasViewers() {
        return !viewers.isEmpty();
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public Hologram addHandler(TouchHandler handler) {
        if (handler != null) {
            handlers.add(handler);
        }
        return this;
    }

    public Hologram setHandler(TouchHandler handler) {
        handlers.clear();

        if (handler != null) {
            handlers.add(handler);
        }
        return this;
    }

    public Hologram addViewer(Player player) {
        return addViewers(List.of(player));
    }

    public Hologram addViewers(List<Player> players) {
        players.forEach(player -> {
            if (!viewers.contains(player)) {
                viewers.add(player);
                try {
                    update(player, getAllSpawnPackets(), true);
                } catch (Throwable ignored) {
                }
            }
        });
        return this;
    }

    public Hologram removeViewer(Player player) {
        return removeViewers(List.of(player));
    }

    public Hologram removeViewers(List<Player> players) {
        players.forEach(player -> {
            if (viewers.contains(player)) {
                viewers.remove(player);
                try {
                    update(player, List.of(getFullDestroyPacket()), true);
                } catch (Throwable ignored) {
                }
            }
        });
        return this;
    }

    public Hologram setLine(int index, String message) {
        if (lines.size() <= index) {
            return addLine(message);
        }
        lines.set(index, message);
        updateEntities(index, true);
        return this;

    }

    public Hologram addLine(String message) {
        lines.add(message);
        // updateEntities(this.lines.size() - 1, true);
        updateEntities(); // TODO just move upper lines
        return this;
    }

    public Hologram removeLine() {
        return removeLine(lines.size() - 1);
    }

    public Hologram removeLine(int index) {
        lines.remove(index);
        updateEntities(index, false);
        return this;
    }

    public Hologram destroy() {
        lines.clear();
        updateEntities();
        viewers.clear();

        return this;
    }

    public boolean handleTouch(Player player, int entityId) {
        if (!touchable)
            return false;
        if (!checkID(entityId))
            return false;

        handlers.forEach(handler -> handler.handle(player, this));

        return true;
    }

    public boolean checkID(int id) {
        for (var entity : entities) {
            if (entity.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void updateEntities() {
        updateEntities(0, false);
    }

    private void updateEntities(int startIndex, boolean justThisIndex) {
        try {
            final var packets = new ArrayList<>();
            var positionChanged = !justThisIndex && lines.size() != entities.size();

            for (var i = startIndex; (i < lines.size()) && (!justThisIndex || i == startIndex); i++) {
                final var line = lines.get(i);
                if (i < entities.size() && entities.get(i) != null) {
                    final var armorStand = entities.get(i);
                    armorStand.setCustomName(line);

                    final var metadataPacket = Objects.requireNonNull(PacketPlayOutEntityMetadata)
                            .getConstructor(int.class, DataWatcher, boolean.class)
                            .newInstance(armorStand.getId(), armorStand.getDataWatcher(), false);
                    packets.add(metadataPacket);

                    if (positionChanged) {
                        final var editedLocation = location.clone().add(0, (lines.size() - i) * .30, 0);
                        armorStand.setLocation(editedLocation);

                        final var teleportPacket = Objects.requireNonNull(PacketPlayOutEntityTeleport)
                                .getConstructor(Entity)
                                .newInstance(armorStand.getHandler());
                        packets.add(teleportPacket);
                    }
                } else {
                    final var editedLocation = location.clone().add(0, (lines.size() - i) * .30, 0);
                    final var armorStand = new ArmorStandNMS(editedLocation);
                    armorStand.setCustomName(line);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvisible(true);
                    armorStand.setSmall(!touchable);
                    armorStand.setArms(false);
                    armorStand.setBasePlate(false);
                    armorStand.setGravity(false);
                    armorStand.setMarker(!touchable);

                    final var spawnLivingPacket = Objects.requireNonNull(PacketPlayOutSpawnEntityLiving)
                            .getConstructor(EntityLiving)
                            .newInstance(armorStand.getHandler());
                    packets.add(spawnLivingPacket);

                    if (Version.isVersion(1, 15)) {
                        final var metadataPacket = Objects.requireNonNull(PacketPlayOutEntityMetadata)
                                .getConstructor(int.class, DataWatcher, boolean.class)
                                .newInstance(armorStand.getId(), armorStand.getDataWatcher(), false);
                        packets.add(metadataPacket);
                    }

                    if (entities.size() <= i) {
                        entities.add(armorStand);
                    } else {
                        entities.set(i, armorStand);
                    }
                }
            }

            final var forRemoval = new ArrayList<Integer>();
            if (entities.size() > lines.size()) {
                for (var i = lines.size(); i < entities.size(); entities.remove(i)) {
                    forRemoval.add(entities.get(i).getId());
                }
            }

            final var destroyPacket = Objects.requireNonNull(PacketPlayOutEntityDestroy)
                    .getConstructor(int[].class)
                    .newInstance((Object) forRemoval.stream().mapToInt(i -> i).toArray());
            packets.add(destroyPacket);

            viewers.forEach(player -> update(player, packets, true));

        } catch (Throwable ignored) {
        }
    }

    void update(Player player, List<Object> packets, boolean check_distance) {
        try {
            if (!manager.getActiveHolograms().contains(this)) {
                manager.getActiveHolograms().add(this);
            }

            if (player.getLocation().getWorld().equals(location.getWorld())) {
                if (check_distance && player.getLocation().distanceSquared(location) >= VISIBILITY_DISTANCE_SQUARED) {
                    return;
                }
            } else if (check_distance) {
                return;
            }

            packets.forEach(packet -> ClassStorage.sendPacket(player, packet));
        } catch (Throwable ignored) {
        }
    }

    public Object getFullDestroyPacket() throws Exception {
        final var removal = new int[entities.size()];
        for (var i = 0; i < entities.size(); i++) {
            removal[i] = entities.get(i).getId();
        }

        return Objects.requireNonNull(PacketPlayOutEntityDestroy).getConstructor(int[].class).newInstance((Object) removal);
    }

    public List<Object> getAllSpawnPackets() throws Exception {
        final var packets = new ArrayList<>();

        for (var entity : entities) {
            packets.add(Objects.requireNonNull(PacketPlayOutSpawnEntityLiving).getConstructor(EntityLiving).newInstance(entity.getHandler()));

            if (Version.isVersion(1, 15)) {
                final var metadataPacket = PacketPlayOutEntityMetadata
                        .getConstructor(int.class, DataWatcher, boolean.class)
                        .newInstance(entity.getId(), entity.getDataWatcher(), true);
                packets.add(metadataPacket);
            }
        }
        return packets;
    }
}
