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

package org.screamingsandals.lib.minestom.entity.damage;

import java.util.Objects;

public class MinestomSpecialDamageCauseHolder extends MinestomDamageCauseHolder {
    private final String name;

    protected MinestomSpecialDamageCauseHolder(String name) {
        super(null);
        this.name = name;
    }

    @Override
    public String platformName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinestomDamageCauseHolder)) return false;
        MinestomDamageCauseHolder that = (MinestomDamageCauseHolder) o;
        return name.equals(that.platformName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
