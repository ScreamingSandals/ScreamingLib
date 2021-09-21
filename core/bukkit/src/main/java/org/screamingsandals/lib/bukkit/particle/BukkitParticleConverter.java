package org.screamingsandals.lib.bukkit.particle;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.bukkit.utils.nms.Version;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.particle.DustOptions;
import org.screamingsandals.lib.particle.DustTransition;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class BukkitParticleConverter {
    public Object convertParticleData(ParticleData data) {
        if (data instanceof BlockTypeHolder) {
            if (Version.isVersion(1, 13)) {
                return data.as(BlockData.class);
            } else {
                return data.as(MaterialData.class);
            }
        } else if (data instanceof ItemTypeHolder) {
            return data.as(ItemStack.class);
        } else if (data instanceof Item) {
            return data.as(ItemStack.class);
        } else if (data instanceof DustOptions) {
            return new Particle.DustOptions(getBukkitColor(((DustOptions) data).color()), ((DustOptions) data).size());
        } else if (data instanceof DustTransition) {
            // TODO: Fix gradle and remove this reflection
            return Reflect.constructor("org.bukkit.Particle$DustTransition", Color.class, Color.class, float.class)
                    .construct(getBukkitColor(((DustTransition) data).fromColor()), getBukkitColor(((DustTransition) data).toColor()), ((DustTransition) data).size());
        }
        return null;
    }

    public Color getBukkitColor(RGBLike rgb) {
        return Color.fromRGB(rgb.red(), rgb.green(), rgb.blue());
    }
}
