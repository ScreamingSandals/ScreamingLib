package org.screamingsandals.lib.sponge.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.sponge.entity.type.SpongeEntityTypeMapping;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.Optional;

@Service(dependsOn = {
        SpongeEntityTypeMapping.class
})
public class SpongeEntityMapper extends EntityMapper {

    public static void init() {
        EntityMapper.init(SpongeEntityMapper::new);
    }

    public SpongeEntityMapper() {
        InitUtils.doIfNot(SpongeEntityTypeMapping::isInitialized, SpongeEntityTypeMapping::init);
    }

    @Override
    protected <T extends EntityBasic> Optional<T> wrapEntity0(Object entity) {
        return Optional.empty();
    }
}
