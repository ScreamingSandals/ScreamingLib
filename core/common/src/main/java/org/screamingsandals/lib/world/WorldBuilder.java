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

package org.screamingsandals.lib.world;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.world.dimension.DimensionHolder;

@Setter
@Accessors(fluent = true, chain = true)
@AbstractService
public abstract class WorldBuilder {
    protected static Class<? extends WorldBuilder> IMPL = null;

    /**
     * The name of the resulted world. Note that some platforms do not support custom names.
     */
    @LimitedVersionSupport("Minestom")
    protected String name;
    /**
     * The dimension of the resulted world.
     */
    protected DimensionHolder dimension;

    /**
     * Creates a {@link WorldHolder} from this builder.
     *
     * @return the world
     */
    public WorldHolder build() {
        Preconditions.checkNotNull(name, "Name can't be null!");
        return build0();
    }

    // abstract methods for implementations

    public abstract WorldHolder build0();
}
