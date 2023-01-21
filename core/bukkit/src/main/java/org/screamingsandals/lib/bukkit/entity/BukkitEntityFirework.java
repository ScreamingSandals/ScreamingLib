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

package org.screamingsandals.lib.bukkit.entity;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityFirework;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitEntityFirework extends BukkitEntityProjectile implements EntityFirework {
    public BukkitEntityFirework(@NotNull Firework wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public void setEffect(@NotNull List<@NotNull FireworkEffectHolder> fireworkEffect, int power) {
        var meta = ((Firework) wrappedObject).getFireworkMeta();
        meta.setPower(power);
        meta.addEffects(fireworkEffect.stream().map(p -> p.as(FireworkEffect.class)).collect(Collectors.toList()));
        ((Firework) wrappedObject).setFireworkMeta(meta);
    }

    @Override
    public @NotNull Pair<@NotNull List<@NotNull FireworkEffectHolder>, @NotNull Integer> getEffect() {
        var meta = ((Firework) wrappedObject).getFireworkMeta();
        return Pair.of(meta.getEffects().stream().map(FireworkEffectHolder::of).collect(Collectors.toList()), meta.getPower());
    }

    @Override
    public void detonate() {
        ((Firework) wrappedObject).detonate();
    }

    @Override
    public boolean isShotAtAngle() {
        return ((Firework) wrappedObject).isShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        ((Firework) wrappedObject).setShotAtAngle(shotAtAngle);
    }
}
