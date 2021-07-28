package org.screamingsandals.lib.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.screamingsandals.lib.entity.DataWatcher;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.slot.EquipmentSlotHolder;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.BlockDataHolder;
import org.screamingsandals.lib.world.LocationHolder;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PacketWriter extends OutputStream {
    private final ByteBuf buffer;

    private final List<AbstractPacket> appendedPackets = new ArrayList<>();

    public void writeComponent(Component component) {
        writeSizedString(GsonComponentSerializer.gson().serializeOr(component, "{\"text\":\"\"}"));
    }

    public void writeBoolean(boolean b) {
        buffer.writeBoolean(b);
    }

    public void writeByte(byte b) {
        buffer.writeByte(b);
    }

    public void writeChar(char c) {
        buffer.writeChar(c);
    }

    public void writeShort(short s) {
        buffer.writeShort(s);
    }

    public void writeInt(int i) {
        buffer.writeInt(i);
    }

    public void writeLong(long l) {
        buffer.writeLong(l);
    }

    public void writeFloat(float f) {
        buffer.writeFloat(f);
    }

    public void writeDouble(double d) {
        buffer.writeDouble(d);
    }

    public void writeVarInt(int value) {
        // Took from velocity
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buffer.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buffer.writeShort(w);
        } else if ((value & (0xFFFFFFFF << 21)) == 0) {
            int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
            buffer.writeMedium(w);
        } else {
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
            buffer.writeInt(w);
            buffer.writeByte(value >>> 28);
        }
    }

    public void writeVarLong(long value) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            writeByte(temp);
        } while (value != 0);
    }

    public void writeSizedString(String string) {
        final int utf8Bytes = ByteBufUtil.utf8Bytes(string);
        writeVarInt(utf8Bytes);
        buffer.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public void writeNullTerminatedString(String s) {
        buffer.writeCharSequence(s + '\0', StandardCharsets.UTF_8);
    }

    public void writeVarIntArray(int[] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (int element : array) {
            writeVarInt(element);
        }
    }

    public void writeLongArray(long[] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (long element : array) {
            writeLong(element);
        }
    }

    public void writeBytes(byte[] bytes) {
        buffer.writeBytes(bytes);
    }

    public void writeStringCollection(Collection<String> collection) {
        if (collection == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(collection.size());
        for (String element : collection) {
            writeSizedString(element);
        }
    }

    public void writeStringArray(String[] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (String element : array) {
            writeSizedString(element);
        }
    }

    public void writeUuid(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public void writeBlockPosition(LocationHolder location) {
        writeBlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void writeByteOffset(Vector3Df main, Vector3Df location) {
        writeByte((byte) (location.getX() - main.getX()));
        writeByte((byte) (location.getY() - main.getY()));
        writeByte((byte) (location.getZ() - main.getZ()));
    }

    public void writeBlockPosition(int x, int y, int z) {
        writeLong((((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF));
    }

    public void writeVector(Vector3D vector3D) {
        writeDouble(vector3D.getX());
        writeDouble(vector3D.getY());
        writeDouble(vector3D.getZ());
    }

    public void writeVector(LocationHolder locationHolder) {
        writeVector(locationHolder.asVector());
    }

    public void writeFixedPointVector(LocationHolder locationHolder) {
        writeFixedPointVector(locationHolder.asVector());
    }

    public void writeFixedPointVector(Vector3D vector3D) {
        writeInt((int) (vector3D.getX() * 32));
        writeInt((int) (vector3D.getY() * 32));
        writeInt((int) (vector3D.getZ() * 32));
    }

    public void writeByteRotation(LocationHolder locationHolder) {
        writeByteRotation(locationHolder.getYaw(), locationHolder.getPitch());
    }

    public void writeByteRotation(float yaw, float pitch) {
        writeByte((byte) (yaw * 256 / 360));
        writeByte((byte) (pitch * 256 / 360));
    }

    public void writeVector(Vector3Df vector3Df) {
        writeFloat(vector3Df.getX());
        writeFloat(vector3Df.getY());
        writeFloat(vector3Df.getZ());
    }

    public void writeMotion(Vector3D vector3D) {
        writeShort((short) (vector3D.getX() * 8000));
        writeShort((short) (vector3D.getY() * 8000));
        writeShort((short) (vector3D.getZ() * 8000));
    }

    public void writeMove(Vector3D vector3D) {
        writeShort((short) (vector3D.getX() * 4096));
        writeShort((short) (vector3D.getY() * 4096));
        writeShort((short) (vector3D.getZ() * 4096));
    }

    public void writeItem(Item item) {
        if (item.getMaterial().isAir()) {
           writeBoolean(false);
        } else {
            writeBoolean(true);

            writeVarInt(getItemId(item.getMaterial()));
            write(item.getAmount());
            write(0); // TODO: write nbt meta
            //write(item.get()); write meta
        }
    }

    public <T> void writeSizedArray(T[] array, Consumer<T> consumer) {
        writeVarInt(array.length);
        for (var a : array) {
            consumer.accept(a);
        }
    }

    public <T> void writeSizedCollection(Collection<T> collection, Consumer<T> consumer) {
        writeVarInt(collection.size());
        for (var a : collection) {
            consumer.accept(a);
        }
    }

    public <K, V> void writeSizedMap(Map<K, V> map, BiConsumer<K, V> consumer) {
        writeVarInt(map.size());
        for (var a : map.entrySet()) {
            consumer.accept(a.getKey(), a.getValue());
        }
    }

    public void writeBlockData(BlockDataHolder blockDataHolder) {
        writeVarInt(getBlockStateId(blockDataHolder));
    }

    public void writeDataWatcherCollection(Collection<MetadataItem> collection) {
        for (var item : collection) {
            item.write(this);
        }

        writeByte((byte) 0xff); // termination sequence
    }

    //public void writeNBT(String name, Object nbt) {
        // TODO: write nbt
    //}

    public void append(AbstractPacket packet) {
        appendedPackets.add(packet);
    }


    @Override
    public void write(int b) {
        writeByte((byte) b);
    }

    protected abstract int getItemId(MaterialHolder material);

    protected abstract int getBlockStateId(BlockDataHolder blockDataHolder);

    public abstract int getEquipmentSlotId(EquipmentSlotHolder equipmentSlotHolder);

    public abstract int protocol();
}
