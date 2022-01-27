package org.screamingsandals.lib.kotlin

import org.jetbrains.annotations.ApiStatus
import org.screamingsandals.lib.utils.ComparableWrapper
import org.screamingsandals.lib.utils.Wrapper
import kotlin.reflect.KClass

infix fun <T : Any> Wrapper.unwrap(type: KClass<T>) : T {
    return this.`as`(type.java)
}

@ApiStatus.Experimental
infix fun <T: Any> ComparableWrapper.compare(type : T) : Boolean {
    return this.`is`(type)
}

@ApiStatus.Experimental
fun <T: Any> ComparableWrapper.compare(vararg type : T) : Boolean {
    return this.`is`(type)
}