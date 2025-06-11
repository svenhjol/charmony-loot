package svenhjol.charmony.loot;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.api.core.ModDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;

@ModDefinition(
    id = LootMod.ID,
    sides = {Side.Client, Side.Common},
    name = "Loot",
    description = "Adds loot to the world.")
public final class LootMod extends Mod {
    public static final String ID = "charmony-loot";
    private static LootMod instance;

    private LootMod() {}

    public static LootMod instance() {
        if (instance == null) {
            instance = new LootMod();
        }
        return instance;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}