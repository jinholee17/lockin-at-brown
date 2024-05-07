package edu.brown.cs.student.backendIntegrationTesting;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.DataSource.ReviewDatasource;
import edu.brown.cs.student.main.server.DataSource.StudySpotDataSource;
import edu.brown.cs.student.main.server.Handlers.AddWordHandler;
import edu.brown.cs.student.main.server.Handlers.ClearUserHandler;
import edu.brown.cs.student.main.server.Handlers.DeleteWordHandler;
import edu.brown.cs.student.main.server.Handlers.ListWordsHandler;
import edu.brown.cs.student.main.server.Handlers.ReviewHandler;
import edu.brown.cs.student.main.server.Handlers.SearchStudyHandler;
import edu.brown.cs.student.main.server.Storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.Storage.StorageInterface;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestIntegratedHandler {

  /** Set up all variables and servers before running integration test */
  static StorageInterface firebaseUtils;

  public TestIntegratedHandler() throws IOException {}

  @BeforeAll
  public static void setup_before_everything() throws IOException {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    firebaseUtils = new FirebaseUtilities();
  }

  StudySpotDataSource studySpotDataSource = new StudySpotDataSource() {};
  ReviewDatasource reviewDatasource = new ReviewDatasource() {};
  JsonAdapter<Map<String, String>> mapAdapter;
  JsonAdapter<Map<String, Object>> mapListAdapter;
  JsonAdapter<List<List<String>>> listAdapter;

  JsonAdapter<String> stringAdapter;

  /** Starting servers before testing */
  @BeforeEach
  public void setup() {
    Moshi moshi = new Moshi.Builder().build();
    this.mapAdapter =
        moshi.adapter(Types.newParameterizedType(Map.class, String.class, String.class));
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    this.mapListAdapter = moshi.adapter(type);
    this.stringAdapter = moshi.adapter(Types.newParameterizedType(List.class, String.class));

    Moshi moshi2 = new Moshi.Builder().build();
    this.listAdapter =
        moshi2.adapter(
            Types.newParameterizedType(
                List.class, Types.newParameterizedType(List.class, String.class)));

    Spark.get("delete-word", new DeleteWordHandler(firebaseUtils));
    Spark.get("list-words", new ListWordsHandler(firebaseUtils));
    Spark.get("add-word", new AddWordHandler(firebaseUtils));
    Spark.get("clear-user", new ClearUserHandler(firebaseUtils));
    Spark.get("search-study", new SearchStudyHandler(studySpotDataSource));
    Spark.get("study-review", new ReviewHandler(reviewDatasource));

    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /** Clean up after calls */
  @AfterEach
  public void tearUp() {
    Spark.unmap("delete-word");
    Spark.awaitStop();
    Spark.unmap("add-word");
    Spark.awaitStop();
    Spark.unmap("list-words");
    Spark.awaitStop();
    Spark.unmap("clear-user");
    Spark.awaitStop();

    Spark.unmap("search-study");
    Spark.awaitStop();

    Spark.unmap("study-review");
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
   * Testing add word server handler functions correctly. Simultaneously tests list of words as we
   * use that to test the adding functionality is correct. Then tests deleting a word to make sure
   * the delete handler works as well.
   *
   * @throws IOException
   */
  @Test
  public void testAddWordThenListWordThenDeleteWord() throws IOException {
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

    HttpURLConnection clientConnection3 = tryRequest("delete-word?uid=id&word=hello");
    assertEquals(200, clientConnection3.getResponseCode());
    // Expected response
    Map<String, String> response3 =
        this.mapAdapter.fromJson(new Buffer().readFrom(clientConnection3.getInputStream()));
    assertEquals("hello", response3.get("word"));
    assertEquals("success", response3.get("response_type"));

    clientConnection1.disconnect();
    clientConnection3.disconnect();
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

    clientConnection1.disconnect();
    clientConnection2.disconnect();
    clientConnection4.disconnect();
  }

  @Test
  public void testSearchStudyHandler() throws IOException {
    // Tests an empty search-study is valid
    HttpURLConnection clientConnection1 = tryRequest("search-study");
    assertEquals(200, clientConnection1.getResponseCode());
    // Expected response
    Map<String, Object> response =
        this.mapListAdapter.fromJson(new Buffer().readFrom(clientConnection1.getInputStream()));
    assertTrue(response.get("Result") != null);

    clientConnection1.disconnect();

    // A search with all parameters is valid
    HttpURLConnection clientConnection2 =
        tryRequest(
            "search-study?volume=Quiet&traffic=Barely%20any%20traffic&capacity=4-8+%20people&lon=41.830254266330954&lat=-71.40281532445142");
    assertEquals(200, clientConnection2.getResponseCode());
    // Expected response
    Map<String, Object> response2 =
        this.mapListAdapter.fromJson(new Buffer().readFrom(clientConnection2.getInputStream()));
    assertTrue(response2.get("Result") != null);

    clientConnection2.disconnect();
  }
}
