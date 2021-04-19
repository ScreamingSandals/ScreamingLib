package org.screamingsandals.lib.placeholders;

import lombok.Data;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;

@Data
@ServiceDependencies(dependsOn = {
        PlaceholderManager.class
})
public abstract class PlaceholderExpansion {
    private final String identifier;

    @Nullable
    public abstract Component onRequest(@Nullable MultiPlatformOfflinePlayer player, @NotNull String params);

    @OnPostEnable
    public void onPostEnable() {
        PlaceholderManager.registerExpansion(this);
    }
}
