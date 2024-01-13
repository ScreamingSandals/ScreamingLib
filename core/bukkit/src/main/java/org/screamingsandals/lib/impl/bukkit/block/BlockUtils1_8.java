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

package org.screamingsandals.lib.impl.bukkit.block;

import lombok.experimental.UtilityClass;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Bed;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.block.Block;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.BlockEntityAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ItemAccessor;
import org.screamingsandals.lib.impl.nms.accessors.TileEntityFlowerPotAccessor;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
@LimitedVersionSupport("<= 1.12.2")
public class BlockUtils1_8 {
    public @NotNull Block getBlock(@NotNull BlockState state) {
        int tileEntityData = 0;
        if (BukkitFeature.COLORED_BEDS.isSupported()) {
            if (state instanceof org.bukkit.block.Bed) {
                tileEntityData = ((Bed) state).getColor().getWoolData();
            }
        }
        if (state instanceof org.bukkit.block.Skull) {
            tileEntityData = ((Skull) state).getSkullType().ordinal();
        }
        if (state.getType() == Material.FLOWER_POT) {
            if (BukkitFeature.FLOWER_POT_BLOCK_STATE.isSupported()) {
                var materialData = (MaterialData) Reflect.fastInvoke(state, "getContents");
                if (materialData != null) {
                    tileEntityData = (materialData.getItemType().getId() << 4) | materialData.getData();
                }
            } else {
                var tile = Reflect.getMethod(state.getWorld(), "getTileEntityAt", int.class, int.class, int.class)
                        .invoke(state.getX(), state.getY(), state.getZ());
                if (tile != null) {
                    var id = (Integer) Reflect.fastInvoke(ItemAccessor.getMethodGetId1(), Reflect.fastInvoke(tile, TileEntityFlowerPotAccessor.getMethodGetItem1()));
                    if (id != null) {
                        var data = (Integer) Reflect.fastInvoke(tile, TileEntityFlowerPotAccessor.getMethodGetData1());
                        tileEntityData = id << 4 | data;
                    }
                }
            }
        }
        return new BukkitBlock1_8(state.getData(), tileEntityData);
    }

    public void finishSettingBlock(@NotNull BlockState state, @NotNull BukkitBlock1_8 type, boolean updateState) {
        int tileEntityVariant = type.tileEntityVariant();
        if (tileEntityVariant != 0) {
            if (BukkitFeature.COLORED_BEDS.isSupported() && state instanceof org.bukkit.block.Bed) {
                ((Bed) state).setColor(DyeColor.getByWoolData((byte) tileEntityVariant));
                if (updateState) {
                    state.update(true, false);
                }
            } else if (state instanceof org.bukkit.block.Skull) {
                ((Skull) state).setSkullType(SkullType.values()[tileEntityVariant]);
                if (updateState) {
                    state.update(true, false);
                }
            } else if (state.getType() == Material.FLOWER_POT) {
                int id = tileEntityVariant >> 4;
                int data = tileEntityVariant & 0xF;
                var flowerPotMaterial = (Material) Reflect.getMethod(Material.class, "getMaterial", int.class).invokeStatic(id);
                if (flowerPotMaterial != null) {
                    if (BukkitFeature.FLOWER_POT_BLOCK_STATE.isSupported()) {
                        Reflect.getMethod(state, "setContents", MaterialData.class).invoke(flowerPotMaterial.getNewData((byte) data));
                        if (updateState) {
                            state.update(true, false);
                        }
                    } else {
                        var tile = Reflect.getMethod(state.getWorld(), "getTileEntityAt", int.class, int.class, int.class)
                                .invoke(state.getX(), state.getY(), state.getZ());
                        if (tile != null) {
                            Reflect.fastInvoke(tile, TileEntityFlowerPotAccessor.getMethodFunc_145964_a1(), Reflect.getMethod(ClassStorage.CB.CraftMagicNumbers, "getItem", Material.class).invokeStatic(flowerPotMaterial), data);
                            if (updateState) {
                                Reflect.fastInvoke(tile, BlockEntityAccessor.getMethodSetChanged1());
                            }
                        }
                    }
                }
            }
        }
    }
}
