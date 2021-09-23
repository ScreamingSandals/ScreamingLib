package org.screamingsandals.lib.world.weather;

import lombok.Data;
import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
@Data
public class WeatherHolder implements ComparableWrapper {
    private final String platformName;

    @Override
    public <T> T as(Class<T> type) {
        return WeatherMapping.convertWeatherHolder(this, type);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    public boolean is(Object object) {
        return equals(ofOptional(object).orElse(null));
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    public static WeatherHolder of(Object weather) {
        return ofOptional(weather).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    public static Optional<WeatherHolder> ofOptional(Object weather) {
        if (weather instanceof WeatherHolder) {
            return Optional.of((WeatherHolder) weather);
        }
        return WeatherMapping.resolve(weather);
    }

    public static List<WeatherHolder> all() {
        return WeatherMapping.getValues();
    }
}
