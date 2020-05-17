package org.screamingsandals.lib.nms.holograms;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.nms.entity.ArmorStandNMS;
import org.screamingsandals.lib.nms.utils.InstanceMethod;
import org.screamingsandals.lib.nms.utils.Version;

import java.util.*;

import static org.screamingsandals.lib.nms.utils.ClassStorage.NMS.*;
import static org.screamingsandals.lib.nms.utils.ClassStorage.getField;
import static org.screamingsandals.lib.nms.utils.ClassStorage.getMethod;

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

    public Hologram(HologramManager manager, List<Player> players, Location loc, String[] lines) {
        this(manager, players, loc, lines, false);
    }

    public Hologram(HologramManager manager, List<Player> players, Location location, String[] lines, boolean touchable) {
        this.manager = manager;
        this.lines.addAll(Arrays.asList(lines));
        this.location = location;
        updateEntities();
        addViewers(players);
        this.touchable = touchable;
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
        return addViewers(Collections.singletonList(player));
    }

    public Hologram addViewers(List<Player> players) {
        for (Player player : players) {
            if (!viewers.contains(player)) {
                viewers.add(player);
                try {
                    update(player, getAllSpawnPackets(), true);
                } catch (Throwable ignored) {
                }
            }
        }
        return this;
    }

    public Hologram removeViewer(Player player) {
        return removeViewers(Collections.singletonList(player));
    }

    public Hologram removeViewers(List<Player> players) {
        for (Player player : players) {
            if (viewers.contains(player)) {
                viewers.remove(player);
                try {
                    update(player, Collections.singletonList(getFullDestroyPacket()), true);
                } catch (Throwable ignored) {
                }
            }
        }
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
        return removeLine(this.lines.size() - 1);
    }

    public Hologram removeLine(int index) {
        lines.remove(index);
        updateEntities(index, false);
        return this;
    }

    public Hologram destroy() {
        lines.clear();
        viewers.clear();

        updateEntities();
        return this;
    }

    public boolean handleTouch(Player player, int entityId) {
        if (!touchable)
            return false;
        if (!checkID(entityId))
            return false;

        for (TouchHandler handler : handlers) {
            handler.handle(player, this);
        }

        return true;
    }

    public boolean checkID(int id) {
        for (ArmorStandNMS entity : entities) {
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
            final List<Object> packets = new ArrayList<>();
            boolean positionChanged = !justThisIndex && this.lines.size() != this.entities.size();

            for (int i = startIndex; (i < lines.size()) && (!justThisIndex || i == startIndex); i++) {
                final String line = lines.get(i);
                if (i < entities.size() && entities.get(i) != null) {
                    final ArmorStandNMS armorStand = entities.get(i);
                    armorStand.setCustomName(line);

                    final Object metadataPacket = Objects.requireNonNull(PacketPlayOutEntityMetadata)
                            .getConstructor(int.class, DataWatcher, boolean.class)
                            .newInstance(armorStand.getId(), armorStand.getDataWatcher(), false);
                    packets.add(metadataPacket);

                    if (positionChanged) {
                        final Location editedLocation = location.clone().add(0, (this.lines.size() - i) * .30, 0);
                        armorStand.setLocation(editedLocation);

                        final Object teleportPacket = Objects.requireNonNull(PacketPlayOutEntityTeleport)
                                .getConstructor(Entity)
                                .newInstance(armorStand.getHandler());
                        packets.add(teleportPacket);
                    }
                } else {
                    final Location editedLocation = location.clone().add(0, (this.lines.size() - i) * .30, 0);
                    final ArmorStandNMS armorStand = new ArmorStandNMS(editedLocation);
                    armorStand.setCustomName(line);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvisible(true);
                    armorStand.setSmall(!touchable);
                    armorStand.setArms(false);
                    armorStand.setBasePlate(false);
                    armorStand.setGravity(false);
                    armorStand.setMarker(!touchable);

                    final Object spawnLivingPacket = Objects.requireNonNull(PacketPlayOutSpawnEntityLiving)
                            .getConstructor(EntityLiving)
                            .newInstance(armorStand.getHandler());
                    packets.add(spawnLivingPacket);

                    if (Version.isVersion(1, 15)) {
                        final Object metadataPacket = Objects.requireNonNull(PacketPlayOutEntityMetadata)
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

            final List<Integer> forRemoval = new ArrayList<>();
            if (entities.size() > lines.size()) {
                for (int i = lines.size(); i < entities.size(); entities.remove(i)) {
                    forRemoval.add(entities.get(i).getId());
                }
            }

            final Object destroyPacket = Objects.requireNonNull(PacketPlayOutEntityDestroy)
                    .getConstructor(int[].class)
                    .newInstance((Object) forRemoval.stream().mapToInt(i -> i).toArray());
            packets.add(destroyPacket);

            viewers.forEach(player -> update(player, packets, true));

        } catch (Throwable ignored) {
        }
    }

    void update(Player player, List<Object> packets, boolean check_distance) {
        try {
            if (!manager.getHolograms().contains(this)) {
                manager.getHolograms().add(this);
            }

            if (player.getLocation().getWorld().equals(location.getWorld())) {
                if (check_distance && player.getLocation().distanceSquared(location) >= VISIBILITY_DISTANCE_SQUARED) {
                    return;
                }
            } else if (check_distance) {
                return;
            }

            final Object handler = getMethod(player, "getHandle").invoke();
            final Object connection = getField(handler, "playerConnection,field_71135_a");
            final InstanceMethod sendPacket = getMethod(connection, "sendPacket,func_147359_a", Packet);

            for (Object packet : packets) {
                sendPacket.invoke(packet);
            }
        } catch (Throwable ignored) {
        }
    }

    public Object getFullDestroyPacket() throws Exception {
        final int[] removal = new int[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            removal[i] = entities.get(i).getId();
        }

        return Objects.requireNonNull(PacketPlayOutEntityDestroy).getConstructor(int[].class).newInstance((Object) removal);
    }

    public List<Object> getAllSpawnPackets() throws Exception {
        final List<Object> packets = new ArrayList<>();

        for (ArmorStandNMS entity : entities) {
            packets.add(Objects.requireNonNull(PacketPlayOutSpawnEntityLiving).getConstructor(EntityLiving).newInstance(entity.getHandler()));

            if (Version.isVersion(1, 15)) {
                final Object metadataPacket = PacketPlayOutEntityMetadata
                        .getConstructor(int.class, DataWatcher, boolean.class)
                        .newInstance(entity.getId(), entity.getDataWatcher(), true);
                packets.add(metadataPacket);
            }
        }
        return packets;
    }
}
