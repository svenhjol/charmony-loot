package svenhjol.charmony.loot.client.mixins;

import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.loot.LootMod;

public class ClientMixinConfig extends MixinConfig {
    @Override
    protected String modId() {
        return LootMod.ID;
    }

    @Override
    protected String modRoot() {
        return "svenhjol.charmony.loot";
    }

    @Override
    protected Side side() {
        return Side.Client;
    }
}
