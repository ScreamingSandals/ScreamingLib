package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLightning;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SCreeperPowerEvent extends SCancellableEvent {
    EntityBasic getEntity();

    @Nullable
    EntityLightning getBolt();

    PowerCause getCause();

    /**
     * An enum to specify the cause of the change in power
     */
    // TODO: holder?
    enum PowerCause {

        /**
         * Power change caused by a lightning bolt
         * <p>
         * Powered state: true
         */
        LIGHTNING,
        /**
         * Power change caused by something else (probably a plugin)
         * <p>
         * Powered state: true
         */
        SET_ON,
        /**
         * Power change caused by something else (probably a plugin)
         * <p>
         * Powered state: false
         */
        SET_OFF
    }
}
