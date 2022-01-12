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

package org.screamingsandals.lib.minestom.world;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.BlockHandler;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

@Service
public class MinestomLocationMapper extends LocationMapper {
    public MinestomLocationMapper() {
        converter.registerW2P(Pos.class, holder -> new Pos(holder.getX(), holder.getY(), holder.getZ(), holder.getYaw(), holder.getPitch()))
                .registerP2W(Pos.class, pos -> wrapPos(pos, null))
                .registerP2W(Entity.class, entity -> wrapPos(entity.getPosition(), WorldMapper.wrapWorld(entity.getInstance())))
                .registerP2W(Point.class, point -> wrapPoint(point, null))
                .registerP2W(BlockHandler.Placement.class, placement -> wrapPoint(placement.getBlockPosition(), WorldMapper.wrapWorld(placement.getInstance())))
                .registerP2W(BlockHandler.Destroy.class, destroy -> wrapPoint(destroy.getBlockPosition(), WorldMapper.wrapWorld(destroy.getInstance())))
                .registerP2W(BlockHandler.Interaction.class, intr -> wrapPoint(intr.getBlockPosition(), WorldMapper.wrapWorld(intr.getInstance())))
                .registerP2W(BlockHandler.Touch.class, touch -> wrapPoint(touch.getBlockPosition(), WorldMapper.wrapWorld(touch.getInstance())))
                .registerP2W(BlockHandler.Tick.class, tick -> wrapPoint(tick.getBlockPosition(), WorldMapper.wrapWorld(tick.getInstance())));
    }

    public static LocationHolder wrapPos(Pos pos, @Nullable WorldHolder world) {
        return new LocationHolder(pos.x(), pos.y(), pos.z(), pos.yaw(), pos.pitch(), world);
    }

    public static LocationHolder wrapPoint(Point point, @Nullable WorldHolder world) {
        return new LocationHolder(point.x(), point.y(), point.z(), 0, 0, world);
    }
}
