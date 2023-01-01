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

package org.screamingsandals.lib.utils.math;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vector2D implements Cloneable {
    private double x;
    private double y;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

    public Vector2D invert() {
        this.x = -x;
        this.y = -y;
        return this;
    }

    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2D add(Vector2D vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2D subtract(Vector2D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public Vector2D multiply(double multiply) {
        this.x *= multiply;
        this.y *= multiply;
        return this;
    }
}