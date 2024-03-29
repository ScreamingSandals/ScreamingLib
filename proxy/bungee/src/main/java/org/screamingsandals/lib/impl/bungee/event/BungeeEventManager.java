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

package org.screamingsandals.lib.impl.bungee.event;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.ServiceInitializer;

@Service
public class BungeeEventManager extends EventManager {

    public BungeeEventManager(@NotNull Controllable controllable) {
        super(controllable);
    }

    @ServiceInitializer
    public static void init(@NotNull Controllable controllable) {
        EventManager.init(() -> new BungeeEventManager(controllable));
    }

    @Override
    public boolean isServerThread() {
        return false;
    }
}
