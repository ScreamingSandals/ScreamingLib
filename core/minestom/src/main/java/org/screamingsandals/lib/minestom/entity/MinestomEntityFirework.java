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

package org.screamingsandals.lib.minestom.entity;

import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.FireworkRocketMeta;
import net.minestom.server.item.firework.FireworkEffect;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTList;
import org.jglrxavpok.hephaistos.nbt.NBTType;
import org.jglrxavpok.hephaistos.nbt.mutable.MutableNBTCompound;
import org.screamingsandals.lib.entity.EntityFirework;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MinestomEntityFirework extends MinestomEntityProjectile implements EntityFirework {
    protected MinestomEntityFirework(EntityProjectile wrappedObject) {
        super(wrappedObject);
        if (wrappedObject.getEntityType() != EntityType.FIREWORK_ROCKET) {
            throw new UnsupportedOperationException("Passed a non-firework rocket entity to MinestomEntityFirework!");
        }
    }

    @Override
    public void setEffect(List<FireworkEffectHolder> fireworkEffect, int power) {
        // TODO: make sure this works
        final MutableNBTCompound nbt = ((FireworkRocketMeta) wrappedObject.getEntityMeta()).getFireworkInfo().meta().toNBT().toMutableCompound();
        final MutableNBTCompound fireworksCompound = Objects.requireNonNullElseGet(nbt.getCompound("Fireworks"), MutableNBTCompound::new);
        final NBTList<NBTCompound> explosionsNbt = new NBTList<>(NBTType.TAG_Compound);
        fireworkEffect.forEach(holder -> explosionsNbt.add(holder.as(FireworkEffect.class).asCompound()));
        fireworksCompound.set("Explosions", explosionsNbt);
        fireworksCompound.setInt("Flight", power);
        nbt.set("Fireworks", fireworksCompound);

        //noinspection UnstableApiUsage
        ((FireworkRocketMeta) wrappedObject.getEntityMeta()).setFireworkInfo(
                ((FireworkRocketMeta) wrappedObject.getEntityMeta()).getFireworkInfo().withMeta(
                        ((FireworkRocketMeta) wrappedObject.getEntityMeta()).getFireworkInfo().meta().with(builder -> builder.read(nbt))
                )
        );
    }

    @Override
    public Pair<List<FireworkEffectHolder>, Integer> getEffect() {
        // TODO: make sure this works
        final NBTCompound nbt = ((FireworkRocketMeta) wrappedObject.getEntityMeta()).getFireworkInfo().meta().toNBT();
        final NBTCompound fireworksCompound = Objects.requireNonNull(nbt.getCompound("Fireworks"), "Fireworks NBT compound not found");
        final NBTList<NBTCompound> explosionsNbt = fireworksCompound.getList("Explosions");
        final List<FireworkEffectHolder> explosions = new ArrayList<>();
        if (explosionsNbt != null) {
            explosions.addAll(
                    StreamSupport.stream(explosionsNbt.spliterator(), false)
                            .map(FireworkEffect::fromCompound)
                            .map(FireworkEffectHolder::of)
                            .collect(Collectors.toList())
            );
        }
        return Pair.of(explosions, fireworksCompound.getAsInt("Flight"));
    }

    @Override
    public void detonate() {

    }

    @Override
    public boolean isShotAtAngle() {
        return ((FireworkRocketMeta) wrappedObject.getEntityMeta()).isShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        ((FireworkRocketMeta) wrappedObject.getEntityMeta()).setShotAtAngle(shotAtAngle);
    }
}
