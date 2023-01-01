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

package org.screamingsandals.lib.bukkit.event;

import org.bukkit.Bukkit;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

@Service
public class BukkitEventManager extends EventManager {

    public BukkitEventManager(Controllable controllable) {
        super(controllable);
    }

    public static void init(Controllable controllable) {
        EventManager.init(() -> new BukkitEventManager(controllable));
    }

    @Override
    public boolean isServerThread() {
        return Bukkit.getServer().isPrimaryThread();
    }
}
