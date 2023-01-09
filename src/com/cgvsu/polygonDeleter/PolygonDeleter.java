package com.cgvsu.polygonDeleter;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.util.ArrayList;
import java.util.Scanner;

public class PolygonDeleter {

    public static void deletePolygon(Model model, int polygonIndex) {
        Polygon polygon = model.polygons.get(polygonIndex);

        model.polygons.remove(polygonIndex);

        ArrayList<Integer> freeVertices = findFreeVertices(model, polygon.getVertexIndices());

        if (!freeVertices.isEmpty()) {
            if (needToDelFreeVertices()) {
                deleteFreeVertices(model, freeVertices);
            }
        }
    }

    protected static ArrayList<Integer> findFreeVertices(Model model, ArrayList<Integer> vertexIndices) {
        for (Polygon polygon : model.polygons) {
            for (Integer polygonVertex : polygon.getVertexIndices()) {
                vertexIndices.removeIf(vertex -> vertex.equals(polygonVertex));
            }
        }
        return vertexIndices;
    }

    protected static void deleteFreeVertices(Model model, ArrayList<Integer> freeVerticesIndices) {
        ArrayList<Vector3f> freeVertices = new ArrayList<>();
        for (Integer vertexIndex : freeVerticesIndices) {
            freeVertices.add(model.vertices.get(vertexIndex));
        }

        model.vertices.removeAll(freeVertices);

        freeVerticesIndices.sort(Integer::compareTo);

        for (Polygon polygon: model.polygons) {
            ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
            for (int i = 0; i < vertexIndices.size(); i++) {
                int vertexIndex = vertexIndices.get(i);
                for (int j = freeVerticesIndices.size() - 1; j >= 0; j--) {
                    if (vertexIndex < freeVerticesIndices.get(j)) {
                        continue;
                    }
                    if (vertexIndex >= freeVerticesIndices.get(j)) {
                        vertexIndex--;
                        vertexIndices.set(i, vertexIndex);
                    }
                }
            }
            polygon.setVertexIndices(vertexIndices);
        }
    }

    private static boolean needToDelFreeVertices() {
        System.out.println("Do you want to delete free vertices?");

        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine();

        if (answer.equals("yes")) {
            return true;
        } else if (answer.equals("no")) {
            return false;
        } else {
            System.out.println("Incorrect answer, please type yes or no");
            return needToDelFreeVertices();
        }
    }
}
