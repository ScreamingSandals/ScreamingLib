package org.screamingsandals.lib.impl.paper.ai.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;

public class PaperGoalType extends BasicWrapper<GoalKey<?>> implements GoalType {
    protected PaperGoalType(@NotNull GoalKey<?> wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.getNamespacedKey().toString();
    }

    @Override
    public boolean applicableTo(@NotNull EntityType entityType) {
        return false;
    }

    @Override
    public @Nullable Goal createGoal(@NotNull Entity entity, @NotNull Object @NotNull ... parameters) {
        return null;
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof GoalKey || object instanceof PaperGoalType) {
            return equals(object);
        }
        return equals(GoalType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(wrappedObject.getNamespacedKey().getNamespace(), wrappedObject.getNamespacedKey().getKey());
    }
}
