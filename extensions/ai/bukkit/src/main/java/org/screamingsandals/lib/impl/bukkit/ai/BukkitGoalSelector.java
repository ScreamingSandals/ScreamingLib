/*
 * Copyright 2023 ScreamingSandals
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
import org.screamingsandals.lib.impl.nms.accessors.EntityType_i_EntityFactoryAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FishingHookAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FloatGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.GoalSelectorAccessor;
import org.screamingsandals.lib.impl.nms.accessors.HurtByTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LightningBoltAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LivingEntityAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MeleeAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MobAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NearestAttackableTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PlayerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomStrollGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RegistryAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ResourceLocationAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WrappedGoalAccessor;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.lang.reflect.Method;
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
        Preconditions.checkArgument(MobAccessor.getType().isInstance(entity));
    }

    @Override
    public void add(int priority, @NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodAddGoal1(), priority, goal.raw());
        }
    }

    @Override
    public boolean has(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    if (Objects.equals(Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1()), goal.raw())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    if (Objects.equals(Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a()), goal.raw())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean has(@NotNull GoalType type) {
        return hasInSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type)
                || hasInSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type);
    }

    private boolean hasInSelector(@Nullable Object selector, @NotNull GoalType type) {
        if (selector == null) {
            return false;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && type.as(Class.class).equals(obj.getClass())) {
                        return true;
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
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

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type, list);

        return Collections.unmodifiableList(list);
    }

    private void getForSelector(@Nullable Object selector, @Nullable GoalType type, @NotNull List<@NotNull Goal> finalGoals) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        finalGoals.add(new BukkitGoal(obj));
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
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

        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), null, list);
        getForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), null, list);

        return Collections.unmodifiableList(list);
    }

    @Override
    public void removeAll() {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), null);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), null);
    }

    @Override
    public void remove(@NotNull Goal goal) {
        var selector = getSelector(goal);

        if (selector != null) {
            Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), goal.raw());
        }
    }

    @Override
    public void remove(@NotNull GoalType type) {
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector()), type);
        removeForSelector(Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector()), type);
    }

    private void removeForSelector(@Nullable Object selector, @Nullable GoalType type) {
        if (selector == null) {
            return;
        }

        if (GoalSelectorAccessor.getFieldAvailableGoals() != null) {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldAvailableGoals());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.fastInvoke(g, WrappedGoalAccessor.getMethodGetGoal1());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), obj);
                    }
                }
            }
        } else {
            var goals = Reflect.getField(selector, GoalSelectorAccessor.getFieldField_75782_a());

            if (goals instanceof Collection) { // therefore is not null
                for (var g : List.copyOf((Collection<?>) goals)) {
                    var obj = Reflect.getField(g, PathfinderGoalSelector_i_PathfinderGoalSelectorItemAccessor.getFieldField_75733_a());
                    if (obj != null && (type == null || type.as(Class.class).equals(obj.getClass()))) {
                        Reflect.fastInvoke(selector, GoalSelectorAccessor.getMethodRemoveGoal1(), obj);
                    }
                }
            }
        }
    }

    private @Nullable Object getSelector(@NotNull Goal goal) {
        if (goal.isTarget()) {
            return Reflect.getField(wrappedObject, MobAccessor.getFieldTargetSelector());
        } else {
            return Reflect.getField(wrappedObject, MobAccessor.getFieldGoalSelector());
        }
    }

    @Override
    public @Nullable Goal addFloatGoal(int priority) {
        var goal = Reflect.construct(FloatGoalAccessor.getConstructor0(), wrappedObject);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addMeleeAttackGoal(int priority, double speed, boolean pauseWhenMobIdle) {
        var goal = Reflect.construct(MeleeAttackGoalAccessor.getConstructor0(), wrappedObject, speed, pauseWhenMobIdle);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addRandomStrollGoal(int priority, double speed) {
        var goal = Reflect.construct(RandomStrollGoalAccessor.getConstructor0(), wrappedObject, speed);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addRandomLookAroundGoal(int priority) {
        var goal = Reflect.construct(MeleeAttackGoalAccessor.getConstructor0(), wrappedObject);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addNearestAttackableTargetGoal(int priority, @NotNull EntityType type, boolean checkVisibility) {
        var constructor = NearestAttackableTargetGoalAccessor.getConstructor0() != null ? NearestAttackableTargetGoalAccessor.getConstructor0() : NearestAttackableTargetGoalAccessor.getConstructor1();
        var goal = Reflect.construct(constructor, wrappedObject, getEntityClass(type), checkVisibility);
        return addNewlyConstructedGoal(priority, goal);
    }

    @Override
    public @Nullable Goal addHurtByTargetGoal(int priority, @NotNull List<@NotNull EntityType> ignoredEntities) {
        var classes = ignoredEntities.stream().map(this::getEntityClass).filter(Objects::nonNull).collect(Collectors.toList());

        @Nullable Object goal;
        if (HurtByTargetGoalAccessor.getConstructor0() != null) {
            goal = Reflect.construct(HurtByTargetGoalAccessor.getConstructor0(), wrappedObject, classes);
        } else {
            goal = Reflect.construct(HurtByTargetGoalAccessor.getConstructor1(), wrappedObject, true, classes);
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
            return ServerPlayerAccessor.getType();
        }

        if (EntityTypeAccessor.getFieldFactory() != null) { // 1.14+
            var optional = Reflect.fastInvoke(EntityTypeAccessor.getMethodByString1(),  new Object[] {type.as(org.bukkit.entity.EntityType.class).getKey().toString()});

            if (optional instanceof Optional && ((Optional<?>) optional).isPresent()) {
                var entityType = ((Optional<?>) optional).get();

                return ENTITY_CLASS_CACHE.computeIfAbsent(entityType, e -> {
                    var factory = Reflect.getField(entityType, EntityTypeAccessor.getFieldFactory());

                    // Here I don't know any better way to obtain the entity class, here we create a dummy entity which we don't spawn and get its class. I hope this operation does not have any consequences for specific entity types.
                    var object = Reflect.fastInvoke(factory, EntityType_i_EntityFactoryAccessor.getMethodCreate1(), entityType, Reflect.fastInvoke(wrappedObject, EntityAccessor.getMethodGetCommandSenderWorld1()));
                    if (object != null) {
                        return object.getClass();
                    }

                    return null;
                });
            }
        } else if (EntityTypeAccessor.getMethodFunc_201760_c1() != null) { // 1.13.X
            var nullable = Reflect.fastInvoke(EntityTypeAccessor.getMethodFunc_200713_a1(), new Object[] {type.as(org.bukkit.entity.EntityType.class).getName()}); // use actual key instead of translated
            return (Class<?>) Reflect.fastInvoke(nullable, EntityTypeAccessor.getMethodFunc_201760_c1());
        } else {
            if (type.is("minecraft:lightning_bolt")) {
                return LightningBoltAccessor.getType();
            }

            if (type.is("minecraft:fishing_hook")) {
                return FishingHookAccessor.getType();
            }

            if (EntityTypeAccessor.getFieldField_191308_b() != null) { // 1.11-1.12.2
                @SuppressWarnings("unchecked")
                var map = (Map<Object, Class<?>>) Reflect.getField(EntityTypeAccessor.getFieldField_191308_b());
                return map.get(Reflect.construct(ResourceLocationAccessor.getConstructor0(), type.as(org.bukkit.entity.EntityType.class).getName()));
            } else if (EntityTypeAccessor.getFieldField_75625_b() != null) { // 1.8-1.10.2
                @SuppressWarnings("unchecked")
                var map = (Map<String, Class<?>>) Reflect.getField(EntityTypeAccessor.getFieldField_75625_b());
                return map.get(type.as(org.bukkit.entity.EntityType.class).getName());
            }
        }

        return null;
    }
}
