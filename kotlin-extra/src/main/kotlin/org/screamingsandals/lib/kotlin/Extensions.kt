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
import org.screamingsandals.lib.entity.EntityBasic
import org.screamingsandals.lib.event.Cancellable
import org.screamingsandals.lib.event.EventManager
import org.screamingsandals.lib.event.SEvent
import org.screamingsandals.lib.item.Item
import org.screamingsandals.lib.player.PlayerWrapper
import org.screamingsandals.lib.spectator.Component
import org.screamingsandals.lib.utils.ComparableWrapper
import org.screamingsandals.lib.api.Wrapper
import org.screamingsandals.lib.item.ItemTypeHolder
import org.screamingsandals.lib.item.builder.ItemFactory
import org.screamingsandals.lib.spectator.ComponentLike
import org.screamingsandals.lib.utils.math.Vector2D
import org.screamingsandals.lib.utils.math.Vector3D
import org.screamingsandals.lib.utils.math.Vector3Df
import org.screamingsandals.lib.utils.math.Vector3Di
import org.screamingsandals.lib.utils.visual.TextEntry
import org.screamingsandals.lib.visuals.LinedVisual
import org.screamingsandals.lib.visuals.Visual
import org.screamingsandals.lib.world.LocationHolder
import org.screamingsandals.lib.world.WorldHolder
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

inline operator fun WorldHolder.contains(entity: EntityBasic): Boolean = this == entity.location.world

// distance = location1 - location2

inline operator fun LocationHolder.minus(loc: LocationHolder): Double = this.getDistanceSquared(loc)

// newLocation = location +|- vector

inline operator fun LocationHolder.plus(vec: Vector3D): LocationHolder = this.add(vec)
inline operator fun LocationHolder.plus(vec: Vector3Df): LocationHolder = this.add(vec)
inline operator fun LocationHolder.plus(vec: Vector3Di): LocationHolder = this.add(vec)
inline operator fun LocationHolder.minus(vec: Vector3D): LocationHolder = this.subtract(vec)
inline operator fun LocationHolder.minus(vec: Vector3Df): LocationHolder = this.subtract(vec)
inline operator fun LocationHolder.minus(vec: Vector3Di): LocationHolder = this.subtract(vec)

// container += item|items

inline operator fun Container.plusAssign(type: String) {
    this.addItem(ItemFactory.build(type)!!)
}

inline operator fun Container.plusAssign(item: Item) {
    this.addItem(item)
}

inline operator fun Container.plusAssign(items: Array<Item>) {
    this.addItem(*items)
}

inline operator fun Container.plusAssign(items: Collection<Item>) {
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
    this.removeItem(ItemFactory.build(type)!!)
}

inline operator fun Container.minusAssign(item: Item) {
    this.removeItem(item)
}

inline operator fun Container.minusAssign(items: Array<Item>) {
    this.removeItem(*items)
}

inline operator fun Container.minusAssign(items: Collection<Item>) {
    this.removeItem(*items.toTypedArray())
}

// container[id] = string|Item

inline operator fun Container.get(slot: Int): Item? = this.getItem(slot)
inline operator fun Container.set(slot: Int, item: Item?) = this.setItem(slot, item)
inline operator fun Container.set(slot: Int, type: String?) = this.setItem(slot, ItemFactory.build(type))
inline operator fun Container.contains(type: String) = this.contains(ItemTypeHolder.of(type))

// visual +=|-= viewer|viewers; viewer in visual

inline operator fun Visual<*>.plusAssign(viewer: PlayerWrapper) {
    this.addViewer(viewer)
}
inline operator fun Visual<*>.plusAssign(viewers: Collection<PlayerWrapper>) {
    viewers.forEach { this.addViewer(it) }
}
inline operator fun Visual<*>.minusAssign(viewer: PlayerWrapper) {
    this.removeViewer(viewer)
}
inline operator fun Visual<*>.minusAssign(viewers: Collection<PlayerWrapper>) {
    viewers.forEach { this.removeViewer(it) }
}
inline operator fun Visual<*>.contains(viewer: PlayerWrapper) = this.visibleTo(viewer)

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

inline operator fun EntityBasic.plusAssign(entity: EntityBasic) {
    this.addPassenger(entity)
}
inline operator fun EntityBasic.minusAssign(entity: EntityBasic) {
    this.removePassenger(entity)
}
inline operator fun EntityBasic.contains(entity: EntityBasic) = this.passengers.contains(entity)

// entity tp location|anotherEntity

inline infix fun EntityBasic.tp(loc: LocationHolder): CompletableFuture<Boolean> = this.teleport(loc)
inline infix fun EntityBasic.tp(entity: EntityBasic): CompletableFuture<Boolean> = this.teleport(entity.location)

// newItem = itemType * 5; newItem = newItem * 6; newItem++; newItem--
inline operator fun ItemTypeHolder.times(amount: Int): Item = ItemFactory.builder().type(this).amount(amount).build()!!
inline operator fun Item.times(amount: Int): Item = this.withAmount(amount)
inline operator fun Item.inc(): Item = this.withAmount(amount + 1)
inline operator fun Item.dec(): Item = this.withAmount(amount - 1)
