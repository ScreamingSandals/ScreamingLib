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

package org.screamingsandals.lib.npc.event;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.npc.NPC;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.event.VisualsTouchEvent;

/**
 * An event signifying that a NPC has been interacted with.
 */
public class NPCInteractEvent extends VisualsTouchEvent<NPC> {
    public NPCInteractEvent(@NotNull Player player, @NotNull NPC visual, @NotNull InteractType interactType) {
        super(player, visual, interactType);
    }
}
