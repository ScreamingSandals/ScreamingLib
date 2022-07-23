package org.screamingsandals.lib.block.tags;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@LimitedVersionSupport(">= 1.13")
@ApiStatus.Internal
public class ModernBlockTagBackPorts {
    @Nullable
    public static List<@NotNull String> getPortedTags(@NotNull BlockTypeHolder blockType, @NotNull Predicate<@NotNull String> nativeTagChecker) {
        if (!Server.isVersion(1, 13)) {
            /* This file doesn't support legacy version backports (these are supported by Bukkit-specific implementation, because Bukkit is the only legacy platform) */
            return List.of();
        }

        /*
        Only block tag additions and renames are backported!!! (And removals are ported to newer versions if it makes sense)

        Modifying existing tag may lead into unexpected behaviour!
        (the tag is updated only in slib, so the plugin would have different tag then the game logic)

        Also tags that are connected only with the new features and their usage doesn't make any sense in older versions are not backported.

        Use Helper to check tags here! BlockTypeHolder#hasTag doesn't have any ported tags yet!
         */
        
        var helper = new TagPortHelper(nativeTagChecker);

        if (!Server.isVersion(1, 14)) {
            if (blockType.is("sign")) {
                helper.port("standing_signs");
                helper.port("signs");
            }
            if (blockType.is("wall_sign")) {
                helper.port("wall_signs");
                helper.port("signs");
            }
            if (blockType.is(
                    "dandelion",
                    "poppy",
                    "blue_orchid",
                    "allium",
                    "azure_bluet",
                    "red_tulip",
                    "orange_tulip",
                    "white_tulip",
                    "pink_tulip",
                    "oxeye_daisy")) {
                helper.port("small_flowers");
            }
            if (blockType.is(
                    "red_bed",
                    "black_bed",
                    "blue_bed",
                    "brown_bed",
                    "cyan_bed",
                    "gray_bed",
                    "green_bed",
                    "light_blue_bed",
                    "light_gray_bed",
                    "lime_bed",
                    "magenta_bed",
                    "orange_bed",
                    "pink_bed",
                    "purple_bed",
                    "white_bed",
                    "yellow_bed")) {
                helper.port("beds");
            }
            if (blockType.is(
                    "oak_fence",
                    "acacia_fence",
                    "dark_oak_fence",
                    "spruce_fence",
                    "birch_fence",
                    "jungle_fence",
                    "nether_brick_fence")) {
                helper.port("fences");
            }
            if (blockType.is(
                    "barrier",
                    "bedrock",
                    "end_portal",
                    "end_portal_frame",
                    "end_gateway",
                    "command_block",
                    "repeating_command_block",
                    "chain_command_block",
                    "structure_block",
                    "moving_piston",
                    "obsidian",
                    "end_stone",
                    "iron_bars")) {
                helper.port("dragon_immune");
            }
            if (blockType.is(
                    "barrier",
                    "bedrock",
                    "end_portal",
                    "end_portal_frame",
                    "end_gateway",
                    "command_block",
                    "repeating_command_block",
                    "chain_command_block",
                    "structure_block",
                    "moving_piston"
            )) {
                helper.port("wither_immune");
            }
        }

        if (!Server.isVersion(1, 15)) {
            if (blockType.is(
                    "beetroots",
                    "carrots",
                    "potatoes",
                    "wheat",
                    "melon_stem",
                    "pumpkin_stem"
            )) {
                helper.port("crops");
            }
            if (blockType.is("sunflower", "lilac", "peony", "rose_bush")) {
                helper.port("tall_flowers");
            }
            if (helper.hasTag("small_flowers", "tall_flowers")) {
                helper.port("flowers");
            }
            if (blockType.is(
                    "shulker_box",
                    "black_shulker_box",
                    "blue_shulker_box",
                    "brown_shulker_box",
                    "cyan_shulker_box",
                    "gray_shulker_box",
                    "green_shulker_box",
                    "light_blue_shulker_box",
                    "light_gray_shulker_box",
                    "lime_shulker_box",
                    "magenta_shulker_box",
                    "orange_shulker_box",
                    "pink_shulker_box",
                    "purple_shulker_box",
                    "red_shulker_box",
                    "white_shulker_box",
                    "yellow_shulker_box"
            )) {
                helper.port("shulker_boxes");
            }
            if (blockType.is("nether_portal", "end_portal", "end_gateway")) {
                helper.port("portals");
            }
        } else {
            // dirt_like has been removed in 1.15 and readded in 1.17 as dirt
            // NOTE: remove this mapping if it causes collision in future
            if (blockType.is("dirt", "grass_block", "podzol", "coarse_dirt", "mycelium")) {
                helper.port("dirt_like");
            }
        }

        if (!Server.isVersion(1, 16)) {
            if (blockType.is("fire")) {
                helper.port("fire");
            }
            if (blockType.is("nether_wart_block")) {
                helper.port("wart_blocks");
            }
            if (blockType.is("emerald_block", "diamond_block", "gold_block", "iron_block")) {
                helper.port("beacon_base_blocks");
            }
            if (blockType.is("soul_sand")) {
                helper.port("wither_summon_base_blocks");
            }
            if (blockType.is("ladder", "vine", "scaffolding")) {
                helper.port("climbable");
            }
            if (blockType.is("gold_ore")) {
                helper.port("gold_ores");
            }
            if (helper.hasTag("dark_oak_logs", "oak_logs", "acacia_logs", "birch_logs", "jungle_logs", "spruce_logs")) {
                helper.port("logs_that_burn");
            }
            if (blockType.is("campfire")) {
                helper.port("campfires");
            }
            if (blockType.is("stone_pressure_plate")) {
                helper.port("stone_pressure_plates");
            }
            if (blockType.is("light_weighted_pressure_plate", "heavy_weighted_pressure_plate")
                    || helper.hasTag("stone_pressure_plates", "wooden_pressure_plates")) {
                helper.port("pressure_plates");
            }
            if (blockType.is("torch", "redstone_torch", "tripwire") || helper.hasTag("signs", "banners", "pressure_plates")) {
                helper.port("wall_post_override");
            }
            if (helper.hasTag("rails")) {
                helper.port("prevent_mob_spawning_inside");
            }
            if (blockType.is(
                    "acacia_fence_gate",
                    "birch_fence_gate",
                    "dark_oak_fence_gate",
                    "jungle_fence_gate",
                    "oak_fence_gate",
                    "spruce_fence_gate"
            )) {
                helper.port("fence_gates");
                helper.port("unstable_bottom_center");
            }
            if (blockType.is("netherrack", "magma_block")) {
                helper.port("infiniburn_overworld");
                helper.port("infiniburn_end");
                helper.port("infiniburn_nether");
            }
            if (blockType.is("bedrock")) {
                helper.port("infiniburn_end");
            }
        }

        if (!Server.isVersion(1, 16, 2)) {
            if (blockType.is("mycelium", "podzol")) {
                helper.port("mushroom_grow_block");
            }
            if (blockType.is("netherrack", "basalt", "blackstone")) {
                helper.port("base_stone_nether");
            }
            if (blockType.is("stone", "granite", "diorite", "andesite")) {
                helper.port("base_stone_overworld");
            }
        }

        if (!Server.isVersion(1, 17)) {
            if (blockType.is("cauldron")) {
                helper.port("cauldrons");
            }
            if (blockType.is("snow")) {
                helper.port("inside_step_sound_blocks");
            }
            if (blockType.is("stone", "granite", "diorite", "andesite")) {
                helper.port("stone_ore_replaceables");
            }
            if (blockType.is("diamond_ore")) {
                helper.port("diamond_ores");
            }
            if (blockType.is("iron_ore")) {
                helper.port("iron_ores");
            }
            if (blockType.is("lapis_ore")) {
                helper.port("lapis_ores");
            }
            if (blockType.is("redstone_ore")) {
                helper.port("redstone_ores");
            }
            if (blockType.is("coal_ore")) {
                helper.port("coal_ores");
            }
            if (blockType.is("emerald_ore")) {
                helper.port("emerald_ores");
            }
            if (blockType.is("dirt", "grass_block", "podzol", "coarse_dirt", "mycelium")) {
                helper.port("dirt");
            }
            if (blockType.is("snow", "snow_block")) {
                helper.port("snow");
            }
            if (blockType.is("bedrock", "spawner", "chest", "end_portal_frame")) {
                helper.port("features_cannot_replace");
            }

            /*

            TODO: Following tags are not currently implemented, because there's no easy way to get mining level and preferred tool in (<1.17) without shitload of NMS

            mineable/axe
            mineable/hoe
            mineable/pickaxe
            mineable/shovel
            needs_diamond_tool
            needs_iron_tool
            needs_stone_tool

             */
        }

        if (!Server.isVersion(1, 18)) {
            if (blockType.is(
                    "terracotta",
                    "white_terracotta",
                    "orange_terracotta",
                    "magenta_terracotta",
                    "light_blue_terracotta",
                    "yellow_terracotta",
                    "lime_terracotta",
                    "pink_terracotta",
                    "gray_terracotta",
                    "light_gray_terracotta",
                    "cyan_terracotta",
                    "purple_terracotta",
                    "blue_terracotta",
                    "brown_terracotta",
                    "green_terracotta",
                    "red_terracotta",
                    "black_terracotta"
            )) {
                helper.port("terracotta");
            }
            if (helper.hasTag("features_cannot_replace", "leaves", "logs")) {
                helper.port("lava_pool_stone_cannot_replace");
            }
            if (blockType.is("grass_block")) {
                helper.port("animals_spawnable_on");
            }
            if (blockType.is("clay")) {
                helper.port("axolotls_spawnable_on");
            }
            if (blockType.is("grass_block", "snow", "snow_block", "podzol", "coarse_dirt")) {
                helper.port("foxes_spawnable_on");
            }
            if (blockType.is("stone", "snow", "snow_block", "packed_ice", "gravel")) {
                helper.port("goats_spawnable_on");
            }
            if (blockType.is("mycelium")) {
                helper.port("mooshrooms_spawnable_on");
            }
            if (blockType.is("grass_block", "air") || helper.hasTag("leaves", "logs")) {
                helper.port("parrots_spawnable_on");
            }
            if (blockType.is("ice")) {
                helper.port("polar_bears_spawnable_on_in_frozen_ocean");
            }
            if (blockType.is("grass_block", "snow", "snow_block", "sand")) {
                helper.port("rabbits_spawnable_on");
            }
            if (blockType.is("grass_block", "snow", "snow_block")) {
                helper.port("wolves_spawnable_on");
            }
            if (blockType.is("snow_block", "powder_snow") || helper.hasTag("dirt", "sand", "terracotta")) {
                helper.port("azalea_grows_on");
            }
            if (blockType.is("red_sand", "snow_block", "powder_snow", "clay", "gravel", "sand")
                    || helper.hasTag("terracotta", "base_stone_overworld", "dirt")) {
                helper.port("azalea_root_replaceable");
            }
            if (blockType.is(
                    "grass",
                    "fern",
                    "dead_bush",
                    "vine",
                    "glow_lichen",
                    "sunflower",
                    "lilac",
                    "rose_bush",
                    "peony",
                    "tall_grass",
                    "large_fern",
                    "hanging_roots"
            )) {
                helper.port("replaceable_plants");
            }
        }

        if (!Server.isVersion(1, 18, 2)) {
            if (blockType.is("sweet_berry_bush", "cobweb") || helper.hasTag("climbable")) {
                helper.port("fall_damage_resetting");
            }
        }

        if (!Server.isVersion(1, 19)) {
            if (blockType.is("light") || helper.hasTag("fire")) {
                helper.port("dragon_transparent");
            }
            if (helper.hasTag("carpets")) {
                helper.port("wool_carpets");
            }
            if (helper.hasTag("sand", "terracotta", "dirt")) {
                helper.port("dead_bush_may_place_on");
            }
            if (helper.hasTag("logs", "leaves", "wart_blocks")) {
                helper.port("completes_find_tree_tutorial");
            }
            if (blockType.is("soul_sand", "soul_soil") || helper.hasTag("base_stone_overworld", "base_stone_nether", "dirt", "nylium", "wart_blocks")) {
                helper.port("nether_carver_replaceables");
            }
            if (blockType.is(
                    "water",
                    "gravel",
                    "sandstone",
                    "red_sandstone",
                    "calcite",
                    "snow",
                    "packed_ice",
                    "raw_iron_block",
                    "raw_copper_block"
            ) || helper.hasTag(
                    "base_stone_overworld",
                    "dirt",
                    "sand",
                    "terracotta",
                    "iron_ores",
                    "copper_ores"
            )) {
                helper.port("overworld_carver_replaceables");
            }
            if (blockType.is("acacia_log", "birch_log", "oak_log", "jungle_log", "spruce_log", "dark_oak_log")) {
                helper.port("overworld_natural_logs");
            }
            if (blockType.is("honey_block", "soul_sand")) {
                helper.port("snow_layer_can_survive_on");
            }
            if (blockType.is("ice", "packed_ice", "barrier")) {
                helper.port("snow_layer_cannot_survive_on");
            }
            if (helper.hasTag("wool", "wool_carpets")) {
                helper.port("dampens_vibrations");
            }
            if (helper.hasTag("polar_bears_spawnable_on_in_frozen_ocean")) {
                helper.port("polar_bears_spawnable_on_alternate");
            }
        } else {
            // Older version of wool_carpets was carpets
            // NOTE: remove this mapping if it causes collision in future
            if (helper.hasTag("wool_carpets")) {
                helper.port("carpets");
            }

            // Older version of polar_bears_spawnable_on_alternate was polar_bears_spawnable_on_in_frozen_ocean
            // NOTE: remove this mapping if it causes collision in future
            if (helper.hasTag("polar_bears_spawnable_on_alternate")) {
                helper.port("polar_bears_spawnable_on_in_frozen_ocean");
            }
        }
        return helper.getPorts();
    }
}
