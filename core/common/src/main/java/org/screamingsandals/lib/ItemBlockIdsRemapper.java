package org.screamingsandals.lib;

import lombok.RequiredArgsConstructor;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.block.BlockTypeMapper;
import org.screamingsandals.lib.item.ItemTypeHolder;
import org.screamingsandals.lib.item.ItemTypeMapper;
import org.screamingsandals.lib.material.MappingFlags;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.methods.OnPostConstruct;
import org.screamingsandals.lib.utils.key.ComplexMappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@AbstractService(
        pattern = "^(?<basePackage>.+)\\.(?<className>.+)$",
        replaceRule = "{basePackage}.{platform}.{Platform}{className}"
)
@RequiredArgsConstructor
public abstract class ItemBlockIdsRemapper {
    private final ItemTypeMapper itemTypeMapper;
    private final BlockTypeMapper blockTypeMapper;
    protected Platform platform;
    protected final List<MappingFlags> mappingFlags = new ArrayList<>(); private static final List<String> colors = List.of(
            "WHITE",
            "ORANGE",
            "MAGENTA",
            "LIGHT_BLUE",
            "YELLOW",
            "LIME",
            "PINK",
            "GRAY",
            "LIGHT_GRAY",
            "CYAN",
            "PURPLE",
            "BLUE",
            "BROWN",
            "GREEN",
            "RED",
            "BLACK"
    );
    protected Map<Predicate<BlockTypeHolder>, Function<String, Optional<BlockTypeHolder>>> colorable = new HashMap<>();


    @OnPostConstruct
    public void doMapping() {
 /*
        if server is running Java Edition Post-Flattening version, flattening remappings have to been applied first
        on legacy versions you had to run it after the legacy

        The reason why the order is important is due to often renaming of java flattening names
        */
        if (platform == Platform.JAVA_FLATTENING) {
            flatteningMapping();
        }

        if (platform.name().startsWith("JAVA")) {
            flatteningLegacyMappingJava();

            if (platform != Platform.JAVA_FLATTENING) {
                flatteningMapping();
            }

            javaAutoColorable();
        }
    }

    private void javaAutoColorable() {
        makeColorable("WOOL");
        makeColorable("CARPET");
        makeColorable("CONCRETE");
        makeColorable("CONCRETE_POWDER");
        makeColorable("TERRACOTTA");
        makeColorable("STAINED_GLASS", "GLASS");
        makeColorable("STAINED_GLASS_PANE", "GLASS_PANE");
        makeColorable("SHULKER_BOX");
        makeColorable("BANNER");
        makeColorable("GLAZED_TERRACOTTA");

        if (!mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            makeColorable("BED");
        }
    }

    private void makeColorable(String baseName) {
        makeColorable(baseName, baseName);
    }

    private void makeColorable(String baseName, String notColoredName) {
        var list = new ArrayList<BlockTypeHolder>();
        colors.forEach(s -> BlockTypeHolder.ofOptional(s + "_" + baseName).ifPresent(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        BlockTypeHolder.ofOptional(notColoredName).ifPresent(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorable.put(list::contains, s -> {
                if (colors.contains(s.toUpperCase())) {
                    return BlockTypeHolder.ofOptional(s.toUpperCase() + "_" + baseName);
                }
                return Optional.empty();
            });
        }
    }

    private void flatteningLegacyMappingJava() {
        // Legacy remapping

        // BLOCKS
        f2l("AIR", 0);

        f2l("STONE", 1);
        f2l("GRANITE", "STONE", 1, 1);
        f2l("POLISHED_GRANITE", "STONE", 1, 2);
        f2l("DIORITE", "STONE", 1, 3);
        f2l("POLISHED_DIORITE", "STONE", 1, 4);
        f2l("ANDESITE", "STONE", 1, 5);
        f2l("POLISHED_ANDESITE", "STONE", 1, 6);

        f2l("GRASS_BLOCK", "GRASS", 2); // collision

        f2l("DIRT", 3);
        f2l("COARSE_DIRT", "DIRT", 3, 1);
        f2l("PODZOL", "DIRT", 3, 2);

        f2l("COBBLESTONE", 4);

        f2l("OAK_PLANKS", "WOOD", 5, "PLANKS");
        f2l("SPRUCE_PLANKS", "WOOD", 5, 1, "PLANKS");
        f2l("BIRCH_PLANKS", "WOOD", 5, 2, "PLANKS");
        f2l("JUNGLE_PLANKS", "WOOD", 5, 3, "PLANKS");
        f2l("ACACIA_PLANKS", "WOOD", 5, 4, "PLANKS");
        f2l("DARK_OAK_PLANKS", "WOOD", 5, 5, "PLANKS");

        f2l("OAK_SAPLING", "SAPLING", 6);
        f2l("SPRUCE_SAPLING", "SAPLING", 6, 1);
        f2l("BIRCH_SAPLING", "SAPLING", 6, 2);
        f2l("JUNGLE_SAPLING", "SAPLING", 6, 3);
        f2l("ACACIA_SAPLING", "SAPLING", 6, 4);
        f2l("DARK_OAK_SAPLING", "SAPLING", 6, 5);

        f2l("BEDROCK", 7);

        // flowing_water and water can't be properly translated. same thing is for lava
        f2lBlock("WATER", 9); // bukkit mapping collision
        f2lBlock("WATER", "FLOWING_WATER", 10, "STATIONARY_WATER");
        f2lBlock("LAVA", 11); // bukkit mapping collision
        f2lBlock("LAVA", "FLOWING_LAVA", 12, "STATIONARY_LAVA");

        f2l("SAND", 12);
        f2l("RED_SAND", "SAND", 12, 1);

        f2l("GRAVEL", 13);
        f2l("GOLD_ORE", 14);
        f2l("IRON_ORE", 15);
        f2l("COAL_ORE", 16);

        f2l("OAK_LOG", "LOG", 17, 0);
        f2l("SPRUCE_LOG", "LOG", 17, 1);
        f2l("BIRCH_LOG", "LOG", 17, 2);
        f2l("JUNGLE_LOG", "LOG", 17, 3);

        // is this correct?
        f2l("OAK_WOOD", "LOG", 17, 12);
        f2l("SPRUCE_WOOD", "LOG", 17, 13);
        f2l("BIRCH_WOOD", "LOG", 17, 14);
        f2l("JUNGLE_WOOD", "LOG", 17, 15);

        f2l("OAK_LEAVES", "LEAVES", 18, 0);
        f2l("SPRUCE_LEAVES", "LEAVES", 18, 1);
        f2l("BIRCH_LEAVES", "LEAVES", 18, 2);
        f2l("JUNGLE_LEAVES", "LEAVES", 18, 3);

        f2l("SPONGE", 19);
        f2l("WET_SPONGE", "SPONGE", 19, 1);

        f2l("GLASS", 20);
        f2l("LAPIS_ORE", 21);
        f2l("LAPIS_BLOCK", 22);
        f2l("DISPENSER", 23);
        f2l("SANDSTONE", 24);
        f2l("CHISELED_SANDSTONE", "SANDSTONE", 24, 1);
        f2l("CUT_SANDSTONE", "SANDSTONE", 24, 2);

        f2l("NOTE_BLOCK", 25, "NOTEBLOCK");
        f2lBlock("BED", "BED_BLOCK", 26, 0);
        f2l("POWERED_RAIL", 27, "GOLDEN_RAIL");
        f2l("DETECTOR_RAIL", 28);
        f2l("STICKY_PISTON", 29, "PISTON_STICKY_BASE");
        f2l("COBWEB", "WEB", 30);
        f2l("DEAD_BUSH", 32, "DEADBUSH");
        f2l("DEAD_BUSH", "LONG_GRASS", 31, "TALLGRASS"); // shrub == dead_bush but on grass
        f2l("GRASS", "LONG_GRASS", 31, 1, "TALLGRASS"); // collision
        f2l("FERN", "LONG_GRASS", 31, 2, "TALLGRASS");
        f2l("DEAD_BUSH", "LONG_GRASS", 31, 3, "TALLGRASS"); // another shrub == dead_bush?? wtf??
        f2l("PISTON", 33, "PISTON_BASE");
        f2lBlock("PISTON_HEAD", 34, "PISTON_EXTENSION"); // TODO: bukkit mapping collision
        f2lcolored("WOOL", 35);
        f2lBlock("MOVING_PISTON", "PISTON_EXTENSION", 36, 0, "PISTON_MOVING_PIECE"); // TODO: bukkit mapping collision
        f2l("DANDELION", "YELLOW_FLOWER", 37, 0);

        f2l("POPPY", "RED_FLOWER", 38, "RED_ROSE");
        f2l("BLUE_ORCHID", "RED_FLOWER", 38, 1, "RED_ROSE");
        f2l("ALLIUM", "RED_FLOWER", 38, 2, "RED_ROSE");
        f2l("AZURE_BLUET", "RED_FLOWER", 38, 3, "RED_ROSE");
        f2l("RED_TULIP", "RED_FLOWER", 38, 4, "RED_ROSE");
        f2l("ORANGE_TULIP", "RED_FLOWER", 38, 5, "RED_ROSE");
        f2l("WHITE_TULIP", "RED_FLOWER", 38, 6, "RED_ROSE");
        f2l("PINK_TULIP", "RED_FLOWER", 38, 7, "RED_ROSE");
        f2l("OXEYE_DAISY", "RED_FLOWER", 38, 8, "RED_ROSE");

        f2l("BROWN_MUSHROOM", 39);
        f2l("RED_MUSHROOM", 40);
        f2l("GOLD_BLOCK", 41);
        f2l("IRON_BLOCK", 42);

        //f2l("", "DOUBLE_STONE_SLAB", 43, 0, "DOUBLE_STEP"); // TODO: double slabs

        f2l("SMOOTH_STONE_SLAB", "STONE_SLAB", 44, 0, "STEP");
        f2l("SANDSTONE_SLAB", "STONE_SLAB", 44, 1, "STEP");
        f2l("PETRIFIED_OAK_SLAB", "STONE_SLAB", 44, 2, "STEP");
        f2l("COBBLESTONE_SLAB", "STONE_SLAB", 44, 3, "STEP");
        f2l("BRICK_SLAB", "STONE_SLAB", 44, 4, "STEP");
        f2l("STONE_BRICK_SLAB", "STONE_SLAB", 44, 5, "STEP");
        f2l("NETHER_BRICK_SLAB", "STONE_SLAB", 44, 6, "STEP");
        f2l("QUARTZ_SLAB", "STONE_SLAB", 44, 7, "STEP");

        f2l("BRICKS", "BRICK_BLOCK", 45, "BRICK");
        f2l("TNT", 46);
        f2l("BOOKSHELF", 47);
        f2l("MOSSY_COBBLESTONE", 48);
        f2l("OBSIDIAN", 49);
        f2l("TORCH", 50);
        // flattening WALL_TORCH - only block (blocks will be supported later)
        f2lBlock( "FIRE", 51);
        f2l("SPAWNER", "MOB_SPAWNER", 52);
        f2l("OAK_STAIRS", "WOOD_STAIRS", 53);
        f2l("CHEST", 54);
        //f2l("", "REDSTONE_WIRE", 55, 0); - block only
        f2l("DIAMOND_ORE", 56);
        f2l("DIAMOND_BLOCK", 57);
        f2l("CRAFTING_TABLE", 58, "WORKBENCH");
        //f2l("WHEAT", 59, "CROPS");
        f2l("FARMLAND", 60, "SOIL");
        f2l("FURNACE", 61);
        //f2l("", "LIT_FURNACE", 62, 0, "BURNING_FURNACE"); - block only
        //f2l("", "STANDING_SIGN", 63, "SIGN_POST"); - block only
        //f2l("", "WOODEN_DOOR", 64, 0); - block only
        f2l("LADDER", 65);
        f2l("RAIL", 66, "RAILS");
        f2l("COBBLESTONE_STAIRS", 67, "STONE_STAIRS");
        // actually official legacy name STONE_STAIRS is colliding with flattening name of another item
        //f2l("", "WALL_SIGN", 68, 0); - block
        f2l("LEVER", 69);
        f2l("STONE_PRESSURE_PLATE", 70, "STONE_PLATE");
        //f2l("", "IRON_DOOR_BLOCK", 71, 0); - block
        f2l("OAK_PRESSURE_PLATE", "WOODEN_PRESSURE_PLATE", 72, "WOOD_PLATE");
        f2l("REDSTONE_ORE", 73);
        //f2l("", "GLOWING_REDSTONE_ORE", 74, 0); - block
        //f2l("", "REDSTONE_TORCH_OFF", 75, 0); - block
        // Flattening WALL_REDSTONE_TORCH - block
        f2l("REDSTONE_TORCH", "REDSTONE_TORCH_ON", 76);
        f2l("STONE_BUTTON", 77);
        f2l("SNOW", 78, "SNOW_LAYER"); // name snow collision with legacy snow_block namespace
        f2l("ICE", 79);
        f2l("SNOW_BLOCK", 80, "SNOW"); // name snow collision with flattening snow_layer namespace
        f2l("CACTUS", 81);
        f2l("CLAY", 82);
        //f2l("", "REEDS", 83, 0", SUGAR_CANE_BLOCK"); - block
        f2l("JUKEBOX", 84);
        f2l("OAK_FENCE", "FENCE", 85);
        f2l("CARVED_PUMPKIN", "PUMPKIN", 86); // colliding with flattening carved_pumpkin
        f2l("NETHERRACK", 87);
        f2l("SOUL_SAND", 88);
        f2l("GLOWSTONE", 89);
        //f2l("NETHER_PORTAL", "PORTAL", 90); - block
        f2l("JACK_O_LANTERN", 91, "LIT_PUMPKIN");
        //f2l("", "CAKE_BLOCK", 92, 0); - block
        // f2l("", "DIODE_BLOCK_OFF", 93, 0); - block
        //f2l("", "DIODE_BLOCK_ON", 94, 0); - block
        f2lcolored("STAINED_GLASS", 95);
        f2l("OAK_TRAPDOOR", "TRAPDOOR", 96, "TRAP_DOOR");

        // TODO: UNRESOLVABLE COLLISION: can't add official minecraft mapping MONSTER_EGG, colliding with bukkit mapping of spawn eggs which is lower in code
        f2l("INFESTED_STONE", "MONSTER_EGGS", 97, 0);
        f2l("INFESTED_COBBLESTONE", "MONSTER_EGGS", 97, 1);
        f2l("INFESTED_STONE_BRICKS", "MONSTER_EGGS", 97, 2);
        f2l("INFESTED_MOSSY_STONE_BRICKS", "MONSTER_EGGS", 97, 3);
        f2l("INFESTED_CRACKED_STONE_BRICKS", "MONSTER_EGGS", 97, 4);
        f2l("INFESTED_CHISELED_STONE_BRICKS", "MONSTER_EGGS", 97, 5);

        f2l("STONE_BRICKS", "STONEBRICK", 98, 0, "SMOOTH_BRICK");
        f2l("MOSSY_STONE_BRICKS", "STONEBRICK", 98, 1, "SMOOTH_BRICK");
        f2l("CRACKED_STONE_BRICKS", "STONEBRICK", 98, 2, "SMOOTH_BRICK");
        f2l("CHISELED_STONE_BRICKS", "STONEBRICK", 98, 3, "SMOOTH_BRICK");

        f2l("BROWN_MUSHROOM_BLOCK", 99, "HUGE_MUSHROOM_1");
        f2l("RED_MUSHROOM_BLOCK", 100, "HUGE_MUSHROOM_2");
        f2l("IRON_BARS", 101, "IRON_FENCE");
        f2l("GLASS_PANE", 102, "THIN_GLASS");
        f2l("MELON", "MELON_BLOCK", 103); // colliding with melon_slice?
        //f2l("", "PUMPKIN_STEM", 104, 0); - block
        //f2l("", "MELON_STEM", 105, 0); - block
        f2l("VINE", 106);
        f2l("OAK_FENCE_GATE", "FENCE_GATE", 107);
        f2l("BRICK_STAIRS", 108);
        f2l("STONE_BRICK_STAIRS", 109, "SMOOTH_STAIRS");
        f2l("MYCELIUM", 110, "MYCEL");
        f2l("LILY_PAD", "WATERLILY", 111, "WATER_LILY");
        f2l("NETHER_BRICKS", "NETHER_BRICK", 112);
        f2l("NETHER_BRICK_FENCE", 113, "NETHER_FENCE");
        f2l("NETHER_BRICK_STAIRS", 114);
        f2lBlock("NETHER_WART", 115, "NETHER_WARTS");
        f2l("ENCHANTING_TABLE", 116, "ENCHANTMENT_TABLE");
        f2lBlock("BREWING_STAND", 117);
        f2lBlock("CAULDRON", 118);
        f2lBlock("END_PORTAL", 119, "ENDER_PORTAL");
        f2lBlock("END_PORTAL_FRAME", 120, "ENDER_PORTAL_FRAME");
        f2l("END_STONE_BRICKS", "END_BRICKS", 121, "ENDER_STONE");
        f2l("DRAGON_EGG", 122);
        f2l("REDSTONE_LAMP", 123, "REDSTONE_LAMP_OFF");
        //f2l("", "REDSTONE_LAMP_ON", 124, 0); - block
        //f2l("", "WOOD_DOUBLE_STEP", 125, 0); - block

        f2l("OAK_SLAB", "WOODEN_SLAB", 126, 0, "WOOD_STEP");
        f2l("SPRUCE_SLAB", "WOODEN_SLAB", 126, 1, "WOOD_STEP");
        f2l("BIRCH_SLAB", "WOODEN_SLAB", 126, 2, "WOOD_STEP");
        f2l("JUNGLE_SLAB", "WOODEN_SLAB", 126, 3, "WOOD_STEP");
        f2l("ACACIA_SLAB", "WOODEN_SLAB", 126, 4, "WOOD_STEP");
        f2l("DARK_OAK_SLAB", "WOODEN_SLAB", 126, 5, "WOOD_STEP");

        //f2l("", "COCOA", 127, 0); - block
        f2l("SANDSTONE_STAIRS", 128);
        f2l("EMERALD_ORE", 129);
        f2l("ENDER_CHEST", 130);
        f2l("TRIPWIRE_HOOK", 131);
        f2lBlock("TRIPWIRE", 132);
        f2l("EMERALD_BLOCK", 133);
        f2l("SPRUCE_STAIRS", 134, "SPRUCE_WOOD_STAIRS");
        f2l("BIRCH_STAIRS", 135, "BIRCH_WOOD_STAIRS");
        f2l("JUNGLE_STAIRS", 136, "JUNGLE_WOOD_STAIRS");
        f2l("COMMAND_BLOCK", 137, "COMMAND");
        f2l("BEACON", 138);
        f2l("COBBLESTONE_WALL", 139, "COBBLE_WALL");
        f2l("MOSSY_COBBLESTONE_WALL", "COBBLESTONE_WALL", 139, 1, "COBBLE_WALL");
        //f2l("", "FLOWER_POT", 140, 0); - block
        //f2l("", "CARROTS", 141, "CARROT"); - block
        //f2l("", "POTATOES", 142, "POTATO"); - block
        f2l("OAK_BUTTON", "WOODEN_BUTTON", 143, "WOOD_BUTTON");
        //f2l("", "SKULL", 144, 0); - block
        f2l("ANVIL", 145);
        f2l("CHIPPED_ANVIL", "ANVIL", 145, 1);
        f2l("DAMAGED_ANVIL", "ANVIL", 145, 2);
        f2l("TRAPPED_CHEST", 146);
        f2l("LIGHT_WEIGHTED_PRESSURE_PLATE", 147, "GOLD_PLATE");
        f2l("heavy_weighted_pressure_plate", 148, "IRON_PLATE");
        //f2l("", "REDSTONE_COMPARATOR_OFF", 149, 0); - block
        //f2l("", "REDSTONE_COMPARATOR_ON", 150, 0); - block
        f2l("DAYLIGHT_DETECTOR", 151);
        f2l("REDSTONE_BLOCK", 152);
        f2l("NETHER_QUARTZ_ORE", 153, "QUARTZ_ORE");
        f2l("HOPPER", 154);

        f2l("QUARTZ_BLOCK", 155);
        f2l("CHISELED_QUARTZ_BLOCK", "QUARTZ_BLOCK", 155, 1);
        f2l("QUARTZ_PILLAR", "QUARTZ_BLOCK", 155, 2);

        f2l("QUARTZ_STAIRS", 156);
        f2l("ACTIVATOR_RAIL", 157);
        f2l("DROPPER", 158);
        f2lcolored("TERRACOTTA", "STAINED_HARDENED_CLAY", 159, "STAINED_CLAY");
        f2lcolored("STAINED_GLASS_PANE", 160);

        f2l("ACACIA_LEAVES", "LEAVES2", 161, 0, "LEAVES_2");
        f2l("DARK_OAK_LEAVES", "LEAVES2", 161, 1, "LEAVES_2");

        f2l("ACACIA_LOG", "LOG2", 162, 0, "LOG_2");
        f2l("DARK_OAK_LOG", "LOG2", 162, 1, "LOG_2");
        f2l("ACACIA_WOOD", "LOG2", 162, 12, "LOG_2");
        f2l("DARK_OAK_WOOD", "LOG2", 162, 13, "LOG_2");

        f2l("ACACIA_STAIRS", 163);
        f2l("DARK_OAK_STAIRS", 164);

        f2l("SLIME_BLOCK", 165, "SLIME");
        f2l("BARRIER", 166);
        f2l("IRON_TRAPDOOR", 167);

        f2l("PRISMARINE", 168);
        f2l("PRISMARINE_BRICKS", "PRISMARINE", 168, 1);
        f2l("DARK_PRISMARINE", "PRISMARINE", 168, 1);

        f2l("SEA_LANTERN", 169);
        f2l("HAY_BLOCK", 170);
        f2lcolored("CARPET", 171);
        f2l("TERRACOTTA", "hardened_clay", 172, "HARD_CLAY");
        f2l("COAL_BLOCK", 173);
        f2l("PACKED_ICE", 174);

        f2l("SUNFLOWER", "DOUBLE_PLANT", 175, 0);
        f2l("LILAC", "DOUBLE_PLANT", 175, 1);
        f2l("TALL_GRASS", "DOUBLE_PLANT", 175, 2);
        f2l("LARGE_FERN", "DOUBLE_PLANT", 175, 3);
        f2l("ROSE_BUSH", "DOUBLE_PLANT", 175, 4);
        f2l("PEONY", "DOUBLE_PLANT", 175, 5);

        //f2l("", "STANDING_BANNER", 176, 0); - block
        //f2l("", "WALL_BANNER", 177, 0); - block

        //f2l("", "DAYLIGHT_DETECTOR_INVERTED", 178, 0); - block

        f2l("RED_SANDSTONE", 179);
        f2l("CHISELED_RED_SANDSTONE", "RED_SANDSTONE", 179, 1);
        f2l("CUT_RED_SANDSTONE", "RED_SANDSTONE", 179, 2);

        f2l("RED_SANDSTONE_STAIRS", 180);
        //f2l("", "DOUBLE_STONE_SLAB2", 181, 0); - block
        f2l("RED_SANDSTONE_SLAB", "STONE_SLAB2", 182);
        // flattening SMOOTH_RED_SANDSTONE - block in legacy, item in flattening
        f2l("SPRUCE_FENCE_GATE", 183);
        f2l("BIRCH_FENCE_GATE", 184);
        f2l("JUNGLE_FENCE_GATE", 185);
        f2l("DARK_OAK_FENCE_GATE", 186);
        f2l("ACACIA_FENCE_GATE", 187);
        f2l("SPRUCE_FENCE", 188);
        f2l("BIRCH_FENCE", 189);
        f2l("JUNGLE_FENCE", 190);
        f2l("DARK_OAK_FENCE", 191);
        f2l("ACACIA_FENCE", 192);
        //f2l("", "SPRUCE_DOOR", 193, 0); - block
        //f2l("", "BIRCH_DOOR", 194, 0); - block
        //f2l("", "JUNGLE_DOOR", 195, 0); - block
        //f2l("", "ACACIA_DOOR", 196, 0); - block
        //f2l("", "DARK_OAK_DOOR", 197, 0); - block
        f2l("END_ROD", 198);
        f2l("CHORUS_PLANT", 199);
        f2l("CHORUS_FLOWER", 200);
        f2l("PURPUR_BLOCK", 201);
        f2l("PURPUR_PILLAR", 202);
        f2l("PURPUR_STAIRS", 203);
        //f2l("", "PURPUR_DOUBLE_SLAB", 204, 0); - block
        f2l("PURPUR_SLAB", 205);
        f2l("END_BRICKS", 206);
        //f2l("", "BEETROOT_BLOCK", 207, "BEETROOTS"); - block
        f2l("GRASS_PATH", 208);
        //f2l("", "END_GATEWAY", 209, 0); - block
        f2l("REPEATING_COMMAND_BLOCK", 210, "COMMAND_REPEATING");
        f2l("CHAIN_COMMAND_BLOCK", 211, "COMMAND_CHAIN");
        f2l("FROSTED_ICE", 212);
        f2l("MAGMA_BLOCK", "MAGMA", 213);
        f2l("NETHER_WART_BLOCK", 214);
        f2l("RED_NETHER_BRICKS", "RED_NETHER_BRICK", 215);
        f2l("BONE_BLOCK", 216);
        f2l("STRUCTURE_VOID", 217);
        f2l("OBSERVER", 218);
        f2l("WHITE_SHULKER_BOX", 219);
        f2l("ORANGE_SHULKER_BOX", 220);
        f2l("MAGENTA_SHULKER_BOX", 221);
        f2l("LIGHT_BLUE_SHULKER_BOX", 222);
        f2l("YELLOW_SHULKER_BOX", 223);
        f2l("LIME_SHULKER_BOX", 224);
        f2l("PINK_SHULKER_BOX", 225);
        f2l("GRAY_SHULKER_BOX", 226);
        f2l("LIGHT_GRAY_SHULKER_BOX", "SILVER_SHULKER_BOX", 227);
        f2l("CYAN_SHULKER_BOX", 228);
        f2l("PURPLE_SHULKER_BOX", 229);
        f2l("BLUE_SHULKER_BOX", 230);
        f2l("BROWN_SHULKER_BOX", 231);
        f2l("GREEN_SHULKER_BOX", 232);
        f2l("RED_SHULKER_BOX", 233);
        f2l("BLACK_SHULKER_BOX", 234);
        f2l("WHITE_GLAZED_TERRACOTTA", 235);
        f2l("ORANGE_GLAZED_TERRACOTTA", 236);
        f2l("MAGENTA_GLAZED_TERRACOTTA", 237);
        f2l("LIGHT_BLUE_GLAZED_TERRACOTTA", 238);
        f2l("YELLOW_GLAZED_TERRACOTTA", 239);
        f2l("LIME_GLAZED_TERRACOTTA", 240);
        f2l("PINK_GLAZED_TERRACOTTA", 241);
        f2l("GRAY_GLAZED_TERRACOTTA", 242);
        f2l("LIGHT_GRAY_GLAZED_TERRACOTTA", "SILVER_GLAZED_TERRACOTTA", 243);
        f2l("CYAN_GLAZED_TERRACOTTA", 244);
        f2l("PURPLE_GLAZED_TERRACOTTA", 245);
        f2l("BLUE_GLAZED_TERRACOTTA", 246);
        f2l("BROWN_GLAZED_TERRACOTTA", 247);
        f2l("GREEN_GLAZED_TERRACOTTA", 248);
        f2l("RED_GLAZED_TERRACOTTA", 249);
        f2l("BLACK_GLAZED_TERRACOTTA", 250);
        f2lcolored("CONCRETE", 251);
        f2lcolored("CONCRETE_POWDER", 252);
        f2l("STRUCTURE_BLOCK", 255);

        // ITEMS
        f2lItem("IRON_SHOVEL", 256, "IRON_SPADE");
        f2lItem("IRON_PICKAXE", 257);
        f2lItem("IRON_AXE", 258);
        f2lItem("FLINT_AND_STEEL", 259);
        f2lItem("APPLE", 260);
        f2lItem("BOW", 261);
        f2lItem("ARROW", 262);

        f2lItem("COAL", 263);
        f2lItem("CHARCOAL", "COAL", 263, 1);

        f2lItem("DIAMOND", 264);
        f2lItem("IRON_INGOT", 265);
        f2lItem("GOLD_INGOT", 266);
        f2lItem("IRON_SWORD", 267);
        f2lItem("WOODEN_SWORD", 268, "WOOD_SWORD");
        f2lItem("WOODEN_SHOVEL", 269, "WOOD_SPADE");
        f2lItem("WOODEN_PICKAXE", 270, "WOOD_PICKAXE");
        f2lItem("WOODEN_AXE", 271, "WOOD_AXE");
        f2lItem("STONE_SWORD", 272);
        f2lItem("STONE_SHOVEL", 273, "STONE_SPADE");
        f2lItem("STONE_PICKAXE", 274);
        f2lItem("STONE_AXE", 275);
        f2lItem("DIAMOND_SWORD", 276);
        f2lItem("DIAMOND_SHOVEL", 277, "DIAMOND_SPADE");
        f2lItem("DIAMOND_PICKAXE", 278);
        f2lItem("DIAMOND_AXE", 279);
        f2lItem("STICK", 280);
        f2lItem("BOWL", 281);
        f2lItem("MUSHROOM_STEW", 282, "MUSHROOM_SOUP");
        f2lItem("GOLDEN_SWORD", 283, "GOLD_SWORD");
        f2lItem("GOLDEN_SHOVEL", 284, "GOLD_SPADE");
        f2lItem("GOLDEN_PICKAXE", 285, "GOLD_PICKAXE");
        f2lItem("GOLDEN_AXE", 286, "GOLD_AXE");
        f2lItem("STRING", 287);
        f2lItem("FEATHER", 288);
        f2lItem("GUNPOWDER", 289, "SULPHUR");
        f2lItem("WOODEN_HOE", 290, "WOOD_HOE");
        f2lItem("STONE_HOE", 291);
        f2lItem("IRON_HOE", 292);
        f2lItem("DIAMOND_HOE", 293);
        f2lItem("GOLDEN_HOE", 294, "GOLD_HOE");
        f2lItem("WHEAT_SEEDS", 295, "SEEDS");
        f2lItem("WHEAT", 296);
        f2lItem("BREAD", 297);
        f2lItem("LEATHER_HELMET", 298);
        f2lItem("LEATHER_CHESTPLATE", 299);
        f2lItem("LEATHER_LEGGINGS", 300);
        f2lItem("LEATHER_BOOTS", 301);
        f2lItem("CHAINMAIL_HELMET", 302);
        f2lItem("CHAINMAIL_CHESTPLATE", 303);
        f2lItem("CHAINMAIL_LEGGINGS", 304);
        f2lItem("CHAINMAIL_BOOTS", 305);
        f2lItem("IRON_HELMET", 306);
        f2lItem("IRON_CHESTPLATE", 307);
        f2lItem("IRON_LEGGINGS", 308);
        f2lItem("IRON_BOOTS", 309);
        f2lItem("DIAMOND_HELMET", 310);
        f2lItem("DIAMOND_CHESTPLATE", 311);
        f2lItem("DIAMOND_LEGGINGS", 312);
        f2lItem("DIAMOND_BOOTS", 313);
        f2lItem("GOLDEN_HELMET", 314, "GOLD_HELMET");
        f2lItem("GOLDEN_CHESTPLATE", 315, "GOLD_CHESTPLATE");
        f2lItem("GOLDEN_LEGGINGS", 316, "GOLD_LEGGINGS");
        f2lItem("GOLDEN_BOOTS", 317, "GOLD_BOOTS");
        f2lItem("FLINT", 318);
        f2lItem("PORKCHOP", 319, "PORK");
        f2lItem("COOKED_PORKCHOP", 320, "GRILLED_PORK");
        f2lItem("PAINTING", 321);
        f2lItem("GOLDEN_APPLE", 322);
        f2lItem("ENCHANTED_GOLDEN_APPLE", "GOLDEN_APPLE", 322, 1);
        f2lItem("OAK_SIGN", "SIGN", 323);
        f2lItem("OAK_DOOR", "WOODEN_DOOR", 324, "WOOD_DOOR");
        f2lItem("BUCKET", 325);
        f2lItem("WATER_BUCKET", 326);
        f2lItem("LAVA_BUCKET", 327);
        f2lItem("MINECART", 328);
        f2lItem("SADDLE", 329);
        f2lItem("IRON_DOOR", 330);
        f2lItem("REDSTONE", 331);
        f2lItem("SNOWBALL", 332, "SNOW_BALL");
        f2lItem("OAK_BOAT", "BOAT", 333);
        f2lItem("LEATHER", 334);
        f2lItem("MILK_BUCKET", 335);
        f2lItem("BRICK", 336, "CLAY_BRICK");
        f2lItem("CLAY_BALL", 337);
        f2lItem("SUGAR_CANE", 338, "REEDS");
        f2lItem("PAPER", 339);
        f2lItem("BOOK", 340);
        f2lItem("SLIME_BALL", 341);
        f2lItem("CHEST_MINECART", 342, "STORAGE_MINECART");
        f2lItem("FURNACE_MINECART", 343, "POWERED_MINECART");
        f2lItem("EGG", 344);
        f2lItem("COMPASS", 345);
        f2lItem("FISHING_ROD", 346);
        f2lItem("CLOCK", 347, "WATCH");
        f2lItem("GLOWSTONE_DUST", 348);

        f2lItem("COD", "FISH", 349, "RAW_FISH");
        f2lItem("SALMON", "FISH", 349, 1, "RAW_FISH");
        f2lItem("TROPICAL_FISH", "FISH", 349, 2, "RAW_FISH");
        f2lItem("PUFFERFISH", "FISH", 349, 3, "RAW_FISH");
        f2lItem("COOKED_COD", "COOKED_FISH", 350);
        f2lItem("COOKED_SALMON", "COOKED_FISH", 350, 1);

        f2lItem("INK_SACK", 351, "DYE");
        f2lItem("ROSE_RED", "DYE", 351, 1, "INK_SACK");
        f2lItem("CACTUS_GREEN", "DYE", 351, 2, "INK_SACK");
        f2lItem("COCOA_BEANS", "DYE", 351, 3, "INK_SACK");
        f2lItem("LAPIS_LAZULI", "DYE", 351, 4, "INK_SACK");
        f2lItem("PURPLE_DYE", "DYE", 351, 5, "INK_SACK");
        f2lItem("CYAN_DYE", "DYE", 351, 6, "INK_SACK");
        f2lItem("LIGHT_GRAY_DYE", "DYE", 351, 7, "INK_SACK");
        f2lItem("GRAY_DYE", "DYE", 351, 8, "INK_SACK");
        f2lItem("PINK_DYE", "DYE", 351, 9, "INK_SACK");
        f2lItem("LIME_DYE", "DYE", 351, 10, "INK_SACK");
        f2lItem("DANDELION_YELLOW", "DYE", 351, 11, "INK_SACK");
        f2lItem("LIGHT_BLUE_DYE", "DYE", 351, 12, "INK_SACK");
        f2lItem("MAGENTA_DYE", "DYE", 351, 13, "INK_SACK");
        f2lItem("ORANGE_DYE", "DYE", 351, 14, "INK_SACK");
        f2lItem("BONE_MEAL", "DYE", 351, 15, "INK_SACK");

        f2lItem("BONE", 352);
        f2lItem("SUGAR", 353);
        f2lItem("CAKE", 354);

        if (mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            f2lcoloredToNonColored("BED", 355);
        } else {
            f2lcolored("BED", 355);
        }

        f2lItem("REPEATER", 356, "DIODE");
        f2lItem("COOKIE", 357);
        f2lItem("FILLED_MAP", 358, "MAP");
        f2lItem("SHEARS", 359);
        f2lItem("MELON_SLICE", "MELON", 360); // collision
        f2lItem("PUMPKIN_SEEDS", 361);
        f2lItem("MELON_SEEDS", 362);
        f2lItem("BEEF", 363, "RAW_BEEF");
        f2lItem("COOKED_BEEF", 364);
        f2lItem("CHICKEN", 365, "RAW_CHICKEN");
        f2lItem("COOKED_CHICKEN", 366);
        f2lItem("ROTTEN_FLESH", 367);
        f2lItem("ENDER_PEARL", 368);
        f2lItem("BLAZE_ROD", 369);
        f2lItem("GHAST_TEAR", 370);
        f2lItem("GOLD_NUGGET", 371);
        f2lItem("NETHER_WART", 372, "NETHER_STALK");
        f2lItem("POTION", 373);
        f2lItem("GLASS_BOTTLE", 374);
        f2lItem("SPIDER_EYE", 375);
        f2lItem("FERMENTED_SPIDER_EYE", 376);
        f2lItem("BLAZE_POWDER", 377);
        f2lItem("MAGMA_CREAM", 378);
        f2lItem("BREWING_STAND", 379, "BREWING_STAND_ITEM");
        f2lItem("CAULDRON", 380, "CAULDRON_ITEM");
        f2lItem("ENDER_EYE", 381, "EYE_OF_ENDER");
        f2lItem("GLISTERING_MELON_SLICE", 382, "SPECKLED_MELON");

        f2lItem("BAT_SPAWN_EGG", "SPAWN_EGG", 383, 65, "MONSTER_EGG");
        f2lItem("BLAZE_SPAWN_EGG", "SPAWN_EGG", 383, 61, "MONSTER_EGG");
        f2lItem("CAVE_SPIDER_SPAWN_EGG", "SPAWN_EGG", 383, 59, "MONSTER_EGG");
        f2lItem("CHICKEN_SPAWN_EGG", "SPAWN_EGG", 383, 93, "MONSTER_EGG");
        f2lItem("COW_SPAWN_EGG", "SPAWN_EGG", 383, 92, "MONSTER_EGG");
        f2lItem("CREEPER_SPAWN_EGG", "SPAWN_EGG", 383, 50, "MONSTER_EGG");
        f2lItem("DONKEY_SPAWN_EGG", "SPAWN_EGG", 383, 31, "MONSTER_EGG");
        f2lItem("ELDER_GUARDIAN_SPAWN_EGG", "SPAWN_EGG", 383, 4, "MONSTER_EGG");
        f2lItem("ENDERMAN_SPAWN_EGG", "SPAWN_EGG", 383, 58, "MONSTER_EGG");
        f2lItem("ENDERMITE_SPAWN_EGG", "SPAWN_EGG", 383, 67, "MONSTER_EGG");
        f2lItem("EVOKER_SPAWN_EGG", "SPAWN_EGG", 383, 34, "MONSTER_EGG");
        f2lItem("GHAST_SPAWN_EGG", "SPAWN_EGG", 383, 56, "MONSTER_EGG");
        f2lItem("GUARDIAN_SPAWN_EGG", "SPAWN_EGG", 383, 68, "MONSTER_EGG");
        f2lItem("HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 100, "MONSTER_EGG");
        f2lItem("HUSK_SPAWN_EGG", "SPAWN_EGG", 383, 23, "MONSTER_EGG");
        f2lItem("LLAMA_SPAWN_EGG", "SPAWN_EGG", 383, 103, "MONSTER_EGG");
        f2lItem("MAGMA_CUBE_SPAWN_EGG", "SPAWN_EGG", 383, 62, "MONSTER_EGG");
        f2lItem("MOOSHROOM_SPAWN_EGG", "SPAWN_EGG", 383, 96, "MONSTER_EGG");
        f2lItem("MULE_SPAWN_EGG", "SPAWN_EGG", 383, 32, "MONSTER_EGG");
        f2lItem("OCELOT_SPAWN_EGG", "SPAWN_EGG", 383, 98, "MONSTER_EGG");
        f2lItem("PARROT_SPAWN_EGG", "SPAWN_EGG", 383, 105, "MONSTER_EGG");
        f2lItem("PIG_SPAWN_EGG", "SPAWN_EGG", 383, 90, "MONSTER_EGG");
        f2lItem("POLAR_BEAR_SPAWN_EGG", "SPAWN_EGG", 383, 102, "MONSTER_EGG");
        f2lItem("RABBIT_SPAWN_EGG", "SPAWN_EGG", 383, 101, "MONSTER_EGG");
        f2lItem("SHEEP_SPAWN_EGG", "SPAWN_EGG", 383, 91, "MONSTER_EGG");
        f2lItem("SHULKER_SPAWN_EGG", "SPAWN_EGG", 383, 69, "MONSTER_EGG");
        f2lItem("SILVERFISH_SPAWN_EGG", "SPAWN_EGG", 383, 60, "MONSTER_EGG");
        f2lItem("SKELETON_SPAWN_EGG", "SPAWN_EGG", 383, 51, "MONSTER_EGG");
        f2lItem("SKELETON_HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 28, "MONSTER_EGG");
        f2lItem("SLIME_SPAWN_EGG", "SPAWN_EGG", 383, 55, "MONSTER_EGG");
        f2lItem("SPIDER_SPAWN_EGG", "SPAWN_EGG", 383, 52, "MONSTER_EGG");
        f2lItem("SQUID_SPAWN_EGG", "SPAWN_EGG", 383, 94, "MONSTER_EGG");
        f2lItem("STRAY_SPAWN_EGG", "SPAWN_EGG", 383, 6, "MONSTER_EGG");
        f2lItem("VEX_SPAWN_EGG", "SPAWN_EGG", 383, 35, "MONSTER_EGG");
        f2lItem("VILLAGER_SPAWN_EGG", "SPAWN_EGG", 383, 120, "MONSTER_EGG");
        f2lItem("VINDICATOR_SPAWN_EGG", "SPAWN_EGG", 383, 36, "MONSTER_EGG");
        f2lItem("WITCH_SPAWN_EGG", "SPAWN_EGG", 383, 66, "MONSTER_EGG");
        f2lItem("WITHER_SKELETON_SPAWN_EGG", "SPAWN_EGG", 383, 5, "MONSTER_EGG");
        f2lItem("WOLF_SPAWN_EGG", "SPAWN_EGG", 383, 95, "MONSTER_EGG");
        f2lItem("ZOMBIE_SPAWN_EGG", "SPAWN_EGG", 383, 54, "MONSTER_EGG");
        f2lItem("ZOMBIE_HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 29, "MONSTER_EGG");
        f2lItem("ZOMBIE_PIGMAN_SPAWN_EGG", "SPAWN_EGG", 383, 57, "MONSTER_EGG");
        f2lItem("ZOMBIE_VILLAGER_SPAWN_EGG", "SPAWN_EGG", 383, 27, "MONSTER_EGG");

        f2lItem("EXPERIENCE_BOTTLE", 384, "EXP_BOTTLE");
        f2lItem("FIRE_CHARGE", 385, "FIREBALL");
        f2lItem("WRITABLE_BOOK", 386, "BOOK_AND_QUILL");
        f2lItem("WRITTEN_BOOK", 387);
        f2lItem("EMERALD", 388);
        f2lItem("ITEM_FRAME", 389);

        f2lItem("FLOWER_POT", 390, "FLOWER_POT_ITEM");
        f2lItem("CARROT", 391, "CARROT_ITEM");
        f2lItem("POTATO", 392, "POTATO_ITEM");
        f2lItem("BAKED_POTATO", 393);
        f2lItem("POISONOUS_POTATO", 394);
        f2lItem("MAP", "EMPTY_MAP", 395); // official minecraft mapping MAP is colliding with legacy FILLED_MAP
        f2lItem("GOLDEN_CARROT", 396);

        f2lItem("SKELETON_SKULL", "SKULL", 397, "SKULL_ITEM");
        f2lItem("WITHER_SKELETON_SKULL", "SKULL", 397, 1, "SKULL_ITEM");
        f2lItem("ZOMBIE_HEAD", "SKULL", 397, 2, "SKULL_ITEM");
        f2lItem("PLAYER_HEAD", "SKULL", 397, 3, "SKULL_ITEM");
        f2lItem("CREEPER_HEAD", "SKULL", 397, 4, "SKULL_ITEM");
        f2lItem("DRAGON_HEAD", "SKULL", 397, 5, "SKULL_ITEM");

        f2lItem("CARROT_ON_A_STICK", 398, "CARROT_STICK");
        f2lItem("NETHER_STAR", 399);
        f2lItem("PUMPKIN_PIE", 400);
        f2lItem("FIREWORK_ROCKET", "FIREWORKS", 401, "FIREWORK");
        f2lItem("FIREWORK_STAR", "FIREWORK_CHARGE", 402);
        f2lItem("ENCHANTED_BOOK", 403);
        f2lItem("COMPARATOR", 404, "REDSTONE_COMPARATOR");
        f2lItem("NETHER_BRICK", "NETHERBRICK", 405, "NETHER_BRICK_ITEM"); // collision
        f2lItem("QUARTZ", 406);
        f2lItem("TNT_MINECART", 407, "EXPLOSIVE_MINECART");
        f2lItem("HOPPER_MINECART", 408);
        f2lItem("PRISMARINE_SHARD", 409);
        f2lItem("PRISMARINE_CRYSTALS", 410);
        f2lItem("RABBIT", 411);
        f2lItem("COOKED_RABBIT", 412);
        f2lItem("RABBIT_STEW", 413);
        f2lItem("RABBIT_FOOT", 414);
        f2lItem("RABBIT_HIDE", 415);
        f2lItem("ARMOR_STAND", 416);
        f2lItem("IRON_HORSE_ARMOR", 417, "IRON_BARDING");
        f2lItem("GOLDEN_HORSE_ARMOR", 418, "GOLD_BARDING");
        f2lItem("DIAMOND_HORSE_ARMOR", 419, "DIAMOND_BARDING");
        f2lItem("LEAD", 420, "LEASH");
        f2lItem("NAME_TAG", 421);
        f2lItem("COMMAND_BLOCK_MINECART", 422, "COMMAND_MINECART");
        f2lItem("MUTTON", 423);
        f2lItem("COOKED_MUTTON", 424);
        f2lcolored("BANNER", 425);
        f2lItem("END_CRYSTAL", 426);
        f2lItem("SPRUCE_DOOR", 427, "SPRUCE_DOOR_ITEM");
        f2lItem("BIRCH_DOOR", 428, "BIRCH_DOOR_ITEM");
        f2lItem("JUNGLE_DOOR", 429, "JUNGLE_DOOR_ITEM");
        f2lItem("ACACIA_DOOR", 430, "ACACIA_DOOR_ITEM");
        f2lItem("DARK_OAK_DOOR", 431, "DARK_OAK_DOOR_ITEM");
        f2lItem("CHORUS_FRUIT", 432);
        f2lItem("POPPED_CHORUS_FRUIT", "CHORUS_FRUIT_POPPED", 433);
        f2lItem("BEETROOT", 434);
        f2lItem("BEETROOT_SEEDS", 435);
        f2lItem("BEETROOT_SOUP", 436);
        f2lItem("DRAGONS_BREATH", 437);
        f2lItem("SPLASH_POTION", 438);
        f2lItem("SPECTRAL_ARROW", 439);
        f2lItem("TIPPED_ARROW", 440);
        f2lItem("LINGERING_POTION", 441);
        f2lItem("SHIELD", 442);
        f2lItem("ELYTRA", 443);
        f2lItem("SPRUCE_BOAT", 444, "BOAT_SPRUCE");
        f2lItem("BIRCH_BOAT", 445, "BOAT_BIRCH");
        f2lItem("JUNGLE_BOAT", 446, "BOAT_JUNGLE");
        f2lItem("ACACIA_BOAT", 447, "BOAT_ACACIA");
        f2lItem("DARK_OAK_BOAT", 448, "BOAT_DARK_OAK");
        f2lItem("TOTEM_OF_UNDYING", 449, "TOTEM");
        f2lItem("SHULKER_SHELL", 450);
        f2lItem("IRON_NUGGET", 452);
        f2lItem("KNOWLEDGE_BOOK", 453);

        // RECORDS
        f2lItem("MUSIC_DISC_13", "RECORD_13", 2256, "GOLD_RECORD");
        f2lItem("MUSIC_DISC_CAT", "RECORD_CAT", 2257, "GREEN_RECORD");
        f2lItem("MUSIC_DISC_BLOCKS", "RECORD_BLOCKS", 2258, "RECORD_3");
        f2lItem("MUSIC_DISC_CHIRP", "RECORD_CHIRP", 2259, "RECORD_4");
        f2lItem("MUSIC_DISC_FAR", "RECORD_FAR", 2260, "RECORD_5");
        f2lItem("MUSIC_DISC_MALL", "RECORD_MALL", 2261, "RECORD_6");
        f2lItem("MUSIC_DISC_MELLOHI", "RECORD_MELLOHI", 2262, "RECORD_7");
        f2lItem("MUSIC_DISC_STAL", "RECORD_STAL", 2263, "RECORD_8");
        f2lItem("MUSIC_DISC_STRAD", "RECORD_STRAD", 2264, "RECORD_9");
        f2lItem("MUSIC_DISC_WARD", "RECORD_WARD", 2265, "RECORD_10");
        f2lItem("MUSIC_DISC_11", "RECORD_11", 2266);
        f2lItem("MUSIC_DISC_WAIT", "RECORD_WAIT", 2267, "RECORD_12");

    }

    private void flatteningMapping() {
        // Flattening remapping
        mapAliasItem("ZOMBIFIED_PIGLIN_SPAWN_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG");
        mapAlias("SMOOTH_STONE_SLAB", "STONE_SLAB");
        mapAliasItem("GREEN_DYE", "CACTUS_GREEN");
        mapAliasItem("YELLOW_DYE", "DANDELION_YELLOW");
        mapAliasItem("RED_DYE", "ROSE_RED");
        mapAlias("OAK_SIGN", "SIGN");
        mapAlias("BIRCH_SIGN", "SIGN");
        mapAlias("DARK_OAK_SIGN", "SIGN");
        mapAlias("JUNGLE_SIGN", "SIGN");
        mapAlias("SPRUCE_SIGN", "SIGN");
        mapAlias("ACACIA_SIGN", "SIGN");
        mapAlias("OAK_WALL_SIGN", "SIGN");
        mapAlias("BIRCH_WALL_SIGN", "SIGN");
        mapAlias("BIRCH_WALL_SIGN", "SIGN");
        mapAlias("DARK_OAK_WALL_SIGN", "SIGN");
        mapAlias("JUNGLE_WALL_SIGN", "SIGN");
        mapAlias("SPRUCE_WALL_SIGN", "SIGN");
        mapAlias("ACACIA_WALL_SIGN", "SIGN");
        mapAlias("DIRT_PATH", "GRASS_PATH");
    }

    private void mapAlias(String mappingKey, String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
        blockTypeMapper.mapAlias(mappingKey, alias);
    }

    private void mapAliasItem(String mappingKey, String alias) {
        itemTypeMapper.mapAlias(mappingKey, alias);
    }

    private void mapAliasBlock(String mappingKey, String alias) {
        blockTypeMapper.mapAlias(mappingKey, alias);
    }

    private void f2lcolored(String material, int legacyId) {
        f2lcolored(material, material, legacyId);
    }

    private void f2lcolored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lcolored(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    private void f2lcolored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        for (int i = 0; i <= 15; i++) {
            f2l(colors.get(i) + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, i, alternativeLegacyName);
        }
    }

    private void f2lcoloredToNonColored(String material, int legacyId) {
        f2lcoloredToNonColored(material, material, legacyId);
    }

    private void f2lcoloredToNonColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId) {
        f2lcoloredToNonColored(flatteningMaterialSuffix, legacyMaterial, legacyId, null);
    }

    private void f2lcoloredToNonColored(String flatteningMaterialSuffix, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        if (flatteningMaterialSuffix == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both materials mustn't be null!");
        }
        colors.forEach(s ->
                f2l(s + "_" + flatteningMaterialSuffix, legacyMaterial, legacyId, alternativeLegacyName)
        );
    }

    /* For Materials where the name is same */
    private void f2l(String material, int legacyId) {
        f2l(material, material, legacyId);
    }

    private void f2lItem(String material, int legacyId) {
        f2lItem(material, material, legacyId);
    }

    private void f2lBlock(String material, int legacyId) {
        f2lBlock(material, material, legacyId);
    }

    private void f2l(String material, int legacyId, String alternativeLegacyName) {
        f2l(material, material, legacyId, alternativeLegacyName);
    }

    private void f2lItem(String material, int legacyId, String alternativeLegacyName) {
        f2lItem(material, material, legacyId, alternativeLegacyName);
    }

    private void f2lBlock(String material, int legacyId, String alternativeLegacyName) {
        f2lBlock(material, material, legacyId, alternativeLegacyName);
    }

    /* For Materials where the name is changed */
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    private void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    private void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    private void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    private void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    /* For Materials where the name is changed and data is not zero*/
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    private void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    private void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        f2lBlock(flatteningMaterial, legacyMaterial, legacyId, data, alternativeLegacyName);
        f2lItem(flatteningMaterial, legacyMaterial, legacyId, data, alternativeLegacyName);
    }

    private void f2lItem(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = NamespacedMappingKey.of(flatteningMaterial);
        var legacyMaterialNamespaced = NamespacedMappingKey.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? NamespacedMappingKey.of(alternativeLegacyName) : null;

        var mapping = itemTypeMapper.getUNSAFE_mapping();
        ItemTypeHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).withDurability((short) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced)) {
                    mapping.put(alternativeLegacyNamespaced, holder);
                }
                if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
                }
            } else if (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)) {
                holder = mapping.get(alternativeLegacyNamespaced).withDurability((short) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced)) {
                    mapping.put(legacyMaterialNamespaced, holder);
                }
                if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
                }
            }
        } else if (!platform.isUsingLegacyNames() && mapping.containsKey(flatteningMaterialNamespaced)) {
            holder = mapping.get(flatteningMaterialNamespaced);
            if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                mapping.put(legacyMaterialNamespaced, holder);
            }
            if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
            }
            if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced) && !flatteningMaterialNamespaced.equals(alternativeLegacyNamespaced)) {
                mapping.put(alternativeLegacyNamespaced, holder);
            }
            if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
            }
        }

        if (holder != null) {
            var legacyIdKey = NumericMappingKey.of(legacyId);
            if (!mapping.containsKey(legacyIdKey) && data == 0) {
                mapping.put(legacyIdKey, holder);
            }
            var legacyIdDataKey = ComplexMappingKey.of(legacyIdKey, NumericMappingKey.of(data));
            if (!mapping.containsKey(legacyIdDataKey)) {
                mapping.put(legacyIdDataKey, holder);
            }
        }
    }


    private void f2lBlock(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = NamespacedMappingKey.of(flatteningMaterial);
        var legacyMaterialNamespaced = NamespacedMappingKey.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? NamespacedMappingKey.of(alternativeLegacyName) : null;

        var mapping = blockTypeMapper.getUNSAFE_mapping();
        BlockTypeHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced)) {
                    mapping.put(alternativeLegacyNamespaced, holder);
                }
                if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
                }
            } else if (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)) {
                holder = mapping.get(alternativeLegacyNamespaced).withLegacyData((byte) data);
                if (!mapping.containsKey(flatteningMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                    mapping.put(flatteningMaterialNamespaced, holder);
                }
                if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced)) {
                    mapping.put(legacyMaterialNamespaced, holder);
                }
                if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                    mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
                }
            }
        } else if (!platform.isUsingLegacyNames() && mapping.containsKey(flatteningMaterialNamespaced)) {
            holder = mapping.get(flatteningMaterialNamespaced);
            if (data == 0 && !mapping.containsKey(legacyMaterialNamespaced) && !flatteningMaterialNamespaced.equals(legacyMaterialNamespaced)) {
                mapping.put(legacyMaterialNamespaced, holder);
            }
            if (!mapping.containsKey(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(legacyMaterialNamespaced, NumericMappingKey.of(data)), holder);
            }
            if (data == 0 && alternativeLegacyNamespaced != null && !mapping.containsKey(alternativeLegacyNamespaced) && !flatteningMaterialNamespaced.equals(alternativeLegacyNamespaced)) {
                mapping.put(alternativeLegacyNamespaced, holder);
            }
            if (alternativeLegacyNamespaced != null && !mapping.containsKey(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)))) {
                mapping.put(ComplexMappingKey.of(alternativeLegacyNamespaced, NumericMappingKey.of(data)), holder);
            }
        }

        if (holder != null) {
            var legacyIdKey = NumericMappingKey.of(legacyId);
            if (!mapping.containsKey(legacyIdKey) && data == 0) {
                mapping.put(legacyIdKey, holder);
            }
            var legacyIdDataKey = ComplexMappingKey.of(legacyIdKey, NumericMappingKey.of(data));
            if (!mapping.containsKey(legacyIdDataKey)) {
                mapping.put(legacyIdDataKey, holder);
            }
        }
    }

}