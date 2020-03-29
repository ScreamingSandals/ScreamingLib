package org.screamingsandals.lib.scoreboards.content;

import lombok.Data;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.*;

@Data
public class ContentAnimation {
    private Map<Scoreboard, Animation> activeAnimations = new HashMap<>();

    @Data
    public static class Animation {
        private final Scoreboard scoreboard;
        private SortedSet<String> lines = new TreeSet<>();

            public void addLine(String line) {
                lines.add(line);
        }
    }
}
