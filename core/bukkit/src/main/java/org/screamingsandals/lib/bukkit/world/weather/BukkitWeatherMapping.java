package org.screamingsandals.lib.bukkit.world.weather;

import org.bukkit.WeatherType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.world.weather.WeatherMapping;

import java.util.Arrays;

@Service
public class BukkitWeatherMapping extends WeatherMapping {
    public BukkitWeatherMapping() {
        weatherConverter
                .registerP2W(WeatherType.class, BukkitWeatherHolder::new);

        Arrays.stream(WeatherType.values()).forEach(weather -> {
            var holder = new BukkitWeatherHolder(weather);
            mapping.put(NamespacedMappingKey.of(weather.name()), holder);
            values.add(holder);
        });
    }
}
