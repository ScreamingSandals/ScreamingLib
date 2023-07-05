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

package org.screamingsandals.lib.impl.vanilla.packet;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.item.ItemType;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.packet.PacketWriter;
import org.screamingsandals.lib.utils.reflect.Reflect;

public abstract class VanillaPacketWriter extends PacketWriter {
    public VanillaPacketWriter(@NotNull ByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected int getItemId(@NotNull ItemType material) {
        return Reflect.fastInvokeResulted(ItemAccessor.getMethodGetId1(), materialHolderToItem(material)).as(Integer.class);
    }

    @Override
    protected int getBlockStateId(@NotNull Block blockDataHolder) {
        return Reflect.fastInvokeResulted(BlockAccessor.getMethodGetId1(), blockDataToBlockState(blockDataHolder)).as(Integer.class);
        // TODO: check if this works for legacy too
    }

    protected abstract @NotNull Object materialHolderToItem(@NotNull ItemType material);

    protected abstract @NotNull Object blockDataToBlockState(@NotNull Block blockData);
}
