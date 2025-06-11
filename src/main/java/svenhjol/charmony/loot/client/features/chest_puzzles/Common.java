package svenhjol.charmony.loot.client.features.chest_puzzles;

import svenhjol.charmony.loot.common.features.chest_puzzles.Registers;
import svenhjol.charmony.loot.common.features.chest_puzzles.ChestPuzzles;

public class Common {
    public final ChestPuzzles feature;
    public final Registers registers;

    public Common() {
        feature = ChestPuzzles.feature();
        registers = feature.registers;
    }
}
