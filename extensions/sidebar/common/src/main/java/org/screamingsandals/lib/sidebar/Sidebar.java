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

package org.screamingsandals.lib.sidebar;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.visuals.DatableVisual;
import org.screamingsandals.lib.visuals.LinedVisual;
public interface Sidebar extends LinedVisual<Sidebar>, DatableVisual<Sidebar>, TeamedSidebar<Sidebar> {

    /**
     * Creates new Sidebar.
     *
     * @return created Sidebar
     */
    @Contract(value = "-> new", pure = true)
    static @NotNull Sidebar of() {
        return SidebarManager.sidebar();
    }
}