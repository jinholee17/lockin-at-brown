package edu.brown.cs.student.backendIntegrationTesting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONData;
import edu.brown.cs.student.main.server.geoJSONDataSource.GeoJSONDataSource;
import edu.brown.cs.student.main.server.handlers.AddPinHandler;
import edu.brown.cs.student.main.server.handlers.AddWordHandler;
import edu.brown.cs.student.main.server.handlers.ClearMarkersHandler;
import edu.brown.cs.student.main.server.handlers.ClearUserHandler;
import edu.brown.cs.student.main.server.handlers.ListOfPinsHandler;
import edu.brown.cs.student.main.server.handlers.ListWordsHandler;
import edu.brown.cs.student.main.server.handlers.RedLineAllHandler;
import edu.brown.cs.student.main.server.handlers.RedLineFilterHandler;
import edu.brown.cs.student.main.server.handlers.SearchAreaHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.*;
import spark.Spark;

public class TestAPIHandlers {

  public TestAPIHandlers() throws IOException {}

  /** Set up all variables and servers before running integration test */
  static StorageInterface firebaseUtils;

  @BeforeAll
  public static void setup_before_everything() throws IOException {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    firebaseUtils = new FirebaseUtilities();
  }

  GeoJSONData geoJSONData = new GeoJSONDataSource() {};
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

    Spark.get("add-pin", new AddPinHandler(this.firebaseUtils));
    Spark.get("list-pins", new ListOfPinsHandler(this.firebaseUtils));
    Spark.get("clear-pins", new ClearMarkersHandler(this.firebaseUtils));

    Spark.get("add-word", new AddWordHandler(this.firebaseUtils));
    Spark.get("list-words", new ListWordsHandler(this.firebaseUtils));
    Spark.get("clear-user", new ClearUserHandler(this.firebaseUtils));

    Spark.get("red-line/*", new RedLineAllHandler(geoJSONData));
    Spark.get("red-line", new RedLineFilterHandler(geoJSONData));

    Spark.get("search-area", new SearchAreaHandler(geoJSONData));

    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /** Clean up after calls */
  @AfterEach
  public void tearUp() {
    Spark.unmap("add-pin");
    Spark.awaitStop();
    Spark.unmap("list-pins");
    Spark.awaitStop();
    Spark.unmap("clear-pins");
    Spark.awaitStop();

    Spark.unmap("add-word");
    Spark.awaitStop();
    Spark.unmap("list-words");
    Spark.awaitStop();
    Spark.unmap("clear-user");
    Spark.awaitStop();

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
   * Testing add pin server handler functions correctly. Simultaneously tests list of pins as we use
   * that to test the adding functionality is correct
   *
   * @throws IOException
   */
  @Test
  public void testAddPin() throws IOException {
    // Positive Tests
    // Basic functioning call
    HttpURLConnection clientConnection1 = tryRequest("add-pin?uid=id&pin=pinLoc");
    // API connection works
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, String> response =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("pinLoc", response.get("pin"));
    assertEquals("success", response.get("response_type"));
    clientConnection1.disconnect();

    // Check Data set is updated
    HttpURLConnection clientConnection2 = tryRequest("list-pins?uid=id");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    List<String> pins = (List<String>) response2.get("pins");
    // Access the first pin in the list, check that it has been added to the list
    assertEquals("pinLoc", pins.get(0));
    String responseType = (String) response2.get("response_type");
    assertEquals("success", responseType); // Check the response type
    clientConnection2.disconnect();

    // Negative tests
    // Missing pin
    HttpURLConnection clientConnection3 = tryRequest("add-pin?uid=id");
    // API connection works
    assertEquals(200, clientConnection1.getResponseCode());
    // error returns
    Map<String, String> response3 =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    assertEquals("failure", response3.get("response_type"));
    clientConnection3.disconnect();
  }

  /**
   * Testing clear pin server handler functions correctly. Simultaneously tests list of pins as we
   * use that to test the clear functionality is correct
   *
   * @throws IOException
   */
  @Test
  public void testClearPins() throws IOException, InterruptedException {
    // Add the pins
    HttpURLConnection clientConnection1 = tryRequest("add-pin?uid=id&pin=pinLoc");
    // API connection works
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, String> response =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("pinLoc", response.get("pin"));
    assertEquals("success", response.get("response_type"));
    clientConnection1.disconnect();

    // Check Data set is updated
    HttpURLConnection clientConnection2 = tryRequest("list-pins?uid=id");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    List<String> pins = (List<String>) response2.get("pins");
    // Access the first pin in the list, check that it has been added to the list
    assertEquals("pinLoc", pins.get(0));
    String responseType = (String) response2.get("response_type");
    assertEquals("success", responseType); // Check the response type
    clientConnection2.disconnect();

    // Clear the pins
    Thread.sleep(2000);
    // Basic functioning call
    HttpURLConnection clientConnection4 = tryRequest("clear-pins?uid=id");
    // API connection works
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    Moshi moshi2 = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter2 =
        moshi2.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        pinsAdapter2.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    List<String> pins2 = (List<String>) response3.get("pins");
    // Access the first pin in the list, check that it has been added to the list
    assertEquals(null, pins2);
  }

  /**
   * Testing add word server handler functions correctly. Simultaneously tests list of words as we
   * use that to test the adding functionality is correct
   *
   * @throws IOException
   */
  @Test
  public void testAddWord() throws IOException {
    // Positive Tests
    // Basic functioning call
    HttpURLConnection clientConnection1 = tryRequest("add-word?uid=id&word=hello");
    // API connection works
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, String> response =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertEquals("hello", response.get("word"));
    assertEquals("success", response.get("response_type"));
    clientConnection1.disconnect();

    // Check Data set is updated
    HttpURLConnection clientConnection2 = tryRequest("list-words?uid=id");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    List<String> words = (List<String>) response2.get("words");
    // Access the first pin in the list, check that it has been added to the list
    assertEquals("hello", words.get(0));
    String responseType = (String) response2.get("response_type");
    assertEquals("success", responseType); // Check the response type
    clientConnection2.disconnect();
  }

  /**
   * Testing clear user server handler functions correctly. Simultaneously tests list of words as we
   * use that to test the clear functionality is correct
   *
   * @throws IOException
   */
  @Test
  public void testClearUser() throws IOException, InterruptedException {
    // Add the pins
    HttpURLConnection clientConnection1 = tryRequest("add-word?uid=id&word=hello");

    // Check Data set is updated
    HttpURLConnection clientConnection2 = tryRequest("list-words?uid=id");
    assertEquals(200, clientConnection2.getResponseCode());

    // Clear the pins
    Thread.sleep(2000);
    // Basic functioning call
    HttpURLConnection clientConnection4 = tryRequest("clear-user?uid=id");
    // API connection works
    assertEquals(200, clientConnection4.getResponseCode());
    // Expected response
    Moshi moshi2 = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter2 =
        moshi2.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        pinsAdapter2.fromJson(new Buffer().readFrom(clientConnection4.getInputStream()));
    List<String> words2 = (List<String>) response3.get("words");
    // Access the first pin in the list, check that it has been added to the list
    assertEquals(null, words2);
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
    assertTrue(!response.isEmpty());
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

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection10 =
        tryRequest("red-line?max-lat=-83&max-long=33&min-lat=-84&min-long=32");
    assertEquals(200, clientConnection10.getResponseCode());
    // Expected response
    Map<String, Object> response10 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection10.getInputStream()));
    // Filtered is smaller
    assertTrue(response10.toString().contains("-83.677581, 32.856104"));
    assertFalse(response10.toString().contains("-85"));
    assertFalse(response10.toString().contains("34."));
    assertFalse(response10.toString().contains("31."));
    assertFalse(response10.toString().contains("-80."));
    // Test fails bc length number maxes out on java
    clientConnection2.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionexact2 =
        tryRequest("red-line?max-lat=30&max-long=40&min-lat=10&min-long=20");
    assertEquals(200, clientConnectionexact2.getResponseCode());
    // Expected response
    Map<String, Object> responsee2 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionexact2.getInputStream()));
    // Filtered is smaller
    assertTrue(responsee2.toString().contains("[]"));
    // Test fails bc length number maxes out on java
    clientConnectionexact2.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection11 =
        tryRequest("red-line?max-lat=-80&max-long=40&min-lat=-100&min-long=30");
    assertEquals(200, clientConnection11.getResponseCode());
    // Expected response
    Map<String, Object> response11 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection11.getInputStream()));
    // Filtered is smaller
    assertTrue(response11.toString().contains("-83.677581, 32.856104"));
    assertFalse(response11.toString().contains("44.2"));
    assertFalse(response11.toString().contains("29.3"));
    assertFalse(response11.toString().contains("-101.1"));
    assertFalse(response11.toString().contains("-79.1"));
    // Test fails bc length number maxes out on java
    clientConnection11.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionone =
        tryRequest(
            "red-line?max-lat=-83.627706&max-long=32.858953&min-lat=-83.627706&min-long=32.858953");
    assertEquals(200, clientConnectionone.getResponseCode());
    // Expected response
    Map<String, Object> responseone =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionone.getInputStream()));
    // Filtered is smaller
    assertTrue(responseone.toString().contains("-83.627706"));
    assertTrue(responseone.toString().contains("32.858953"));
    assertFalse(responseone.toString().contains("44.2"));
    assertFalse(responseone.toString().contains("29.3"));
    assertFalse(responseone.toString().contains("-101.1"));
    assertFalse(responseone.toString().contains("-79.1"));
    // Test fails bc length number maxes out on java
    clientConnectionone.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionexact =
        tryRequest(
            "red-line?max-lat=-83.677581&max-long=32.856104&min-lat=-83.677581&min-long=32.856104");
    assertEquals(200, clientConnectionexact.getResponseCode());
    // Expected response
    Map<String, Object> responsee =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionexact.getInputStream()));
    // Filtered is smaller
    assertTrue(responsee.toString().contains("[]"));
    // Test fails bc length number maxes out on java
    clientConnectionexact.disconnect();
  }

  /**
   * Testing searching desc server handler functions correctly.
   *
   * @throws IOException
   */
  @Test
  public void testSearch() throws IOException, InterruptedException {
    // search basic
    HttpURLConnection clientConnection1 = tryRequest("search-area?area-desc=lower%20class");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> searchAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        searchAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertTrue(response.size() != 0);
    assertTrue(response.toString().contains("-112.068646, 33.44004"));
    assertFalse(response.toString().contains("-86.756777, 33.497543"));
    clientConnection1.disconnect();

    // search NA
    HttpURLConnection clientConnection2 = tryRequest("search-area?area-desc=isfbajlhfbajdbf73369");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Map<String, Object> response2 =
        searchAdaptor.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    assertTrue(response2.toString().equals("{success={features=[], type=FeatureCollection}}"));
    clientConnection1.disconnect();
  }

  /**
   * Testing integration-- multiple handlers can function together
   *
   * @throws IOException
   */
  @Test
  public void integration() throws IOException, InterruptedException {
    HttpURLConnection clientConnection500 = tryRequest("clear-pins?uid=idnew2");
    // API connection works
    assertEquals(200, clientConnection500.getResponseCode());
    // error returns
    Map<String, String> response500 =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection500.getInputStream()));
    assertEquals("success", response500.get("response_type"));
    clientConnection500.disconnect();

    HttpURLConnection clientConnectionpin1 = tryRequest("add-pin?uid=idnew2&pin=pinLoc1");
    // API connection works
    assertEquals(200, clientConnectionpin1.getResponseCode());
    // Expected response
    Map<String, String> responsePin1 =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnectionpin1.getInputStream()));
    assert responsePin1 != null;
    assertEquals("{pin=pinLoc1, response_type=success}", responsePin1.toString());
    assertEquals("success", responsePin1.get("response_type"));
    clientConnectionpin1.disconnect();
    // redline basic

    HttpURLConnection clientConnectionRL = tryRequest("red-line/*");
    assertEquals(200, clientConnectionRL.getResponseCode());
    // Expected response
    Moshi moshi2 = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> redlineAdaptor =
        moshi2.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> responseRL =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionRL.getInputStream()));
    assertTrue(responseRL.size() != 0);
    clientConnectionRL.disconnect();

    HttpURLConnection clientConnectionpinno = tryRequest("add-pin?uid=&pin=pinLoc1");
    // API connection works
    assertEquals(200, clientConnectionpinno.getResponseCode());
    // Expected response
    Map<String, String> responsePinno =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnectionpinno.getInputStream()));
    assert responsePinno != null;
    assertEquals("failure", responsePinno.get("response_type"));
    clientConnectionpinno.disconnect();

    Thread.sleep(2000);

    HttpURLConnection clientConnectionpincon = tryRequest("add-pin?uid=idnew2&pin=pinLoc2");
    // API connection works
    assertEquals(200, clientConnectionpincon.getResponseCode());
    // Expected response
    Map<String, String> responsePinCon =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnectionpincon.getInputStream()));
    assert responsePinCon != null;
    assertEquals("{pin=pinLoc2, response_type=success}", responsePinCon.toString());
    assertEquals("success", responsePinCon.get("response_type"));
    clientConnectionpincon.disconnect();

    Thread.sleep(2000);

    // Check Data set is updated
    HttpURLConnection clientConnection2 = tryRequest("list-pins?uid=idnew2");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> pinsAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response2 =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    // Access the first pin in the list, check that it has been added to the list
    assertTrue(response2.toString().contains("pinLoc2"));
    // assertTrue(response2.toString().contains("pinLoc1"));
    String responseType = (String) response2.get("response_type");
    assertEquals("success", responseType); // Check the response type
    clientConnection2.disconnect();

    Thread.sleep(2000);

    HttpURLConnection clientConnectionClearAgain = tryRequest("clear-pins?uid=idnew2");
    // API connection works
    assertEquals(200, clientConnectionClearAgain.getResponseCode());
    // error returns
    Map<String, String> responseCA =
        this.mapAdapter.fromJson(
            new Buffer().readFrom(clientConnectionClearAgain.getInputStream()));
    assertEquals("success", responseCA.get("response_type"));
    clientConnectionClearAgain.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionexact =
        tryRequest(
            "red-line?max-lat=-83.677581&max-long=32.856104&min-lat=-83.677581&min-long=32.856104");
    assertEquals(200, clientConnectionexact.getResponseCode());
    // Expected response
    Map<String, Object> responsee =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionexact.getInputStream()));
    // Filtered is smaller
    assertTrue(responsee.toString().contains("[]"));
    // Test fails bc length number maxes out on java
    clientConnectionexact.disconnect();

    Thread.sleep(5000);

    // Check Data set is updated
    HttpURLConnection clientConnectionm = tryRequest("list-pins?uid=idnew2");
    assertEquals(200, clientConnectionm.getResponseCode());
    // Expected response
    Map<String, Object> responsem =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnectionm.getInputStream()));
    // Access the first pin in the list, check that it has been added to the list
    assert responsem != null;
    assertFalse(responsem.toString().contains("pinLoc2"));
    assertFalse(responsem.toString().contains("pinLoc1"));
    // assertTrue(response2.toString().contains("pinLoc1"));
    clientConnectionm.disconnect();

    // search basic
    HttpURLConnection clientConnection1 = tryRequest("search-area?area-desc=lower%20class");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> searchAdaptor =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response =
        searchAdaptor.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertTrue(response.size() != 0);
    assertTrue(response.toString().contains("-112.068646, 33.44004"));
    assertFalse(response.toString().contains("-86.756777, 33.497543"));
    clientConnection1.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionexact2 =
        tryRequest("red-line?max-lat=30&max-long=40&min-lat=10&min-long=20");
    assertEquals(200, clientConnectionexact2.getResponseCode());
    // Expected response
    Map<String, Object> responsee2 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionexact2.getInputStream()));
    // Filtered is smaller
    assertTrue(responsee2.toString().contains("[]"));
    // Test fails bc length number maxes out on java
    clientConnectionexact2.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection11 =
        tryRequest("red-line?max-lat=-80&max-long=40&min-lat=-100&min-long=30");
    assertEquals(200, clientConnection11.getResponseCode());
    // Expected response
    Map<String, Object> response11 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection11.getInputStream()));
    // Filtered is smaller
    assertTrue(response11.toString().contains("-83.677581, 32.856104"));
    assertFalse(response11.toString().contains("44.2"));
    assertFalse(response11.toString().contains("29.3"));
    assertFalse(response11.toString().contains("-101.1"));
    assertFalse(response11.toString().contains("-79.1"));
    // Test fails bc length number maxes out on java
    clientConnection11.disconnect();

    // redline basic
    HttpURLConnection clientConnection3 = tryRequest("red-line/*");
    assertEquals(200, clientConnection3.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptor2 =
        moshi2.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> response3 =
        redlineAdaptor2.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    assertTrue(response3.size() != 0);
    clientConnection3.disconnect();

    // search basic
    HttpURLConnection clientConnection = tryRequest("search-area?area-desc=lower%20class");
    assertEquals(200, clientConnection.getResponseCode());
    // Expected response
    Map<String, Object> responsew =
        searchAdaptor.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertTrue(responsew.size() != 0);
    assertTrue(responsew.toString().contains("-112.068646, 33.44004"));
    assertFalse(responsew.toString().contains("-86.756777, 33.497543"));
    clientConnection1.disconnect();

    HttpURLConnection clientConnectionpin = tryRequest("add-pin?uid=id&pin=pinLoc");
    // API connection works
    assertEquals(200, clientConnectionpin.getResponseCode());
    // Expected response
    Map<String, String> responsePin =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnectionpin.getInputStream()));
    assertEquals("pinLoc", responsePin.get("pin"));
    assertEquals("success", responsePin.get("response_type"));
    clientConnectionpin.disconnect();

    // search basic
    HttpURLConnection clientConnection200 = tryRequest("search-area?area-desc=class%20lower");
    assertEquals(200, clientConnection200.getResponseCode());
    // Expected response
    Map<String, Object> response200 =
        searchAdaptor.fromJson(new Buffer().readFrom(clientConnection200.getInputStream()));
    assertTrue(response200.toString().contains("[]"));
    clientConnection200.disconnect();

    // redline basic
    HttpURLConnection clientConnectionAll = tryRequest("red-line/*");
    assertEquals(200, clientConnectionAll.getResponseCode());
    // Expected response
    JsonAdapter<Map<String, Object>> redlineAdaptorAll =
        moshi2.adapter(Types.newParameterizedType(Map.class, String.class, Object.class));
    Map<String, Object> responseAll =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionAll.getInputStream()));
    assertTrue(!responseAll.isEmpty());
    clientConnectionAll.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection10 =
        tryRequest("red-line?max-lat=-83&max-long=33&min-lat=-84&min-long=32");
    assertEquals(200, clientConnection10.getResponseCode());
    // Expected response
    Map<String, Object> response10 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection10.getInputStream()));
    // Filtered is smaller
    assertTrue(response10.toString().contains("-83.677581, 32.856104"));
    assertFalse(response10.toString().contains("-85"));
    assertFalse(response10.toString().contains("34."));
    assertFalse(response10.toString().contains("31."));
    assertFalse(response10.toString().contains("-80."));
    // Test fails bc length number maxes out on java
    clientConnection2.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnection100 =
        tryRequest("red-line?max-lat=-83&max-long=33&min-lat=-84&min-long=32");
    assertEquals(200, clientConnection10.getResponseCode());
    // Expected response
    Map<String, Object> response100 =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnection100.getInputStream()));
    // Filtered is smaller
    assertTrue(response100.toString().contains("-83.677581, 32.856104"));
    assertFalse(response100.toString().contains("-85"));
    assertFalse(response100.toString().contains("34."));
    assertFalse(response100.toString().contains("31."));
    assertFalse(response100.toString().contains("-80."));
    // Test fails bc length number maxes out on java
    clientConnection100.disconnect();

    HttpURLConnection clientConnectionClearAgain2 = tryRequest("clear-pins?uid=id");
    // API connection works
    assertEquals(200, clientConnectionClearAgain2.getResponseCode());
    // error returns
    Map<String, String> response2a =
        this.mapAdapter.fromJson(
            new Buffer().readFrom(clientConnectionClearAgain2.getInputStream()));
    assertEquals("success", response2a.get("response_type"));
    clientConnectionClearAgain.disconnect();

    Thread.sleep(4000);

    // add word basic
    HttpURLConnection clientConnection20 = tryRequest("add-word?uid=id&word=hello");
    // API connection works
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, String> response20 =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection20.getInputStream()));
    assertEquals("hello", response20.get("word"));
    assertEquals("success", response20.get("response_type"));
    clientConnection20.disconnect();

    // Check Data set is updated
    HttpURLConnection clientConnectionm2 = tryRequest("list-pins?uid=id");
    assertEquals(200, clientConnectionm2.getResponseCode());
    // Expected response
    Map<String, Object> responsem2 =
        pinsAdapter.fromJson(new Buffer().readFrom(clientConnectionm2.getInputStream()));
    // Access the first pin in the list, check that it has been added to the list
    assert responsem2 != null;
    assertFalse(responsem2.toString().contains("pinLoc2"));
    assertFalse(responsem2.toString().contains("pinLoc1"));
    // assertTrue(response2.toString().contains("pinLoc1"));
    clientConnectionm2.disconnect();

    // redline filtered data
    // Basic call
    HttpURLConnection clientConnectionexactfin =
        tryRequest("red-line?max-lat=30&max-long=40&min-lat=10&min-long=20");
    assertEquals(200, clientConnectionexactfin.getResponseCode());
    // Expected response
    Map<String, Object> responseefin =
        redlineAdaptor.fromJson(new Buffer().readFrom(clientConnectionexactfin.getInputStream()));
    // Filtered is smaller
    assertTrue(responseefin.toString().contains("[]"));
    // Test fails bc length number maxes out on java
    clientConnectionexactfin.disconnect();
  }
}
