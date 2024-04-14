package edu.brown.cs.student.main.server;

import static spark.Spark.after;

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
import spark.Filter;
import spark.Spark;

/** Top Level class for our project, utilizes spark to create and maintain our server. */
public class Server {

  public static void setUpServer() {
    int port = 3232;
    Spark.port(port);

    after(
        (Filter)
            (request, response) -> {
              response.header("Access-Control-Allow-Origin", "*");
              response.header("Access-Control-Allow-Methods", "*");
            });

    StorageInterface firebaseUtils;
    GeoJSONData geoJsonDataSource;

    try {
      firebaseUtils = new FirebaseUtilities();
      geoJsonDataSource = new GeoJSONDataSource();
      // various end points
      Spark.get("add-word", new AddWordHandler(firebaseUtils));
      Spark.get("list-words", new ListWordsHandler(firebaseUtils));
      Spark.get("clear-user", new ClearUserHandler(firebaseUtils));
      Spark.get("add-pin", new AddPinHandler(firebaseUtils));
      Spark.get("list-pins", new ListOfPinsHandler(firebaseUtils));
      Spark.get("clear-pins", new ClearMarkersHandler(firebaseUtils));
      Spark.get("red-line", new RedLineFilterHandler(geoJsonDataSource));
      Spark.get("red-line/*", new RedLineAllHandler(geoJsonDataSource));
      Spark.get("search-area", new SearchAreaHandler(geoJsonDataSource));

      Spark.notFound(
          (request, response) -> {
            response.status(404); // Not Found
            System.out.println("ERROR");
            return "404 Not Found - The requested endpoint does not exist.";
          });
      Spark.init();
      Spark.awaitInitialization();

      System.out.println("Server started at http://localhost:" + port);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(
          "Error: Could not initialize Firebase. Likely due to firebase_config.json not being found. Exiting.");
      System.exit(1);
    }
  }

  /**
   * Runs Server.
   *
   * @param args none
   */
  public static void main(String[] args) {
    setUpServer();
  }
}
