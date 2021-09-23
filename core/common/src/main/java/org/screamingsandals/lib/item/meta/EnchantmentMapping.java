package org.screamingsandals.lib.item.meta;

import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.RomanToDecimal;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService(pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$")
public abstract class EnchantmentMapping extends AbstractTypeMapper<EnchantmentHolder> {

    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(?<namespaced>[A-Za-z][A-Za-z0-9_.\\-/:]*)(\\s+(?<level>(\\d+|(?=[MDCLXVI])M*(C[MD]|D?C*)(X[CL]|L?X*)(I[XV]|V?I*)))?)?$");
    private static EnchantmentMapping enchantmentMapping;

    protected BidirectionalConverter<EnchantmentHolder> enchantmentConverter = BidirectionalConverter.<EnchantmentHolder>build()
            .registerW2P(String.class, EnchantmentHolder::getPlatformName)
            .registerP2W(EnchantmentHolder.class, e -> e)
            .registerP2W(Map.Entry.class, entry -> {
                Optional<EnchantmentHolder> holder = resolve(entry.getKey());
                if (holder.isPresent()) {
                    int level;
                    if (entry.getValue() instanceof Number) {
                        level = ((Number) entry.getValue()).intValue();
                    } else if (entry.getValue() instanceof ConfigurationNode) {
                        try {
                            level = Integer.parseInt(((ConfigurationNode) entry.getValue()).getString("1"));
                        } catch (Throwable t) {
                            level = RomanToDecimal.romanToDecimal(((ConfigurationNode) entry.getValue()).getString("I"));
                        }
                    } else {
                        try {
                            level = Integer.parseInt(entry.getValue().toString());
                        } catch (Throwable t) {
                            level = RomanToDecimal.romanToDecimal(entry.getValue().toString());
                        }
                    }
                    return holder.get().withLevel(level);
                }
                return null;
            });

    @CustomAutocompletion(CustomAutocompletion.Type.FIREWORK_EFFECT)
    @OfMethodAlternative(value = EnchantmentHolder.class, methodName = "ofOptional")
    public static Optional<EnchantmentHolder> resolve(Object enchantmentObject) {
        if (enchantmentMapping == null) {
            throw new UnsupportedOperationException("Enchantment mapping is not initialized yet.");
        }
        if (enchantmentObject == null) {
            return Optional.empty();
        }

        return enchantmentMapping.enchantmentConverter.convertOptional(enchantmentObject).or(() -> {
            var enchantment = enchantmentObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(enchantment);

            if (matcher.matches() && matcher.group("namespaced") != null) {

                var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

                String level_str = matcher.group("level");

                if (enchantmentMapping.mapping.containsKey(namespaced)) {
                    if (level_str != null && !level_str.isEmpty()) {
                        int level;
                        try {
                            level = Integer.parseInt(level_str);
                        } catch (Throwable t) {
                            level = RomanToDecimal.romanToDecimal(level_str);
                        }
                        return Optional.of(enchantmentMapping.mapping.get(namespaced).withLevel(level));
                    } else {
                        return Optional.of(enchantmentMapping.mapping.get(namespaced));
                    }
                }
            }

            return Optional.empty();
        });
    }

    @OfMethodAlternative(value = EnchantmentHolder.class, methodName = "all")
    public static List<EnchantmentHolder> getValues() {
        if (enchantmentMapping == null) {
            throw new UnsupportedOperationException("EnchantmentMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(enchantmentMapping.values);
    }

    @SneakyThrows
    protected EnchantmentMapping() {
        if (enchantmentMapping != null) {
            throw new UnsupportedOperationException("Enchantment mapping is already initialized.");
        }

        enchantmentMapping = this;
    }

    @OnPostConstruct
    public void legacyMapping() {
        mapAlias("POWER", "ARROW_DAMAGE");
        mapAlias("FLAME", "ARROW_FIRE");
        mapAlias("INFINITY", "ARROW_INFINITE");
        mapAlias("PUNCH", "ARROW_KNOCKBACK");
        mapAlias("SHARPNESS", "DAMAGE_ALL");
        mapAlias("BANE_OF_ARTHROPODS", "DAMAGE_ARTHROPODS");
        mapAlias("SMITE", "DAMAGE_UNDEAD");
        mapAlias("EFFICIENCY", "DIG_SPEED");
        mapAlias("UNBREAKING", "DURABILITY");
        mapAlias("FORTUNE", "LOOT_BONUS_BLOCKS");
        mapAlias("LOOTING", "LOOT_BONUS_MOBS");
        mapAlias("LUCK_OF_THE_SEA", "LUCK");
        mapAlias("RESPIRATION", "OXYGEN");
        mapAlias("PROTECTION", "PROTECTION_ENVIRONMENTAL");
        mapAlias("BLAST_PROTECTION", "PROTECTION_EXPLOSIONS");
        mapAlias("FEATHER_FALLING", "PROTECTION_FALL");
        mapAlias("FIRE_PROTECTION", "PROTECTION_FIRE");
        mapAlias("PROJECTILE_PROTECTION", "PROTECTION_PROJECTILE");
        mapAlias("SWEEPING", "SWEEPING_EDGE");
        mapAlias("AQUA_AFFINITY", "WATER_WORKER");
    }

    public static <T> T convertEnchantmentHolder(EnchantmentHolder holder, Class<T> newType) {
        if (enchantmentMapping == null) {
            throw new UnsupportedOperationException("Enchantment mapping is not initialized yet.");
        }
        return enchantmentMapping.enchantmentConverter.convert(holder, newType);
    }

}
