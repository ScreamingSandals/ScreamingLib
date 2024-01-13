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

package org.screamingsandals.lib.impl.bukkit.particle;

import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.ResourceLocation;
import org.screamingsandals.lib.utils.registry.RegistryItemStream;
import org.screamingsandals.lib.impl.utils.registry.SimpleRegistryItemStream;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class BukkitParticleTypeRegistry1_9 extends BukkitParticleTypeRegistry {
    public BukkitParticleTypeRegistry1_9() {
        specialType(Particle.class, BukkitParticleType1_9::new);
    }

    // TODO: is there any bukkit-like server supporting custom values for this registry?
    @Override
    protected @Nullable ParticleType resolveMappingPlatform(@NotNull ResourceLocation location) {
        if (!"minecraft".equals(location.namespace())) {
            return null;
        }

        var path = location.path();

        String enumName;
        switch (path) {
            //<editor-fold desc="1.13+ flattening names -> Enum constant" defaultstate="collapsed">
            // @formatter:off

            case "poof": enumName = "EXPLOSION_NORMAL"; break;
            case "explosion": enumName = "EXPLOSION_LARGE"; break;
            case "explosion_emitter": enumName = "EXPLOSION_HUGE"; break;
            case "firework": enumName = "FIREWORKS_SPARK"; break;
            case "bubble": enumName = "WATER_BUBBLE"; break;
            case "splash": enumName = "WATER_SPLASH"; break;
            case "fishing": enumName = "WATER_WAKE"; break;
            case "underwater": enumName = "SUSPENDED"; break;
            //case "crit": enumName = "CRIT"; break; - same
            case "enchanted_hit": enumName = "CRIT_MAGIC"; break;
            case "smoke": enumName = "SMOKE_NORMAL"; break;
            case "large_smoke": enumName = "SMOKE_LARGE"; break;
            case "effect": enumName = "SPELL"; break;
            case "instant_effect": enumName = "SPELL_INSTANT"; break;
            case "entity_effect": enumName = "SPELL_MOB"; break;
            case "ambient_entity_effect": enumName = "SPELL_MOB_AMBIENT"; break;
            case "witch": enumName = "SPELL_WITCH"; break;
            case "dripping_water": enumName = "DRIP_WATER"; break;
            case "dripping_lava": enumName = "DRIP_LAVA"; break;
            case "angry_villager": enumName = "VILLAGER_ANGRY"; break;
            case "happy_villager": enumName = "VILLAGER_HAPPY"; break;
            case "mycelium": enumName = "TOWN_AURA"; break;
            //case "note": enumName = "NOTE"; break; - same
            //case "portal": enumName = "PORTAL"; break; - same
            case "enchant": enumName = "ENCHANTMENT_TABLE"; break;
            //case "flame": enumName = "FLAME"; break; - same
            //case "lava": enumName = "LAVA"; break; - same
            //case "cloud": enumName = "CLOUD"; break; - same
            case "dust": enumName = "REDSTONE"; break;
            case "item_snowball": enumName = "SNOWBALL"; break;
            case "item_slime": enumName = "SLIME"; break;
            //case "heart": enumName = "HEART"; break; - same
            case "item": enumName = "ITEM_CRACK"; break;
            case "block": enumName = "BLOCK_CRACK"; break;
            case "rain": enumName = "WATER_DROP"; break;
            case "elder_guardian": enumName = "MOB_APPEARANCE"; break;
            //case "dragon_breath": enumName = "DRAGON_BREATH"; break; - same
            //case "end_rod": enumName = "END_ROD"; break; - same
            //case "damage_indicator": enumName = "DAMAGE_INDICATOR"; break; - same
            //case "sweep_attack": enumName = "SWEEP_ATTACK"; break; - same
            //case "falling_dust": enumName = "FALLING_DUST"; break; - same
            case "totem_of_undying": enumName = "TOTEM"; break;
            //case "spit": enumName = "SPIT"; break; - same

            // particles added in 1.13+ omitted, they are always the same, with one exception (marker completely replaced barrier and light):
            case "block_marker":
                if (Version.isVersion(1, 18)) {
                    enumName = "BLOCK_MARKER";
                } else {
                    enumName = "BARRIER";
                }
                break;
            default:
                enumName = path.toUpperCase(Locale.ROOT);
        }
        try {
            var value = Particle.valueOf(enumName);
            return new BukkitParticleType1_9(value);
        } catch (IllegalArgumentException ignored) {
        }

        return null;
    }

    @Override
    protected @NotNull RegistryItemStream<@NotNull ParticleType> getRegistryItemStream0() {
        return new SimpleRegistryItemStream<>(
                () -> Arrays.stream(Particle.values()).filter(particle -> !particle.name().startsWith("LEGACY_")),
                BukkitParticleType1_9::new,
                particleType -> ResourceLocation.of(BukkitParticleType1_9.convertPath(particleType)),
                (particleType, literal) -> BukkitParticleType1_9.convertPath(particleType).contains(literal),
                (particleType, namespace) -> "minecraft".equals(namespace),
                List.of()
        );
    }

}
