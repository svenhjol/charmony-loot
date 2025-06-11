package svenhjol.charmony.loot.common.mixins;

import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.MixinConfig;
import svenhjol.charmony.loot.LootMod;

public class CommonMixinConfig extends MixinConfig {
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
        return Side.Common;
    }
}
