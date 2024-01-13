/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.fakedeath;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Core;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

@AbstractService("org.screamingsandals.lib.impl.{platform}.fakedeath.{Platform}FakeDeath")
@ServiceDependencies(dependsOn = Core.class)
public abstract class FakeDeath {
    private static @Nullable FakeDeath fakeDeath;

    @ApiStatus.Internal
    public FakeDeath() {
        Preconditions.checkArgument(fakeDeath == null, "FakeDeath is already initialized.");
        fakeDeath = this;
    }

    public static void die(@Nullable Player player, @NotNull FakeDeath.PlayerInventoryLifeResetFunction function) {
        if (player == null) {
            return;
        }
        Preconditions.checkArgument(fakeDeath != null, "FakeDeath is not initialized yet.");
        fakeDeath.die0(player, function);
    }

    protected abstract void die0(@NotNull Player player, @NotNull FakeDeath.PlayerInventoryLifeResetFunction function);

    @FunctionalInterface
    public interface PlayerInventoryLifeResetFunction {
        void reset(@NotNull Player player, boolean keepInventory);
    }
}
