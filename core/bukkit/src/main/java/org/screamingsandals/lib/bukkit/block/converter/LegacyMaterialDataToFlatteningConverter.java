package org.screamingsandals.lib.bukkit.block.converter;

import org.bukkit.CropState;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;

public class LegacyMaterialDataToFlatteningConverter {
    public static MaterialData set(MaterialData materialData, String key, String value) {
        materialData = materialData.clone();
        var materialName = materialData.getItemType().name();
        try {
            if ("ANVIL".equals(materialName)) {
                // We want to reset only the lower two bits, the upper two bits are used for damage
                if (key.equalsIgnoreCase("facing")) {
                    switch (value.toLowerCase()) {
                        case "east":
                            materialData.setData((byte) ((materialData.getData() & 0b1100) | 0b0010));
                            break;
                        case "west":
                            materialData.setData((byte) ((materialData.getData() & 0b1100)));
                            break;
                        case "north":
                            materialData.setData((byte) ((materialData.getData() & 0b1100) | 0b0001));
                            break;
                        case "south":
                            materialData.setData((byte) ((materialData.getData() & 0b1100) | 0b0011));
                            break;
                    }
                }
            } else if (materialData instanceof Banner) {
                if (((Banner) materialData).isWallBanner()) {
                    if (key.equalsIgnoreCase("facing")) {
                        switch (value.toLowerCase()) {
                            case "east":
                                ((Banner) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "west":
                                ((Banner) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                            case "north":
                                ((Banner) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Banner) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                        }
                    }
                } else {
                    if (key.equalsIgnoreCase("rotation")) {
                        var c = ((Banner) materialData).clone();
                        c.setData(Byte.parseByte(value));
                        return c;
                    }
                }
            } else if (materialData instanceof Bed) {
                switch (key.toLowerCase()) {
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "east":
                                ((Bed) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "west":
                                ((Bed) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                            case "north":
                                ((Bed) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Bed) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                        }
                        break;
                    case "part":
                        if ("head".equalsIgnoreCase(value)) {
                            ((Bed) materialData).setHeadOfBed(true);
                        } else if ("foot".equalsIgnoreCase(value)) {
                            ((Bed) materialData).setHeadOfBed(false);
                        }
                        break;
                    //case "occupied": break; - Does not exist in legacy
                }
            } else if (materialData instanceof Crops) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if ("BEETROOT_BLOCK".equals(materialName) || "NETHER_WARTS".equals(materialName)) {
                        if (age < 0 || age > 3) {
                            age = 0;
                        }
                    } else {
                        if (age < 0 || age > 7) {
                            age = 0;
                        }
                    }
                    ((Crops) materialData).setState(CropState.getByData(age));
                }
            } else if (materialData instanceof NetherWarts) {
                var age = Byte.parseByte(value);
                if (age < 0 || age > 3) {
                    age = 0;
                }
                ((NetherWarts) materialData).setState(NetherWartsState.values()[age]);
            }
            // TODO: Bone block
            else if ("BREWING_STAND".equals(materialName)) {
                switch (key.toLowerCase()) {
                    case "has_bottle_0":
                        if (Boolean.parseBoolean(value)) {
                            materialData.setData((byte) (materialData.getData() | 0x1));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x1));
                        }
                        break;
                    case "has_bottle_1":
                        if (Boolean.parseBoolean(value)) {
                            materialData.setData((byte) (materialData.getData() | 0x2));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x2));
                        }
                        break;
                    case "has_bottle_2":
                        if (Boolean.parseBoolean(value)) {
                            materialData.setData((byte) (materialData.getData() | 0x4));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x4));
                        }
                        break;
                }
            }
            else if (materialData instanceof Button) {
                switch (key.toLowerCase()) {
                    case "powered":
                        ((Button) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        if (((Button) materialData).getFacing() != BlockFace.UP && ((Button) materialData).getFacing() != BlockFace.DOWN) { // only works when on side in legacy
                            switch (value.toLowerCase()) {
                                case "east":
                                    ((Button) materialData).setFacingDirection(BlockFace.EAST);
                                    break;
                                case "west":
                                    ((Button) materialData).setFacingDirection(BlockFace.WEST);
                                    break;
                                case "north":
                                    ((Button) materialData).setFacingDirection(BlockFace.NORTH);
                                    break;
                                case "south":
                                    ((Button) materialData).setFacingDirection(BlockFace.SOUTH);
                                    break;
                            }
                        }
                        break;
                    case "face":
                        switch (value.toLowerCase()) {
                            case "wall":
                                if (((Button) materialData).getFacing() == BlockFace.UP || ((Button) materialData).getFacing() == BlockFace.DOWN) {
                                    ((Button) materialData).setFacingDirection(BlockFace.NORTH);
                                }
                                break;
                            case "ceiling":
                                ((Button) materialData).setFacingDirection(BlockFace.DOWN);
                                break;
                            case "floor":
                                ((Button) materialData).setFacingDirection(BlockFace.UP);
                                break;
                        }
                        break;
                }
            } else if ("CACTUS".equals(materialName) || "SUGAR_CANE_BLOCK".equals(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 15) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            } else if (materialData instanceof Cake) {
                if ("bites".equalsIgnoreCase(key)) {
                    var bites = Byte.parseByte(value);
                    if (bites < 0 || bites > 6) {
                        bites = 0;
                    }
                    materialData.setData(bites); // setSlicesRemaining incorrectly does not allow value 6
                }
            } else if (materialData instanceof Cauldron) {
                if ("level".equalsIgnoreCase(key)) {
                    var level = Byte.parseByte(value);
                    if (level < 0 || level > 3) {
                        level = 0;
                    }
                    materialData.setData(level);
                }
            } else if (materialData instanceof DirectionalContainer) {
                if (materialData instanceof Chest || materialData instanceof EnderChest) {
                    if ("facing".equalsIgnoreCase(key)) {
                        switch (value.toLowerCase()) {
                            case "east":
                                ((DirectionalContainer) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "west":
                                ((DirectionalContainer) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                            case "north":
                                ((DirectionalContainer) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((DirectionalContainer) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                        }
                    } // other parameters does not exist in legacy
                }
            } else if ("CHORUS_FLOWER".equalsIgnoreCase(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 5) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            } else if (materialData instanceof CocoaPlant) {
                switch (key.toLowerCase()) {
                    case "age":
                        var age = Byte.parseByte(value);
                        if (age < 0 || age > 2) {
                            age = 0;
                        }
                        ((CocoaPlant) materialData).setSize(CocoaPlant.CocoaPlantSize.values()[age]);
                        break;
                    case "facing":
                        // Bukkit api inverts the face, and we don't want it to do
                        switch (value.toLowerCase()) {
                            case "east":
                                ((CocoaPlant) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                            case "west":
                                ((CocoaPlant) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((CocoaPlant) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "south":
                                ((CocoaPlant) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                        }
                        break;
                }
            } else if (materialData instanceof Command) {
                switch (key.toLowerCase()) {
                    case "conditional":
                        ((Command) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        // TODO
                        break;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return materialData;
    }
}
