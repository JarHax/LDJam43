package com.jarhax.ld34;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Block extends ModelInstance {
    
    public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;
    
    private final static BoundingBox bounds = new BoundingBox();
    private Pos pos;
    
    public Block(Model model, Pos pos) {
        super(model);
        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
        this.pos = pos;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        
        Block pos = (Block) o;
        
        if(Float.compare(pos.pos.getX(), this.pos.getX()) != 0)
            return false;
        return Float.compare(pos.pos.getZ(), this.pos.getZ()) == 0;
    }
    
    @Override
    public int hashCode() {
        int result = (pos.getX() != +0.0f ? Float.floatToIntBits(pos.getX()) : 0);
        result = 31 * result + (pos.getZ() != +0.0f ? Float.floatToIntBits(pos.getZ()) : 0);
        return result;
    }
}