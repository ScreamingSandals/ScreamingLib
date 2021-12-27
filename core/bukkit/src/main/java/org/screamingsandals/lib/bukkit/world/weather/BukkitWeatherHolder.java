package org.screamingsandals.lib.bukkit.world.weather;

import org.bukkit.WeatherType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.Arrays;

public class BukkitWeatherHolder extends BasicWrapper<WeatherType> implements WeatherHolder {

    protected BukkitWeatherHolder(WeatherType wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.name();
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof WeatherType || object instanceof WeatherHolder) {
            return equals(object);
        }
        return equals(WeatherHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }
}
