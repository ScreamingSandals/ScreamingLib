package org.screamingsandals.lib.entity.type;

import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.key.MappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityTypeMapping {
    private static EntityTypeMapping entityTypeMapping;

    protected final BidirectionalConverter<EntityTypeHolder> entityTypeConverter = BidirectionalConverter.build();
    protected final Map<MappingKey, EntityTypeHolder> mapping = new HashMap<>();

    public static void init(Supplier<EntityTypeMapping> supplier) {
        if (entityTypeMapping != null) {
            throw new UnsupportedOperationException("EntityTypeMapping is already initialized.");
        }

        entityTypeMapping = supplier.get();
        entityTypeMapping.entityTypeConverter.finish();

        entityTypeMapping.legacyMapping();
    }

    public static Optional<EntityTypeHolder> resolve(Object entity) {
        if (entityTypeMapping == null) {
            throw new UnsupportedOperationException("EntityTypeMapping is not initialized yet.");
        }

        if (entity == null) {
            return Optional.empty();
        }

        var converted = entityTypeMapping.entityTypeConverter.convertOptional(entity);
        if (converted.isPresent()) {
            return converted;
        }

        var namespacedKey = NamespacedMappingKey.ofOptional(entity.toString());

        if (namespacedKey.isPresent()) {
            if (entityTypeMapping.mapping.containsKey(namespacedKey.get())) {
                return Optional.of(entityTypeMapping.mapping.get(namespacedKey.get()));
            }
        }

        return Optional.empty();
    }

    public static <T> T convertEntityTypeHolder(EntityTypeHolder holder, Class<T> newType) {
        if (entityTypeMapping == null) {
            throw new UnsupportedOperationException("EntityTypeMapping is not initialized yet.");
        }
        return entityTypeMapping.entityTypeConverter.convert(holder, newType);
    }

    public static boolean isInitialized() {
        return entityTypeMapping != null;
    }

    private void legacyMapping() {
        // Flattening <-> Bukkit
        f2l("ITEM", "DROPPED_ITEM");
        f2l("LEASH_KNOT", "LEASH_HITCH");
        f2l("EYE_OF_ENDER", "ENDER_SIGNAL");
        f2l("POTION", "SPLASH_POTION");
        f2l("EXPERIENCE_BOTTLE", "THROWN_EXP_BOTTLE");
        f2l("TNT", "PRIMED_TNT");
        f2l("FIREWORK_ROCKET", "FIREWORK");
        f2l("COMMAND_BLOCK_MINECART", "MINECART_COMMAND");
        f2l("CHEST_MINECART", "MINECART_CHEST");
        f2l("FURNACE_MINECART", "MINECART_FURNACE");
        f2l("TNT_MINECART", "MINECART_TNT");
        f2l("HOPPER_MINECART", "MINECART_HOPPER");
        f2l("SPAWNER_MINECART", "MINECART_MOB_SPAWNER");
        f2l("MOOSHROOM", "MUSHROOM_COW");
        f2l("SNOW_GOLEM", "SNOWMAN");
        f2l("END_CRYSTAL", "ENDER_CRYSTAL");
        f2l("FISHING_BOBBER", "FISHING_HOOK");
        f2l("LIGHTNING_BOLT", "LIGHTNING");

        // TODO check legacy
    }

    private void f2l(String entityType1, String entityType2) {
        if (entityType1 == null || entityType2 == null) {
            throw new IllegalArgumentException("Both effects mustn't be null!");
        }

        var namespace1 = NamespacedMappingKey.of(entityType1);
        var namespace2 = NamespacedMappingKey.of(entityType2);

        if (mapping.containsKey(namespace1) && !mapping.containsKey(namespace2)) {
            mapping.put(namespace2, mapping.get(namespace1));
        } else if (mapping.containsKey(namespace2) && !mapping.containsKey(namespace1)) {
            mapping.put(namespace1, mapping.get(namespace2));
        }
    }
}
