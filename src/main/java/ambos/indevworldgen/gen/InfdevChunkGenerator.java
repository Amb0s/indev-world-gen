package ambos.indevworldgen.gen;

import ambos.indevworldgen.util.noise.OctaveInfdev0415NoiseSampler;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;

import java.util.Random;

public class InfdevChunkGenerator extends SurfaceChunkGenerator<AlphaChunkGeneratorConfig> {
    private OctaveInfdev0415NoiseSampler terrainAlt1Generator;
    private OctaveInfdev0415NoiseSampler terrainAlt2Generator;
    private OctaveInfdev0415NoiseSampler terrainGenerator;
    private OctaveInfdev0415NoiseSampler noiseSandGen;
    private OctaveInfdev0415NoiseSampler rockSandGen;
    private OctaveInfdev0415NoiseSampler treeGen;

    public InfdevChunkGenerator(IWorld world, BiomeSource biomeSource, AlphaChunkGeneratorConfig config){
        super(world, biomeSource, 4, 8, 256, config, true);

        Random rand = new Random(world.getSeed());

        terrainAlt1Generator = new OctaveInfdev0415NoiseSampler(rand, 16);
        terrainAlt2Generator = new OctaveInfdev0415NoiseSampler(rand, 16);
        terrainGenerator = new OctaveInfdev0415NoiseSampler(rand, 8);
        noiseSandGen = new OctaveInfdev0415NoiseSampler(rand, 4);
        rockSandGen = new OctaveInfdev0415NoiseSampler(rand, 4);
        new OctaveInfdev0415NoiseSampler(rand, 5);
        treeGen = new OctaveInfdev0415NoiseSampler(rand, 5);
    }

    @Override
    public int getSpawnHeight() {
        return this.world.getSeaLevel() + 1;
    }

    /*@Override
    public void generateTerrain(int i, int j, byte abyte0[]){
        for(int k = 0; k < 4; k++)
        {
            for(int i1 = 0; i1 < 4; i1++)
            {
                double ad[][] = new double[33][4];
                int k1 = (i << 2) + k;
                int i2 = (j << 2) + i1;
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
                                int i5 = 0;
                                if((l1 << 2) + i4 < 64)
                                    i5 = Block.waterStill.blockID;
                                if(d21 > 0.0D)
                                    i5 = Block.stone.blockID;
                                abyte0[k4] = (byte)i5;
                                k4 += 128;
                            }
                        }
                    }
                }
            }
        }
    }*/

    /*
    public void replaceSurfaceBlocks(Chunk chunk){
        for(int l = 0; l < 16; l++)
        {
            for(int j1 = 0; j1 < 16; j1++)
            {
                double d2 = (i << 4) + l;
                double d4 = (j << 4) + j1;
                double asd = 0.0D;
                boolean flag = ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INFDEV0415 && noiseSandGen.generateNoise(d2 * 0.03125D, d4 * 0.03125D, 0.0D) + rand.nextDouble() * 0.20000000000000001D > asd;
                boolean flag1 = ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INFDEV0415 && noiseSandGen.generateNoise(d4 * 0.03125D, 109.0134D, d2 * 0.03125D) + rand.nextDouble() * 0.20000000000000001D > 3D;
                int k2 = (int)(rockSandGen.func_806_a(d2 * 0.03125D * 2D, d4 * 0.03125D * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                int l2 = l << 11 | j1 << 7 | 0x7f;
                int i3 = -1;
                int j3 = Block.grass.blockID;
                int k3 = Block.dirt.blockID;
                for(int l3 = 127; l3 >= 0; l3--)
                {
                    if(abyte0[l2] == 0)
                        i3 = -1;
                    else
                    if(abyte0[l2] == Block.stone.blockID)
                        if(i3 == -1)
                        {
                            if(k2 <= 0)
                            {
                                j3 = 0;
                                k3 = (byte)Block.stone.blockID;
                            } else
                            if(l3 >= 60 && l3 <= 65)
                            {
                                j3 = Block.grass.blockID;
                                k3 = Block.dirt.blockID;
                                if(flag1)
                                    j3 = 0;
                                if(flag1)
                                    k3 = Block.gravel.blockID;
                                if(flag)
                                        j3 = Block.sand.blockID;
                                if(flag)
                                    k3 = Block.sand.blockID;
                            }
                            if(l3 < 64 && j3 == 0)
                                j3 = Block.lavaStill.blockID;
                            i3 = k2;
                            if(l3 >= 63)
                                abyte0[l2] = (byte)j3;
                            else
                                abyte0[l2] = (byte)k3;
                        } else
                        if(i3 > 0)
                        {
                            i3--;
                            abyte0[l2] = (byte)k3;
                        }
                    l2--;
                }
            }
        }
    }*/

    private double generateNoise(double d1, double d2, double d3)
    {
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
    protected double[] computeNoiseRange(int x, int z) {
        return new double[0];
    }

    @Override
    protected double computeNoiseFalloff(double depth, double scale, int y) {
        return 0;
    }

    @Override
    protected void sampleNoiseColumn(double[] buffer, int x, int z) {

    }


    /*
    @Override
    public void populate(IChunkProvider ichunkprovider, int x, int z){
        rand.setSeed((long)x * 0x12f88dd3L + (long)z * 0x36d41eecL);
        int x1 = x << 4;
        int z1 = z << 4;
        if(mapFeaturesEnabled)
        {
            if (ODNBXlite.Structures[2]){
                strongholdGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            }
            if (ODNBXlite.Structures[1]){
                villageGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            }
            if (ODNBXlite.Structures[3]){
                mineshaftGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            }
        }
        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreCoal.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
        }

        for(int i = 0; i < 10; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(64);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreIron.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
        }

        if(rand.nextInt(2) == 0)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(32);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreGold.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
        }
        if(rand.nextInt(8) == 0)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(16);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreDiamond.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
        }
        if (ODNBXlite.getFlag("newores")){
            for(int i = 0; i < 8; i++)
            {
                int x2 = x1 + rand.nextInt(16);
                int y2 = rand.nextInt(16);
                int z2 = z1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreRedstone.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
            }
            for(int i = 0; i < 1; i++)
            {
                int x2 = x1 + rand.nextInt(16);
                int y2 = rand.nextInt(16) + rand.nextInt(16);
                int z2 = z1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreLapis.blockID)).generate_infdev(worldObj, rand, x2, y2, z2);
            }
            int max = 0;
            detection: for(int i = x1; i < x1 + 16; i++){
                for(int j = z1; j < z1 + 16; j++){
                    int h = worldObj.getPrecipitationHeight(i, j);
                    if (max < h){
                        max = h;
                    }
                    if (max > 108){
                        break detection;
                    }
                }
            }
            if (max > 108){
                for (int i = 0; i < 3 + rand.nextInt(6); i++){
                    int x2 = x1 + rand.nextInt(16);
                    int y2 = rand.nextInt(28) + 4;
                    int z2 = z1 + rand.nextInt(16);
                    int id = worldObj.getBlockId(x2, y2, z2);
                    if (id == Block.stone.blockID){
                        worldObj.setBlock(x2, y2, z2, Block.oreEmerald.blockID);
                    }
                }
            }
        }
        int trees = (int)treeGen.func_806_a((double)x1 * 0.25D, (double)z1 * 0.25D) << 3;
        if(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS)
        {
            if (trees < 0){
                trees = 0;
            }
            trees += 20;
        }
        WorldGenerator treegen = ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INFDEV0327 ? new OldWorldGenTrees(false) : new OldWorldGenBigTree();
        for(int i = 0; i < trees; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int z2 = z1 + rand.nextInt(16) + 8;
            treegen.setScale(1.0D, 1.0D, 1.0D);
            treegen.generate(worldObj, rand, x2, worldObj.getHeightValue(x2, z2), z2);
        }
        spawnAnimals(x * 16, z * 16);
    }

   */
}