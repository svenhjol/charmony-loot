package svenhjol.charmony.loot.common.features.secret_chests;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.api.secret_chests.SecretChestDefinition;
import svenhjol.charmony.api.secret_chests.SecretChestDefinitionProvider;
import svenhjol.charmony.api.secret_chests.SecretChestsApi;
import svenhjol.charmony.api.stone_chests.StoneChestBlockEntity;
import svenhjol.charmony.api.stone_chests.StoneChestsApi;
import svenhjol.charmony.core.Api;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.loot.common.features.chest_puzzles.ChestPuzzles;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registers extends Setup<SecretChests> {
    public static final String STRUCTURE_ID = "secret_chest";
    public static final String PIECE_ID = "secret_chest_piece";
    public static final String DEFINITION_ID = "secret_chest_definition";

    public final Supplier<StructureType<SecretChestStructure>> structureType;
    public final Supplier<StructurePieceType> structurePiece;
    public Codec<SecretChestDefinition> secretChestCodec;
    public final Map<String, SecretChestDefinition> secretChestDefinitions = new HashMap<>();

    public Registers(SecretChests feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);

        structureType = registry.structure(STRUCTURE_ID, () -> SecretChestStructure.CODEC);
        structurePiece = registry.structurePiece(PIECE_ID, () -> SecretChestPiece::new);

        // Consumer of secret chest definitions.
        Api.consume(SecretChestDefinitionProvider.class, provider -> {
            for (var definition : provider.getSecretChestDefinitions()) {
                this.secretChestDefinitions.put(definition.name(), definition);
            }

            secretChestCodec = StringRepresentable.fromValues(
                () -> secretChestDefinitions.values().toArray(new SecretChestDefinition[0]));
        });
    }

    @Override
    public Runnable boot() {
        return () -> SecretChestsApi.Impl.chestCreator((definition, level, random, pos, waterlogged, facing) -> {
            var material = definition.material();
            var block = StoneChestsApi.instance().getBlock(material).orElse(null);
            if (block == null) {
                return false;
            }

            var lootTable = SecretChests.feature().handlers.randomLootTable(definition, random).orElse(null);

            if (lootTable == null) {
                log().debug("No loot tables for secret chest");
                return false;
            }

            var state = StructurePiece.reorient(level, pos, block.defaultBlockState());
            if (waterlogged) {
                state = state.setValue(ChestBlock.WATERLOGGED, true);
            }
            if (facing != null) {
                state = state.setValue(ChestBlock.FACING, facing);
            }

            level.setBlock(pos, state, 2);
            if (!(level.getBlockEntity(pos) instanceof StoneChestBlockEntity chest)) {
                return false;
            }
            if (!(chest instanceof RandomizableContainerBlockEntity lootChest)) {
                return false;
            }

            // If the puzzles feature is enabled then add a puzzle to the chest entity.
            // Add the loot table to the "unlocked loot table" property so that
            // if the chest is broken it won't drop anything.
            if (Mod.getSidedFeature(ChestPuzzles.class).enabled()) {
                if (!definition.puzzleMenus().isEmpty()) {
                    chest.setCustomDefinition(definition.name());
                    chest.lock();
                } else {
                    log().warn("No menu providers");
                }
            }

            // If the puzzles feature isn't enabled or has failed then the chest
            // will not be locked. Set the custom loot table directly.
            if (!chest.isLocked()) {
                lootChest.setLootTable(lootTable);
                lootChest.setChanged();
            }

            log().debug("Generated " + material.getSerializedName() + " chest at " + pos);
            return true;
        });
    }
}
