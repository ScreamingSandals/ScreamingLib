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

package org.screamingsandals.lib.bungee.spectator.backports;

import com.google.gson.JsonObject;
import lombok.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@ApiStatus.Internal
public class BlockNBTPortedComponent extends NBTPortedComponent {
    private String blockPosition;

    public BlockNBTPortedComponent(@NotNull BlockNBTPortedComponent component) {
        super(component);
        this.blockPosition = component.blockPosition;
    }

    @Override
    public @NotNull BlockNBTPortedComponent duplicate() {
        return new BlockNBTPortedComponent(this);
    }

    @Override
    public void write(@NotNull JsonObject out) {
        super.write(out);
        out.addProperty("block", this.blockPosition);
    }
}
