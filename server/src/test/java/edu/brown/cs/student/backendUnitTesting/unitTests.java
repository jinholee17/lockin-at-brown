package edu.brown.cs.student.backendUnitTesting;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonObject;
import edu.brown.cs.student.main.server.geoJsonParser.GeoJsonParser;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class unitTests {
  // Testing the GeoJSON Parser filter
  @Test
  public void parserTest() throws IOException {
    // Tests the creategeoJSON does not return something null
    GeoJsonParser parser = new GeoJsonParser();
    GeoJsonObject geoJsonObject =
        parser.createGeoJson("./data/provided_fullDownload.geojson", true);
    assertNotNull(geoJsonObject);
    // Tests the fromGeoJSon method, ensuring that it correctly parses JSON strings into instances
    // of GeoJsonObject.
    String jsonString = "{\"type\":\"Point\",\"coordinates\":[100.0,0.0]}";
    GeoJsonObject parsedObject = GeoJsonParser.fromJsonGeneral(jsonString, GeoJsonObject.class);
    assertEquals("Point", parsedObject.type);

    // Tests the creategeoJSON does not return something null
    GeoJsonObject geoJsonObject2 =
        parser.createGeoJson("./data/provided_fullDownload.geojson", true);
    assertNotNull(geoJsonObject2);
    String jsonString2 =
        "{\n"
            + "  \"type\": \"FeatureCollection\",\n"
            + "  \"features\": [\n"
            + "    {\n"
            + "      \"type\": \"Feature\",\n"
            + "      \"geometry\": {\n"
            + "        \"type\": \"Point\",\n"
            + "        \"coordinates\": [\n"
            + "          [\n"
            + "            [\n"
            + "              [\n"
            + "                -86.75692,\n"
            + "                33.495789\n"
            + "              ],\n"
            + "              [\n"
            + "                -86.762018,\n"
            + "                33.491924\n"
            + "              ],\n"
            + "              [\n"
            + "                -86.762268,\n"
            + "                33.488921\n"
            + "              ],\n"
            + "              [\n"
            + "                -86.754496,\n"
            + "                33.488832\n"
            + "              ]\n"
            + "            ]\n"
            + "          ]\n"
            + "        ]\n"
            + "      },\n"
            + "      \"properties\": {\n"
            + "        \"state\": \"Pancakeland\",\n"
            + "        \"city\": \"Muffinburg\",\n"
            + "        \"name\": \"Butter and cream\",\n"
            + "        \"holc_id\": \"A\",\n"
            + "        \"holc_grade\": \"A+\",\n"
            + "        \"neighborhood_id\": 123,\n"
            + "        \"area_description_data\": {\n"
            + "          \"1\": \"Super\",\n"
            + "          \"2\": \"cool\",\n"
            + "          \"3\": \"food\"\n"
            + "        }\n"
            + "      }\n"
            + "    },\n"
            + "    {\n"
            + "      \"type\": \"Feature\",\n"
            + "      \"geometry\": {\n"
            + "        \"type\": \"Point\",\n"
            + "        \"coordinates\": [\n"
            + "          [\n"
            + "            [\n"
            + "              [\n"
            + "                -90.75692,\n"
            + "                30.495789\n"
            + "              ],\n"
            + "              [\n"
            + "                -90.754496,\n"
            + "                30.488832\n"
            + "              ]\n"
            + "            ]\n"
            + "          ]\n"
            + "        ]\n"
            + "      },\n"
            + "      \"properties\": {\n"
            + "        \"state\": \"Spaghettitopia\",\n"
            + "        \"city\": \"Sushiville\",\n"
            + "        \"name\": \"Chopsticks!\",\n"
            + "        \"holc_id\": \"C\",\n"
            + "        \"holc_grade\": \"B\",\n"
            + "        \"neighborhood_id\": 476,\n"
            + "        \"area_description_data\": {\n"
            + "          \"1\": \"Eat\",\n"
            + "          \"2\": \"slowly\"\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}";
    GeoJsonObject parsedObject2 = GeoJsonParser.fromJsonGeneral(jsonString2, GeoJsonObject.class);
    assertEquals("FeatureCollection", parsedObject2.type);
  }
}
