package edu.brown.cs.student.backendIntegrationTesting;

import com.squareup.moshi.JsonAdapter;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import spark.Spark;

public class TestSearchStudyHandler {

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

}
