package org.screamingsandals.lib.packet;

import lombok.Data;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.utils.math.Vector3Df;

@Data
public abstract class MetadataItem {
    private final byte index;

    public void write(PacketWriter writer) {
        writer.writeByte(index);
    }

    public static ByteMetadataItem of(byte index, byte value) {
        return new ByteMetadataItem(index, value);
    }

    public static VarIntMetadataItem of(byte index, int value) {
        return new VarIntMetadataItem(index, value);
    }

    public static FloatMetadataItem of(byte index, float value) {
        return new FloatMetadataItem(index, value);
    }

    public static StringMetadataItem of(byte index, String value) {
        return new StringMetadataItem(index, value);
    }

    public static ComponentMetadataItem of(byte index, Component value) {
        return new ComponentMetadataItem(index, value);
    }

    public static BooleanMetadataItem of(byte index, boolean value) {
        return new BooleanMetadataItem(index, value);
    }

    public static Vector3DfMetadataItem of(byte index, Vector3Df value) {
        return new Vector3DfMetadataItem(index, value);
    }

    public static OptionalComponentMetadataItem ofOpt(byte index, Component value) {
        return new OptionalComponentMetadataItem(index, value);
    }

    @Getter
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

    @Getter
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

    @Getter
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

    @Getter
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

    @Getter
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

    @Getter
    public static class OptionalComponentMetadataItem extends MetadataItem {
        private final Component text;

        public OptionalComponentMetadataItem(byte index, Component text) {
            super(index);
            this.text = text;
        }

        @Override
        public void write(PacketWriter writer) {
            super.write(writer);
            writer.writeVarInt(5);
            var flag = text != null && !text.equals(Component.empty());
            writer.writeBoolean(flag);
            if (flag) {
                writer.writeComponent(text);
            }
        }
    }

    @Getter
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

    @Getter
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



    // TODO: add more metadata types
}
