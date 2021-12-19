package org.screamingsandals.lib.bukkit.metadata;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.item.meta.EnchantmentHolder;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.item.meta.PotionHolder;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.List;

public class MetadataValuesRemapper {
    private static final List<MetadataConverter<?>> CONVERTERS = List.of(
            new MetadataConverter<>(
                    PotionHolder.class,
                    PotionHolder::of,
                    PotionHolder::as
            ),
            new MetadataConverter<>(
                    RGBLike.class,
                    value -> {
                        if (value instanceof Color) {
                            return TextColor.color(((Color) value).getRed(), ((Color) value).getGreen(), ((Color) value).getBlue());
                        } else if (value instanceof DyeColor) {
                            return TextColor.color(((DyeColor) value).getColor().getRed(), ((DyeColor) value).getColor().getGreen(), ((DyeColor) value).getColor().getBlue());
                        }
                        throw new UnsupportedOperationException("Can't remap " + value.getClass().getName() + " to RGBLike");
                    },
                    (rgbLike, aClass) -> {
                        if (aClass == Color.class) {
                            return Color.fromRGB((rgbLike).red(), (rgbLike).green(), (rgbLike).blue());
                        } else if (aClass == DyeColor.class) {
                            return DyeColor.getByColor(Color.fromRGB((rgbLike).red(), (rgbLike).green(), (rgbLike).blue()));
                        }
                        throw new UnsupportedOperationException("Can't remap RGBLike to " + aClass.getName());
                    }
            ),
            new MetadataConverter<>(
                    PotionEffectHolder.class,
                    PotionEffectHolder::of,
                    PotionEffectHolder::as
            ),
            new MetadataConverter<>(
                    EnchantmentHolder.class,
                    EnchantmentHolder::of,
                    EnchantmentHolder::as
            ),
            new MetadataConverter<>(
                    FireworkEffectHolder.class,
                    FireworkEffectHolder::of,
                    FireworkEffectHolder::as
            ),
            new MetadataConverter<>(
                    NamespacedMappingKey.class,
                    value -> NamespacedMappingKey.of(value.toString()),
                    (namespacedMappingKey, aClass) -> {
                        if (aClass == String.class) {
                            return namespacedMappingKey.asString();
                        } else if (Reflect.has("org.bukkit.NamespacedKey")) {
                            if (aClass == NamespacedKey.class) {
                                return new NamespacedKey(namespacedMappingKey.namespace(), namespacedMappingKey.value());
                            }
                        }
                        throw new UnsupportedOperationException("Can't remap NamespacedMappingKey to " + aClass.getName());
                    }
            )
    );

    public static Object remapToWrapper(Object object, Class<?> theResultClass) {
        if (object == null) {
            return null;
        }
        // class can be primitive, so we should convert it to the boxed type (object can't be a primitive)
        if (theResultClass.isPrimitive()) {
            if (theResultClass == byte.class) {
                theResultClass = Byte.class;
            } else if (theResultClass == short.class) {
                theResultClass = Short.class;
            } else if (theResultClass == int.class) {
                theResultClass = Integer.class;
            } else if (theResultClass == long.class) {
                theResultClass = Long.class;
            } else if (theResultClass == float.class) {
                theResultClass = Float.class;
            } else if (theResultClass == double.class) {
                theResultClass = Double.class;
            } else if (theResultClass == boolean.class) {
                theResultClass = Boolean.class;
            } else if (theResultClass == char.class) {
                theResultClass = Character.class;
            } else if (theResultClass == void.class) {
                theResultClass = Void.class;
            }
        }
        if (theResultClass.isAssignableFrom(object.getClass())) {
            return object;
        }
        var finalTheResultClass = theResultClass;
        return CONVERTERS.stream()
                .filter(metadataConverter -> finalTheResultClass.equals(metadataConverter.getRequiredClass()))
                .findFirst()
                .orElseThrow()
                .getMapToWrapper()
                .apply(object);
    }


    @SuppressWarnings("unchecked")
    public static Object remapToPlatform(Object object, Class<?> theResultClass) {
        if (object == null) {
            return null;
        }
        // class can be primitive, so we should convert it to the boxed type (object can't be a primitive)
        if (theResultClass.isPrimitive()) {
            if (theResultClass == byte.class) {
                theResultClass = Byte.class;
            } else if (theResultClass == short.class) {
                theResultClass = Short.class;
            } else if (theResultClass == int.class) {
                theResultClass = Integer.class;
            } else if (theResultClass == long.class) {
                theResultClass = Long.class;
            } else if (theResultClass == float.class) {
                theResultClass = Float.class;
            } else if (theResultClass == double.class) {
                theResultClass = Double.class;
            } else if (theResultClass == boolean.class) {
                theResultClass = Boolean.class;
            } else if (theResultClass == char.class) {
                theResultClass = Character.class;
            } else if (theResultClass == void.class) {
                theResultClass = Void.class;
            }
        }
        if (theResultClass.isAssignableFrom(object.getClass())) {
            return object;
        }
        var finalTheResultClass = theResultClass;
        return ((MetadataConverter) CONVERTERS.stream()
                .filter(metadataConverter -> metadataConverter.getRequiredClass().isInstance(object))
                .findFirst()
                .orElseThrow())
                .getMapToPlatform()
                .apply(object, finalTheResultClass);
    }
}
