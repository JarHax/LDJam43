package com.jarhax.ld34;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.util.*;

public class LD34 extends ApplicationAdapter {
    
    public Environment environment;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;
    public List<ModelInstance> instances = new ArrayList<ModelInstance>();
    public FirstPersonCameraController camController;
    private static final OpenSimplexNoise NOISE = new OpenSimplexNoise();
    
    Map<Pos, GameObject> blocks = new HashMap<Pos, GameObject>();
    ModelBuilder modelBuilder;
    Model model;
    
    Random rand = new Random();
    
    @Override
    public void create() {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
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
        model = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(new Color((int) (0x200080)))), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }
    
    Vector3 dimensions = new Vector3(1, 1, 1);
    float lastX = 0;
    float lastZ = 0;
    float totalTime = 0;
    
    int lastTime = 0;
    
    public static class GameObject extends ModelInstance {
        
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;
        
        private final static BoundingBox bounds = new BoundingBox();
        
        public GameObject(Model model) {
            super(model);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }
    
    
    private Vector3 position = new Vector3();
    
    protected boolean isVisible(GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }
    
    @Override
    public void render() {
        totalTime += Gdx.graphics.getDeltaTime();
        camController.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond());
        modelBatch.begin(cam);
        for(GameObject instance : blocks.values()) {
            System.out.println(instance.model.meshes.size);
            if(isVisible(instance)) {
                
                modelBatch.render(instance, environment);
            }
        }
        modelBatch.end();
        
        
        int i = 5;
        if(Math.round(totalTime) % 0.5f == 0/*Math.abs(lastX - cam.position.x) > i || Math.abs(lastZ - cam.position.z) > i*/) {
            int dist = 50;
            for(int x = -dist; x < dist; x++) {
                for(int z = -dist; z < dist; z++) {
                    float xx = Math.round(cam.position.x) + x;
                    float zz = Math.round(cam.position.z) + z;
                    
                    float y = (float) Math.round(NOISE.eval(xx / 128f, zz / 128f) * 64);
                    GameObject e = new GameObject(model);
                    e.transform.setTranslation(xx, y, zz);
                    
                    for(Material material : e.materials) {
                        material.set(ColorAttribute.createDiffuse(new Color((int) (0x7BCCB5 * y))));
                    }
                    Pos key = new Pos(xx, y, zz);
                    blocks.put(key, e);
                }
            }
            lastX = cam.position.x;
            lastZ = cam.position.z;
        }
        
        
    }
    
    @Override
    public void dispose() {
        modelBatch.dispose();
    }
}
