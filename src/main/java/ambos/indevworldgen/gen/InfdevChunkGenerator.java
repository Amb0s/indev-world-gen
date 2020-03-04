package ambos.indevworldgen.gen;

import java.util.Random;

import ambos.indevworldgen.util.noise.OctaveInfdevNoiseSampler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.IWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;

public class InfdevChunkGenerator extends SurfaceChunkGenerator<ChunkGeneratorConfig> {
    private OctaveInfdevNoiseSampler terrainAlt1Generator;
    private OctaveInfdevNoiseSampler terrainAlt2Generator;
    private OctaveInfdevNoiseSampler terrainGenerator;
    private OctaveInfdevNoiseSampler noiseSandGen;
    private OctaveInfdevNoiseSampler rockSandGen;
    private OctaveInfdevNoiseSampler treeGen;

    private double[] heightNoise;

    private Random rand = new Random(world.getSeed());

    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private static final BlockState STONE = Blocks.STONE.getDefaultState();
    private static final BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.getDefaultState();
    private static final BlockState DIRT = Blocks.DIRT.getDefaultState();
    private static final BlockState WATER = Blocks.WATER.getDefaultState();
    private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    private static final BlockState SAND = Blocks.SAND.getDefaultState();

    public InfdevChunkGenerator(IWorld world, BiomeSource biomeSource, ChunkGeneratorConfig config) {
        super(world, biomeSource, 4, 8, 256, config, true);

        terrainAlt1Generator = new OctaveInfdevNoiseSampler(rand, 16);
        terrainAlt2Generator = new OctaveInfdevNoiseSampler(rand, 16);
        terrainGenerator = new OctaveInfdevNoiseSampler(rand, 8);
        noiseSandGen = new OctaveInfdevNoiseSampler(rand, 4);
        rockSandGen = new OctaveInfdevNoiseSampler(rand, 4);
        //new OctaveInfdevNoiseSampler(rand, 5);
        treeGen = new OctaveInfdevNoiseSampler(rand, 5);
    }

    @Override
    public void buildSurface(Chunk chunk){
        this.replaceSurfaceBlocks(chunk);
    }

    private void replaceSurfaceBlocks(Chunk chunk){
        BlockPos.Mutable pos = new BlockPos.Mutable();

        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;

        final int seaLevel = this.getSeaLevel();
        final double oneSixteenth = 0.03125D;

        for(int k = 0; k < 4; k++)
        {
            for(int i1 = 0; i1 < 4; i1++)
            {
                double ad[][] = new double[33][4];
                int k1 = (chunkX << 2) + k;
                int i2 = (chunkZ << 2) + i1;
                for(int j2 = 0; j2 < ad.length; j2++)
                {
                    ad[j2][0] = generateNoise(k1, j2, i2);
                    ad[j2][1] = generateNoise(k1, j2, i2 + 1);
                    ad[j2][2] = generateNoise(k1 + 1, j2, i2);
                    ad[j2][3] = generateNoise(k1 + 1, j2, i2 + 1);
                }

                for(int l1 = 0; l1 < 32; l1++)
                {
                    double d3 = ad[l1][0];
                    double d5 = ad[l1][1];
                    double d6 = ad[l1][2];
                    double d7 = ad[l1][3];
                    double d8 = ad[l1 + 1][0];
                    double d9 = ad[l1 + 1][1];
                    double d10 = ad[l1 + 1][2];
                    double d11 = ad[l1 + 1][3];
                    for(int i4 = 0; i4 < 4; i4++)
                    {
                        double d12 = (double)i4 / 4D;
                        double d13 = d3 + (d8 - d3) * d12;
                        double d14 = d5 + (d9 - d5) * d12;
                        double d15 = d6 + (d10 - d6) * d12;
                        double d16 = d7 + (d11 - d7) * d12;
                        for(int j4 = 0; j4 < 4; j4++)
                        {
                            double d17 = (double)j4 / 4D;
                            double d18 = d13 + (d15 - d13) * d17;
                            double d19 = d14 + (d16 - d14) * d17;
                            int k4 = j4 + (k << 2) << 11 | 0 + (i1 << 2) << 7 | (l1 << 2) + i4;
                            for(int l4 = 0; l4 < 4; l4++)
                            {
                                double d20 = (double)l4 / 4D;
                                double d21 = d18 + (d19 - d18) * d20;
                                BlockState i5 = STONE;
                                if((l1 << 2) + i4 < 64)
                                    i5 = WATER;
                                if(d21 > 0.0D)
                                    i5 = STONE;
                                //abyte0[k4] = (byte)i5;
                                chunk.setBlockState(new BlockPos(k4, j4, l4), i5, false);
                                k4 += 128;
                            }

                        }

                    }

                }

            }

        }

        for(int l = 0; l < 16; l++)
        {
            pos.set(new Vec3i(l, pos.getY(), pos.getZ()));
            for(int j1 = 0; j1 < 16; j1++)
            {
                pos.set(new Vec3i(pos.getX(), pos.getY(), j1));
                double d2 = (chunkX << 4) + l;
                double d4 = (chunkZ << 4) + j1;
                double asd = 0.0D;
                int k2 = (int)(rockSandGen.func_806_a(d2 * 0.03125D * 2D, d4 * 0.03125D * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                int l2 = l << 11 | j1 << 7 | 0x7f;
                int i3 = -1;
                BlockState j3 = GRASS_BLOCK;
                BlockState k3 = DIRT;

                for (int l3 = 256; l3 >= 128; --l3) {
                    pos.setY(l3);
                    chunk.setBlockState(pos, AIR, false);
                }

                for(int l3 = 127; l3 >= 0; l3--)
                {
                    pos.setY(l3);

                    if (l3 <= random.nextInt(6) - 1) {
                        chunk.setBlockState(new BlockPos(l, l3, j1), Blocks.BEDROCK.getDefaultState(), false);
                    } else {
                        Block currentBlock = chunk.getBlockState(pos).getBlock();
                        if (currentBlock == Blocks.AIR)
                            i3 = -1;
                        else if (currentBlock == Blocks.STONE)
                            if (i3 == -1) {
                                if (k2 <= 0) {
                                    j3 = AIR;
                                    k3 = STONE;
                                } else if (l3 >= 60 && l3 <= 65) {
                                    j3 = GRASS_BLOCK;
                                    k3 = DIRT;
                                }
                                if (l3 < 64 && j3 == AIR)
                                    j3 = WATER;
                                i3 = k2;
                                if (l3 >= 63)
                                    //abyte0[l2] = (byte) j3;
                                    chunk.setBlockState(new BlockPos(l, l3, j1), j3, false);
                                else
                                    //abyte0[l2] = (byte) k3;
                                    chunk.setBlockState(new BlockPos(l, l3, j1), k3, false);
                            } else if (i3 > 0) {
                                i3--;
                                chunk.setBlockState(new BlockPos(l, l3, j1), k3, false);
                            }
                        l2--;
                    }
                }

            }

        }
    }

    private double generateNoise(double d1, double d2, double d3) {
        double d4;
        if((d4 = d2 * 4D - 64D) < 0.0D)
            d4 *= 3D;
        double d5;
        double d9;
        if((d5 = terrainGenerator.generateNoise((d1 * 684.41200000000003D) / 80D, (d2 * 684.41200000000003D) / 400D, (d3 * 684.41200000000003D) / 80D) / 2D) < -1D)
        {
            double d6;
            if((d9 = (d6 = terrainAlt1Generator.generateNoise(d1 * 684.41200000000003D, d2 * 984.41200000000003D, d3 * 684.41200000000003D) / 512D) - d4) < -10D)
                d9 = -10D;
            if(d9 > 10D)
                d9 = 10D;
        } else
        if(d5 > 1.0D)
        {
            double d7;
            if((d9 = (d7 = terrainAlt2Generator.generateNoise(d1 * 684.41200000000003D, d2 * 984.41200000000003D, d3 * 684.41200000000003D) / 512D) - d4) < -10D)
                d9 = -10D;
            if(d9 > 10D)
                d9 = 10D;
        } else
        {
            double d10 = terrainAlt1Generator.generateNoise(d1 * 684.41200000000003D, d2 * 984.41200000000003D, d3 * 684.41200000000003D) / 512D - d4;
            double d11 = terrainAlt2Generator.generateNoise(d1 * 684.41200000000003D, d2 * 984.41200000000003D, d3 * 684.41200000000003D) / 512D - d4;
            if(d10 < -10D)
                d10 = -10D;
            if(d10 > 10D)
                d10 = 10D;
            if(d11 < -10D)
                d11 = -10D;
            if(d11 > 10D)
                d11 = 10D;
            double d12 = (d5 + 1.0D) / 2D;
            double d8;
            d9 = d8 = d10 + (d11 - d10) * d12;
        }
        return d9;
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        int centreX = region.getCenterChunkX();
        int centreZ = region.getCenterChunkZ();
        Biome biome = region.getChunk(centreX, centreZ).getBiomeArray()[0];
        ChunkRandom rand = new ChunkRandom();
        rand.setSeed(region.getSeed(), centreX << 4, centreZ << 4);
        SpawnHelper.populateEntities(region, biome, centreX, centreZ, rand);
    }

    @Override
    public int getSpawnHeight() {
        return this.world.getSeaLevel() + 1;
    }

    @Override
    protected void sampleNoiseColumn(double[] array, int x, int z) {
        this.sampleNoiseColumn(array, x, z, 684.4119873046875D, 684.4119873046875D, 8.555149841308594D, 4.277574920654297D, 3, -10);
    }

    @Override
    protected double computeNoiseFalloff(double depth, double scale, int y) {
        return 0.1f;
    }

    @Override
    protected double[] computeNoiseRange(int x, int z) {
        return new double[] {0.1f, 0.1f};
    }

    @Override
    public void populateNoise(IWorld world, Chunk chunk) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        final double oneEighth = 0.125D;
        final double oneQuarter = 0.25D;
        this.heightNoise = new double[5 * 17 * 5];

        for (int i = 0; i < heightNoise.length; i++) {
            this.heightNoise[i] = this.generateNoise(chunkX * 4, 0, chunkZ * 4);
        }

        BlockPos.Mutable posMutable = new BlockPos.Mutable();

        for (int xSubChunk = 0; xSubChunk < 4; ++xSubChunk) {
            for (int zSubChunk = 0; zSubChunk < 4; ++zSubChunk) {
                for (int ySubChunk = 0; ySubChunk < 16; ++ySubChunk) {

                    double sampleNWLow = this.heightNoise[(xSubChunk * 5 + zSubChunk) * 17 + ySubChunk];
                    double sampleSWLow = this.heightNoise[((xSubChunk) * 5 + zSubChunk + 1) * 17 + ySubChunk];
                    double sampleNELow = this.heightNoise[((xSubChunk + 1) * 5 + zSubChunk) * 17 + ySubChunk];
                    double sampleSELow = this.heightNoise[((xSubChunk + 1) * 5 + zSubChunk + 1) * 17 + ySubChunk];

                    double sampleNWHigh = (this.heightNoise[((xSubChunk) * 5 + zSubChunk) * 17 + ySubChunk + 1] - sampleNWLow) * oneEighth;
                    double sampleSWHigh = (this.heightNoise[((xSubChunk) * 5 + zSubChunk + 1) * 17 + ySubChunk + 1] - sampleSWLow) * oneEighth;
                    double sampleNEHigh = (this.heightNoise[((xSubChunk + 1) * 5 + zSubChunk) * 17 + ySubChunk + 1] - sampleNELow) * oneEighth;
                    double sampleSEHigh = (this.heightNoise[((xSubChunk + 1) * 5 + zSubChunk + 1) * 17 + ySubChunk + 1] - sampleSELow) * oneEighth;

                    for (int localY = 0; localY < 8; ++localY) {
                        int y = ySubChunk * 8 + localY;
                        posMutable.setY(y);

                        double sampleNWInitial = sampleNWLow;
                        double sampleSWInitial = sampleSWLow;
                        double sampleNAverage = (sampleNELow - sampleNWLow) * oneQuarter;
                        double sampleSAverage = (sampleSELow - sampleSWLow) * oneQuarter;

                        for (int localX = 0; localX < 4; ++localX) {
                            posMutable.set(new Vec3i(localX + xSubChunk * 4 , posMutable.getY(), posMutable.getZ()));

                            double sample = sampleNWInitial;
                            double someOffsetThing = (sampleSWInitial - sampleNWInitial) * oneQuarter;

                            for (int localZ = 0; localZ < 4; ++localZ) {
                                posMutable.set(new Vec3i(posMutable.getX(), posMutable.getY(), zSubChunk * 4 + localZ));

                                BlockState toSet = AIR;

                                if (y < this.getSeaLevel()) {
                                    toSet = Blocks.WATER.getDefaultState();
                                }
                                if (sample > 0.0D) {
                                    toSet = STONE;
                                }

                                chunk.setBlockState(posMutable, toSet, false);
                                sample += someOffsetThing;
                            }

                            sampleNWInitial += sampleNAverage;
                            sampleSWInitial += sampleSAverage;
                        }

                        sampleNWLow += sampleNWHigh;
                        sampleSWLow += sampleSWHigh;
                        sampleNELow += sampleNEHigh;
                        sampleSELow += sampleSEHigh;
                    }
                }
            }
        }
    }
}
