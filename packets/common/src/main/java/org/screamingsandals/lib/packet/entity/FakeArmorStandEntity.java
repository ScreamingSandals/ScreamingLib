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

package org.screamingsandals.lib.packet.entity;

import lombok.Getter;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;

@Getter
public class FakeArmorStandEntity extends FakeLivingEntity {
    private byte armorStandFlags;
    private Vector3Df headRotation;

    public FakeArmorStandEntity(LocationHolder location, int typeId) {
        super(location, typeId);
        this.armorStandFlags = 0;
        this.headRotation = new Vector3Df(0.0f, 0.0f, 0.0f);
        setArmorStandFlags();
    }

    @Override
    public void setGravity(boolean gravity) {
        if (Server.isVersion(1, 10)) {
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HAS_NO_GRAVITY), !gravity));
        } else {
            setArmorStandFlagsFromValue( 2, gravity);
            put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
        }
    }

    public void setArmorStandFlags() {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
    }

    public void setHeadRotation(Vector3Df value) {
        this.headRotation = value;
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.HEAD_ROTATION), value));
    }

    public void setBodyRotation(Vector3Df value) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.BODY_ROTATION), value));
    }

    public void setLeftArmRotation(Vector3Df value) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.LEFT_ARM_ROTATION), value));
    }

    public void setRightArmRotation(Vector3Df value) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.RIGHT_ARM_ROTATION), value));
    }

    public void setLeftLegRotation(Vector3Df value) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.LEFT_LEG_ROTATION), value));
    }

    public void setRightLegRotation(Vector3Df value) {
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.RIGHT_LEG_ROTATION), value));
    }

    public void setMarker(boolean marker) {
        setArmorStandFlagsFromValue( 16, marker);
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
    }

    public void setArms(boolean hasArms) {
        setArmorStandFlagsFromValue(4, hasArms);
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
    }

    public void setBasePlate(boolean hasBasePlate) {
        setArmorStandFlagsFromValue(8, hasBasePlate);
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
    }

    public void setSmall(boolean isSmall) {
        setArmorStandFlagsFromValue(1, isSmall);
        put(MetadataItem.of(EntityMetadata.Registry.getId(EntityMetadata.ARMOR_STAND_FLAGS), armorStandFlags));
    }

    private void setArmorStandFlagsFromValue(int i, boolean flag) {
        if (flag) {
            armorStandFlags = (byte) (armorStandFlags | i);
        } else {
            armorStandFlags = (byte) (armorStandFlags & (~i));
        }
    }
}
