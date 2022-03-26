package org.screamingsandals.lib.kotlin

import org.screamingsandals.lib.event.Cancellable

interface SCancellableKt : Cancellable {
    var cancelled: Boolean

    override fun cancelled(): Boolean = cancelled
    override fun cancelled(cancel: Boolean) {
        this.cancelled = cancel
    }
}