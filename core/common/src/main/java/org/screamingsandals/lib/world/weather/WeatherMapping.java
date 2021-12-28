package org.screamingsandals.lib.world.weather;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+\\.[^\\.]+)\\.(?<className>.+)$"
)
public abstract class WeatherMapping extends AbstractTypeMapper<WeatherHolder> {
    private static WeatherMapping weatherMapping;

    protected final BidirectionalConverter<WeatherHolder> weatherConverter = BidirectionalConverter.<WeatherHolder>build()
            .registerP2W(WeatherHolder.class, d -> d);

    @ApiStatus.Internal
    public WeatherMapping() {
        if (weatherMapping != null) {
            throw new UnsupportedOperationException("WeatherMapping is already initialized!");
        }
        weatherMapping = this;
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    @OfMethodAlternative(value = WeatherHolder.class, methodName = "ofOptional")
    public static Optional<WeatherHolder> resolve(Object weather) {
        if (weatherMapping == null) {
            throw new UnsupportedOperationException("WeatherMapping is not initialized yet.");
        }

        if (weather == null) {
            return Optional.empty();
        }

        return weatherMapping.weatherConverter.convertOptional(weather).or(() -> weatherMapping.resolveFromMapping(weather));
    }

    @OfMethodAlternative(value = WeatherHolder.class, methodName = "all")
    public static List<WeatherHolder> getValues() {
        if (weatherMapping == null) {
            throw new UnsupportedOperationException("WeatherMapping is not initialized yet.");
        }
        return Collections.unmodifiableList(weatherMapping.values);
    }
}