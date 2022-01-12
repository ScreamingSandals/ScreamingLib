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

import net.minestom.server.entity.Player;
import org.screamingsandals.lib.entity.EntityHuman;

public class MinestomEntityHuman extends MinestomEntityLiving implements EntityHuman {
    protected MinestomEntityHuman(Player wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExpToLevel() {
        return ((Player) wrappedObject).getLevel();
    }

    @Override
    public float getSaturation() {
        return ((Player) wrappedObject).getFoodSaturation();
    }

    @Override
    public void setSaturation(float saturation) {
        ((Player) wrappedObject).setFoodSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return 0;
    }

    @Override
    public void setExhaustion(float exhaustion) {

    }

    @Override
    public int getFoodLevel() {
        return ((Player) wrappedObject).getFood();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        ((Player) wrappedObject).setFood(foodLevel);
    }
}
