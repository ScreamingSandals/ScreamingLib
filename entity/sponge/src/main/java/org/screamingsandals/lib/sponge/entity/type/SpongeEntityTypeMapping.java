package org.screamingsandals.lib.sponge.entity.type;

import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.entity.type.EntityTypeMapping;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.registry.RegistryTypes;

@Service
public class SpongeEntityTypeMapping extends EntityTypeMapping {
    public static void init() {
        EntityTypeMapping.init(SpongeEntityTypeMapping::new);
    }

    public SpongeEntityTypeMapping() {
        entityTypeConverter
                .registerW2P(EntityType.class, entityTypeHolder -> Sponge.getGame().registries().registry(RegistryTypes.ENTITY_TYPE).findEntry(ResourceKey.resolve(entityTypeHolder.getPlatformName())).orElseThrow().value())
                .registerP2W(EntityType.class, entityType -> new EntityTypeHolder(Sponge.getGame().registries().registry(RegistryTypes.ENTITY_TYPE).findValueKey(entityType).orElseThrow().getFormatted()));

        Sponge.getGame().registries().registry(RegistryTypes.ENTITY_TYPE).forEach(entityType ->
                mapping.put(NamespacedMappingKey.of(entityType.key().getFormatted()), new EntityTypeHolder(entityType.key().getFormatted()))
        );
    }
}
