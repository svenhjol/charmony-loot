package svenhjol.charmony.loot.common.features.chest_puzzles;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootTable;
import svenhjol.charmony.core.Charmony;

public final class Tags {
    public static final ResourceKey<LootTable> LOOT_TRASH = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("chests/trash"));

    public static final TagKey<Item> ENCHANTABLE_ITEMS = TagKey.create(Registries.ITEM,
        Charmony.id("enchantable_items"));

    public static final TagKey<Enchantment> ENCHANTMENTS_FOR_BOOKS = TagKey.create(Registries.ENCHANTMENT,
        Charmony.id("on_books"));

    public static final TagKey<Enchantment> ENCHANTMENTS_FOR_ITEMS = TagKey.create(Registries.ENCHANTMENT,
        Charmony.id("on_items"));

    public static final TagKey<EntityType<?>> END_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("item_puzzles/end_monsters"));

    public static final TagKey<EntityType<?>> NETHER_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("item_puzzles/nether_monsters"));

    public static final TagKey<EntityType<?>> OVERWORLD_MONSTERS = TagKey.create(Registries.ENTITY_TYPE,
        Charmony.id("item_puzzles/overworld_monsters"));

    public static final ResourceKey<LootTable> PUZZLE_CANDLES = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/candles"));

    public static final ResourceKey<LootTable> PUZZLE_ENDER = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/ender"));

    public static final ResourceKey<LootTable> PUZZLE_GEMS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/gems"));

    public static final ResourceKey<LootTable> PUZZLE_GLAZED_TERRACOTTA = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/glazed_terracotta"));

    public static final ResourceKey<LootTable> PUZZLE_INGOTS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/ingots"));

    public static final ResourceKey<LootTable> PUZZLE_LOGS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/logs"));

    public static final ResourceKey<LootTable> PUZZLE_NETHER = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/nether"));

    public static final ResourceKey<LootTable> PUZZLE_SHERDS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/sherds"));

    public static final ResourceKey<LootTable> PUZZLE_STAINED_GLASS = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/stained_glass"));

    public static final ResourceKey<LootTable> PUZZLE_STONES = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/stones"));

    public static final ResourceKey<LootTable> PUZZLE_TERRACOTTA = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/terracotta"));

    public static final ResourceKey<LootTable> PUZZLE_WOOL = ResourceKey.create(Registries.LOOT_TABLE,
        Charmony.id("item_puzzles/wool"));
}
