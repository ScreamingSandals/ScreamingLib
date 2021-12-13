package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.SCancellableEvent;

import java.util.Collection;

public interface SAreaEffectCloudApplyEvent extends SCancellableEvent {
    EntityBasic getEntity();

    Collection<EntityBasic> getAffectedEntities();
}
