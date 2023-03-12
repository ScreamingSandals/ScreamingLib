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

package org.screamingsandals.lib.item;

import lombok.experimental.UtilityClass;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.Potion;
import org.screamingsandals.lib.metadata.MetadataCollectionKey;
import org.screamingsandals.lib.metadata.MetadataKey;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.key.ResourceLocation;

@UtilityClass
@Deprecated
public class ItemMeta {
    // Potions
    public static final MetadataKey<Potion> POTION_TYPE = MetadataKey.of("Potion", Potion.class);
    public static final MetadataKey<Color> CUSTOM_POTION_COLOR = MetadataKey.of("CustomPotionColor", Color.class);
    public static final MetadataCollectionKey<PotionEffectHolder> CUSTOM_POTION_EFFECTS = MetadataCollectionKey.of("CustomPotionEffects", PotionEffectHolder.class);

    // Knowledge Book
    public static final MetadataCollectionKey<ResourceLocation> RECIPES = MetadataCollectionKey.of("Recipes", ResourceLocation.class);

    // Fireworks
    public static final MetadataCollectionKey<FireworkEffectHolder> FIREWORK_EFFECTS = MetadataCollectionKey.of("Fireworks.Explosions", FireworkEffectHolder.class);
    public static final MetadataKey<Integer> FIREWORK_POWER = MetadataKey.of("Fireworks.Flight", Integer.class);

    // Firework star
    public static final MetadataKey<FireworkEffectHolder> FIREWORK_STAR_EFFECT = MetadataKey.of("Explosion", FireworkEffectHolder.class);

    // Leather armor
    public static final MetadataKey<Color> COLOR = MetadataKey.of("display.color", Color.class);

    // Skull Owner
    // TODO: Skull Owner is now a Compound Tag, so let's create a SkullHolder ;)
    public static final MetadataKey<String> SKULL_OWNER = MetadataKey.of("SkullOwner", String.class);
}
