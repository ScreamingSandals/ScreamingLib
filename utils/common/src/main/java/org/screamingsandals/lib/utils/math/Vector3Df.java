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
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.ProtoLocation;
import org.screamingsandals.lib.utils.ProtoVector3D;
import org.screamingsandals.lib.utils.ProtoVector3Df;
import com.iamceph.resulter.core.pack.ProtoWrapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector3Df implements Cloneable, ProtoWrapper<ProtoVector3Df> {
    private float x;
    private float y;
    private float z;

    public static Vector3Df unwrap(ProtoVector3Df wrapped) {
        return new Vector3Df(wrapped.getX(), wrapped.getY(), wrapped.getZ());
    }

    public static Vector3Df unwrap(ProtoVector3D wrapped) {
        return new Vector3Df((float) wrapped.getX(), (float) wrapped.getY(), (float) wrapped.getZ());
    }

    public static Vector3Df unwrap(ProtoLocation wrapped) {
        return new Vector3Df((float) wrapped.getX(), (float) wrapped.getY(), (float) wrapped.getZ());
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector3Df clone() {
        return new Vector3Df(x, y, z);
    }

    public Vector3Df add(float x, float y, float z) {
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

    public float distance(@NotNull Vector3Df o) {
        return (float) Math.sqrt(distanceSquared(o));
    }

    public float distanceSquared(@NotNull Vector3Df o) {
        var deltaX = x - o.x;
        var deltaY = y - o.y;
        var deltaZ = z - o.z;

        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
    }

    @NotNull
    public Vector3Df normalize() {
        var length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    @Override
    public ProtoVector3Df asProto() {
        return ProtoVector3Df.newBuilder()
                .setX(this.x)
                .setY(this.y)
                .setZ(this.z)
                .build();
    }
}
