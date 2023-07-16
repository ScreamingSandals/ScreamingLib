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

package org.screamingsandals.lib.impl.bukkit;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.ItemBlockIdsRemapper;
import org.screamingsandals.lib.impl.block.BlockRegistry;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlock1_8;
import org.screamingsandals.lib.impl.bukkit.block.BukkitBlockRegistry1_8;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemType1_8;
import org.screamingsandals.lib.impl.bukkit.item.BukkitItemTypeRegistry1_8;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.impl.item.ItemTypeRegistry;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.ResourceLocation;

import java.util.Locale;

@Service
public class BukkitItemBlockIdsRemapper extends ItemBlockIdsRemapper {
    public BukkitItemBlockIdsRemapper(@NotNull ItemTypeRegistry itemTypeMapper, @NotNull BlockRegistry blockTypeMapper) {
        super(itemTypeMapper, blockTypeMapper);

        if (!BukkitFeature.COLORED_BEDS.isSupported()) {
            mappingFlags.add(MappingFlags.NO_COLORED_BEDS);
        }
    }

    @Override
    @OnPostConstruct
    public void doMapping() {
        if (!BukkitFeature.FLATTENING.isSupported()) {
            flatteningLegacyMappingJava();
        }

        super.doMapping();
    }

    private void flatteningLegacyMappingJava() {
        // Legacy remapping

        //<editor-fold desc="Blocks" defaultstate="collapsed">

        // BLOCKS
        // Some block states still don't have correct mapping
        f2l("AIR");

        f2l("STONE");
        f2l("GRANITE", "STONE", 1);
        f2l("POLISHED_GRANITE", "STONE", 2);
        f2l("DIORITE", "STONE", 3);
        f2l("POLISHED_DIORITE", "STONE", 4);
        f2l("ANDESITE", "STONE", 5);
        f2l("POLISHED_ANDESITE", "STONE", 6);

        f2l("GRASS_BLOCK", "GRASS");

        f2l("DIRT");
        f2l("COARSE_DIRT", "DIRT", 1);
        f2l("PODZOL", "DIRT", 2);

        f2l("COBBLESTONE");

        f2l("OAK_PLANKS", "WOOD");
        f2l("SPRUCE_PLANKS", "WOOD", 1);
        f2l("BIRCH_PLANKS", "WOOD", 2);
        f2l("JUNGLE_PLANKS", "WOOD", 3);
        f2l("ACACIA_PLANKS", "WOOD", 4);
        f2l("DARK_OAK_PLANKS", "WOOD", 5);

        f2l("OAK_SAPLING", "SAPLING");
        f2l("SPRUCE_SAPLING", "SAPLING", 1);
        f2l("BIRCH_SAPLING", "SAPLING", 2);
        f2l("JUNGLE_SAPLING", "SAPLING", 3);
        f2l("ACACIA_SAPLING", "SAPLING", 4);
        f2l("DARK_OAK_SAPLING", "SAPLING", 5);

        f2l("BEDROCK");

        f2lBlock("WATER");
        //f2lBlock("WATER", "STATIONARY_WATER"); // bukkit mapping collision
        f2lBlock("LAVA");
        //f2lBlock("LAVA", "STATIONARY_LAVA"); // bukkit mapping collision

        f2l("SAND");
        f2l("RED_SAND", "SAND", 1);

        f2l("GRAVEL");
        f2l("GOLD_ORE");
        f2l("IRON_ORE");
        f2l("COAL_ORE");

        f2l("OAK_LOG", "LOG", 0);
        f2l("SPRUCE_LOG", "LOG", 1);
        f2l("BIRCH_LOG", "LOG", 2);
        f2l("JUNGLE_LOG", "LOG", 3);

        // is this correct?
        f2l("OAK_WOOD", "LOG", 12);
        f2l("SPRUCE_WOOD", "LOG", 13);
        f2l("BIRCH_WOOD", "LOG", 14);
        f2l("JUNGLE_WOOD", "LOG", 15);

        f2l("OAK_LEAVES", "LEAVES", 0);
        f2l("SPRUCE_LEAVES", "LEAVES", 1);
        f2l("BIRCH_LEAVES", "LEAVES", 2);
        f2l("JUNGLE_LEAVES", "LEAVES", 3);

        f2l("SPONGE");
        f2l("WET_SPONGE", "SPONGE", 1);

        f2l("GLASS");
        f2l("LAPIS_ORE");
        f2l("LAPIS_BLOCK");

        f2l("DISPENSER");

        f2l("SANDSTONE");
        f2l("CHISELED_SANDSTONE", "SANDSTONE", 1);
        f2l("CUT_SANDSTONE", "SANDSTONE", 2);

        f2l("NOTE_BLOCK");

        if (mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            f2lBlock("RED_BED", "BED_BLOCK");
            f2lColoredToNonColoredBlock("BED", "BED_BLOCK");
        } else {
            f2lBlock("WHITE_BED", "BED_BLOCK", 0, 0);
            f2lBlock("ORANGE_BED", "BED_BLOCK", 0, 1);
            f2lBlock("MAGENTA_BED", "BED_BLOCK", 0, 2);
            f2lBlock("LIGHT_BLUE_BED", "BED_BLOCK", 0, 3);
            f2lBlock("YELLOW_BED", "BED_BLOCK", 0, 4);
            f2lBlock("LIME_BED", "BED_BLOCK", 0, 5);
            f2lBlock("PINK_BED", "BED_BLOCK", 0, 6);
            f2lBlock("GRAY_BED", "BED_BLOCK", 0, 7);
            f2lBlock("LIGHT_GRAY_BED", "BED_BLOCK", 0, 8);
            f2lBlock("CYAN_BED", "BED_BLOCK", 0, 9);
            f2lBlock("PURPLE_BED", "BED_BLOCK", 0, 10);
            f2lBlock("BLUE_BED", "BED_BLOCK", 0, 11);
            f2lBlock("BROWN_BED", "BED_BLOCK", 0, 12);
            f2lBlock("GREEN_BED", "BED_BLOCK", 0, 13);
            f2lBlock("RED_BED", "BED_BLOCK", 0, 14);
            f2lBlock("BLACK_BED", "BED_BLOCK", 0, 15);
        }

        f2l("POWERED_RAIL");
        f2l("DETECTOR_RAIL");

        f2l("STICKY_PISTON", "PISTON_STICKY_BASE");
        f2l("COBWEB", "WEB");
        f2l("DEAD_BUSH");
        f2l("DEAD_BUSH", "LONG_GRASS"); // shrub == dead_bush but on grass
        f2l("GRASS", "LONG_GRASS", 1);
        f2l("FERN", "LONG_GRASS", 2);
        f2l("DEAD_BUSH", "LONG_GRASS", 3); // another shrub == dead_bush?? wtf??
        f2l("PISTON", "PISTON_BASE");
        f2lBlock("PISTON_HEAD", "PISTON_EXTENSION");
        f2lColored("WOOL");
        f2lBlock("MOVING_PISTON", "PISTON_MOVING_PIECE", 0);
        f2l("DANDELION", "YELLOW_FLOWER", 0);

        f2l("POPPY", "RED_ROSE");
        f2l("BLUE_ORCHID", "RED_ROSE", 1);
        f2l("ALLIUM", "RED_ROSE", 2);
        f2l("AZURE_BLUET", "RED_ROSE", 3);
        f2l("RED_TULIP", "RED_ROSE", 4);
        f2l("ORANGE_TULIP", "RED_ROSE", 5);
        f2l("WHITE_TULIP", "RED_ROSE", 6);
        f2l("PINK_TULIP", "RED_ROSE", 7);
        f2l("OXEYE_DAISY", "RED_ROSE", 8);

        f2l("BROWN_MUSHROOM");
        f2l("RED_MUSHROOM");
        f2l("GOLD_BLOCK");
        f2l("IRON_BLOCK");

        f2l("SMOOTH_STONE_SLAB", "STEP", 0);
        f2l("SANDSTONE_SLAB", "STEP", 1);
        f2l("PETRIFIED_OAK_SLAB", "STEP", 2);
        f2l("COBBLESTONE_SLAB", "STEP", 3);
        f2l("BRICK_SLAB", "STEP", 4);
        f2l("STONE_BRICK_SLAB", "STEP", 5);
        f2l("NETHER_BRICK_SLAB", "STEP", 6);
        f2l("QUARTZ_SLAB", "STEP", 7);

        f2l("BRICKS", "BRICK");
        f2l("TNT");
        f2l("BOOKSHELF");
        f2l("MOSSY_COBBLESTONE");
        f2l("OBSIDIAN");
        f2lItem("TORCH");
        f2lBlock("WALL_TORCH", "TORCH", 4); // north, default one
        f2lBlock("TORCH", "TORCH", 5);
        f2lBlock("FIRE");
        f2l("SPAWNER", "MOB_SPAWNER");
        f2l("OAK_STAIRS", "WOOD_STAIRS");
        f2lItem("CHEST");
        f2lBlock("CHEST", "CHEST", 2); // north, default value
        f2lBlock("REDSTONE_WIRE");
        f2l("DIAMOND_ORE");
        f2l("DIAMOND_BLOCK");
        f2l("CRAFTING_TABLE", "WORKBENCH");
        f2lBlock("WHEAT", "CROPS");
        f2l("FARMLAND", "SOIL");
        f2lItem("FURNACE");
        f2lBlock("FURNACE", "FURNACE", 2); // north, default value
        f2lBlock("OAK_SIGN", "SIGN_POST");
        f2lBlock("OAK_DOOR", "WOODEN_DOOR", 64);
        f2lItem("LADDER");
        f2lBlock("LADDER", "LADDER", 2); // north
        f2l("RAIL", "RAILS"); //default value north_south
        f2l("COBBLESTONE_STAIRS");
        f2lBlock("OAK_WALL_SIGN", "WALL_SIGN"); // north
        f2l("LEVER");
        f2l("STONE_PRESSURE_PLATE", "STONE_PLATE");
        f2lBlock("IRON_DOOR", "IRON_DOOR_BLOCK");
        f2l("OAK_PRESSURE_PLATE", "WOOD_PLATE");
        f2l("REDSTONE_ORE");
        f2lItem("REDSTONE_TORCH", "REDSTONE_TORCH_ON");
        f2lBlock("WALL_REDSTONE_TORCH", "REDSTONE_TORCH_ON", 4); // north, default one
        f2lBlock("REDSTONE_TORCH", "REDSTONE_TORCH_ON", 5);
        f2l("STONE_BUTTON");
        f2l("SNOW");
        f2l("ICE");
        f2l("SNOW_BLOCK");
        f2l("CACTUS");
        f2l("CLAY");
        f2lBlock("SUGAR_CANE", "SUGAR_CANE_BLOCK");
        f2l("JUKEBOX");
        f2l("OAK_FENCE", "FENCE");
        f2l("PUMPKIN");
        f2l("CARVED_PUMPKIN", "PUMPKIN");
        f2l("NETHERRACK");
        f2l("SOUL_SAND");
        f2l("GLOWSTONE");
        f2lBlock("NETHER_PORTAL", "PORTAL", 1);
        f2l("JACK_O_LANTERN");
        f2lBlock("CAKE", "CAKE_BLOCK");
        f2lBlock("REPEATER", "DIODE_BLOCK_OFF");
        f2lColored("STAINED_GLASS");
        f2l("OAK_TRAPDOOR", "TRAP_DOOR");

        f2l("INFESTED_STONE", "MONSTER_EGGS", 0);
        f2l("INFESTED_COBBLESTONE", "MONSTER_EGGS", 1);
        f2l("INFESTED_STONE_BRICKS", "MONSTER_EGGS", 2);
        f2l("INFESTED_MOSSY_STONE_BRICKS", "MONSTER_EGGS", 3);
        f2l("INFESTED_CRACKED_STONE_BRICKS", "MONSTER_EGGS", 4);
        f2l("INFESTED_CHISELED_STONE_BRICKS", "MONSTER_EGGS", 5);

        f2l("STONE_BRICKS", "SMOOTH_BRICK", 0);
        f2l("MOSSY_STONE_BRICKS", "SMOOTH_BRICK", 1);
        f2l("CRACKED_STONE_BRICKS", "SMOOTH_BRICK", 2);
        f2l("CHISELED_STONE_BRICKS", "SMOOTH_BRICK", 3);

        f2l("BROWN_MUSHROOM_BLOCK", "HUGE_MUSHROOM_1");
        f2l("RED_MUSHROOM_BLOCK", "HUGE_MUSHROOM_2");
        f2l("IRON_BARS", "IRON_FENCE");
        f2l("GLASS_PANE", "THIN_GLASS");
        f2l("MELON", "MELON_BLOCK");
        f2lBlock("PUMPKIN_STEM");
        f2lBlock("MELON_STEM");
        f2l("VINE");
        f2l("OAK_FENCE_GATE", "FENCE_GATE");
        f2l("BRICK_STAIRS");
        f2l("STONE_BRICK_STAIRS", "SMOOTH_STAIRS");
        f2l("MYCELIUM", "MYCEL");
        f2l("LILY_PAD", "WATER_LILY");
        f2l("NETHER_BRICKS", "NETHER_BRICK");
        f2l("NETHER_BRICK_FENCE", "NETHER_FENCE");
        f2l("NETHER_BRICK_STAIRS");
        f2lBlock("NETHER_WART", "NETHER_WARTS");
        f2l("ENCHANTING_TABLE", "ENCHANTMENT_TABLE");
        f2lBlock("BREWING_STAND");
        f2lBlock("CAULDRON");
        f2lBlock("END_PORTAL", "ENDER_PORTAL");
        f2lBlock("END_PORTAL_FRAME", "ENDER_PORTAL_FRAME");
        f2l("END_STONE", "ENDER_STONE");
        f2l("DRAGON_EGG");
        f2l("REDSTONE_LAMP", "REDSTONE_LAMP_OFF");

        f2l("OAK_SLAB", "WOOD_STEP", 0);
        f2l("SPRUCE_SLAB", "WOOD_STEP", 1);
        f2l("BIRCH_SLAB", "WOOD_STEP", 2);
        f2l("JUNGLE_SLAB", "WOOD_STEP", 3);
        f2l("ACACIA_SLAB", "WOOD_STEP", 4);
        f2l("DARK_OAK_SLAB", "WOOD_STEP", 5);

        f2lBlock("COCOA");
        f2l("SANDSTONE_STAIRS");
        f2l("EMERALD_ORE");
        f2lItem("ENDER_CHEST");
        f2lBlock("ENDER_CHEST", "ENDER_CHEST", 2); // north, default value
        f2l("TRIPWIRE_HOOK");
        f2lBlock("TRIPWIRE");
        f2l("EMERALD_BLOCK");
        f2l("SPRUCE_STAIRS", "SPRUCE_WOOD_STAIRS");
        f2l("BIRCH_STAIRS", "BIRCH_WOOD_STAIRS");
        f2l("JUNGLE_STAIRS", "JUNGLE_WOOD_STAIRS");
        f2l("COMMAND_BLOCK", "COMMAND");
        f2l("BEACON");
        f2l("COBBLESTONE_WALL", "COBBLE_WALL");
        f2l("MOSSY_COBBLESTONE_WALL", "COBBLE_WALL", 1);

        f2lBlock("FLOWER_POT");
        f2lBlock("POTTED_POPPY", "FLOWER_POT", 0, 0x260);
        f2lBlock("POTTED_DANDELION", "FLOWER_POT", 0, 0x250);
        f2lBlock("POTTED_OAK_SAPLING", "FLOWER_POT", 0, 0x60);
        f2lBlock("POTTED_SPRUCE_SAPLING", "FLOWER_POT", 0, 0x61);
        f2lBlock("POTTED_BIRCH_SAPLING", "FLOWER_POT", 0, 0x62);
        f2lBlock("POTTED_JUNGLE_SAPLING", "FLOWER_POT", 0, 0x63);
        f2lBlock("POTTED_RED_MUSHROOM", "FLOWER_POT", 0, 0x280);
        f2lBlock("POTTED_BROWN_MUSHROOM", "FLOWER_POT", 0, 0x270);
        f2lBlock("POTTED_CACTUS", "FLOWER_POT", 0, 0x510);
        f2lBlock("POTTED_DEAD_BUSH", "FLOWER_POT", 0, 0x200);
        f2lBlock("POTTED_FERN", "FLOWER_POT", 0, 0x1F2);
        f2lBlock("POTTED_ACACIA_SAPLING", "FLOWER_POT", 0, 0x64);
        f2lBlock("POTTED_DARK_OAK_SAPLING", "FLOWER_POT", 0, 0x65);
        f2lBlock("POTTED_BLUE_ORCHID", "FLOWER_POT", 0, 0x261);
        f2lBlock("POTTED_ALLIUM", "FLOWER_POT", 0, 0x262);
        f2lBlock("POTTED_AZURE_BLUET", "FLOWER_POT", 0, 0x263);
        f2lBlock("POTTED_RED_TULIP", "FLOWER_POT", 0, 0x264);
        f2lBlock("POTTED_ORANGE_TULIP", "FLOWER_POT", 0, 0x265);
        f2lBlock("POTTED_WHITE_TULIP", "FLOWER_POT", 0, 0x266);
        f2lBlock("POTTED_PINK_TULIP", "FLOWER_POT", 0, 0x267);
        f2lBlock("POTTED_OXEYE_DAISY", "FLOWER_POT", 0, 0x268);

        f2lBlock("CARROTS", "CARROT");
        f2lBlock("POTATOES", "POTATO");
        f2l("OAK_BUTTON", "WOOD_BUTTON");
        f2lBlock("SKELETON_SKULL", "SKULL", 1);
        f2lBlock("WITHER_SKELETON_SKULL", "SKULL", 1, 1);
        f2lBlock("ZOMBIE_HEAD", "SKULL", 1, 2);
        f2lBlock("PLAYER_HEAD", "SKULL", 1, 3);
        f2lBlock("CREEPER_HEAD", "SKULL", 1, 4);
        if (Version.isVersion(1, 9)) {
            f2lBlock("DRAGON_HEAD", "SKULL", 1, 5);
        }
        f2lBlock("SKELETON_WALL_SKULL", "SKULL", 2);
        f2lBlock("WITHER_SKELETON_WALL_SKULL", "SKULL", 2, 1);
        f2lBlock("ZOMBIE_WALL_HEAD", "SKULL", 2, 2);
        f2lBlock("PLAYER_WALL_HEAD", "SKULL", 2, 3);
        f2lBlock("CREEPER_WALL_HEAD", "SKULL", 2, 4);
        if (Version.isVersion(1, 9)) {
            f2lBlock("DRAGON_WALL_HEAD", "SKULL", 2, 5);
        }
        f2l("ANVIL");
        f2lItem("CHIPPED_ANVIL", "ANVIL", 1);
        f2lBlock("CHIPPED_ANVIL", "ANVIL", 4);
        f2lItem("DAMAGED_ANVIL", "ANVIL", 2);
        f2lBlock("DAMAGED_ANVIL", "ANVIL", 8);
        f2lItem("TRAPPED_CHEST");
        f2lBlock("TRAPPED_CHEST", "TRAPPED_CHEST", 2); // north, default value
        f2l("LIGHT_WEIGHTED_PRESSURE_PLATE", "GOLD_PLATE");
        f2l("HEAVY_WEIGHTED_PRESSURE_PLATE", "IRON_PLATE");
        f2lBlock("COMPARATOR", "REDSTONE_COMPARATOR_OFF");
        f2l("DAYLIGHT_DETECTOR");
        f2l("REDSTONE_BLOCK");
        f2l("NETHER_QUARTZ_ORE", "QUARTZ_ORE");
        f2l("HOPPER");
        f2l("QUARTZ_BLOCK");
        f2l("CHISELED_QUARTZ_BLOCK", "QUARTZ_BLOCK", 1);
        f2l("QUARTZ_PILLAR", "QUARTZ_BLOCK", 2);

        f2l("QUARTZ_STAIRS");
        f2l("ACTIVATOR_RAIL");
        f2l("DROPPER");
        f2lColored("TERRACOTTA", "STAINED_CLAY");
        f2lColored("STAINED_GLASS_PANE");

        f2l("ACACIA_LEAVES", "LEAVES_2", 0);
        f2l("DARK_OAK_LEAVES", "LEAVES_2", 1);

        f2l("ACACIA_LOG", "LOG_2", 0);
        f2l("DARK_OAK_LOG", "LOG_2", 1);
        f2l("ACACIA_WOOD", "LOG_2", 12);
        f2l("DARK_OAK_WOOD", "LOG_2", 13);

        f2l("ACACIA_STAIRS");
        f2l("DARK_OAK_STAIRS");

        f2l("SLIME_BLOCK");
        f2l("BARRIER");
        f2l("IRON_TRAPDOOR");

        f2l("PRISMARINE");
        f2l("PRISMARINE_BRICKS", "PRISMARINE", 1);
        f2l("DARK_PRISMARINE", "PRISMARINE", 2);

        f2l("SEA_LANTERN");
        f2l("HAY_BLOCK");
        f2lColored("CARPET");
        f2l("TERRACOTTA", "HARD_CLAY");
        f2l("COAL_BLOCK");
        f2l("PACKED_ICE");

        f2l("SUNFLOWER", "DOUBLE_PLANT", 0);
        f2l("LILAC", "DOUBLE_PLANT", 1);
        f2l("TALL_GRASS", "DOUBLE_PLANT", 2);
        f2l("LARGE_FERN", "DOUBLE_PLANT", 3);
        f2l("ROSE_BUSH", "DOUBLE_PLANT", 4);
        f2l("PEONY", "DOUBLE_PLANT", 5);
        // top half has just one variant and the block type is read from the bottom block data

        f2lColoredToNonColoredBlock("BANNER", "STANDING_BANNER");
        f2lColoredToNonColoredBlock("WALL_BANNER");

        f2l("RED_SANDSTONE");
        f2l("CHISELED_RED_SANDSTONE", "RED_SANDSTONE", 1);
        f2l("CUT_RED_SANDSTONE", "RED_SANDSTONE", 2);

        f2l("RED_SANDSTONE_STAIRS");
        f2l("SMOOTH_RED_SANDSTONE", "DOUBLE_STONE_SLAB2");
        f2l("RED_SANDSTONE_SLAB", "STONE_SLAB2");
        f2l("SPRUCE_FENCE_GATE");
        f2l("BIRCH_FENCE_GATE");
        f2l("JUNGLE_FENCE_GATE");
        f2l("DARK_OAK_FENCE_GATE");
        f2l("ACACIA_FENCE_GATE");
        f2l("SPRUCE_FENCE");
        f2l("BIRCH_FENCE");
        f2l("JUNGLE_FENCE");
        f2l("DARK_OAK_FENCE");
        f2l("ACACIA_FENCE");
        f2lBlock("SPRUCE_DOOR");
        f2lBlock("BIRCH_DOOR");
        f2lBlock("JUNGLE_DOOR");
        f2lBlock("ACACIA_DOOR");
        f2lBlock("DARK_OAK_DOOR");
        f2l("END_ROD"); // down, default one
        f2l("CHORUS_PLANT");
        f2l("CHORUS_FLOWER");
        f2l("PURPUR_BLOCK");
        f2l("PURPUR_PILLAR");
        f2l("PURPUR_STAIRS");
        f2l("PURPUR_SLAB");

        f2l("END_STONE_BRICKS", "END_BRICKS");
        f2lBlock("BEETROOTS", "BEETROOT_BLOCK");
        f2l("GRASS_PATH");
        f2lBlock("END_GATEWAY");
        f2l("REPEATING_COMMAND_BLOCK", "COMMAND_REPEATING");
        f2l("CHAIN_COMMAND_BLOCK", "COMMAND_CHAIN");
        f2l("FROSTED_ICE");
        f2l("MAGMA_BLOCK", "MAGMA");
        f2l("NETHER_WART_BLOCK");
        f2l("RED_NETHER_BRICKS", "RED_NETHER_BRICK");
        f2l("BONE_BLOCK");
        f2l("STRUCTURE_VOID");
        f2l("OBSERVER");
        f2l("WHITE_SHULKER_BOX");
        f2l("ORANGE_SHULKER_BOX");
        f2l("MAGENTA_SHULKER_BOX");
        f2l("LIGHT_BLUE_SHULKER_BOX");
        f2l("YELLOW_SHULKER_BOX");
        f2l("LIME_SHULKER_BOX");
        f2l("PINK_SHULKER_BOX");
        f2l("GRAY_SHULKER_BOX");
        f2l("LIGHT_GRAY_SHULKER_BOX", "SILVER_SHULKER_BOX");
        f2l("CYAN_SHULKER_BOX");
        f2l("PURPLE_SHULKER_BOX");
        f2l("BLUE_SHULKER_BOX");
        f2l("BROWN_SHULKER_BOX");
        f2l("GREEN_SHULKER_BOX");
        f2l("RED_SHULKER_BOX");
        f2l("BLACK_SHULKER_BOX");
        f2l("WHITE_GLAZED_TERRACOTTA"); // north
        f2l("ORANGE_GLAZED_TERRACOTTA"); // north
        f2l("MAGENTA_GLAZED_TERRACOTTA"); // north
        f2l("LIGHT_BLUE_GLAZED_TERRACOTTA"); // north
        f2l("YELLOW_GLAZED_TERRACOTTA"); // north
        f2l("LIME_GLAZED_TERRACOTTA"); // north
        f2l("PINK_GLAZED_TERRACOTTA"); // north
        f2l("GRAY_GLAZED_TERRACOTTA"); // north
        f2l("LIGHT_GRAY_GLAZED_TERRACOTTA", "SILVER_GLAZED_TERRACOTTA"); // north
        f2l("CYAN_GLAZED_TERRACOTTA"); // north
        f2l("PURPLE_GLAZED_TERRACOTTA"); // north
        f2l("BLUE_GLAZED_TERRACOTTA"); // north
        f2l("BROWN_GLAZED_TERRACOTTA"); // north
        f2l("GREEN_GLAZED_TERRACOTTA"); // north
        f2l("RED_GLAZED_TERRACOTTA"); // north
        f2l("BLACK_GLAZED_TERRACOTTA"); // north
        f2lColored("CONCRETE");
        f2lColored("CONCRETE_POWDER");
        f2l("STRUCTURE_BLOCK"); // data
        //</editor-fold>

        //<editor-fold desc="Items" defaultstate="collapsed">

        // ITEMS
        f2lItem("IRON_SHOVEL", "IRON_SPADE");
        f2lItem("IRON_PICKAXE");
        f2lItem("IRON_AXE");
        f2lItem("FLINT_AND_STEEL");
        f2lItem("APPLE");
        f2lItem("BOW");
        f2lItem("ARROW");

        f2lItem("COAL");
        f2lItem("CHARCOAL", "COAL", 1);

        f2lItem("DIAMOND");
        f2lItem("IRON_INGOT");
        f2lItem("GOLD_INGOT");
        f2lItem("IRON_SWORD");
        f2lItem("WOODEN_SWORD", "WOOD_SWORD");
        f2lItem("WOODEN_SHOVEL", "WOOD_SPADE");
        f2lItem("WOODEN_PICKAXE", "WOOD_PICKAXE");
        f2lItem("WOODEN_AXE", "WOOD_AXE");
        f2lItem("STONE_SWORD");
        f2lItem("STONE_SHOVEL", "STONE_SPADE");
        f2lItem("STONE_PICKAXE");
        f2lItem("STONE_AXE");
        f2lItem("DIAMOND_SWORD");
        f2lItem("DIAMOND_SHOVEL", "DIAMOND_SPADE");
        f2lItem("DIAMOND_PICKAXE");
        f2lItem("DIAMOND_AXE");
        f2lItem("STICK");
        f2lItem("BOWL");
        f2lItem("MUSHROOM_STEW", "MUSHROOM_SOUP");
        f2lItem("GOLDEN_SWORD", "GOLD_SWORD");
        f2lItem("GOLDEN_SHOVEL", "GOLD_SPADE");
        f2lItem("GOLDEN_PICKAXE", "GOLD_PICKAXE");
        f2lItem("GOLDEN_AXE", "GOLD_AXE");
        f2lItem("STRING");
        f2lItem("FEATHER");
        f2lItem("GUNPOWDER", "SULPHUR");
        f2lItem("WOODEN_HOE", "WOOD_HOE");
        f2lItem("STONE_HOE");
        f2lItem("IRON_HOE");
        f2lItem("DIAMOND_HOE");
        f2lItem("GOLDEN_HOE", "GOLD_HOE");
        f2lItem("WHEAT_SEEDS", "SEEDS");
        f2lItem("WHEAT");
        f2lItem("BREAD");
        f2lItem("LEATHER_HELMET");
        f2lItem("LEATHER_CHESTPLATE");
        f2lItem("LEATHER_LEGGINGS");
        f2lItem("LEATHER_BOOTS");
        f2lItem("CHAINMAIL_HELMET");
        f2lItem("CHAINMAIL_CHESTPLATE");
        f2lItem("CHAINMAIL_LEGGINGS");
        f2lItem("CHAINMAIL_BOOTS");
        f2lItem("IRON_HELMET");
        f2lItem("IRON_CHESTPLATE");
        f2lItem("IRON_LEGGINGS");
        f2lItem("IRON_BOOTS");
        f2lItem("DIAMOND_HELMET");
        f2lItem("DIAMOND_CHESTPLATE");
        f2lItem("DIAMOND_LEGGINGS");
        f2lItem("DIAMOND_BOOTS");
        f2lItem("GOLDEN_HELMET", "GOLD_HELMET");
        f2lItem("GOLDEN_CHESTPLATE", "GOLD_CHESTPLATE");
        f2lItem("GOLDEN_LEGGINGS", "GOLD_LEGGINGS");
        f2lItem("GOLDEN_BOOTS", "GOLD_BOOTS");
        f2lItem("FLINT");
        f2lItem("PORKCHOP", "PORK");
        f2lItem("COOKED_PORKCHOP", "GRILLED_PORK");
        f2lItem("PAINTING");
        f2lItem("GOLDEN_APPLE");
        f2lItem("ENCHANTED_GOLDEN_APPLE", "GOLDEN_APPLE", 1);
        f2lItem("OAK_SIGN", "SIGN");
        f2lItem("OAK_DOOR", "WOOD_DOOR");
        f2lItem("BUCKET");
        f2lItem("WATER_BUCKET");
        f2lItem("LAVA_BUCKET");
        f2lItem("MINECART");
        f2lItem("SADDLE");
        f2lItem("IRON_DOOR");
        f2lItem("REDSTONE");
        f2lItem("SNOWBALL", "SNOW_BALL");
        f2lItem("OAK_BOAT", "BOAT");
        f2lItem("LEATHER");
        f2lItem("MILK_BUCKET");
        f2lItem("BRICK", "CLAY_BRICK");
        f2lItem("CLAY_BALL");
        f2lItem("SUGAR_CANE", "REEDS");
        f2lItem("PAPER");
        f2lItem("BOOK");
        f2lItem("SLIME_BALL");
        f2lItem("CHEST_MINECART", "STORAGE_MINECART");
        f2lItem("FURNACE_MINECART", "POWERED_MINECART");
        f2lItem("EGG");
        f2lItem("COMPASS");
        f2lItem("FISHING_ROD");
        f2lItem("CLOCK", "WATCH");
        f2lItem("GLOWSTONE_DUST");

        f2lItem("COD", "RAW_FISH");
        f2lItem("SALMON", "RAW_FISH", 1);
        f2lItem("TROPICAL_FISH", "RAW_FISH", 2);
        f2lItem("PUFFERFISH", "RAW_FISH", 3);
        f2lItem("COOKED_COD", "COOKED_FISH");
        f2lItem("COOKED_SALMON", "COOKED_FISH", 1);

        f2lItem("INK_SACK");
        f2lItem("ROSE_RED", "INK_SACK", 1);
        f2lItem("CACTUS_GREEN", "INK_SACK", 2);
        f2lItem("COCOA_BEANS", "INK_SACK", 3);
        f2lItem("LAPIS_LAZULI", "INK_SACK", 4);
        f2lItem("PURPLE_DYE", "INK_SACK", 5);
        f2lItem("CYAN_DYE", "INK_SACK", 6);
        f2lItem("LIGHT_GRAY_DYE", "INK_SACK", 7);
        f2lItem("GRAY_DYE", "INK_SACK", 8);
        f2lItem("PINK_DYE", "INK_SACK", 9);
        f2lItem("LIME_DYE", "INK_SACK", 10);
        f2lItem("DANDELION_YELLOW", "INK_SACK", 11);
        f2lItem("LIGHT_BLUE_DYE", "INK_SACK", 12);
        f2lItem("MAGENTA_DYE", "INK_SACK", 13);
        f2lItem("ORANGE_DYE", "INK_SACK", 14);
        f2lItem("BONE_MEAL", "INK_SACK", 15);

        f2lItem("BONE");
        f2lItem("SUGAR");
        f2lItem("CAKE");

        if (mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            f2lColoredToNonColoredItem("BED");
        } else {
            f2lColoredItem("BED");
        }

        f2lItem("REPEATER", "DIODE");
        f2lItem("COOKIE");
        f2lItem("FILLED_MAP", "MAP");
        f2lItem("SHEARS");
        f2lItem("MELON_SLICE", "MELON");
        f2lItem("PUMPKIN_SEEDS");
        f2lItem("MELON_SEEDS");
        f2lItem("BEEF", "RAW_BEEF");
        f2lItem("COOKED_BEEF");
        f2lItem("CHICKEN", "RAW_CHICKEN");
        f2lItem("COOKED_CHICKEN");
        f2lItem("ROTTEN_FLESH");
        f2lItem("ENDER_PEARL");
        f2lItem("BLAZE_ROD");
        f2lItem("GHAST_TEAR");
        f2lItem("GOLD_NUGGET");
        f2lItem("NETHER_WART", "NETHER_STALK");
        f2lItem("POTION");
        f2lItem("GLASS_BOTTLE");
        f2lItem("SPIDER_EYE");
        f2lItem("FERMENTED_SPIDER_EYE");
        f2lItem("BLAZE_POWDER");
        f2lItem("MAGMA_CREAM");
        f2lItem("BREWING_STAND", "BREWING_STAND_ITEM");
        f2lItem("CAULDRON", "CAULDRON_ITEM");
        f2lItem("ENDER_EYE", "EYE_OF_ENDER");
        f2lItem("GLISTERING_MELON_SLICE", "SPECKLED_MELON");

        f2lItem("BAT_SPAWN_EGG", "MONSTER_EGG", 65);
        f2lItem("BLAZE_SPAWN_EGG", "MONSTER_EGG", 61);
        f2lItem("CAVE_SPIDER_SPAWN_EGG", "MONSTER_EGG", 59);
        f2lItem("CHICKEN_SPAWN_EGG", "MONSTER_EGG", 93);
        f2lItem("COW_SPAWN_EGG", "MONSTER_EGG", 92);
        f2lItem("CREEPER_SPAWN_EGG", "MONSTER_EGG", 50);
        f2lItem("DONKEY_SPAWN_EGG", "MONSTER_EGG", 31);
        f2lItem("ELDER_GUARDIAN_SPAWN_EGG", "MONSTER_EGG", 4);
        f2lItem("ENDERMAN_SPAWN_EGG", "MONSTER_EGG", 58);
        f2lItem("ENDERMITE_SPAWN_EGG", "MONSTER_EGG", 67);
        f2lItem("EVOKER_SPAWN_EGG", "MONSTER_EGG", 34);
        f2lItem("GHAST_SPAWN_EGG", "MONSTER_EGG", 56);
        f2lItem("GUARDIAN_SPAWN_EGG", "MONSTER_EGG", 68);
        f2lItem("HORSE_SPAWN_EGG", "MONSTER_EGG", 100);
        f2lItem("HUSK_SPAWN_EGG", "MONSTER_EGG", 23);
        f2lItem("LLAMA_SPAWN_EGG", "MONSTER_EGG", 103);
        f2lItem("MAGMA_CUBE_SPAWN_EGG", "MONSTER_EGG", 62);
        f2lItem("MOOSHROOM_SPAWN_EGG", "MONSTER_EGG", 96);
        f2lItem("MULE_SPAWN_EGG", "MONSTER_EGG", 32);
        f2lItem("OCELOT_SPAWN_EGG", "MONSTER_EGG", 98);
        f2lItem("PARROT_SPAWN_EGG", "MONSTER_EGG", 105);
        f2lItem("PIG_SPAWN_EGG", "MONSTER_EGG", 90);
        f2lItem("POLAR_BEAR_SPAWN_EGG", "MONSTER_EGG", 102);
        f2lItem("RABBIT_SPAWN_EGG", "MONSTER_EGG", 101);
        f2lItem("SHEEP_SPAWN_EGG", "MONSTER_EGG", 91);
        f2lItem("SHULKER_SPAWN_EGG", "MONSTER_EGG", 69);
        f2lItem("SILVERFISH_SPAWN_EGG", "MONSTER_EGG", 60);
        f2lItem("SKELETON_SPAWN_EGG", "MONSTER_EGG", 51);
        f2lItem("SKELETON_HORSE_SPAWN_EGG", "MONSTER_EGG", 28);
        f2lItem("SLIME_SPAWN_EGG", "MONSTER_EGG", 55);
        f2lItem("SPIDER_SPAWN_EGG", "MONSTER_EGG", 52);
        f2lItem("SQUID_SPAWN_EGG", "MONSTER_EGG", 94);
        f2lItem("STRAY_SPAWN_EGG", "MONSTER_EGG", 6);
        f2lItem("VEX_SPAWN_EGG", "MONSTER_EGG", 35);
        f2lItem("VILLAGER_SPAWN_EGG", "MONSTER_EGG", 120);
        f2lItem("VINDICATOR_SPAWN_EGG", "MONSTER_EGG", 36);
        f2lItem("WITCH_SPAWN_EGG", "MONSTER_EGG", 66);
        f2lItem("WITHER_SKELETON_SPAWN_EGG", "MONSTER_EGG", 5);
        f2lItem("WOLF_SPAWN_EGG", "MONSTER_EGG", 95);
        f2lItem("ZOMBIE_SPAWN_EGG", "MONSTER_EGG", 54);
        f2lItem("ZOMBIE_HORSE_SPAWN_EGG", "MONSTER_EGG", 29);
        f2lItem("ZOMBIE_PIGMAN_SPAWN_EGG", "MONSTER_EGG", 57);
        f2lItem("ZOMBIE_VILLAGER_SPAWN_EGG", "MONSTER_EGG", 27);

        f2lItem("EXPERIENCE_BOTTLE", "EXP_BOTTLE");
        f2lItem("FIRE_CHARGE", "FIREBALL");
        f2lItem("WRITABLE_BOOK", "BOOK_AND_QUILL");
        f2lItem("WRITTEN_BOOK");
        f2lItem("EMERALD");
        f2lItem("ITEM_FRAME");

        f2lItem("FLOWER_POT", "FLOWER_POT_ITEM");
        f2lItem("CARROT", "CARROT_ITEM");
        f2lItem("POTATO", "POTATO_ITEM");
        f2lItem("BAKED_POTATO");
        f2lItem("POISONOUS_POTATO");
        f2lItem("MAP", "EMPTY_MAP");
        f2lItem("GOLDEN_CARROT");

        f2lItem("SKELETON_SKULL", "SKULL_ITEM");
        f2lItem("WITHER_SKELETON_SKULL", "SKULL_ITEM", 1);
        f2lItem("ZOMBIE_HEAD", "SKULL_ITEM", 2);
        f2lItem("PLAYER_HEAD", "SKULL_ITEM", 3);
        f2lItem("CREEPER_HEAD", "SKULL_ITEM", 4);
        if (Version.isVersion(1, 9)) {
            f2lItem("DRAGON_HEAD", "SKULL_ITEM", 5);
        }

        f2lItem("CARROT_ON_A_STICK", "CARROT_STICK");
        f2lItem("NETHER_STAR");
        f2lItem("PUMPKIN_PIE");
        f2lItem("FIREWORK_ROCKET", "FIREWORK");
        f2lItem("FIREWORK_STAR", "FIREWORK_CHARGE");
        f2lItem("ENCHANTED_BOOK");
        f2lItem("COMPARATOR", "REDSTONE_COMPARATOR");
        f2lItem("NETHER_BRICK", "NETHER_BRICK_ITEM");
        f2lItem("QUARTZ");
        f2lItem("TNT_MINECART", "EXPLOSIVE_MINECART");
        f2lItem("HOPPER_MINECART");
        f2lItem("PRISMARINE_SHARD");
        f2lItem("PRISMARINE_CRYSTALS");
        f2lItem("RABBIT");
        f2lItem("COOKED_RABBIT");
        f2lItem("RABBIT_STEW");
        f2lItem("RABBIT_FOOT");
        f2lItem("RABBIT_HIDE");
        f2lItem("ARMOR_STAND");
        f2lItem("IRON_HORSE_ARMOR", "IRON_BARDING");
        f2lItem("GOLDEN_HORSE_ARMOR", "GOLD_BARDING");
        f2lItem("DIAMOND_HORSE_ARMOR", "DIAMOND_BARDING");
        f2lItem("LEAD", "LEASH");
        f2lItem("NAME_TAG");
        f2lItem("COMMAND_BLOCK_MINECART", "COMMAND_MINECART");
        f2lItem("MUTTON");
        f2lItem("COOKED_MUTTON");
        f2lColoredItem("BANNER");
        f2lItem("END_CRYSTAL");
        f2lItem("SPRUCE_DOOR", "SPRUCE_DOOR_ITEM");
        f2lItem("BIRCH_DOOR", "BIRCH_DOOR_ITEM");
        f2lItem("JUNGLE_DOOR", "JUNGLE_DOOR_ITEM");
        f2lItem("ACACIA_DOOR", "ACACIA_DOOR_ITEM");
        f2lItem("DARK_OAK_DOOR", "DARK_OAK_DOOR_ITEM");
        f2lItem("CHORUS_FRUIT");
        f2lItem("POPPED_CHORUS_FRUIT", "CHORUS_FRUIT_POPPED");
        f2lItem("BEETROOT");
        f2lItem("BEETROOT_SEEDS");
        f2lItem("BEETROOT_SOUP");
        f2lItem("DRAGONS_BREATH");
        f2lItem("SPLASH_POTION");
        f2lItem("SPECTRAL_ARROW");
        f2lItem("TIPPED_ARROW");
        f2lItem("LINGERING_POTION");
        f2lItem("SHIELD");
        f2lItem("ELYTRA");
        f2lItem("SPRUCE_BOAT", "BOAT_SPRUCE");
        f2lItem("BIRCH_BOAT", "BOAT_BIRCH");
        f2lItem("JUNGLE_BOAT", "BOAT_JUNGLE");
        f2lItem("ACACIA_BOAT", "BOAT_ACACIA");
        f2lItem("DARK_OAK_BOAT", "BOAT_DARK_OAK");
        f2lItem("TOTEM_OF_UNDYING", "TOTEM");
        f2lItem("SHULKER_SHELL");
        f2lItem("IRON_NUGGET");
        f2lItem("KNOWLEDGE_BOOK");

        // RECORDS
        f2lItem("MUSIC_DISC_13", "GOLD_RECORD");
        f2lItem("MUSIC_DISC_CAT", "GREEN_RECORD");
        f2lItem("MUSIC_DISC_BLOCKS", "RECORD_3");
        f2lItem("MUSIC_DISC_CHIRP", "RECORD_4");
        f2lItem("MUSIC_DISC_FAR", "RECORD_5");
        f2lItem("MUSIC_DISC_MALL", "RECORD_6");
        f2lItem("MUSIC_DISC_MELLOHI", "RECORD_7");
        f2lItem("MUSIC_DISC_STAL", "RECORD_8");
        f2lItem("MUSIC_DISC_STRAD", "RECORD_9");
        f2lItem("MUSIC_DISC_WARD", "RECORD_10");
        f2lItem("MUSIC_DISC_11", "RECORD_11");
        f2lItem("MUSIC_DISC_WAIT", "RECORD_12");
        //</editor-fold>

    }


    protected void f2lColored(@NotNull String material) {
        f2lColored(material, material);
    }

    protected void f2lColoredBlock(@NotNull String material) {
        f2lColoredBlock(material, material);
    }

    protected void f2lColoredItem(@NotNull String material) {
        f2lColoredItem(material, material);
    }

    protected void f2lColored(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        f2lColoredBlock(flatteningMaterialSuffix, legacyBukkitMaterial);
        f2lColoredItem(flatteningMaterialSuffix, legacyBukkitMaterial);
    }

    protected void f2lColoredItem(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        for (int i = 0; i <= 15; i++) {
            f2lItem(COLORS.get(i) + "_" + flatteningMaterialSuffix, legacyBukkitMaterial, i);
        }
    }

    protected void f2lColoredBlock(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        for (int i = 0; i <= 15; i++) {
            f2lBlock(COLORS.get(i) + "_" + flatteningMaterialSuffix, legacyBukkitMaterial, i);
        }
    }

    protected void f2lColoredToNonColored(@NotNull String material) {
        f2lColoredToNonColored(material, material);
    }

    protected void f2lColoredToNonColoredBlock(@NotNull String material) {
        f2lColoredToNonColoredBlock(material, material);
    }

    protected void f2lColoredToNonColoredItem(@NotNull String material) {
        f2lColoredToNonColoredItem(material, material);
    }

    protected void f2lColoredToNonColoredBlock(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyBukkitMaterial, 0);
    }

    protected void f2lColoredToNonColored(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        f2lColoredToNonColoredBlock(flatteningMaterialSuffix, legacyBukkitMaterial);
        f2lColoredToNonColoredItem(flatteningMaterialSuffix, legacyBukkitMaterial);
    }

    protected void f2lColoredToNonColoredBlock(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial, int data) {
        COLORS.forEach(s ->
                f2lBlock(s + "_" + flatteningMaterialSuffix, legacyBukkitMaterial, data)
        );
    }

    protected void f2lColoredToNonColoredItem(@NotNull String flatteningMaterialSuffix, @NotNull String legacyBukkitMaterial) {
        COLORS.forEach(s ->
                f2lItem(s + "_" + flatteningMaterialSuffix, legacyBukkitMaterial)
        );
    }

    /* For Materials where the name is same */
    protected void f2l(@NotNull String material) {
        f2l(material, material);
    }

    protected void f2lItem(@NotNull String material) {
        f2lItem(material, material);
    }

    protected void f2lBlock(@NotNull String material) {
        f2lBlock(material, material);
    }

    /* For Materials where the name is changed */
    protected void f2l(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial) {
        f2l(flatteningMaterial, legacyBukkitMaterial, 0);
    }

    protected void f2lItem(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial) {
        f2lItem(flatteningMaterial, legacyBukkitMaterial, 0);
    }

    protected void f2lBlock(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial) {
        f2lBlock(flatteningMaterial, legacyBukkitMaterial, 0);
    }

    /* For Materials where the name is changed and data is not zero*/
    protected void f2l(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial, int data) {
        f2lBlock(flatteningMaterial, legacyBukkitMaterial, data);
        f2lItem(flatteningMaterial, legacyBukkitMaterial, data);
    }

    protected void f2lItem(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial, int data) {
        var flatteningMaterialNamespaced = ResourceLocation.of(flatteningMaterial);

        if (!(itemTypeMapper instanceof BukkitItemTypeRegistry1_8)) {
            return;
        }

        try {
            var mat = Material.valueOf(legacyBukkitMaterial.toUpperCase(Locale.ROOT));
            var holder = new BukkitItemType1_8(mat, (short) data);

            var mapping = ((BukkitItemTypeRegistry1_8) itemTypeMapper).getPorts();
            if (!mapping.containsKey(flatteningMaterialNamespaced)) {
                mapping.put(flatteningMaterialNamespaced, holder);
            }

            var locations = ((BukkitItemTypeRegistry1_8) itemTypeMapper).getResourceLocations();
            if (!locations.containsKey(holder)) {
                locations.put(holder, flatteningMaterialNamespaced);
            }
        } catch (IllegalArgumentException ignored) {
            // not present in this version
        }
    }

    protected void f2lBlock(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial, int data) {
        f2lBlock(flatteningMaterial, legacyBukkitMaterial, data, 0);
    }

    protected void f2lBlock(@NotNull String flatteningMaterial, @NotNull String legacyBukkitMaterial, int data, int tileEntityData) {
        var flatteningMaterialNamespaced = ResourceLocation.of(flatteningMaterial);

        if (!(blockTypeMapper instanceof BukkitBlockRegistry1_8)) {
            return;
        }

        try {
            var mat = Material.valueOf(legacyBukkitMaterial.toUpperCase(Locale.ROOT));
            if (!mat.isBlock()) {
                return; // not a block
            }
            var holder = new BukkitBlock1_8(mat, (byte) data, tileEntityData);

            var mapping = ((BukkitBlockRegistry1_8) blockTypeMapper).getPorts();
            if (!mapping.containsKey(flatteningMaterialNamespaced)) {
                mapping.put(flatteningMaterialNamespaced, holder);
            }

            var locations = ((BukkitBlockRegistry1_8) blockTypeMapper).getResourceLocations();
            if (!locations.containsKey(holder)) {
                locations.put(holder, flatteningMaterialNamespaced);
            }
        } catch (IllegalArgumentException ignored) {
            // not present in this version
        }
    }
}
