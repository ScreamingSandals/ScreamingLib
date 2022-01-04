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

package org.screamingsandals.lib.world.weather;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.configurate.WeatherHolderSerializer;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class WeatherMapping extends AbstractTypeMapper<WeatherHolder> {
    private static WeatherMapping weatherMapping;

    protected final BidirectionalConverter<WeatherHolder> weatherConverter = BidirectionalConverter.<WeatherHolder>build()
            .registerP2W(WeatherHolder.class, d -> d)
            .registerP2W(ConfigurationNode.class, node -> {
                try {
                    return WeatherHolderSerializer.INSTANCE.deserialize(WeatherHolder.class, node);
                } catch (SerializationException ex) {
                    ex.printStackTrace();
                    return null;
                }
            });

    @ApiStatus.Internal
    public WeatherMapping() {
        Preconditions.checkArgument(weatherMapping == null, "WeatherMapping is already initialized!");
        weatherMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    @OfMethodAlternative(value = WeatherHolder.class, methodName = "ofOptional")
    public static Optional<WeatherHolder> resolve(Object weather) {
        Preconditions.checkNotNull(weatherMapping, "WeatherMapping is not initialized yet!");

        if (weather == null) {
            return Optional.empty();
        }

        return weatherMapping.weatherConverter.convertOptional(weather).or(() -> weatherMapping.resolveFromMapping(weather));
    }

    @OfMethodAlternative(value = WeatherHolder.class, methodName = "all")
    public static List<WeatherHolder> getValues() {
        Preconditions.checkNotNull(weatherMapping, "WeatherMapping is not initialized yet!");
        return Collections.unmodifiableList(weatherMapping.values);
    }
}
