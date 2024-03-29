/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.packet;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.spectator.Spectator;
import org.screamingsandals.lib.impl.spectator.SpectatorBackend;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.nbt.NBTSerializer;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.nbt.StringTag;
import org.screamingsandals.lib.slot.EquipmentSlot;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.configurate.SpectatorSerializers;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.Location;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a PacketWriter that populates a ByteBuf buffer with packet contents that is useful to send the Player connections.
 * <b>It is important to note that the packet header must always contain the packet id!</b>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PacketWriter extends OutputStream {

    /**
     * Maximum string length in bytes allowed by the current protocol.
     */
    public static final int MAX_STRING_LENGTH = 32767;

    /**
     * Gets the size of the variable integer.
     *
     * @param value the integer value to calculate the variable size from
     * @return the variable size this value can be contained into, upto 5 bytes in total.
     */
    public static int getVarIntSize(int value) {
        for (int j = 1; j < 5; j++) {
            if ((value & -1 << j * 7) == 0)
                return j;
        }
        return 5;
    }

    /**
     * Gets the size of the variable long.
     *
     * @param value the long value to calculate the variable size from
     * @return the variable size this value can be contained into, upto 10 bytes in total.
     */
    public static long getVarLongSize(long value) {
        for (int j = 1; j < 10; j++) {
            if ((value & -1L << j * 7) == 0L)
                return j;
        }
        return 10;
    }

    /**
     * The ByteBuf buffer that will be populated according to packet contents.
     */
    private final @NotNull ByteBuf buffer;

    /**
     * Additional packets that are required to be sent alongside other packets for proper functionality.
     */
    private final @NotNull List<@NotNull AbstractPacket> appendedPackets = new ArrayList<>();

    private boolean cancelled;

    /**
     * Serializes a component to the buffer as a sized string or a nbt tag depending on the version..
     *
     * @param component the component to serialize
     */
    public void writeComponent(@Nullable Component component) {
        if (protocol() >= 765) {
            if (component == null) {
                writeByte((byte) 0x8);
                writeSizedString("");
            } else {
                try {
                    // TODO: implement more direct conversion
                    writeBytes(NBTSerializer.INSTANCE.serializeNetworking1_20_2(SNBTSerializer.builder().build().deserialize(component.toJavaJson(false))));
                } catch (IOException e) {
                    e.printStackTrace();
                    writeByte((byte) 0x8);
                    writeSizedString(component.toLegacy());
                }
            }
        } else {
            writeComponentAsString(component);
        }
    }

    /**
     * Serializes a component to the buffer as a sized string. This method should be called only if the packet specifically states the component is sent as json text.
     *
     * @param component the component to serialize
     */
    public void writeComponentAsString(@Nullable Component component) {
        writeSizedString(component == null ? "{\"text\":\"\"}" : component.toJavaJson());
    }

    /**
     * Writes a boolean to the buffer.
     *
     * @param b the boolean to Writes
     */
    public void writeBoolean(boolean b) {
        buffer.writeBoolean(b);
    }

    /**
     * Writes a byte to the buffer.
     *
     * @param b the byte to write
     */
    public void writeByte(byte b) {
        buffer.writeByte(b);
    }

    /**
     * Writes a char to the buffer.
     *
     * @param c the char to write
     */
    public void writeChar(char c) {
        buffer.writeChar(c);
    }

    /**
     * Writes a short to the buffer.
     *
     * @param s the short to write
     */
    public void writeShort(int s) {
        buffer.writeShort(s);
    }

    /**
     * Writes an int to the buffer.
     *
     * @param i the int to write
     */
    public void writeInt(int i) {
        buffer.writeInt(i);
    }

    /**
     * Writes a long to the buffer.
     *
     * @param l the long to write
     */
    public void writeLong(long l) {
        buffer.writeLong(l);
    }

    /**
     * Writes a float to the buffer.
     *
     * @param f the float to write
     */
    public void writeFloat(float f) {
        buffer.writeFloat(f);
    }

    /**
     * Writes a double to the buffer.
     *
     * @param d the double to write
     */
    public void writeDouble(double d) {
        buffer.writeDouble(d);
    }

    /**
     * Writes an int with variable size to the buffer.
     *
     * @param value the int to write
     */
    public void writeVarInt(int value) {
        // Taken from velocity
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            buffer.writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            buffer.writeShort(w);
        } else {
            writeVarIntFull(value);
        }
    }

    /**
     * Writes an int with variable size to the buffer.
     *
     * @param value the int to write
     */
    public void writeVarIntFull(int value) {
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

    /**
     * Writes a long with variable size to the buffer.
     *
     * @param value the long to write
     */
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

    /**
     * Writes a string with fixed size to the buffer.
     *
     * @param string to write
     */
    public void writeSizedString(@NotNull String string) {
        writeSizedString(string, MAX_STRING_LENGTH);
    }

    /**
     * Writes a string with provided size to the buffer.
     *
     * @param string the string to write
     * @param maxLength the maximum length the string can hold
     */
    public void writeSizedString(String string, int maxLength) {
        var bytes = string.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > maxLength) {
            throw new UnsupportedOperationException("String too big! (is " + bytes.length + " bytes encoded, should be less than: " + maxLength + ")");
        }
        writeVarInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    /**
     * Writes a string that is properly null terminated to the buffer.
     *
     * @param s the string to write
     */
    public void writeNullTerminatedString(@NotNull String s) {
        buffer.writeCharSequence(s + '\0', StandardCharsets.UTF_8);
    }

    /**
     * Writes a var-int array to the buffer.
     *
     * @param array the array to write
     */
    public void writeVarIntArray(int @Nullable [] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (int element : array) {
            writeVarInt(element);
        }
    }

    /**
     * Writes a var-long array to the buffer.
     *
     * @param array the array to write
     */
    public void writeLongArray(long @Nullable [] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (long element : array) {
            writeLong(element);
        }
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param bytes the byte array to write
     */
    public void writeBytes(byte @Nullable [] bytes) {
        buffer.writeBytes(bytes);
    }

    /**
     * Writes a collection of string to the buffer.
     *
     * @param collection the string collection to write
     */
    public void writeStringCollection(@Nullable Collection<@NotNull String> collection) {
        if (collection == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(collection.size());
        for (String element : collection) {
            writeSizedString(element);
        }
    }

    /**
     * Writes a string array to the buffer.
     *
     * @param array the string array to write
     */
    public void writeStringArray(@NotNull String @Nullable [] array) {
        if (array == null) {
            writeVarInt(0);
            return;
        }
        writeVarInt(array.length);
        for (String element : array) {
            writeSizedString(element);
        }
    }

    /**
     * Writes a UUID to the buffer.
     *
     * @param uuid the uuid to write
     */
    public void writeUuid(@NotNull UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Writes a block position to the buffer.
     *
     * @param location the location at which the block exists to write to the buffer
     */
    public void writeBlockPosition(@NotNull Location location) {
        writeBlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Writes the offset between two locations to the buffer
     *
     * @param main the main location to calculate the offset from
     * @param location the location to find subtract to the main vector to calculate the offset from
     */
    public void writeByteOffset(@NotNull Vector3Df main, @NotNull Vector3Df location) {
        writeByte((byte) (location.getX() - main.getX()));
        writeByte((byte) (location.getY() - main.getY()));
        writeByte((byte) (location.getZ() - main.getZ()));
    }

    /**
     * Writes a block position to the buffer.
     *
     * @param x the x position of the block
     * @param y the y position of the block
     * @param z the z position of the block
     */
    public void writeBlockPosition(int x, int y, int z) {
        writeLong((((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF));
    }

    /**
     * Writes a Vector3D to the buffer.
     *
     * @param vector3D the vector3d instance to write to the buffer
     */
    public void writeVector(@NotNull Vector3D vector3D) {
        writeDouble(vector3D.getX());
        writeDouble(vector3D.getY());
        writeDouble(vector3D.getZ());
    }

    /**
     * Writes a location vector to the buffer.
     *
     * @param locationHolder the location to write
     */
    public void writeVector(@NotNull Location locationHolder) {
        writeVector(locationHolder.asVector());
    }

    /**
     * Writes a fixed point vector from a location with each axis multiplied by 32 to the buffer.
     *
     * @param locationHolder the location to write the fixed point vector
     */
    public void writeFixedPointVector(@NotNull Location locationHolder) {
        writeFixedPointVector(locationHolder.asVector());
    }

    /**
     * Writes a fixed point vector with each axis of the vector multiplied by 32 to the buffer.
     *
     * @param vector3D the vector to write
     */
    public void writeFixedPointVector(@NotNull Vector3D vector3D) {
        writeInt((int) (vector3D.getX() * 32));
        writeInt((int) (vector3D.getY() * 32));
        writeInt((int) (vector3D.getZ() * 32));
    }

    /**
     * Writes a ByteRotation (yaw/pitch) to the buffer.
     *
     * @param locationHolder the location from which to write the (yaw/pitch) from
     */
    public void writeByteRotation(@NotNull Location locationHolder) {
        writeByteRotation(locationHolder.getYaw(), locationHolder.getPitch());
    }

    /**
     * Writes a ByteRotation to the buffer.
     *
     * @param yaw the yaw to write
     * @param pitch the pitch to write
     */
    public void writeByteRotation(float yaw, float pitch) {
        writeByte((byte) (yaw * 256 / 360));
        writeByte((byte) (pitch * 256 / 360));
    }

    /**
     * Writes a Vector3Df to the buffer.
     *
     * @param vector3Df the vector to write
     */
    public void writeVector(@NotNull Vector3Df vector3Df) {
        writeFloat(vector3Df.getX());
        writeFloat(vector3Df.getY());
        writeFloat(vector3Df.getZ());
    }

    /**
     * Writes a Vector3Df of motion to the buffer.
     * Note: Each axis is multiplied by 8000
     *
     * @param vector3D the motion vector to write
     */
    public void writeMotion(@NotNull Vector3D vector3D) {
        writeShort((int) (vector3D.getX() * 8000));
        writeShort((int) (vector3D.getY() * 8000));
        writeShort((int) (vector3D.getZ() * 8000));
    }

    // TODO:
    // misat tf is this? fill jd for it
    public void writeMove(@NotNull Vector3D vector3D) {
        writeShort((int) (vector3D.getX() * 4096));
        writeShort((int) (vector3D.getY() * 4096));
        writeShort((int) (vector3D.getZ() * 4096));
    }

    /**
     * Writes an Item to the buffer.
     *
     * @param item the item to write
     */
    public void writeItem(@NotNull ItemStack item) {
        if (item.getMaterial().isAir()) {
            if (protocol() >= 402) {
                writeBoolean(false);
            } else {
                writeShort(-1);
            }
        } else {
            if (protocol() >= 402) {
                writeBoolean(true);

                writeVarInt(getItemId(item.getMaterial()));
            } else {
                writeShort(getItemId(item.getMaterial()));
            }

            write(item.getAmount());
            if (protocol() < 351) {
                writeShort(getForcedDurability(item.getMaterial())); // TODO: I think this should be durability in general and not just data value
            }
            writeNBTFromItem(item);
        }
    }

    public <T> void writeSizedArray(@NotNull T @NotNull [] array, @NotNull Consumer<@NotNull T> consumer) {
        writeVarInt(array.length);
        for (var a : array) {
            consumer.accept(a);
        }
    }

    public <T> void writeSizedCollection(@NotNull Collection<@NotNull T> collection, @NotNull Consumer<@NotNull T> consumer) {
        writeVarInt(collection.size());
        for (var a : collection) {
            consumer.accept(a);
        }
    }

    public <K, V> void writeSizedMap(@NotNull Map<@NotNull K, V> map, @NotNull BiConsumer<@NotNull K, V> consumer) {
        writeVarInt(map.size());
        for (var a : map.entrySet()) {
            consumer.accept(a.getKey(), a.getValue());
        }
    }

    public void writeBlockData(@NotNull Block blockDataHolder) {
        writeVarInt(getBlockStateId(blockDataHolder));
    }

    public void writeDataWatcherCollection(@NotNull Collection<@NotNull MetadataItem> collection) {
        for (var item : collection) {
            item.write(this);
        }

        writeByte(protocol() <= 47 ? (byte) 0x7f : (byte) 0xff); // termination sequence
    }

    // Platform classes must override this method
    public void writeNBTFromItem(@NotNull ItemStack item) {
        write(0);
    }

    //public void writeNBT(String name, Object nbt) {
        // TODO: write nbt
    //}

    public void append(@NotNull AbstractPacket packet) {
        appendedPackets.add(packet);
    }

    @Override
    public void write(int b) {
        writeByte((byte) b);
    }

    public int protocol() {
        return Server.getProtocolVersion();
    }

    protected abstract int getItemId(@NotNull ItemType material);

    protected short getForcedDurability(@NotNull ItemType material) {
        return 0;
    }

    protected abstract int getBlockStateId(@NotNull Block blockDataHolder);

    public abstract int getEquipmentSlotId(@NotNull EquipmentSlot equipmentSlotHolder);
}
