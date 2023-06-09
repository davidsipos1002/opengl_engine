package com.sipos.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.sipos.entity.Camera;
import com.sipos.entity.Entity;
import com.sipos.entity.Light;
import com.sipos.mesh.TexturedMesh;
import com.sipos.shader.StaticShader;
import com.sipos.shader.TerrainShader;
import com.sipos.terrain.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedMesh, List<Entity>> entities = new HashMap<TexturedMesh, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer() {
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public void render(Light light, Camera camera) {
		prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void proccesTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void proccesEntity(Entity entity) {
		TexturedMesh tm = entity.getMesh();
		List<Entity> batch = entities.get(tm);
		if(batch != null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(tm, newBatch);
		}
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glClearColor(1, 1, 1, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
 
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
