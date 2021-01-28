package org.screamingsandals.lib.minestom.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.minestom.entity.type.MinestomEntityTypeMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        MinestomEntityTypeMapping.class
})
public class MinestomEntityMapper extends EntityMapper {

    public static void init() {
        EntityMapper.init(MinestomEntityMapper::new);
    }

    public MinestomEntityMapper() {
        InitUtils.doIfNot(MinestomEntityTypeMapping::isInitialized, MinestomEntityTypeMapping::init);
    }

    @Override
    protected Optional<EntityBasic> wrapEntity0(Object entity) {
        return Optional.empty();
    }
}
