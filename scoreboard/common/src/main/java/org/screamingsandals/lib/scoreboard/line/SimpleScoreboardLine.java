package org.screamingsandals.lib.scoreboard.line;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.Component;

@Data
@AllArgsConstructor(staticName = "of")
public class SimpleScoreboardLine implements ScoreboardLine {
    private final String identifier;
    private Component text;
}
