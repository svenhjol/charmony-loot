package svenhjol.charmony.loot.client;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.loot.LootMod;
import svenhjol.charmony.loot.client.features.chest_puzzles.ChestPuzzles;

public final class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Ensure charmony is launched first.
        svenhjol.charmony.core.client.ClientInitializer.init();

        // Prepare and run the mod.
        var mod = LootMod.instance();
        mod.addSidedFeature(ChestPuzzles.class);
        mod.run(Side.Client);
    }
}
