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

package org.screamingsandals.lib.impl.bukkit.entity.animal;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.DyeColor;
import org.screamingsandals.lib.entity.animal.Sheep;
import org.screamingsandals.lib.spectator.Color;

public class BukkitSheep extends BukkitAnimal implements Sheep {
    public BukkitSheep(@NotNull org.bukkit.entity.Sheep wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull DyeColor woolColor() {
        var color = ((org.bukkit.entity.Sheep) wrappedObject).getColor();
        return DyeColor.of(color == null ? Color.WHITE : color);
    }

    @Override
    public void woolColor(@NotNull DyeColor color) {
        ((org.bukkit.entity.Sheep) wrappedObject).setColor(color.as(org.bukkit.DyeColor.class));
    }
}
