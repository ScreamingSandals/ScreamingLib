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

package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.title.Title;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.TitleUtils;

@Data
public final class TitleTimesWrapper implements Wrapper {
    private final Title.Times times;

    public Title.Times asTitleTimes() {
        return times;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(times)) {
            return (T) times;
        }

        return (T) TitleUtils.timesToPlatform(times, type);
    }
}
