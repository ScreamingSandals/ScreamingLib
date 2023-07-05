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

package org.screamingsandals.lib.entity.monster.zombie;

import org.screamingsandals.lib.entity.Ageable;
import org.screamingsandals.lib.entity.monster.Monster;

/**
 * Represents an entity with identifier {@code minecraft:zombie}.
 * It is also a parent type for {@code minecraft:zombie_villager}, {@code minecraft:husk}, {@code minecraft:zombified_piglin}
 * and {@code minecraft:drowned}.
 */
public interface Zombie extends Monster, Ageable {
}
