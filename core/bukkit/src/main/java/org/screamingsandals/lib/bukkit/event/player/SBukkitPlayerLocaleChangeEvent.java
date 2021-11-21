package org.screamingsandals.lib.bukkit.event.player;

import lombok.Data;
import net.kyori.adventure.translation.Translator;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.event.player.SPlayerLocaleChangeEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Locale;

@Data
public class SBukkitPlayerLocaleChangeEvent implements SPlayerLocaleChangeEvent {
    private final PlayerLocaleChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private Locale locale;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public Locale getLocale() {
        if (locale == null) {
            if (Reflect.hasMethod(event, "locale")) {
                locale = event.locale(); // java.util.Locale is not an adventure thing so we can
            } else {
                locale = Translator.parseLocale(event.getLocale());
            }
        }
        return locale;
    }
}
