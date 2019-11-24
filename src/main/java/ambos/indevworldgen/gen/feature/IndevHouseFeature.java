package ambos.indevworldgen.gen.feature;

import ambos.indevworldgen.IndevWorldGen;
import ambos.indevworldgen.gen.IndevChunkGenerator;
import ambos.indevworldgen.structure.IndevHouseGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class IndevHouseFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    public IndevHouseFeature()
    {
        super(DefaultFeatureConfig::deserialize);
    }

    @Override
    public int getRadius()
    {
        return 0;
    }

    @Override
    public StructureStartFactory getStructureStartFactory()
    {
        return IndevHouseStart::new;
    }

    @Override
    public String getName()
    {
        return "indev_house";
    }

    @Override
    public int getSeedModifier()
    {
        return 3;
    }

    @Override
    public boolean shouldStartAt(ChunkGenerator<?> chunkGenerator_1, Random random_1, int int_1, int int_2)
    {
        return chunkGenerator_1 instanceof IndevChunkGenerator;
    }

    public static class IndevHouseStart extends StructureStart
    {

        public IndevHouseStart(StructureFeature<?> structureFeature_1, int int_1, int int_2, Biome biome_1, MutableIntBoundingBox mutableIntBoundingBox_1, int int_3, long long_1)
        {
            super(structureFeature_1, int_1, int_2, biome_1, mutableIntBoundingBox_1, int_3, long_1);
        }

        @Override
        public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome)
        {
            DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, IndevWorldGen.indevHouseFeature);

            int x = chunkX * 16;
            int z = chunkZ * 16;

            BlockPos startingPos = new BlockPos(x, 0, z);
            BlockRotation blockRotation = BlockRotation.values()[this.random.nextInt(BlockRotation.values().length)];
            IndevHouseGenerator.addParts(structureManager, startingPos, blockRotation, this.children, this.random, defaultFeatureConfig);
            this.setBoundingBoxFromChildren();
        }
    }
}
