/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.bukkit.ai;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.ai.GoalSelector;
import org.screamingsandals.lib.ai.goal.Goal;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.entity.type.EntityType;
import org.screamingsandals.lib.impl.bukkit.ai.goal.BukkitGoal;
import org.screamingsandals.lib.impl.nms.accessors.EntityAccessor;
import org.screamingsandals.lib.impl.nms.accessors.EntityTypeAccessor;
import org.screamingsandals.lib.impl.nms.accessors.EntityType$EntityFactoryAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FishingHookAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FloatGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.GoalSelectorAccessor;
import org.screamingsandals.lib.impl.nms.accessors.HurtByTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LightningBoltAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MeleeAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MobAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NearestAttackableTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PathfinderGoalSelector$PathfinderGoalSelectorItemAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomStrollGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ResourceLocationAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WrappedGoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BukkitGoalSelector extends BasicWrapper<Object> implements GoalSelector {
    protected BukkitGoalSelector(@NotNull Object entity) {
        super(entity);
        Preconditions.checkArgument(MobAccessor.TYPE.get().isInstance(entity));
    }

    @Override
    public void add(int priority, @NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.METHOD_ADD_GOAL.get(), priority, goal.raw());
        }
    }

    @Override
    public boolean has(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    if (Objects.equals(Reflect.fastInvoke(g, WrappedGoalAccessor.METHOD_GET_GOAL.get()), goal.raw())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_FIELD_75782_A_1.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    if (Objects.equals(Reflect.getField(g, PathfinderGoalSelector$PathfinderGoalSelectorItemAccessor.FIELD_FIELD_75733_A.get()), goal.raw())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean has(@NotNull GoalType type) {
        return hasInSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get()), type)
                || hasInSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get()), type);
    }

    private boolean hasInSelector(@Nullable Object selector, @NotNull GoalType type) {
        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.METHOD_GET_GOAL.get());
                    if (obj != null && type.as(Class.class).equals(obj.getClass())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_FIELD_75782_A_1.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector$PathfinderGoalSelectorItemAccessor.FIELD_FIELD_75733_A.get());
                    if (obj != null && type.as(Class.class).equals(obj.getClass())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Goal> get(@NotNull GoalType type) {
        var list = new ArrayList<Goal>();

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get()), type, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get()), type, list);

        return Collections.unmodifiableList(list);
    }

    private void getForSelector(@Nullable Object selector, @Nullable GoalType type, @NotNull List<@NotNull Goal> finalGoals) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.METHOD_GET_GOAL.get());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        finalGoals.add(new BukkitGoal(obj));
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_FIELD_75782_A_1.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector$PathfinderGoalSelectorItemAccessor.FIELD_FIELD_75733_A.get());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        finalGoals.add(new BukkitGoal(obj));
                    }
                }
            }
        }
    }

    @Override
    public @Unmodifiable @NotNull List<@NotNull Goal> all() {
        var list = new ArrayList<Goal>();

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get()), null, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get()), null, list);

        return Collections.unmodifiableList(list);
    }

    @Override
    public void removeAll() {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get()), null);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get()), null);
    }

    @Override
    public void remove(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.METHOD_REMOVE_GOAL.get(), goal.raw());
        }
    }

    @Override
    public void remove(@NotNull GoalType type) {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get()), type);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get()), type);
    }

    private void removeForSelector(@Nullable Object selector, @Nullable GoalType type) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_AVAILABLE_GOALS.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.METHOD_GET_GOAL.get());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.METHOD_REMOVE_GOAL.get(), obj);
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.FIELD_FIELD_75782_A_1.get());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector$PathfinderGoalSelectorItemAccessor.FIELD_FIELD_75733_A.get());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.METHOD_REMOVE_GOAL.get(), obj);
                    }
                }
            }
        }
    }

    private @Nullable Object getSelector(@NotNull Goal goal) {
        if (goal.isTarget()) {
            return Reflect.getField(wrappedObject, MobAccessor.FIELD_TARGET_SELECTOR.get());
        } else {
            return Reflect.getField(wrappedObject, MobAccessor.FIELD_GOAL_SELECTOR.get());
        }
    }

    @Override
    public @Nullable Goal addFloatGoal(int priority) {
        var goal = Reflect.construct(FloatGoalAccessor.CONSTRUCTOR_0.get(), wrappedObject);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addMeleeAttackGoal(int priority, double speed, boolean pauseWhenMobIdle) {
        var goal = Reflect.construct(MeleeAttackGoalAccessor.CONSTRUCTOR_0.get(), wrappedObject, speed, pauseWhenMobIdle);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addRandomStrollGoal(int priority, double speed) {
        var goal = Reflect.construct(RandomStrollGoalAccessor.CONSTRUCTOR_0.get(), wrappedObject, speed);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addRandomLookAroundGoal(int priority) {
        var goal = Reflect.construct(MeleeAttackGoalAccessor.CONSTRUCTOR_0.get(), wrappedObject);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addNearestAttackableTargetGoal(int priority, @NotNull EntityType type, boolean checkVisibility) {
        var constructor = NearestAttackableTargetGoalAccessor.CONSTRUCTOR_0.get() != null ? NearestAttackableTargetGoalAccessor.CONSTRUCTOR_0.get() : NearestAttackableTargetGoalAccessor.CONSTRUCTOR_1.get();
        var goal = Reflect.construct(constructor, wrappedObject, getEntityClass(type), checkVisibility);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addHurtByTargetGoal(int priority, @NotNull List<@NotNull EntityType> ignoredEntities) {
        var classes = ignoredEntities.stream().map(this::getEntityClass).filter(Objects::nonNull).collect(Collectors.toList());

        @Nullable Object goal;
        if (HurtByTargetGoalAccessor.CONSTRUCTOR_0.get() != null) {
            goal = Reflect.construct(HurtByTargetGoalAccessor.CONSTRUCTOR_0.get(), wrappedObject, classes);
        } else {
            goal = Reflect.construct(HurtByTargetGoalAccessor.CONSTRUCTOR_1.get(), wrappedObject, true, classes);
        }
        return addNewlyConstructedGoal(priority, goal);
    }

    private @Nullable Goal addNewlyConstructedGoal(int priority, @Nullable Object goal) {
        if (goal != null) {
            var bGoal = new BukkitGoal(goal);
            add(priority, bGoal);
            return bGoal;
        }
        return null;
    }

    private static final @NotNull Map<@NotNull Object, Class<?>> ENTITY_CLASS_CACHE = new HashMap<>();

    private @Nullable Class<?> getEntityClass(@NotNull EntityType type) {
        if (type.is("minecraft:player")) {
            return ServerPlayerAccessor.TYPE.get();
        }

        if (EntityTypeAccessor.FIELD_FACTORY.get() != null) { // 1.14+
            var optional = Reflect.fastInvoke(EntityTypeAccessor.METHOD_BY_STRING.get(),  new Object[] {type.as(org.bukkit.entity.EntityType.class).getKey().toString()});

            if (optional instanceof Optional && ((Optional<?>) optional).isPresent()) {
                var entityType = ((Optional<?>) optional).get();

                return ENTITY_CLASS_CACHE.computeIfAbsent(entityType, e -> {
                    var factory = Reflect.getField(entityType, EntityTypeAccessor.FIELD_FACTORY.get());

                    // Here I don't know any better way to obtain the entity class, here we create a dummy entity which we don't spawn and get its class. I hope this operation does not have any consequences for specific entity types.
                    var object = Reflect.fastInvoke(factory, EntityType$EntityFactoryAccessor.METHOD_CREATE.get(), entityType, Reflect.fastInvoke(wrappedObject, EntityAccessor.METHOD_GET_COMMAND_SENDER_WORLD.get()));
                    if (object != null) {
                        return object.getClass();
                    }

                    return null;
                });
            }
        } else if (EntityTypeAccessor.METHOD_FUNC_201760_C.get() != null) { // 1.13.X
            var nullable = Reflect.fastInvoke(EntityTypeAccessor.METHOD_FUNC_200713_A.get(), new Object[] {type.as(org.bukkit.entity.EntityType.class).getName()}); // use actual key instead of translated
            return (Class<?>) Reflect.fastInvoke(nullable, EntityTypeAccessor.METHOD_FUNC_201760_C.get());
        } else {
            if (type.is("minecraft:lightning_bolt")) {
                return LightningBoltAccessor.TYPE.get();
            }

            if (type.is("minecraft:fishing_hook")) {
                return FishingHookAccessor.TYPE.get();
            }

            if (EntityTypeAccessor.FIELD_FIELD_191308_B.get() != null) { // 1.11-1.12.2
                @SuppressWarnings("unchecked")
                var map = (Map<Object, Class<?>>) EntityTypeAccessor.FIELD_FIELD_191308_B.get();
                return map.get(Reflect.construct(ResourceLocationAccessor.CONSTRUCTOR_0.get(), type.as(org.bukkit.entity.EntityType.class).getName()));
            } else if (EntityTypeAccessor.FIELD_FIELD_75625_B.get() != null) { // 1.8-1.10.2
                @SuppressWarnings("unchecked")
                var map = (Map<String, Class<?>>) EntityTypeAccessor.FIELD_FIELD_75625_B.get();
                return map.get(type.as(org.bukkit.entity.EntityType.class).getName());
            }
        }

        return null;
    }
}
