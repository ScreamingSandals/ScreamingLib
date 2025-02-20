/*
 * Copyright 2025 ScreamingSandals
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

package org.screamingsandals.lib.impl.attribute;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class AttributeModifierIds {
    private static final @NotNull Map<@NotNull UUID, ResourceLocation> UUID_TO_RESOURCE_LOCATION = new HashMap<>();
    private static final @NotNull Map<@NotNull ResourceLocation, UUID> RESOURCE_LOCATION_TO_UUID;
    private static final @NotNull Map<@NotNull String, ResourceLocation> NAME_TO_RESOURCE_LOCATION = new HashMap<>();
    private static final @NotNull Map<@NotNull ResourceLocation, String> RESOURCE_LOCATION_TO_NAME;

    static {
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("736565d2-e1a7-403d-a3f8-1aeb3e302542"), ResourceLocation.of("minecraft", "creative_mode_block_range"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("98491ef6-97b1-4584-ae82-71a8cc85cf73"), ResourceLocation.of("minecraft", "creative_mode_entity_range"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"), ResourceLocation.of("minecraft", "effect.speed"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("7107DE5E-7CE8-4030-940E-514C1F160890"), ResourceLocation.of("minecraft", "effect.slowness"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3"), ResourceLocation.of("minecraft", "effect.haste"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("55FCED67-E92A-486E-9800-B47F202C4386"), ResourceLocation.of("minecraft", "effect.mining_fatigue"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"), ResourceLocation.of("minecraft", "effect.strength"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("C0105BF3-AEF8-46B0-9EBC-92943757CCBE"), ResourceLocation.of("minecraft", "effect.jump_boost"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("22653B89-116E-49DC-9B6B-9971489B5BE5"), ResourceLocation.of("minecraft", "effect.weakness"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC"), ResourceLocation.of("minecraft", "effect.health_boost"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("EAE29CF0-701E-4ED6-883A-96F798F3DAB5"), ResourceLocation.of("minecraft", "effect.absorption"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("03C3C89D-7037-4B42-869F-B146BCB64D2E"), ResourceLocation.of("minecraft", "effect.luck"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("CC5AF142-2BD2-4215-B636-2605AED11727"), ResourceLocation.of("minecraft", "effect.unluck"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("6555be74-63b3-41f1-a245-77833b3c2562"), ResourceLocation.of("minecraft", "evil"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ce"), ResourceLocation.of("minecraft", "powder_snow"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D"), ResourceLocation.of("minecraft", "sprinting"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0"), ResourceLocation.of("minecraft", "attacking"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667"), ResourceLocation.of("minecraft", "baby"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F"), ResourceLocation.of("minecraft", "covered"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("9e362924-01de-4ddd-a2b2-d0f7a405a174"), ResourceLocation.of("minecraft", "suffocating"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), ResourceLocation.of("minecraft", "drinking"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836"), ResourceLocation.of("minecraft", "baby"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718"), ResourceLocation.of("minecraft", "attacking"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), ResourceLocation.of("minecraft", "armor.boots"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), ResourceLocation.of("minecraft", "armor.leggings"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), ResourceLocation.of("minecraft", "armor.chestplate"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"), ResourceLocation.of("minecraft", "armor.helmet"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("C1C72771-8B8E-BA4A-ACE0-81A93C8928B2"), ResourceLocation.of("minecraft", "armor.body"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("b572ecd2-ac0c-4071-abde-9594af072a37"), ResourceLocation.of("minecraft", "enchantment.fire_protection"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("40a9968f-5c66-4e2f-b7f4-2ec2f4b3e450"), ResourceLocation.of("minecraft", "enchantment.blast_protection"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("07a65791-f64d-4e79-86c7-f83932f007ec"), ResourceLocation.of("minecraft", "enchantment.respiration"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("60b1b7db-fffd-4ad0-817c-d6c6a93d8a45"), ResourceLocation.of("minecraft", "enchantment.aqua_affinity"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("11dc269a-4476-46c0-aff3-9e17d7eb6801"), ResourceLocation.of("minecraft", "enchantment.depth_strider"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038"), ResourceLocation.of("minecraft", "enchantment.soul_speed"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("b9716dbd-50df-4080-850e-70347d24e687"), ResourceLocation.of("minecraft", "enchantment.soul_speed"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("92437d00-c3a7-4f2e-8f6c-1f21585d5dd0"), ResourceLocation.of("minecraft", "enchantment.swift_sneak"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("5d3d087b-debe-4037-b53e-d84f3ff51f17"), ResourceLocation.of("minecraft", "enchantment.sweeping_edge"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("3ceb37c0-db62-46b5-bd02-785457b01d96"), ResourceLocation.of("minecraft", "enchantment.efficiency"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"), ResourceLocation.of("minecraft", "base_attack_damage"));
        UUID_TO_RESOURCE_LOCATION.put(UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3"), ResourceLocation.of("minecraft", "base_attack_speed"));

        RESOURCE_LOCATION_TO_UUID = UUID_TO_RESOURCE_LOCATION.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a));

        NAME_TO_RESOURCE_LOCATION.put("Random spawn bonus", ResourceLocation.of("minecraft", "random_spawn_bonus"));
        NAME_TO_RESOURCE_LOCATION.put("Random zombie-spawn bonus", ResourceLocation.of("minecraft", "zombie_random_spawn_bonus"));
        NAME_TO_RESOURCE_LOCATION.put("Leader zombie bonus", ResourceLocation.of("minecraft", "leader_zombie_bonus"));
        NAME_TO_RESOURCE_LOCATION.put("Zombie reinforcement callee charge", ResourceLocation.of("minecraft", "reinforcement_callee_charge"));
        NAME_TO_RESOURCE_LOCATION.put("Zombie reinforcement caller charge", ResourceLocation.of("minecraft", "reinforcement_caller_charge"));

        RESOURCE_LOCATION_TO_NAME = NAME_TO_RESOURCE_LOCATION.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (a, b) -> a));
    }

    public static @NotNull ResourceLocation getResourceLocation(@NotNull UUID uuid, @Nullable String name) {
        var location = UUID_TO_RESOURCE_LOCATION.get(uuid);
        if (location != null) {
            return location;
        }

        if (name != null) {
            location = NAME_TO_RESOURCE_LOCATION.get(name);
            if (location != null) {
                return location;
            }
        }

        return ResourceLocation.of("minecraft", uuid.toString().toLowerCase(Locale.ROOT));
    }

    public static @NotNull Pair<@NotNull UUID, @NotNull String> downgradeResourceLocation(@NotNull ResourceLocation location) {
        var uuid = RESOURCE_LOCATION_TO_UUID.get(location);
        if (uuid != null) {
            return Pair.of(uuid, location.path());
        }

        var name = RESOURCE_LOCATION_TO_NAME.get(location);
        if (name != null) {
            return Pair.of(UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)), name);
        }

        return Pair.of(UUID.nameUUIDFromBytes(location.toString().getBytes(StandardCharsets.UTF_8)), location.toString());
    }
}
