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

import net.minestom.server.entity.ExperienceOrb;
import org.screamingsandals.lib.entity.EntityExperience;

public class MinestomEntityExperience extends MinestomEntityBasic implements EntityExperience {
    protected MinestomEntityExperience(ExperienceOrb wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int getExperience() {
        return ((ExperienceOrb) wrappedObject).getExperienceCount();
    }

    @Override
    public void setExperience(int experience) {
        ((ExperienceOrb) wrappedObject).setExperienceCount((short) experience);
    }
}
