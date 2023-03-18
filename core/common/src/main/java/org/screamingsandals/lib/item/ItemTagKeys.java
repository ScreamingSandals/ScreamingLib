/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.item;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.nbt.*;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@UtilityClass
public class ItemTagKeys {
    // GENERAL TAGS
    public static final @NotNull TreeInspectorKey<IntTag> DAMAGE = TreeInspectorKey.of(IntTag.class, "Damage");
    public static final @NotNull TreeInspectorKey<ByteTag> UNBREAKABLE = TreeInspectorKey.of(ByteTag.class, "Unbreakable");
    public static final @NotNull TreeInspectorKey<ListTag> CAN_DESTROY = TreeInspectorKey.of(ListTag.class, "CanDestroy");
    @LimitedVersionSupport(">= 1.14")
    public static final @NotNull TreeInspectorKey<IntTag> CUSTOM_MODEL_DATA = TreeInspectorKey.of(IntTag.class, "CustomModelData");

    // ATTRIBUTE MODIFIERS
    public static final @NotNull TreeInspectorKey<ListTag> ATTRIBUTE_MODIFIERS = TreeInspectorKey.of(ListTag.class, "AttributeModifiers");
    @UtilityClass
    public static class AttributeModifiers {
        // tags in each Attribute modifier
        public static final @NotNull TreeInspectorKey<StringTag> ATTRIBUTE_NAME = TreeInspectorKey.of(StringTag.class, "AttributeName");
        public static final @NotNull TreeInspectorKey<StringTag> NAME = TreeInspectorKey.of(StringTag.class, "Name");
        public static final @NotNull TreeInspectorKey<StringTag> SLOT = TreeInspectorKey.of(StringTag.class, "Slot");
        public static final @NotNull TreeInspectorKey<IntTag> OPERATION = TreeInspectorKey.of(IntTag.class, "Operation");
        public static final @NotNull TreeInspectorKey<DoubleTag> AMOUNT = TreeInspectorKey.of(DoubleTag.class, "Amount");
        public static final @NotNull TreeInspectorKey<IntArrayTag> UUID = TreeInspectorKey.of(IntArrayTag.class, "UUID");
    }

    // BLOCK TAGS
    public static final @NotNull TreeInspectorKey<ListTag> CAN_PLACE_ON = TreeInspectorKey.of(ListTag.class, "CanPlaceOn");
    public static final @NotNull TreeInspectorKey<CompoundTag> BLOCK_ENTITY_TAG = TreeInspectorKey.of(CompoundTag.class, "BlockEntityTag");
    @LimitedVersionSupport(">= 1.14")
    public static final @NotNull TreeInspectorKey<CompoundTag> BLOCK_STATE_TAG = TreeInspectorKey.of(CompoundTag.class, "BlockStateTag");

    // DISPLAY TAGS
    public static final @NotNull TreeInspectorKey<IntTag> DISPLAY_COLOR = TreeInspectorKey.of(IntTag.class, "display", "color");
    public static final @NotNull TreeInspectorKey<StringTag> DISPLAY_NAME = TreeInspectorKey.of(StringTag.class, "display", "Name");
    @LimitedVersionSupport("<= 1.12.2")
    public static final @NotNull TreeInspectorKey<StringTag> DISPLAY_LOC_NAME = TreeInspectorKey.of(StringTag.class, "display", "LocName");
    public static final @NotNull TreeInspectorKey<ListTag> DISPLAY_LORE = TreeInspectorKey.of(ListTag.class, "display", "lore");
    public static final @NotNull TreeInspectorKey<IntTag> HIDE_FLAGS = TreeInspectorKey.of(IntTag.class, "HideFlags");

    // ENCHANTMENTS
    public static final @NotNull TreeInspectorKey<CompoundTag> ENCHANTMENTS = TreeInspectorKey.of(CompoundTag.class, () -> {
        if (Server.isVersion(1, 13)) {
            return new String[] {"Enchantments"};
        } else {
            return new String[] {"ench"};
        }
    });
    @UtilityClass
    public static class Enchantments {
        // tags in each enchantment
        public static final @NotNull TreeInspectorKey<StringTag> ID = TreeInspectorKey.of(StringTag.class, "id");
        public static final @NotNull TreeInspectorKey<ShortTag> LVL = TreeInspectorKey.of(ShortTag.class, "lvl");
    }
    public static final @NotNull TreeInspectorKey<CompoundTag> STORED_ENCHANTMENTS = TreeInspectorKey.of(CompoundTag.class, "StoredEnchantments");
    // tags in each stored enchantment are identical to enchantment
    public static final @NotNull TreeInspectorKey<IntTag> REPAIR_COST = TreeInspectorKey.of(IntTag.class, "RepairCost");

    // POTION
    public static final @NotNull TreeInspectorKey<ListTag> CUSTOM_POTION_EFFECTS = TreeInspectorKey.of(ListTag.class, "CustomPotionEffects");
    @UtilityClass
    public static class CustomPotionEffect {
        // tags in each custom potion effect
        public static final @NotNull TreeInspectorKey<ByteTag> ID = TreeInspectorKey.of(ByteTag.class, "Id");
        public static final @NotNull TreeInspectorKey<ByteTag> AMPLIFIER = TreeInspectorKey.of(ByteTag.class, "Amplifier");
        public static final @NotNull TreeInspectorKey<IntTag> DURATION = TreeInspectorKey.of(IntTag.class, "Duration");
        public static final @NotNull TreeInspectorKey<ByteTag> SHOW_PARTICLES = TreeInspectorKey.of(ByteTag.class, "ShowParticles");
        public static final @NotNull TreeInspectorKey<ByteTag> SHOW_ICON = TreeInspectorKey.of(ByteTag.class, "ShowIcon");
    }
    public static final @NotNull TreeInspectorKey<StringTag> POTION = TreeInspectorKey.of(StringTag.class, "Potion");
    public static final @NotNull TreeInspectorKey<IntTag> CUSTOM_POTION_COLOR = TreeInspectorKey.of(IntTag.class, "CustomPotionColor");

    // tags specific for ARMOR STANDS, SPAWN EGGS, ITEM FRAMES and BUCKETS OF AQUATIC MOB
    public static final @NotNull TreeInspectorKey<CompoundTag> ENTITY_TAG = TreeInspectorKey.of(CompoundTag.class, "EntityTag");

    // tags of BUCKETS OF AQUATIC MOB
    public static final @NotNull TreeInspectorKey<IntTag> BUCKET_VARIANT_TAG = TreeInspectorKey.of(IntTag.class, "BucketVariantTag");

    // BOOK AND QUILLS has only pages (this tag is also part of written books)

    // WRITTEN BOOKS
    public static final @NotNull TreeInspectorKey<CompoundTag> FILTERED_PAGES = TreeInspectorKey.of(CompoundTag.class, "filtered_pages");
    public static final @NotNull TreeInspectorKey<StringTag> FILTERED_TITLE = TreeInspectorKey.of(StringTag.class, "filtered_title");
    public static final @NotNull TreeInspectorKey<ByteTag> RESOLVED = TreeInspectorKey.of(ByteTag.class, "resolved");
    public static final @NotNull TreeInspectorKey<IntTag> GENERATION = TreeInspectorKey.of(IntTag.class, "generation");
    public static final @NotNull TreeInspectorKey<StringTag> AUTHOR = TreeInspectorKey.of(StringTag.class, "author");
    public static final @NotNull TreeInspectorKey<StringTag> TITLE = TreeInspectorKey.of(StringTag.class, "title");
    public static final @NotNull TreeInspectorKey<ListTag> PAGES = TreeInspectorKey.of(ListTag.class, "pages");

    // BUNDLE
    public static final @NotNull TreeInspectorKey<ListTag> ITEMS = TreeInspectorKey.of(ListTag.class, "Items");

    // COMPASSES
    public static final @NotNull TreeInspectorKey<ByteTag> LODESTONE_TRACKED = TreeInspectorKey.of(ByteTag.class, "LodestoneTracked");
    public static final @NotNull TreeInspectorKey<StringTag> LODESTONE_DIMENSION = TreeInspectorKey.of(StringTag.class, "LodestoneDimension");
    public static final @NotNull TreeInspectorKey<IntTag> LODESTONE_POS_X = TreeInspectorKey.of(IntTag.class, "LodestonePos", "X");
    public static final @NotNull TreeInspectorKey<IntTag> LODESTONE_POS_Y = TreeInspectorKey.of(IntTag.class, "LodestonePos", "Y");
    public static final @NotNull TreeInspectorKey<IntTag> LODESTONE_POS_Z = TreeInspectorKey.of(IntTag.class, "LodestonePos", "Z");

    // CROSSBOWS
    public static final @NotNull TreeInspectorKey<ListTag> CHARGED_PROJECTILES = TreeInspectorKey.of(ListTag.class, "ChargedProjectiles");
    public static final @NotNull TreeInspectorKey<ByteTag> CHARGED = TreeInspectorKey.of(ByteTag.class, "Charged");

    // DEBUG STICK
    public static final @NotNull TreeInspectorKey<CompoundTag> DEBUG_PROPERTY = TreeInspectorKey.of(CompoundTag.class, "DebugProperty");

    // FIREWORK ROCKETS
    public static final @NotNull TreeInspectorKey<ListTag> FIREWORKS_EXPLOSIONS = TreeInspectorKey.of(ListTag.class, "Fireworks", "Explosions");
    @UtilityClass
    public static class Explosion {
        public static final @NotNull TreeInspectorKey<IntArrayTag> COLORS = TreeInspectorKey.of(IntArrayTag.class, "Colors");
        public static final @NotNull TreeInspectorKey<IntArrayTag> FADE_COLORS = TreeInspectorKey.of(IntArrayTag.class, "FadeColors");
        public static final @NotNull TreeInspectorKey<ByteTag> FLICKER = TreeInspectorKey.of(ByteTag.class, "Flicker");
        public static final @NotNull TreeInspectorKey<ByteTag> TRAIL = TreeInspectorKey.of(ByteTag.class, "Trail");
        public static final @NotNull TreeInspectorKey<ByteTag> TYPE = TreeInspectorKey.of(ByteTag.class, "Type");
    }
    public static final @NotNull TreeInspectorKey<ListTag> FIREWORKS_FLIGHT = TreeInspectorKey.of(ListTag.class, "Fireworks", "Flight");

    // FIREWORK STAR
    public static final @NotNull TreeInspectorKey<CompoundTag> EXPLOSION = TreeInspectorKey.of(CompoundTag.class, "Explosion");

    // MAPS
    public static final @NotNull TreeInspectorKey<IntTag> MAP = TreeInspectorKey.of(IntTag.class, "map");
    public static final @NotNull TreeInspectorKey<IntTag> MAP_SCALE_DIRECTION = TreeInspectorKey.of(IntTag.class, "map_scale_direction");
    public static final @NotNull TreeInspectorKey<ByteTag> MAP_TO_LOCK = TreeInspectorKey.of(ByteTag.class, "map_to_lock");
    public static final @NotNull TreeInspectorKey<ListTag> DECORATIONS = TreeInspectorKey.of(ListTag.class, "Decorations");
    @UtilityClass
    public static class Decorations {
        // tags in each decoration
        public static final @NotNull TreeInspectorKey<StringTag> ID = TreeInspectorKey.of(StringTag.class, "id");
        public static final @NotNull TreeInspectorKey<ByteTag> TYPE = TreeInspectorKey.of(ByteTag.class, "type");
        public static final @NotNull TreeInspectorKey<DoubleTag> X = TreeInspectorKey.of(DoubleTag.class, "x");
        public static final @NotNull TreeInspectorKey<DoubleTag> Z = TreeInspectorKey.of(DoubleTag.class, "z");
        public static final @NotNull TreeInspectorKey<DoubleTag> ROT = TreeInspectorKey.of(DoubleTag.class, "rot");
    }
    public static final @NotNull TreeInspectorKey<IntTag> DISPLAY_MAP_COLOR = TreeInspectorKey.of(IntTag.class, "display", "MapColor");

    // SKULLS
    public static final @NotNull TreeInspectorKey<StringTag> SKULL_OWNER_STRING = TreeInspectorKey.of(StringTag.class, "SkullOwner");
    public static final @NotNull TreeInspectorKey<IntArrayTag> SKULL_OWNER_ID = TreeInspectorKey.of(IntArrayTag.class, "SkullOwner", "Id");
    public static final @NotNull TreeInspectorKey<StringTag> SKULL_OWNER_NAME = TreeInspectorKey.of(StringTag.class, "SkullOwner", "Name");
    public static final @NotNull TreeInspectorKey<ListTag> SKULL_OWNER_PROPERTIES_TEXTURES = TreeInspectorKey.of(ListTag.class, "SkullOwner", "Properties", "textures");
    @UtilityClass
    public static class SkullTexture {
        // tags in each skull texture
        public static final @NotNull TreeInspectorKey<StringTag> VALUE = TreeInspectorKey.of(StringTag.class, "Value");
        public static final @NotNull TreeInspectorKey<StringTag> SIGNATURE = TreeInspectorKey.of(StringTag.class, "Signature");
    }

    // SUS STEW
    public static final @NotNull TreeInspectorKey<ListTag> EFFECTS = TreeInspectorKey.of(ListTag.class, "Effects");
    @UtilityClass
    public static class Effects {
        public static final @NotNull TreeInspectorKey<ByteTag> EFFECT_ID = TreeInspectorKey.of(ByteTag.class, "EffectId");
        public static final @NotNull TreeInspectorKey<IntTag> EFFECT_DURATION = TreeInspectorKey.of(IntTag.class, "EffectDuration");
    }

    public static final @NotNull TreeInspectorKey<CompoundTag> PUBLIC_BUKKIT_VALUES = TreeInspectorKey.of(CompoundTag.class, "PublicBukkitValues");
}
