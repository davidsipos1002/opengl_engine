package com.sipos.mesh;

import com.sipos.texture.MeshTexture;

public class TexturedMesh {

	private Mesh mesh;
	private MeshTexture texture;
	
	public TexturedMesh(Mesh mesh, MeshTexture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public MeshTexture getTexture() {
		return texture;
	}
}
