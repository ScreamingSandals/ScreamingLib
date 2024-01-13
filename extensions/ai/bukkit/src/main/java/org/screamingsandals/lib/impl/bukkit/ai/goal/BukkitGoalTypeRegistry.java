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
        putSafe("avoid_entity", AvoidEntityGoalAccessor.getType());
        putSafe("beg", BegGoalAccessor.getType());
        putSafe("break_door", BreakDoorGoalAccessor.getType());
        putSafe("breath_air", BreathAirGoalAccessor.getType());
        putSafe("breed", BreedGoalAccessor.getType());
        putSafe("cat_lie_on_bed", CatLieOnBedGoalAccessor.getType());
        putSafe("cat_sit_on_block", CatSitOnBlockGoalAccessor.getType());
        putSafe("climb_on_top_of_powder_snow", ClimbOnTopOfPowderSnowGoalAccessor.getType());
        putSafe("dolphin_jump", DolphinJumpGoalAccessor.getType());
        putSafe("door_interact", DoorInteractGoalAccessor.getType());
        putSafe("eat_block", EatBlockGoalAccessor.getType());
        putSafe("flee_sun", FleeSunGoalAccessor.getType());
        putSafe("float", FloatGoalAccessor.getType());
        putSafe("follow_boat", FollowBoatGoalAccessor.getType());
        putSafe("follow_flock_leader", FollowFlockLeaderGoalAccessor.getType());
        putSafe("follow_mob", FollowMobGoalAccessor.getType());
        putSafe("follow_owner", FollowOwnerGoalAccessor.getType());
        putSafe("follow_parent", FollowParentGoalAccessor.getType());
        putSafe("golem_random_stroll_in_village", GolemRandomStrollInVillageGoalAccessor.getType());
        putSafe("interact", InteractGoalAccessor.getType());
        putSafe("jump", JumpGoalAccessor.getType());
        putSafe("land_on_owners_shoulder", LandOnOwnersShoulderGoalAccessor.getType());
        putSafe("leap_at_target", LeapAtTargetGoalAccessor.getType());
        putSafe("llama_follow_caravan", LlamaFollowCaravanGoalAccessor.getType());
        putSafe("look_at_player", LookAtPlayerGoalAccessor.getType());
        putSafe("look_at_trading_player", LookAtTradingPlayerGoalAccessor.getType());
        putSafe("melee_attack", MeleeAttackGoalAccessor.getType());
        putSafe("move_back_to_village", MoveBackToVillageGoalAccessor.getType());
        putSafe("move_through_village", MoveThroughVillageGoalAccessor.getType());
        putSafe("move_to_block", MoveToBlockGoalAccessor.getType());
        putSafe("move_towards_restriction", MoveTowardsRestrictionGoalAccessor.getType());
        putSafe("move_towards_target", MoveTowardsTargetGoalAccessor.getType());
        putSafe("ocelot_attack", OcelotAttackGoalAccessor.getType());
        putSafe("offer_flower", OfferFlowerGoalAccessor.getType());
        putSafe("open_door", OpenDoorGoalAccessor.getType());
        putSafe("panic", PanicGoalAccessor.getType());
        putSafe("pathfind_to_raid", PathfindToRaidGoalAccessor.getType());
        putSafe("random_look_around", RandomLookAroundGoalAccessor.getType());
        putSafe("random_stand", RandomStandGoalAccessor.getType());
        putSafe("random_stroll", RandomStrollGoalAccessor.getType());
        putSafe("random_swimming", RandomSwimmingGoalAccessor.getType());
        putSafe("ranged_attack", RangedAttackGoalAccessor.getType());
        putSafe("ranged_bow_attack", RangedBowAttackGoalAccessor.getType());
        putSafe("ranged_crossbow_attack", RangedCrossbowAttackGoalAccessor.getType());
        putSafe("remove_block", RemoveBlockGoalAccessor.getType());
        putSafe("restrict_sun", RestrictSunGoalAccessor.getType());
        putSafe("run_around_like_crazy", RunAroundLikeCrazyGoalAccessor.getType());
        putSafe("sit_when_ordered_to", SitWhenOrderedToGoalAccessor.getType());
        putSafe("stroll_through_village", StrollThroughVillageGoalAccessor.getType());
        putSafe("swell", SwellGoalAccessor.getType());
        putSafe("tempt", TemptGoalAccessor.getType());
        putSafe("trade_with_player", TradeWithPlayerGoalAccessor.getType());
        putSafe("try_find_water", TryFindWaterGoalAccessor.getType());
        putSafe("use_item", UseItemGoalAccessor.getType());
        putSafe("water_avoiding_random_flying", WaterAvoidingRandomFlyingGoalAccessor.getType());
        putSafe("water_avoiding_random_stroll", WaterAvoidingRandomStrollGoalAccessor.getType());
        putSafe("wrapped", WrappedGoalAccessor.getType());
        putSafe("zombie_attack", ZombieAttackGoalAccessor.getType());

        putSafe("defend_village", DefendVillageTargetGoalAccessor.getType());
        putSafe("hurt_by", HurtByTargetGoalAccessor.getType());
        putSafe("nearest_attackable", NearestAttackableTargetGoalAccessor.getType());
        putSafe("nearest_attackable_witch", NearestAttackableWitchTargetGoalAccessor.getType());
        putSafe("nearest_healable_raider", NearestHealableRaiderTargetGoalAccessor.getType());
        putSafe("non_tame_random", NonTameRandomTargetGoalAccessor.getType());
        putSafe("owner_hurt_by", OwnerHurtByTargetGoalAccessor.getType());
        putSafe("owner_hurt", OwnerHurtTargetGoalAccessor.getType());
        putSafe("reset_universal_anger", ResetUniversalAngerTargetGoalAccessor.getType());
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
