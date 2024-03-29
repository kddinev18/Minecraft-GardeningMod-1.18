package com.sAnimVsa.gardening_mod.world.gen;

import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Arrays;

public class ModOreGeneration {
    public static void generateOres(final BiomeLoadingEvent event) {
        spawnOreInAllBiomes(OreType.PETRIFIED_PLANT_ORE, event, LevelStem.OVERWORLD.toString());

    }

    private static OreConfiguration getOverworldFeatureConfig(OreType ore) {
        return new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static OreConfiguration getNetherFeatureConfig(OreType ore) {
        return new OreConfiguration(OreFeatures.NETHER_ORE_REPLACEABLES,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static OreConfiguration getEndFeatureConfig(OreType ore) {
        return new OreConfiguration(new BlockMatchTest(Blocks.END_STONE),
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());
    }

    private static PlacedFeature makeOreFeature(OreType ore, String dimensionToSpawnIn) {
        OreConfiguration oreFeatureConfig = null;

        if(dimensionToSpawnIn.equals(LevelStem.OVERWORLD.toString())) {
            oreFeatureConfig = getOverworldFeatureConfig(ore);
        } else if(dimensionToSpawnIn.equals(LevelStem.NETHER.toString())) {
            oreFeatureConfig = getNetherFeatureConfig(ore);
        } else if(dimensionToSpawnIn.equals(LevelStem.END.toString())) {
            oreFeatureConfig = getEndFeatureConfig(ore);
        }

        HeightRangePlacement heightRangePlacement = HeightRangePlacement.uniform(VerticalAnchor.absolute(ore.getMinHeight()),
                VerticalAnchor.absolute(ore.getMaxHeight()));

        return registerOreFeature(oreFeatureConfig, heightRangePlacement);
    }

    private static void spawnOreInOverworldInGivenBiomes(OreType ore, final BiomeLoadingEvent event, Biome... biomesToSpawnIn) {
        OreConfiguration oreFeatureConfig = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());

        HeightRangePlacement heightRangePlacement = HeightRangePlacement.uniform(VerticalAnchor.absolute(ore.getMinHeight()),
                VerticalAnchor.absolute(ore.getMaxHeight()));


        PlacedFeature oreFeature = registerOreFeature(oreFeatureConfig, heightRangePlacement);

        if (Arrays.stream(biomesToSpawnIn).anyMatch(b -> b.getRegistryName().equals(event.getName()))) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, oreFeature);
        }
    }

    private static void spawnOreInOverworldInAllBiomes(OreType ore, final BiomeLoadingEvent event) {
        OreConfiguration oreFeatureConfig = new OreConfiguration(OreFeatures.STONE_ORE_REPLACEABLES,
                ore.getBlock().get().defaultBlockState(), ore.getMaxVeinSize());

        HeightRangePlacement heightRangePlacement = HeightRangePlacement.uniform(VerticalAnchor.absolute(ore.getMinHeight()),
                VerticalAnchor.absolute(ore.getMaxHeight()));

        PlacedFeature oreFeature = registerOreFeature(oreFeatureConfig, heightRangePlacement);

        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, oreFeature);
    }

    private static void spawnOreInSpecificModBiome(Biome biomeToSpawnIn, OreType currentOreType,
                                                   final BiomeLoadingEvent event, String dimension) {
        if(event.getName().toString().contains(biomeToSpawnIn.getRegistryName().toString())) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    makeOreFeature(currentOreType, dimension));
        }
    }

    private static void spawnOreInSpecificBiome(ResourceKey<Biome> biomeToSpawnIn, OreType currentOreType,
                                                final BiomeLoadingEvent event, String dimension) {
        if(event.getName().toString().contains(biomeToSpawnIn.location().toString())) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    makeOreFeature(currentOreType, dimension));
        }
    }

    private static void spawnOreInAllBiomes(OreType currentOreType, final BiomeLoadingEvent event, String dimension) {
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                makeOreFeature(currentOreType, dimension));
    }

    private static PlacedFeature registerOreFeature(OreConfiguration oreFeatureConfig,
                                                    HeightRangePlacement heightRangePlacement) {
        return Feature.ORE.configured(oreFeatureConfig).placed(heightRangePlacement);
    }
}