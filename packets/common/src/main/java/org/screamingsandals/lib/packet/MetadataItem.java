package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.block.BlockPosition;
import org.screamingsandals.lib.utils.math.Vector3Df;

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
    public void write(PacketWriter writer) {
        writer.writeByte(index);
    }

    /**
     * Returns a new {@link ByteMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a ByteMetaDataItem instance that holds the index and value provided
     */
    public static ByteMetadataItem of(byte index, byte value) {
        return new ByteMetadataItem(index, value);
    }

    /**
     * Returns a new {@link VarIntMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a VarIntMetadataItem instance that holds the index and value provided
     */
    public static VarIntMetadataItem of(byte index, int value) {
        return new VarIntMetadataItem(index, value);
    }

    /**
     * Returns a new {@link FloatMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a FloatMetadataItem instance that holds the index and value provided
     */
    public static FloatMetadataItem of(byte index, float value) {
        return new FloatMetadataItem(index, value);
    }

    /**
     * Returns a new {@link StringMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a StringMetadataItem instance that holds the index and value provided
     */
    public static StringMetadataItem of(byte index, String value) {
        return new StringMetadataItem(index, value);
    }

    /**
     * Returns a new {@link ComponentMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a ComponentMetadataItem instance that holds the index and value provided
     */
    public static ComponentMetadataItem of(byte index, Component value) {
        return new ComponentMetadataItem(index, value);
    }

    /**
     * Returns a new {@link BooleanMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a BooleanMetadataItem instance that holds the index and value provided
     */
    public static BooleanMetadataItem of(byte index, boolean value) {
        return new BooleanMetadataItem(index, value);
    }

    /**
     * Returns a new {@link Vector3DfMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a Vector3DfMetadataItem instance that holds the index and value provided
     */
    public static Vector3DfMetadataItem of(byte index, Vector3Df value) {
        return new Vector3DfMetadataItem(index, value);
    }

    /**
     * Returns a new {@link OptionalComponentMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a OptionalComponentMetadataItem instance that holds the index and value provided
     */
    public static OptionalComponentMetadataItem ofOpt(byte index, Component value) {
        return new OptionalComponentMetadataItem(index, value);
    }

    /**
     * Returns a new {@link OptionalBlockPositionMetadataItem} instance from the given index and value.
     *
     * @param index the index position of the MetaDataItem
     * @param value the value the MetaDataItem should hold
     * @return a OptionalBlockPositionMetadataItem instance that holds the index and value provided
     */
    public static OptionalBlockPositionMetadataItem ofOpt(byte index, BlockPosition value) {
        return new OptionalBlockPositionMetadataItem(index, value);
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
        public void write(PacketWriter writer) {
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
        public void write(PacketWriter writer) {
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
        public void write(PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(2);
            writer.writeFloat(data);
        }
    }

    /**
     * Represents a StringMetadataItem which is used to serialize data of type string to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class StringMetadataItem extends MetadataItem {
        private final String text;

        public StringMetadataItem(byte index, String text) {
            super(index);
            this.text = text;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(3);
            writer.writeSizedString(text);
        }
    }

    /**
     * Represents a ComponentMetadataItem which is used to serialize data of type component to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class ComponentMetadataItem extends MetadataItem {
        private final Component text;

        public ComponentMetadataItem(byte index, Component text) {
            super(index);
            this.text = text;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(4);
            writer.writeComponent(text);
        }
    }

    /**
     * Represents a OptionalComponentMetadataItem which is used to serialize data of Component with a flag that indicates its presence to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class OptionalComponentMetadataItem extends MetadataItem {
        private final Component val;

        public OptionalComponentMetadataItem(byte index, Component val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(5);
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
        public void write(PacketWriter writer) {
            super.write(writer);
            if (writer.protocol() < 393) {
                writer.writeVarInt(6);
            } else {
                writer.writeVarInt(7);
            }
            writer.writeBoolean(val);
        }
    }

    /**
     * Represents a Vector3DfMetadataItem which is used to serialize data of type Vector3Df to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class Vector3DfMetadataItem extends MetadataItem {
        private final Vector3Df val;

        public Vector3DfMetadataItem(byte index, Vector3Df val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            if (writer.protocol() < 393) {
                writer.writeVarInt(7);
            } else {
                writer.writeVarInt(8);
            }
            writer.writeVector(val);
        }
    }

    /**
     * Represents a BlockPositionMetadataItem which is used to serialize data of type BlockPosition to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class BlockPositionMetadataItem extends MetadataItem {
        private final BlockPosition val;

        public BlockPositionMetadataItem(byte index, BlockPosition val) {
            super(index);
            this.val = val;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            if (writer.protocol() < 393) {
                writer.writeVarInt(8);
            } else {
                writer.writeVarInt(9);
            }
            writer.writeLong(val.asLong());
        }
    }

    /**
     * Represents a OptionalBlockPositionMetadataItem which is used to serialize data of BlockPosition with a flag that indicates its presence to a PacketWriter.
     */
    @Getter
    @ToString(callSuper = true)
    public static class OptionalBlockPositionMetadataItem extends MetadataItem {
        private final BlockPosition blockPosition;

        public OptionalBlockPositionMetadataItem(byte index, BlockPosition blockPosition) {
            super(index);
            this.blockPosition = blockPosition;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            if (writer.protocol() < 393) {
                writer.writeVarInt(9);
            } else {
                writer.writeVarInt(10);
            }
            var present = blockPosition != null;
            writer.writeBoolean(present);
            if (present) {
                writer.writeLong(blockPosition.asLong());
            }
        }
    }



    // TODO: add more metadata types
}
