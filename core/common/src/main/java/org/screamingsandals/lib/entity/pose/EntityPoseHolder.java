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

package org.screamingsandals.lib.entity.pose;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.RawValueHolder;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.List;

@SuppressWarnings("AlternativeMethodAvailable")
@LimitedVersionSupport("Bukkit >= 1.17")
public interface EntityPoseHolder extends ComparableWrapper, RawValueHolder {
    @NotNull String platformName();

    /**
     * {@inheritDoc}
     */
    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    boolean is(@Nullable Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    boolean is(@Nullable Object @NotNull... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    static @NotNull EntityPoseHolder of(@NotNull Object entityPose) {
        var result = ofNullable(entityPose);
        Preconditions.checkNotNullIllegal(result, "Could not find entity pose: " + entityPose);
        return result;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.ENTITY_POSE)
    @Contract("null -> null")
    static @Nullable EntityPoseHolder ofNullable(@Nullable Object entityPose) {
        if (entityPose instanceof EntityPoseHolder) {
            return (EntityPoseHolder) entityPose;
        }
        return EntityPoseMapping.resolve(entityPose);
    }

    static @Unmodifiable @NotNull List<@NotNull EntityPoseHolder> all() {
        return EntityPoseMapping.getValues();
    }
}
