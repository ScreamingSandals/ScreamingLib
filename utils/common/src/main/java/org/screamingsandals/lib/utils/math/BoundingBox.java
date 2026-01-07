/*
 * Copyright 2026 ScreamingSandals
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
public class BoundingBox implements Cloneable {
    public static final @NotNull BoundingBox ZERO = new BoundingBox(0, 0, 0, 0, 0, 0);

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double minZ;
    private double maxZ;

    @Override
    public @NotNull BoundingBox clone() {
        return new BoundingBox(this.minX, this.maxX, this.minY, this.maxY, this.minZ, this.maxZ);
    }

    public static @NotNull BoundingBox of(@NotNull Vector3Df corner1, @NotNull Vector3Df corner2) {
        return of(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    public static @NotNull BoundingBox of(@NotNull Vector3D corner1, @NotNull Vector3D corner2) {
        return of(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    public static @NotNull BoundingBox of(double x, double y, double z, double x2, double y2, double z2) {
        return new BoundingBox(
                Math.min(x, x2),
                Math.max(x, x2),
                Math.min(y, y2),
                Math.max(y, y2),
                Math.min(z, z2),
                Math.max(z, z2)
        );
    }

    public double getWidthX() {
        return this.maxX - this.minX;
    }

    public double getWidthZ() {
        return this.maxZ - this.minZ;
    }

    public double getHeight() {
        return this.maxY - this.minY;
    }

    public double getVolume() {
        return this.getHeight() * this.getWidthX() * this.getWidthZ();
    }

    public double getCenterX() {
        return this.minX + this.getWidthX() * 0.5;
    }

    public double getCenterY() {
        return this.minY + this.getHeight() * 0.5;
    }

    public double getCenterZ() {
        return this.minZ + this.getWidthZ() * 0.5;
    }

    public @NotNull Vector3D getCenter() {
        return new Vector3D(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public @NotNull Vector3D getMin() {
        return new Vector3D(this.minX, this.minY, this.minZ);
    }

    public @NotNull Vector3D getMax() {
        return new Vector3D(this.maxX, this.maxY, this.maxZ);
    }

    private boolean overlaps(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX < maxX && this.maxX > minX
                && this.minY < maxY && this.maxY > minY
                && this.minZ < maxZ && this.maxZ > minZ;
    }

    public boolean overlaps(@NotNull BoundingBox other) {
        return this.overlaps(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }


    public boolean overlaps(@NotNull Vector3Df min, @NotNull Vector3Df max) {
        return this.overlaps(min.toVector3D(), max.toVector3D());
    }

    public boolean overlaps(@NotNull Vector3D min, @NotNull Vector3D max) {
        double x1 = min.getX();
        double y1 = min.getY();
        double z1 = min.getZ();
        double x2 = max.getX();
        double y2 = max.getY();
        double z2 = max.getZ();
        return this.overlaps(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.min(z1, z2),
                Math.max(x1, x2),
                Math.max(y1, y2),
                Math.max(z1, z2)
        );
    }

    public boolean contains(double x, double y, double z) {
        return x >= this.minX && x < this.maxX
                && y >= this.minY && y < this.maxY
                && z >= this.minZ && z < this.maxZ;
    }

    public boolean contains(@NotNull Vector3D position) {
        return this.contains(position.getX(), position.getY(), position.getZ());
    }

    public boolean contains(@NotNull Vector3Df position) {
        return this.contains(position.getX(), position.getY(), position.getZ());
    }

    private boolean contains(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX <= minX && this.maxX >= maxX
                && this.minY <= minY && this.maxY >= maxY
                && this.minZ <= minZ && this.maxZ >= maxZ;
    }

    public boolean contains(@NotNull BoundingBox other) {
        return this.contains(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean contains(@NotNull Vector3Df min, @NotNull Vector3Df max) {
        return this.contains(min.toVector3D(), max.toVector3D());
    }

    public boolean contains(@NotNull Vector3D min, @NotNull Vector3D max) {
        double x1 = min.getX();
        double y1 = min.getY();
        double z1 = min.getZ();
        double x2 = max.getX();
        double y2 = max.getY();
        double z2 = max.getZ();
        return this.contains(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.min(z1, z2),
                Math.max(x1, x2),
                Math.max(y1, y2),
                Math.max(z1, z2)
        );
    }
}
