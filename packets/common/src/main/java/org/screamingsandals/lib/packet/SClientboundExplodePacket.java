package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
public class SClientboundExplodePacket extends AbstractPacket {
    private Vector3Df location;
    private float strength;
    private Vector3Df knockBackVelocity;
    private List<LocationHolder> blockLocations;

    @Override
    public void write(PacketWriter writer) {
        writer.writeVector(location);
        writer.writeFloat(strength);
        writer.writeSizedCollection(blockLocations, locationHolder -> writer.writeByteOffset(location, locationHolder.asVectorf()));
        writer.writeVector(knockBackVelocity);
    }
}
