package org.screamingsandals.lib.world.weather;

import org.screamingsandals.lib.utils.ComparableWrapper;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("AlternativeMethodAvailable")
public interface WeatherHolder extends ComparableWrapper {

    String platformName();

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    boolean is(Object object);

    @Override
    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    boolean is(Object... objects);

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    static WeatherHolder of(Object weather) {
        return ofOptional(weather).orElseThrow();
    }

    @CustomAutocompletion(CustomAutocompletion.Type.WEATHER)
    static Optional<WeatherHolder> ofOptional(Object weather) {
        if (weather instanceof WeatherHolder) {
            return Optional.of((WeatherHolder) weather);
        }
        return WeatherMapping.resolve(weather);
    }

    static List<WeatherHolder> all() {
        return WeatherMapping.getValues();
    }
}
