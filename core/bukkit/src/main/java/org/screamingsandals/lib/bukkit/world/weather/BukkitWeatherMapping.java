package org.screamingsandals.lib.bukkit.world.weather;

import org.bukkit.WeatherType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.weather.WeatherHolder;
import org.screamingsandals.lib.world.weather.WeatherMapping;

import java.util.Arrays;

@Service
public class BukkitWeatherMapping extends WeatherMapping {
    public BukkitWeatherMapping() {
        weatherConverter
                .registerP2W(WeatherType.class, weather -> new WeatherHolder(weather.name()))
                .registerW2P(WeatherType.class, weatherHolder -> WeatherType.valueOf(weatherHolder.getPlatformName()));

        Arrays.stream(WeatherType.values()).forEach(weather -> mapping.put(NamespacedMappingKey.of(weather.name()), new WeatherHolder(weather.name())));
    }
}
