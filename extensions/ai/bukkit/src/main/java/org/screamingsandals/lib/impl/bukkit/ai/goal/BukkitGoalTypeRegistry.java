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

package org.screamingsandals.lib.impl.bukkit.ai.goal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.ai.goal.GoalType;
import org.screamingsandals.lib.ai.impl.goal.GoalTypeRegistry;
import org.screamingsandals.lib.impl.nms.accessors.AvoidEntityGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.BegGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.BreakDoorGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.BreathAirGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.BreedGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.CatLieOnBedGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.CatSitOnBlockGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ClimbOnTopOfPowderSnowGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.DefendVillageTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.DolphinJumpGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.DoorInteractGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.EatBlockGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FleeSunGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FloatGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FollowBoatGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FollowFlockLeaderGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FollowMobGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FollowOwnerGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.FollowParentGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.GolemRandomStrollInVillageGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.HurtByTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.InteractGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.JumpGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LandOnOwnersShoulderGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LeapAtTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LlamaFollowCaravanGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LookAtPlayerGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.LookAtTradingPlayerGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MeleeAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MoveBackToVillageGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MoveThroughVillageGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MoveToBlockGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MoveTowardsRestrictionGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.MoveTowardsTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NearestAttackableTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NearestAttackableWitchTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NearestHealableRaiderTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.NonTameRandomTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.OcelotAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.OfferFlowerGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.OpenDoorGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.OwnerHurtByTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.OwnerHurtTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PanicGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.PathfindToRaidGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomLookAroundGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomStandGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomStrollGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RandomSwimmingGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RangedAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RangedBowAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RangedCrossbowAttackGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RemoveBlockGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ResetUniversalAngerTargetGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RestrictSunGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.RunAroundLikeCrazyGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.SitWhenOrderedToGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.StrollThroughVillageGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.SwellGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.TemptGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.TradeWithPlayerGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.TryFindWaterGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.UseItemGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WaterAvoidingRandomFlyingGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WaterAvoidingRandomStrollGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.WrappedGoalAccessor;
import org.screamingsandals.lib.impl.nms.accessors.ZombieAttackGoalAccessor;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BukkitGoalTypeRegistry extends GoalTypeRegistry {
    private final @NotNull Map<@NotNull ResourceLocation, BukkitGoalType> LOCATION_GOAL_MAP = new HashMap<>();

    public BukkitGoalTypeRegistry() {
        // TODO: finish this list or find a better way to do this
        putSafe("avoid_entity", AvoidEntityGoalAccessor.TYPE.get());
        putSafe("beg", BegGoalAccessor.TYPE.get());
        putSafe("break_door", BreakDoorGoalAccessor.TYPE.get());
        putSafe("breath_air", BreathAirGoalAccessor.TYPE.get());
        putSafe("breed", BreedGoalAccessor.TYPE.get());
        putSafe("cat_lie_on_bed", CatLieOnBedGoalAccessor.TYPE.get());
        putSafe("cat_sit_on_block", CatSitOnBlockGoalAccessor.TYPE.get());
        putSafe("climb_on_top_of_powder_snow", ClimbOnTopOfPowderSnowGoalAccessor.TYPE.get());
        putSafe("dolphin_jump", DolphinJumpGoalAccessor.TYPE.get());
        putSafe("door_interact", DoorInteractGoalAccessor.TYPE.get());
        putSafe("eat_block", EatBlockGoalAccessor.TYPE.get());
        putSafe("flee_sun", FleeSunGoalAccessor.TYPE.get());
        putSafe("float", FloatGoalAccessor.TYPE.get());
        putSafe("follow_boat", FollowBoatGoalAccessor.TYPE.get());
        putSafe("follow_flock_leader", FollowFlockLeaderGoalAccessor.TYPE.get());
        putSafe("follow_mob", FollowMobGoalAccessor.TYPE.get());
        putSafe("follow_owner", FollowOwnerGoalAccessor.TYPE.get());
        putSafe("follow_parent", FollowParentGoalAccessor.TYPE.get());
        putSafe("golem_random_stroll_in_village", GolemRandomStrollInVillageGoalAccessor.TYPE.get());
        putSafe("interact", InteractGoalAccessor.TYPE.get());
        putSafe("jump", JumpGoalAccessor.TYPE.get());
        putSafe("land_on_owners_shoulder", LandOnOwnersShoulderGoalAccessor.TYPE.get());
        putSafe("leap_at_target", LeapAtTargetGoalAccessor.TYPE.get());
        putSafe("llama_follow_caravan", LlamaFollowCaravanGoalAccessor.TYPE.get());
        putSafe("look_at_player", LookAtPlayerGoalAccessor.TYPE.get());
        putSafe("look_at_trading_player", LookAtTradingPlayerGoalAccessor.TYPE.get());
        putSafe("melee_attack", MeleeAttackGoalAccessor.TYPE.get());
        putSafe("move_back_to_village", MoveBackToVillageGoalAccessor.TYPE.get());
        putSafe("move_through_village", MoveThroughVillageGoalAccessor.TYPE.get());
        putSafe("move_to_block", MoveToBlockGoalAccessor.TYPE.get());
        putSafe("move_towards_restriction", MoveTowardsRestrictionGoalAccessor.TYPE.get());
        putSafe("move_towards_target", MoveTowardsTargetGoalAccessor.TYPE.get());
        putSafe("ocelot_attack", OcelotAttackGoalAccessor.TYPE.get());
        putSafe("offer_flower", OfferFlowerGoalAccessor.TYPE.get());
        putSafe("open_door", OpenDoorGoalAccessor.TYPE.get());
        putSafe("panic", PanicGoalAccessor.TYPE.get());
        putSafe("pathfind_to_raid", PathfindToRaidGoalAccessor.TYPE.get());
        putSafe("random_look_around", RandomLookAroundGoalAccessor.TYPE.get());
        putSafe("random_stand", RandomStandGoalAccessor.TYPE.get());
        putSafe("random_stroll", RandomStrollGoalAccessor.TYPE.get());
        putSafe("random_swimming", RandomSwimmingGoalAccessor.TYPE.get());
        putSafe("ranged_attack", RangedAttackGoalAccessor.TYPE.get());
        putSafe("ranged_bow_attack", RangedBowAttackGoalAccessor.TYPE.get());
        putSafe("ranged_crossbow_attack", RangedCrossbowAttackGoalAccessor.TYPE.get());
        putSafe("remove_block", RemoveBlockGoalAccessor.TYPE.get());
        putSafe("restrict_sun", RestrictSunGoalAccessor.TYPE.get());
        putSafe("run_around_like_crazy", RunAroundLikeCrazyGoalAccessor.TYPE.get());
        putSafe("sit_when_ordered_to", SitWhenOrderedToGoalAccessor.TYPE.get());
        putSafe("stroll_through_village", StrollThroughVillageGoalAccessor.TYPE.get());
        putSafe("swell", SwellGoalAccessor.TYPE.get());
        putSafe("tempt", TemptGoalAccessor.TYPE.get());
        putSafe("trade_with_player", TradeWithPlayerGoalAccessor.TYPE.get());
        putSafe("try_find_water", TryFindWaterGoalAccessor.TYPE.get());
        putSafe("use_item", UseItemGoalAccessor.TYPE.get());
        putSafe("water_avoiding_random_flying", WaterAvoidingRandomFlyingGoalAccessor.TYPE.get());
        putSafe("water_avoiding_random_stroll", WaterAvoidingRandomStrollGoalAccessor.TYPE.get());
        putSafe("wrapped", WrappedGoalAccessor.TYPE.get());
        putSafe("zombie_attack", ZombieAttackGoalAccessor.TYPE.get());

        putSafe("defend_village", DefendVillageTargetGoalAccessor.TYPE.get());
        putSafe("hurt_by", HurtByTargetGoalAccessor.TYPE.get());
        putSafe("nearest_attackable", NearestAttackableTargetGoalAccessor.TYPE.get());
        putSafe("nearest_attackable_witch", NearestAttackableWitchTargetGoalAccessor.TYPE.get());
        putSafe("nearest_healable_raider", NearestHealableRaiderTargetGoalAccessor.TYPE.get());
        putSafe("non_tame_random", NonTameRandomTargetGoalAccessor.TYPE.get());
        putSafe("owner_hurt_by", OwnerHurtByTargetGoalAccessor.TYPE.get());
        putSafe("owner_hurt", OwnerHurtTargetGoalAccessor.TYPE.get());
        putSafe("reset_universal_anger", ResetUniversalAngerTargetGoalAccessor.TYPE.get());
    }

    private void putSafe(@NotNull String path, @Nullable Class<?> clazz) {
        if (clazz != null) {
            var loc = ResourceLocation.of("minecraft", path);
            LOCATION_GOAL_MAP.put(loc, new BukkitGoalType(clazz, loc));
        }
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull GoalType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                LOCATION_GOAL_MAP.entrySet()::stream,
                Map.Entry::getValue,
                Map.Entry::getKey,
                (v, s) -> v.getKey().path().contains(s),
                (v, s) -> v.getKey().namespace().equals(s),
                List.of()
        );
    }

    @Override
    protected @Nullable GoalType resolveMappingPlatform(@NotNull ResourceLocation location) {
        return LOCATION_GOAL_MAP.get(location);
    }
}
