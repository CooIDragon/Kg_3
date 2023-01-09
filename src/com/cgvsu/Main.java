package com.cgvsu;

import com.cgvsu.writer.ObjWriter;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.polygonDeleter.PolygonDeleter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {

        Path fileName = Path.of("cube.obj");
        String fileContent = Files.readString(fileName);

        System.out.println("Loading...");
        System.out.println();
        Model model = ObjReader.read(fileContent);

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());

        System.out.println();

        PolygonDeleter.deletePolygon(model, 12);
        System.out.println();

        System.out.println("Vertices: " + model.vertices.size());
        System.out.println("Texture vertices: " + model.textureVertices.size());
        System.out.println("Normals: " + model.normals.size());
        System.out.println("Polygons: " + model.polygons.size());

        System.out.println();

        //запись в файл
        String filePath = "result.obj";

        File f = new File(filePath);
        try {
            System.out.println("Updating...");
            ObjWriter.writeToFile(model, f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Model has been saved into file.");
    }
}
