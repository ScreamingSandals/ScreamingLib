package org.screamingsandals.lib.kotlin

import org.screamingsandals.lib.utils.Wrapper
import kotlin.reflect.KClass

infix fun <T : Any> Wrapper.unwrap(type: KClass<T>) : T {
    return this.`as`(type.java)
}