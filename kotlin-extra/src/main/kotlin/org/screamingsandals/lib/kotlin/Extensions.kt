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
import org.screamingsandals.lib.item.Item
import org.screamingsandals.lib.item.ItemTypeHolder
import org.screamingsandals.lib.item.builder.ItemFactory
import org.screamingsandals.lib.player.PlayerWrapper
import org.screamingsandals.lib.utils.ComparableWrapper
import org.screamingsandals.lib.utils.Wrapper
import org.screamingsandals.lib.utils.math.Vector2D
import org.screamingsandals.lib.utils.math.Vector3D
import org.screamingsandals.lib.utils.math.Vector3Df
import org.screamingsandals.lib.utils.math.Vector3Di
import org.screamingsandals.lib.utils.visual.TextEntry
import org.screamingsandals.lib.visuals.LinedVisual
import org.screamingsandals.lib.visuals.Visual
import org.screamingsandals.lib.world.LocationHolder
import org.screamingsandals.lib.world.WorldHolder
import kotlin.reflect.KClass

infix fun <T : Any> Wrapper.unwrap(type: KClass<T>): T = `as`(type.java)
@ApiStatus.Experimental
infix fun ComparableWrapper.compare(type: Any): Boolean = `is`(type)
@ApiStatus.Experimental
fun ComparableWrapper.compare(vararg type: Any): Boolean = `is`(type)
infix fun EntityBasic.tp(loc: LocationHolder) {
    teleport(loc)
}

operator fun Vector2D.unaryMinus(): Vector2D = clone().invert()
operator fun Vector2D.plus(vec: Vector2D): Vector2D = clone().add(vec)
operator fun Vector2D.minus(vec: Vector2D): Vector2D = clone().subtract(vec)
operator fun Vector2D.times(multiplier: Double): Vector2D = clone().multiply(multiplier)

operator fun Vector3D.unaryMinus(): Vector3D = clone().invert()
operator fun Vector3D.plus(vec: Vector3D): Vector3D = clone().add(vec)
operator fun Vector3D.minus(vec: Vector3D): Vector3D = clone().subtract(vec)
operator fun Vector3D.times(multiplier: Double): Vector3D = clone().multiply(multiplier)

operator fun Vector3Df.unaryMinus(): Vector3Df = clone().invert()
operator fun Vector3Df.plus(vec: Vector3Df): Vector3Df = clone().add(vec)
operator fun Vector3Df.minus(vec: Vector3Df): Vector3Df = clone().subtract(vec)
operator fun Vector3Df.times(multiplier: Float): Vector3Df = clone().multiply(multiplier)

operator fun Vector3Di.unaryMinus(): Vector3Di = clone().invert()
operator fun Vector3Di.plus(vec: Vector3Di): Vector3Di = clone().add(vec)
operator fun Vector3Di.minus(vec: Vector3Di): Vector3Di = clone().subtract(vec)
operator fun Vector3Di.times(multiplier: Int): Vector3Di = clone().multiply(multiplier)

operator fun WorldHolder.contains(entity: EntityBasic): Boolean = this == entity.location.world

operator fun LocationHolder.plus(loc: LocationHolder): LocationHolder = add(loc)
operator fun LocationHolder.plus(vec: Vector3D): LocationHolder = add(vec)
operator fun LocationHolder.plus(vec: Vector3Df): LocationHolder = add(vec)
operator fun LocationHolder.plus(vec: Vector3Di): LocationHolder = add(vec)
operator fun LocationHolder.minus(loc: LocationHolder): LocationHolder = subtract(loc)
operator fun LocationHolder.minus(vec: Vector3D): LocationHolder = subtract(vec)
operator fun LocationHolder.minus(vec: Vector3Df): LocationHolder = subtract(vec)
operator fun LocationHolder.minus(vec: Vector3Di): LocationHolder = subtract(vec)

operator fun Container.plusAssign(type: String) {
    addItem(ItemFactory.builder().type(ItemTypeHolder.of(type)).build().orElseThrow())
}
operator fun Container.plusAssign(item: Item) {
    addItem(item)
}
operator fun Container.plusAssign(items: Array<Item>) {
    addItem(*items)
}
operator fun Container.plusAssign(items: Collection<Item>) {
    addItem(*items.toTypedArray())
}
operator fun Container.plusAssign(container: Container) {
    addItem(*container.contents)
}
operator fun Container.minusAssign(type: String) {
    removeItem(ItemFactory.builder().type(ItemTypeHolder.of(type)).build().orElseThrow())
}
operator fun Container.minusAssign(item: Item) {
    removeItem(item)
}
operator fun Container.minusAssign(items: Array<Item>) {
    removeItem(*items)
}
operator fun Container.minusAssign(items: Collection<Item>) {
    removeItem(*items.toTypedArray())
}
operator fun Container.get(slot: Int): Item? = getItem(slot).orElse(null)
operator fun Container.set(slot: Int, item: Item) = setItem(slot, item)
operator fun Container.set(slot: Int, type: String) = setItem(slot, ItemFactory.builder().type(type).build().orElseThrow())
operator fun Container.contains(type: String) = contains(ItemTypeHolder.of(type))

operator fun <T : Any> Visual<T>.plusAssign(viewer: PlayerWrapper) {
    addViewer(viewer)
}
operator fun <T : Any> Visual<T>.plusAssign(viewers: Collection<PlayerWrapper>) {
    viewers.forEach { addViewer(it) }
}
operator fun <T : Any> Visual<T>.minusAssign(viewer: PlayerWrapper) {
    removeViewer(viewer)
}
operator fun <T : Any> Visual<T>.minusAssign(viewers: Collection<PlayerWrapper>) {
    viewers.forEach { removeViewer(it) }
}
operator fun <T : Any> Visual<T>.contains(viewer: PlayerWrapper) = visibleTo(viewer)

operator fun <T : Visual<T>> LinedVisual<T>.plusAssign(line: Component) {
    newLine(lines().size, line)
}
operator fun <T : Visual<T>> LinedVisual<T>.get(line: Int): TextEntry? = lines()[line]
operator fun <T : Visual<T>> LinedVisual<T>.set(line: Int, entry: TextEntry) {
    newLine(line, entry)
}
operator fun <T : Visual<T>> LinedVisual<T>.set(line: Int, text: Component) {
    newLine(line, text)
}

operator fun EntityBasic.plusAssign(entity: EntityBasic) {
    addPassenger(entity)
}
operator fun EntityBasic.minusAssign(entity: EntityBasic) {
    removePassenger(entity)
}
operator fun EntityBasic.contains(entity: EntityBasic) = passengers.contains(entity)

operator fun ItemTypeHolder.times(amount: Int): Item = ItemFactory.builder().type(this).amount(amount).build().orElseThrow()
operator fun Array<String>.contains(holder: ItemTypeHolder): Boolean = holder.`is`(*this)
operator fun Collection<String>.contains(holder: ItemTypeHolder): Boolean = contains(holder.platformName().lowercase())
operator fun String.contains(holder: ItemTypeHolder): Boolean = equals(holder.platformName(), ignoreCase = true)

operator fun Item.times(amount: Int): Item = withAmount(amount)
operator fun Item.inc(): Item = withAmount(amount + 1)
operator fun Item.dec(): Item = withAmount(amount - 1)