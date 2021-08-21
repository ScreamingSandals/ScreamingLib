package org.screamingsandals.lib.bukkit.world;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.BlockHolder;
import org.screamingsandals.lib.world.BlockMapper;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

@Service
public class BukkitBlockMapper extends BlockMapper {

    public BukkitBlockMapper() {
        converter.registerP2W(Location.class, location -> {
                    final var block = location.getBlock();
                    final var instanced = LocationMapper.resolve(block.getLocation()).orElseThrow(); // normalize to block location
                    final var material = block.getType();
                    if (!Version.isVersion(1,13)) {
                        return new BlockHolder(instanced, MaterialHolder.of(material.name() + ":" + location.getBlock().getData()));
                    } else {
                        return new BlockHolder(instanced, MaterialHolder.of(material));
                    }
                })
                .registerP2W(Block.class, block ->
                        resolve(block.getLocation()).orElseThrow())
                .registerP2W(LocationHolder.class, location ->
                        resolve(location.as(Location.class)).orElseThrow())
                .registerW2P(Block.class, holder -> {
                    final var location = holder.getLocation().as(Location.class);
                    return location.getBlock();
                });

        if (Reflect.has("org.bukkit.block.data.BlockData")) {
           converter.registerW2P(BlockData.class, holder -> {
                final var data = holder.getBlockData().orElse(null);
                if (data == null) {
                    return null;
                }

                return data.as(BlockData.class);
           });
        }
    }

    @Override
    protected BlockHolder getBlockAt0(LocationHolder location) {
        return resolve(location).orElseThrow();
    }

    @Override
    protected void setBlockAt0(LocationHolder location, MaterialHolder material) {
        final var bukkitLocation = location.as(Location.class);
        PaperLib.getChunkAtAsync(bukkitLocation)
                .thenAccept(result -> {
                    bukkitLocation.getBlock().setType(material.as(Material.class));
                    if (!Version.isVersion(1,13)) {
                        Reflect.getMethod(bukkitLocation.getBlock(), "setData", byte.class).invoke((byte) material.getDurability());
                    }
                });
    }

    @Override
    protected void breakNaturally0(LocationHolder location) {
        location.as(Location.class).getBlock().breakNaturally();
    }
}
