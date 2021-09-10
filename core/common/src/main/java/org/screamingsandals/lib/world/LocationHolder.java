package org.screamingsandals.lib.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.MathUtils;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigSerializable
public class LocationHolder implements Wrapper, Serializable {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private WorldHolder world;

    public LocationHolder(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LocationHolder add(double x, double y, double z) {
        final var clone = clone();
        clone.x += x;
        clone.y += y;
        clone.z += z;
        return clone;
    }

    public LocationHolder add(LocationHolder holder) {
        return add(holder.getX(), holder.getY(), holder.getZ());
    }

    public LocationHolder add(Vector3D vec) {
        return add(vec.getX(), vec.getY(), vec.getZ());
    }

    public LocationHolder add(Vector3Df vec) {
        return add(vec.getX(), vec.getY(), vec.getZ());
    }

    public LocationHolder remove(double x, double y, double z) {
        final var clone = clone();
        clone.x -= x;
        clone.y -= y;
        clone.z -= z;
        return clone;
    }

    public LocationHolder remove(LocationHolder holder) {
        return remove(holder.getX(), holder.getY(), holder.getZ());
    }

    public LocationHolder remove(Vector3D vec) {
        return remove(vec.getX(), vec.getY(), vec.getZ());
    }

    @Override
    public <T> T as(Class<T> type) {
        return LocationMapper.convert(this, type);
    }

    public double getDistanceSquared(@NotNull LocationHolder holder) {
        return MathUtils.square(getX() - holder.getX()) +
                MathUtils.square(getY() - holder.getY()) +
                MathUtils.square(getZ() - holder.getZ());
    }

    public Vector3D asVector() {
        return new Vector3D(this.x, this.y, this.z);
    }

    public Vector3Df asVectorf() {
        return new Vector3Df((float) this.x, (float) this.y, (float) this.z);
    }

    public int getBlockX() {
        return (int) Math.floor(x);
    }

    public int getBlockY() {
        return (int) Math.floor(y);
    }

    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    public Vector3D getFacingDirection() {
        var vector = new Vector3D();

        vector.setY(-Math.sin(Math.toRadians(pitch)));

        var xz = Math.cos(Math.toRadians(pitch));
        vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
        vector.setZ(xz * Math.cos(Math.toRadians(yaw)));

        return vector;
    }

    public BlockHolder getBlock() {
        return as(BlockHolder.class);
    }

    public ChunkHolder getChunk() {
        return getWorld().getChunkAt(this).orElseThrow();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public LocationHolder clone() {
        final var location = new LocationHolder();
        location.setX(getX());
        location.setY(getY());
        location.setZ(getZ());
        location.setPitch(getPitch());
        location.setYaw(getYaw());
        location.setWorld(getWorld());

        return location;
    }

    public List<EntityBasic> getNearbyEntities(int radius) {
        final int squaredRadius = MathUtils.square(radius);
        return world.getEntities().stream()
                .filter(e -> e.getLocation().getDistanceSquared(this) <= squaredRadius)
                .collect(Collectors.toList());
    }

    public <T extends EntityBasic> List<T> getNearbyEntitiesByClass(Class<T> clazz, int radius) {
        final int squaredRadius = MathUtils.square(radius);
        return world.getEntitiesByClass(clazz).stream()
                .filter(e -> e.getLocation().getDistanceSquared(this) <= squaredRadius)
                .collect(Collectors.toList());
    }

    public boolean isWorldSame(LocationHolder holder) {
        return getWorld().equals(holder.getWorld());
    }

    public boolean isInRange(LocationHolder holder, int distance) {
        return getDistanceSquared(holder) < distance;
    }

    public boolean outOfRange(LocationHolder holder, int distance) {
        return getDistanceSquared(holder) >= distance;
    }
}
