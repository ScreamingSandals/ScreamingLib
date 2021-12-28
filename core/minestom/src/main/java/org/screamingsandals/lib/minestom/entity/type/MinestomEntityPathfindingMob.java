package org.screamingsandals.lib.minestom.entity.type;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.pathfinding.NavigableEntity;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityPathfindingMob;
import org.screamingsandals.lib.minestom.entity.MinestomEntityLiving;
import org.screamingsandals.lib.minestom.world.MinestomLocationMapper;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.Optional;

public class MinestomEntityPathfindingMob extends MinestomEntityLiving implements EntityPathfindingMob {
    protected <T extends LivingEntity & NavigableEntity> MinestomEntityPathfindingMob(T wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void setCurrentTarget(@Nullable EntityLiving target) {
        ((NavigableEntity) wrappedObject).getNavigator().setPathTo(target != null ? target.getLocation().as(Pos.class) : null);
    }

    @Override
    public Optional<EntityLiving> getCurrentTarget() {
        return Optional.ofNullable(((NavigableEntity) wrappedObject).getNavigator().getPathPosition())
                .map(pos -> MinestomLocationMapper.wrapPoint(pos, WorldMapper.wrapWorld(wrappedObject.getInstance())))
                .map(pos -> pos.getNearbyEntitiesByClass(EntityLiving.class, 5))
                .flatMap(entities -> Optional.ofNullable(entities.get(0)));
    }
}
