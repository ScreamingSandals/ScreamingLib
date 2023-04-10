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

package org.screamingsandals.lib.bukkit.particle;

import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockType;
import org.screamingsandals.lib.item.ItemStack;
import org.screamingsandals.lib.particle.*;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;

public class BukkitParticleType1_9 extends BasicWrapper<Particle> implements ParticleType {
    public BukkitParticleType1_9(@NotNull Particle wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String platformName() {
        return wrappedObject.name();
    }

    @Override
    public @Nullable Class<? extends ParticleData> expectedDataClass() {
        var dataType = wrappedObject.getDataType();
        if (dataType != Void.class) {
            switch (dataType.getSimpleName()) {
                case "MaterialData":
                case "BlockData":
                    return BlockType.class;
                case "ItemStack":
                    return ItemStack.class;
                case "DustOptions":
                    return DustOptions.class;
                case "DustTransition":
                    return DustTransition.class;
            }
            // TODO: Integer, Float, Vibration
        }
        return null;
    }

    @Override
    public boolean is(@Nullable Object object) {
        if (object instanceof Particle || object instanceof ParticleType) {
            return equals(object);
        }
        return equals(ParticleType.ofNullable(object));
    }

    @Override
    public boolean is(@Nullable Object @NotNull... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @Override
    public @NotNull ResourceLocation location() {
        return ResourceLocation.of(convertPath(wrappedObject));
    }

    public static @NotNull String convertPath(@NotNull Particle particle) {
        switch (particle.name()) {
            //<editor-fold desc=" Enum constants -> 1.13+ flattening names" defaultstate="collapsed">
            // @formatter:off

            case "EXPLOSION_NORMAL": return "poof";
            case "EXPLOSION_LARGE": return "explosion";
            case "EXPLOSION_HUGE": return "explosion_emitter";
            case "FIREWORKS_SPARK": return "firework";
            case "WATER_BUBBLE": return "bubble";
            case "WATER_SPLASH": return "splash";
            case "WATER_WAKE": return "fishing";
            case "SUSPENDED": return "underwater";
            //case "CRIT": return "crit";
            case "CRIT_MAGIC": return "enchanted_hit";
            case "SMOKE_NORMAL": return "smoke";
            case "SMOKE_LARGE": return "large_smoke";
            case "SPELL": return "effect";
            case "SPELL_INSTANT": return "instant_effect";
            case "SPELL_MOB": return "entity_effect";
            case "SPELL_MOB_AMBIENT": return "ambient_entity_effect";
            case "SPELL_WITCH": return "witch";
            case "DRIP_WATER": return "dripping_water";
            case "DRIP_LAVA": return "dripping_lava";
            case "VILLAGER_ANGRY": return "angry_villager";
            case "VILLAGER_HAPPY": return "happy_villager";
            case "TOWN_AURA": return "mycelium";
            //case "NOTE": return "note";
            //case "PORTAL": return "portal";
            case "ENCHANTMENT_TABLE": return "enchant";
            //case "FLAME": return "flame";
            //case "LAVA": return "lava";
            //case "CLOUD": return "cloud";
            case "REDSTONE": return "dust";
            case "SNOWBALL": return "item_snowball";
            case "SLIME": return "item_slime";
            //case "HEART": return "heart";
            case "ITEM_CRACK": return "item";
            case "BLOCK_CRACK": return "block";
            case "WATER_DROP": return "rain";
            case "MOB_APPEARANCE": return "elder_guardian";
            //case "DRAGON_BREATH": return "dragon_breath";
            //case "END_ROD": return "end_rod";
            //case "DAMAGE_INDICATOR": return "damage_indicator";
            //case "SWEEP_ATTACK": return "sweep_attack";
            //case "FALLING_DUST": return "falling_dust";
            case "TOTEM": return "totem_of_undying";
            //case "SPIT": return "spit";

            // particles added in 1.13+ omitted, they are always the same, with these exceptiosn (marker completely replaced barrier and light):
            case "BARRIER":
            case "LIGHT":
                return "block_marker";
            default:
                return particle.name().toLowerCase(Locale.ROOT);
        }
    }
}
