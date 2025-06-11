package svenhjol.charmony.loot.common.features.chest_puzzles;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import svenhjol.charmony.api.chest_puzzles.ChestPuzzlesApi;
import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirement;
import svenhjol.charmony.api.chest_puzzles.ItemPuzzleRequirementProvider;
import svenhjol.charmony.api.secret_chests.SecretChestPuzzleMenuProvider;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.loot.common.features.chest_puzzles.menus.ClockPuzzleMenu;
import svenhjol.charmony.loot.common.features.chest_puzzles.menus.ItemPuzzleMenu;
import svenhjol.charmony.loot.common.features.chest_puzzles.menus.MoonPuzzleMenu;

import java.util.*;
import java.util.function.Supplier;

public class Registers extends Setup<ChestPuzzles> {
    public final Supplier<MenuType<MoonPuzzleMenu>> moonPuzzleMenu;
    public final Supplier<MenuType<ClockPuzzleMenu>> clockPuzzleMenu;
    public final Map<Integer, Supplier<MenuType<ItemPuzzleMenu>>> itemPuzzleMenus = new HashMap<>();
    public final Map<String, SecretChestPuzzleMenuProvider> puzzleMenuProviders = new HashMap<>();
    public final List<ItemPuzzleRequirement> itemPuzzleRequirements = new ArrayList<>();

    public Registers(ChestPuzzles feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);

        moonPuzzleMenu = registry.menuType("moon_puzzle",
            () -> new MenuType<>(MoonPuzzleMenu::new, FeatureFlags.VANILLA_SET));

        clockPuzzleMenu = registry.menuType("clock_puzzle",
            () -> new MenuType<>(ClockPuzzleMenu::new, FeatureFlags.VANILLA_SET));

        for (var i = 1; i <= Constants.MAX_ITEM_SLOTS; i++) {
            var slots = i;
            itemPuzzleMenus.put(i, registry.menuType("item_puzzle_" + i + "_slots",
                () -> new MenuType<>((id, inv) -> new ItemPuzzleMenu(id, inv, slots), FeatureFlags.VANILLA_SET)));
        }
    }

    @Override
    public Runnable boot() {
        return () -> {
            Api.consume(SecretChestPuzzleMenuProvider.class,
                provider -> puzzleMenuProviders.put(provider.getMenuProviderId(), provider));

            Api.consume(ItemPuzzleRequirementProvider.class,
                provider -> itemPuzzleRequirements.addAll(provider.getItemPuzzleTags()));

            ChestPuzzlesApi.Impl.menuProviderCreator(((serverLevel, chest, syncId, inventory, material) -> {
                if (feature().enabled()) {
                    var opt = feature().handlers.getMenuProvider(serverLevel, chest, syncId, inventory, material);
                    if (opt.isPresent() && opt.get() instanceof AbstractContainerMenu menu) {
                        return Optional.of(menu);
                    }
                }
                return Optional.empty();
            }));
        };
    }
}
