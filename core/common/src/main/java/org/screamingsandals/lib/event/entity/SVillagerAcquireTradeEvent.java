package org.screamingsandals.lib.event.entity;

import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;

public interface SVillagerAcquireTradeEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic getEntity();

    SPlayerCraftItemEvent.Recipe getRecipe();

    @Deprecated // because there's no proper Recipe API yet
    void setRecipe(SPlayerCraftItemEvent.Recipe recipe);
}
