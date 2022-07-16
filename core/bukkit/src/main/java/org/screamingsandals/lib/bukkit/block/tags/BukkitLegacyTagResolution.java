package org.screamingsandals.lib.bukkit.block.tags;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BukkitLegacyTagResolution {
    @NotNull
    private static List<String> constructTags(@NotNull Material material) {
        var list = new ArrayList<String>();
        /* tags related to features released after 1.12.2 are not backported
         unless the tag contains older block and can be useful in older versions or for better compatibility (beds, *_ores, etc.) */

        // this list is based on the MinecraftWiki page: https://minecraft.fandom.com/wiki/Tag
        switch (material.name()) {
            // mineable/axe
            // mineable/hoe
            // mineable/pickaxe
            // mineable/shovel
            // acacia_logs
            // animals_spawnable_on
            case "GRASS":
                list.add("animals_spawnable_on");
                list.add("dirt"); // also part of dirt
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on
                break;

            // anvil
            case "ANVIL":
                list.add("anvil");
                break;
            // banners
            case "STANDING_BANNER":
            case "WALL_BANNER":
                list.add("banners");
                break;
            // base_stone_nether
            case "NETHERRACK":
                list.add("base_stone_nether");
                list.add("infiniburn_overworld"); // also part of infiniburn_overworld
                list.add("infiniburn_end"); // also part of infiniburn_end
                list.add("infiniburn_nether"); // also part of infiniburn_nether
                break;
            // base_stone_overworld
            case "STONE":
                list.add("base_stone_overworld");
                break;
            // beacon_base_blocks
            case "EMERALD_BLOCK":
            case "DIAMOND_BLOCK":
            case "IRON_BLOCK":
            case "GOLD_BLOCK":
                list.add("beacon_base_blocks");
                break;
            // beds
            case "BED_BLOCK":
                list.add("beds");
                break;
            // birch_logs
            // buttons (includes wooden_buttons)
            case "STONE_BUTTON":
                list.add("buttons");
                break;
            // cauldrons
            case "CAULDRON":
                list.add("cauldrons");
                break;
            // cave_vines
            case "VINE":
                list.add("vine");
                // vine is also climbable
            // climbable
            case "LADDER":
                list.add("climbable");
                list.add("fall_damage_resetting"); // also part of fall_damage_resetting
                break;
            // coal_ores
            case "COAL_ORE":
                list.add("coal_ores");
                break;
            // completes_find_tree_tutorial (includes logs, leaves)
            // crops
            // dark_oak_logs
            // dead_bush_may_place_on (includes sand, terracotta and dirt)
            // diamond_ores
            case "DIAMOND_ORE":
                list.add("diamond_ores");
                break;
            // dirt
            case "DIRT":
                list.add("dirt");
                break;
            // doors (includes wooden_doors)
            case "IRON_DOOR_BLOCK":
                list.add("doors");
                break;
            // dragon_immune
            // dragon_transparent
            // emerald_ores
            case "EMERALD_ORE":
                list.add("emerald_ores");
                break;
            // enderman_holdable (include only blocks that Endermen can hold in legacy versions)
            // fall_damage_resetting (includes climbable)
            case "WEB":
                list.add("fall_damage_resetting");
                break;
            // fence_gates
            case "FENCE_GATE":
            case "SPRUCE_FENCE_GATE":
            case "BIRCH_FENCE_GATE":
            case "JUNGLE_FENCE_GATE":
            case "DARK_OAK_FENCE_GATE":
            case "ACACIA_FENCE_GATE":
                list.add("fence_gates");
                break;
            // fences (includes wooden_fences)
            case "NETHER_FENCE":
                list.add("fences");
                break;
            // fire
            case "FIRE":
                list.add("fire");
                break;
            // flower_pots
            case "FLOWER_POT":
                list.add("flower_pots");
                break;
            // flowers (includes small_flowers and tall_flowers)
            // gold_ores
            case "GOLD_ORE":
                list.add("GOLD_ORE");
                break;
            // ice
            case "ICE":
            case "PACKED_ICE":
            case "FROSTED_ICE":
                list.add("ice");
                break;
            // impermeable
            case "GLASS":
            case "STAINED_GLASS":
                list.add("impermeable");
                break;
            // infiniburn_end (includes infiniburn_overworld)
            case "BEDROCK":
                list.add("infiniburn_end");
                break;
            // infiniburn_nether (includes infiniburn_overworld)
            // infiniburn_overworld
            case "MAGMA":
                list.add("infiniburn_overworld");
                list.add("infiniburn_end"); // also part of infiniburn_end
                list.add("infiniburn_nether"); // also part of infiniburn_nether
                break;
            // inside_step_sound_blocks
            case "SNOW":
                list.add("inside_step_sound_blocks");
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on
                break;
            // iron_ores
            case "IRON_ORE":
                list.add("iron_ores");
                break;
            // jungle_logs
            // lapis_ores
            case "LAPIS_ORE":
                list.add("lapis_ore");
                break;
            // leaves
            case "LEAVES":
            case "LEAVES_2":
                list.add("leaves");
                list.add("completes_find_tree_tutorial"); // also part of completes_find_tree_tutorial
                list.add("parrots_spawnable_on"); // also part of parrots_spawnable_on
                break;
            // logs (includes logs_that_burn)
            // logs_that_burn (includes *_logs)
            case "LOG":
            case "LOG_2":
                list.add("logs");
                list.add("parrots_spawnable_on"); // also part of parrots_spawnable_on
                break;
            // mooshrooms_spawnable_on
            case "MYCEL":
                list.add("mooshrooms_spawnable_on");
                list.add("dirt"); // also part of dirt
                list.add("mushroom_grow_block"); // also part of mushroom_grow_block
                break;
            // TODO: add dirt:3 to mushroom_grow_block
            // TODO: needs_diamond_tool
            // TODO: needs_iron_tool
            // TODO: needs_stone_tool
            // TODO: add log:0 to oak_logs
            // TODO: add log:[0-3] and log2:[0-1] to overworld_natural_logs
            // parrots_spawnable_on (includes leaves and logs)
            // planks
            case "WOOD":
                list.add("planks");
                break;
            // portals
            case "PORTAL":
            case "ENDER_PORTAL":
            case "END_GATEWAY":
                list.add("portals");
                break;
            // pressure_plates (includes wooden_pressure_plates adn stone_pressure_plates)
            // rabbits_spawnable_on
            // rails
            case "POWERED_RAIL":
            case "DETECTOR_RAIL":
            case "RAILS":
            case "ACTIVATOR_RAIL":
                list.add("rails");
                break;
            // redstone_ores
            case "REDSTONE_ORE":
            case "GLOWING_REDSTONE_ORE":
                list.add("redstone_ores");
                break;
            // sand
            case "SAND":
                list.add("sand");
                list.add("rabbits_spawnable_on"); // also part of rabbits_spawnable_on // TODO: exclude red_sand (sand:1)
                break;
            // saplings
            case "SAPLING":
                list.add("saplings");
                break;
            // shulker_boxes
            case "WHITE_SHULKER_BOX":
            case "ORANGE_SHULKER_BOX":
            case "MAGENTA_SHULKER_BOX":
            case "LIGHT_BLUE_SHULKER_BOX":
            case "YELLOW_SHULKER_BOX":
            case "LIME_SHULKER_BOX":
            case "PINK_SHULKER_BOX":
            case "GRAY_SHULKER_BOX":
            case "SILVER_SHULKER_BOX":
            case "CYAN_SHULKER_BOX":
            case "PURPLE_SHULKER_BOX":
            case "BLUE_SHULKER_BOX":
            case "BROWN_SHULKER_BOX":
            case "GREEN_SHULKER_BOX":
            case "RED_SHULKER_BOX":
            case "BLACK_SHULKER_BOX":
                list.add("shulker_boxes");
                break;
            // signs (includes standing_signs and wall_signs)
            // slabs (includes wooden_slabs)
            case "DOUBLE_STEP":
            case "STEP":
            case "DOUBLE_STONE_SLAB2":
            case "STONE_SLAB2":
            case "PURPUR_DOUBLE_SLAB":
            case "PURPUR_SLAB":
                list.add("slabs");
                break;
            // small_flowers
            case "YELLOW_FLOWER":
            case "RED_FLOWER":
                list.add("small_flowers");
                list.add("flowers"); // also part of flowers
                break;
            // snow
            // spruce_logs
            // stairs
            // standing_signs
            case "SIGN_POST":
                list.add("standing_signs");
                list.add("signs"); // also part of signs
                break;
            // stone_bricks
            // stone_pressure_plates
            // tall_flowers
            // terracotta
            // trapdoors (includes wooden_trapdoors)
            // valid_spawn
            // walls
            // wall_post_override
            // wall_signs
            case "WALL_SIGN":
                list.add("wall_signs");
                list.add("signs"); // also part of signs
                break;
            // wart_blocks
            // wither_immune
            // wolves_spawnable_on
            // wooden_buttons
            // wooden_doors
            case "WOODEN_DOOR":
            case "SPRUCE_DOOR":
            case "BIRCH_DOOR":
            case "JUNGLE_DOOR":
            case "ACACIA_DOOR":
            case "DARK_OAK_DOOR":
                list.add("wooden_doors");
                list.add("doors"); // also part of doors
                break;
            // wooden_fences
            case "FENCE":
            case "SPRUCE_FENCE":
            case "BIRCH_FENCE":
            case "JUNGLE_FENCE":
            case "DARK_OAK_FENCE":
            case "ACACIA_FENCE":
                list.add("wooden_fences");
                list.add("fences"); // also part of fences
                break;
            // wooden_pressure_plates
            // wooden_slabs
            case "WOOD_DOUBLE_STEP":
            case "WOOD_STEP":
                list.add("wooden_slabs");
                list.add("slabs"); // also part of slabs
                break;
            // wooden_stairs
            // wooden_trapdoors
            // wool_carpets (old name carpets)
            // wool
        }
        return list;
    }
}
