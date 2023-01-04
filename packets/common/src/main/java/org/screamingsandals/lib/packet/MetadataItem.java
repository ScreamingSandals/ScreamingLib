/*
 * Copyright 2023 ScreamingSandals
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

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.utils.math.Vector3Di;

/**
 * Represents an abstract MetaDataItem that contains a byte indicating the index position of this item.
 * This class has to be overridden to write the data of preferred type since an MetadataItem always comprises an index and a data.
 */
@Data
public abstract class MetadataItem {
    /**
     *  The index position of this MetadataItem.
     */
    private final byte index;

    /**
     * Serializes the index position of this MetadataItem instance to the provided PacketWriter object.
     *
     * Note: This method is to be overridden by subclasses to also serialize the data to the provided PacketWriter
     *
     * @param writer the PacketWriter instance to serialize this MetaDataItem to
     */
    public void write(@NotNull PacketWriter writer) {
        writer.writeByte(index);
    }

    /**
     * Returns a new {@link ByteMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a ByteMetaDataItem instance that holds the index and value provided
     */
    public static @NotNull ByteMetadataItem of(byte index, byte value) {
        return new ByteMetadataItem(index, value);
    }

    /**
     * Returns a new {@link VarIntMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a VarIntMetadataItem instance that holds the index and value provided
     */
    public static @NotNull VarIntMetadataItem of(byte index, int value) {
        return new VarIntMetadataItem(index, value);
    }

    /**
     * Returns a new {@link FloatMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a FloatMetadataItem instance that holds the index and value provided
     */
    public static @NotNull FloatMetadataItem of(byte index, float value) {
        return new FloatMetadataItem(index, value);
    }

    /**
     * Returns a new {@link StringMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a StringMetadataItem instance that holds the index and value provided
     */
    public static @NotNull StringMetadataItem of(byte index, @NotNull String value) {
        return new StringMetadataItem(index, value);
    }

    /**
     * Returns a new {@link ComponentMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a ComponentMetadataItem instance that holds the index and value provided
     */
    public static @NotNull ComponentMetadataItem of(byte index, @NotNull Component value) {
        return new ComponentMetadataItem(index, value);
    }

    /**
     * Returns a new {@link BooleanMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a BooleanMetadataItem instance that holds the index and value provided
     */
    public static @NotNull BooleanMetadataItem of(byte index, boolean value) {
        return new BooleanMetadataItem(index, value);
    }

    /**
     * Returns a new {@link Vector3DfMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a Vector3DfMetadataItem instance that holds the index and value provided
     */
    public static @NotNull Vector3DfMetadataItem of(byte index, @NotNull Vector3Df value) {
        return new Vector3DfMetadataItem(index, value);
    }

    /**
     * Returns a new {@link OptionalComponentMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a OptionalComponentMetadataItem instance that holds the index and value provided
     */
    public static @NotNull OptionalComponentMetadataItem ofOpt(byte index, @NotNull Component value) {
        return new OptionalComponentMetadataItem(index, value);
    }

    /**
     * Returns a new {@link OptionalBlockPositionMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a OptionalBlockPositionMetadataItem instance that holds the index and value provided
     */
    public static @NotNull OptionalBlockPositionMetadataItem ofOpt(byte index, @NotNull Vector3Di value) {
        return new OptionalBlockPositionMetadataItem(index, value);
    }

    /**
     * Turns XYZ coordinates to a protocol BlockPosition.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the protocol BlockPosition
     * @see <a href="https://wiki.vg/Protocol#Position">https://wiki.vg/Protocol#Position</a>
     */
    public static long blockPosToLong(int x, int y, int z) {
        if (Server.isVersion(1, 14)) {
            return (((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF);
        }
        return (((long) x & 0x3FFFFFF) << 38) | (((long) y & 0xFFF) << 26) | ((long) z & 0x3FFFFFF);
    }

    /**
     * Represents a ByteMetadataItem which is used to serialize data of type byte to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class ByteMetadataItem extends MetadataItem {
        private final byte data;

        public ByteMetadataItem(byte index, byte data) {
            super(index);
            this.data = data;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(0);
            writer.writeByte(data);
        }
    }

    /**
     * Represents a VarIntMetadataItem which is used to serialize data of type int to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class VarIntMetadataItem extends MetadataItem {
        private final int data;

        public VarIntMetadataItem(byte index, int data) {
            super(index);
            this.data = data;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(1);
            writer.writeVarInt(data);
        }
    }

    /**
     * Represents a FloatMetadataItem which is used to serialize data of type float to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class FloatMetadataItem extends MetadataItem {
        private final float data;

        public FloatMetadataItem(byte index, float data) {
            super(index);
            this.data = data;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() >= 761 ? 3 : 2);
            writer.writeFloat(data);
        }
    }

    /**
     * Represents a StringMetadataItem which is used to serialize data of type string to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class StringMetadataItem extends MetadataItem {
        private final @NotNull String text;

        public StringMetadataItem(byte index, @NotNull String text) {
            super(index);
            this.text = text;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() >= 761 ? 4 : 3);
            writer.writeSizedString(text);
        }
    }

    /**
     * Represents a ComponentMetadataItem which is used to serialize data of type component to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class ComponentMetadataItem extends MetadataItem {
        private final @NotNull Component text;

        public ComponentMetadataItem(byte index, @NotNull Component text) {
            super(index);
            this.text = text;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() >= 761 ? 5 : 4);
            writer.writeComponent(text);
        }
    }

    /**
     * Represents a OptionalComponentMetadataItem which is used to serialize data of Component with a flag that indicates its presence to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class OptionalComponentMetadataItem extends MetadataItem {
        private final @Nullable Component val;

        public OptionalComponentMetadataItem(byte index, @Nullable Component val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() >= 761 ? 6 : 5);
            var flag = val != null && !val.equals(Component.empty());
            writer.writeBoolean(flag);
            if (flag) {
                writer.writeComponent(val);
            }
        }
    }

    /**
     * Represents a BooleanMetadataItem which is used to serialize data of type boolean to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class BooleanMetadataItem extends MetadataItem {
        private final boolean val;

        public BooleanMetadataItem(byte index, boolean val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() < 393 ? 6 : writer.protocol() >= 761 ? 8 : 7);
            writer.writeBoolean(val);
        }
    }

    /**
     * Represents a Vector3DfMetadataItem which is used to serialize data of type Vector3Df to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class Vector3DfMetadataItem extends MetadataItem {
        private final @NotNull Vector3Df val;

        public Vector3DfMetadataItem(byte index, @NotNull Vector3Df val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() < 393 ? 7 : writer.protocol() >= 761 ? 9 : 8);
            writer.writeVector(val);
        }
    }

    /**
     * Represents a BlockPositionMetadataItem which is used to serialize data of type BlockPosition to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class BlockPositionMetadataItem extends MetadataItem {
        private final @NotNull Vector3Di val;

        public BlockPositionMetadataItem(byte index, @NotNull Vector3Di val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() < 393 ? 8 : writer.protocol() >= 761 ? 10 : 9);
            writer.writeLong(blockPosToLong(val.getX(), val.getY(), val.getZ()));
        }
    }

    /**
     * Represents a OptionalBlockPositionMetadataItem which is used to serialize data of BlockPosition with a flag that indicates its presence to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class OptionalBlockPositionMetadataItem extends MetadataItem {
        private final @Nullable Vector3Di blockPosition;

        public OptionalBlockPositionMetadataItem(byte index, @Nullable Vector3Di blockPosition) {
            super(index);
            this.blockPosition = blockPosition;
        }

        @Override
        public void write(@NotNull PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(writer.protocol() < 393 ? 9 : writer.protocol() >= 761 ? 11 : 10);
            var present = blockPosition != null;
            writer.writeBoolean(present);
            if (present) {
                writer.writeLong(blockPosToLong(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()));
            }
        }
    }

    // TODO: add more metadata types
}
