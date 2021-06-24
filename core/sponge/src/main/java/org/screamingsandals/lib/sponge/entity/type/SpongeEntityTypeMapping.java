package org.screamingsandals.lib.sponge.entity.type;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.sponge.utils.SpongeRegistryMapper;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.registry.RegistryType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeEntityTypeMapping extends EntityTypeMapping implements SpongeRegistryMapper<EntityType<?>> {
    public static void init() {
        EntityTypeMapping.init(SpongeEntityTypeMapping::new);
    }

    public SpongeEntityTypeMapping() {
        entityTypeConverter
                .registerW2P(EntityType.class, entityTypeHolder -> getEntry(entityTypeHolder.getPlatformName()).value())
                .registerP2W(EntityType.class, entityType -> new EntityTypeHolder(getKeyByValue(entityType).getFormatted()));

        getAllKeys().forEach(key ->
                mapping.put(NamespacedMappingKey.of(key.getFormatted()), new EntityTypeHolder(key.getFormatted()))
        );
    }

    @Override
    @NotNull
    public RegistryType<EntityType<?>> getRegistryType() {
        return RegistryTypes.ENTITY_TYPE;
    }
}
