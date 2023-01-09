package com.cgvsu.polygonDeleter;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class PolygonDeleterTest {

    Model readTestModel() throws IOException {
        Path fileName = Path.of("tests/com/cgvsu/polygonDeleter/testModel.obj");
        String fileContent = Files.readString(fileName);
        Model testModel = ObjReader.read(fileContent);
        return testModel;
    }

    @Test
    void findFreeVertices01() throws IOException {

        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(0);
        vertices.add(1);
        vertices.add(2);

        ArrayList<Integer> result = PolygonDeleter.findFreeVertices(readTestModel(), vertices);
        ArrayList<Integer> expectedResult = new ArrayList<>();
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    void findFreeVertices02() throws IOException {
        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(3);
        vertices.add(4);
        vertices.add(7);

        ArrayList<Integer> result = PolygonDeleter.findFreeVertices(readTestModel(), vertices);
        ArrayList<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(7);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    void findFreeVertices03() throws IOException {
        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(5);
        vertices.add(6);
        vertices.add(7);

        ArrayList<Integer> result = PolygonDeleter.findFreeVertices(readTestModel(), vertices);
        ArrayList<Integer> expectedResult = new ArrayList<>();
        expectedResult.add(5);
        expectedResult.add(6);
        expectedResult.add(7);
        Assertions.assertTrue(result.equals(expectedResult));
    }

    @Test
    void deleteFreeVertices() throws IOException {
        Model expectedResult = readTestModel();
        Model result = readTestModel();
        int v1 = 5;
        int v2 = 6;
        int v3 = 7;

        ArrayList<Integer> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        PolygonDeleter.deleteFreeVertices(result, vertices);

        expectedResult.vertices.remove(v3);
        expectedResult.vertices.remove(v2);
        expectedResult.vertices.remove(v1);
        Polygon shiftablePolygon = expectedResult.polygons.get(4);
        ArrayList<Integer> polygonIndices = shiftablePolygon.getVertexIndices();
        for (int i = 0; i < polygonIndices.size(); i++) {
            Integer vertice = polygonIndices.get(i);
            polygonIndices.set(i, vertice - 3);
        }
        shiftablePolygon.setVertexIndices(polygonIndices);
        expectedResult.polygons.set(4, shiftablePolygon);

        boolean verticesEqualExpected = true;
        boolean polygonsEqualExpected = true;

        for (int i = 0; i < expectedResult.vertices.size(); i++) {
            if (!expectedResult.vertices.get(i).equals(result.vertices.get(i))) {
                verticesEqualExpected = false;
                break;
            }
        }

        for (int i = 0; i < expectedResult.polygons.size(); i++){
            ArrayList<Integer> expectedVertices = expectedResult.polygons.get(i).getVertexIndices();
            ArrayList<Integer> resultVertices = result.polygons.get(i).getVertexIndices();
            polygonsEqualExpected = expectedVertices.equals(resultVertices);
        }

        Assertions.assertTrue(verticesEqualExpected && polygonsEqualExpected);

    }
}