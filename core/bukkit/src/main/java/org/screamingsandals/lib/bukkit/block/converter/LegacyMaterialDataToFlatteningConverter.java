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

package org.screamingsandals.lib.bukkit.block.converter;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;
import org.screamingsandals.lib.Server;

@SuppressWarnings("deprecation")
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
                    materialData.setData(age);
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
            } else if (materialData instanceof Button) {
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
                } else if (materialData instanceof Dispenser) { // according to Bukkit API, Dropper is also Dispenser
                    switch (key.toLowerCase()) {
                        case "facing":
                            // setFacing is not properly implemented (other properties are removed when used)
                            switch (value.toLowerCase()) {
                                case "down":
                                    materialData.setData((byte) (materialData.getData() & 0x8));
                                    break;
                                case "up":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x1));
                                    break;
                                case "east":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x5));
                                    break;
                                case "west":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x4));
                                    break;
                                case "north":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x2));
                                    break;
                                case "south":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x3));
                                    break;
                            }
                            break;
                        case "triggered":
                            var bool = Boolean.parseBoolean(value);
                            if (bool) {
                                materialData.setData((byte) (materialData.getData() | 0x8));
                            } else {
                                materialData.setData((byte) (materialData.getData() & ~0x8));
                            }
                            break;
                    }
                } else if (materialData instanceof Furnace) {
                    switch (key.toLowerCase()) {
                        case "facing":
                            switch (value.toLowerCase()) {
                                case "east":
                                    ((Furnace) materialData).setFacingDirection(BlockFace.EAST);
                                    break;
                                case "west":
                                    ((Furnace) materialData).setFacingDirection(BlockFace.WEST);
                                    break;
                                case "north":
                                    ((Furnace) materialData).setFacingDirection(BlockFace.NORTH);
                                    break;
                                case "south":
                                    ((Furnace) materialData).setFacingDirection(BlockFace.SOUTH);
                                    break;
                            }
                            break;
                        case "lit":
                            var bool = Boolean.parseBoolean(value);
                            if (bool && "FURNACE".equals(materialName)) {
                                materialData = new Furnace(Material.valueOf("BURNING_FURNACE"), materialData.getData());
                            } else if (!bool && "BURNING_FURNACE".equals(materialName)) {
                                materialData = new Furnace(Material.valueOf("FURNACE"), materialData.getData());
                            }
                            break;
                    }
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
                if (Server.isVersion(1, 9)) {
                    switch (key.toLowerCase()) {
                        case "conditional":
                            materialData.setData((byte) (Boolean.parseBoolean(value) ? (materialData.getData() | 0x8) : (materialData.getData()) & ~0x8));
                            break;
                        case "facing":
                            switch (value.toLowerCase()) {
                                case "down":
                                    materialData.setData((byte) (materialData.getData() & 0x8));
                                    break;
                                case "east":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x5));
                                    break;
                                case "north":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x2));
                                    break;
                                case "south":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x3));
                                    break;
                                case "up":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x1));
                                    break;
                                case "west":
                                    materialData.setData((byte) ((materialData.getData() & 0x8) | 0x4));
                                    break;
                            }
                            break;
                    }
                } // 1.8 only had "powered"
            } else if ("DAYLIGHT_DETECTOR".equals(materialName) || "DAYLIGHT_DETECTOR_INVERTED".equals(materialName)) {
                switch (key.toLowerCase()) {
                    case "inverted":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData = new MaterialData(Material.valueOf("DAYLIGHT_DETECTOR_INVERTED"), materialData.getData());
                        } else {
                            materialData = new MaterialData(Material.valueOf("DAYLIGHT_DETECTOR"), materialData.getData());
                        }
                        break;
                    case "power":
                        var power = Byte.parseByte(value);
                        if (power < 0 || power > 15) {
                            power = 0;
                        }
                        materialData.setData(power);
                        break;
                }
            } else if (materialData instanceof Door) {
                // That guy who created legacy doors was surely on some drugs
                if (((Door) materialData).isTopHalf()) {
                    switch (key.toLowerCase()) {
                        case "half":
                            if ("lower".equalsIgnoreCase(value)) {
                                // all data are basically invalid because each half stores different information, so we have to reset it first
                                materialData.setData((byte) 0);
                                ((Door) materialData).setTopHalf(false);
                            }
                            break;
                        case "powered":
                            var bool = Boolean.parseBoolean(value);
                            if (bool) {
                                materialData.setData((byte) (materialData.getData() | 0x2));
                            } else {
                                materialData.setData((byte) (materialData.getData() & ~0x2));
                            }
                            break;
                        case "hinge":
                            if ("left".equalsIgnoreCase(value)) {
                                ((Door) materialData).setHinge(false);
                            } else if ("right".equalsIgnoreCase(value)) {
                                ((Door) materialData).setHinge(true);
                            }
                            break;
                    }
                } else {
                    switch (key.toLowerCase()) {
                        case "half":
                            if ("upper".equalsIgnoreCase(value)) {
                                // all data are basically invalid because each half stores different information, so we have to reset it first
                                materialData.setData((byte) 0);
                                ((Door) materialData).setTopHalf(true);
                            }
                            break;
                        case "open":
                            ((Door) materialData).setOpen(Boolean.parseBoolean(value));
                            break;
                        case "facing":
                            switch (value.toLowerCase()) {
                                case "east":
                                    ((Door) materialData).setFacingDirection(BlockFace.EAST);
                                    break;
                                case "north":
                                    ((Door) materialData).setFacingDirection(BlockFace.NORTH);
                                    break;
                                case "south":
                                    ((Door) materialData).setFacingDirection(BlockFace.SOUTH);
                                    break;
                                case "west":
                                    ((Door) materialData).setFacingDirection(BlockFace.WEST);
                                    break;
                            }
                            break;
                    }
                }
            } else if ("ENDER_PORTAL_FRAME".equals(materialName)) {
                switch (key.toLowerCase()) {
                    case "eye":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() | 0x4));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x4));
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "east":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x3));
                                break;
                            case "north":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x2));
                                break;
                            case "south":
                                materialData.setData((byte) ((materialData.getData() & 0x4)));
                                break;
                            case "west":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x1));
                                break;
                        }
                        break;
                }
            } else if ("END_ROD".equals(materialName)) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "up":
                            materialData.setData((byte) 0x1);
                            break;
                        case "down":
                            materialData.setData((byte) 0x0);
                            break;
                        case "east":
                            materialData.setData((byte) 0x5);
                            break;
                        case "north":
                            materialData.setData((byte) 0x2);
                            break;
                        case "south":
                            materialData.setData((byte) 0x3);
                            break;
                        case "west":
                            materialData.setData((byte) 0x4);
                            break;
                    }
                }
            } else if (materialName.equals("SOIL")) {
                if ("moisture".equalsIgnoreCase(key)) {
                    var moisture = Byte.parseByte(value);
                    if (moisture < 0 || moisture > 7) {
                        moisture = 0;
                    }
                    materialData.setData(moisture);
                }
            }
            // skipping fence as fence didn't have any values in legacy, everything was computed client-side
            else if (materialData instanceof Gate) {
                switch (key.toLowerCase()) {
                    case "facing":
                        // implementation in Gate is a bit sus and does not correspond with wiki
                        switch (value.toLowerCase()) {
                            case "east":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x3));
                                break;
                            case "north":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x2));
                                break;
                            case "south":
                                materialData.setData((byte) ((materialData.getData() & 0x4)));
                                break;
                            case "west":
                                materialData.setData((byte) ((materialData.getData() & 0x4) | 0x1));
                                break;
                        }
                        break;
                    case "open":
                        ((Gate) materialData).setOpen(Boolean.parseBoolean(value));
                        break;
                }
            } else if ("FIRE".equals(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var fire = Byte.parseByte(value);
                    if (fire < 0 || fire > 15) {
                        fire = 0;
                    }
                    materialData.setData(fire);
                } // other keys are not present in legacy
            } else if ("DOUBLE_PLANT".equals(materialName)) {
                if ("half".equalsIgnoreCase(key)) {
                    if ("lower".equalsIgnoreCase(value)) {
                        materialData.setData((byte) (materialData.getData() & ~0x8));
                    } else if ("upper".equalsIgnoreCase(value)) {
                        materialData.setData((byte) (materialData.getData() | 0x8));
                    }
                }
            } else if ("FROSTED_ICE".equals(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 3) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            }
            // skipping glass panes as glass pane didn't have any values (except color) in legacy, everything was computed client-side
            else if (materialName.endsWith("GLAZED_TERRACOTTA")) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "east":
                            materialData.setData((byte) 0x3);
                            break;
                        case "north":
                            materialData.setData((byte) 0x2);
                            break;
                        case "south":
                            materialData.setData((byte) 0x0);
                            break;
                        case "west":
                            materialData.setData((byte) 0x1);
                            break;
                    }
                }
            }
            // skipping Grass block, mycelium & podzol: no snowy property in legacy
            else if ("HAY_BLOCK".equals(materialName)) {
                if ("axis".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "x":
                            materialData.setData((byte) 0x4);
                            break;
                        case "y":
                            materialData.setData((byte) 0x0);
                            break;
                        case "z":
                            materialData.setData((byte) 0x8);
                            break;
                    }
                }
            } else if ("HOPPER".equalsIgnoreCase(materialName)) { // can't use o.b.m.Hopper due to compatibility with 1.8.8
                switch (key.toLowerCase()) {
                    case "enabled":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() & ~0x8));
                        } else {
                            materialData.setData((byte) (materialData.getData() | 0x8));
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "down":
                                materialData.setData((byte) ((materialData.getData() & 0x8)));
                                break;
                            case "east":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x5));
                                break;
                            case "north":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x2));
                                break;
                            case "south":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x3));
                                break;
                            case "west":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x4));
                                break;
                        }
                        break;
                }
            }
            // skipping iron bars as iron bars didn't have any values in legacy, everything was computed client-side
            else if (materialData instanceof Pumpkin) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "east":
                            ((Pumpkin) materialData).setFacingDirection(BlockFace.EAST);
                            break;
                        case "north":
                            ((Pumpkin) materialData).setFacingDirection(BlockFace.NORTH);
                            break;
                        case "south":
                            ((Pumpkin) materialData).setFacingDirection(BlockFace.SOUTH);
                            break;
                        case "west":
                            ((Pumpkin) materialData).setFacingDirection(BlockFace.WEST);
                            break;
                    }
                }
            } else if ("JUKEBOX".equals(materialName)) {
                if ("has_record".equalsIgnoreCase(key)) {
                    var bool = Boolean.parseBoolean(value);
                    if (bool) {
                        materialData.setData((byte) 0x0);
                    } else {
                        materialData.setData((byte) 0x1);
                    }
                }
            } else if (materialData instanceof Ladder) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "east":
                            ((Ladder) materialData).setFacingDirection(BlockFace.EAST);
                            break;
                        case "north":
                            ((Ladder) materialData).setFacingDirection(BlockFace.NORTH);
                            break;
                        case "south":
                            ((Ladder) materialData).setFacingDirection(BlockFace.SOUTH);
                            break;
                        case "west":
                            ((Ladder) materialData).setFacingDirection(BlockFace.WEST);
                            break;
                    }
                }
            } else if ("WATER".equals(materialName) || "STATIONARY_WATER".equals(materialName) || "LAVA".equals(materialName) || "STATIONARY_LAVA".equals(materialName)) {
                if ("level".equalsIgnoreCase(key)) {
                    var level = Byte.parseByte(value);
                    if (level < 0 || level > 15) {
                        level = 0;
                    }
                    materialData.setData(level);
                }
            } else if ("LEAVES".equals(materialName) || "LEAVES_2".equals(materialName)) { // o.b.m.Leaves can't be used because implementation in 1.8.8 was broken
                if ("persistent".equalsIgnoreCase(key)) {
                    var bool = Boolean.parseBoolean(value);
                    if (bool) {
                        materialData.setData((byte) (materialData.getData() | 0x4));
                    } else {
                        materialData.setData((byte) (materialData.getData() & ~0x4));
                    }
                } // distance does not exist in legacy, however check decay does
            } else if (materialData instanceof Lever) {
                switch (key.toLowerCase()) {
                    case "face":
                        switch (value.toLowerCase()) {
                            case "ceiling":
                                if (((Lever) materialData).getAttachedFace() != BlockFace.UP) {
                                    var oldData = materialData.getData();
                                    materialData.setData((byte) ((oldData & 0x8))); // ceiling east-west
                                    var oldFacing = oldData & 0x7;
                                    if (oldFacing == 0x3 || oldFacing == 0x4 || oldFacing == 0x5) {
                                        ((Lever) materialData).setFacingDirection(BlockFace.SOUTH); // ceiling south-north
                                    }
                                }
                                break;
                            case "wall":
                                if (((Lever) materialData).getAttachedFace() == BlockFace.UP || ((Lever) materialData).getAttachedFace() == BlockFace.DOWN) {
                                    var oldData = materialData.getData();
                                    materialData.setData((byte) ((oldData & 0x8) | 0x3)); // east
                                    var oldFacing = oldData & 0x7;
                                    if (oldFacing == 0x5 || oldFacing == 0x7) {
                                        ((Lever) materialData).setFacingDirection(BlockFace.SOUTH); // south
                                    }
                                }
                                break;
                            case "floor":
                                if (((Lever) materialData).getAttachedFace() != BlockFace.DOWN) {
                                    var oldData = materialData.getData();
                                    materialData.setData((byte) ((oldData & 0x8) | 0x6)); // floor east-west
                                    var oldFacing = oldData & 0x7;
                                    if (oldFacing == 0x3 || oldFacing == 0x4 || oldFacing == 0x7) {
                                        ((Lever) materialData).setFacingDirection(BlockFace.SOUTH); // floor south-north
                                    }
                                }
                                break;
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "east":
                                ((Lever) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((Lever) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Lever) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((Lever) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                    case "powered":
                        ((Lever) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                }
            } else if (materialData instanceof Tree) {
                if ("axis".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase()) {
                        case "x":
                            ((Tree) materialData).setDirection(BlockFace.EAST);
                            break;
                        case "y":
                            ((Tree) materialData).setDirection(BlockFace.UP);
                            break;
                        case "z":
                            ((Tree) materialData).setDirection(BlockFace.SOUTH);
                            break;
                    }
                }
            } else if ("MELON_STEM".equals(materialName) || "PUMPKIN_STEM".equals(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 7) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            }
            // TODO: fucking heads (involves tile entities and other shits)
            // TODO: Mushroom blocks (that's also retarded)
            else if ("PORTAL".equals(materialName)) {
                if ("axis".equalsIgnoreCase(key)) {
                    if ("x".equalsIgnoreCase(value)) {
                        materialData.setData((byte) 0x1);
                    } else if ("z".equalsIgnoreCase(value)) {
                        materialData.setData((byte) 0x2);
                    }
                }
            }
            // skipping note block as note block didn't have any values in legacy, everything was computed
            else if ("OBSERVER".equals(materialName)) {
                /* can't do instanceof o.b.m.Observer because Observer is not present in 1.8, 1.9 and 1.10.
                Also o.b.m.Observer has been added in 1.11.2 while Observer itself has been added in 1.11.0, so we can't use it */
                switch (key.toLowerCase()) {
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "down":
                                materialData.setData((byte) (materialData.getData() & 0x8));
                                break;
                            case "east":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x5));
                                break;
                            case "north":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x2));
                                break;
                            case "south":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x3));
                                break;
                            case "up":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x1));
                                break;
                            case "west":
                                materialData.setData((byte) ((materialData.getData() & 0x8) | 0x4));
                                break;
                        }
                        break;
                    case "powered":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() | 0x8));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x8));
                        }
                        break;
                }
            } else if (materialData instanceof PistonBaseMaterial) {
                switch (key.toLowerCase()) {
                    case "extended":
                        ((PistonBaseMaterial) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "down":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.DOWN);
                                break;
                            case "east":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "up":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.UP);
                                break;
                            case "west":
                                ((PistonBaseMaterial) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                }
            } else if (materialData instanceof PistonExtensionMaterial) {
                switch (key.toLowerCase()) {
                    case "type":
                        if ("normal".equalsIgnoreCase(value)) {
                            ((PistonExtensionMaterial) materialData).setSticky(false);
                        } else if ("sticky".equalsIgnoreCase(value)) {
                            ((PistonExtensionMaterial) materialData).setSticky(true);
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase()) {
                            case "down":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.DOWN);
                                break;
                            case "east":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "up":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.UP);
                                break;
                            case "west":
                                ((PistonExtensionMaterial) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                }
            } else if (materialData instanceof PressurePlate) {
                if ("powered".equalsIgnoreCase(key)) {
                    materialData.setData((byte) (Boolean.parseBoolean(value) ? 0x1 : 0x0));
                }
            } else if ("IRON_PLATE".equals(materialName) || "GOLD_PLATE".equals(materialName)) {
                if ("power".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 15) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            } else if ("QUARTZ_BLOCK".equals(materialName)) {
                if (materialData.getData() >= 0x2 && "axis".equals(key)) { // is pillar
                    switch (value.toLowerCase()) {
                        case "x":
                            materialData.setData((byte) 0x3);
                            break;
                        case "y":
                            materialData.setData((byte) 0x2);
                            break;
                        case "z":
                            materialData.setData((byte) 0x4);
                            break;
                    }
                }
            } else if ("PURPUR_PILLAR".equals(materialName)) {
                if ("axis".equals(key)) {
                    switch (value.toLowerCase()) {
                        case "x":
                            materialData.setData((byte) 0x4);
                            break;
                        case "y":
                            materialData.setData((byte) 0x0);
                            break;
                        case "z":
                            materialData.setData((byte) 0x8);
                            break;
                    }
                }
            }
            // TODO: Continue at Rails
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return materialData;
    }
}
