package org.screamingsandals.lib.bukkit.firework;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.firework.FireworkEffectMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class BukkitFireworkEffectMapping extends FireworkEffectMapping {
    public static void init() {
        FireworkEffectMapping.init(BukkitFireworkEffectMapping::new);
    }

    public BukkitFireworkEffectMapping() {
        fireworkEffectConverter
                .registerW2P(FireworkEffect.Type.class, fireworkEffectHolder -> FireworkEffect.Type.valueOf(fireworkEffectHolder.getPlatformName()))
                .registerP2W(FireworkEffect.Type.class, type -> new FireworkEffectHolder(type.name()))
                .registerW2P(FireworkEffect.class, fireworkEffectHolder -> FireworkEffect.builder()
                                .with(FireworkEffect.Type.valueOf(fireworkEffectHolder.getPlatformName()))
                                .flicker(fireworkEffectHolder.isFlicker())
                                .trail(fireworkEffectHolder.isTrail())
                                .withColor(fireworkEffectHolder.getColors().stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                                .withFade(fireworkEffectHolder.getFadeColors().stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                                .build()
                )
                .registerP2W(FireworkEffect.class, effect -> new FireworkEffectHolder(
                                effect.getType().name(),
                                effect.getColors().stream().map(color -> TextColor.color(color.getRed(), color.getGreen(), color.getBlue())).collect(Collectors.toList()),
                                effect.getFadeColors().stream().map(color -> TextColor.color(color.getRed(), color.getGreen(), color.getBlue())).collect(Collectors.toList()),
                                effect.hasFlicker(),
                                effect.hasTrail()
                        )
                );

        Arrays.stream(FireworkEffect.Type.values()).forEach(fireworkEffectType ->
                mapping.put(NamespacedMappingKey.of(fireworkEffectType.name()), new FireworkEffectHolder(fireworkEffectType.name()))
        );
    }
}
