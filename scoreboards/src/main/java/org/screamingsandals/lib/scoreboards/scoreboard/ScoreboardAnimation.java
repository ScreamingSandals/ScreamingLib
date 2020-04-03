package org.screamingsandals.lib.scoreboards.scoreboard;

import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Data
public class ScoreboardAnimation {
    private Map<Integer, Animation> activeAnimations = new HashMap<>();

    @Data
    public static class Animation {
        private final Plugin plugin;
        private final Scoreboard scoreboard;
        private final long animationTicks;
        private final int animatedLine;

        private List<String> animationContent = new ArrayList<>();
        private String active;
        private String next;
        private BukkitTask animationTask;

        public void addLine(String animation) {
            animationContent.add(animation);
        }

        public void runAnimation() {
            Iterator<String> animationLines = animationContent.iterator();
            animationTask = new BukkitRunnable() {
                @Override
                public void run() {
                    if (active == null && next == null) {
                        active = animationLines.next();
                    } else {
                        active = next;
                    }
                    next = animationLines.next();

                }
            }.runTaskTimer(plugin, 1L, animationTicks);
        }
    }
}
