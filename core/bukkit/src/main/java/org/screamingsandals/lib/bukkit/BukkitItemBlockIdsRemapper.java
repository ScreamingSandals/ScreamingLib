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

package org.screamingsandals.lib.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.ItemBlockIdsRemapper;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitItemBlockIdsRemapper extends ItemBlockIdsRemapper {
    @Getter
    private static final int versionNumber;
    @Getter
    private static final @NotNull Platform bPlatform;

    static {
        var bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        var ver = 0;

        for (int i = 0; i < 2; i++) {
            ver += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }
        versionNumber = ver;

        bPlatform = versionNumber < 113 ? Platform.JAVA_LEGACY : Platform.JAVA_FLATTENING;
    }

    public BukkitItemBlockIdsRemapper(@NotNull ItemTypeMapper itemTypeMapper, @NotNull BlockTypeMapper blockTypeMapper) {
        super(itemTypeMapper, blockTypeMapper, bPlatform);

        if (versionNumber < 112) {
            mappingFlags.add(MappingFlags.NO_COLORED_BEDS);
        }
    }
}
