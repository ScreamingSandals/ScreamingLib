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

package org.screamingsandals.lib.impl.bukkit.block.converter;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockFace;
import org.bukkit.material.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bukkit.BukkitFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
@UtilityClass
public class LegacyMaterialDataToFlatteningConverter {
    // TODO: remove usage of o.b.m as it is not reliable
    public static @NotNull MaterialData set(@NotNull MaterialData materialData, @NotNull String key, @NotNull String value) {
        materialData = materialData.clone();
        var materialName = materialData.getItemType().name();
        try {
            if ("ANVIL".equals(materialName)) {
                // We want to reset only the lower two bits, the upper two bits are used for damage
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
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
                    if ("facing".equalsIgnoreCase(key)) {
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    if ("rotation".equalsIgnoreCase(key)) {
                        var c = ((Banner) materialData).clone();
                        c.setData(Byte.parseByte(value));
                        return c;
                    }
                }
            } else if (materialData instanceof Bed) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
            } else if (materialData instanceof Crops
                    || "CARROT".equals(materialName)
                    || "POTATO".equals(materialName)) {
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
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 3) {
                        age = 0;
                    }
                    ((NetherWarts) materialData).setState(NetherWartsState.values()[age]);
                }
            } else if ("BREWING_STAND".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "powered":
                        ((Button) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        if (((Button) materialData).getFacing() != BlockFace.UP && ((Button) materialData).getFacing() != BlockFace.DOWN) { // only works when on side in legacy
                            switch (value.toLowerCase(Locale.ROOT)) {
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
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "facing":
                            // setFacing is not properly implemented (other properties are removed when used)
                            switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "facing":
                            switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "age":
                        var age = Byte.parseByte(value);
                        if (age < 0 || age > 2) {
                            age = 0;
                        }
                        ((CocoaPlant) materialData).setSize(CocoaPlant.CocoaPlantSize.values()[age]);
                        break;
                    case "facing":
                        // Bukkit api inverts the face, and we don't want it to do
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                if (BukkitFeature.COMMAND_BLOCK_VERBOSE_BLOCK_STATE.isSupported()) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "conditional":
                            materialData.setData((byte) (Boolean.parseBoolean(value) ? (materialData.getData() | 0x8) : (materialData.getData()) & ~0x8));
                            break;
                        case "facing":
                            switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
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
                    switch (key.toLowerCase(Locale.ROOT)) {
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
                    switch (key.toLowerCase(Locale.ROOT)) {
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
                            switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "eye":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() | 0x4));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x4));
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (value.toLowerCase(Locale.ROOT)) {
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
            } else if ("SOIL".equals(materialName)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        // implementation in Gate is a bit sus and does not correspond with wiki
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (value.toLowerCase(Locale.ROOT)) {
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
            else if ("HOPPER".equalsIgnoreCase(materialName)) { // can't use o.b.m.Hopper due to compatibility with 1.8.8
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "enabled":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() & ~0x8));
                        } else {
                            materialData.setData((byte) (materialData.getData() | 0x8));
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (value.toLowerCase(Locale.ROOT)) {
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
                    switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "face":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                    if (((Tree) materialData).getDirection() != BlockFace.SELF) { // can't rotate wood/bark in legacy
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                }
            } else if ("MELON_STEM".equals(materialName) || "PUMPKIN_STEM".equals(materialName)) {
                if ("age".equalsIgnoreCase(key)) {
                    var age = Byte.parseByte(value);
                    if (age < 0 || age > 7) {
                        age = 0;
                    }
                    materialData.setData(age);
                }
            } else if (materialData instanceof Skull) {
                if (((Skull) materialData).getFacing() == BlockFace.SELF) {
                    // TODO: Rotation of mob head is stored inside Tile instead of Block Data in legacy
                } else {
                    if ("facing".equalsIgnoreCase(key)) {
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                materialData.setData((byte) 0x5); // implementation was broken in some versions
                                break;
                            case "north":
                                ((Skull) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Skull) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                materialData.setData((byte) 0x4); // implementation was broken in some versions
                                break;
                        }
                    }
                }
            } else if (materialData instanceof Mushroom && materialData.getData() != 10 && materialData.getData() != 15) { // ignore stems to not do something unexpected
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "east": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.EAST);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.EAST, bool);
                        }
                        break;
                    }
                    case "down": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.DOWN);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.DOWN, bool);
                        }
                        break;
                    }
                    case "north": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.NORTH);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.NORTH, bool);
                        }
                        break;
                    }
                    case "south": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.SOUTH);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.SOUTH, bool);
                        }
                        break;
                    }
                    case "up": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.UP);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.UP, bool);
                        }
                        break;
                    }
                    case "west": {
                        var bool = Boolean.parseBoolean(value);
                        var isPainted = ((Mushroom) materialData).isFacePainted(BlockFace.WEST);
                        if (bool != isPainted) {
                            ((Mushroom) materialData).setFacePainted(BlockFace.WEST, bool);
                        }
                        break;
                    }
                }
            } else if ("PORTAL".equals(materialName)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "extended":
                        ((PistonBaseMaterial) materialData).setPowered(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "type":
                        if ("normal".equalsIgnoreCase(value)) {
                            ((PistonExtensionMaterial) materialData).setSticky(false);
                        } else if ("sticky".equalsIgnoreCase(value)) {
                            ((PistonExtensionMaterial) materialData).setSticky(true);
                        }
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
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
                if (materialData.getData() >= 0x2 && "axis".equalsIgnoreCase(key)) { // is pillar
                    switch (value.toLowerCase(Locale.ROOT)) {
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
            } else if ("PURPUR_PILLAR".equals(materialName) || "BONE_BLOCK".equals(materialName) || "HAY_BLOCK".equals(materialName)) {
                if ("axis".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
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
            } else if (materialData instanceof Rails) {
                if (materialData instanceof ExtendedRails) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "powered":
                            var bool = Boolean.parseBoolean(value);
                            if (bool) {
                                materialData.setData((byte) (materialData.getData() | 0x8));
                            } else {
                                materialData.setData((byte) (materialData.getData() & ~0x8));
                            }
                            break;
                        case "shape":
                            switch (value.toLowerCase(Locale.ROOT)) {
                                case "east_west":
                                    ((Rails) materialData).setDirection(BlockFace.EAST, false);
                                    break;
                                case "north_south":
                                    ((Rails) materialData).setDirection(BlockFace.NORTH, false);
                                    break;
                                case "ascending_east":
                                    ((Rails) materialData).setDirection(BlockFace.EAST, true);
                                    break;
                                case "ascending_north":
                                    ((Rails) materialData).setDirection(BlockFace.NORTH, true);
                                    break;
                                case "ascending_south":
                                    ((Rails) materialData).setDirection(BlockFace.SOUTH, true);
                                    break;
                                case "ascending_west":
                                    ((Rails) materialData).setDirection(BlockFace.WEST, true);
                                    break;
                            }
                            break;
                    }
                } else {
                    if ("shape".equalsIgnoreCase(key)) {
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east_west":
                                ((Rails) materialData).setDirection(BlockFace.EAST, false);
                                break;
                            case "north_east":
                                ((Rails) materialData).setDirection(BlockFace.SOUTH_WEST, false); // Bukkit API inverts it for some reason
                                break;
                            case "north_south":
                                ((Rails) materialData).setDirection(BlockFace.NORTH, false);
                                break;
                            case "north_west":
                                ((Rails) materialData).setDirection(BlockFace.SOUTH_EAST, false); // Bukkit API inverts it for some reason
                                break;
                            case "south_east":
                                ((Rails) materialData).setDirection(BlockFace.NORTH_WEST, false); // Bukkit API inverts it for some reason
                                break;
                            case "south_west":
                                ((Rails) materialData).setDirection(BlockFace.NORTH_EAST, false); // Bukkit API inverts it for some reason
                                break;
                            case "ascending_east":
                                ((Rails) materialData).setDirection(BlockFace.EAST, true);
                                break;
                            case "ascending_north":
                                ((Rails) materialData).setDirection(BlockFace.NORTH, true);
                                break;
                            case "ascending_south":
                                ((Rails) materialData).setDirection(BlockFace.SOUTH, true);
                                break;
                            case "ascending_west":
                                ((Rails) materialData).setDirection(BlockFace.WEST, true);
                                break;
                        }
                    }
                }
            } else if ("REDSTONE_COMPARATOR_OFF".equals(materialName) || "REDSTONE_COMPARATOR_ON".equals(materialName)) { // o.b.m.Comparator is too new
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                materialData.setData((byte) ((materialData.getData() & 0xC) | 0x2));
                                break;
                            case "north":
                                materialData.setData((byte) ((materialData.getData() & 0xC) | 0x1));
                                break;
                            case "south":
                                materialData.setData((byte) ((materialData.getData() & 0xC)));
                                break;
                            case "west":
                                materialData.setData((byte) ((materialData.getData() & 0xC) | 0x3));
                                break;
                        }
                        break;
                    case "mode":
                        if ("compare".equalsIgnoreCase(value)) {
                            materialData.setData((byte) (materialData.getData() & ~0x4));
                        } else if ("subtract".equalsIgnoreCase(value)) {
                            materialData.setData((byte) (materialData.getData() | 0x4));
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
            } else if (materialData instanceof RedstoneWire) {
                if ("power".equalsIgnoreCase(key)) {
                    var power = Byte.parseByte(value);
                    if (power < 0 || power > 15) {
                        power = 0;
                    }
                    materialData.setData(power);
                }
            } else if ("REDSTONE_LAMP_OFF".equals(materialName) || "REDSTONE_LAMP_ON".equals(materialName)) {
                if ("lit".equalsIgnoreCase(key)) {
                    var bool = Boolean.parseBoolean(value);
                    if (bool && "REDSTONE_LAMP_OFF".equals(materialName)) {
                        materialData = new MaterialData(Material.valueOf("REDSTONE_LAMP_ON"));
                    } else if (!bool && "REDSTONE_LAMP_ON".equals(materialName)) {
                        materialData = new MaterialData(Material.valueOf("REDSTONE_LAMP_OFF"));
                    }
                }
            } else if ("REDSTONE_ORE".equals(materialName) || "GLOWING_REDSTONE_ORE".equals(materialName)) {
                if ("lit".equalsIgnoreCase(key)) {
                    var bool = Boolean.parseBoolean(value);
                    if (bool && "REDSTONE_ORE".equals(materialName)) {
                        materialData = new MaterialData(Material.valueOf("GLOWING_REDSTONE_ORE"));
                    } else if (!bool && "GLOWING_REDSTONE_ORE".equals(materialName)) {
                        materialData = new MaterialData(Material.valueOf("REDSTONE_ORE"));
                    }
                }
            } else if (materialData instanceof Diode) { // repeater
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "delay":
                        var delay = Byte.parseByte(value);
                        if (delay < 1 || delay > 4) {
                            delay = 1;
                        }
                        ((Diode) materialData).setDelay(delay);
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                ((Diode) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((Diode) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Diode) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((Diode) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                    case "powered":
                        var bool = Boolean.parseBoolean(value);
                        if (bool && "DIODE_BLOCK_OFF".equals(materialName)) {
                            materialData = new Diode(Material.valueOf("DIODE_BLOCK_ON"), materialData.getData());
                        } else if (!bool && "DIODE_BLOCK_ON".equals(materialName)) {
                            materialData = new Diode(Material.valueOf("DIODE_BLOCK_OFF"), materialData.getData());
                        }
                        break;
                } // locked does not exist in legacy
            } else if (materialData instanceof RedstoneTorch) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "lit":
                        var bool = Boolean.parseBoolean(value);
                        if (bool && "REDSTONE_TORCH_OFF".equals(materialName)) {
                            materialData = new MaterialData(Material.valueOf("REDSTONE_TORCH_ON"), materialData.getData());
                        } else if (!bool && "REDSTONE_TORCH_ON".equals(materialName)) {
                            materialData = new MaterialData(Material.valueOf("REDSTONE_TORCH_OFF"), materialData.getData());
                        }
                        break;
                    case "facing":
                        // flattening split floor and wall torches, so we should behave the same and not set the facing if this is not a wall torch
                        if (((RedstoneTorch) materialData).getAttachedFace() != BlockFace.DOWN) {
                            switch (value.toLowerCase(Locale.ROOT)) {
                                case "east":
                                    ((RedstoneTorch) materialData).setFacingDirection(BlockFace.EAST);
                                    break;
                                case "north":
                                    ((RedstoneTorch) materialData).setFacingDirection(BlockFace.NORTH);
                                    break;
                                case "south":
                                    ((RedstoneTorch) materialData).setFacingDirection(BlockFace.SOUTH);
                                    break;
                                case "west":
                                    ((RedstoneTorch) materialData).setFacingDirection(BlockFace.WEST);
                                    break;
                            }
                        }
                        break;
                }
            } else if ("SAPLING".equals(materialName)) { // o.b.m.Sapling is too new
                if ("stage".equalsIgnoreCase(key)) {
                    var stage = Byte.parseByte(value) == 1;
                    if (stage) {
                        materialData.setData((byte) (materialData.getData() | 0x8));
                    } else {
                        materialData.setData((byte) (materialData.getData() & ~0x8));
                    }
                }
            } else if (materialName.endsWith("_SHULKER_BOX")) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
                        case "down":
                            materialData.setData((byte) 0x0);
                            break;
                        case "up":
                            materialData.setData((byte) 0x1);
                            break;
                        case "east":
                            materialData.setData((byte) 0x5);
                            break;
                        case "west":
                            materialData.setData((byte) 0x4);
                            break;
                        case "north":
                            materialData.setData((byte) 0x2);
                            break;
                        case "south":
                            materialData.setData((byte) 0x3);
                            break;
                    }
                }
            } else if (materialData instanceof Sign) {
                if (((Sign) materialData).isWallSign()) {
                    if ("facing".equalsIgnoreCase(key)) {
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                ((Sign) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((Sign) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Sign) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((Sign) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                    }
                } else {
                    if ("rotation".equalsIgnoreCase(key)) {
                        var rotation = Byte.parseByte(value);
                        if (rotation < 0 || rotation > 15) {
                            rotation = 0;
                        }
                        materialData.setData(rotation);
                    }
                }
            } else if ("STEP".equals(materialName) // the o.b.m implementation is a bit retarded and not completed
                    || "WOOD_STEP".equals(materialName)
                    || "STONE_SLAB2".equals(materialName)
                    || "PURPUR_SLAB".equals(materialName)
            ) {
                if ("type".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
                        case "bottom":
                            materialData.setData((byte) (materialData.getData() & ~0x8));
                            break;
                        case "top":
                            materialData.setData((byte) (materialData.getData() | 0x8));
                            break;
                        case "double":
                            switch (materialName) {
                                case "STEP":
                                    materialData = new MaterialData(Material.valueOf("DOUBLE_STEP"), (byte) (materialData.getData() & 0x7));
                                    break;
                                case "WOOD_STEP":
                                    materialData = new MaterialData(Material.valueOf("WOOD_DOUBLE_STEP"), (byte) (materialData.getData() & 0x7));
                                    break;
                                case "STONE_SLAB2":
                                    materialData = new MaterialData(Material.valueOf("DOUBLE_STONE_SLAB2"), (byte) (materialData.getData() & 0x7));
                                    break;
                                case "PURPUR_SLAB":
                                    materialData = new MaterialData(Material.valueOf("PURPUR_DOUBLE_SLAB"), (byte) (materialData.getData() & 0x7));
                                    break;
                            }
                            break;
                    }
                }
            } else if ("DOUBLE_STEP".equals(materialName)
                    || "WOOD_DOUBLE_STEP".equals(materialName)
                    || "DOUBLE_STONE_SLAB2".equals(materialName)
                    || "PURPUR_DOUBLE_SLAB".equals(materialName)
            ) {
                if ("type".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
                        case "bottom":
                            switch (materialName) {
                                case "DOUBLE_STEP":
                                    materialData = new MaterialData(Material.valueOf("STEP"), materialData.getData());
                                    break;
                                case "WOOD_DOUBLE_STEP":
                                    materialData = new MaterialData(Material.valueOf("WOOD_STEP"), materialData.getData());
                                    break;
                                case "DOUBLE_STONE_SLAB2":
                                    materialData = new MaterialData(Material.valueOf("STONE_SLAB2"), materialData.getData());
                                    break;
                                case "PURPUR_DOUBLE_SLAB":
                                    materialData = new MaterialData(Material.valueOf("PURPUR_SLAB"), materialData.getData());
                                    break;
                            }
                            break;
                        case "top":
                            switch (materialName) {
                                case "DOUBLE_STEP":
                                    materialData = new MaterialData(Material.valueOf("STEP"), (byte) (materialData.getData() | 0x8));
                                    break;
                                case "WOOD_DOUBLE_STEP":
                                    materialData = new MaterialData(Material.valueOf("WOOD_STEP"), (byte) (materialData.getData() | 0x8));
                                    break;
                                case "DOUBLE_STONE_SLAB2":
                                    materialData = new MaterialData(Material.valueOf("STONE_SLAB2"), (byte) (materialData.getData() | 0x8));
                                    break;
                                case "PURPUR_DOUBLE_SLAB":
                                    materialData = new MaterialData(Material.valueOf("PURPUR_SLAB"), (byte) (materialData.getData() | 0x8));
                                    break;
                            }
                            break;
                        // case "double": already double, no need to change anything
                    }
                }
            } else if ("SNOW".equals(materialName)) {
                if ("layers".equalsIgnoreCase(key)) {
                    var layer = (byte) (Byte.parseByte(value) - 1);
                    if (layer < 0 || layer > 7) {
                        layer = 0;
                    }
                    materialData.setData(layer);
                }
            } else if (materialData instanceof Stairs) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "north":
                                ((Stairs) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "west":
                                ((Stairs) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                            case "south":
                                ((Stairs) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "east":
                                ((Stairs) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                        }
                        break;
                    case "half":
                        if ("bottom".equalsIgnoreCase(value)) {
                            ((Stairs) materialData).setInverted(false);
                        } else if ("top".equalsIgnoreCase(value)) {
                            ((Stairs) materialData).setInverted(true);
                        }
                        break;
                    // case "shape": - shape was computed on client-side in legacy
                }
            } else if ("STRUCTURE_BLOCK".equals(materialName)) {
                if ("mode".equalsIgnoreCase(key)) {
                    switch (value.toLowerCase(Locale.ROOT)) {
                        case "corner":
                            materialData.setData((byte) 0x3);
                            break;
                        case "data":
                            materialData.setData((byte) 0x0);
                            break;
                        case "load":
                            materialData.setData((byte) 0x2);
                            break;
                        case "save":
                            materialData.setData((byte) 0x1);
                            break;
                    }
                }
            } else if ("TNT".equals(materialName)) {
                if ("unstable".equalsIgnoreCase(key)) {
                    var bool = Boolean.parseBoolean(value);
                    if (bool) {
                        materialData.setData((byte) 0x1);
                    } else {
                        materialData.setData((byte) 0x0);
                    }
                }
            } else if (materialData instanceof Torch) {
                if ("facing".equalsIgnoreCase(key)) {
                    // flattening split floor and wall torches, so we should behave the same and not set the facing if this is not a wall torch
                    if (((Torch) materialData).getAttachedFace() != BlockFace.DOWN) {
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                ((Torch) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((Torch) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((Torch) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((Torch) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                    }
                }
            } else if (materialData instanceof TrapDoor) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                ((TrapDoor) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((TrapDoor) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((TrapDoor) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((TrapDoor) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                    case "half":
                        if ("bottom".equalsIgnoreCase(value)) {
                            ((TrapDoor) materialData).setInverted(false);
                        } else if ("top".equalsIgnoreCase(value)) {
                            ((TrapDoor) materialData).setInverted(true);
                        }
                        break;
                    case "open":
                        ((TrapDoor) materialData).setOpen(Boolean.parseBoolean(value));
                        break;
                }
            } else if (materialData instanceof Tripwire) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "attached":
                        ((Tripwire) materialData).setActivated(Boolean.parseBoolean(value));
                        break;
                    case "disarmed":
                        var bool = Boolean.parseBoolean(value);
                        if (bool) {
                            materialData.setData((byte) (materialData.getData() | 0x8));
                        } else {
                            materialData.setData((byte) (materialData.getData() & ~0x8));
                        }
                        break;
                    case "powered":
                        ((Tripwire) materialData).setObjectTriggering(Boolean.parseBoolean(value));
                        break;
                } // directions did not exist in legacy, everything was computed
            } else if (materialData instanceof TripwireHook) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "attached":
                        ((TripwireHook) materialData).setConnected(Boolean.parseBoolean(value));
                        break;
                    case "facing":
                        switch (value.toLowerCase(Locale.ROOT)) {
                            case "east":
                                ((TripwireHook) materialData).setFacingDirection(BlockFace.EAST);
                                break;
                            case "north":
                                ((TripwireHook) materialData).setFacingDirection(BlockFace.NORTH);
                                break;
                            case "south":
                                ((TripwireHook) materialData).setFacingDirection(BlockFace.SOUTH);
                                break;
                            case "west":
                                ((TripwireHook) materialData).setFacingDirection(BlockFace.WEST);
                                break;
                        }
                        break;
                    case "powered":
                        ((TripwireHook) materialData).setActivated(Boolean.parseBoolean(value));
                        break;
                }
            } else if (materialData instanceof Vine) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "east":
                        if (Boolean.parseBoolean(value)) {
                            ((Vine) materialData).putOnFace(BlockFace.EAST);
                        } else {
                            ((Vine) materialData).removeFromFace(BlockFace.EAST);
                        }
                        break;
                    case "north":
                        if (Boolean.parseBoolean(value)) {
                            ((Vine) materialData).putOnFace(BlockFace.NORTH);
                        } else {
                            ((Vine) materialData).removeFromFace(BlockFace.NORTH);
                        }
                        break;
                    case "south":
                        if (Boolean.parseBoolean(value)) {
                            ((Vine) materialData).putOnFace(BlockFace.SOUTH);
                        } else {
                            ((Vine) materialData).removeFromFace(BlockFace.SOUTH);
                        }
                        break;
                    case "west":
                        if (Boolean.parseBoolean(value)) {
                            ((Vine) materialData).putOnFace(BlockFace.WEST);
                        } else {
                            ((Vine) materialData).removeFromFace(BlockFace.WEST);
                        }
                        break;
                    // case "up": - computed in legacy
                }
            }
            // skipping walls as walls didn't have any values in legacy, everything was computed client-side
            // TODO: Note block (tile entity)
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return materialData;
    }

    public static @Nullable String get(@NotNull MaterialData materialData, @NotNull String key) {
        try {
            var materialName = materialData.getItemType().name();

            if ("waterlogged".equalsIgnoreCase(key)) {
                // Waterlogging was added in 1.13, but let's return false for 1.13 waterloggable block in older versions too
                switch (materialName) {
                    case "CHEST":
                    case "TRAPPED_CHEST":
                    case "WOOD_STAIRS":
                    case "COBBLESTONE_STAIRS":
                    case "BRICK_STAIRS":
                    case "SMOOTH_STAIRS":
                    case "SANDSTONE_STAIRS":
                    case "QUARTZ_STAIRS":
                    case "ACACIA_STAIRS":
                    case "DARK_OAK_STAIRS":
                    case "SPRUCE_WOOD_STAIRS":
                    case "BIRCH_WOOD_STAIRS":
                    case "JUNGLE_WOOD_STAIRS":
                    case "RED_SANDSTONE_STAIRS":
                    case "PURPUR_STAIRS":
                    case "NETHER_BRICK_STAIRS":
                    case "STEP":
                    case "WOOD_STEP":
                    case "STONE_SLAB2":
                    case "PURPUR_SLAB":
                    case "DOUBLE_STEP":
                    case "WOOD_DOUBLE_STEP":
                    case "DOUBLE_STONE_SLAB2":
                    case "PURPUR_DOUBLE_SLAB":
                    case "FENCE":
                    case "IRON_FENCE":
                    case "NETHER_FENCE":
                    case "SPRUCE_FENCE":
                    case "BIRCH_FENCE":
                    case "JUNGLE_FENCE":
                    case "DARK_OAK_FENCE":
                    case "ACACIA_FENCE":
                    case "COBBLE_WALL":
                    case "STAINED_GLASS_PANE":
                    case "THIN_GLASS":
                    case "ENDER_CHEST":
                    case "IRON_TRAPDOOR":
                    case "TRAP_DOOR":
                    case "LADDER":
                    case "SIGN_POST":
                    case "WALL_SIGN":
                        return "false";
                    default:
                        return null; // not waterloggable
                }
            }

            // Only features present in 1.13 should be backported if possible
            if ("ANVIL".equals(materialName)) {
                if ("facing".equalsIgnoreCase(key)) {
                    var value = materialData.getData() & 0x3;
                    switch (value) {
                        case 0x0:
                            return "west";
                        case 0x1:
                            return "north";
                        case 0x2:
                            return "east";
                        case 0x3:
                            return "south";
                    }
                }
            } else if (materialData instanceof Banner) {
                if (((Banner) materialData).isWallBanner()) {
                    if ("facing".equalsIgnoreCase(key)) {
                        return ((Banner) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    }
                } else {
                    if ("rotation".equalsIgnoreCase(key)) {
                        return String.valueOf(materialData.getData());
                    }
                }
            } else if (materialData instanceof Bed) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        return ((Bed) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "part":
                        return ((Bed) materialData).isHeadOfBed() ? "head" : "foot";
                    case "occupied":
                        return "false"; // Does not exist in legacy
                }
            } else if (materialData instanceof Crops
                    || materialData instanceof NetherWarts
                    || "CARROT".equals(materialName) // 1.8.8-1.9
                    || "POTATO".equals(materialName) // 1.8.8-1.9
                    || "CACTUS".equals(materialName)
                    || "SUGAR_CANE_BLOCK".equals(materialName)
                    || "CHORUS_FLOWER".equalsIgnoreCase(materialName)
                    || "FROSTED_ICE".equals(materialName)
                    || "MELON_STEM".equals(materialName)
                    || "PUMPKIN_STEM".equals(materialName)
            ) {
                if ("age".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if ("FIRE".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "age":
                        return String.valueOf(materialData.getData());
                    case "east":
                    case "north":
                    case "south":
                    case "up":
                    case "west":
                        return "true"; // we can't tell based on the data we have, but let's assume fire is everywhere :haha:
                }
            } else if ("BREWING_STAND".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "has_bottle_0":
                        return (materialData.getData() & 0x1) == 0x1 ? "true" : "false";
                    case "has_bottle_1":
                        return (materialData.getData() & 0x2) == 0x2 ? "true" : "false";
                    case "has_bottle_2":
                        return (materialData.getData() & 0x4) == 0x4 ? "true" : "false";
                }
            } else if (materialData instanceof Button) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "powered":
                        return String.valueOf(((Button) materialData).isPowered());
                    case "facing":
                        if (((Button) materialData).getFacing() != BlockFace.UP && ((Button) materialData).getFacing() != BlockFace.DOWN) { // only works when on side in legacy
                            return ((Button) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        }
                        return "north";
                    case "face":
                        if (((Button) materialData).getFacing() == BlockFace.UP) {
                            return "floor";
                        } else if (((Button) materialData).getFacing() == BlockFace.DOWN) {
                            return "ceiling";
                        } else {
                            return "wall";
                        }
                }
            } else if (materialData instanceof Cake) {
                if ("bites".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if (materialData instanceof Cauldron) {
                if ("level".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if (materialData instanceof DirectionalContainer) {
                if (materialData instanceof Chest || materialData instanceof EnderChest) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "facing":
                            return ((DirectionalContainer) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        case "type":
                            return "single"; // Can't determine in legacy without knowing if there's any chest
                    }
                } else if (materialData instanceof Dispenser) { // according to Bukkit API, Dropper is also Dispenser
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "facing":
                            return ((Dispenser) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        case "triggered":
                            return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                    }
                } else if (materialData instanceof Furnace) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "facing":
                            return ((Furnace) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        case "lit":
                            return "BURNING_FURNACE".equals(materialName) ? "true" : "false";
                    }
                }
            } else if (materialData instanceof CocoaPlant) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "age":
                        return String.valueOf(((CocoaPlant) materialData).getSize().ordinal());
                    case "facing":
                        return ((CocoaPlant) materialData).getAttachedFace().name().toLowerCase(Locale.ROOT);
                }
            } else if (materialData instanceof Command) {
                if (BukkitFeature.COMMAND_BLOCK_VERBOSE_BLOCK_STATE.isSupported()) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "conditional":
                            return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                        case "facing":
                            switch (materialData.getData() & 0x7) {
                                default:
                                case 0x0:
                                    return "down";
                                case 0x1:
                                    return "up";
                                case 0x2:
                                    return "north";
                                case 0x3:
                                    return "south";
                                case 0x4:
                                    return "west";
                                case 0x5:
                                    return "east";
                            }
                    }
                } else {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "conditional":
                            return "false";
                        case "facing":
                            return "north";
                    }
                }
            } else if ("DAYLIGHT_DETECTOR".equals(materialName) || "DAYLIGHT_DETECTOR_INVERTED".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "inverted":
                        return "DAYLIGHT_DETECTOR_INVERTED".equals(materialName) ? "true" : "false";
                    case "power":
                        return String.valueOf(materialData.getData());
                }
            } else if (materialData instanceof Door) {
                if (((Door) materialData).isTopHalf()) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "half":
                            return "upper";
                        case "powered":
                            return (materialData.getData() & 0x2) == 0x2 ? "true" : "false";
                        case "hinge":
                            return ((Door) materialData).getHinge() ? "right" : "left";
                        case "facing":
                            return "north"; // we can't get that from upper half
                        case "open":
                            return "false"; // we can't get that from upper half
                    }
                } else {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "half":
                            return "lower";
                        case "open":
                            return ((Door) materialData).isOpen() ? "true" : "false";
                        case "facing":
                            return ((Door) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        case "hinge":
                            return "left"; // we can't get that from lower half
                        case "powered":
                            return "false"; // we can't get that from lower half
                    }
                }
            } else if ("ENDER_PORTAL_FRAME".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "eye":
                        return (materialData.getData() & 0x4) == 0x4 ? "true" : "false";
                    case "facing":
                        switch (materialData.getData() & 0x3) {
                            default:
                            case 0x0:
                                return "south";
                            case 0x1:
                                return "west";
                            case 0x2:
                                return "north";
                            case 0x3:
                                return "east";
                        }
                }
            } else if ("END_ROD".equals(materialName)) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (materialData.getData()) {
                        default:
                        case 0x0:
                            return "down";
                        case 0x1:
                            return "up";
                        case 0x2:
                            return "north";
                        case 0x3:
                            return "south";
                        case 0x4:
                            return "west";
                        case 0x5:
                            return "east";
                    }
                }
            } else if ("SOIL".equals(materialName)) {
                if ("moisture".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            }
            // skipping fence as fence didn't have any values in legacy, everything was computed client-side
            else if (materialData instanceof Gate) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        // implementation in Gate is a bit sus and does not correspond with wiki
                        switch (materialData.getData() & 0x3) {
                            default:
                            case 0x0:
                                return "south";
                            case 0x1:
                                return "west";
                            case 0x2:
                                return "north";
                            case 0x3:
                                return "east";
                        }
                    case "open":
                        return ((Gate) materialData).isOpen() ? "true" : "false";
                    case "in_wall":
                        return "false"; // computed in legacy
                    case "powered":
                        return "false"; // does not exist in legacy
                }
            } else if ("DOUBLE_PLANT".equals(materialName)) {
                if ("half".equalsIgnoreCase(key)) {
                    return (materialData.getData() & 0x8) == 0x8 ? "upper" : "lower";
                }
            } else if (materialName.endsWith("GLAZED_TERRACOTTA")) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (materialData.getData()) {
                        default:
                        case 0x0:
                            return "south";
                        case 0x1:
                            return "west";
                        case 0x2:
                            return "north";
                        case 0x3:
                            return "east";
                    }
                }
            }
            // skipping Grass block, mycelium & podzol: no snowy property in legacy
            else if ("HOPPER".equalsIgnoreCase(materialName)) { // can't use o.b.m.Hopper due to compatibility with 1.8.8
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "enabled":
                        return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                    case "facing":
                        switch (materialData.getData() & 0x7) {
                            default:
                            case 0x0:
                                return "down";
                            case 0x2:
                                return "north";
                            case 0x3:
                                return "south";
                            case 0x4:
                                return "west";
                            case 0x5:
                                return "east";
                        }
                }
            }
            // skipping iron bars as iron bars didn't have any values in legacy, everything was computed client-side
            else if (materialData instanceof Pumpkin) {
                if ("facing".equalsIgnoreCase(key)) {
                    if (materialData.getData() == 0x3) { // getFacing() returns wrong facing for 0x3
                        return "west";
                    }
                    return ((Pumpkin) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                }
            } else if ("JUKEBOX".equals(materialName)) {
                if ("has_record".equalsIgnoreCase(key)) {
                    return materialData.getData() == 0x1 ? "true" : "false";
                }
            } else if (materialData instanceof Ladder) {
                if ("facing".equalsIgnoreCase(key)) {
                    return ((Ladder) materialData).getAttachedFace().name().toLowerCase(Locale.ROOT);
                }
            } else if ("WATER".equals(materialName) || "STATIONARY_WATER".equals(materialName) || "LAVA".equals(materialName) || "STATIONARY_LAVA".equals(materialName)) {
                if ("level".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if ("LEAVES".equals(materialName) || "LEAVES_2".equals(materialName)) { // o.b.m.Leaves can't be used because implementation in 1.8.8 was broken
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "persistent":
                        return (materialData.getData() & 0x4) == 0x4 ? "true" : "false";
                    case "distance":
                        return (materialData.getData() & 0x4) == 0x4 ? "1" : "6"; // I have no idea what to return and if check decay can be used for some guessing or not
                }
            } else if (materialData instanceof Lever) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "face":
                        switch (((Lever) materialData).getAttachedFace()) {
                            case UP:
                                return "ceiling";
                            case DOWN:
                                return "floor";
                            default:
                                return "wall";
                        }
                    case "facing":
                        switch (((Lever) materialData).getAttachedFace()) {
                            case UP:
                                return (materialData.getData() & 0x7) == 0x5 ? "east" : "south";
                            case DOWN:
                                return (materialData.getData() & 0x7) == 0x0 ? "east" : "south";
                            default:
                                return ((Lever) materialData).getAttachedFace().name().toLowerCase(Locale.ROOT);
                        }
                    case "powered":
                        return ((Lever) materialData).isPowered() ? "true" : "false";
                }
            } else if (materialData instanceof Tree) {
                if ("axis".equalsIgnoreCase(key)) {
                    if (((Tree) materialData).getDirection() != BlockFace.SELF) { // can't rotate wood/bark in legacy
                        switch (((Tree) materialData).getDirection()) {
                            case WEST:
                                return "x";
                            default:
                            case UP:
                                return "y";
                            case NORTH:
                                return "z";
                        }
                    } else {
                        return "y";
                    }
                }
            } else if (materialData instanceof Skull) {
                if (((Skull) materialData).getFacing() == BlockFace.SELF) {
                    // TODO: Rotation of mob head is stored inside Tile instead of Block Data in legacy
                } else {
                    if ("facing".equalsIgnoreCase(key)) {
                        switch (materialData.getData()) { // implementation was broken in some versions
                            default:
                            case 0x2:
                                return "north";
                            case 0x3:
                                return "south";
                            case 0x4:
                                return "west";
                            case 0x5:
                                return "east";
                        }
                    }
                }
            } else if (materialData instanceof Mushroom) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "east":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.EAST) ? "true" : "false";
                    case "down":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.DOWN) ? "true" : "false";
                    case "north":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.NORTH) ? "true" : "false";
                    case "south":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.SOUTH) ? "true" : "false";
                    case "up":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.UP) ? "true" : "false";
                    case "west":
                        return ((Mushroom) materialData).isFacePainted(BlockFace.WEST) ? "true" : "false";
                }
            } else if ("PORTAL".equals(materialName)) {
                if ("axis".equalsIgnoreCase(key)) {
                    return materialData.getData() == 0x1 ? "x" : "z";
                }
            }
            // skipping note block as note block didn't have any values in legacy, everything was computed
            else if ("OBSERVER".equals(materialName)) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (materialData.getData() & 0x7) {
                            default:
                            case 0x0:
                                return "down";
                            case 0x1:
                                return "up";
                            case 0x2:
                                return "north";
                            case 0x3:
                                return "south";
                            case 0x4:
                                return "west";
                            case 0x5:
                                return "east";
                        }
                    case "powered":
                        return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                }
            } else if (materialData instanceof PistonBaseMaterial) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "extended":
                        return ((PistonBaseMaterial) materialData).isPowered() ? "true" : "false";
                    case "facing":
                        return ((PistonBaseMaterial) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                }
            } else if (materialData instanceof PistonExtensionMaterial) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "type":
                        return ((PistonExtensionMaterial) materialData).isSticky() ? "sticky" : "normal";
                    case "facing":
                        return ((PistonExtensionMaterial) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "short":
                        return "false"; // not in legacy
                }
            } else if (materialData instanceof PressurePlate) {
                if ("powered".equalsIgnoreCase(key)) {
                    return materialData.getData() == 0x1 ? "true" : "false";
                }
            } else if ("IRON_PLATE".equals(materialName) || "GOLD_PLATE".equals(materialName)) {
                if ("power".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if ("QUARTZ_BLOCK".equals(materialName)) {
                if (materialData.getData() >= 0x2 && "axis".equalsIgnoreCase(key)) { // is pillar
                    switch (materialData.getData()) {
                        case 0x3:
                            return "x";
                        case 0x2:
                            return "y";
                        case 0x4:
                            return "z";
                    }
                }
            } else if ("PURPUR_PILLAR".equals(materialName) || "BONE_BLOCK".equals(materialName) || "HAY_BLOCK".equals(materialName)) {
                if ("axis".equalsIgnoreCase(key)) {
                    switch (materialData.getData()) {
                        case 0x4:
                            return "x";
                        case 0x0:
                            return "y";
                        case 0x8:
                            return "z";
                    }
                }
            } else if (materialData instanceof Rails) {
                if (materialData instanceof ExtendedRails) {
                    switch (key.toLowerCase(Locale.ROOT)) {
                        case "powered":
                            return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                        case "shape":
                            if (((ExtendedRails) materialData).isOnSlope()) {
                                switch (((ExtendedRails) materialData).getDirection()) {
                                    default:
                                    case NORTH:
                                        return "ascending_north";
                                    case EAST:
                                        return "ascending_east";
                                    case SOUTH:
                                        return "ascending_south";
                                    case WEST:
                                        return "ascending_west";
                                }
                            } else {
                                switch (((ExtendedRails) materialData).getDirection()) {
                                    default:
                                    case SOUTH:
                                        return "north_south";
                                    case EAST:
                                        return "north_east";
                                }
                            }
                    }
                } else {
                    if ("shape".equalsIgnoreCase(key)) {
                        if (((Rails) materialData).isOnSlope()) {
                            switch (((Rails) materialData).getDirection()) {
                                default:
                                case NORTH:
                                    return "ascending_north";
                                case EAST:
                                    return "ascending_east";
                                case SOUTH:
                                    return "ascending_south";
                                case WEST:
                                    return "ascending_west";
                            }
                        } else {
                            switch (((Rails) materialData).getDirection()) {
                                default:
                                case SOUTH:
                                    return "north_south";
                                case EAST:
                                    return "north_east";
                                case NORTH_EAST:
                                    return "south_west"; // Bukkit API inverts it for some reason
                                case NORTH_WEST:
                                    return "south_east"; // Bukkit API inverts it for some reason
                                case SOUTH_EAST:
                                    return "north_west"; // Bukkit API inverts it for some reason
                                case SOUTH_WEST:
                                    return "north_east"; // Bukkit API inverts it for some reason
                            }
                        }
                    }
                }
            } else if ("REDSTONE_COMPARATOR_OFF".equals(materialName) || "REDSTONE_COMPARATOR_ON".equals(materialName)) { // o.b.m.Comparator is too new
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        switch (materialData.getData() & 0x3) {
                            default:
                            case 0x0:
                                return "south";
                            case 0x1:
                                return "north";
                            case 0x2:
                                return "east";
                            case 0x3:
                                return "west";
                        }
                    case "mode":
                        return (materialData.getData() & 0x4) == 0x4 ? "subtract" : "compare";
                    case "powered":
                        return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                }
            } else if (materialData instanceof RedstoneWire) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "power":
                        return String.valueOf(materialData.getData());
                    case "east":
                    case "north":
                    case "south":
                    case "west":
                        return "side"; // computed in legacy
                }
            } else if ("REDSTONE_LAMP_OFF".equals(materialName) || "REDSTONE_ORE".equals(materialName)) {
                if ("lit".equalsIgnoreCase(key)) {
                    return "false";
                }
            } else if ("REDSTONE_LAMP_ON".equals(materialName) || "GLOWING_REDSTONE_ORE".equals(materialName)) {
                if ("lit".equalsIgnoreCase(key)) {
                    return "true";
                }
            } else if (materialData instanceof Diode) { // repeater
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "delay":
                        return String.valueOf(((Diode) materialData).getDelay());
                    case "facing":
                        return ((Diode) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "powered":
                        return "DIODE_BLOCK_ON".equals(materialName) ? "true" : "false";
                    case "locked":
                        return "false"; // not in legacy
                }
            } else if (materialData instanceof RedstoneTorch) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "lit":
                        return "REDSTONE_TORCH_ON".equals(materialName) ? "true" : "false";
                    case "facing":
                        if (((RedstoneTorch) materialData).getAttachedFace() != BlockFace.DOWN) {
                            return ((RedstoneTorch) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                        }
                        break;
                }
            } else if ("SAPLING".equals(materialName)) {
                if ("stage".equalsIgnoreCase(key)) {
                    return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                }
            } else if (materialName.endsWith("_SHULKER_BOX")) {
                if ("facing".equalsIgnoreCase(key)) {
                    switch (materialData.getData()) {
                        default:
                        case 0x0:
                            return "down";
                        case 0x1:
                            return "up";
                        case 0x2:
                            return "north";
                        case 0x3:
                            return "south";
                        case 0x4:
                            return "west";
                        case 0x5:
                            return "east";
                    }
                }
            } else if (materialData instanceof Sign) {
                if (((Sign) materialData).isWallSign()) {
                    if ("facing".equalsIgnoreCase(key)) {
                        return ((Sign) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    }
                } else {
                    if ("rotation".equalsIgnoreCase(key)) {
                        return String.valueOf(materialData.getData());
                    }
                }
            } else if ("STEP".equals(materialName) // the o.b.m implementation is a bit retarded and not completed
                    || "WOOD_STEP".equals(materialName)
                    || "STONE_SLAB2".equals(materialName)
                    || "PURPUR_SLAB".equals(materialName)
            ) {
                if ("type".equalsIgnoreCase(key)) {
                    return (materialData.getData() & 0x8) == 0x8 ? "top" : "bottom";
                }
            } else if ("DOUBLE_STEP".equals(materialName)
                    || "WOOD_DOUBLE_STEP".equals(materialName)
                    || "DOUBLE_STONE_SLAB2".equals(materialName)
                    || "PURPUR_DOUBLE_SLAB".equals(materialName)
            ) {
                if ("type".equalsIgnoreCase(key)) {
                    return "double";
                }
            } else if ("SNOW".equals(materialName)) {
                if ("layers".equalsIgnoreCase(key)) {
                    return String.valueOf(materialData.getData());
                }
            } else if (materialData instanceof Stairs) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        return ((Stairs) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "half":
                        return ((Stairs) materialData).isInverted() ? "top" : "bottom";
                    case "shape":
                        return "straight"; // computed in legacy
                }
            } else if ("STRUCTURE_BLOCK".equals(materialName)) {
                if ("mode".equalsIgnoreCase(key)) {
                    switch (materialData.getData()) {
                        default:
                        case 0x0:
                            return "data";
                        case 0x1:
                            return "save";
                        case 0x2:
                            return "load";
                        case 0x3:
                            return "corner";
                    }
                }
            } else if ("TNT".equals(materialName)) {
                if ("unstable".equalsIgnoreCase(key)) {
                    return materialData.getData() == 0x1 ? "true" : "false";
                }
            } else if (materialData instanceof Torch) {
                if ("facing".equalsIgnoreCase(key)) {
                    // flattening split floor and wall torches, so we should behave the same and not set the facing if this is not a wall torch
                    if (((Torch) materialData).getAttachedFace() != BlockFace.DOWN) {
                        return ((Torch) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    }
                }
            } else if (materialData instanceof TrapDoor) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "facing":
                        return ((TrapDoor) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "half":
                        return ((TrapDoor) materialData).isInverted() ? "top" : "bottom";
                    case "open":
                        return ((TrapDoor) materialData).isOpen() ? "true" : "false";
                    case "powered":
                        return "false"; // not in legacy
                }
            } else if (materialData instanceof Tripwire) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "attached":
                        return ((Tripwire) materialData).isActivated() ? "true" : "false";
                    case "disarmed":
                        return (materialData.getData() & 0x8) == 0x8 ? "true" : "false";
                    case "powered":
                        return ((Tripwire) materialData).isObjectTriggering() ? "true" : "false";
                    case "east":
                    case "north":
                    case "south":
                    case "west":
                        return "true"; // computed in legacy
                }
            } else if (materialData instanceof TripwireHook) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "attached":
                        return ((TripwireHook) materialData).isConnected() ? "true" : "false";
                    case "facing":
                        return ((TripwireHook) materialData).getFacing().name().toLowerCase(Locale.ROOT);
                    case "powered":
                        return ((TripwireHook) materialData).isActivated() ? "true" : "false";
                }
            } else if (materialData instanceof Vine) {
                switch (key.toLowerCase(Locale.ROOT)) {
                    case "east":
                        return ((Vine) materialData).isOnFace(BlockFace.EAST) ? "true" : "false";
                    case "north":
                        return ((Vine) materialData).isOnFace(BlockFace.NORTH) ? "true" : "false";
                    case "south":
                        return ((Vine) materialData).isOnFace(BlockFace.SOUTH) ? "true" : "false";
                    case "west":
                        return ((Vine) materialData).isOnFace(BlockFace.WEST) ? "true" : "false";
                    case "up":
                        return "true"; // computed
                }
            }
            // skipping walls as walls didn't have any values in legacy, everything was computed client-side
            // TODO: Note block (tile entity)
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static @NotNull Map<@NotNull String, String> get(@NotNull MaterialData materialData) {
        // TODO: actually implement it in some normal way instead of this
        var possibleKeys = List.of(
                "waterlogged",
                "facing",
                "rotation",
                "part",
                "occupied",
                "age",
                "east",
                "north",
                "south",
                "up",
                "west",
                "down",
                "has_bottle_0",
                "has_bottle_1",
                "has_bottle_2",
                "powered",
                "face",
                "bites",
                "level",
                "type",
                "triggered",
                "lit",
                "conditional",
                "inverted",
                "power",
                "half",
                "hinge",
                "open",
                "eye",
                "moisture",
                "in_wall",
                "enabled",
                "has_record",
                "persistent",
                "distance",
                "axis",
                "extended",
                "short",
                "shape",
                "mode",
                "delay",
                "locked",
                "stage",
                "layers",
                "unstable",
                "attached",
                "disarmed"
        );

        var map = new HashMap<String, String>();

        for (var possible : possibleKeys) {
            var resolve = get(materialData, possible);
            if (resolve != null) {
                map.put(possible, resolve);
            }
        }

        return Map.copyOf(map);
    }
}
