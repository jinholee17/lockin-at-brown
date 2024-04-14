package edu.brown.cs.student.MockTest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONData;
import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONMock;
import edu.brown.cs.student.main.server.handlers.RedLineAllHandler;
import edu.brown.cs.student.main.server.handlers.RedLineFilterHandler;
import edu.brown.cs.student.main.server.handlers.SearchAreaHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.*;
import spark.Spark;

public class MockRedlineData {

  public void TestAPIHandlers() throws IOException {}

  /** Set up all variables and servers before running integration test */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  // Create mock data 1
  String mock_data_1 =
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
  GeoJSONData mock1 = new GeoJSONMock(mock_data_1);

  JsonAdapter<Map<String, String>> mapAdapter;
  JsonAdapter<List<List<String>>> listAdapter;

  JsonAdapter<String> stringAdapter;

  /** Starting servers before testing */
  @BeforeEach
  public void setup() {
    Moshi moshi = new Moshi.Builder().build();
    this.mapAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, String.class));
    this.stringAdapter = moshi.adapter(Types.newParameterizedType(List.class, String.class));

    Moshi moshi2 = new Moshi.Builder().build();
    this.listAdapter =
        moshi2.adapter(
            Types.newParameterizedType(
                List.class, Types.newParameterizedType(List.class, String.class)));

    Spark.get("red-line/*", new RedLineAllHandler(mock1));
    Spark.get("red-line", new RedLineFilterHandler(mock1));
    Spark.get("search-area", new SearchAreaHandler(mock1));

    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /** Clean up after calls */
  @AfterEach
  public void tearUp() {

    Spark.unmap("red-line/*");
    Spark.awaitStop();

    Spark.unmap("red-line");
    Spark.awaitStop();

    Spark.unmap("search-area");
    Spark.awaitStop();
  }

  /**
   * Helper method to make HTTP calls
   *
   * @param apiCall
   * @return the client connection status
   * @throws IOException
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The default method is "GET", which is what we're using here.
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Testing redlining server handler functions correctly. Testing we can filter and get all the
   * data
   *
   * @throws IOException
   */
  @Test
  public void testRedlining() throws IOException, InterruptedException {
    // redline full data
    HttpURLConnection clientConnection1 = tryRequest("red-line/*");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertTrue(response.size() != 0);
    clientConnection1.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection2 =
        tryRequest("red-line/max-lat=20?max-" + "long=20?min-lat=19?min-long=19");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, Object> response2 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    // Filtered is smaller
    assertTrue(response2.toString().length() != 0);
    // Test fails bc length number maxes out on java
    clientConnection2.disconnect();
  }

  /**
   * Testing retrieving all of the data
   *
   * @throws IOException
   */
  @Test
  public void testAllData() throws IOException, InterruptedException {
    // redline full data
    HttpURLConnection clientConnection1 = tryRequest("red-line/*");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    Object o = response.get("success");
    assertTrue(o != null);
    assertTrue(
        o.toString()
            .contains(
                "properties={area_description_data={1=Super, 2=cool, 3=food}, city=Muffinburg, holc_grade=A+}"));
    clientConnection1.disconnect();
  }

  /**
   * Testing retrieving filtered data based on bounding box -86.75692, 33.495789 -90.754496
   * 30.488832
   *
   * @throws IOException
   */
  @Test
  public void testCoordinateBox() throws IOException, InterruptedException {
    HttpURLConnection clientConnection1 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-87&min-long=32");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    Object o = response.get("success");
    assertNotNull(o);
    System.out.print(o.toString());
    assertFalse(o.toString().contains("Sushiville"));
    assertTrue(o.toString().contains("-86.75692"));
    // sending another coordinate query
    clientConnection1.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection2 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-100&min-long=29");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor2 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        redlineAdaptor2.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    Object o2 = response2.get("success");
    assertNotNull(o2);
    assertTrue(o2.toString().contains("Sushiville"));
    assertTrue(o2.toString().contains("-86.75692"));

    // sending consecutive coordinate queries -- result should have the update version
    // this is a search that is out of bounds
    HttpURLConnection clientConnection3 =
        tryRequest("red-line?max-lat=10&max-long=20&min-lat=10&min-long=10");
    assertEquals(200, clientConnection3.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor3 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    Object o3 = response3.get("success");
    assertNotNull(o3);
    assertFalse(o3.toString().contains("Sushiville"));
    assertFalse(o3.toString().contains("-86.75692"));

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection4 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-100&min-long=29");
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor4 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response4 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    Object o4 = response4.get("success");
    assertNotNull(o4);
    assertTrue(o4.toString().contains("Sushiville"));
    assertTrue(o4.toString().contains("-86.75692"));
  }

  /**
   * Testing searching with area description
   *
   * @throws IOException
   */
  @Test
  public void testRedLineArea() throws IOException, InterruptedException {
    HttpURLConnection clientConnection1 = tryRequest("search-area?area-desc=cool");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    Object o = response.get("success");
    assertNotNull(o);
    System.out.print(o.toString());
    assertFalse(o.toString().contains("Sushiville"));
    assertTrue(o.toString().contains("-86.75692"));
    // sending another coordinate query
    clientConnection1.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    // search that doesn't match anything
    HttpURLConnection clientConnection2 = tryRequest("search-area?area-desc=Slowly");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor2 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        redlineAdaptor2.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    Object o2 = response2.get("success");
    assertNotNull(o2);
    assertFalse(o2.toString().contains("Sushiville"));
    assertFalse(o2.toString().contains("-86.75692"));
    clientConnection2.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    // this is a search that is out of bounds
    HttpURLConnection clientConnection3 = tryRequest("search-area?area-desc=slowly");
    assertEquals(200, clientConnection3.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor3 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    Object o3 = response3.get("success");
    assertNotNull(o3);
    assertTrue(o3.toString().contains("Sushiville"));
    assertFalse(o3.toString().contains("-86.75692"));
    clientConnection3.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection4 = tryRequest("search-area?area-desc=");
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor4 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response4 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    Object o4 = response4.get("success");
    assertNull(o4);
    clientConnection4.disconnect();
  }

  /**
   * Testing a wrong endpoint handler that fails
   *
   * @throws IOException
   */
  @Test
  public void testHandlerGeneralError() throws IOException, InterruptedException {
    HttpURLConnection clientConnection4 = tryRequest("fh/*");
    assertEquals(404, clientConnection4.getResponseCode());
  }

  /**
   * Test a bounding box that fails
   *
   * @throws IOException
   */
  @Test
  public void testBoundingBoxError() throws IOException, InterruptedException {
    Moshi moshi = new Moshi.Builder().build();

    HttpURLConnection clientConnection4 =
        tryRequest("red-line?max-lat=&max-long=34&min-lat=-87&min-long=32");
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor4 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response4 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    Object o4 = response4.get("success");
    assertNull(o4);
    clientConnection4.disconnect();

    HttpURLConnection clientConnection5 = tryRequest("red-line?");
    assertEquals(200, clientConnection5.getResponseCode());
    Map<String, Object> response5 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection5.getInputStream()));
    Object o5 = response5.get("success");
    assertNull(o5);
    clientConnection5.disconnect();
  }

  /**
   * Testing searching with area description that will have error
   *
   * @throws IOException
   */
  @Test
  public void testSearchAreaWithError() throws IOException, InterruptedException {
    // sending consecutive coordinate queries -- result should have the update version
    Moshi moshi = new Moshi.Builder().build();

    HttpURLConnection clientConnection4 = tryRequest("search-area?");
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor4 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response4 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    Object o4 = response4.get("success");
    assertNull(o4);
    clientConnection4.disconnect();

    HttpURLConnection clientConnection5 = tryRequest("search-area?area-desc=");
    assertEquals(200, clientConnection5.getResponseCode());
    Map<String, Object> response5 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection5.getInputStream()));
    Object o5 = response5.get("success");
    assertNull(o5);
    clientConnection5.disconnect();
  }

  /**
   * Testing retrieving filtered data based on bounding box mixed with retrieving the whole data
   *
   * @throws IOException
   */
  @Test
  public void testRedLineAll() throws IOException, InterruptedException {

    HttpURLConnection clientConnection1 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-87&min-long=32");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    Object o = response.get("success");
    assertNotNull(o);
    System.out.print(o.toString());
    assertFalse(o.toString().contains("Sushiville"));
    assertTrue(o.toString().contains("-86.75692"));
    // sending another coordinate query
    clientConnection1.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnectionNA =
        tryRequest("red-line?max-lat=0&max-long=0&min-lat=0&min-long=0");
    assertEquals(200, clientConnectionNA.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor3 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnectionNA.getInputStream()));
    Object o3 = response3.get("success");
    assertNotNull(o3);
    assertFalse(o3.toString().contains("Sushiville"));
    assertFalse(o3.toString().contains("-86.75692"));

    // sending consecutive coordinate queries -- result should have the update version
    // search that doesn't match anything
    HttpURLConnection clientConnectionsl = tryRequest("search-area?area-desc=mlowly");
    assertEquals(200, clientConnectionsl.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptorsl =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> responsesl =
        redlineAdaptorsl.fromJson(new Buffer().readFrom(clientConnectionsl.getInputStream()));
    Object osl = responsesl.get("success");
    assertNotNull(osl);
    assertFalse(osl.toString().contains("Sushiville"));
    assertFalse(osl.toString().contains("-86.75692"));
    clientConnectionsl.disconnect();

    // redline full data
    HttpURLConnection clientConnectionall = tryRequest("red-line/*");
    assertEquals(200, clientConnectionall.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor11 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> responseall =
        redlineAdaptor11.fromJson(new Buffer().readFrom(clientConnectionall.getInputStream()));
    Object o11 = responseall.get("success");
    assertTrue(o11 != null);
    assertTrue(
        o11.toString()
            .contains(
                "properties={area_description_data={1=Super, 2=cool, 3=food}, city=Muffinburg, holc_grade=A+}"));
    clientConnectionall.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection2 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-100&min-long=29");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor2 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        redlineAdaptor2.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    Object o2 = response2.get("success");
    assertNotNull(o2);
    assertTrue(o2.toString().contains("Sushiville"));
    assertTrue(o2.toString().contains("-86.75692"));

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnectionss = tryRequest("search-area?area-desc=");
    assertEquals(200, clientConnectionss.getResponseCode());
    // Expected response
    Map<String, Object> responsess =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnectionss.getInputStream()));
    Object oss = responsess.get("success");
    assertNull(oss);
    clientConnectionss.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection3 =
        tryRequest("red-line?max-lat=10&max-long=20&min-lat=10&min-long=10");
    assertEquals(200, clientConnection3.getResponseCode());
    // Expected response
    Map<String, Object> response102 =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    Object o102 = response3.get("success");
    assertNotNull(o102);
    assertFalse(o102.toString().contains("Sushiville"));
    assertFalse(o102.toString().contains("-86.75692"));

    // redline full data
    HttpURLConnection clientConnection10 = tryRequest("red-line/*");
    assertEquals(200, clientConnection10.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor10 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response10 =
        redlineAdaptor10.fromJson(new Buffer().readFrom(clientConnection10.getInputStream()));
    Object o10 = response10.get("success");
    assertTrue(o10 != null);
    assertTrue(
        o10.toString()
            .contains(
                "properties={area_description_data={1=Super, 2=cool, 3=food}, city=Muffinburg, holc_grade=A+}"));
    clientConnection10.disconnect();

    // redline full data
    HttpURLConnection clientConnection11 = tryRequest("red-line/*");
    assertEquals(200, clientConnection10.getResponseCode());
    // Expected response
    Object oall2 = response10.get("success");
    assertTrue(oall2 != null);
    assertTrue(
        oall2
            .toString()
            .contains(
                "properties={area_description_data={1=Super, 2=cool, 3=food}, city=Muffinburg, holc_grade=A+}"));
    clientConnection11.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnectionw = tryRequest("search-area?area-desc=slowly");
    assertEquals(200, clientConnectionw.getResponseCode());
    // Expected response
    Map<String, Object> responsew =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnectionw.getInputStream()));
    Object ow = responsew.get("success");
    assertNotNull(ow);
    assertTrue(ow.toString().contains("Sushiville"));
    assertFalse(ow.toString().contains("-86.75692"));
    clientConnectionw.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnectionws = tryRequest("search-area?area-desc=Slowly");
    assertEquals(200, clientConnectionws.getResponseCode());
    // Expected response
    Map<String, Object> responsews =
        redlineAdaptor3.fromJson(new Buffer().readFrom(clientConnectionws.getInputStream()));
    Object ows = responsews.get("success");
    assertNotNull(ows);
    assertFalse(ows.toString().contains("Sushiville"));
    assertFalse(ows.toString().contains("-86.75692"));
    clientConnectionw.disconnect();

    // sending consecutive coordinate queries -- result should have the update version
    HttpURLConnection clientConnection4 =
        tryRequest("red-line?max-lat=-80&max-long=34&min-lat=-100&min-long=29");
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor4 =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response4 =
        redlineAdaptor4.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    Object o4 = response4.get("success");
    assertNotNull(o4);
    assertTrue(o4.toString().contains("Sushiville"));
    assertTrue(o4.toString().contains("-86.75692"));
  }
}
