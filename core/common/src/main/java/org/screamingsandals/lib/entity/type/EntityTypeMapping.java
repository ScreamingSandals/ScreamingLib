package org.screamingsandals.lib.entity.type;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class EntityTypeMapping extends AbstractTypeMapper<EntityTypeHolder> {
    private static EntityTypeMapping entityTypeMapping;

    protected final BidirectionalConverter<EntityTypeHolder> entityTypeConverter = BidirectionalConverter.<EntityTypeHolder>build()
            .registerP2W(EntityTypeHolder.class, e -> e);

    @ApiStatus.Internal
    public EntityTypeMapping() {
        if (entityTypeMapping != null) {
            throw new UnsupportedOperationException("EntityTypeMapping is already initialized.");
        }

        entityTypeMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_TYPE)
    @OfMethodAlternative(value = EntityTypeHolder.class, methodName = "ofOptional")
    public static Optional<EntityTypeHolder> resolve(Object entity) {
        if (entityTypeMapping == null) {
            throw new UnsupportedOperationException("EntityTypeMapping is not initialized yet.");
        }

        if (entity == null) {
            return Optional.empty();
        }

        return entityTypeMapping.entityTypeConverter.convertOptional(entity).or(() -> entityTypeMapping.resolveFromMapping(entity));
    }

    @OfMethodAlternative(value = EntityTypeHolder.class, methodName = "all")
    public static List<EntityTypeHolder> getValues() {
        if (entityTypeMapping == null) {
            throw new UnsupportedOperationException("EntityTypeMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(entityTypeMapping.values);
    }

    @OnPostConstruct
    public void legacyMapping() {
        // Flattening <-> Bukkit
        mapAlias("ITEM", "DROPPED_ITEM");
        mapAlias("LEASH_KNOT", "LEASH_HITCH");
        mapAlias("EYE_OF_ENDER", "ENDER_SIGNAL");
        mapAlias("POTION", "SPLASH_POTION");
        mapAlias("EXPERIENCE_BOTTLE", "THROWN_EXP_BOTTLE");
        mapAlias("TNT", "PRIMED_TNT");
        mapAlias("FIREWORK_ROCKET", "FIREWORK");
        mapAlias("COMMAND_BLOCK_MINECART", "MINECART_COMMAND");
        mapAlias("CHEST_MINECART", "MINECART_CHEST");
        mapAlias("FURNACE_MINECART", "MINECART_FURNACE");
        mapAlias("TNT_MINECART", "MINECART_TNT");
        mapAlias("HOPPER_MINECART", "MINECART_HOPPER");
        mapAlias("SPAWNER_MINECART", "MINECART_MOB_SPAWNER");
        mapAlias("MOOSHROOM", "MUSHROOM_COW");
        mapAlias("SNOW_GOLEM", "SNOWMAN");
        mapAlias("END_CRYSTAL", "ENDER_CRYSTAL");
        mapAlias("FISHING_BOBBER", "FISHING_HOOK");
        mapAlias("LIGHTNING_BOLT", "LIGHTNING");

        // TODO check legacy
    }
}
