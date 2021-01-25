package org.screamingsandals.lib.minestom.material;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.material.MaterialMapping;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Arrays;

@Service
public class MinestomMaterialMapping extends MaterialMapping {

    public static void init() {
        MaterialMapping.init(MinestomMaterialMapping::new);
    }

    public MinestomMaterialMapping() {
        platform = Platform.JAVA_FLATTENING;

        materialConverter
                .registerW2P(Material.class, holder -> Material.valueOf(holder.getPlatformName()))
                .registerW2P(ItemStack.class, holder -> new ItemStack(Material.valueOf(holder.getPlatformName()), (byte) 1, holder.getDurability()))
                .registerW2P(Block.class, holder -> Block.valueOf(holder.getPlatformName()))
                .registerP2W(Material.class, material -> new MaterialHolder(material.name()))
                .registerP2W(ItemStack.class, stack -> new MaterialHolder(stack.getMaterial().name()))
                .registerP2W(Block.class, block -> new MaterialHolder(block.getName()));

        Arrays.stream(Material.values()).forEach(material -> materialMapping.put(material.name().toUpperCase(), new MaterialHolder(material.name())));
    }
}
