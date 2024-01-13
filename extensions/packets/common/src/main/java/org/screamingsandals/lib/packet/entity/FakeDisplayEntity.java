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

package org.screamingsandals.lib.packet.entity;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.world.Location;

@Getter
public class FakeDisplayEntity extends FakeEntity {
    public FakeDisplayEntity(@NotNull Location location, int typeId) {
        super(location, typeId);

        put(MetadataItem.of((byte) (Server.isVersion(1, 20, 2) ? 15 : 14), (byte) 3)); // put to center
    }
}
