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

package org.screamingsandals.lib.world.gamerule;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.registry.SimpleRegistry;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
@ApiStatus.Internal
public abstract class GameRuleRegistry extends SimpleRegistry<GameRuleType> {
    private static @Nullable GameRuleRegistry registry;

    public GameRuleRegistry() {
        super(GameRuleType.class);
        Preconditions.checkArgument(registry == null, "GameRuleRegistry is already initialized!");
        registry = this;
    }

    public static @NotNull GameRuleRegistry getInstance() {
        return Preconditions.checkNotNull(registry, "GameRuleRegistry is not initialized yet!");
    }
}
