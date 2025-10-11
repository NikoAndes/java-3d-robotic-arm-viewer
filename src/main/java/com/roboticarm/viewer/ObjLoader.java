package com.roboticarm.viewer;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjLoader {

    public static MeshView loadObjFile(String filename) {
        try {
            List<Float> vertices = new ArrayList<>();
            List<Float> texCoords = new ArrayList<>();
            List<Integer> faces = new ArrayList<>();

            // Intentar cargar desde recursos
            InputStream inputStream = ObjLoader.class.getResourceAsStream("/models/" + filename);
            BufferedReader reader;

            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                System.out.println("Cargando " + filename + " desde recursos...");
            } else {
                // Si no está en recursos, intentar cargar desde archivo
                reader = new BufferedReader(new FileReader(filename));
                System.out.println("Cargando " + filename + " desde archivo...");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("v ")) {
                    // Vértice
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 4) {
                        vertices.add(Float.parseFloat(parts[1]));
                        vertices.add(Float.parseFloat(parts[2]));
                        vertices.add(Float.parseFloat(parts[3]));
                    }
                } else if (line.startsWith("vt ")) {
                    // Coordenada de textura
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 3) {
                        texCoords.add(Float.parseFloat(parts[1]));
                        texCoords.add(Float.parseFloat(parts[2]));
                    }
                } else if (line.startsWith("f ")) {
                    // Cara (face)
                    String[] parts = line.split("\\s+");
                    // Procesar solo triángulos (3 vértices por cara)
                    if (parts.length >= 4) {
                        for (int i = 1; i <= 3; i++) {
                            String[] indices = parts[i].split("/");
                            int vertexIndex = Integer.parseInt(indices[0]) - 1; // OBJ usa índices 1-based
                            faces.add(vertexIndex);

                            // Coordenada de textura (si existe)
                            if (indices.length > 1 && !indices[1].isEmpty()) {
                                int texIndex = Integer.parseInt(indices[1]) - 1;
                                faces.add(texIndex);
                            } else {
                                faces.add(0); // Valor por defecto
                            }
                        }
                    }
                }
            }
            reader.close();

            System.out.println("Vertices: " + vertices.size()/3 + ", Caras: " + faces.size()/6);

            // Crear mesh
            TriangleMesh mesh = new TriangleMesh();

            // Convertir listas a arrays
            float[] vertexArray = new float[vertices.size()];
            for (int i = 0; i < vertices.size(); i++) {
                vertexArray[i] = vertices.get(i);
            }

            // Si no hay coordenadas de textura, crear valores por defecto
            if (texCoords.isEmpty()) {
                texCoords.add(0.0f);
                texCoords.add(0.0f);
                texCoords.add(1.0f);
                texCoords.add(0.0f);
                texCoords.add(1.0f);
                texCoords.add(1.0f);
                texCoords.add(0.0f);
                texCoords.add(1.0f);
            }

            float[] texArray = new float[texCoords.size()];
            for (int i = 0; i < texCoords.size(); i++) {
                texArray[i] = texCoords.get(i);
            }

            int[] faceArray = new int[faces.size()];
            for (int i = 0; i < faces.size(); i++) {
                faceArray[i] = faces.get(i);
            }

            mesh.getPoints().setAll(vertexArray);
            mesh.getTexCoords().setAll(texArray);
            mesh.getFaces().setAll(faceArray);

            // Crear MeshView
            MeshView meshView = new MeshView(mesh);

            // Material por defecto
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.LIGHTGRAY);
            material.setSpecularColor(Color.WHITE);
            meshView.setMaterial(material);

            System.out.println("✓ " + filename + " cargado exitosamente!");
            return meshView;

        } catch (Exception e) {
            System.err.println("Error cargando archivo OBJ: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}