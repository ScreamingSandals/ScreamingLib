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

package org.screamingsandals.lib.visuals.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import org.screamingsandals.lib.event.SCancellableAsyncEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.Visual;

@Accessors(fluent = true, chain = false)
@EqualsAndHashCode(callSuper = false)
@Data
public class VisualsTouchEvent<T extends Visual<T>> implements SCancellableAsyncEvent {
    private final PlayerWrapper player;
    private final T visual;
    private final InteractType interactType;
    private boolean cancelled;
}

