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

package org.screamingsandals.lib.entity.animal;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

/**
 * Represents an entity with identifier {@code minecraft:cat}.
 * <p>
 * If running on version before 1.14, this class represents an entity with identifier {@code minecraft:ocelot}. (Cats and ocelots were the same entity)
 */
@LimitedVersionSupport(">= 1.14; <= 1.13.2: Ocelots implement this interface")
public interface Cat extends Tamable {
}
