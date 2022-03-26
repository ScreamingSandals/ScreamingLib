/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.kotlin

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.screamingsandals.lib.container.Container
import org.screamingsandals.lib.entity.EntityBasic
import org.screamingsandals.lib.event.Cancellable
import org.screamingsandals.lib.event.EventManager
import org.screamingsandals.lib.event.SEvent
import org.screamingsandals.lib.item.Item
import org.screamingsandals.lib.player.PlayerWrapper
import org.screamingsandals.lib.utils.ComparableWrapper
import org.screamingsandals.lib.utils.Wrapper
import org.screamingsandals.lib.utils.math.Vector2D
import org.screamingsandals.lib.utils.math.Vector3D
import org.screamingsandals.lib.utils.math.Vector3Df
import org.screamingsandals.lib.utils.math.Vector3Di
import org.screamingsandals.lib.visuals.LinedVisual
import org.screamingsandals.lib.visuals.Visual
import org.screamingsandals.lib.world.LocationHolder
import org.screamingsandals.lib.world.WorldHolder
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

infix fun <T : Any> Wrapper.unwrap(type: KClass<T>): T = this.`as`(type.java)
@ApiStatus.Experimental
infix fun ComparableWrapper.compare(type: Any): Boolean = this.`is`(type)
@ApiStatus.Experimental
fun ComparableWrapper.compare(vararg type: Any): Boolean = this.`is`(type)

fun <K : SEvent> K.fire(): K = EventManager.fire(this)
infix fun <K : SEvent> K.fire(manager: EventManager): K = manager.fireEvent(this)

fun <K : SEvent> K.fireAsync(): CompletableFuture<K> = EventManager.fireAsync(this)
infix fun <K : SEvent> K.fireAsync(manager: EventManager): CompletableFuture<K> = manager.fireEventAsync(this)

operator fun Vector2D.unaryMinus(): Vector2D = this.clone().invert()
operator fun Vector2D.plus(vec: Vector2D): Vector2D = this.clone().add(vec)
operator fun Vector2D.minus(vec: Vector2D): Vector2D = this.clone().subtract(vec)
operator fun Vector2D.times(multiplier: Double): Vector2D = this.clone().multiply(multiplier)

operator fun Vector3D.unaryMinus(): Vector3D = this.clone().invert()
operator fun Vector3D.plus(vec: Vector3D): Vector3D = this.clone().add(vec)
operator fun Vector3D.minus(vec: Vector3D): Vector3D = this.clone().subtract(vec)
operator fun Vector3D.times(multiplier: Double): Vector3D = this.clone().multiply(multiplier)

operator fun Vector3Df.unaryMinus(): Vector3Df = this.clone().invert()
operator fun Vector3Df.plus(vec: Vector3Df): Vector3Df = this.clone().add(vec)
operator fun Vector3Df.minus(vec: Vector3Df): Vector3Df = this.clone().subtract(vec)
operator fun Vector3Df.times(multiplier: Float): Vector3Df = this.clone().multiply(multiplier)

operator fun Vector3Di.unaryMinus(): Vector3Di = this.clone().invert()
operator fun Vector3Di.plus(vec: Vector3Di): Vector3Di = this.clone().add(vec)
operator fun Vector3Di.minus(vec: Vector3Di): Vector3Di = this.clone().subtract(vec)
operator fun Vector3Di.times(multiplier: Int): Vector3Di = this.clone().multiply(multiplier)

operator fun WorldHolder.contains(entity: EntityBasic): Boolean = this == entity.location.world

operator fun LocationHolder.minus(loc: LocationHolder): Double = this.getDistanceSquared(loc)

operator fun Container.plusAssign(item: Item) {
    this.addItem(item)
}
operator fun Container.minusAssign(item: Item) {
    this.removeItem(item)
}

operator fun Visual<*>.plusAssign(viewer: PlayerWrapper) {
    this.addViewer(viewer)
}
operator fun Visual<*>.minusAssign(viewer: PlayerWrapper) {
    this.removeViewer(viewer)
}
operator fun Visual<*>.contains(viewer: PlayerWrapper) = this.visibleTo(viewer)

operator fun LinedVisual<*>.plusAssign(line: Component) {
    this.newLine(this.lines().size, line)
}

var Cancellable.cancelled: Boolean
    get() = cancelled()
    set(value) = cancelled(value)
