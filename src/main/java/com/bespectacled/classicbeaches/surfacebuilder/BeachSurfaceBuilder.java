package com.bespectacled.classicbeaches.surfacebuilder;

import java.util.Random;
import java.util.stream.IntStream;

import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class BeachSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {

    private static final BlockState STONE = Blocks.STONE.getDefaultState();
    private static final BlockState AIR = Blocks.AIR.getDefaultState();
    private static final BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    private static final BlockState SAND = Blocks.SAND.getDefaultState();
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    
    protected long seed;
    protected OctavePerlinNoiseSampler noise;
    
    public BeachSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(
        Random random, 
        Chunk chunk, 
        Biome biome, 
        int x, 
        int z, 
        int integer7, 
        double stoneNoise, 
        BlockState defaultBlock, 
        BlockState defaultFluid, 
        int fluidLevel, 
        long long13, 
        TernarySurfaceConfig ternarySurfaceConfig
    ) {
        int seaLevel = fluidLevel;
        int relX = x & 0xF;
        int relZ = z & 0xF;
        double eighth = 0.03125;
        
        //boolean genSand = this.noise.sample(x * 0.03125, z * 0.03125, 0.0, eighth, eighth, false) * 75D + random.nextDouble() > 0.0D;
        //boolean genGravel = this.noise.sample(x * 0.03125, 109.0, z * 0.03125, eighth, eighth, false) * 75D + random.nextDouble() > 3.0D;
        
        double sandNoise = this.noise.sample(x * 0.03125, z * 0.03125, 0.0) * 75D + random.nextDouble();
        double gravelNoise = this.noise.sample(x * 0.03125, 109.0, z * 0.03125) * 75D + random.nextDouble();
        
        boolean genSand = sandNoise > 5D;
        boolean genGravel = gravelNoise > 20D;
        
        if (genSand) System.out.println("Sand noise: " + sandNoise);
        
        int genStone = (int)(stoneNoise / 3.0 + 3.0 + random.nextDouble() * 0.25);
        
        BlockPos.Mutable pos = new BlockPos.Mutable();
        int flag = -1;
        
        BlockState topBlock = ternarySurfaceConfig.getTopMaterial();
        BlockState fillerBlock = ternarySurfaceConfig.getUnderMaterial();
        
        for (int y = 127; y >= 0; --y) {
            pos.set(relX, y, relZ);
            BlockState thisBlock = chunk.getBlockState(pos);
            
            if (thisBlock.isAir()) {
                flag = -1;
            }
            
            else if (thisBlock.isOf(defaultBlock.getBlock())) {
                if (flag == -1) {
                    if (genStone <= 0) {
                        topBlock = AIR;
                        fillerBlock = STONE;
                    }
                    
                    else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                        topBlock = ternarySurfaceConfig.getTopMaterial();
                        fillerBlock = ternarySurfaceConfig.getUnderMaterial();
                        
                        if (genGravel) {
                            topBlock = AIR;
                            fillerBlock = GRAVEL;
                        }
                        
                        if (genSand) {
                            topBlock = SAND;
                            fillerBlock = SAND;
                        }
                    }
                    if (y < seaLevel && topBlock.isAir()) {
                        topBlock = defaultFluid;
                    }
                    
                    flag = genStone;
                    if (y >= seaLevel - 1) {
                        chunk.setBlockState(pos, topBlock, false);
                    }
                    else {
                        chunk.setBlockState(pos, fillerBlock, false);
                    }
                }
                
                else if (flag > 0) {
                    --flag;
                    chunk.setBlockState(pos, fillerBlock, false);
                    
                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.equals(SAND)) {
                        flag = random.nextInt(4);
                        fillerBlock = SANDSTONE;
                    }
                }
            }
        }
    }
    
    @Override
    public void initSeed(long long2) {
        if (this.seed != long2 || this.noise == null) {
            this.noise = new OctavePerlinNoiseSampler(new ChunkRandom(long2), IntStream.rangeClosed(-3, 0));
        }
        this.seed = long2;
    }
}
