package org.screamingsandals.lib.bukkit;

import lombok.Getter;
import org.bukkit.Bukkit;
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
    private static final Platform bPlatform;

    static {
        var bukkitVersion = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
        var ver = 0;

        for (int i = 0; i < 2; i++) {
            ver += Integer.parseInt(bukkitVersion[i]) * (i == 0 ? 100 : 1);
        }
        versionNumber = ver;

        bPlatform = versionNumber < 113 ? Platform.JAVA_LEGACY : Platform.JAVA_FLATTENING;
    }

    public BukkitItemBlockIdsRemapper(ItemTypeMapper itemTypeMapper, BlockTypeMapper blockTypeMapper) {
        super(itemTypeMapper, blockTypeMapper);

        super.platform = bPlatform;

        if (versionNumber < 112) {
            mappingFlags.add(MappingFlags.NO_COLORED_BEDS);
        }
    }
}
