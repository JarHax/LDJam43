package com.jarhax.ld34;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;

public class LD34 extends ApplicationAdapter {
    
    public Environment environment;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public FirstPersonCameraController camController;
    
    private World world;
    ModelBuilder modelBuilder;
    Model model;  
    
    @Override
    public void create() {
        
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1f));
        environment.add(new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f));
        modelBatch = new ModelBatch();
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        
        
        camController = new FirstPersonCameraController(cam);
        camController.setVelocity(50);
        Gdx.input.setInputProcessor(camController);
        
        modelBuilder = new ModelBuilder();
        Material material = new Material(ColorAttribute.createDiffuse(new Color((int) (0x200080))));
        model = modelBuilder.createBox(1f, 1f, 1f, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        
        world = new World("hello again", 512, 256, 512);
        world.generate(this);
        world.generateModel(this);
    }
    
    @Override
    public void render() {
        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
        
        modelBatch.begin(cam);
        
        this.world.render(this);
        
        modelBatch.end();
    }
    
    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
