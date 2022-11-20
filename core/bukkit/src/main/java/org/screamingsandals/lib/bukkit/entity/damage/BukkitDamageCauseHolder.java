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

package org.screamingsandals.lib.bukkit.entity.damage;

import org.bukkit.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.entity.damage.DamageCauseHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;

public class BukkitDamageCauseHolder extends BasicWrapper<EntityDamageEvent.DamageCause> implements DamageCauseHolder {

    public BukkitDamageCauseHolder(EntityDamageEvent.DamageCause wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object damageCause) {
        if (damageCause instanceof EntityDamageEvent.DamageCause || damageCause instanceof DamageCauseHolder) {
            return equals(damageCause);
        }
        return equals(DamageCauseHolder.ofNullable(damageCause));
    }

    @Override
    public boolean is(Object... damageCauses) {
        return Arrays.stream(damageCauses).anyMatch(this::is);
    }
}
