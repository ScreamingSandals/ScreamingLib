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

package org.screamingsandals.lib.nbt;

import com.sun.source.tree.UsesTree;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBTSerializer {
    public byte @NotNull[] serialize(@NotNull Tag tag) throws IOException {
        var buf = new ByteArrayOutputStream();
        var output = new DataOutputStream(buf);
        writeType(tag, output);
        output.writeUTF("");
        write(tag, output);
        return buf.toByteArray();
    }

    public byte @NotNull[] serialize(@NotNull Tag tag, @NotNull String name) throws IOException {
        var buf = new ByteArrayOutputStream();
        var output = new DataOutputStream(buf);
        writeType(tag, output);
        output.writeUTF(name);
        write(tag, output);
        return buf.toByteArray();
    }

    private byte getId(@NotNull Tag tag) {
        if (tag instanceof ByteArrayTag) {
            return 7;
        } else if (tag instanceof ByteTag) {
            return 1;
        } else if (tag instanceof CompoundTag) {
            return 10;
        } else if (tag instanceof DoubleTag) {
            return 6;
        } else if (tag instanceof FloatTag) {
            return 5;
        } else if (tag instanceof IntArrayTag) {
            return 11;
        } else if (tag instanceof IntTag) {
            return 3;
        } else if (tag instanceof ListTag) {
            return 9;
        } else if (tag instanceof LongArrayTag) {
            return 12;
        } else if (tag instanceof LongTag) {
            return 4;
        } else if (tag instanceof ShortTag) {
            return 2;
        } else if (tag instanceof StringTag) {
            return 8;
        } else {
            return -1;
        }
    }

    private void writeType(@NotNull Tag tag, @NotNull DataOutput output) throws IOException {
        var id = getId(tag);
        if (id == -1) {
            throw new IllegalArgumentException("Unknown tag " + tag);
        } else {
            output.writeByte(id);
        }
    }

    private void write(@NotNull Tag tag, @NotNull DataOutput output) throws IOException {
        if (tag instanceof ByteArrayTag) {
            var bytes = ((ByteArrayTag) tag).value();
            output.writeInt(bytes.length);
            output.write(bytes);
        } else if (tag instanceof ByteTag) {
            output.writeByte(((ByteTag) tag).value());
        } else if (tag instanceof CompoundTag) {
            for (var entry : ((CompoundTag) tag).value().entrySet()) {
                writeType(entry.getValue(), output);
                output.writeUTF(entry.getKey());
                write(entry.getValue(), output);
            }
            output.writeByte(0);
        } else if (tag instanceof DoubleTag) {
            output.writeDouble(((DoubleTag) tag).value());
        } else if (tag instanceof FloatTag) {
            output.writeFloat(((FloatTag) tag).value());
        } else if (tag instanceof IntArrayTag) {
            var ints = ((IntArrayTag) tag).value();
            output.writeInt(ints.length);
            for (var intValue : ints) {
                output.writeInt(intValue);
            }
        } else if (tag instanceof IntTag) {
            output.writeInt(((IntTag) tag).value());
        } else if (tag instanceof ListTag) {
            var values = ((ListTag) tag).value();
            if (values.size() == 0) {
                output.writeByte(0);
                output.writeInt(0);
            } else {
                var id = getId(values.get(0));
                output.writeByte(id);
                output.writeInt(values.size());
                for (var value : values) {
                    if (getId(value) == id) {
                        write(value, output);
                    } else {
                        throw new IllegalArgumentException("Only one kind of tag can be saved in a list!");
                    }
                }
            }
        } else if (tag instanceof LongArrayTag) {
            var longs = ((LongArrayTag) tag).value();
            output.writeInt(longs.length);
            for (var longValue : longs) {
                output.writeLong(longValue);
            }
        } else if (tag instanceof LongTag) {
            output.writeLong(((LongTag) tag).value());
        } else if (tag instanceof ShortTag) {
            output.writeShort(((ShortTag) tag).value());
        } else if (tag instanceof StringTag) {
            output.writeUTF(((StringTag) tag).value());
        } else {
            throw new IllegalArgumentException("Unknown tag " + tag);
        }
    }

    @NotNull
    public Tag deserialize(byte @NotNull[] bytes) throws IOException {
        var input = new DataInputStream(new ByteArrayInputStream(bytes));
        var id = input.readByte();
        input.skipBytes(input.readUnsignedShort()); // skip name
        return read(input, id);
    }

    @NotNull
    private Tag read(@NotNull DataInputStream stream, byte id) throws IOException {
        switch (id) {
            case 1:
                return new ByteTag(stream.readByte());
            case 2:
                return new ShortTag(stream.readShort());
            case 3:
                return new IntTag(stream.readInt());
            case 4:
                return new LongTag(stream.readLong());
            case 5:
                return new FloatTag(stream.readFloat());
            case 6:
                return new DoubleTag(stream.readDouble());
            case 7:
                return new ByteArrayTag(stream.readNBytes(stream.readInt()));
            case 8:
                return new StringTag(stream.readUTF());
            case 9: {
                var typeId = stream.readByte();
                var size = stream.readInt();
                if (typeId != 0) {
                    var list = new ArrayList<Tag>();
                    for (var i = 0; i < size; i++) {
                        list.add(read(stream, typeId));
                    }
                    return new ListTag(list);
                } else {
                    return new ListTag(List.of());
                }
            }
            case 10:
                var map = new HashMap<String, Tag>();
                byte typeId;
                while ((typeId = stream.readByte()) != 0) {
                    var name = stream.readUTF();
                    var tag = read(stream, typeId);
                    map.put(name, tag);
                }
                return new CompoundTag(map);
            case 11:
                var ints = new int[stream.readInt()];
                for (var i = 0; i < ints.length; i++) {
                    ints[i] = stream.readInt();
                }
                return new IntArrayTag(ints);
            case 12:
                var longs = new long[stream.readInt()];
                for (var i = 0; i < longs.length; i++) {
                    longs[i] = stream.readLong();
                }
                return new LongArrayTag(longs);
            default:
                throw new IllegalArgumentException("Unknown tag type " + id);
        }
    }
}
