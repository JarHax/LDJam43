package com.jarhax.ld34;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.TimeUtils;

public class World {
    
    private final String seed;
    private final int width;
    private final int height;
    private final int depth;
    private final Random random;
    private final OpenSimplexNoise noise;
    private final List<Block> blocks;
    private final ModelCache modelCache;
    
    public World (String seed, int width, int height, int depth) {
        
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.random = new Random (seed.hashCode() & 0xbeef);
        this.noise = new OpenSimplexNoise(seed.hashCode() & 0xbeef);
        this.blocks = new ArrayList<Block>();
        this.modelCache = new ModelCache();
    }
    
    public void generate(LD34 game) {
        
        long start = TimeUtils.millis();
        Gdx.app.log("Game", "Generating world...");
        for(int x = 0; x < width; x++) {
            for(int z = 0; z < depth; z++) {
                float xx = Math.round(game.cam.position.x) + x;
                float zz = Math.round(game.cam.position.z) + z;
                float y = (float) Math.round(noise.eval(xx / 128f, zz / 128f) * 64);
                Block e = new Block(game.model, new Pos(xx, y, zz));
                e.transform.setToTranslation(xx, y, zz);
                blocks.add(e);
            }
        }
        Gdx.app.log("Game", "World generated in " + (TimeUtils.millis() - start) + "ms.");
    }
    
    public void generateModel(LD34 game) {
                
        long start = TimeUtils.millis();
        Gdx.app.log("Game", "Generating model cache...");
        modelCache.begin();
        modelCache.add(blocks);
        modelCache.end();
        Gdx.app.log("Game", "Model cache generated in " + (TimeUtils.millis() - start) + "ms.");
    }
    
    public void render(LD34 game) {
        
        game.modelBatch.render(modelCache, game.environment);
    }
    
    public Collection<Block> getBlocks() {
        
        return this.blocks;
    }
}