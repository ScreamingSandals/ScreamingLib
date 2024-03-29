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

package org.screamingsandals.lib.event.player;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.entity.EntityDeathEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;

public interface PlayerDeathEvent extends EntityDeathEvent, PlayerEvent {

    @Nullable Component deathMessage();

    void deathMessage(@Nullable Component deathMessage);

    void deathMessage(@Nullable ComponentLike deathMessage);

    boolean keepInventory();

    void keepInventory(boolean keepInventory);

    boolean shouldDropExperience();

    void shouldDropExperience(boolean shouldDropExperience);

    boolean keepLevel();

    void keepLevel(boolean keepLevel);

    int newLevel();

    void newLevel(int newLevel);

    int newTotalExp();

    void newTotalExp(int newTotalExp);

    int getNewExp();

    void newExp(int newExp);

    int droppedExp();

    void droppedExp(int droppedExp);

    @Nullable Player killer();
}
