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

package org.screamingsandals.lib.event.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.entity.BasicEntity;
import org.screamingsandals.lib.event.PlatformEvent;
import org.screamingsandals.lib.event.SEvent;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.Location;

public interface EntityPortalExitEvent extends SEvent, PlatformEvent {

    @NotNull BasicEntity entity();

    @NotNull Location from();

    void from(@NotNull Location location);

    @Nullable Location to();

    void to(@Nullable Location location);

    @NotNull Vector3D before();

    @NotNull Vector3D after();

    void after(@NotNull Vector3D after);
}
