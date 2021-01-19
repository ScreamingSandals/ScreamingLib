package org.screamingsandals.sponge.world;

import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.world.LocationMapping;

public class SpongeLocationMapping extends LocationMapping {

    public static void init() {
        LocationMapping.init(SpongeLocationMapping::new);
    }

    public SpongeLocationMapping() {
        platform = Platform.JAVA_FLATTENING;

        //todo
    }
}
