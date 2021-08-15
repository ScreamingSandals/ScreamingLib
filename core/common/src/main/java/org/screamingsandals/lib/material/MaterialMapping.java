package org.screamingsandals.lib.material;

import lombok.Getter;
import lombok.SneakyThrows;
import org.screamingsandals.lib.utils.BidirectionalConverter;
import org.screamingsandals.lib.utils.Platform;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ide.CustomAutocompletion;
import org.screamingsandals.lib.utils.annotations.ide.OfMethodAlternative;
import org.screamingsandals.lib.utils.key.ComplexMappingKey;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;
import org.screamingsandals.lib.utils.key.NumericMappingKey;
import org.screamingsandals.lib.utils.mapper.AbstractTypeMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@SuppressWarnings("AlternativeMethodAvailable")
@AbstractService
public abstract class MaterialMapping extends AbstractTypeMapper<MaterialHolder> {
    @Getter
    protected Platform platform;
    protected final List<MappingFlags> mappingFlags = new ArrayList<>();
    protected BidirectionalConverter<MaterialHolder> materialConverter = BidirectionalConverter.<MaterialHolder>build()
            .registerW2P(String.class, MaterialHolder::getPlatformName)
            .registerP2W(MaterialHolder.class, e -> e);
    protected Map<Predicate<MaterialHolder>, Function<String, Optional<MaterialHolder>>> colorable = new HashMap<>();

    private static MaterialMapping materialMapping = null;
    private static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(((?<namespaced>(?:([A-Za-z][A-Za-z0-9_.\\-]*):)?[A-Za-z][A-Za-z0-9_.\\-/ ]*)(?::)?(?<durability>\\d+)?)|((?<id>\\d+)(?::)?(?<data>\\d+)?))$");
    private static final List<String> colors = List.of(
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

    @CustomAutocompletion(CustomAutocompletion.Type.MATERIAL)
    @OfMethodAlternative(value = MaterialHolder.class, methodName = "ofOptional")
    public static Optional<MaterialHolder> resolve(Object materialObject) {
        if (materialMapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        if (materialObject == null) {
            return Optional.empty();
        }

        return materialMapping.materialConverter.convertOptional(materialObject).or(() -> {
            var material = materialObject.toString().trim();

            var matcher = RESOLUTION_PATTERN.matcher(material);

            if (matcher.matches()) {
                if (matcher.group("namespaced") != null) {

                    var namespaced = NamespacedMappingKey.of(matcher.group("namespaced"));

                    Integer data = null;
                    try {
                        data = Integer.parseInt(matcher.group("durability"));
                    } catch (NumberFormatException ignored) {
                    }

                    if (data != null) {
                        var namespacedDurability = ComplexMappingKey.of(namespaced, NumericMappingKey.of(data));

                        if (materialMapping.mapping.containsKey(namespacedDurability)) {
                            return Optional.of(materialMapping.mapping.get(namespacedDurability));
                        } else if (materialMapping.mapping.containsKey(namespaced)) {
                            MaterialHolder holder = materialMapping.mapping.get(namespaced);
                            return Optional.of(holder.newDurability(data));
                        }
                    } else if (materialMapping.mapping.containsKey(namespaced)) {
                        return Optional.of(materialMapping.mapping.get(namespaced));
                    }
                } else if (matcher.group("id") != null) {
                    try {
                        var id = Integer.parseInt(matcher.group("id"));
                        int data = 0;
                        try {
                            data = Integer.parseInt(matcher.group("data"));
                        } catch (NumberFormatException ignored) {
                        }

                        var keyWithData = ComplexMappingKey.of(NumericMappingKey.of(id), NumericMappingKey.of(data));
                        var key = NumericMappingKey.of(id);

                        if (materialMapping.mapping.containsKey(keyWithData)) {
                            return Optional.of(materialMapping.mapping.get(keyWithData));
                        } else if (materialMapping.mapping.containsKey(key)) {
                            MaterialHolder holder = materialMapping.mapping.get(key);
                            return Optional.of(holder.newDurability(data));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            return Optional.empty();
        });
    }

    public static MaterialHolder colorize(MaterialHolder holder, String color) {
        if (materialMapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        return materialMapping.colorable.entrySet().stream()
                .filter(c -> c.getKey().test(holder))
                .map(Map.Entry::getValue)
                .findFirst()
                .flatMap(fun -> fun.apply(color))
                .orElse(holder);
    }

    public static <T> T convertMaterialHolder(MaterialHolder holder, Class<T> newType) {
        if (materialMapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        return materialMapping.materialConverter.convert(holder, newType);
    }

    @SneakyThrows
    public static void init(Supplier<MaterialMapping> materialMappingSupplier) {
        if (materialMapping != null) {
            throw new UnsupportedOperationException("Material mapping is already initialized.");
        }

        materialMapping = materialMappingSupplier.get();

        /*
        if server is running Java Edition Post-Flattening version, flattening remappings have to been applied first
        on legacy versions you had to run it after the legacy

        The reason why the order is important is due to often renaming java flattening names
        */
        if (materialMapping.getPlatform() == Platform.JAVA_FLATTENING) {
            materialMapping.flatteningMapping();
        }

        if (materialMapping.getPlatform().name().startsWith("JAVA")) {
            materialMapping.flatteningLegacyMappingJava();

            if (materialMapping.getPlatform() != Platform.JAVA_FLATTENING) {
                materialMapping.flatteningMapping();
            }

            materialMapping.javaAutoColorable();
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
        var list = new ArrayList<MaterialHolder>();
        colors.forEach(s -> resolve(s + "_" + baseName).ifPresent(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        }));

        resolve(notColoredName).ifPresent(materialHolder -> {
            if (!list.contains(materialHolder)) {
                list.add(materialHolder);
            }
        });

        if (!list.isEmpty()) { // if list is empty, we don't have this material
            colorable.put(list::contains, s -> {
                if (colors.contains(s.toUpperCase())) {
                    return resolve(s.toUpperCase() + "_" + baseName);
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
        //f2l("WATER", 9); - only block (blocks will be supported later), bukkit mapping collision
        //f2l("LAVA", 11); - only block (blocks will be supported later), bukkit mapping collision

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
        //f2l("", "BED_BLOCK", 26, 0); - only block (blocks will be supported later), bukkit mapping collision
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
        //f2l("PISTON_HEAD", 34, "PISTON_EXTENSION"); - only block (blocks will be supported later), bukkit mapping collision
        f2lcolored("WOOL", 35);
        //f2l("MOVING_PISTON", "PISTON_EXTENSION", 36, 0, "PISTON_MOVING_PIECE"); - only block (blocks will be supported later), bukkit mapping collision
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

        //f2l("", "DOUBLE_STONE_SLAB", 43, 0, "DOUBLE_STEP"); - only block (blocks will be supported later)

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
        //f2l( "FIRE", 51); - only block (blocks will be supported later)
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

        // UNRESOLVABLE COLLISION: can't add official minecraft mapping MONSTER_EGG, colliding with bukkit mapping of spawn eggs which is lower in code
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
        //f2l("NETHER_WART", 115, "NETHER_WARTS"); - block
        f2l("ENCHANTING_TABLE", 116, "ENCHANTMENT_TABLE");
        // f2l("BREWING_STAND", 117);- block
        //f2l("CAULDRON", 118); - block
        //f2l("END_PORTAL", 119, "ENDER_PORTAL"); - block
        //f2l("END_PORTAL_FRAME", 120, "ENDER_PORTAL_FRAME"); - block
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
        //f2l("TRIPWIRE", 132); - block
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
        f2l("IRON_SHOVEL", 256, "IRON_SPADE");
        f2l("IRON_PICKAXE", 257);
        f2l("IRON_AXE", 258);
        f2l("FLINT_AND_STEEL", 259);
        f2l("APPLE", 260);
        f2l("BOW", 261);
        f2l("ARROW", 262);

        f2l("COAL", 263);
        f2l("CHARCOAL", "COAL", 263, 1);

        f2l("DIAMOND", 264);
        f2l("IRON_INGOT", 265);
        f2l("GOLD_INGOT", 266);
        f2l("IRON_SWORD", 267);
        f2l("WOODEN_SWORD", 268, "WOOD_SWORD");
        f2l("WOODEN_SHOVEL", 269, "WOOD_SPADE");
        f2l("WOODEN_PICKAXE", 270, "WOOD_PICKAXE");
        f2l("WOODEN_AXE", 271, "WOOD_AXE");
        f2l("STONE_SWORD", 272);
        f2l("STONE_SHOVEL", 273, "STONE_SPADE");
        f2l("STONE_PICKAXE", 274);
        f2l("STONE_AXE", 275);
        f2l("DIAMOND_SWORD", 276);
        f2l("DIAMOND_SHOVEL", 277, "DIAMOND_SPADE");
        f2l("DIAMOND_PICKAXE", 278);
        f2l("DIAMOND_AXE", 279);
        f2l("STICK", 280);
        f2l("BOWL", 281);
        f2l("MUSHROOM_STEW", 282, "MUSHROOM_SOUP");
        f2l("GOLDEN_SWORD", 283, "GOLD_SWORD");
        f2l("GOLDEN_SHOVEL", 284, "GOLD_SPADE");
        f2l("GOLDEN_PICKAXE", 285, "GOLD_PICKAXE");
        f2l("GOLDEN_AXE", 286, "GOLD_AXE");
        f2l("STRING", 287);
        f2l("FEATHER", 288);
        f2l("GUNPOWDER", 289, "SULPHUR");
        f2l("WOODEN_HOE", 290, "WOOD_HOE");
        f2l("STONE_HOE", 291);
        f2l("IRON_HOE", 292);
        f2l("DIAMOND_HOE", 293);
        f2l("GOLDEN_HOE", 294, "GOLD_HOE");
        f2l("WHEAT_SEEDS", 295, "SEEDS");
        f2l("WHEAT", 296);
        f2l("BREAD", 297);
        f2l("LEATHER_HELMET", 298);
        f2l("LEATHER_CHESTPLATE", 299);
        f2l("LEATHER_LEGGINGS", 300);
        f2l("LEATHER_BOOTS", 301);
        f2l("CHAINMAIL_HELMET", 302);
        f2l("CHAINMAIL_CHESTPLATE", 303);
        f2l("CHAINMAIL_LEGGINGS", 304);
        f2l("CHAINMAIL_BOOTS", 305);
        f2l("IRON_HELMET", 306);
        f2l("IRON_CHESTPLATE", 307);
        f2l("IRON_LEGGINGS", 308);
        f2l("IRON_BOOTS", 309);
        f2l("DIAMOND_HELMET", 310);
        f2l("DIAMOND_CHESTPLATE", 311);
        f2l("DIAMOND_LEGGINGS", 312);
        f2l("DIAMOND_BOOTS", 313);
        f2l("GOLDEN_HELMET", 314, "GOLD_HELMET");
        f2l("GOLDEN_CHESTPLATE", 315, "GOLD_CHESTPLATE");
        f2l("GOLDEN_LEGGINGS", 316, "GOLD_LEGGINGS");
        f2l("GOLDEN_BOOTS", 317, "GOLD_BOOTS");
        f2l("FLINT", 318);
        f2l("PORKCHOP", 319, "PORK");
        f2l("COOKED_PORKCHOP", 320, "GRILLED_PORK");
        f2l("PAINTING", 321);
        f2l("GOLDEN_APPLE", 322);
        f2l("ENCHANTED_GOLDEN_APPLE", "GOLDEN_APPLE", 322, 1);
        f2l("OAK_SIGN", "SIGN", 323);
        f2l("OAK_DOOR", "WOODEN_DOOR", 324, "WOOD_DOOR");
        f2l("BUCKET", 325);
        f2l("WATER_BUCKET", 326);
        f2l("LAVA_BUCKET", 327);
        f2l("MINECART", 328);
        f2l("SADDLE", 329);
        f2l("IRON_DOOR", 330);
        f2l("REDSTONE", 331);
        f2l("SNOWBALL", 332, "SNOW_BALL");
        f2l("OAK_BOAT", "BOAT", 333);
        f2l("LEATHER", 334);
        f2l("MILK_BUCKET", 335);
        f2l("BRICK", 336, "CLAY_BRICK");
        f2l("CLAY_BALL", 337);
        f2l("SUGAR_CANE", 338, "REEDS");
        f2l("PAPER", 339);
        f2l("BOOK", 340);
        f2l("SLIME_BALL", 341);
        f2l("CHEST_MINECART", 342, "STORAGE_MINECART");
        f2l("FURNACE_MINECART", 343, "POWERED_MINECART");
        f2l("EGG", 344);
        f2l("COMPASS", 345);
        f2l("FISHING_ROD", 346);
        f2l("CLOCK", 347, "WATCH");
        f2l("GLOWSTONE_DUST", 348);

        f2l("COD", "FISH", 349, "RAW_FISH");
        f2l("SALMON", "FISH", 349, 1, "RAW_FISH");
        f2l("TROPICAL_FISH", "FISH", 349, 2, "RAW_FISH");
        f2l("PUFFERFISH", "FISH", 349, 3, "RAW_FISH");
        f2l("COOKED_COD", "COOKED_FISH", 350);
        f2l("COOKED_SALMON", "COOKED_FISH", 350, 1);

        f2l("INK_SACK", 351, "DYE");
        f2l("ROSE_RED", "DYE", 351, 1, "INK_SACK");
        f2l("CACTUS_GREEN", "DYE", 351, 2, "INK_SACK");
        f2l("COCOA_BEANS", "DYE", 351, 3, "INK_SACK");
        f2l("LAPIS_LAZULI", "DYE", 351, 4, "INK_SACK");
        f2l("PURPLE_DYE", "DYE", 351, 5, "INK_SACK");
        f2l("CYAN_DYE", "DYE", 351, 6, "INK_SACK");
        f2l("LIGHT_GRAY_DYE", "DYE", 351, 7, "INK_SACK");
        f2l("GRAY_DYE", "DYE", 351, 8, "INK_SACK");
        f2l("PINK_DYE", "DYE", 351, 9, "INK_SACK");
        f2l("LIME_DYE", "DYE", 351, 10, "INK_SACK");
        f2l("DANDELION_YELLOW", "DYE", 351, 11, "INK_SACK");
        f2l("LIGHT_BLUE_DYE", "DYE", 351, 12, "INK_SACK");
        f2l("MAGENTA_DYE", "DYE", 351, 13, "INK_SACK");
        f2l("ORANGE_DYE", "DYE", 351, 14, "INK_SACK");
        f2l("BONE_MEAL", "DYE", 351, 15, "INK_SACK");

        f2l("BONE", 352);
        f2l("SUGAR", 353);
        f2l("CAKE", 354);

        if (mappingFlags.contains(MappingFlags.NO_COLORED_BEDS)) {
            f2lcoloredToNonColored("BED", 355);
        } else {
            f2lcolored("BED", 355);
        }

        f2l("REPEATER", 356, "DIODE");
        f2l("COOKIE", 357);
        f2l("FILLED_MAP", 358, "MAP");
        f2l("SHEARS", 359);
        f2l("MELON_SLICE", "MELON", 360); // collision
        f2l("PUMPKIN_SEEDS", 361);
        f2l("MELON_SEEDS", 362);
        f2l("BEEF", 363, "RAW_BEEF");
        f2l("COOKED_BEEF", 364);
        f2l("CHICKEN", 365, "RAW_CHICKEN");
        f2l("COOKED_CHICKEN", 366);
        f2l("ROTTEN_FLESH", 367);
        f2l("ENDER_PEARL", 368);
        f2l("BLAZE_ROD", 369);
        f2l("GHAST_TEAR", 370);
        f2l("GOLD_NUGGET", 371);
        f2l("NETHER_WART", 372, "NETHER_STALK");
        f2l("POTION", 373);
        f2l("GLASS_BOTTLE", 374);
        f2l("SPIDER_EYE", 375);
        f2l("FERMENTED_SPIDER_EYE", 376);
        f2l("BLAZE_POWDER", 377);
        f2l("MAGMA_CREAM", 378);
        f2l("BREWING_STAND", 379, "BREWING_STAND_ITEM");
        f2l("CAULDRON", 380, "CAULDRON_ITEM");
        f2l("ENDER_EYE", 381, "EYE_OF_ENDER");
        f2l("GLISTERING_MELON_SLICE", 382, "SPECKLED_MELON");

        f2l("BAT_SPAWN_EGG", "SPAWN_EGG", 383, 65, "MONSTER_EGG");
        f2l("BLAZE_SPAWN_EGG", "SPAWN_EGG", 383, 61, "MONSTER_EGG");
        f2l("CAVE_SPIDER_SPAWN_EGG", "SPAWN_EGG", 383, 59, "MONSTER_EGG");
        f2l("CHICKEN_SPAWN_EGG", "SPAWN_EGG", 383, 93, "MONSTER_EGG");
        f2l("COW_SPAWN_EGG", "SPAWN_EGG", 383, 92, "MONSTER_EGG");
        f2l("CREEPER_SPAWN_EGG", "SPAWN_EGG", 383, 50, "MONSTER_EGG");
        f2l("DONKEY_SPAWN_EGG", "SPAWN_EGG", 383, 31, "MONSTER_EGG");
        f2l("ELDER_GUARDIAN_SPAWN_EGG", "SPAWN_EGG", 383, 4, "MONSTER_EGG");
        f2l("ENDERMAN_SPAWN_EGG", "SPAWN_EGG", 383, 58, "MONSTER_EGG");
        f2l("ENDERMITE_SPAWN_EGG", "SPAWN_EGG", 383, 67, "MONSTER_EGG");
        f2l("EVOKER_SPAWN_EGG", "SPAWN_EGG", 383, 34, "MONSTER_EGG");
        f2l("GHAST_SPAWN_EGG", "SPAWN_EGG", 383, 56, "MONSTER_EGG");
        f2l("GUARDIAN_SPAWN_EGG", "SPAWN_EGG", 383, 68, "MONSTER_EGG");
        f2l("HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 100, "MONSTER_EGG");
        f2l("HUSK_SPAWN_EGG", "SPAWN_EGG", 383, 23, "MONSTER_EGG");
        f2l("LLAMA_SPAWN_EGG", "SPAWN_EGG", 383, 103, "MONSTER_EGG");
        f2l("MAGMA_CUBE_SPAWN_EGG", "SPAWN_EGG", 383, 62, "MONSTER_EGG");
        f2l("MOOSHROOM_SPAWN_EGG", "SPAWN_EGG", 383, 96, "MONSTER_EGG");
        f2l("MULE_SPAWN_EGG", "SPAWN_EGG", 383, 32, "MONSTER_EGG");
        f2l("OCELOT_SPAWN_EGG", "SPAWN_EGG", 383, 98, "MONSTER_EGG");
        f2l("PARROT_SPAWN_EGG", "SPAWN_EGG", 383, 105, "MONSTER_EGG");
        f2l("PIG_SPAWN_EGG", "SPAWN_EGG", 383, 90, "MONSTER_EGG");
        f2l("POLAR_BEAR_SPAWN_EGG", "SPAWN_EGG", 383, 102, "MONSTER_EGG");
        f2l("RABBIT_SPAWN_EGG", "SPAWN_EGG", 383, 101, "MONSTER_EGG");
        f2l("SHEEP_SPAWN_EGG", "SPAWN_EGG", 383, 91, "MONSTER_EGG");
        f2l("SHULKER_SPAWN_EGG", "SPAWN_EGG", 383, 69, "MONSTER_EGG");
        f2l("SILVERFISH_SPAWN_EGG", "SPAWN_EGG", 383, 60, "MONSTER_EGG");
        f2l("SKELETON_SPAWN_EGG", "SPAWN_EGG", 383, 51, "MONSTER_EGG");
        f2l("SKELETON_HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 28, "MONSTER_EGG");
        f2l("SLIME_SPAWN_EGG", "SPAWN_EGG", 383, 55, "MONSTER_EGG");
        f2l("SPIDER_SPAWN_EGG", "SPAWN_EGG", 383, 52, "MONSTER_EGG");
        f2l("SQUID_SPAWN_EGG", "SPAWN_EGG", 383, 94, "MONSTER_EGG");
        f2l("STRAY_SPAWN_EGG", "SPAWN_EGG", 383, 6, "MONSTER_EGG");
        f2l("VEX_SPAWN_EGG", "SPAWN_EGG", 383, 35, "MONSTER_EGG");
        f2l("VILLAGER_SPAWN_EGG", "SPAWN_EGG", 383, 120, "MONSTER_EGG");
        f2l("VINDICATOR_SPAWN_EGG", "SPAWN_EGG", 383, 36, "MONSTER_EGG");
        f2l("WITCH_SPAWN_EGG", "SPAWN_EGG", 383, 66, "MONSTER_EGG");
        f2l("WITHER_SKELETON_SPAWN_EGG", "SPAWN_EGG", 383, 5, "MONSTER_EGG");
        f2l("WOLF_SPAWN_EGG", "SPAWN_EGG", 383, 95, "MONSTER_EGG");
        f2l("ZOMBIE_SPAWN_EGG", "SPAWN_EGG", 383, 54, "MONSTER_EGG");
        f2l("ZOMBIE_HORSE_SPAWN_EGG", "SPAWN_EGG", 383, 29, "MONSTER_EGG");
        f2l("ZOMBIE_PIGMAN_SPAWN_EGG", "SPAWN_EGG", 383, 57, "MONSTER_EGG");
        f2l("ZOMBIE_VILLAGER_SPAWN_EGG", "SPAWN_EGG", 383, 27, "MONSTER_EGG");

        f2l("EXPERIENCE_BOTTLE", 384, "EXP_BOTTLE");
        f2l("FIRE_CHARGE", 385, "FIREBALL");
        f2l("WRITABLE_BOOK", 386, "BOOK_AND_QUILL");
        f2l("WRITTEN_BOOK", 387);
        f2l("EMERALD", 388);
        f2l("ITEM_FRAME", 389);

        f2l("FLOWER_POT", 390, "FLOWER_POT_ITEM");
        f2l("CARROT", 391, "CARROT_ITEM");
        f2l("POTATO", 392, "POTATO_ITEM");
        f2l("BAKED_POTATO", 393);
        f2l("POISONOUS_POTATO", 394);
        f2l("MAP", "EMPTY_MAP", 395); // official minecraft mapping MAP is colliding with legacy FILLED_MAP
        f2l("GOLDEN_CARROT", 396);

        f2l("SKELETON_SKULL", "SKULL", 397, "SKULL_ITEM");
        f2l("WITHER_SKELETON_SKULL", "SKULL", 397, 1, "SKULL_ITEM");
        f2l("ZOMBIE_HEAD", "SKULL", 397, 2, "SKULL_ITEM");
        f2l("PLAYER_HEAD", "SKULL", 397, 3, "SKULL_ITEM");
        f2l("CREEPER_HEAD", "SKULL", 397, 4, "SKULL_ITEM");
        f2l("DRAGON_HEAD", "SKULL", 397, 5, "SKULL_ITEM");

        f2l("CARROT_ON_A_STICK", 398, "CARROT_STICK");
        f2l("NETHER_STAR", 399);
        f2l("PUMPKIN_PIE", 400);
        f2l("FIREWORK_ROCKET", "FIREWORKS", 401, "FIREWORK");
        f2l("FIREWORK_STAR", "FIREWORK_CHARGE", 402);
        f2l("ENCHANTED_BOOK", 403);
        f2l("COMPARATOR", 404, "REDSTONE_COMPARATOR");
        f2l("NETHER_BRICK", "NETHERBRICK", 405, "NETHER_BRICK_ITEM"); // collision
        f2l("QUARTZ", 406);
        f2l("TNT_MINECART", 407, "EXPLOSIVE_MINECART");
        f2l("HOPPER_MINECART", 408);
        f2l("PRISMARINE_SHARD", 409);
        f2l("PRISMARINE_CRYSTALS", 410);
        f2l("RABBIT", 411);
        f2l("COOKED_RABBIT", 412);
        f2l("RABBIT_STEW", 413);
        f2l("RABBIT_FOOT", 414);
        f2l("RABBIT_HIDE", 415);
        f2l("ARMOR_STAND", 416);
        f2l("IRON_HORSE_ARMOR", 417, "IRON_BARDING");
        f2l("GOLDEN_HORSE_ARMOR", 418, "GOLD_BARDING");
        f2l("DIAMOND_HORSE_ARMOR", 419, "DIAMOND_BARDING");
        f2l("LEAD", 420, "LEASH");
        f2l("NAME_TAG", 421);
        f2l("COMMAND_BLOCK_MINECART", 422, "COMMAND_MINECART");
        f2l("MUTTON", 423);
        f2l("COOKED_MUTTON", 424);
        f2lcolored("BANNER", 425);
        f2l("END_CRYSTAL", 426);
        f2l("SPRUCE_DOOR", 427, "SPRUCE_DOOR_ITEM");
        f2l("BIRCH_DOOR", 428, "BIRCH_DOOR_ITEM");
        f2l("JUNGLE_DOOR", 429, "JUNGLE_DOOR_ITEM");
        f2l("ACACIA_DOOR", 430, "ACACIA_DOOR_ITEM");
        f2l("DARK_OAK_DOOR", 431, "DARK_OAK_DOOR_ITEM");
        f2l("CHORUS_FRUIT", 432);
        f2l("POPPED_CHORUS_FRUIT", "CHORUS_FRUIT_POPPED", 433);
        f2l("BEETROOT", 434);
        f2l("BEETROOT_SEEDS", 435);
        f2l("BEETROOT_SOUP", 436);
        f2l("DRAGONS_BREATH", 437);
        f2l("SPLASH_POTION", 438);
        f2l("SPECTRAL_ARROW", 439);
        f2l("TIPPED_ARROW", 440);
        f2l("LINGERING_POTION", 441);
        f2l("SHIELD", 442);
        f2l("ELYTRA", 443);
        f2l("SPRUCE_BOAT", 444, "BOAT_SPRUCE");
        f2l("BIRCH_BOAT", 445, "BOAT_BIRCH");
        f2l("JUNGLE_BOAT", 446, "BOAT_JUNGLE");
        f2l("ACACIA_BOAT", 447, "BOAT_ACACIA");
        f2l("DARK_OAK_BOAT", 448, "BOAT_DARK_OAK");
        f2l("TOTEM_OF_UNDYING", 449, "TOTEM");
        f2l("SHULKER_SHELL", 450);
        f2l("IRON_NUGGET", 452);
        f2l("KNOWLEDGE_BOOK", 453);

        // RECORDS
        f2l("MUSIC_DISC_13", "RECORD_13", 2256, "GOLD_RECORD");
        f2l("MUSIC_DISC_CAT", "RECORD_CAT", 2257, "GREEN_RECORD");
        f2l("MUSIC_DISC_BLOCKS", "RECORD_BLOCKS", 2258, "RECORD_3");
        f2l("MUSIC_DISC_CHIRP", "RECORD_CHIRP", 2259, "RECORD_4");
        f2l("MUSIC_DISC_FAR", "RECORD_FAR", 2260, "RECORD_5");
        f2l("MUSIC_DISC_MALL", "RECORD_MALL", 2261, "RECORD_6");
        f2l("MUSIC_DISC_MELLOHI", "RECORD_MELLOHI", 2262, "RECORD_7");
        f2l("MUSIC_DISC_STAL", "RECORD_STAL", 2263, "RECORD_8");
        f2l("MUSIC_DISC_STRAD", "RECORD_STRAD", 2264, "RECORD_9");
        f2l("MUSIC_DISC_WARD", "RECORD_WARD", 2265, "RECORD_10");
        f2l("MUSIC_DISC_11", "RECORD_11", 2266);
        f2l("MUSIC_DISC_WAIT", "RECORD_WAIT", 2267, "RECORD_12");

    }

    private void flatteningMapping() {
        // Flattening remapping
        mapAlias("ZOMBIFIED_PIGLIN_SPAWN_EGG", "ZOMBIE_PIGMAN_SPAWN_EGG");
        mapAlias("SMOOTH_STONE_SLAB", "STONE_SLAB");
        mapAlias("GREEN_DYE", "CACTUS_GREEN");
        mapAlias("YELLOW_DYE", "DANDELION_YELLOW");
        mapAlias("RED_DYE", "ROSE_RED");
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

    private void f2l(String material, int legacyId, String alternativeLegacyName) {
        f2l(material, material, legacyId, alternativeLegacyName);
    }

    /* For Materials where the name is changed */
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0);
    }

    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, String alternativeLegacyName) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, 0, alternativeLegacyName);
    }

    /* For Materials where the name is changed and data is not zero*/
    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data) {
        f2l(flatteningMaterial, legacyMaterial, legacyId, data, null);
    }

    private void f2l(String flatteningMaterial, String legacyMaterial, int legacyId, int data, String alternativeLegacyName) {
        if (flatteningMaterial == null || legacyMaterial == null) {
            throw new IllegalArgumentException("Both flattening and legacy materials mustn't be null!");
        }
        var flatteningMaterialNamespaced = NamespacedMappingKey.of(flatteningMaterial);
        var legacyMaterialNamespaced = NamespacedMappingKey.of(legacyMaterial);
        var alternativeLegacyNamespaced =
                alternativeLegacyName != null && !alternativeLegacyName.equalsIgnoreCase(legacyMaterial) ? NamespacedMappingKey.of(alternativeLegacyName) : null;

        MaterialHolder holder = null;
        if (platform.isUsingLegacyNames() && (mapping.containsKey(legacyMaterialNamespaced) || (alternativeLegacyNamespaced != null && mapping.containsKey(alternativeLegacyNamespaced)))) {
            if (mapping.containsKey(legacyMaterialNamespaced)) {
                holder = mapping.get(legacyMaterialNamespaced).newDurability(data);
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
                holder = mapping.get(alternativeLegacyNamespaced).newDurability(data);
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

    public static boolean isInitialized() {
        return materialMapping != null;
    }

    private static MaterialHolder cachedAir;

    public static MaterialHolder getAir() {
        if (cachedAir == null) {
            cachedAir = resolve("AIR").orElseThrow();
        }
        return cachedAir;
    }

    public static boolean isBlock(MaterialHolder materialHolder) {
        if (materialMapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        return materialMapping.isBlock0(materialHolder);
    }

    protected abstract boolean isBlock0(MaterialHolder materialHolder);

    public static boolean isItem(MaterialHolder materialHolder) {
        if (materialMapping == null) {
            throw new UnsupportedOperationException("Material mapping is not initialized yet.");
        }
        return materialMapping.isItem0(materialHolder);
    }

    protected abstract boolean isItem0(MaterialHolder materialHolder);
}
