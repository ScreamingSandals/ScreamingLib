package org.screamingsandals.lib.signs;

import org.bukkit.Location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignBlock {
    private final Location location;
    private final String name;
}
