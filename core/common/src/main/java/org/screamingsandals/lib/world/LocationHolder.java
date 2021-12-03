package org.screamingsandals.lib.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.utils.*;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.chunk.ChunkHolder;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>A class holding a specific location in a world.</p>
 */
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

    /**
     * <p>Constructs a new location.</p>
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public LocationHolder(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * <p>Clones the current location and increments the coordinates by the supplied values.</p>
     *
     * @param x X coordinate to add
     * @param y Y coordinate to add
     * @param z Z coordinate to add
     * @return the new location
     */
    public LocationHolder add(double x, double y, double z) {
        final var clone = clone();
        clone.x += x;
        clone.y += y;
        clone.z += z;
        return clone;
    }

    /**
     * <p>Clones the current location and increments the coordinates by the XYZ values of the supplied {@link LocationHolder}.</p>
     *
     * @param holder the location holder to add
     * @return the new location
     */
    public LocationHolder add(LocationHolder holder) {
        return add(holder.getX(), holder.getY(), holder.getZ());
    }

    /**
     * <p>Clones the current location and increments the coordinates by the XYZ values of the supplied {@link Vector3D}.</p>
     *
     * @param vec the vector to add
     * @return the new location
     */
    public LocationHolder add(Vector3D vec) {
        return add(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * <p>Clones the current location and increments the coordinates by the XYZ values of the supplied {@link Vector3Df}.</p>
     *
     * @param vec the vector to add
     * @return the new location
     */
    public LocationHolder add(Vector3Df vec) {
        return add(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * <p>Clones the current location and increments the coordinates by the XYZ values of the supplied {@link BlockFace#getBlockDirection()}.</p>
     *
     * @param blockFace the block face to add
     * @return the new location
     */
    public LocationHolder add(BlockFace blockFace) {
        return add(blockFace.getBlockDirection());
    }

    /**
     * <p>Clones the current location and increments the coordinates by the XYZ values of the supplied {@link BlockFace#getBlockDirection()}.</p>
     *
     * @param blockFace the block face to add
     * @param distance  how far in the direction the new location should be
     * @return the new location
     */
    public LocationHolder add(BlockFace blockFace, int distance) {
        return add(blockFace.getBlockDirection().multiply(distance));
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link Vector3Df}.</p>
     *
     * @param x X coordinate to subtract
     * @param y Y coordinate to subtract
     * @param z Z coordinate to subtract
     * @return the new location
     */
    @NotNull
    public LocationHolder subtract(double x, double y, double z) {
        final var clone = clone();
        clone.x -= x;
        clone.y -= y;
        clone.z -= z;
        return clone;
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link LocationHolder}.</p>
     *
     * @param holder the location holder to subtract
     * @return the new location
     */
    public LocationHolder subtract(LocationHolder holder) {
        return subtract(holder.getX(), holder.getY(), holder.getZ());
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link Vector3D}.</p>
     *
     * @param vec the vector to subtract
     * @return the new location
     */
    public LocationHolder subtract(Vector3D vec) {
        return subtract(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link Vector3Df}.</p>
     *
     * @param vec the vector to subtract
     * @return the new location
     */
    public LocationHolder subtract(Vector3Df vec) {
        return subtract(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link BlockFace#getBlockDirection()}.</p>
     *
     * @param blockFace the block face to add
     * @return the new location
     */
    public LocationHolder subtract(BlockFace blockFace) {
        return subtract(blockFace.getBlockDirection());
    }

    /**
     * <p>Clones the current location and decrements the coordinates by the XYZ values of the supplied {@link BlockFace#getBlockDirection()}.</p>
     *
     * @param blockFace the block face to add
     * @param distance  how far in the direction the new location should be
     * @return the new location
     */
    public LocationHolder subtract(BlockFace blockFace, int distance) {
        return subtract(blockFace.getBlockDirection().multiply(distance));
    }

    @Override
    public <T> T as(Class<T> type) {
        return LocationMapper.convert(this, type);
    }

    /**
     * <p>Gets the squared distance between this location and a supplied one.</p>
     *
     * @param holder the second location holder
     * @return the squared distance
     */
    public double getDistanceSquared(@NotNull LocationHolder holder) {
        return MathUtils.square(getX() - holder.getX()) +
                MathUtils.square(getY() - holder.getY()) +
                MathUtils.square(getZ() - holder.getZ());
    }

    /**
     * <p>Converts this location to a {@link Vector3D}.</p>
     *
     * @return the vector
     */
    public Vector3D asVector() {
        return new Vector3D(this.x, this.y, this.z);
    }

    /**
     * <p>Converts this location to a {@link Vector3Df}.</p>
     *
     * @return the vector
     */
    public Vector3Df asVectorf() {
        return new Vector3Df((float) this.x, (float) this.y, (float) this.z);
    }

    /**
     * Converts this location into a {@link WrappedLocation}. Useful for Protobuf sending :)
     *
     * @return wrapped location into a Protobuf message
     */
    public WrappedLocation asWrappedLocation() {
        return WrappedLocation.newBuilder()
                .setX(this.x)
                .setY(this.y)
                .setZ(this.z)
                .setPitch(this.pitch)
                .setYaw(this.yaw)
                .setWorldUuid(this.world.getUuid().toString())
                .build();
    }

    /**
     * Converts this location into a {@link WrappedVector3D}. Useful for Protobuf sending :)
     *
     * @return wrapped location into a Protobuf message
     */
    public WrappedVector3D asWrappedVector() {
       return asVector().wrap();
    }

    /**
     * Converts this location into a {@link WrappedVector3Df}. Useful for Protobuf sending :)
     *
     * @return wrapped location into a Protobuf message
     */
    public WrappedVector3Df asWrappedVectorf() {
        return asVectorf().wrap();
    }

    /**
     * <p>Gets the rounded X coordinate of this location (a block coordinate).</p>
     *
     * @return the block X coordinate
     */
    public int getBlockX() {
        return (int) Math.floor(x);
    }

    /**
     * <p>Gets the rounded Y coordinate of this location (a block coordinate).</p>
     *
     * @return the block Y coordinate
     */
    public int getBlockY() {
        return (int) Math.floor(y);
    }

    /**
     * <p>Gets the rounded Z coordinate of this location (a block coordinate).</p>
     *
     * @return the block Z coordinate
     */
    public int getBlockZ() {
        return (int) Math.floor(z);
    }

    /**
     * <p>Gets the facing direction vector of this location.</p>
     *
     * @return the facing direction vector
     */
    public Vector3D getFacingDirection() {
        var vector = new Vector3D();

        vector.setY(-Math.sin(Math.toRadians(pitch)));

        var xz = Math.cos(Math.toRadians(pitch));
        vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
        vector.setZ(xz * Math.cos(Math.toRadians(yaw)));

        return vector;
    }

    /**
     * <p>Gets the block at this location.</p>
     *
     * @return the block
     */
    public BlockHolder getBlock() {
        return as(BlockHolder.class);
    }

    /**
     * <p>Gets the chunk which this location is in.</p>
     *
     * @return the chunk
     */
    public ChunkHolder getChunk() {
        return getWorld().getChunkAt(this).orElseThrow();
    }

    /**
     * <p>Spawns a particle at this location.</p>
     *
     * @param particle the particle
     */
    public void sendParticle(ParticleHolder particle) {
        getWorld().sendParticle(particle, this);
    }

    /**
     * <p>Clones this location holder.</p>
     *
     * @return the cloned holder
     */
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

    /**
     * <p>Gets the entities in the radius from this location.</p>
     *
     * @param radius the radius
     * @return the entities
     */
    public List<EntityBasic> getNearbyEntities(int radius) {
        return world.getEntities().stream()
                .filter(e -> isInRange(e.getLocation(), radius))
                .collect(Collectors.toList());
    }

    /**
     * <p>Gets the entities extending the supplied class in the radius from this location.</p>
     *
     * @param clazz  the entity type class
     * @param radius the radius
     * @param <T>    the entity type
     * @return the entities
     */
    public <T extends EntityBasic> List<T> getNearbyEntitiesByClass(Class<T> clazz, int radius) {
        return world.getEntitiesByClass(clazz).stream()
                .filter(e -> isInRange(e.getLocation(), radius))
                .collect(Collectors.toList());
    }

    /**
     * <p>Compares the supplied world to the world of this location.</p>
     *
     * @param holder the world to compare
     * @return is the world the same?
     */
    public boolean isWorldSame(LocationHolder holder) {
        return getWorld().equals(holder.getWorld());
    }

    /**
     * <p>Checks if the supplied location is within the radius from this location.</p>
     *
     * @param holder   the location holder to compare
     * @param distance the radius
     * @return is the supplied location within the radius of this location?
     */
    public boolean isInRange(LocationHolder holder, int distance) {
        return getDistanceSquared(holder) < distance;
    }

    /**
     * <p>Checks if the supplied location is out of range from this location.</p>
     *
     * @param holder   the location holder to compare
     * @param distance the radius
     * @return is the supplied location out of range of this location?
     */
    public boolean outOfRange(LocationHolder holder, int distance) {
        return getDistanceSquared(holder) >= distance;
    }

    /**
     * Sets the {@link #getYaw() yaw} and {@link #getPitch() pitch} to point
     * in the direction of the vector.
     *
     * @param vector the direction vector
     * @return the same location
     */
    @NotNull
    public LocationHolder setDirection(@NotNull Vector3D vector) {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        final double _2PI = 2 * Math.PI;
        final double x = vector.getX();
        final double z = vector.getZ();

        if (x == 0 && z == 0) {
            pitch = vector.getY() > 0 ? -90 : 90;
            return this;
        }

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

        double x2 = Math.pow(x, 2);
        double z2 = Math.pow(z, 2);
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

        return this;
    }
}
