package com.sipos.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.sipos.entity.Entity;
import com.sipos.mesh.Mesh;
import com.sipos.mesh.TexturedMesh;
import com.sipos.shader.StaticShader;
import com.sipos.texture.MeshTexture;
import com.sipos.toolbox.Maths;

public class EntityRenderer {
	
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	
	
	public void render(Map<TexturedMesh, List<Entity>> entities) {
		for(TexturedMesh mesh : entities.keySet()) {
			prepareTexturedMesh(mesh);
			List<Entity> batch = entities.get(mesh);
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedMesh(TexturedMesh mesh) {
		Mesh m = mesh.getMesh();
		GL30.glBindVertexArray(m.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		MeshTexture texture = mesh.getTexture();
		shader.loadShineData(texture.getReflectivity(), texture.getShineDamper());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getTexture().getTextureID());
	}
	
	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transform = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transform);
	}
}
