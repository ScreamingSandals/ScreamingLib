package org.screamingsandals.lib.impl.bukkit.compat.datafixer2;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.nms.accessors.nbt.NbtOpsAccessor;
import org.screamingsandals.lib.impl.nms.accessors.util.datafix.fixes.ReferencesAccessor;

@UtilityClass
public class DataFixer2Compat {
    @SuppressWarnings("unchecked")
    public static @NotNull Object dataFix(@NotNull DataFixer fixerUpper, @NotNull Object vanilla, int dataVersion, int currentVersion) {
        return fixerUpper
                .update((DSL.TypeReference) ReferencesAccessor.CONST_ITEM_STACK.get(), new Dynamic<>((DynamicOps<Object>) NbtOpsAccessor.CONST_INSTANCE.get(), vanilla), dataVersion, currentVersion)
                .getValue();
    }
}
