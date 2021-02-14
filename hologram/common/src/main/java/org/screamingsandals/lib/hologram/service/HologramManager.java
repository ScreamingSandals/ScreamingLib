package org.screamingsandals.lib.hologram.service;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.hologram.builder.TextHologramBuilder;

public class HologramManager {

    public void test() {
        final var holo = TextHologramBuilder.newBuilder()
                .line(1, Component.text("Fuck you!"))
                .touch((playerWrapper, hologram) -> {

                })
                .build();
    }
}
