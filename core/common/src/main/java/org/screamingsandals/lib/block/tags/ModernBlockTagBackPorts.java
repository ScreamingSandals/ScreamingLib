package org.screamingsandals.lib.block.tags;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

import java.util.ArrayList;
import java.util.List;

@LimitedVersionSupport(">= 1.13")
public class ModernBlockTagBackPorts {
    @NotNull
    public static List<String> getPortedTags(@NotNull BlockTypeHolder blockType) {
        if (!Server.isVersion(1, 13)) {
            return List.of(); // no legacy version here
        }

        var list = new ArrayList<String>();

        if (!Server.isVersion(1, 14)) {
            if (blockType.is("sign")) {
                list.add("standing_signs");
                list.add("signs");
            }
            if (blockType.is("wall_sign")) {
                list.add("wall_signs");
                list.add("signs");
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
                list.add("small_flowers");
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
                list.add("beds");
            }
            if (blockType.is(
                    "oak_fence",
                    "acacia_fence",
                    "dark_oak_fence",
                    "spruce_fence",
                    "birch_fence",
                    "jungle_fence",
                    "nether_brick_fence")) {
                list.add("fences");
            }
            if (blockType.is("barrier", "bedrock", "end_portal", "end_portal_frame", "end_gateway", "command_block", "repeating_command_block", "chain_command_block", "structure_block", "jigsaw", "moving_piston", "obsidian", "end_stone", "iron_bars")) {
                list.add("dragon_immune");
            }
            // TODO: wither_immune
        }

        if (!Server.isVersion(1, 15)) {

        }

        if (!Server.isVersion(1, 16)) {

        }

        if (!Server.isVersion(1, 17)) {

        }

        if (!Server.isVersion(1, 18)) {

        }

        if (!Server.isVersion(1, 19)) {

        }
        return list;
    }
}
