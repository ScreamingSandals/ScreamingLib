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

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.utils.math.Vector3Di;
import org.screamingsandals.lib.world.Location;

public class FakeLivingEntity extends FakeEntity {
    public FakeLivingEntity(@NotNull Location location, int typeId) {
        super(location, typeId);
    }

    public void setHandStates(byte handStates) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HAND_STATES), handStates));
    }

    public void setHealth(float health) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HEALTH), health));
    }

    /**
     * Sets the potion effect color, 0 if no effect.
     * @param color the color to set
     */
    public void setPotionEffectColor(int color) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POTION_EFFECT_COLOR), color));
    }

    public void setPotionAmbiency(boolean isAmbient) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.POTION_AMBIENCY), isAmbient));
    }

    public void setBodyArrowCount(int arrowCount) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.BODY_ARROW_COUNT), arrowCount));
    }

    public void setBeeStingerCount(int beeStingerCount) {
        if (Server.isVersion(1,15)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.BEE_STINGER_COUNT), beeStingerCount));
        }
    }

    public void setBedPosition(@NotNull Vector3Di bedPosition) {
        if (Server.isVersion(1, 14)) {
            put(MetadataItem.ofOpt(EntityMetadata.Registry.getId(EntityMetadata.BED_BLOCK_POSITION), bedPosition));
        }
    }
}
