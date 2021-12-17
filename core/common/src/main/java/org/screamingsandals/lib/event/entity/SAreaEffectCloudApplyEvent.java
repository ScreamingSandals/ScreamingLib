package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;

import java.util.Collection;

public interface SAreaEffectCloudApplyEvent extends SCancellableEvent, PlatformEventWrapper {
    EntityBasic getEntity();

    Collection<EntityBasic> getAffectedEntities();
}
