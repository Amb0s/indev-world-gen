package ambos.indevworldgen;

import ambos.indevworldgen.gen.AlphaChunkGeneratorConfig;
import ambos.indevworldgen.gen.AlphaChunkGeneratorType;
import ambos.indevworldgen.gen.IndevChunkGeneratorConfig;
import ambos.indevworldgen.gen.IndevChunkGeneratorType;
import ambos.indevworldgen.gen.feature.IndevHouseFeature;
import ambos.indevworldgen.structure.IndevHouseGenerator;
import ambos.indevworldgen.worldtype.OldWorldType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;

public class IndevWorldGen implements ModInitializer {
	private static final String MOD_ID = "indevworldgen";
	
	public static IndevChunkGeneratorType INDEV_CGT;
	public static AlphaChunkGeneratorType ALPHA_CGT;
	
	static OldWorldType<?> loadMeOnClientPls; // make sure world types are loaded on client by referencing a field in onInitialize()

	public static final StructurePieceType indevHouseStructurePieceType = Registry.register(Registry.STRUCTURE_PIECE, "indev_house_piece", IndevHouseGenerator.Piece::new);
	public static final StructureFeature<DefaultFeatureConfig> indevHouseFeature = Registry.register(Registry.FEATURE, "indev_house_feature", new IndevHouseFeature());
	public static final StructureFeature<?> indevHouseStructure = Registry.register(Registry.STRUCTURE_FEATURE, "indev_house_structure", indevHouseFeature);
	
	@Override
	public void onInitialize() {
		loadMeOnClientPls = OldWorldType.INDEV;

		INDEV_CGT = Registry.register(Registry.CHUNK_GENERATOR_TYPE, IndevWorldGen.id("old_indev"), new IndevChunkGeneratorType(false, () -> new IndevChunkGeneratorConfig()));
		ALPHA_CGT = Registry.register(Registry.CHUNK_GENERATOR_TYPE, IndevWorldGen.id("old_alpha"), new AlphaChunkGeneratorType(false, () -> new AlphaChunkGeneratorConfig()));

		Feature.STRUCTURES.put("Indev House Feature", indevHouseFeature);

		for(Biome biome : Registry.BIOME)
		{
			if(biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER)
			{
				biome.addStructureFeature(indevHouseFeature, new DefaultFeatureConfig());
				biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Biome.configureFeature(indevHouseFeature, new DefaultFeatureConfig(), Decorator.CHANCE_PASSTHROUGH, new ChanceDecoratorConfig(0)));
			}
		}
	}

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}
}
