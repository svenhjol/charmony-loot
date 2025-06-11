package svenhjol.charmony.loot.common;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.loot.LootMod;
import svenhjol.charmony.loot.common.features.chest_puzzles.ChestPuzzles;
import svenhjol.charmony.loot.common.features.secret_chests.SecretChests;

public final class CommonInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.common.CommonInitializer.init();

        // Prepare and run the mod.
        var mod = LootMod.instance();
        mod.addSidedFeature(ChestPuzzles.class);
        mod.addSidedFeature(SecretChests.class);
        mod.run(Side.Common);
    }
}
