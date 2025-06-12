package svenhjol.charmony.loot.common.features.secret_chests;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressPieces;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.api.secret_chests.SecretChestDefinition;
import svenhjol.charmony.api.secret_chests.SecretChestPlacement;
import svenhjol.charmony.api.secret_chests.SecretChestSideEffects;
import svenhjol.charmony.api.secret_chests.SecretChestsApi;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.TagHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Handlers extends Setup<SecretChests> {
    public @Nullable String template; // Used by End Cities
    public @Nullable ResourceLocation jigsawTemplate; // Used by Bastions

    public static final List<String> END_CITY_TEMPLATES = List.of(
        "third_roof", "bridge_piece", "base_floor"
    );

    public Handlers(SecretChests feature) {
        super(feature);
    }

    public Optional<SecretChestDefinition> getDefinition(String name) {
        return Optional.ofNullable(feature().registers.secretChestDefinitions.get(name));
    }

    public Optional<ResourceKey<LootTable>> randomLootTable(SecretChestDefinition definition, RandomSource random) {
        var lootTables = new ArrayList<>(definition.lootTables());

        if (lootTables.isEmpty()) {
            log().debug("No loot tables for secret chest definition: " + definition);
            return Optional.empty();
        }

        Util.shuffle(lootTables, random);
        return Optional.of(lootTables.getFirst());
    }

    public Optional<SecretChestSideEffects> randomSideEffect(SecretChestDefinition definition, RandomSource random) {
        var sideEffects = new ArrayList<>(definition.sideEffects());

        if (sideEffects.isEmpty()) {
            log().debug("No side effects for secret chest definition: " + definition);
            return Optional.empty();
        }

        Util.shuffle(sideEffects, random);
        return Optional.of(sideEffects.getFirst());
    }

    public Optional<String> randomPuzzleMenuId(SecretChestDefinition definition, RandomSource random) {
        var puzzleMenus = new ArrayList<>(definition.puzzleMenus());

        if (puzzleMenus.isEmpty()) {
            log().debug("No puzzle menu IDs for secret chest definition: " + definition);
            return Optional.empty();
        }

        Util.shuffle(puzzleMenus, random);
        return Optional.of(puzzleMenus.getFirst());
    }

    public void createNetherFortressChest(StructurePiece piece, WorldGenLevel level, RandomSource random) {
        if (random.nextInt(1000) < feature().fortressChance()) {
            var definition = definitionForPlacement(SecretChestPlacement.Fortress, random).orElse(null);
            if (definition == null) return;

            BlockPos pos;
            if (piece instanceof NetherFortressPieces.CastleSmallCorridorPiece) {
                pos = piece.getWorldPos(random.nextBoolean() ? 1 : 3, 2, 2);
            } else if (piece instanceof NetherFortressPieces.BridgeStraight) {
                pos = piece.getWorldPos(random.nextBoolean() ? 1 : 3, 5, 2);
            } else {
                return;
            }

            var stateBelow = level.getBlockState(pos.below());
            if (stateBelow.is(Tags.GENERATES_FORTRESS_CHESTS)) {
                SecretChestsApi.instance().createChest(definition, level, random, pos, false, null);
            }
        }
    }

    public boolean createEndCityChest(ServerLevelAccessor level, BlockPos pos, BlockState stateBelow) {
        if (template != null && END_CITY_TEMPLATES.contains(template) && stateBelow.is(Tags.GENERATES_END_CITY_CHESTS)) {
            if (level.getRandom().nextInt(1000) < feature().endCityChance()) {
                var definition = definitionForPlacement(SecretChestPlacement.EndCity, level.getRandom()).orElse(null);
                if (definition == null) return false;
                return SecretChestsApi.instance().createChest(definition, level, level.getRandom(), pos, false, null);
            }
        }
        return false;
    }

    public boolean createBastionChest(ServerLevelAccessor level, BlockPos pos, BlockState stateBelow) {
        if (jigsawTemplate != null && jigsawTemplate.getPath().contains("bastion") && stateBelow.is(Tags.GENERATES_BASTION_CHESTS)) {
            if (level.getRandom().nextInt(1000) < feature().bastionChance()) {
                var definition = definitionForPlacement(SecretChestPlacement.Bastion, level.getRandom()).orElse(null);
                if (definition == null) return false;
                return SecretChestsApi.instance().createChest(definition, level, level.getRandom(), pos, false, null);
            }
        }
        return false;
    }

    public void createFlowerRing(WorldGenLevel level, BlockPos pos, RandomSource random, TagKey<Block> flowers) {
        if (!feature().flowerRings()) return;

        var values = new ArrayList<>(TagHelper.getValues(level.registryAccess().lookupOrThrow(Registries.BLOCK), flowers));
        if (values.isEmpty()) return;
        Util.shuffle(values, random);
        var flower = values.getFirst();

        var heightMap = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
        var radius = random.nextIntBetweenInclusive(6, 10);
        var heightTolerance = 5;

        for (int i = 0; i < 360; i += (28 + random.nextInt(5))) {
            var x = (int)(radius * Math.cos(i * Math.PI / 180));
            var z = (int)(radius * Math.sin(i * Math.PI / 180));

            for (int y = -heightTolerance; y < heightTolerance; y++) {
                var tryPos = heightMap.offset(x, y, z);
                var tryState = level.getBlockState(tryPos);
                var tryStateBelow = level.getBlockState(tryPos.below());
                var validState = tryState.isAir() || (tryState.canBeReplaced() && tryState.getFluidState().isEmpty());

                if (tryStateBelow.is(Tags.GENERATES_FLOWER_RINGS) && validState) {
                    level.setBlock(tryPos, flower.defaultBlockState(), 3);
                }
            }
        }
    }

    public Optional<SecretChestDefinition> definitionForPlacement(SecretChestPlacement placement, RandomSource random) {
        var definitions = new ArrayList<>(feature().registers.secretChestDefinitions.values().stream()
            .filter(def -> def.placement() == placement).toList());

        if (definitions.isEmpty()) return Optional.empty();
        Util.shuffle(definitions, random);
        return Optional.of(definitions.getFirst());
    }
}
