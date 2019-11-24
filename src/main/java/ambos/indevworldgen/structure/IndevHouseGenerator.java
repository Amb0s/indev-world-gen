package ambos.indevworldgen.structure;

import ambos.indevworldgen.IndevWorldGen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.List;
import java.util.Random;

public class IndevHouseGenerator {
    public static final Identifier id = new Identifier("indevworldgen:indev_house");

    public static void addParts(StructureManager structureManager, BlockPos blockPos, BlockRotation rotation, List<StructurePiece> list_1, Random random, DefaultFeatureConfig defaultFeatureConfig)
    {
        list_1.add(new IndevHouseGenerator.Piece(structureManager, id, blockPos, rotation));
    }

    public static class Piece extends SimpleStructurePiece
    {
        private final BlockRotation rotation;
        private final Identifier identifier;

        public Piece(StructureManager structureManager_1, CompoundTag compoundTag_1) {
            super(IndevWorldGen.indevHouseStructurePieceType, compoundTag_1);

            this.identifier = new Identifier(compoundTag_1.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag_1.getString("Rot"));

            this.setStructureData(structureManager_1);
        }

        public Piece(StructureManager structureManager, Identifier identifier, BlockPos pos, BlockRotation rotation)
        {
            super(IndevWorldGen.indevHouseStructurePieceType, 0);

            this.rotation = rotation;
            this.identifier = identifier;
            this.pos = pos;

            this.setStructureData(structureManager);
        }

        public void setStructureData(StructureManager structureManager)
        {
            Structure structure_1 = structureManager.getStructureOrBlank(this.identifier);
            StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        protected void handleMetadata(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableIntBoundingBox mutableIntBoundingBox)
        {

        }

        @Override
        public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1)
        {
            ChunkPos chunkPos_2 = new ChunkPos(iWorld_1.getSpawnPos()); // Get spawn chunk pos.

            int yHeight = iWorld_1.getTop(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
            this.pos = this.pos.add(0, yHeight - 1, 0);

            if (chunkPos_1.getStartX() == chunkPos_2.getStartX() && chunkPos_1.getStartZ() == chunkPos_2.getStartZ())
                return  super.generate(iWorld_1, random_1, mutableIntBoundingBox_1, chunkPos_1);

            return false;
        }
    }
}
