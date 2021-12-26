package org.screamingsandals.lib.item;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.util.RGBLike;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@UtilityClass
// TODO: map the rest
public class ItemMeta {
    // Potions
    public static final MetadataKey<PotionHolder> POTION_TYPE = MetadataKey.of("Potion", PotionHolder.class);
    public static final MetadataKey<RGBLike> CUSTOM_POTION_COLOR = MetadataKey.of("CustomPotionColor", RGBLike.class);
    public static final MetadataCollectionKey<PotionEffectHolder> CUSTOM_POTION_EFFECTS = MetadataCollectionKey.of("CustomPotionEffects", PotionEffectHolder.class);

    // Knowledge Book
    public static final MetadataCollectionKey<NamespacedMappingKey> RECIPES = MetadataCollectionKey.of("Recipes", NamespacedMappingKey.class);

    // Fireworks
    public static final MetadataCollectionKey<FireworkEffectHolder> FIREWORK_EFFECTS = MetadataCollectionKey.of("Fireworks.Explosions", FireworkEffectHolder.class);
    public static final MetadataKey<Integer> FIREWORK_POWER = MetadataKey.of("Fireworks.Flight", Integer.class);

    // Firework star
    public static final MetadataKey<FireworkEffectHolder> FIREWORK_STAR_EFFECT = MetadataKey.of("Explosion", FireworkEffectHolder.class);

    // Leather armor
    public static final MetadataKey<RGBLike> COLOR = MetadataKey.of("display.color", RGBLike.class);

    // Skull Owner
    // TODO: Skull Owner is now a Compound Tag, so let's create a SkullHolder ;)
    public static final MetadataKey<String> SKULL_OWNER = MetadataKey.of("SkullOwner", String.class);
}
