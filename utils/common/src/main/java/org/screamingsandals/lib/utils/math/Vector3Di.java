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

package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Vector3Di implements Cloneable {
    private int x;
    private int y;
    private int z;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3Di clone() {
        return new Vector3Di(x, y, z);
    }

    public Vector3Di add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public float distance(@NotNull Vector3Di o) {
        return (float) Math.sqrt(distanceSquared(o));
    }

    public int distanceSquared(@NotNull Vector3Di o) {
        var deltaX = x - o.x;
        var deltaY = y - o.y;
        var deltaZ = z - o.z;

        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    @NotNull
    public Vector3Di normalize() {
        var length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }
}
