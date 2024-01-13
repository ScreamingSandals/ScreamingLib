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

package org.screamingsandals.lib.impl.entity.type;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.impl.tags.TagPortHelper;

import java.util.List;
import java.util.function.Predicate;

@ApiStatus.Internal
@UtilityClass
public class EntityTypeTagBackPorts {
    public static @Nullable List<@NotNull String> getPortedTags(@NotNull EntityType entityType, @NotNull Predicate<@NotNull String> nativeTagChecker, boolean platformSupportsTags) {
        var helper = new TagPortHelper(nativeTagChecker);

        /*
        Only entity tag additions and renames are backported!!! (And removals are ported to newer versions if it makes sense)

        Modifying existing tag may lead into unexpected behaviour!
        (the tag is updated only in slib, so the plugin would have different tag then the game logic)

        Also tags that are connected only with the new features and their usage doesn't make any sense in older versions are not backported.

        Use Helper to check tags here! EntityTypeHolder#hasTag doesn't have any ported tags yet!
         */

        if (!Server.isVersion(1, 14) || !platformSupportsTags) {
            if (entityType.is("skeleton", "stray", "wither_skeleton")) {
                helper.port("skeletons");
            }
            if (entityType.is(
                    "evoker",
                    "illusioner",
                    "vindicator",
                    "witch"
            )) {
                helper.port("raiders");
            }
        }

        if (!Server.isVersion(1, 15) || !platformSupportsTags) {
            if (entityType.is("arrow", "spectral_arrow")) {
                helper.port("arrows");
            }
        }

        if (!Server.isVersion(1, 16) || !platformSupportsTags) {
            if (helper.hasTag("arrows") || entityType.is(
                    "snowball",
                    "fireball",
                    "small_fireball",
                    "egg",
                    "trident",
                    "dragon_fireball",
                    "wither_skull"
            )) {
                helper.port("impact_projectiles");
            }
        }

        return helper.getPorts();
    }
}
