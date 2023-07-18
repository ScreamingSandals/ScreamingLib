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

package org.screamingsandals.lib.pathfinder;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;

@AbstractService("org.screamingsandals.lib.impl.{platform}.pathfinder.{Platform}PathfinderManager")
public abstract class PathfinderManager {
    private static @Nullable PathfinderManager manager;

    @ApiStatus.Internal
    public PathfinderManager() {
        Preconditions.checkArgument(manager == null, "PathfinderManager is already initialized.");
        manager = this;
    }
}
