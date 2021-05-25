package org.screamingsandals.lib.bukkit.world;

import lombok.experimental.UtilityClass;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class LegacyBlockDataConverter {
    public Map<String, Object> convertMaterialData(MaterialData data) {
        var map = new HashMap<String, Object>();

        // Banner: TODO

        // Bed
        if (data instanceof Bed) {
            map.put("facing", ((Bed) data).getFacing().name().toLowerCase());
            map.put("part", ((Bed) data).isHeadOfBed() ? "head" : "foot");
        }

        // Button: TODO

        // Cake
        if (data instanceof Cake) {
            map.put("bites", ((Cake) data).getSlicesEaten());
        }

        // Cauldron
        if (data instanceof Cauldron) {
            map.put("level", data.getData());
        }

        // Chest: TODO

        // CocoaPlant
        if (data instanceof CocoaPlant) {
            map.put("age", ((CocoaPlant) data).getSize().ordinal());
            map.put("facing", ((CocoaPlant) data).getFacing().name().toLowerCase());
        }

        // Command: TODO

        // Comparator
        if (data instanceof Comparator) {
            map.put("facing", ((Comparator) data).getFacing().name().toLowerCase());
            map.put("mode", ((Comparator) data).isSubtractionMode() ? "subtract" : "compare");
            map.put("powered", ((Comparator) data).isPowered());
        }

        // Crops
        if (data instanceof Crops) {
            map.put("age", ((Crops) data).getState().getData());
        }

        // DetectorRail
        if (data instanceof DetectorRail) {
            map.put("powered", ((DetectorRail) data).isPressed());
        }

        // Diode
        if (data instanceof Diode) {
            map.put("delay", ((Diode) data).getDelay());
            map.put("facing", ((Diode) data).getFacing().name().toLowerCase());
            // locked: TODO
            map.put("powered", ((Diode) data).isPowered());
        }

        // Dispenser: TODO

        // Door
        if (data instanceof Door) {
            map.put("facing", ((Door) data).getFacing().name().toLowerCase());
            map.put("half", ((Door) data).isTopHalf() ? "upper" : "lower");
            map.put("hinge", ((Door) data).getHinge() ? "right" : "left");
            map.put("open", ((Door) data).isOpen());
        }

        // EnderChest: TODO

        // ExtendedRails: TODO

        // FlowerPot: TODO

        // Gate
        if (data instanceof Gate) {
            map.put("facing", ((Gate) data).getFacing().name().toLowerCase());
            map.put("open", ((Gate) data).isOpen());
        }

        // Hopper
        if (data instanceof Hopper) {
            map.put("enabled", ((Hopper) data).isActive());
            map.put("facing", ((Hopper) data).getFacing().name().toLowerCase());
        }

        // Ladder
        if (data instanceof Ladder) {
            map.put("facing", ((Ladder) data).getFacing().name().toLowerCase());
        }

        // Leaves
        if (data instanceof Leaves) {
            map.put("persistent", ((Leaves) data).isDecayable());
        }

        // Lever: TODO

        // LongGrass: TODO

        // MonsterEggs: TODO

        // Mushroom: TODO

        // NetherWarts: TODO

        // Observer: TODO

        // PistonBaseMaterial: TODO

        // PistonExtensionMaterial: TODO

        // PoweredRail: TODO

        // PressurePlate: TODO

        // Pumpkin: TODO

        // Rails: TODO

        // RedstoneTorch: TODO

        // RedstoneWire: TODO

        // Sandstone: TODO

        // Sapling: TODO

        // Sign: TODO

        // SimpleAttachableMaterialData: TODO

        // Skull: TODO

        // SmoothBrick: TODO

        // Stairs: TODO

        // Step: TODO

        // TexturedMaterial: TODO

        // Torch: TODO

        // TrapDoor: TODO

        // Tree: TODO

        // Tripwire: TODO

        // TripwireHook: TODO

        // Vine: TODO

        // Wood: TODO

        // WoodenStep: TODO

        // Wool: TODO

        return map;
    }

    public MaterialData asMaterialData(Material material, int damage, Map<String, Object> data) {
        var materialData = material.getNewData((byte) damage);

        // Banner: TODO

        // Bed
        if (materialData instanceof Bed) {
            if (data.containsKey("facing")) {
                ((Bed) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
            if (data.containsKey("part")) {
                ((Bed) materialData).setHeadOfBed(data.get("part").equals("head"));
            }
        }

        // Button: TODO

        // Cake
        if (materialData instanceof Cake && data.containsKey("bites")) {
            ((Cake) materialData).setSlicesEaten(Integer.parseInt(data.get("bites").toString()));
        }

        // Cauldron
        if (materialData instanceof Cauldron && data.containsKey("level")) {
            materialData.setData(Byte.parseByte(data.get("level").toString()));
        }

        // Chest: TODO

        // CocoaPlant
        if (materialData instanceof CocoaPlant) {
            if (data.containsKey("age")) {
                ((CocoaPlant) materialData).setSize(CocoaPlant.CocoaPlantSize.values()[Integer.parseInt(data.get("age").toString())]);
            }
            if (data.containsKey("facing")) {
                ((CocoaPlant) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
        }

        // Command: TODO

        // Comparator
        if (materialData instanceof Comparator) {
            if (data.containsKey("facing")) {
                ((Comparator) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
            if (data.containsKey("mode")) {
                ((Comparator) materialData).setSubtractionMode(data.get("mode").equals("subtract"));
            }
            // powered: TODO
        }

        // Crops
        if (materialData instanceof Crops && data.containsKey("age")) {
            ((Crops) materialData).setState(CropState.getByData(Byte.parseByte(data.get("age").toString())));
        }

        // DetectorRail
        if (materialData instanceof DetectorRail && data.containsKey("powered")) {
            ((DetectorRail) materialData).setPressed(Boolean.parseBoolean(data.get("powered").toString()));
        }

        // Diode
        if (materialData instanceof Diode) {
            if (data.containsKey("delay")) {
                ((Diode) materialData).setDelay(Integer.parseInt(data.get("delay").toString()));
            }
            if (data.containsKey("facing")) {
                ((Diode) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
            // locked: TODO
            // powered: TODO
        }

        // DirectionalContainer: TODO

        // Dispenser: TODO

        // Door
        if (materialData instanceof Door) {
            if (data.containsKey("facing")) {
                ((Door) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
            if (data.containsKey("half")) {
                ((Door) materialData).setTopHalf(data.get("powered").equals("upper"));
            }
            if (data.containsKey("hinge")) {
                ((Door) materialData).setHinge(data.get("hinge").equals("right"));
            }
            if (data.containsKey("open")) {
                ((Door) materialData).setOpen(Boolean.parseBoolean(data.get("open").toString()));
            }
        }

        // EnderChest: TODO

        // ExtendedRails: TODO

        // FlowerPot: TODO

        // Gate
        if (materialData instanceof Gate) {
            if (data.containsKey("facing")) {
                ((Gate) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
            if (data.containsKey("open")) {
                ((Gate) materialData).setOpen(Boolean.parseBoolean(data.get("open").toString()));
            }
        }

        // Hopper
        if (materialData instanceof Hopper) {
            if (data.containsKey("enabled")) {
                ((Hopper) materialData).setActive(Boolean.parseBoolean(data.get("enabled").toString()));
            }
            if (data.containsKey("facing")) {
                ((Hopper) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
            }
        }

        // Ladder
        if (materialData instanceof Ladder && data.containsKey("facing")) {
            ((Ladder) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing").toString()));
        }

        // Leaves
        if (materialData instanceof Leaves && data.containsKey("persistent")) {
            ((Leaves) materialData).setDecayable(Boolean.parseBoolean(data.get("persistent").toString()));
        }

        // Lever: TODO

        // LongGrass: TODO

        // MonsterEggs: TODO

        // Mushroom: TODO

        // NetherWarts: TODO

        // Observer: TODO

        // PistonBaseMaterial: TODO

        // PistonExtensionMaterial: TODO

        // PoweredRail: TODO

        // PressurePlate: TODO

        // Pumpkin: TODO

        // Rails: TODO

        // RedstoneTorch: TODO

        // RedstoneWire: TODO

        // Sandstone: TODO

        // Sapling: TODO

        // Sign: TODO

        // SimpleAttachableMaterialData: TODO

        // Skull: TODO

        // SmoothBrick: TODO

        // Stairs: TODO

        // Step: TODO

        // TexturedMaterial: TODO

        // Torch: TODO

        // TrapDoor: TODO

        // Tree: TODO

        // Tripwire: TODO

        // TripwireHook: TODO

        // Vine: TODO

        // Wood: TODO

        // WoodenStep: TODO

        // Wool: TODO

        return materialData;
    }
}
