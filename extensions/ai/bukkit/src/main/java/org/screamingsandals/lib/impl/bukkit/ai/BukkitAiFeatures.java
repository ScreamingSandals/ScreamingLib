package org.screamingsandals.lib.impl.bukkit.ai;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class BukkitAiFeatures {
    public static final @NotNull PlatformFeature PAPER_MOB_GOAL_API = PlatformFeature.of(() -> Reflect.has("com.destroystokyo.paper.entity.ai.Goal"));
}
