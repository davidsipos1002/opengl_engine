package com.sipos.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.sipos.mesh.Mesh;

public class OBJLoader {

	public static Mesh loadMesh(String filename, DataLoader dataLoader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File(filename));
			
		}catch(IOException e) {
			System.err.println("Could not read file !");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>(); 
		List<Integer> indices = new ArrayList<Integer>(); 
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {
			while(true) {
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 3];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}
			
			while(line != null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				proccesVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				proccesVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				proccesVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				
				line = reader.readLine();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int pointer = 0;
		
		for(Vector3f vertex : vertices) {
			verticesArray[pointer++] = vertex.x;
			verticesArray[pointer++] = vertex.y;
			verticesArray[pointer++] = vertex.z;
		}
		
		for(int i = 0;i < indices.size();i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return dataLoader.load(verticesArray, textureArray, normalsArray, indicesArray);
	}
	
	private static void proccesVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int pointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(pointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[pointer * 2] = currentTex.x;
		textureArray[pointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[pointer * 3] = currentNormal.x;
		normalsArray[pointer * 3 + 1] = currentNormal.y;
		normalsArray[pointer * 3 + 2] = currentNormal.z;
	}
}
