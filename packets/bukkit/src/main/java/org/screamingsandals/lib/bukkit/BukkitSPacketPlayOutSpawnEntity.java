package org.screamingsandals.lib.bukkit;
import org.bukkit.entity.LivingEntity;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.common.SPacketPlayOutSpawnEntity;
import org.screamingsandals.lib.entity.EntityLiving;

public class BukkitSPacketPlayOutSpawnEntity extends BukkitSPacket implements SPacketPlayOutSpawnEntity {
    public BukkitSPacketPlayOutSpawnEntity() {
        super(ClassStorage.NMS.PacketPlayOutSpawnEntity);
    }

    @Override
    public void setEntity(EntityLiving entity, int objectData) {
        if (entity == null) {
            throw new UnsupportedOperationException("Entity cannot be null!");
        }

        packet.setField("a", entity.getEntityId());
        packet.setField("b", entity.getUniqueId());

        final var entityLocation = entity.getLocation();
        packet.setField("c", entityLocation.getX());
        packet.setField("d", entityLocation.getY());
        packet.setField("e", entityLocation.getZ());

        final var entityVelocity = entity.getVelocity();
        packet.setField("f", entityVelocity.getX());
        packet.setField("g", entityVelocity.getY());
        packet.setField("h", entityVelocity.getZ());

        packet.setField("i", entityLocation.getPitch());
        packet.setField("j", entityLocation.getYaw());
        packet.setField("k", ClassStorage.getEntityType(entity.as(LivingEntity.class)));
        packet.setField("l", objectData);
    }
}
