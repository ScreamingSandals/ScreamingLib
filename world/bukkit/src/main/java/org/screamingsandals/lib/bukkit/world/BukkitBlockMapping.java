package org.screamingsandals.lib.bukkit.world;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.annotations.PlatformMapping;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapping;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapping;

@PlatformMapping(platform = PlatformType.BUKKIT)
public class BukkitBlockMapping extends BlockMapping {

    public static void init() {
        BlockMapping.init(BukkitBlockMapping::new);
    }

    public BukkitBlockMapping() {
        converter
                .registerP2W(Location.class, location -> {
                    final var instanced = LocationMapping.resolve(location).orElseThrow();
                    final var material = location.getBlock().getBlockData().getMaterial();
                    return new BlockHolder(instanced, MaterialMapping.resolve(material).orElseThrow());
                })
                .registerP2W(Block.class, block -> {
                    final var material = block.getBlockData().getMaterial();
                    return new BlockHolder(LocationMapping.resolve(block.getLocation()).orElseThrow(),
                            MaterialMapping.resolve(material).orElseThrow());
                })
                .registerW2P(Block.class, holder -> {
                    final var location = holder.getLocation().as(Location.class);
                    return location.getBlock();
                })
                .registerW2P(BlockData.class, holder -> {
                    final var location = holder.getLocation().as(Location.class);
                    final var block = location.getBlock();
                    return block.getBlockData();
                });
    }

    @Override
    protected BlockHolder getBlockAt0(LocationHolder location) {
        final var bukkitLocation = location.as(Location.class);
        final var material = bukkitLocation.getBlock().getBlockData().getMaterial();
        return new BlockHolder(location, MaterialMapping.resolve(material).orElseThrow());
    }

    @Override
    protected void setBlockAt0(LocationHolder location, MaterialHolder material) {
        final var bukkitLocation = location.as(Location.class);
        PaperLib.getChunkAtAsync(bukkitLocation)
                .thenAccept(result ->
                        bukkitLocation.getBlock().setType(material.as(Material.class)));
    }
}
