package org.screamingsandals.lib.bukkit.block;

import lombok.experimental.UtilityClass;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class LegacyBlockDataConverter {
    public Map<String, String> convertMaterialData(MaterialData data) {
        var map = new HashMap<String, String>();

        // Banner: TODO

        // Bed
        if (data instanceof Bed) {
            map.put("facing", ((Bed) data).getFacing().name().toLowerCase());
            map.put("part", ((Bed) data).isHeadOfBed() ? "head" : "foot");
        }

        // Button: TODO

        // Cake
        if (data instanceof Cake) {
            map.put("bites", String.valueOf(((Cake) data).getSlicesEaten()));
        }

        // Cauldron
        if (data instanceof Cauldron) {
            map.put("level", String.valueOf(data.getData()));
        }

        // Chest: TODO

        // CocoaPlant
        if (data instanceof CocoaPlant) {
            map.put("age", String.valueOf(((CocoaPlant) data).getSize().ordinal()));
            map.put("facing", ((CocoaPlant) data).getFacing().name().toLowerCase());
        }

        // Command: TODO

        // Comparator
        if (data instanceof Comparator) {
            map.put("facing", ((Comparator) data).getFacing().name().toLowerCase());
            map.put("mode", ((Comparator) data).isSubtractionMode() ? "subtract" : "compare");
            map.put("powered", String.valueOf(((Comparator) data).isPowered()));
        }

        // Crops
        if (data instanceof Crops) {
            map.put("age", String.valueOf(((Crops) data).getState().getData()));
        }

        // DetectorRail
        if (data instanceof DetectorRail) {
            map.put("powered", String.valueOf(((DetectorRail) data).isPressed()));
        }

        // Diode
        if (data instanceof Diode) {
            map.put("delay", String.valueOf(((Diode) data).getDelay()));
            map.put("facing", ((Diode) data).getFacing().name().toLowerCase());
            // locked: TODO
            map.put("powered", String.valueOf(((Diode) data).isPowered()));
        }

        // Dispenser: TODO

        // Door
        if (data instanceof Door) {
            map.put("facing", ((Door) data).getFacing().name().toLowerCase());
            map.put("half", ((Door) data).isTopHalf() ? "upper" : "lower");
            map.put("hinge", ((Door) data).getHinge() ? "right" : "left");
            map.put("open", String.valueOf(((Door) data).isOpen()));
        }

        // EnderChest: TODO

        // ExtendedRails: TODO

        // FlowerPot: TODO

        // Gate
        if (data instanceof Gate) {
            map.put("facing", ((Gate) data).getFacing().name().toLowerCase());
            map.put("open", String.valueOf(((Gate) data).isOpen()));
        }

        // Hopper
        if (data instanceof Hopper) {
            map.put("enabled", String.valueOf(((Hopper) data).isActive()));
            map.put("facing", ((Hopper) data).getFacing().name().toLowerCase());
        }

        // Ladder
        if (data instanceof Ladder) {
            map.put("facing", ((Ladder) data).getFacing().name().toLowerCase());
        }

        // Leaves
        if (data instanceof Leaves) {
            map.put("persistent", String.valueOf(((Leaves) data).isDecayable()));
        }

        // Lever: TODO

        // NetherWarts
        if (data instanceof NetherWarts) {
            map.put("age", String.valueOf(data.getData()));
        }

        // Observer
        if (data instanceof Observer) {
            map.put("facing", ((Observer) data).getFacing().name().toLowerCase());
            map.put("powered", String.valueOf(((Observer) data).isPowered()));
        }

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

    public MaterialData asMaterialData(Material material, int damage, Map<String, String> data) {
        var materialData = material.getNewData((byte) damage);

        // Banner: TODO

        // Bed
        if (materialData instanceof Bed) {
            if (data.containsKey("facing")) {
                ((Bed) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            if (data.containsKey("part")) {
                ((Bed) materialData).setHeadOfBed(data.get("part").equals("head"));
            }
        }

        // Button: TODO

        // Cake
        if (materialData instanceof Cake && data.containsKey("bites")) {
            ((Cake) materialData).setSlicesEaten(Integer.parseInt(data.get("bites")));
        }

        // Cauldron
        if (materialData instanceof Cauldron && data.containsKey("level")) {
            materialData.setData(Byte.parseByte(data.get("level")));
        }

        // Chest: TODO

        // CocoaPlant
        if (materialData instanceof CocoaPlant) {
            if (data.containsKey("age")) {
                ((CocoaPlant) materialData).setSize(CocoaPlant.CocoaPlantSize.values()[Integer.parseInt(data.get("age"))]);
            }
            if (data.containsKey("facing")) {
                ((CocoaPlant) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
        }

        // Command: TODO

        // Comparator
        if (materialData instanceof Comparator) {
            if (data.containsKey("facing")) {
                ((Comparator) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            if (data.containsKey("mode")) {
                ((Comparator) materialData).setSubtractionMode(data.get("mode").equals("subtract"));
            }
            // powered: TODO
        }

        // Crops
        if (materialData instanceof Crops && data.containsKey("age")) {
            ((Crops) materialData).setState(CropState.getByData(Byte.parseByte(data.get("age"))));
        }

        // DetectorRail
        if (materialData instanceof DetectorRail && data.containsKey("powered")) {
            ((DetectorRail) materialData).setPressed(Boolean.parseBoolean(data.get("powered")));
        }

        // Diode
        if (materialData instanceof Diode) {
            if (data.containsKey("delay")) {
                ((Diode) materialData).setDelay(Integer.parseInt(data.get("delay")));
            }
            if (data.containsKey("facing")) {
                ((Diode) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            // locked: TODO
            // powered: TODO
        }

        // DirectionalContainer: TODO

        // Dispenser: TODO

        // Door
        if (materialData instanceof Door) {
            if (data.containsKey("facing")) {
                ((Door) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            if (data.containsKey("half")) {
                ((Door) materialData).setTopHalf(data.get("powered").equals("upper"));
            }
            if (data.containsKey("hinge")) {
                ((Door) materialData).setHinge(data.get("hinge").equals("right"));
            }
            if (data.containsKey("open")) {
                ((Door) materialData).setOpen(Boolean.parseBoolean(data.get("open")));
            }
        }

        // EnderChest: TODO

        // ExtendedRails: TODO

        // FlowerPot: TODO

        // Gate
        if (materialData instanceof Gate) {
            if (data.containsKey("facing")) {
                ((Gate) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            if (data.containsKey("open")) {
                ((Gate) materialData).setOpen(Boolean.parseBoolean(data.get("open")));
            }
        }

        // Hopper
        if (materialData instanceof Hopper) {
            if (data.containsKey("enabled")) {
                ((Hopper) materialData).setActive(Boolean.parseBoolean(data.get("enabled")));
            }
            if (data.containsKey("facing")) {
                ((Hopper) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
        }

        // Ladder
        if (materialData instanceof Ladder && data.containsKey("facing")) {
            ((Ladder) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
        }

        // Leaves
        if (materialData instanceof Leaves && data.containsKey("persistent")) {
            ((Leaves) materialData).setDecayable(Boolean.parseBoolean(data.get("persistent")));
        }

        // Lever: TODO

        // Mushroom: TODO

        // NetherWarts
        if (materialData instanceof NetherWarts && data.containsKey("age")) {
            materialData.setData(Byte.parseByte(data.get("age")));
        }

        // Observer
        if (materialData instanceof Observer) {
            if (data.containsKey("facing")) {
                ((Observer) materialData).setFacingDirection(BlockFace.valueOf(data.get("facing")));
            }
            // TODO: powered
        }

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
