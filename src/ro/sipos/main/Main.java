package com.sipos.main;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.sipos.entity.Camera;
import com.sipos.entity.Entity;
import com.sipos.entity.Light;
import com.sipos.mesh.Mesh;
import com.sipos.mesh.TexturedMesh;
import com.sipos.render.DataLoader;
import com.sipos.render.DisplayManager;
import com.sipos.render.MasterRenderer;
import com.sipos.render.OBJLoader;
import com.sipos.terrain.Terrain;
import com.sipos.texture.MeshTexture;

public class Main {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		MasterRenderer renderer = new MasterRenderer();
		Camera camera = new Camera();
		DataLoader dataLoader = new DataLoader();
		MeshTexture texture = new MeshTexture(dataLoader.loadTexture("res/stallTexture.png"));
		texture.setReflectivity(1.5f);
		texture.setShineDamper(2f);
		Mesh mesh = OBJLoader.loadMesh("res/dragon.obj", dataLoader);
		
		TexturedMesh texturedMesh = new TexturedMesh(mesh, texture);
		Entity entity = new Entity(texturedMesh, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		Light light = new Light(new Vector3f(100, 100, 100), new Vector3f(1, 1, 1));
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			renderer.proccesEntity(entity);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		dataLoader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
