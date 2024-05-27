package com.harleylizard.trouble.common.brewing;

import com.harleylizard.trouble.common.MultiMap;
import com.harleylizard.trouble.common.ToilAndTrouble;
import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.ritual.ConfiguredRitual;
import com.harleylizard.trouble.common.ritual.Ritual;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public final class BrewingRitual implements HasIngredients {
    private static final MultiMap<Item, BrewingRitual> MULTI_MAP = MultiMap.mutableOf();

    public static final Codec<BrewingRitual> CODEC = RecordCodecBuilder.create(builder -> builder.group(ItemStack.CODEC.listOf().fieldOf("ingredients").forGetter(BrewingRitual::getIngredients), ResourceLocation.CODEC.fieldOf("configured-ritual").forGetter(brewingRitual -> brewingRitual.configuredRitual)).apply(builder, BrewingRitual::new));

    public static final SimpleSynchronousResourceReloadListener RELOAD_LISTENER = new SimpleSynchronousResourceReloadListener() {
        @Override
        public ResourceLocation getFabricId() {
            return ToilAndTrouble.resourceLocation("brewing_ritual");
        }

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            MULTI_MAP.clear();
            for (var resourceEntry : resourceManager.listResources("brewing/ritual", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
                var parsed = ToilAndTrouble.parseJson(CODEC, resourceEntry.getValue());
                if (parsed.isPresent()) {
                    var brewingRitual = parsed.get();
                    for (var ingredient : brewingRitual.ingredients) {
                        MULTI_MAP.put(ingredient.getItem(), brewingRitual);
                    }
                }
            }
        }
    };

    private final List<ItemStack> ingredients;
    private final ResourceLocation configuredRitual;

    private BrewingRitual(List<ItemStack> ingredients, ResourceLocation configuredRitual) {
        this.ingredients = ingredients;
        this.configuredRitual = configuredRitual;
    }

    public Ritual getRitual() {
        return ConfiguredRitual.getRitual(configuredRitual);
    }

    @Override
    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    @NotNull
    @Override
    public Iterator<ItemStack> iterator() {
        return ingredients.iterator();
    }

    public static BrewingRitual getRitual(BrewingCauldronBlockEntity.Ingredients ingredients) {
        for (var ingredient : ingredients) {
            for (var ritual : MULTI_MAP.get(ingredient.getItem())) {
                if (ingredients.canBrewRitual(ritual)) {
                    return ritual;
                }
            }
        }
        return null;
    }
}
