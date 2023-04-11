/*
 * Copyright 2023 ScreamingSandals
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

@file:Suppress("NOTHING_TO_INLINE")

package org.screamingsandals.lib.kotlin

import org.screamingsandals.lib.container.Container
import org.screamingsandals.lib.entity.Entity
import org.screamingsandals.lib.event.Cancellable
import org.screamingsandals.lib.event.EventManager
import org.screamingsandals.lib.event.SEvent
import org.screamingsandals.lib.item.ItemStack
import org.screamingsandals.lib.player.Player
import org.screamingsandals.lib.spectator.Component
import org.screamingsandals.lib.utils.ComparableWrapper
import org.screamingsandals.lib.api.Wrapper
import org.screamingsandals.lib.item.ItemType
import org.screamingsandals.lib.item.builder.ItemStackFactory
import org.screamingsandals.lib.spectator.ComponentLike
import org.screamingsandals.lib.utils.math.Vector2D
import org.screamingsandals.lib.utils.math.Vector3D
import org.screamingsandals.lib.utils.math.Vector3Df
import org.screamingsandals.lib.utils.math.Vector3Di
import org.screamingsandals.lib.utils.visual.TextEntry
import org.screamingsandals.lib.visuals.LinedVisual
import org.screamingsandals.lib.visuals.Visual
import org.screamingsandals.lib.world.Location
import org.screamingsandals.lib.world.World
import java.util.concurrent.CompletableFuture
import kotlin.reflect.KClass

// wrapper
inline infix fun <T : Any> Wrapper.unwrap(type: KClass<T>): T = this.`as`(type.java)

// comparableWrapper in stringArray|stringCollection|string|anotherComparableWrapper

inline operator fun Array<String>.contains(holder: ComparableWrapper): Boolean = holder.`is`(*this)
inline operator fun Collection<String>.contains(holder: ComparableWrapper): Boolean = holder.`is`(*this.toTypedArray())
inline operator fun String.contains(holder: ComparableWrapper): Boolean = holder.`is`(this)
inline operator fun <T : ComparableWrapper> T.contains(holder: T): Boolean = holder == this

// events

inline fun <K : SEvent> K.fire(): K = EventManager.fire(this)
inline infix fun <K : SEvent> K.fire(manager: EventManager): K = manager.fireEvent(this)

inline fun <K : SEvent> K.fireAsync(): CompletableFuture<K> = EventManager.fireAsync(this)
inline infix fun <K : SEvent> K.fireAsync(manager: EventManager): CompletableFuture<K> = manager.fireEventAsync(this)

inline var Cancellable.cancelled: Boolean
    get() = cancelled()
    set(value) = cancelled(value)

// newVector = vector1 +|- vector2;
// inverseVector = -vector3

inline operator fun Vector2D.unaryMinus(): Vector2D = this.clone().invert()
inline operator fun Vector2D.plus(vec: Vector2D): Vector2D = this.clone().add(vec)
inline operator fun Vector2D.minus(vec: Vector2D): Vector2D = this.clone().subtract(vec)
inline operator fun Vector2D.times(multiplier: Double): Vector2D = this.clone().multiply(multiplier)

inline operator fun Vector3D.unaryMinus(): Vector3D = this.clone().invert()
inline operator fun Vector3D.plus(vec: Vector3D): Vector3D = this.clone().add(vec)
inline operator fun Vector3D.minus(vec: Vector3D): Vector3D = this.clone().subtract(vec)
inline operator fun Vector3D.times(multiplier: Double): Vector3D = this.clone().multiply(multiplier)

inline operator fun Vector3Df.unaryMinus(): Vector3Df = this.clone().invert()
inline operator fun Vector3Df.plus(vec: Vector3Df): Vector3Df = this.clone().add(vec)
inline operator fun Vector3Df.minus(vec: Vector3Df): Vector3Df = this.clone().subtract(vec)
inline operator fun Vector3Df.times(multiplier: Float): Vector3Df = this.clone().multiply(multiplier)

inline operator fun Vector3Di.unaryMinus(): Vector3Di = this.clone().invert()
inline operator fun Vector3Di.plus(vec: Vector3Di): Vector3Di = this.clone().add(vec)
inline operator fun Vector3Di.minus(vec: Vector3Di): Vector3Di = this.clone().subtract(vec)
inline operator fun Vector3Di.times(multiplier: Int): Vector3Di = this.clone().multiply(multiplier)

inline operator fun World.contains(entity: Entity): Boolean = this == entity.location.world

// distance = location1 - location2

inline operator fun Location.minus(loc: Location): Double = this.getDistanceSquared(loc)

// newLocation = location +|- vector

inline operator fun Location.plus(vec: Vector3D): Location = this.add(vec)
inline operator fun Location.plus(vec: Vector3Df): Location = this.add(vec)
inline operator fun Location.plus(vec: Vector3Di): Location = this.add(vec)
inline operator fun Location.minus(vec: Vector3D): Location = this.subtract(vec)
inline operator fun Location.minus(vec: Vector3Df): Location = this.subtract(vec)
inline operator fun Location.minus(vec: Vector3Di): Location = this.subtract(vec)

// container += item|items

inline operator fun Container.plusAssign(type: String) {
    this.addItem(ItemStackFactory.build(type)!!)
}

inline operator fun Container.plusAssign(item: ItemStack) {
    this.addItem(item)
}

inline operator fun Container.plusAssign(items: Array<ItemStack>) {
    this.addItem(*items)
}

inline operator fun Container.plusAssign(items: Collection<ItemStack>) {
    this.addItem(*items.toTypedArray())
}

inline operator fun Container.plusAssign(container: Container) {
    container.contents?.forEach {
        if (it != null) {
            this.addItem(it)
        }
    }
}

// container -= item|items

inline operator fun Container.minusAssign(type: String) {
    this.removeItem(ItemStackFactory.build(type)!!)
}

inline operator fun Container.minusAssign(item: ItemStack) {
    this.removeItem(item)
}

inline operator fun Container.minusAssign(items: Array<ItemStack>) {
    this.removeItem(*items)
}

inline operator fun Container.minusAssign(items: Collection<ItemStack>) {
    this.removeItem(*items.toTypedArray())
}

// container[id] = string|Item

inline operator fun Container.get(slot: Int): ItemStack? = this.getItem(slot)
inline operator fun Container.set(slot: Int, item: ItemStack?) = this.setItem(slot, item)
inline operator fun Container.set(slot: Int, type: String?) = this.setItem(slot, ItemStackFactory.build(type))
inline operator fun Container.contains(type: String) = this.contains(ItemType.of(type))

// visual +=|-= viewer|viewers; viewer in visual

inline operator fun Visual<*>.plusAssign(viewer: Player) {
    this.addViewer(viewer)
}
inline operator fun Visual<*>.plusAssign(viewers: Collection<Player>) {
    viewers.forEach { this.addViewer(it) }
}
inline operator fun Visual<*>.minusAssign(viewer: Player) {
    this.removeViewer(viewer)
}
inline operator fun Visual<*>.minusAssign(viewers: Collection<Player>) {
    viewers.forEach { this.removeViewer(it) }
}
inline operator fun Visual<*>.contains(viewer: Player) = this.visibleTo(viewer)

// linedVisual += componentLike; linedVisual[line]

inline operator fun LinedVisual<*>.plusAssign(line: ComponentLike) {
    this.newLine(this.lines().size, line)
}
inline operator fun LinedVisual<*>.get(line: Int): TextEntry? = this.lines()[line]
inline operator fun LinedVisual<*>.set(line: Int, entry: TextEntry) {
    this.replaceLine(line, entry)
}
inline operator fun LinedVisual<*>.set(line: Int, text: Component) {
    this.replaceLine(line, text)
}

// entity +=|-= passenger; passenger in entity

inline operator fun Entity.plusAssign(entity: Entity) {
    this.addPassenger(entity)
}
inline operator fun Entity.minusAssign(entity: Entity) {
    this.removePassenger(entity)
}
inline operator fun Entity.contains(entity: Entity) = this.passengers.contains(entity)

// entity tp location|anotherEntity

inline infix fun Entity.tp(loc: Location): CompletableFuture<Boolean> = this.teleport(loc)
inline infix fun Entity.tp(entity: Entity): CompletableFuture<Boolean> = this.teleport(entity.location)

// newItem = itemType * 5; newItem = newItem * 6; newItem++; newItem--
inline operator fun ItemType.times(amount: Int): ItemStack = ItemStackFactory.builder().type(this).amount(amount).build()!!
inline operator fun ItemStack.times(amount: Int): ItemStack = this.withAmount(amount)
inline operator fun ItemStack.inc(): ItemStack = this.withAmount(amount + 1)
inline operator fun ItemStack.dec(): ItemStack = this.withAmount(amount - 1)
