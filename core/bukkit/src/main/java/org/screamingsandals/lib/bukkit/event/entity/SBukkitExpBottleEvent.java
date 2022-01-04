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

package org.screamingsandals.lib.bukkit.event.entity;

import org.bukkit.event.entity.ExpBottleEvent;
import org.screamingsandals.lib.event.entity.SExpBottleEvent;

public class SBukkitExpBottleEvent extends SBukkitProjectileHitEvent implements SExpBottleEvent {
    public SBukkitExpBottleEvent(ExpBottleEvent event) {
        super(event);
    }

    @Override
    public int getExp() {
        return getEvent().getExperience();
    }

    @Override
    public void setExp(int exp) {
        getEvent().setExperience(exp);
    }

    @Override
    public boolean isShowEffect() {
        return getEvent().getShowEffect();
    }

    @Override
    public void setShowEffect(boolean showEffect) {
        getEvent().setShowEffect(showEffect);
    }

    @Override
    public ExpBottleEvent getEvent() {
        return (ExpBottleEvent) super.getEvent();
    }
}
