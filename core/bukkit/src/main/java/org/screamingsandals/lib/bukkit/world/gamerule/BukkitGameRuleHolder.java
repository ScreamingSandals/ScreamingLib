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

package org.screamingsandals.lib.bukkit.world.gamerule;

import org.bukkit.GameRule;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.gamerule.GameRuleHolder;

import java.util.Arrays;

public class BukkitGameRuleHolder extends BasicWrapper<GameRule<?>> implements GameRuleHolder {

    protected BukkitGameRuleHolder(GameRule<?> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof GameRule || object instanceof GameRuleHolder) {
            return equals(object);
        }
        return equals(GameRuleHolder.ofNullable(object));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
