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

package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@LimitedVersionSupport("Bukkit >= 1.13.2")
public interface SBlockDropItemEvent extends SCancellableEvent, PlatformEventWrapper {

    PlayerWrapper getPlayer();

    BlockStateHolder getBlockState();

    Collection<EntityItem> getItems();
}
