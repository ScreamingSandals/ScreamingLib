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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.event.player.SPlayerCraftItemEvent;

public interface SVillagerReplenishTradeEvent extends SCancellableEvent, PlatformEventWrapper {

    EntityBasic entity();

    SPlayerCraftItemEvent.Recipe recipe();

    @ApiStatus.Internal
    @Deprecated // because there's no proper Recipe API yet
    void recipe(SPlayerCraftItemEvent.Recipe recipe);

    int bonus();

    void bonus(int bonus);
}
